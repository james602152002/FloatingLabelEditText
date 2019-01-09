# Android浮动字体文本-FloatingLabelEditText

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-FloatingLabelEditText-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/6727)
[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![MinSDK](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![](https://jitpack.io/v/james602152002/FloatingLabelEditText.svg)](https://jitpack.io/#james602152002/FloatingLabelEditText)
[![Build Status](https://travis-ci.org/james602152002/FloatingLabelEditText.svg?branch=master)](https://travis-ci.org/james602152002/FloatingLabelEditText)
[![codecov](https://codecov.io/gh/james602152002/FloatingLabelEditText/branch/master/graph/badge.svg)](https://codecov.io/gh/james602152002/FloatingLabelEditText)

## [English](common_md/README_EN.md) | 中文

一般浮动字体文本无法自定义清除图标，亦无法支持多行模式，故开发此控件。

## 特点功能:

 - 支持代码更改字体大小(包含浮动文字、提示文字、错误文字)
 - 支持代码设置提示、分割线以及错误状态下的颜色 
 - 支持自定义清除图标
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
 
 [更多效果](common_md/DEMONSTRATION_CH.md)
 
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
 	implementation 'com.github.james602152002:FloatingLabelEditText:1.3.8'
 }
 ```
 
 ## 属性
 ```xml
 <com.james602152002.floatinglabeledittext.FloatingLabelEditText
           //设置获取焦点状态下的颜色
           app:j_fle_colorHighlight="#0000FF" 
           //设置分割线未获取焦点状态下的颜色
           app:j_fle_colorDivider="#FF00FF"
           //设置错误状态下的颜色
           app:j_fle_colorError="#0000FF"
           //设置Label提示文字
           app:j_fle_hint="label"
           //设置divider厚度
           app:j_fle_thickness="2dp"
           //设置label水平间距
           app:j_fle_label_horizontal_margin="2dp"
           //设置label垂直间距
           app:j_fle_label_vertical_margin="2dp"
           //设置错误文字水平间距
           app:j_fle_error_horizontal_margin="2dp"
           //设置分割线垂直间距
           app:j_fle_divider_vertical_margin="2dp"
           //设置浮动文字大小
           app:j_fle_label_textSize="14sp"
           //设置错误文字大小
           app:j_fle_error_textSize="14sp"
           //浮动文字动画时间(单位：毫秒)
           app:j_fle_float_anim_duration="800"
           //错误动画滚动时间(单位：毫秒)
           app:j_fle_error_anim_duration="8000"
           //错误模式关闭(默认打开)
           app:j_fle_error_disable="true"
           //多行模式打开(默认关闭)
           app:j_fle_multiline_mode_enable="true"
           //清除按钮打开(默认关闭)
           app:j_fle_enable_clear_btn="true"
           //设置清除按钮大小
           app:j_fle_clear_btn_size="10dp"
           //设置清除按钮颜色
           app:j_fle_clear_btn_color="#FF0000"
           //设置清除按钮水平间距
           app:j_fle_clear_btn_horizontal_margin="2dp"
           //设置png, VectorDrawable清除按钮Id
           app:j_fle_clear_btn_id = "@drawable/icon"
           //没焦点状况下，依然显示清除按钮(默认不显示)
           app:j_fle_show_clear_btn_without_focus="true"
           //显示字数模式开关(默认关闭)
           app:j_fle_show_text_length="true"
           //显示字数提示颜色(默认highlight_color)
           app:j_fle_text_length_display_color="#00FFFF"
           />
           
 ```
 
 ## 方法
 ```java
 public void setting(){
    //设置获取焦点状态下的颜色
    setHighlightColor(int color);
    //设置分割线未获取状态下的颜色
    setDivider_color(int divider_color);
    //设置错误状态下的颜色
    setError_color(int error_color);
    //设置Label提示文字
    setLabel(CharSequence hint);
    //设置divider厚度
    setThickness(int thickness);
    //设置label水平间距
    setLabelMargins(int horizontal_margin, int vertical_margin);
    //设置错误文字水平间距
    setErrorMargin(int horizontal_margin);
    //设置分割线间距(单位：毫秒)
    setDivider_vertical_margin(int divider_vertical_margin);
    //设置浮动文字大小
    setLabel_text_size(float label_text_size);
    //设置错误文字大小
    setError_text_size(float error_text_size);
    //浮动文字动画时间(单位：毫秒)
    setAnimDuration(int ANIM_DURATION);
    //错误动画滚动时间(单位：毫秒)
    setErrorAnimDuration(int ERROR_ANIM_DURATION);
    //开启错误模式
    setError_enabled();
    //关闭错误模式
    setError_disabled();
    //启用多行模式(默认关闭)
    setMultiline_mode(boolean enable);
    //清除按钮打开(默认关闭)
    enableClearBtn(boolean enable);
    //设置清除按钮大小
    setClear_btn_size(int clear_btn_size);
    //设置清除按钮颜色
    setClear_btn_color(int clear_btn_color);
    //设置清除按钮水平间距
    setClear_btn_horizontal_margin(int clear_btn_horizontal_margin);
    //设置ttf清除图标
    customizeClearBtn(Typeface typeface, String uni_code, int color, int clear_btn_size);
    //设置png, VectorDrawable清除按钮
    customizeClearBtn(int drawableId, int clear_btn_width);
    //没焦点状况下，依然显示清除按钮
    showClearButtonWithoutFocus();
    //显示字数模式开关(默认关闭)
    showMaxTextLength(boolean show);
    //显示字数提示颜色(默认highlight_color)
    setText_length_display_color(int text_length_display_color);
    //设置错误
    setError(CharSequence error);
 }
 
 ```
 ## 验证模式使用
 ```java
    public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            //实例化对象
            FloatingLabelEditText label = findViewById(R.id.label);
            //设置错误信息以及正则表达式
            label.addValidator(new RegexValidator("long error hint", "\\d+"));
            label.addValidator(new RegexValidator("You input letters.", "[A-Za-z]+$"));
        }
   }
    
 ```
 
 ```
    在错误验证模式下，TextWatcher会自动依據正则表达式校验，
    如果符合正则表达式，则显示error message于下方。
 ```
 ## 混淆
 
 无需混淆代码。
 
 ## 赞赏
 
 如果觉得效果写得不错，欢迎赏小弟一口蛋白粉 :)
 
 |微信|支付宝|
 |:---:|:---:|
 |![](art/weixin_green.jpg)|![](art/zhifubao_blue.jpg)|
 
 ## 建议
 
 ```
 提示文字不得超过控件宽度，
 传统文本编辑框使用TextView作为标题配合EditText占用太多版面，
 使用浮动字体文本配合简短的提示文字，
 可实现半扁平设计。
 ```
 
 License
 -------
 
     Copyright 2018 james602152002
 
     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at
 
        http://www.apache.org/licenses/LICENSE-2.0
 
     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.