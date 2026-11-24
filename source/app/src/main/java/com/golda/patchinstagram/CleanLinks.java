package com.golda.patchinstagram;

import android.content.ClipData;
import android.content.ClipboardManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Removes tracking parameters from copied Instagram links. The parser is plain Java so it can be
 * tested on the JVM; the hook only replaces the ClipData when a link was actually cleaned.
 */
final class CleanLinks {
    private static final Set<String> TRACKING = new HashSet<>(Arrays.asList(
            "igsh", "igshid", "fbclid", "gclid", "msclkid", "mc_cid", "mc_eid", "ref", "ref_src", "ref_url"
    ));

    private CleanLinks() { }

    static void install(ClassLoader loader) {
        Hooks.hookAll(ClipboardManager.class, "setPrimaryClip", new Hooks.Hook() {
            @Override protected void before(Hooks.Call param) {
                if (!Prefs.on(Prefs.CLEAN_LINKS) || param.args.length == 0
                        || !(param.args[0] instanceof ClipData)) return;
                ClipData original = (ClipData) param.args[0];
                ClipData cleaned = clean(original);
                if (cleaned != null) param.args[0] = cleaned;
            }
        });
    }

    private static ClipData clean(ClipData data) {
        boolean changed = false;
        List<ClipData.Item> items = new ArrayList<>();
        for (int i = 0; i < data.getItemCount(); i++) {
            CharSequence text = data.getItemAt(i).getText();
            String value = text == null ? "" : text.toString();
            String result = clean(value);
            if (!result.equals(value)) {
                changed = true;
                items.add(new ClipData.Item(result));
            } else {
                items.add(data.getItemAt(i));
            }
        }
        if (!changed || items.isEmpty()) return null;
        CharSequence label = data.getDescription().getLabel();
        String[] types = new String[data.getDescription().getMimeTypeCount()];
        for (int i = 0; i < types.length; i++) types[i] = data.getDescription().getMimeType(i);
        return new ClipData(label == null ? "" : label, types, items.get(0));
    }

    /** Cleans the first Instagram URL found in {@code text}, leaving surrounding text untouched. */
    static String clean(String text) {
        if (text == null) return "";
        int start = text.indexOf("http");
        if (start < 0) return text;
        int end = start;
        while (end < text.length()) {
            char c = text.charAt(end);
            if (Character.isWhitespace(c) || c == '"' || c == '<' || c == '>') break;
            end++;
        }
        String url = text.substring(start, end);
        String cleaned = cleanUrl(url);
        return cleaned.equals(url) ? text : text.substring(0, start) + cleaned + text.substring(end);
    }

    private static String cleanUrl(String url) {
        int question = url.indexOf('?');
        if (question < 0) return url;
        String base = url.substring(0, question);
        String fragment = "";
        String query = url.substring(question + 1);
        int hash = query.indexOf('#');
        if (hash >= 0) {
            fragment = query.substring(hash);
            query = query.substring(0, hash);
        }
        List<String> kept = new ArrayList<>();
        for (String pair : query.split("&")) {
            if (pair.isEmpty()) continue;
            String key = pair.contains("=") ? pair.substring(0, pair.indexOf('=')) : pair;
            if (!tracking(key)) kept.add(pair);
        }
        return base + (kept.isEmpty() ? "" : "?" + String.join("&", kept)) + fragment;
    }

    static boolean tracking(String key) {
        if (key == null) return false;
        String lower = key.toLowerCase(Locale.ROOT);
        return lower.startsWith("utm_") || TRACKING.contains(lower);
    }
}
