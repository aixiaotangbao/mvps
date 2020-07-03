package com.kegy.mvps.commons;

import java.util.Set;

import com.kegy.mvps.commons.accessor.AccessorWrapper;

public class ObjectProviderImpl extends ObjectProvider {

  @Override
  public <T> T fetch(AccessorWrapper obj, String fieldName) {
    return obj.fetch(fieldName);
  }

  @Override
  public <T> T fetch(AccessorWrapper obj, Class<T> tClass) {
    return obj.fetch(tClass);
  }

  @Override
  public <T> void set(AccessorWrapper obj, String fieldName, T value) {
    obj.set(fieldName, value);
  }

  @Override
  public <T> void set(AccessorWrapper obj, Class tClass, T value) {
    obj.set(tClass, value);
  }

  @Override
  public Set<String> allFieldNames(AccessorWrapper obj) {
    return obj.allNames();
  }

  @Override
  public Set<Class> allTypes(AccessorWrapper obj) {
    return obj.allTypes();
  }

  @Override
  public Set<Object> allDirectFields(AccessorWrapper obj) {
    return obj.allValues();
  }
}
