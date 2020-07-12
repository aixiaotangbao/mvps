package com.kegy.mvps.presenter;

import java.util.List;

import androidx.annotation.NonNull;

import com.google.common.collect.ImmutableList;
import com.kegy.mvps.commons.accessor.AccessorFactory;
import com.kegy.mvps.commons.accessor.AccessorWrapper;
import com.kegy.mvps.commons.accessor.Accessors;
import com.kegy.mvps.commons.injector.ComposedInjector;
import com.kegy.mvps.commons.injector.Injector;
import com.kegy.mvps.commons.injector.Injectors;
import com.kegy.mvps.receiver.InjectorReceiver;

/**
 * @author keguoyu
 * @version 1.0.2
 */
@SuppressWarnings("unchecked")
public final class InjectExtension implements InjectorReceiver {
  private final Object mTarget;

  private AccessorWrapper mInitialWrapper = new AccessorWrapper();
  private final Injector<Object> mInjector;

  private boolean mHasInjected;

  public InjectExtension(Object target) {
    mTarget = target;
    ImmutableList.Builder<Injector<Object>> builder = ImmutableList.builder();

    List<Injector<Object>> list = Injectors.injectorList(target);

    for (Object item: list) {
      builder.add((Injector<Object>)item);
    }

    mInjector = new ComposedInjector<Object>(builder.build());
  }

  AccessorWrapper buildAccessWrapper(Object... src) {
    AccessorWrapper accessorWrapper;
    if (src != null && src.length == 1 && src[0] instanceof AccessorWrapper) {
      if (mInitialWrapper.isEmpty()) {
        accessorWrapper = (AccessorWrapper) src[0];
      } else {
        accessorWrapper = new AccessorWrapper();
        accessorWrapper.copy(mInitialWrapper);
        accessorWrapper.copy((AccessorWrapper) src[0]);
      }
    } else {
      accessorWrapper = new AccessorWrapper();
      accessorWrapper.copy(mInitialWrapper);
      if (src != null) {
        for (Object context : src) {
          Accessors.instance().factory(context).addToWrapper(accessorWrapper,
              context);
        }
      }
    }
    return accessorWrapper;
  }

  void inject(AccessorWrapper accessorWrapper) {
    mInjector.inject(mTarget, accessorWrapper);
    mHasInjected = true;
  }

  @Override
  public void addToProvider(@NonNull Object src) {
    AccessorFactory<Object> factory = Accessors.instance().factory(src);
    if (factory != null) {
      factory.addToWrapper(mInitialWrapper, src);
    }
  }

  @Override
  public void inject(Object... objects) {
    AccessorWrapper accessorWrapper = buildAccessWrapper(objects);
    inject(accessorWrapper);
  }

  @Override
  public void reset() {
    mInjector.reset(mTarget);
    mHasInjected = false;
  }

  @Override
  public boolean available() {
    return !mHasInjected;
  }

}
