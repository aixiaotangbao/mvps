package com.kegy.mvps.commons.injector;

import java.util.Set;

public interface Injector<T> {

  void inject(T target, Object wrapper);

  Set<String> getAllNames();

  Set<Class> getAllTypes();

  void reset(T target);

}
