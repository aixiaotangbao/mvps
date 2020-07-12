package com.kegy.mvps.presenter;

import static com.kegy.mvps.presenter.Presenter.PresenterState.BIND;
import static com.kegy.mvps.presenter.Presenter.PresenterState.CREATE;
import static com.kegy.mvps.presenter.Presenter.PresenterState.DESTROY;
import static com.kegy.mvps.presenter.Presenter.PresenterState.UNBIND;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import com.google.common.base.Preconditions;
import com.kegy.mvps.commons.accessor.AccessorWrapper;
import com.kegy.mvps.receiver.InjectorReceiver;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Presenter基类，一个Presenter用来表示单个的子业务。通过
 * 组合这些Presenter来完成复杂的业务。Presenter分为Root
 * Presenter和子Presenter，Root Presenter被外部调用从而
 * 触发子Presenter的生命周期
 *
 * 一个Presenter共有四个生命周期，分别是create，bind，
 * unbind，destroy。
 *
 * create：负责视图的绑定操作，绑定完成将回掉onCreate()
 *
 * bind：数据绑定操作，绑定完成将回调onBind()，bind是可
 * 重入的方法
 *
 * unbind：解绑定操作，会将数据reset，但是可以再次bind
 * 进入绑定状态
 *
 * destroy：销毁状态，视图解除绑定。
 *
 * 回掉onCreate()的时候所有的view会初始化完成，但是这时候的
 * 数据还没有进行绑定，如果在这时候使用@Inject注入的变量，那
 * 么将会出现数据为空的情况。所以我们通常一般在onBind()中进
 * 性具体的逻辑处理。
 *
 * Presenter表示一个具体的子逻辑，但是通常来说可能各个子逻辑
 * 之间可能会有某些数据的通信，所以这时候需要用到RxJava，我们
 * 不能直接通过一个Presenter引用另一个Presenter的方式来进行数
 * 据的通信。所以我们通常会在一个Presenter中subscribe，那么这
 * 时候我们需要使用这样的代码来避免内存泄漏的产生。
 *
 * <code>
 *   addToAutoDisposable(mSubject.subscribe(new Consumer<Boolean>() {
 *
 *       @Override
 *       public void accept(Boolean aBoolean) throws Exception {
 *          //your our action
 *       }
 *     }));
 *
 * </code>
 *
 * 通过addToAutoDisposable()添加的订阅，Root Presenter会在onUnbind()
 * 的时候统一进行解除订阅，避免内存泄漏的发生。
 *
 * @author keguoyu
 * @version 1.0.2
 */
public class Presenter implements ViewBinder {
  private final List<Presenter> mPresenters = new ArrayList<>();
  private final PresenterStateMachine mPresenterStateMachine = new PresenterStateMachine();
  private final InjectExtension mInjectExtension = new InjectExtension(this);
  private final List<InjectorReceiver> mInjectorReceivers = new ArrayList<>();

  private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

  private PresenterContext mPresenterContext = new PresenterContext();
  private PresenterState mState = PresenterState.INIT;

  private Unbinder mUnbinder;

  private boolean mIsRoot = true;

  public Presenter() {
    addToProvider(this);
  }

  @Override
  public void doBindView(View view) {
   mUnbinder = ButterKnife.bind(this, view);
  }

  @UiThread
  public final void create(Activity activity) {
    create(activity.getWindow().getDecorView());
  }

  @UiThread
  public final void create(View rootView) {
    updateView(rootView);
    moveToState(mState, CREATE);
  }

  private void updateView(View view) {
    mPresenterContext.mView = view;
  }

  private void updateData(Object...objects) {
    mPresenterContext.mObjects = objects;
  }

  private void createInternal() {
    checkDuplicateCreate();
    doBindView(mPresenterContext.mView);
    for (Presenter presenter : mPresenters) {
      createChildren(presenter);
    }
    mState = CREATE;
  }

  private void createChildren(Presenter presenter) {
    presenter.updateView(mPresenterContext.mView);
    presenter.createInternal();
  }

  protected void onCreate() {
  }

  public final void bind(Object...objects) {
    updateData(objects);
    moveToState(mState, PresenterState.BIND);
  }

  private void bindInternal() {
    throwIfNotCreate();
    AccessorWrapper accessorWrapper =
        mInjectExtension.buildAccessWrapper(mPresenterContext.mObjects);
    mInjectExtension.inject(accessorWrapper);

    injectReceivers(accessorWrapper);
    bindChildren(accessorWrapper);

    mState = BIND;
  }

  private void bindChildren(AccessorWrapper accessorWrapper) {
    Object[] objects = new Object[]{accessorWrapper};
    for (Presenter presenter : mPresenters) {
      presenter.updateData(objects);
      presenter.bindInternal();
    }
  }

  protected void onBind() {
  }

  public final void unBind() {
    moveToState(mState, UNBIND);
  }

  private void unBindInternal() {
    mInjectExtension.reset();
    for (Presenter presenter : mPresenters) {
      presenter.unBindInternal();
    }
    mState = UNBIND;
  }

  protected void onUnBind() {
  }

