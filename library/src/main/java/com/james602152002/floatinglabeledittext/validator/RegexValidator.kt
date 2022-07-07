package com.james602152002.floatinglabeledittext.validator

import java.util.regex.Pattern

/**
 * Created by shiki60215 on 18-1-19.
 */
open class RegexValidator {
    private var pattern: Pattern
    private var error_message: String
    protected var matches = false

    constructor(error_message: String, regex: String) {
        this.error_message = error_message
        pattern = Pattern.compile(regex)
    }

    constructor(error_message: String, regex: String, matches: Boolean) {
        this.error_message = error_message
        pattern = Pattern.compile(regex)
        this.matches = matches
    }

    fun getError_message(text: CharSequence?): String? {
        return if (pattern.matcher(text ?: "").matches()) {
            if (matches) error_message else null
        } else error_message
    }
}