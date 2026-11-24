package com.golda.patchinstagram;

import android.content.Context;
import android.content.SharedPreferences;

/** Minimal settings store, kept inside Instagram's data directory. */
final class Prefs {
    static final String CLEAN_LINKS = "clean_links";
    static final String NO_TRACKERS = "no_trackers";
    static final String BLOCK_LOCATION = "block_location";
    static final String ALLOW_SCREENSHOTS = "allow_screenshots";
    static final String BLOCK_CLIPBOARD_READ = "block_clipboard_read";
    static final String DOWNLOAD_MEDIA = "download_media";
    private static final String FILE = "instagrampatchxposed";
    private static volatile SharedPreferences preferences;
    private static volatile boolean cleanLinks = true;
    private static volatile boolean noTrackers = true;
    private static volatile boolean blockLocation = true;
    private static volatile boolean allowScreenshots = true;
    private static volatile boolean blockClipboardRead = true;
    private static volatile boolean downloadMedia = true;

    private Prefs() { }

    static synchronized void load(Context context) {
        if (preferences != null || context == null) return;
        preferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        cleanLinks = preferences.getBoolean(CLEAN_LINKS, true);
        noTrackers = preferences.getBoolean(NO_TRACKERS, true);
        blockLocation = preferences.getBoolean(BLOCK_LOCATION, true);
        allowScreenshots = preferences.getBoolean(ALLOW_SCREENSHOTS, true);
        blockClipboardRead = preferences.getBoolean(BLOCK_CLIPBOARD_READ, true);
        downloadMedia = preferences.getBoolean(DOWNLOAD_MEDIA, true);
    }

    static boolean on(String key) {
        if (CLEAN_LINKS.equals(key)) return cleanLinks;
        if (NO_TRACKERS.equals(key)) return noTrackers;
        if (BLOCK_LOCATION.equals(key)) return blockLocation;
        if (ALLOW_SCREENSHOTS.equals(key)) return allowScreenshots;
        if (BLOCK_CLIPBOARD_READ.equals(key)) return blockClipboardRead;
        if (DOWNLOAD_MEDIA.equals(key)) return downloadMedia;
        return false;
    }
}
