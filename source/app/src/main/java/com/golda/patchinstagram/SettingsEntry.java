package com.golda.patchinstagram;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

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
                addButton((Activity) param.thisObject);
            }
        });
    }

    private static void addButton(Activity activity) {
        ViewGroup root = activity.findViewById(android.R.id.content);
        if (root == null || root.findViewWithTag(TAG) != null) return;
        Button button = new Button(activity);
        button.setTag(TAG);
        button.setText("IGPatch");
        button.setTextSize(12);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2,
                Gravity.TOP | Gravity.END);
        int margin = Math.round(8 * activity.getResources().getDisplayMetrics().density);
        params.setMargins(margin, margin, margin, margin);
        button.setOnClickListener(view -> open(activity));
        root.addView(button, params);
    }

    private static void open(Activity activity) {
        Intent intent = new Intent(activity, OpenInInstagram.class);
        intent.putExtra(ModSettingsActivity.EXTRA, true);
        activity.startActivity(intent);
    }
}
