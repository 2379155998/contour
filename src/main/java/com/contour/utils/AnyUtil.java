package com.contour.utils;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;

public class AnyUtil {
    public static Any.Builder anyBuilder(String typeUrl, ByteString value) {
        return Any.newBuilder()
                .setTypeUrl(typeUrl)
                .setValue(value);
    }
}
