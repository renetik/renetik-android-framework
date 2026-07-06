#!/usr/bin/env bash
# Release a renetik-android version:
#   Renetik Android build -> mavenLocal smoke publish -> sample assemble + unit
#   tests -> git tag + push -> GitHub Release -> JitPack verification.
#
# Usage: ./release.sh <version>   e.g. ./release.sh 2.0.1

set -euo pipefail

SCRIPT_DIR="$(CDPATH= cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

VERSION="${1:?Usage: $0 <version>}"
GROUP="com.github.renetik.renetik-android"
SMOKE_VERSION="0.0.0-smoke"

require_command() {
    local command_name="$1"
    if ! command -v "$command_name" >/dev/null 2>&1; then
        echo "Missing required command: $command_name" >&2
        exit 2
    fi
}

require_command git
require_command gh
require_command curl
require_command sed

if ! git diff-index --quiet HEAD --; then
    echo "Working tree is not clean — commit or stash first." >&2
    exit 1
fi

echo "==> Renetik Android build"
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
    echo "==> Creating GitHub Release for $VERSION"
    if gh release view "$VERSION" >/dev/null 2>&1; then
        echo "GitHub Release $VERSION already exists."
    else
        gh release create "$VERSION" --verify-tag --title "$VERSION" \
            --generate-notes --latest
    fi
    echo "==> Verifying JitPack artifacts for $VERSION (may need retries)"
    ./verify_jitpack.sh "$VERSION"
else
    echo "Tag created locally; push and release it later with:"
    echo "  git push origin $VERSION"
    echo "  gh release create $VERSION --verify-tag --title $VERSION --generate-notes --latest"
fi
