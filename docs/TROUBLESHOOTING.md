# Troubleshooting

## The module is not active

1. Make sure the module is enabled in LSPosed and Instagram is ticked.
2. Force-stop Instagram and open it again.
3. Check LSPosed's module log for `InstagramPatchXposed: started ...`.

## Copied links are not cleaned

The feature only rewrites text placed on the clipboard after the module is active. It does not change links already copied before installation. If Instagram writes the clipboard through a path other than `ClipboardManager.setPrimaryClip`, the hook will not see it; a bug report with the Instagram version is welcome.

## Limitations

- The project is at foundation stage; only clean links is implemented.
- Hooks may need updating after Instagram updates.
- The module is unofficial and not affiliated with Instagram or Meta.

Back to [README](../README.md)
