# Developing...

# Android浮动字文本-FloatingLabelEditText

[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![MinSDK](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![](https://jitpack.io/v/james602152002/FloatingLabelEditText.svg)](https://jitpack.io/#james602152002/FloatingLabelEditText)
[![Build Status](https://travis-ci.org/james602152002/FloatingLabelEditText.svg?branch=master)](https://travis-ci.org/james602152002/FloatingLabelEditText)
[![codecov](https://codecov.io/gh/james602152002/FloatingLabelEditText/branch/master/graph/badge.svg)](https://codecov.io/gh/james602152002/FloatingLabelEditText)

## [English](README_EN.md) | 中文

一般浮动字体文本设置自定义图标仅支持png格式，不支持Icon Font形式，也无法设置图标大小。效果与TextInputLayout相比略差一截，故开发此控件。

## 特点功能:

 - 支持代码更改字体大小(包含浮动文字、提示文字、错误文字)
 - 支持代码设置提示、分割线以及错误状态下的颜色 
 - 支持ttf设置EditText左侧图标
 - 错误文字的字数太多会以跑马灯动画展示
 - 浮动文字显示字数过多以ellipsize展示
 - 浮动文字支持ForegroundColorSpan
 
 ## Demo
  - [下载 APK-Demo](art/demo.apk)
  - [示例网址](https://github.com/james602152002/FloatingLabelEditTextDemo)
 
 ## 项目演示
 
 |错误/浮动效果|跑马灯效果|
 |:---:|:---:|
 |![](art/error_demo.gif)|![](art/text_slide_demo.gif)|
 
 ## 依赖:
 
 ```
 allprojects {
 	repositories {
 		...
 		maven { url 'https://jitpack.io' }
 	}
 }
 ```
 
 ```
 dependencies {
 	compile 'com.github.james602152002:FloatingLabelSpinner:1.0.0'
 }
 ```