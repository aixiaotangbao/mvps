package com.kegy.mvps.commons.accessor;

import java.util.Map;

import com.kegy.mvps.commons.RootNamedParam;


public enum Accessors {

  INSTANCE;

  public static Accessors instance() {
    return INSTANCE;
  }

  private static final AccessorFactory<Object> DEFAULT = new AccessorFactory<Object>() {
    @Override
    public void addToWrapper(AccessorWrapper wrapper, Object target) {
      wrapper.put(target.getClass(), new SelfAccessor(target));
    }
  };

  private static final AccessorFactory<RootNamedParam> NAMED_PARAM = new AccessorFactory<RootNamedParam>() {
    @Override
    public void addToWrapper(AccessorWrapper wrapper, RootNamedParam accessible) {
      wrapper.put(accessible.getKey(), new Accessor() {

        @Override
        public void set(Object o) {
          accessible.setObject(o);
        }

        @Override
        public Object get() {
          return accessible.getObject();
        }
      });
    }
  };

  private static final AccessorFactory<Map<String, Object>> MAP_PARAM = new AccessorFactory<Map<String, Object>>() {
    @Override
    public void addToWrapper(AccessorWrapper wrapper, Map<String, Object> target) {
      for (Map.Entry<String, Object> entry : target.entrySet()) {
        String key = entry.getKey();
        wrapper.put(key, new Accessor() {

          @Override
          public void set(Object o) {
            target.put(key, o);
          }

          @Override
          public Object get() {
            return target.get(key);
          }
        });
      }
    }
  };

  public AccessorWrapper wrapper(Object obj) {
    if (obj instanceof AccessorWrapper) {
      return (AccessorWrapper) obj;
    }
    return DEFAULT.wrapper(obj);
  }

  public AccessorFactory factory(Object obj) {
    if (Map.class.isAssignableFrom(obj.getClass())) {
      return MAP_PARAM;
    }

    if (RootNamedParam.class.isAssignableFrom(obj.getClass())) {
      return NAMED_PARAM;
    }

    String accessorClassName = getAccessorClassName(obj);
    try {
      return (AccessorFactory) Class.forName(accessorClassName).newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return DEFAULT;
  }

  private String getAccessorClassName(Object obj) {
    return obj.getClass().getName() + "_" + "Accessor";
  }

  private static class SelfAccessor implements Accessor<Object> {

    private Object mValue;

    public SelfAccessor(Object value) {
      mValue = value;
    }

    @Override
    public Object get() {
      return mValue;
    }

    @Override
    public void set(Object o) {
      mValue = o;
    }
  }

}
