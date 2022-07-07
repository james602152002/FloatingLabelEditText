package com.james602152002.floatinglabeledittext.intercepter

interface ClearBtnInterceptor {
    fun onProcessClear(clear: Boolean?)
    fun invokeTouchClearBtn()
}