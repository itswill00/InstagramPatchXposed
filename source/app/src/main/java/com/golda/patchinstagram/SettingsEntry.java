package com.golda.patchinstagram;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/** Injects a settings row into Instagram's own list and hosts the settings activity in-process. */
final class SettingsEntry {
    private static final String HOST = "com.golda.patchinstagram.OpenInInstagram";
    private static final String TAG = "igpatch-settings-row";

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
                if (!(activity instanceof ModSettingsActivity)) inject(activity);
            }
        });
    }

    private static void inject(Activity activity) {
        View root = activity.findViewById(android.R.id.content);
        if (root == null) return;
        root.postDelayed(() -> {
            try {
                TextView anchor = findSettingsRow(root);
                if (anchor == null) return;
                ViewGroup parent = anchor.getParent() instanceof ViewGroup ? (ViewGroup) anchor.getParent() : null;
                if (parent == null || parent.findViewWithTag(TAG) != null) return;
                TextView row = new TextView(activity);
                row.setTag(TAG);
                row.setText("InstagramPatchXposed");
                row.setTextColor(anchor.getCurrentTextColor());
                row.setTextSize(TypedValue.COMPLEX_UNIT_PX, anchor.getTextSize());
                row.setPadding(anchor.getPaddingLeft(), anchor.getPaddingTop(),
                        anchor.getPaddingRight(), anchor.getPaddingBottom());
                row.setGravity(anchor.getGravity());
                row.setOnClickListener(view -> open(activity));
                parent.addView(row, parent.indexOfChild(anchor));
            } catch (Throwable error) {
                Hooks.log("settings row unavailable: " + error);
            }
        }, 300);
    }

    private static TextView findSettingsRow(View view) {
        if (view instanceof TextView) {
            CharSequence text = ((TextView) view).getText();
            if (text != null) {
                String value = text.toString().trim().toLowerCase();
                if (value.equals("settings") || value.contains("settings and privacy")) return (TextView) view;
            }
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                TextView found = findSettingsRow(group.getChildAt(i));
                if (found != null) return found;
            }
        }
        return null;
    }

    private static void open(Activity activity) {
        Intent intent = new Intent(activity, OpenInInstagram.class);
        intent.putExtra(ModSettingsActivity.EXTRA, true);
        activity.startActivity(intent);
    }
}
