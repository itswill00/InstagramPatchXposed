package com.golda.patchinstagram;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Finds direct Instagram CDN media URLs in copied text and derives a safe file name. */
final class MediaUrl {
    private static final Pattern URL = Pattern.compile("https?://[^\\s\"'<>]+", Pattern.CASE_INSENSITIVE);
    private static final Pattern EXT = Pattern.compile("\\.(jpg|jpeg|png|webp|mp4)(?:\\?|#|$)", Pattern.CASE_INSENSITIVE);

    private MediaUrl() { }

    static String find(String text) {
        if (text == null) return null;
        Matcher urls = URL.matcher(text);
        while (urls.find()) {
            String url = urls.group();
            if (EXT.matcher(url).find()) return url;
        }
        return null;
    }

    static boolean video(String url) {
        return url != null && url.toLowerCase(Locale.ROOT).contains(".mp4");
    }

    static String name(String url) {
        if (url == null) return "instagram-media";
        String path = url;
        int query = path.indexOf('?');
        if (query >= 0) path = path.substring(0, query);
        int fragment = path.indexOf('#');
        if (fragment >= 0) path = path.substring(0, fragment);
        String last = path.substring(path.lastIndexOf('/') + 1);
        last = last.replaceAll("[^A-Za-z0-9._-]", "_");
        if (last.isEmpty() || ".".equals(last)) return "instagram-media";
        return last;
    }
}
