package com.kegy.mvps.commons.accessor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.kegy.mvps.commons.BoxUtils;

public class AccessorWrapper {

  private final Map<String, Accessor<?>> mIdAccessors = new HashMap<>();
  private final Map<Class, Accessor<?>> mTypeAccessors = new HashMap<>();

  public void copy(AccessorWrapper accessorWrapper) {
    checkForDuplicate(accessorWrapper);
    mIdAccessors.putAll(accessorWrapper.mIdAccessors);
    mTypeAccessors.putAll(accessorWrapper.mTypeAccessors);
  }

  public void put(String id, Accessor accessor) {
    checkForDuplicate(id, accessor);
    mIdAccessors.put(id, accessor);
  }

  public void put(Class cls, Accessor accessor) {
    checkForDuplicate(cls, accessor);
    mTypeAccessors.put(cls, accessor);
  }

  public boolean have(String id) {
    return mIdAccessors.containsKey(id);
  }

  public boolean have(Class cls) {
    return mTypeAccessors.containsKey(cls);
  }

  public <T> void set(String id, T obj) {
    Accessor<T> accessor = (Accessor<T>) mIdAccessors.get(id);
    if (accessor != null) {
      accessor.set(obj);
    }
  }

  public <T> void set(Class cls, T obj) {
    Accessor<T> accessor = (Accessor<T>) mTypeAccessors.get(cls);
    if (accessor != null) {
      accessor.set(obj);
    }
  }

  public <T> T fetch(String id) {
    Accessor<T> accessor = (Accessor<T>) mIdAccessors.get(id);
    return accessor == null ? null : (T) BoxUtils.getBoxValue(accessor.get());
  }

  public <T> T fetch(Class cls) {
    Accessor<T> accessor = (Accessor<T>) mTypeAccessors.get(cls);
    return accessor == null ? null : (T) BoxUtils.getBoxValue(accessor.get());
  }

  public Set<String> allNames() {
    return mIdAccessors.keySet();
  }

  public Set<Class> allTypes() {
    return mTypeAccessors.keySet();
  }

  public void clear() {
    mIdAccessors.clear();
    mTypeAccessors.clear();
  }

  public Set<Object> allValues() {
    Set<Object> allFields = new HashSet<>();
    if (!mIdAccessors.isEmpty()) {
      for (Accessor accessor : mIdAccessors.values()) {
        if (accessor.get() != null) {
          allFields.add(accessor.get());
        }
      }
    }
    if (!mTypeAccessors.isEmpty()) {
      for (Accessor accessor : mTypeAccessors.values()) {
        if (accessor.get() != null) {
          allFields.add(accessor.get());
        }
      }
    }
    return allFields;
  }

  private void checkForDuplicate(AccessorWrapper src) {
    Set<String> currentKeys = new HashSet<>(mIdAccessors.keySet());
    currentKeys.retainAll(src.mIdAccessors.keySet());
    if (!currentKeys.isEmpty()) {
      for (String key : currentKeys) {
        checkForDuplicate(key, src.mIdAccessors.get(key));
      }
    }
    Set<Class> currentClasses = new HashSet<>(mTypeAccessors.keySet());
    currentClasses.retainAll(src.mTypeAccessors.keySet());
    if (!currentClasses.isEmpty()) {
      for (Class cls : currentClasses) {
        checkForDuplicate(cls, src.mTypeAccessors.get(cls));
      }
    }
  }

  private void checkForDuplicate(String id, Accessor compare) {
    if (mIdAccessors.containsKey(id) && isSameAccessor(mIdAccessors.get(id), compare)) {
      throw new IllegalStateException(String.format("key %s 重复", id));
    }
  }

  private void checkForDuplicate(Class cls, Accessor compare) {
    if (mTypeAccessors.containsKey(cls) && isSameAccessor(mTypeAccessors.get(cls), compare)) {
      throw new IllegalStateException(String.format("class %s 重复", cls.getSimpleName()));
    }
  }

  private boolean isSameAccessor(Accessor accessor1, Accessor accessor2) {
    return accessor1.getClass() == accessor2.getClass() && accessor1.get() == accessor2.get();
  }

  public boolean isEmpty() {
    return mIdAccessors.size() == 0 && mTypeAccessors.size() == 0;
  }

}
