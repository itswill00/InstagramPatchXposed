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

## Planned

Areas under consideration, in no particular order:

- hide read receipts and typing indicators;
- save feed/story media without re-encoding;
- remove sponsored posts from the feed;
- exact timestamps;
- interface tweaks (hide elements, accent color).

Nothing planned is enabled or committed yet. Each feature should arrive with a JVM-testable core, a hook registration in `Module.java`, a settings key in `Prefs.java`, and documentation here.

Back to [README](../README.md) · [Building](BUILDING.md)
