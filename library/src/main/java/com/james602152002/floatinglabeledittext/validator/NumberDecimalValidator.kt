package com.james602152002.floatinglabeledittext.validator

class NumberDecimalValidator(error_message: String) :
    RegexValidator(error_message, "^(-)?[0-9]*['.']?[0-9]*")