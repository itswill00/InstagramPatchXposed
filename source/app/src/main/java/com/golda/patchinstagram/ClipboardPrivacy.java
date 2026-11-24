package com.golda.patchinstagram;

import android.content.ClipboardManager;

/** Blocks Instagram from reading the clipboard while the switch is on. */
final class ClipboardPrivacy {
    private ClipboardPrivacy() { }

    static void install() {
        Hooks.Hook hook = new Hooks.Hook() {
            @Override protected void before(Hooks.Call param) {
                if (Prefs.on(Prefs.BLOCK_CLIPBOARD_READ)) param.setResult(null);
            }
        };
        int hooks = Hooks.hookAll(ClipboardManager.class, "getPrimaryClip", hook);
        Hooks.log("clipboard privacy: hooks=" + hooks);
    }
}
