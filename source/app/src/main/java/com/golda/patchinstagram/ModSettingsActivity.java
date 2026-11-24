package com.golda.patchinstagram;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

/** Settings screen hosted inside Instagram's process, so it edits the same preferences. */
public final class ModSettingsActivity extends Activity {
    static final String EXTRA = "com.golda.patchinstagram.open_settings";

    private static final String[][] SETTINGS = {
            {Prefs.CLEAN_LINKS, "Clean copied links", "Remove tracking parameters from copied Instagram links"},
            {Prefs.DOWNLOAD_MEDIA, "Download copied media", "Save direct CDN media URLs to Download/InstagramPatchXposed"},
            {Prefs.NO_TRACKERS, "Block trackers", "Disable Firebase and Facebook analytics metadata"},
            {Prefs.BLOCK_LOCATION, "Block location", "Answer location APIs with no data"},
            {Prefs.BLOCK_CLIPBOARD_READ, "Block clipboard reads", "Prevent Instagram from reading the clipboard"},
            {Prefs.ALLOW_SCREENSHOTS, "Allow screenshots", "Clear FLAG_SECURE so screenshots work"}
    };

    @Override protected void onCreate(Bundle saved) {
        setTheme(android.R.style.Theme_Material_Light);
        super.onCreate(saved);
        setTitle("InstagramPatchXposed");
        ScrollView scroll = new ScrollView(this);
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(16);
        list.setPadding(pad, pad, pad, pad);
        TextView title = new TextView(this);
        title.setText("InstagramPatchXposed");
        title.setTextSize(20);
        title.setPadding(0, 0, 0, dp(12));
        list.addView(title);
        for (String[] setting : SETTINGS) list.addView(row(setting[0], setting[1], setting[2]));
        scroll.addView(list, new ViewGroup.LayoutParams(-1, -1));
        setContentView(scroll);
        Hooks.log("settings screen opened");
    }

    private LinearLayout row(String key, String label, String description) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, dp(8), 0, dp(8));
        LinearLayout texts = new LinearLayout(this);
        texts.setOrientation(LinearLayout.VERTICAL);
        TextView name = new TextView(this);
        name.setText(label);
        name.setTextSize(16);
        TextView hint = new TextView(this);
        hint.setText(description);
        hint.setTextSize(12);
        texts.addView(name);
        texts.addView(hint);
        Switch toggle = new Switch(this);
        toggle.setChecked(Prefs.on(key));
        toggle.setOnCheckedChangeListener((button, checked) -> Prefs.set(key, checked));
        row.addView(texts, new LinearLayout.LayoutParams(0, -2, 1f));
        row.addView(toggle);
        return row;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
