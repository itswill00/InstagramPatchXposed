package com.golda.patchinstagram;

import android.view.Window;
import android.view.WindowManager;

/** Removes FLAG_SECURE so screenshots and screen recording work everywhere in Instagram. */
final class Screenshots {
    private Screenshots() { }

    static int clearSecure(int flags) {
        return flags & ~WindowManager.LayoutParams.FLAG_SECURE;
    }

    static void install() {
        Hooks.Hook hook = new Hooks.Hook() {
            @Override protected void before(Hooks.Call param) {
                if (!Prefs.on(Prefs.ALLOW_SCREENSHOTS) || param.args.length == 0
                        || !(param.args[0] instanceof Integer)) return;
                int flags = (Integer) param.args[0];
                int cleared = clearSecure(flags);
                if (cleared != flags) param.args[0] = cleared;
            }
        };
        int hooks = Hooks.hookAll(Window.class, "setFlags", hook)
                + Hooks.hookAll(Window.class, "addFlags", hook);
        Hooks.log("screenshots: hooks=" + hooks);
    }
}
