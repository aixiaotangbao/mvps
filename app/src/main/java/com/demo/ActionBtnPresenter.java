package com.demo;

import android.view.View;
import androidx.appcompat.widget.AppCompatButton;

import com.kegy.mvps.annotation.Inject;
import com.kegy.mvps.presenter.Presenter;

import butterknife.BindView;
import io.reactivex.subjects.BehaviorSubject;

public class ActionBtnPresenter extends Presenter {

  @BindView(R.id.actionbtn) AppCompatButton mButton;

  @Inject BehaviorSubject<Boolean> mSubject;

  @Override
  protected void onBind() {
    super.onBind();
    mButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mSubject.onNext(true);
      }
    });
  }
}
