package com.golda.patchinstagram;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/** Adds a clickable panel shortcut above Instagram's own content and hosts the settings activity. */
final class SettingsEntry {
    private static final String HOST = "com.golda.patchinstagram.OpenInInstagram";
    private static final Map<Activity, View> BUTTONS = Collections.synchronizedMap(new WeakHashMap<>());

    private SettingsEntry() { }

    static void installHost() {
        Hooks.hookAll(Instrumentation.class, "newActivity", new Hooks.Hook() {
            @Override protected void before(Hooks.Call param) {
                if (param.args.length != 3 || !(param.args[2] instanceof Intent)) return;
                if (HOST.equals(param.args[1]) && ((Intent) param.args[2]).getBooleanExtra(ModSettingsActivity.EXTRA, false)) {
                    param.setResult(new ModSettingsActivity());
                }
            }
        });
    }

    static void install(Application app) {
        Hooks.hookAll(Activity.class, "onResume", new Hooks.Hook() {
            @Override protected void after(Hooks.Call param) {
                if (!(param.thisObject instanceof Activity)) return;
                Activity activity = (Activity) param.thisObject;
                if (!(activity instanceof ModSettingsActivity)) addButton(activity);
            }
        });
        Hooks.hookAll(Activity.class, "onDestroy", new Hooks.Hook() {
            @Override protected void before(Hooks.Call param) {
                if (param.thisObject instanceof Activity) removeButton((Activity) param.thisObject);
            }
        });
    }

    private static void addButton(Activity activity) {
        if (BUTTONS.containsKey(activity)) return;
        float density = activity.getResources().getDisplayMetrics().density;
        int size = Math.round(44 * density);
        int margin = Math.round(14 * density);

        TextView button = new TextView(activity);
        button.setText("IG");
        button.setTextColor(0xFFFFFFFF);
        button.setTextSize(14);
        button.setGravity(Gravity.CENTER);
        button.setClickable(true);
        button.setFocusable(true);
        button.setElevation(8 * density);

        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.OVAL);
        background.setColor(0xFF8B5CF6);
        background.setStroke(Math.max(1, Math.round(density)), 0xFFFFFFFF);
        button.setBackground(background);
        button.setOnClickListener(view -> {
            try {
                open(activity);
            } catch (Throwable error) {
                Hooks.log("settings open failed: " + error);
            }
        });

        WindowManager manager = activity.getWindowManager();
        View decor = activity.getWindow() == null ? null : activity.getWindow().getDecorView();
        if (manager == null || decor == null) return;
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(size, size,
                WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        params.token = decor.getWindowToken();
        params.gravity = Gravity.TOP | Gravity.END;
        params.x = margin;
        params.y = margin;
        try {
            manager.addView(button, params);
            BUTTONS.put(activity, button);
        } catch (Throwable error) {
            Hooks.log("settings button unavailable: " + error);
        }
    }

    private static void removeButton(Activity activity) {
        View button = BUTTONS.remove(activity);
        if (button == null) return;
        try {
            activity.getWindowManager().removeViewImmediate(button);
        } catch (Throwable ignored) { }
    }

    private static void open(Activity activity) {
        Intent intent = new Intent(activity, OpenInInstagram.class);
        intent.putExtra(ModSettingsActivity.EXTRA, true);
        activity.startActivity(intent);
    }
}
