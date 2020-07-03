package com.kegy.mvps.commons;

import java.util.Set;

import com.kegy.mvps.commons.accessor.AccessorWrapper;

public abstract class ObjectProvider {

  public abstract <T> T fetch(AccessorWrapper obj, String fieldName);

  public abstract <T> T fetch(AccessorWrapper obj, Class<T> tClass);

  public abstract <T> void set(AccessorWrapper obj, String fieldName, T value);

  public abstract <T> void set(AccessorWrapper obj, Class tClass, T value);

  public abstract Set<String> allFieldNames(AccessorWrapper obj);

  public abstract Set<Class> allTypes(AccessorWrapper obj);

  public final boolean have(AccessorWrapper obj, String fieldName) {
    Set<String> names = allFieldNames(obj);
    return names != null && names.contains(fieldName);
  }

  public final boolean have(AccessorWrapper obj, Class tClass) {
    Set<Class> types = allTypes(obj);
    return types != null && types.contains(tClass);
  }

  public abstract Set<Object> allDirectFields(AccessorWrapper obj);

}
