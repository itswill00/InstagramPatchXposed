package com.golda.patchinstagram;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

/** Saves a direct media URL copied to the clipboard into Download/InstagramPatchXposed. */
final class MediaDownloader {
    private static volatile Context context;

    private MediaDownloader() { }

    static void attach(Context value) {
        context = value == null ? null : value.getApplicationContext();
    }

    static void install() {
        Hooks.hookAll(ClipboardManager.class, "setPrimaryClip", new Hooks.Hook() {
            @Override protected void after(Hooks.Call param) {
                if (!Prefs.on(Prefs.DOWNLOAD_MEDIA) || param.args.length == 0
                        || !(param.args[0] instanceof ClipData)) return;
                ClipData data = (ClipData) param.args[0];
                if (data.getItemCount() == 0) return;
                CharSequence text = data.getItemAt(0).getText();
                String url = MediaUrl.find(text == null ? "" : text.toString());
                if (url == null) return;
                save(url);
            }
        });
    }

    private static void save(String url) {
        Context app = context;
        if (app == null) return;
        try {
            DownloadManager manager = (DownloadManager) app.getSystemService(Context.DOWNLOAD_SERVICE);
            if (manager == null) return;
            String name = MediaUrl.name(url);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle(name);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                    "InstagramPatchXposed/" + name);
            manager.enqueue(request);
            Hooks.log("download queued: " + name);
        } catch (Throwable error) {
            Hooks.log("download failed: " + error);
        }
    }
}
