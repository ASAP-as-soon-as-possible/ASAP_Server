package com.asap.server.exception.model

import com.asap.server.exception.Error

class HostTimeForbiddenException(
    val data: String,
    override val error: Error
) : AsapException(error) {
}