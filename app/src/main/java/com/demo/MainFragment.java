package com.demo;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.kegy.mvps.annotation.Provider;
import com.kegy.mvps.presenter.Presenter;
import com.kegy.mvps.presenter.PresenterFragment;

import io.reactivex.subjects.BehaviorSubject;

public class MainFragment extends PresenterFragment {

  private static final String EXTRA_INCREASE = "ext_increase";

  @Provider boolean mIncrease;

  @Provider BehaviorSubject<Boolean> mSubject = BehaviorSubject.create();

  public static MainFragment newInstance(boolean increase) {
    MainFragment fragment = new MainFragment();
    Bundle bundle = new Bundle();
    bundle.putBoolean(EXTRA_INCREASE, increase);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mIncrease = getArguments().getBoolean(EXTRA_INCREASE);
  }

  @Override
  protected void addToPresenter(Presenter presenter) {
    presenter.add(new ActionBtnPresenter());
    presenter.add(new CalculatePresenter());
  }

  @Override
  protected int getLayoutId() {
    return R.layout.main_fragment;
  }
}
