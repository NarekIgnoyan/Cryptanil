package com.primesoft.cryptanil.models

import com.primesoft.cryptanil.utils.extensions.EMPTY_STRING

open class TypeItem : java.io.Serializable {

    open fun getType(): String {
        return EMPTY_STRING
    }

    open fun getIconURL(): String {
        return EMPTY_STRING
    }

}