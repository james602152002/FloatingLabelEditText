package com.james602152002.floatinglabeledittext.validator;

import android.support.annotation.NonNull;

public class NumberDecimalValidator extends RegexValidator {

    public NumberDecimalValidator(@NonNull String error_message) {
        super(error_message, "[0-9]*['.']?[0-9]*");
    }

}
