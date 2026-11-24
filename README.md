# InstagramPatchXposed

[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/platform-Android%208.1%2B-green.svg)](https://www.android.com/)

An [LSPosed](https://github.com/JingMatrix/LSPosed) module for Instagram (`com.instagram.android`). The codebase is fresh and intentionally small: a stable Xposed backend, a settings store, and JVM-testable feature logic.

> **Status: 0.1.0 — foundation.** The first feature removes tracking parameters from copied Instagram links. More features are planned; see [docs/FEATURES.md](docs/FEATURES.md).

## Contents

- [What it does](#what-it-does)
- [Installation](#installation)
- [Documentation](#documentation)
- [Building](#building)
- [Disclaimer](#disclaimer)

## What it does

| Area | Feature |
| --- | --- |
| Links | Clean copied Instagram links: remove `igsh`, `igshid`, `fbclid`, `utm_*` and similar tracking parameters while keeping useful ones such as `img_index` and URL fragments |

The link parser is plain Java and covered by unit tests. Hook installation is isolated so one failing feature cannot break the module startup.

## Installation

**You need:** Android 8.1+, root and [LSPosed](https://github.com/JingMatrix/LSPosed) with libxposed 101–102 or legacy Xposed support.

1. Build the APK or download a release.
2. Enable the module in LSPosed and tick Instagram.
3. Force-stop Instagram and open it again.

## Documentation

| Document | About |
| --- | --- |
| [Features](docs/FEATURES.md) | Current and planned features |
| [Building](docs/BUILDING.md) | JDK/SDK, `build.sh`, Telegram upload, tests |
| [Troubleshooting](docs/TROUBLESHOOTING.md) | Common problems and limits |
| [Contributing](CONTRIBUTING.md) | Code layout, commits, signoff |
| [Docs index](docs/README.md) | All documents |

## Building

```shell
./build.sh
```

One command runs the unit tests, builds debug and release, uploads new APKs to Telegram and prints a compact summary. See [docs/BUILDING.md](docs/BUILDING.md) for options.

## Disclaimer

> This is an unofficial project, not affiliated with Instagram or Meta. Modifying a client can lead to account restrictions. Use at your own risk.

## License

[MIT](LICENSE) © InstagramPatchXposed contributors.
