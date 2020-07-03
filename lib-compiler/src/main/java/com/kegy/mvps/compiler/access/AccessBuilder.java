package com.kegy.mvps.compiler.access;

import javax.lang.model.element.Modifier;

import com.kegy.mvps.commons.BoxUtils;
import com.kegy.mvps.commons.accessor.Accessor;
import com.kegy.mvps.commons.accessor.AccessorFactory;
import com.kegy.mvps.commons.accessor.AccessorWrapper;
import com.kegy.mvps.compiler.base.ClassBuilder;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

public class AccessBuilder extends ClassBuilder {
  private static final String SUFFIX = "Accessor";
  private static final String ADD_TO_WRAPPER = "addToWrapper";

  private static final String TARGET = "target";
  private static final String WRAPPER = "wrapper";
  private static final String GET = "get";
  private static final String SET = "set";
  private static final String VALUE = "value";


  private final MethodSpec.Builder mAddToWrapper;
  private final TypeSpec.Builder mTypeBuilder;

  public AccessBuilder(String pkg, String clsName, AnnotationSpec autoGenerate, TypeName typeName) {
    mPkg = pkg;
    mCls = clsName + "_" + SUFFIX;
    mAddToWrapper = MethodSpec
        .methodBuilder(ADD_TO_WRAPPER)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addAnnotation(Override.class)
        .addParameter(AccessorWrapper.class, WRAPPER, Modifier.FINAL)
        .addParameter(typeName, TARGET, Modifier.FINAL);
    mTypeBuilder = TypeSpec.classBuilder(mCls)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addAnnotation(autoGenerate)
        .superclass(ParameterizedTypeName.get(ClassName.get(AccessorFactory.class), typeName));
  }

  @Override
  public TypeSpec.Builder build() {
    return mTypeBuilder.addMethod(mAddToWrapper.build());
  }

  public void onFieldByType(String key, String fieldName, TypeName typeName, boolean canBeNull,
      boolean assignable) {
    on(() -> CodeBlock.of("$L.put($T.class, $L)", WRAPPER, typeName, TypeSpec
        .anonymousClassBuilder("")
        .addSuperinterface(ParameterizedTypeName.get(ClassName.get(Accessor.class), BoxUtils.getBoxTypeName(typeName)))
        .addMethod(MethodSpec.methodBuilder(SET)
            .addStatement("$L.$L = $L", TARGET, fieldName, VALUE)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(BoxUtils.getBoxTypeName(typeName), VALUE)
            .addAnnotation(Override.class).build())
        .addMethod(MethodSpec.methodBuilder(GET)
            .addModifiers(Modifier.PUBLIC)
            .addStatement("return $L.$L",TARGET, fieldName)
            .returns(BoxUtils.getBoxTypeName(typeName)).build())
        .build()));
  }

  public void onFieldByName(String key, String fieldName, TypeName typeName, boolean canBeNull,
      boolean assignable) {
    on(() -> CodeBlock.of("$L.put($S, $L)", WRAPPER, key, TypeSpec
        .anonymousClassBuilder("")
        .addSuperinterface(ParameterizedTypeName.get(ClassName.get(Accessor.class), BoxUtils.getBoxTypeName(typeName)))
        .addMethod(MethodSpec.methodBuilder(SET)
            .addStatement("$L.$L = $L", TARGET, fieldName, VALUE)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(BoxUtils.getBoxTypeName(typeName), VALUE)
            .addAnnotation(Override.class).build())
        .addMethod(MethodSpec.methodBuilder(GET)
            .addModifiers(Modifier.PUBLIC)
            .addStatement("return $L.$L", TARGET, fieldName)
            .returns(BoxUtils.getBoxTypeName(typeName)).build())
        .build()));
  }

  public void onMethod(String key, String fieldName, TypeName typeName) {
    on(() -> CodeBlock.of("$L.put($S, $L)", WRAPPER, key, TypeSpec
        .anonymousClassBuilder("")
        .addSuperinterface(ParameterizedTypeName.get(ClassName.get(Accessor.class), BoxUtils.getBoxTypeName(typeName)))
        .addMethod(MethodSpec.methodBuilder(GET)
            .addModifiers(Modifier.PUBLIC)
            .addStatement("return $L.$N()",TARGET, fieldName)
            .returns(BoxUtils.getBoxTypeName(typeName)).build())
        .build()));
  }

  private void on(AddToWrapperBlock addToWrapperBlock) {
    mAddToWrapper
        .addStatement(addToWrapperBlock.buildCodeBlock());
  }

  interface AddToWrapperBlock {
    CodeBlock buildCodeBlock();
  }

}
