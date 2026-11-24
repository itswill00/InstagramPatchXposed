package com.golda.patchinstagram;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/** Adds an in-app shortcut and swaps the module's settings activity into Instagram's process. */
final class SettingsEntry {
    private static final String HOST = "com.golda.patchinstagram.OpenInInstagram";
    private static final String TAG = "igpatch-settings-button";

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
    }

    private static void addButton(Activity activity) {
        ViewGroup root = activity.findViewById(android.R.id.content);
        if (root == null || root.findViewWithTag(TAG) != null) return;
        float density = activity.getResources().getDisplayMetrics().density;
        int size = Math.round(40 * density);
        int margin = Math.round(12 * density);

        TextView button = new TextView(activity);
        button.setTag(TAG);
        button.setText("IG");
        button.setTextColor(0xFFFFFFFF);
        button.setTextSize(14);
        button.setGravity(Gravity.CENTER);
        button.setElevation(6 * density);

        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.OVAL);
        background.setColor(0xFF8B5CF6);
        background.setStroke(Math.max(1, Math.round(density)), 0xFFFFFFFF);
        button.setBackground(background);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size, Gravity.TOP | Gravity.END);
        params.setMargins(margin, margin, margin, margin);
        button.setOnClickListener(view -> {
            try {
                open(activity);
            } catch (Throwable error) {
                Hooks.log("settings open failed: " + error);
            }
        });
        root.addView(button, params);
    }

    private static void open(Activity activity) {
        Intent intent = new Intent(activity, OpenInInstagram.class);
        intent.putExtra(ModSettingsActivity.EXTRA, true);
        activity.startActivity(intent);
    }
}
