package com.kegy.mvps.receiver;

import androidx.annotation.NonNull;

/**
 * 被注入对象的抽象接口 任何能被注入的对象都需要直接或者
 * 间接实现该类 也可以选择实现{@link BaseInjectReceiver}
 * 该类 该接口的inject()方法是用于注入的核心方法 该类将从
 * inject()方法的参数来进行注入
 *
 * @author keguoyu
 * @version 1.0
 */
public interface InjectorReceiver {

  void addToProvider(@NonNull Object src);

  /**
   * 注入的核心方法 它将会将objects中相应的变量
   * 注入到该类
   *
   * @param objects 注入的对象
   */
  void inject(Object... objects);

  /**
   * 重置方法
   */
  void reset();

  boolean available();

}
