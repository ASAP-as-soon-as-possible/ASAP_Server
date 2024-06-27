package com.asap.server.exception.model

import com.asap.server.exception.Error

class NotFoundException(override val error : Error) : AsapException(error)