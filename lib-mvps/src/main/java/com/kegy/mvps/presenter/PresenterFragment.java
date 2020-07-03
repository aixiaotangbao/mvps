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
    return view;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mPresenter.bind(new RootNamedParam(InternalAccessIds.ACCESS_FRAGMENT_ID, this), this);
  }

  /**
   * add your own presenter here
   */
  protected abstract void addToPresenter(Presenter presenter);

  protected abstract int getLayoutId();

  @Override
  public void onStop() {
    super.onStop();
    mPresenter.unBind();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mPresenter.destroy();
  }
}
