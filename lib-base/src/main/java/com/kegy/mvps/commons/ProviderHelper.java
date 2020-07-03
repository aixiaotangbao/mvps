package com.kegy.mvps.commons;

import com.kegy.mvps.commons.accessor.Accessors;

public class ProviderHelper {

  private static ObjectProvider sObjectProvider = new ObjectProviderImpl();

  public static boolean have(Object access, String key) {
    return sObjectProvider.have(Accessors.instance().wrapper(access), key);
  }

  public static <T> T fetch(Object access, String key) {
    return sObjectProvider.fetch(Accessors.instance().wrapper(access), key);
  }

  public static <T> boolean have(Object access, Class<T> key) {
    return sObjectProvider.have(Accessors.instance().wrapper(access), key);
  }

  public static <T> T fetch(Object access, Class<T> key) {
    return sObjectProvider.fetch(Accessors.instance().wrapper(access), key);
  }


}
