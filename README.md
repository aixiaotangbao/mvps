# mvps

mvps是一个android开发组件库，主要的目的是对庞大的业务进行切割，切割成各个可独立变化的子业务。从而实现当未来的业务拓展的时候，尽量减少对已有业务的影响。切割的每个子业务都是可以独立变化的模块，切割的模块尽可能的小，尽可能的可以独立变化。mvps的主要作用是将这些独立变化的模块组装成一个庞大的业务。

## mvps的作用

mvps主要的作用是组装各个子模块，以及提供一种尽可能简单，单一的子模块之间通信的方案。我们将Presenter每一个子模块称为一个Presenter（对应了Presenter类），Presenter分为root和child两类，我们可以通过Presenter.addPresenter()来添加子模块，那么被添加的Presenter就是root presenter。

## Presenter

Presenter用于描述一个子业务，由mvps框架提供生命周期的回掉，分别是(create,bind,unbind,destroy)。
create生命周期回掉的时候所有view已经绑定成功，bind生命周期回掉的时候所有的数据都已经注入完成。

## 使用

在project的build.gradle中加入：

    maven { url " https://dl.bintray.com/kgy/maven" }

其次在app的build.gradle中加入：

    annotationProcessor 'com.kegy.mvps:compiler:1.0.2'
    implementation 'com.kegy.mvps:mvps:1.0.2'
    implementation 'com.kegy.mvps:base:1.0.2'
    
这是mvps框架所需的依赖，此外mvps还必须引入rxjava2,butterknife。

    annotationProcessor 'com.jakewharton:butterknife-compiler:10.2.1'
    annotationProcessor"com.google.dagger:dagger-compiler:2.0.2"
    implementation 'com.jakewharton:butterknife:10.2.1'

    implementation 'io.reactivex.rxjava2:rxjava:2.2.12'
    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
    
可以查看demo的具体使用方式
