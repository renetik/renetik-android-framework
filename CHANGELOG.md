# Changelog

## 2.0.1 — 2026-07-06

Publishing and repository layout cleanup after the 2.0 unification release:

- Configured JitPack with an explicit `publishToMavenLocal` install command so
  tagged builds publish all module artifacts with the JitPack-provided group
  and version.
- Moved published modules out of the intermediate `library/` folder into the
  repository root module layout.
- Renamed the project from Renetik Android Framework to Renetik Android,
  including Maven group ID, GitHub/JitPack links, API docs paths, sample labels,
  module documentation, workflow references, and verification scripts.
- Added a dedicated release guide and clarified the difference between local
  verification, Git tags, GitHub Releases, and JitPack artifact verification.

## 2.0 — 2026-07-05

Unification of all Renetik Android libraries into this single
repository, with a package reorganization (the `extensions` packages were
dissolved into the domain packages of the types they extend), removal of dead
and unused deprecated API, a `sample/` app whose main screen is a live module
checklist, tests for every published module, and an API docs site published
from Dokka to GitHub Pages.
