package com.demo;

import androidx.appcompat.widget.AppCompatTextView;

import com.kegy.mvps.annotation.Inject;
import com.kegy.mvps.presenter.Presenter;

import butterknife.BindView;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

public class CalculatePresenter extends Presenter {

  @BindView(R.id.textview) AppCompatTextView mTextView;

  @Inject BehaviorSubject<Boolean> mSubject;

  @Inject boolean mIncrease;

  @Override
  protected void onBind() {
    super.onBind();
    addToAutoDisposable(mSubject.subscribe(new Consumer<Boolean>() {
      @Override
      public void accept(Boolean aBoolean) throws Exception {
        int value = Integer.parseInt(mTextView.getText().toString());
        mTextView.setText("" +   (value + (mIncrease ? 1 : -1)));
      }
    }));
  }
}
