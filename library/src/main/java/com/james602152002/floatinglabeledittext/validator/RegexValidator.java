package com.james602152002.floatinglabeledittext.validator;

import android.support.annotation.NonNull;

import java.util.regex.Pattern;

/**
 * Created by shiki60215 on 18-1-19.
 */

public class RegexValidator {

    private Pattern pattern;
    private String error_message;
    private boolean matches;

    public RegexValidator(@NonNull String error_message, @NonNull String regex) {
        this.error_message = error_message;
        pattern = Pattern.compile(regex);
    }

    public RegexValidator(@NonNull String error_message, @NonNull String regex, boolean matches) {
        this.error_message = error_message;
        pattern = Pattern.compile(regex);
        this.matches = matches;
    }

    public String getError_message(CharSequence text) {
        if (pattern.matcher(text).matches()) {
            return matches ? error_message : null;
        }
        return error_message;
    }
}
