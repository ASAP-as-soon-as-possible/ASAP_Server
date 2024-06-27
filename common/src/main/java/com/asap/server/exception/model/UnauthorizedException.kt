package com.asap.server.exception.model

import com.asap.server.exception.Error

class UnauthorizedException(override val error: Error) : AsapException(error)