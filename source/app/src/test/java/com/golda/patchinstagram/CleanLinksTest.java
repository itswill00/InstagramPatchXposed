package com.golda.patchinstagram;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanLinksTest {
    @Test public void removesInstagramTrackingParameters() {
        assertEquals("https://www.instagram.com/p/ABC/",
                CleanLinks.clean("https://www.instagram.com/p/ABC/?igsh=abc123&utm_source=copy"));
        assertEquals("https://www.instagram.com/reel/XYZ/?img_index=2",
                CleanLinks.clean("https://www.instagram.com/reel/XYZ/?img_index=2&igshid=xyz&fbclid=1"));
    }

    @Test public void keepsFragmentAndUnrelatedParameters() {
        assertEquals("https://www.instagram.com/p/ABC/?img_index=3#comments",
                CleanLinks.clean("https://www.instagram.com/p/ABC/?img_index=3&igsh=1#comments"));
    }

    @Test public void leavesPlainTextAndCleanLinksAlone() {
        assertEquals("check this out", CleanLinks.clean("check this out"));
        assertEquals("https://www.instagram.com/p/ABC/", CleanLinks.clean("https://www.instagram.com/p/ABC/"));
    }

    @Test public void recognisesOnlyTrackingKeys() {
        assertTrue(CleanLinks.tracking("IGSH"));
        assertTrue(CleanLinks.tracking("utm_medium"));
        assertEquals(false, CleanLinks.tracking("img_index"));
    }
}
