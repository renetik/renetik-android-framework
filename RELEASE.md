# Release Process

Renetik Android is published by JitPack. A public release version is a Git tag
on this repository, for example `2.0.1`. There is no separate Gradle version
file to edit for publishing.

The published coordinates are:

```gradle
implementation 'com.github.renetik.renetik-android:renetik-android-framework:2.0.1'
```

Use `master-SNAPSHOT` instead of a tag version to consume the latest `master`
commit through JitPack.

## Before Release

1. Make sure the code and docs for the release are committed.
2. Make sure the working tree is clean:

```sh
git status --short --branch
```

3. Update `CHANGELOG.md` with the release version and date.
4. Update README examples if the recommended stable version changes.

The release script refuses to run with uncommitted changes because the tag must
point at an exact, committed repository state.

## Run Release

From the repository root:

```sh
./release.sh 2.0.1
```

The script performs these steps:

1. Runs the full Gradle build:

```sh
./gradlew build --no-daemon --no-configuration-cache
```

2. Publishes all modules to `mavenLocal()` with a temporary smoke-test version:

```sh
./gradlew publishToMavenLocal --no-daemon --no-configuration-cache \
    -Pgroup=com.github.renetik.renetik-android \
    -Pversion=0.0.0-smoke
```

3. Builds and tests the sample app against the local smoke artifacts:

```sh
./gradlew -p sample assembleDebug --no-daemon --no-configuration-cache \
    -PsmokeVersion=0.0.0-smoke
./gradlew -p sample testDebugUnitTest --no-daemon --no-configuration-cache \
    -PsmokeVersion=0.0.0-smoke
```

4. Creates the local Git tag:

```sh
git tag 2.0.1
```

5. Asks whether to push the tag:

```text
Push tag 2.0.1 to origin? [y/N]
```

Answer `y` to publish the release tag to GitHub. JitPack builds the release
from that pushed tag.

If you answer `N`, the tag remains local. Publish it later with:

```sh
git push origin 2.0.1
```

## Verify JitPack

After the tag is pushed, verify all release POMs:

```sh
./verify_jitpack.sh 2.0.1
```

The release script runs this automatically when you answer `y` to the push
prompt. JitPack may need a little time before all artifacts are available; rerun
the verification command if the first attempt races the build.

To verify the current `master-SNAPSHOT` instead of a release tag:

```sh
./verify_jitpack.sh master-SNAPSHOT
```

For snapshots, the verification script also checks that JitPack resolves to the
current local HEAD commit.

## Version Meaning

The version passed to `./release.sh` is the Git tag name. During smoke testing,
the script uses `0.0.0-smoke` only for local `mavenLocal()` artifacts so the
sample app can prove that the published module graph resolves before the real
tag is pushed.
