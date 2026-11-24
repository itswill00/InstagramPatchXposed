# Contributing

## Workflow

1. Work on `main` unless asked otherwise.
2. Make one logical change per commit.
3. Write commit messages in English, using the imperative mood.
4. Sign every commit:

```shell
git commit -s -m "Add clean link parser"
```

The signoff must match the GitHub identity:

```text
Signed-off-by: itswill00 <anstykx00@gmail.com>
```

Check with `gh api user --jq .login`, `git config user.name`, `git config user.email`.

## Code layout

- Source: `source/app/src/main/java/com/golda/patchinstagram/`
- Tests: `source/app/src/test/java/com/golda/patchinstagram/`
- `Module.java` registers features; `Hooks.java` is the framework-neutral hook layer.
- `Prefs.java` stores settings in Instagram's data directory.
- Feature logic should be plain Java where possible so it can be unit-tested on the JVM.

## Tests and build

```shell
cd source && ./gradlew testDebugUnitTest
./build.sh --no-upload
./build.sh
```

Never commit credentials. Bot token and chat ID belong in `source/local.properties` (gitignored) or environment variables.