  public final void destroy() {
    moveToState(mState, DESTROY);
  }

  private void destroyInternal() {
    mCompositeDisposable.clear();
    if (mUnbinder != null) {
      mUnbinder.unbind();
    }
    for (Presenter presenter : mPresenters) {
      presenter.destroyInternal();
    }
    mState = PresenterState.DESTROY;
  }

  protected void onDestroy() {
  }

  @Nullable
  public final Activity getActivity() {
    Context context = getContext();
    while (context instanceof ContextWrapper) {
      if (context instanceof Activity) {
        return (Activity) context;
      }
      context = ((ContextWrapper) context).getBaseContext();
    }
    return null;
  }

  @Nullable
  public final Context getContext() {
    return mPresenterContext.mView  == null ?  null : mPresenterContext.mView.getContext();
  }

  public final void addToAutoDisposable(Disposable disposable) {
    mCompositeDisposable.add(disposable);
  }

  public void add(Presenter presenter) {
    Preconditions.checkNotNull(presenter, "presenter不能为null");
    onNewChildPresenter(presenter);
    if (isCreate() && !presenter.isCreate()) {
      matchCreateState(presenter);
    }
  }

  private void injectReceivers(AccessorWrapper accessorWrapper) {
    for (InjectorReceiver injectorReceiver : mInjectorReceivers) {
      injectorReceiver.inject(accessorWrapper);
    }
  }

  public void addInjectReceiver(InjectorReceiver injectorReceiver) {
    mInjectorReceivers.add(injectorReceiver);
  }

  private void onNewChildPresenter(Presenter presenter) {
    mPresenters.add(presenter);
    presenter.mIsRoot = false;
  }

  private void addToProvider(Presenter presenter) {
    mInjectExtension.addToProvider(presenter);
  }

  private void matchCreateState(Presenter presenter) {
    presenter.create(mPresenterContext.mView);
    presenter.onCreate();
  }

  private void moveToState(PresenterState fromState, PresenterState toState) {
    mPresenterStateMachine.moveToState(fromState, toState,
        state -> {
          PresenterAction action = fromStateToAction(state);
          callEntryAction(action);
          if (mIsRoot) {
            callOnState(action);
          }
        });
  }

  private void callEntryAction(PresenterAction action) {
    if (action != null) {
      action.performActionInternal(this);
    }
  }

  private void callOnState(PresenterAction action) {
    action.performCallActionState(this);
    for (Presenter presenter : mPresenters) {
      action.performCallActionState(presenter);
    }
  }

  private void checkDuplicateCreate() {
    if (isCreate()) {
      throw new IllegalStateException("仅能被Create一次");
    }
  }

  private void throwIfNotCreate() {
    if (!isCreate()) {
      throw new IllegalStateException("还没有被初始化");
    }
  }

  private boolean isCreate() {
    return mState.index() >= CREATE.index();
  }


  private Presenter.PresenterAction fromStateToAction(PresenterState state) {
    switch (state) {
      case CREATE:
        return PresenterActionImpl.ACTION_CREATE;
      case BIND:
        return PresenterActionImpl.ACTION_BIND;
      case UNBIND:
        return PresenterActionImpl.ACTION_UNBIND;
      case DESTROY:
        return PresenterActionImpl.ACTION_DESTROY;
      default:
        return PresenterActionImpl.ACTION_NONE;
    }
  }

  public enum PresenterActionImpl implements Presenter.PresenterAction {
    ACTION_NONE {
      @Override
      public void performActionInternal(Presenter presenter) {
      }

      @Override
      public void performCallActionState(Presenter presenter) {
      }
    },
    ACTION_CREATE {
      @Override
      public void performActionInternal(Presenter presenter) {
        presenter.createInternal();
      }

      @Override
      public void performCallActionState(Presenter presenter) {
        presenter.onCreate();
      }
    },
    ACTION_BIND {
      @Override
      public void performActionInternal(Presenter presenter) {
        presenter.bindInternal();
      }

      @Override
      public void performCallActionState(Presenter presenter) {
        presenter.onBind();
      }
    },
    ACTION_UNBIND {
      @Override
      public void performActionInternal(Presenter presenter) {
        presenter.unBindInternal();
      }

      @Override
      public void performCallActionState(Presenter presenter) {
        presenter.onUnBind();
      }
    },
    ACTION_DESTROY {
      @Override
      public void performActionInternal(Presenter presenter) {
        presenter.destroyInternal();
      }

      @Override
      public void performCallActionState(Presenter presenter) {
        presenter.onDestroy();
      }
    }
  }


  interface PresenterAction {
    void performActionInternal(Presenter presenter);

    void performCallActionState(Presenter presenter);
  }

  enum PresenterState {
    NONE(-1),
    INIT(0),
    CREATE(1),
    BIND(2),
    UNBIND(3),
    DESTROY(4);

    private int mIndex;

    PresenterState(int index) {
      this.mIndex = index;
    }

    public int index() {
      return mIndex;
    }

    interface PresenterStateCallBack {
      void onState(PresenterState state);
    }

  }

  static class PresenterContext {
    View mView;
    Object[] mObjects;
  }

}
