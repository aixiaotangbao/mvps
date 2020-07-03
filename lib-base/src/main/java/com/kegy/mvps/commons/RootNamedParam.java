package com.kegy.mvps.commons;

public class RootNamedParam {

  private String mKey;
  private Object mObject;

  public RootNamedParam(String key, Object object) {
    mKey = key;
    mObject = object;
  }

  public String getKey() {
    return mKey;
  }

  public void setObject(Object object) {
    mObject = object;
  }

  public Object getObject() {
    return mObject;
  }
}
