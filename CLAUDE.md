# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

Decompose-Router is a Compose Multiplatform navigation library that wraps [Decompose](https://github.com/arkivanov/Decompose) in a Compose-first, [Conductor](https://github.com/bluelinelabs/Conductor)-inspired API. Targets: Android, WearOS, Desktop (JVM), iOS, JS (browser), and Wasm/JS.

Group/coordinates: `io.github.xxfast`. Published modules: `decompose-router` and `decompose-router-wear`.

## Modules

- `decompose-router` — the library. Multiplatform `commonMain` holds the public API; per-platform source sets (`androidMain`, `iosMain`, `desktopMain`, `jsMain`, `wasmJsMain`) provide `DefaultRouterContext` and `Key` actuals.
- `decompose-router-wear` — Android-only WearOS overlay built on top of `decompose-router` (depends on it via `api(project(":decompose-router"))`). Has a phantom `jvm()` target only as a Dokka workaround.
- `app` — internal demo/integration-test app across all targets. **Not published** (excluded from publishing and API validation). Holds the Android instrumented tests that actually exercise the routers.
- `sample` — a git submodule pointing at the separate [NYTimes-KMP](https://github.com/xxfast/NYTimes-KMP) repo. Not part of this build.

## Common commands

Run via the Gradle wrapper (`./gradlew`). JDK 17+ (CI uses 21).

```bash
# API binary-compatibility check — run before pushing public-API changes
./gradlew apiCheck
# Regenerate the .api dumps after an intentional public API change
./gradlew apiDump

# Per-target test suites (mirrors CI matrix)
./gradlew testAndroidHostTest        # Android host (JVM) unit tests
./gradlew iosSimulatorArm64Test      # iOS simulator (macOS only)
./gradlew jsTest                     # JS browser
./gradlew desktopTest                # Desktop JVM
./gradlew connectedAndroidDeviceTest # Android instrumented tests (needs emulator/device)

# Run the demo app
./gradlew :app:run                   # Desktop
./gradlew :app:jsBrowserRun          # JS in browser
./gradlew :app:wasmJsBrowserRun      # Wasm in browser
# Android: install :app on a device/emulator; iOS: open app/ios/ios.xcodeproj in Xcode

# Documentation
./gradlew dokkaGenerate              # API reference (HTML) into build/dokka/html
```

There is no separate lint step; `apiCheck` (binary-compatibility-validator) is the gating static check in CI alongside the per-target test tasks.

## Where the behavior lives

Most of the real behavior is **integration-tested in `app`, not unit-tested in the library**. When changing router semantics, the tests to run/update are the Android instrumented tests under `app/src/androidDeviceTest/.../app/` (`TestStackRouter`, `TestSlotRouters`, `TestPagesRouters`, `TestNestedRouters`). These require an emulator (`connectedAndroidDeviceTest`) and only run on Android in CI. `commonTest` in the library is minimal.

## Architecture

The library is intentionally small — roughly a hundred lines of core glue over Decompose. Understanding these pieces is enough to work anywhere in it.

**`RouterContext` (`commonMain/.../RouterContext.kt`)** — the central type. It wraps Decompose's `ComponentContext` (via delegation) and adds a `storage` map for retaining router instances. Each route/screen gets its own `RouterContext`. It is passed down the Compose tree through the `LocalRouterContext` composition local. The root must be provided by the host platform: on Android via `ComponentActivity.defaultRouterContext()` provided into `LocalRouterContext` in `MainActivity`; other platforms have their own `defaultRouterContext()` actuals in their source sets.

**Three router flavors**, each a folder under `commonMain/.../router/` with a `Router.kt` (the `rememberRouter*` factory + `Router` class) and a `RoutedContent.kt` (the Composable that renders the current child(ren)):
- `stack/` — a back stack (`push`/`pop`); `RoutedContent` renders the top.
- `slot/` — zero-or-one active child (e.g. dialogs/overlays).
- `pages/` — a pager of children.

Each `Router<C>` wraps a Decompose `*Navigation<C>` (delegating navigation calls to it) plus a Compose `State` of the current Decompose child collection. `Value.asState()` (`State.kt`) bridges Decompose's `Value<T>` to Compose `State<T>`, subscribed to the lifecycle.

**Instance retention — two distinct mechanisms, don't confuse them:**
- `rememberOnRoute(key) { ... }` (`RememberOnRoute.kt`) scopes an arbitrary instance (view model, state holder) to a route using Decompose's `InstanceKeeper`. It survives configuration changes and is cleared when the route leaves the back stack. The `RouterContext` itself is what makes this work per-route.
- `RouterContext.state(initial, ...) { supplier }` (`RouterContext.kt`) survives **process death** by registering a `@Serializable` value with Decompose's `StateKeeper`. Use this for state that must outlive the process, not just config changes.

**Configurations** are the navigation keys: user-defined `@Serializable sealed` types. Serialization is how Decompose persists the stack, hence the `kotlinx.serialization` requirement on configs.

**API surface conventions:** public `rememberRouter` / `rememberOnRoute` come in an `inline reified` form (preferred) and a `@Deprecated` `KClass`-based form kept for migration — keep both in sync when changing signatures, and run `apiDump` for any public change.

## Versioning & publishing

- Versions are centralized in `gradle/libs.versions.toml`. Current version is set in the root `build.gradle.kts` (`allprojects { version = ... }`).
- Publishing uses the `com.vanniktech.maven.publish` plugin, applied to every published subproject from the root `build.gradle.kts` `subprojects {}` block (the `app` is skipped). Two registries: Maven Central via `publishToMavenCentral` (Sonatype Central Portal, `SNAPSHOT`s go to the snapshots repo; releases wait for manual "Publish" on the portal since there's no `automaticRelease`) and GitHub Packages via `publishAllPublicationsToGitHubPackagesRepository`. Artifacts are GPG-signed (`signAllPublications()`) and the plugin wires the Dokka javadoc jar automatically.
- Credentials are Gradle properties, not `local.properties`: `mavenCentralUsername`/`mavenCentralPassword` and `signingInMemoryKey`/`signingInMemoryKeyPassword` for Central; `GITHUB_ACTOR`/`GITHUB_TOKEN` env vars for GitHub Packages. In CI these come from `ORG_GRADLE_PROJECT_*` env vars fed by repo secrets (`MAVEN_CENTRAL_USERNAME`/`MAVEN_CENTRAL_PASSWORD`, `GPG_KEY_SECRET`/`GPG_KEY_PASSWORD`).
- CI (`.github/workflows/build.yml`) runs `apiCheck` → per-target tests + Android emulator tests → (on push to `main`) a `release` matrix job publishing to both registries and a docs job publishing to GitHub Pages.
