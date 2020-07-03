package com.kegy.mvps.commons.injector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComposedInjector<T> implements Injector<T> {

  private final List<Injector<T>> mInjectors = new ArrayList<>();

  public ComposedInjector(List<Injector<T>> injectors) {
    mInjectors.addAll(injectors);
  }

  @Override
  public void inject(T target, Object wrapper) {
    for (Injector<T> injector : mInjectors) {
      injector.inject(target, wrapper);
    }
  }

  @Override
  public Set<String> getAllNames() {
    Set<String> allNames = new HashSet<>();
    for (Injector<T> injector : mInjectors) {
      allNames.addAll(injector.getAllNames());
    }
    return allNames;
  }

  @Override
  public Set<Class> getAllTypes() {
    Set<Class> allTypes = new HashSet<>();
    for (Injector<T> injector : mInjectors) {
      allTypes.addAll(injector.getAllTypes());
    }
    return allTypes;
  }

  @Override
  public void reset(T target) {
    for (Injector<T> injector : mInjectors) {
      injector.reset(target);
    }
  }
}
