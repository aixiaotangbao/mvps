package com.kegy.mvps.compiler.base;

import com.squareup.javapoet.TypeSpec;

public abstract class ClassBuilder {

  protected String mPkg;
  protected String mCls;
  protected TypeSpec.Builder mTypeBuilder;

  public String getPkg() {
    return mPkg;
  }

  public String getCls() {
    return mCls;
  }

  public abstract TypeSpec.Builder build();

}
