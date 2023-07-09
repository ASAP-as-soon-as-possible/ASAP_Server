package com.asap.server.common.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

@Component
public class SecureUrlUtil {

    public String encodeUrl(Long meetingId) {
        return Base64Utils.encodeToUrlSafeString(meetingId.toString().getBytes());
    }

    public Long decodeUrl(String url) {
        return Long.parseLong(new String(Base64Utils.decodeFromUrlSafeString(url)));
    }
}
