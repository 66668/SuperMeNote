# 超级笔记-开发问题汇总

## 版本及分支相关说明相关

| 更新时间阶段 |分支名|开发语言|说明 |
| :-----: | :------- | :------- | :----- |
| 2019-05-11 |master|android-java语言|该分支为java语言开发|
|  || |

## 编译配置和混淆相关

1. 舍弃support包，用androidx包替换问题：

Migrating to AndroidX:https://developer.android.google.cn/jetpack/androidx/migrate

https://blog.csdn.net/yin_ol/article/details/89421390

点击：Refacter/MigRate to AndroidX，如果是老项目，最好保存zip包，方便回退，新项目，直接点击OK，自动集成即可。

## 功能模块设计变迁及说明

## 数据库模块

| 更新时间阶段 |代码模块位置|代码说明|功能说明 |
| :-----: | :------- | :------- | :----- |
| 2019-05-11 |app/com.supermenote.basedb|使用android原生sqlite|现阶段写在app下，作为app的小功能，初次开发，后期会独立成一个module模块，做复杂设计，可能会引入第三方数据库|
|  || |






