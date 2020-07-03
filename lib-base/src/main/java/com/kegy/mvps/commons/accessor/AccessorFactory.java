package com.kegy.mvps.commons.accessor;

public abstract class AccessorFactory<T> {

  public abstract void addToWrapper(AccessorWrapper wrapper, T target);

  public AccessorWrapper wrapper(T access) {
    AccessorWrapper wrapper = new AccessorWrapper();
    addToWrapper(wrapper, access);
    return wrapper;
  }

}
