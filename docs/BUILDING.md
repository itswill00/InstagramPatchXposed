# Building

## Requirements

| Tool | Version |
| --- | --- |
| JDK | 17 |
| Android SDK | 36 (`platforms;android-36`, `build-tools;36.0.0`) |
| Gradle | 8.13 via wrapper |

The Gradle project is in [`source/`](../source). Local settings live in `source/local.properties`, which is gitignored.

## Build

```shell
./build.sh
```

The script runs `testDebugUnitTest`, `assembleDebug`, and `assembleRelease` in one Gradle invocation, then uploads new APKs to Telegram and prints a summary. Unchanged APK hashes are skipped.

Options: `--debug`, `--release`, `--all`, `--no-tests`, `--clean`, `--daemon`, `--no-upload`, `--upload-only`, `--force-upload`, `--strict`, `--help`.

## Telegram upload

Credentials come from `TELEGRAM_BOT_TOKEN` / `TELEGRAM_CHAT_ID` or from `source/local.properties`:

```properties
sdk.dir=/path/to/android-sdk
telegram.botToken=123456:ABC...
telegram.chatId=-1001234567890
```

## Manual Gradle

```shell
cd source
./gradlew testDebugUnitTest assembleDebug assembleRelease
```

APKs:

```text
source/app/build/outputs/apk/debug/app-debug.apk
source/app/build/outputs/apk/release/app-release.apk
```

Tests are plain JUnit4 and need no device or emulator.

Back to [README](../README.md) · [Features](FEATURES.md)
