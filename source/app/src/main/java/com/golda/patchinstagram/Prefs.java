package com.golda.patchinstagram;

import android.content.Context;
import android.content.SharedPreferences;

/** Minimal settings store, kept inside Instagram's data directory. */
final class Prefs {
    static final String CLEAN_LINKS = "clean_links";
    private static final String FILE = "instagrampatchxposed";
    private static volatile SharedPreferences preferences;
    private static volatile boolean cleanLinks = true;

    private Prefs() { }

    static synchronized void load(Context context) {
        if (preferences != null || context == null) return;
        preferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        cleanLinks = preferences.getBoolean(CLEAN_LINKS, true);
    }

    static boolean on(String key) {
        return CLEAN_LINKS.equals(key) && cleanLinks;
    }
}
