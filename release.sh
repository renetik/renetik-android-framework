#!/usr/bin/env bash
# Release a renetik-android-framework version:
#   framework build -> mavenLocal smoke publish -> sample assemble + unit
#   tests -> git tag + push -> JitPack verification.
#
# Usage: ./release.sh <version>   e.g. ./release.sh 2.0

set -euo pipefail

SCRIPT_DIR="$(CDPATH= cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

VERSION="${1:?Usage: $0 <version>}"
GROUP="com.github.renetik.renetik-android-framework"
SMOKE_VERSION="0.0.0-smoke"

if ! git diff-index --quiet HEAD --; then
    echo "Working tree is not clean — commit or stash first." >&2
    exit 1
fi

echo "==> Framework build"
./gradlew build --no-daemon --no-configuration-cache

echo "==> Publish smoke artifacts to mavenLocal"
./gradlew publishToMavenLocal --no-daemon --no-configuration-cache \
    -Pgroup="$GROUP" -Pversion="$SMOKE_VERSION"

echo "==> Sample assemble + unit tests"
./gradlew -p sample assembleDebug --no-daemon --no-configuration-cache \
    -PsmokeVersion="$SMOKE_VERSION"
./gradlew -p sample testDebugUnitTest --no-daemon --no-configuration-cache \
    -PsmokeVersion="$SMOKE_VERSION"

echo "==> Tagging $VERSION"
git tag "$VERSION"

read -r -p "Push tag $VERSION to origin? [y/N] " answer
if [[ "$answer" =~ ^[Yy]$ ]]; then
    git push origin "$VERSION"
    echo "==> Verifying JitPack artifacts for $VERSION (may need retries)"
    ./verify_jitpack.sh "$VERSION"
else
    echo "Tag created locally; push it later with: git push origin $VERSION"
fi
