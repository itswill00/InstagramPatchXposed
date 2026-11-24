package com.golda.patchinstagram;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScreenshotsTest {
    @Test public void clearsOnlySecureFlag() {
        int secure = 0x00002000; // WindowManager.LayoutParams.FLAG_SECURE
        assertEquals(0, Screenshots.clearSecure(secure));
        assertEquals(1, Screenshots.clearSecure(secure | 1));
        assertEquals(8, Screenshots.clearSecure(8));
    }
}
