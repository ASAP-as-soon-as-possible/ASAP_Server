package com.asap.server.exception.model

import com.asap.server.exception.Error

open class AsapException(
    open val error: Error
) : RuntimeException(error.message) {
}