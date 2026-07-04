#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(CDPATH= cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
SCRIPT_NAME="$(basename "$0")"
JITPACK_BASE_URL="https://jitpack.io"
GROUP_ID="com.github.renetik.renetik-android-framework"
GROUP_PATH="${GROUP_ID//.//}"
DEFAULT_VERSION="master-SNAPSHOT"
ARTIFACTS=(
    renetik-android-testing
    renetik-android-core
    renetik-android-core-leakcanary
    renetik-android-event
    renetik-android-json
    renetik-android-store
    renetik-android-preset
    renetik-android-ui
    renetik-android-ui-picker
    renetik-android-material
    renetik-android-imaging
    renetik-android-controller
    renetik-android-framework
    renetik-android-testing-ui
)
TEMP_PATHS=()

usage() {
    cat <<EOF
Usage: $SCRIPT_NAME [version] [artifact ...]

Verifies JitPack POM availability for Renetik Android Framework artifacts.
When version ends with -SNAPSHOT, the JitPack redirect must point to the
current local framework HEAD commit. For release versions, the POM only needs
to be available with the expected group and artifact.

Examples:
  $SCRIPT_NAME
  $SCRIPT_NAME 2.0
  $SCRIPT_NAME master-SNAPSHOT renetik-android-core renetik-android-event
EOF
}

cleanup() {
    if [[ ${#TEMP_PATHS[@]} -gt 0 ]]; then
        rm -rf "${TEMP_PATHS[@]}" 2>/dev/null || true
    fi
}
trap cleanup EXIT

new_temp_file() {
    local file
    file="$(mktemp)"
    TEMP_PATHS+=("$file")
    echo "$file"
}

require_command() {
    local command_name="$1"
    if ! command -v "$command_name" >/dev/null 2>&1; then
        echo "Missing required command: $command_name" >&2
        exit 2
    fi
}

short_commit() {
    local commit="$1"
    printf '%.10s' "$commit"
}

xml_tag_value() {
    local tag="$1"
    local file="$2"
    sed -n "s:.*<$tag>\\([^<]*\\)</$tag>.*:\\1:p" "$file" | head -n 1
}

commit_from_effective_pom_url() {
    local artifact="$1"
    local version="$2"
    local effective_url="$3"
    local snapshot_prefix="${version%-SNAPSHOT}"
    local file_name rest commit

    file_name="${effective_url##*/}"
    rest="${file_name#"$artifact-$snapshot_prefix-"}"

    if [[ "$rest" == "$file_name" ]]; then
        return
    fi

    commit="${rest%%-*}"
    if [[ "$commit" =~ ^[0-9a-f]{10}$ ]]; then
        echo "$commit"
    fi
}

check_pom() {
    local artifact="$1"
    local version="$2"
    local expected_short="${3:-}"
    local pom_url="$JITPACK_BASE_URL/$GROUP_PATH/$artifact/$version/$artifact-$version.pom"
    local pom_file error_file effective_url_file effective_url pom_group pom_artifact pom_commit

    pom_file="$(new_temp_file)"
    error_file="$(new_temp_file)"
    effective_url_file="$(new_temp_file)"

    echo "Checking POM: $GROUP_ID:$artifact:$version"
    echo "  $pom_url"

    if ! curl -fL --connect-timeout 20 --max-time 300 -sS "$pom_url" \
        -o "$pom_file" -w "%{url_effective}" >"$effective_url_file" 2>"$error_file"; then
        echo "FAIL $artifact POM cannot be downloaded"
        cat "$error_file"
        return 1
    fi

    effective_url="$(cat "$effective_url_file")"
    pom_group="$(xml_tag_value groupId "$pom_file")"
    pom_artifact="$(xml_tag_value artifactId "$pom_file")"

    if [[ "$pom_group" != "$GROUP_ID" || "$pom_artifact" != "$artifact" ]]; then
        echo "FAIL $artifact downloaded POM is $pom_group:$pom_artifact"
        return 1
    fi

    if [[ "$version" == *-SNAPSHOT ]]; then
        pom_commit="$(commit_from_effective_pom_url "$artifact" "$version" "$effective_url")"
        if [[ -z "$pom_commit" ]]; then
            echo "FAIL $artifact snapshot POM redirect does not contain a JitPack commit"
            echo "     effective URL: $effective_url"
            return 1
        fi
        if [[ "$pom_commit" != "$expected_short" ]]; then
            echo "FAIL $artifact snapshot POM points to $pom_commit, expected HEAD $expected_short"
            echo "     effective URL: $effective_url"
            return 1
        fi
        echo "OK   $artifact:$version points to HEAD $expected_short"
    else
        echo "OK   $artifact:$version POM is available"
    fi
}

version="$DEFAULT_VERSION"
if [[ $# -gt 0 ]]; then
    case "$1" in
        -h|--help)
            usage
            exit 0
            ;;
        -*)
            echo "Unknown option: $1" >&2
            usage >&2
            exit 2
            ;;
        *)
            version="$1"
            shift
            ;;
    esac
fi

if [[ $# -gt 0 ]]; then
    ARTIFACTS=("$@")
fi

require_command git
require_command curl
require_command sed

cd "$SCRIPT_DIR"

expected_short=""
if [[ "$version" == *-SNAPSHOT ]]; then
    expected_short="$(short_commit "$(git rev-parse HEAD)")"
    echo "Expected JitPack snapshot commit: $expected_short"
fi

failures=0
for artifact in "${ARTIFACTS[@]}"; do
    if ! check_pom "$artifact" "$version" "$expected_short"; then
        failures=$((failures + 1))
    fi
done

if [[ "$failures" -eq 0 ]]; then
    echo "All ${#ARTIFACTS[@]} JitPack POM checks passed for $version."
    exit 0
fi

echo "$failures JitPack POM check(s) failed for $version."
exit 1
