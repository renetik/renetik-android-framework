#!/usr/bin/env bash
# Build the full documentation site locally into build/site:
#   - docs/index.html  -> site root (curated, ordered landing page)
#   - Dokka multi-module HTML -> site/api
# Open build/site/index.html to preview exactly what GitHub Pages serves.

set -euo pipefail
SCRIPT_DIR="$(CDPATH= cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

./gradlew :dokkaGenerate --no-daemon --no-configuration-cache

rm -rf build/site
mkdir -p build/site
cp -R docs/. build/site/
mkdir -p build/site/api
cp -R build/dokka/html/. build/site/api/

echo "Docs site assembled at: build/site/index.html"
