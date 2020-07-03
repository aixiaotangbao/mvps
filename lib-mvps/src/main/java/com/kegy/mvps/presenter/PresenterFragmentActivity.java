package com.kegy.mvps.presenter;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.kegy.mvps.commons.RootNamedParam;
import com.kegy.mvps.utils.InternalAccessIds;

/**
 * 不应该使用该类 应该使用PresenterFragment
 */
public abstract class PresenterFragmentActivity extends FragmentActivity {

  private Presenter mPresenter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutId());
    mPresenter = new Presenter();
    addToPresenter(mPresenter);
    mPresenter.create(this);
  }

  @Override
  protected void onStart() {
    super.onStart();
    mPresenter.bind(new RootNamedParam(InternalAccessIds.ACCESS_ACTIVITY_ID, this), this);
  }

  /**
   * add your own presenter here
   */
  protected abstract void addToPresenter(Presenter presenter);

  protected abstract int getLayoutId();

  @Override
  protected void onStop() {
    super.onStop();
    mPresenter.unBind();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mPresenter.destroy();
  }
}
