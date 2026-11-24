package com.golda.patchinstagram;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MediaUrlTest {
    @Test public void findsDirectMediaUrlInCopiedText() {
        String text = "see https://scontent.cdninstagram.com/v/t51.2885-15/abc.jpg?stp=dst-jpg&_nc_cat=1";
        assertEquals("https://scontent.cdninstagram.com/v/t51.2885-15/abc.jpg?stp=dst-jpg&_nc_cat=1",
                MediaUrl.find(text));
        assertTrue(MediaUrl.video(MediaUrl.find("https://cdn.example/video.mp4?x=1")));
    }

    @Test public void ignoresPostLinksAndPlainText() {
        assertNull(MediaUrl.find("https://www.instagram.com/p/ABC/?igsh=1"));
        assertNull(MediaUrl.find("no link here"));
    }

    @Test public void derivesSafeFileName() {
        assertEquals("abc.jpg", MediaUrl.name("https://cdn.example/path/abc.jpg?stp=1"));
        assertEquals("video_1.mp4", MediaUrl.name("https://cdn.example/v/video 1.mp4"));
        assertEquals("instagram-media", MediaUrl.name("https://cdn.example/"));
    }
}
