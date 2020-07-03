package com.kegy.mvps.commons.injector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Injectors {

  private static final Injector NOOP = new Injector() {
    @Override
    public void inject(Object target, Object accessible) {}

    @Override
    public Set<String> getAllNames() {
      return Collections.emptySet();
    }

    @Override
    public Set<Class> getAllTypes() {
      return Collections.emptySet();
    }

    @Override
    public void reset(Object target) {}
  };


  public static List<Injector<Object>> injectorList(Object obj) {
    List<Injector<Object>> list = new ArrayList<>();

    Class clazz = obj.getClass();

    try {
      while (clazz != null) {
        try {
          Class<? extends Injector<Object>> injector =
              (Class<? extends Injector<Object>>) Class.forName(getInjectorClassName(clazz));
          list.add((Injector<Object>) injector.newInstance());
        } catch (ClassNotFoundException e) {

        }
        clazz = clazz.getSuperclass();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (list.isEmpty()) {
      list.add(NOOP);
    }
    return list;
  }

  private static String getInjectorClassName(Class clazz) {
    String injectorClassName = clazz.getName();
    return injectorClassName + "_Injector";
  }

}
