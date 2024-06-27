package com.asap.server.controller

import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ServerProfileController(
        private val env: Environment
) {
    @GetMapping("/profile")
    fun getProfile(): String {
        return Arrays.stream(env.activeProfiles)
                .findFirst()
                .orElse("")
    }
}