package com.golda.patchinstagram;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.atomic.AtomicBoolean;

/** Registers the module's features once Instagram's Application has attached. */
final class Module {
    private static final String TARGET = "com.instagram.android";
    private static final AtomicBoolean STARTED = new AtomicBoolean();

    private Module() { }

    static boolean targets(String packageName) {
        return TARGET.equals(packageName);
    }

    static void load(String packageName, String processName, ClassLoader loader) {
        if (!targets(packageName)) return;
        Hooks.findAndHook(Application.class, "attach", Context.class, new Hooks.Hook() {
            @Override protected void before(Hooks.Call param) {
                if (param.args[0] instanceof Context) Prefs.load((Context) param.args[0]);
            }

            @Override protected void after(Hooks.Call param) {
                if (!STARTED.compareAndSet(false, true)) return;
                try {
                    CleanLinks.install(loader);
                    Hooks.log("started " + BuildConfig.VERSION_NAME);
                } catch (Throwable error) {
                    Hooks.log("startup failed: " + error);
                }
            }
        });
    }
}
