package com.kegy.mvps.receiver;

import androidx.annotation.NonNull;

import com.kegy.mvps.presenter.InjectExtension;


/**
 * InjectReceiver默认实现，通过只需要从该类进行派生即可
 *
 * @author keguoyu
 * @since 1.0
 */
public class BaseInjectReceiver implements InjectorReceiver {

  private final InjectExtension mInjectExtension = new InjectExtension(this);

  @Override
  public void addToProvider(@NonNull Object src) {
    mInjectExtension.addToProvider(src);
  }

  @Override
  public void inject(Object... objects) {
    mInjectExtension.inject(objects);
  }

  @Override
  public void reset() {
    mInjectExtension.reset();
  }

  @Override
  public boolean available() {
    return mInjectExtension.available();
  }
}
