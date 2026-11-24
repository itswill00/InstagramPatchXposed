# Features

## Current

### Clean copied links

Instagram adds tracking parameters to shared URLs. The module removes them from text copied to the clipboard while leaving useful parameters and URL fragments intact.

Removed keys: `igsh`, `igshid`, `fbclid`, `gclid`, `msclkid`, `mc_cid`, `mc_eid`, `ref`, `ref_src`, `ref_url`, and every `utm_*` key.

Example:

```text
https://www.instagram.com/reel/XYZ/?img_index=2&igshid=abc&fbclid=1
→ https://www.instagram.com/reel/XYZ/?img_index=2
```

### Settings screen

An **InstagramPatchXposed** row is added to Instagram's own settings list, next to the Settings row. It opens a settings activity hosted inside Instagram's process, so toggles write the same preferences the hooks read. LSPosed's module settings button opens the same screen.

### Media downloader

When a direct CDN media URL is copied to the clipboard, the module queues it through Android's `DownloadManager` and saves it under `Download/InstagramPatchXposed`. Post links are ignored; only direct `.jpg`, `.jpeg`, `.png`, `.webp` and `.mp4` URLs are downloaded.

### Privacy and analytics

- **Block location** — `LocationManager` answers with no fix, no providers enabled.
- **Block clipboard reads** — `ClipboardManager.getPrimaryClip()` answers with no data; copied links are still cleaned on write.
- **Allow screenshots** — `FLAG_SECURE` is cleared when Instagram sets window flags.
- **Disable third-party analytics** — Firebase/Facebook collection flags are turned off in the app metadata before the SDKs read it.

## Planned

Areas under consideration, in no particular order:

- hide read receipts and typing indicators;
- save feed/story media without re-encoding;
- remove sponsored posts from the feed;
- exact timestamps;
- interface tweaks (hide elements, accent color).

Nothing planned is enabled or committed yet. Each feature should arrive with a JVM-testable core, a hook registration in `Module.java`, a settings key in `Prefs.java`, and documentation here.

Back to [README](../README.md) · [Building](BUILDING.md)
