package com.kegy.mvps.presenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kegy.mvps.commons.RootNamedParam;
import com.kegy.mvps.utils.InternalAccessIds;

/**
 * 该类封装了通用的Presenter和Fragment搭配使用的能力，
 * 实现addToPresenter()方法，加入你自己的业务Presenter。
 *
 * @author keguoyu
 * @version 1.0.2
 */
public abstract class PresenterFragment extends Fragment {

  private Presenter mPresenter;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(getLayoutId(), container, false);
    mPresenter = new Presenter();
    addToPresenter(mPresenter);
    mPresenter.create(view);
    mPresenter.bind(new RootNamedParam(InternalAccessIds.ACCESS_FRAGMENT_ID, this), this);
    return view;
  }

  /**
   * add your own presenter here
   */
  protected abstract void addToPresenter(Presenter presenter);

  protected abstract int getLayoutId();

  @Override
  public void onDestroy() {
    super.onDestroy();
    mPresenter.unBind();
    mPresenter.destroy();
  }
}
