package com.kegy.mvps.commons;

import com.squareup.javapoet.TypeName;

@SuppressWarnings("unchecked")
public class BoxUtils {

  public static <R, T> R getBoxValue(T data) {
    if (data == null) {
      return null;
    }
    if (int.class.isAssignableFrom(data.getClass())) {
      return (R) Integer.valueOf(String.valueOf(data));
    } else if (byte.class.isAssignableFrom(data.getClass())) {
      return (R) Byte.valueOf(String.valueOf(data));
    } else if (short.class.isAssignableFrom(data.getClass())) {
      return (R) Short.valueOf(String.valueOf(data));
    } else if(boolean.class.isAssignableFrom(data.getClass())) {
      return (R) Boolean.valueOf(String.valueOf(data));
    } else if(long.class.isAssignableFrom(data.getClass())) {
      return (R) Long.valueOf(String.valueOf(data));
    } else if(Double.class.isAssignableFrom(data.getClass())) {
      return (R)Double.valueOf(String.valueOf(data));
    } else if(char.class.isAssignableFrom(data.getClass())) {
      return (R) (Character) data;
    } else {
      return (R) data;
    }
  }

  public static TypeName getBoxTypeName(TypeName fieldType) {
    TypeName afterBox;
    if (fieldType.isPrimitive() || fieldType.isBoxedPrimitive()) {
      fieldType = fieldType.unbox();
    }
    if (fieldType.equals(TypeName.INT)) {
      afterBox = TypeName.get(Integer.class);
    } else if(fieldType.equals(TypeName.SHORT)) {
      afterBox = TypeName.get(Short.class);
    } else if(fieldType.equals(TypeName.BYTE)) {
      afterBox = TypeName.get(Byte.class);
    } else if (fieldType.equals(TypeName.LONG)) {
      afterBox = TypeName.get(Long.class);
    } else if (fieldType.equals(TypeName.FLOAT)) {
      afterBox = TypeName.get(Float.class);
    } else if (fieldType.equals(TypeName.DOUBLE)) {
      afterBox = TypeName.get(Double.class);
    } else if (fieldType.equals(TypeName.CHAR)) {
      afterBox = TypeName.get(Character.class);
    } else if (fieldType.equals(TypeName.BOOLEAN)) {
      afterBox = TypeName.get(Boolean.class);
    } else {
      afterBox = fieldType;
    }
    return afterBox;
  }

  public static String defaultValue(TypeName fieldType) {
    String defaultValue;
    if (fieldType.isPrimitive() || fieldType.isBoxedPrimitive()) {
      fieldType = fieldType.unbox();
    }
    if (fieldType.equals(TypeName.INT)
        || fieldType.equals(TypeName.SHORT)
        || fieldType.equals(TypeName.BYTE)) {
      defaultValue = "0";
    } else if (fieldType.equals(TypeName.LONG)) {
      defaultValue = "0L";
    } else if (fieldType.equals(TypeName.FLOAT)) {
      defaultValue = "0.0f";
    } else if (fieldType.equals(TypeName.DOUBLE)) {
      defaultValue = "0.0";
    } else if (fieldType.equals(TypeName.CHAR)) {
      defaultValue = "java.lang.Character.MIN_VALUE";
    } else if (fieldType.equals(TypeName.BOOLEAN)) {
      defaultValue = "false";
    } else {
      defaultValue = "null";
    }
    return defaultValue;
  }


}
