package com.asap.server.domain.meeting

enum class Duration(needBlock: Int) {
    HALF(1),
    HOUR(2),
    HOUR_HALF(3),
    TWO_HOUR(4),
    TWO_HOUR_HALF(5),
    THREE_HOUR(6)
}