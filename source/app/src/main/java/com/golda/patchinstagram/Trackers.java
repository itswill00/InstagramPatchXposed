package com.golda.patchinstagram;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;

/** Disables third-party analytics metadata before the SDKs read it during app startup. */
final class Trackers {
    private Trackers() { }

    static void install() {
        Hooks.Hook hook = new Hooks.Hook() {
            @Override protected void after(Hooks.Call param) {
                if (!Prefs.on(Prefs.NO_TRACKERS)) return;
                Object result = param.getResult();
                if (!(result instanceof ApplicationInfo)) return;
                ApplicationInfo info = (ApplicationInfo) result;
                Bundle meta = info.metaData == null ? new Bundle() : new Bundle(info.metaData);
                meta.putBoolean("firebase_analytics_collection_deactivated", true);
                meta.putBoolean("firebase_analytics_collection_enabled", false);
                meta.putBoolean("google_analytics_adid_collection_enabled", false);
                meta.putBoolean("firebase_performance_collection_deactivated", true);
                meta.putBoolean("com.facebook.sdk.AutoLogAppEventsEnabled", false);
                meta.putBoolean("com.facebook.sdk.AdvertiserIDCollectionEnabled", false);
                meta.putBoolean("com.facebook.sdk.AttributionCollectionEnabled", false);
                ApplicationInfo copy = new ApplicationInfo(info);
                copy.metaData = meta;
                param.setResult(copy);
            }
        };
        Class<?> manager = Hooks.findClass("android.app.ApplicationPackageManager", null);
        int hooks = manager == null ? 0 : Hooks.hookAll(manager, "getApplicationInfo", hook);
        Hooks.log("trackers: metadata hooks=" + hooks);
    }
}
