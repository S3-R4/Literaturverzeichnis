# Copilot instructions for this repository

## Project shape

This repository is a small Java workspace created from the VS Code Java template. The source root is `src`, and compiled output is written to `bin`.

Key files:
- `src/App.java`: the entry-point class with a `main` method.
- `src/QuellenTypen/Audiovisuell.java`: a POJO-style model for an audiovisual source.
- `src/QuellenTypen/PodcastGesamtwerk.java`: a second model class for a podcast/work collection.
- `.vscode/settings.json`: configures `src` as the Java source path and `bin` as the generated output folder.

## Build, test, and lint commands

There is no Gradle, Maven, or other build tool configured in this repository, and there are no automated tests or lint scripts checked in.

Use the Java compiler directly from the repository root:

```bash
javac -d bin $(find src -name '*.java')
```

Run the application entry point from the compiled output:

```bash
java -cp bin App
```

If a new test runner or linter is added later, document the exact commands here and prefer the smallest relevant command for a single file or test case.

## High-level architecture

This project is intentionally simple: it is a model-layer Java project, not a framework app. The main architectural pattern is a set of plain Java classes under `src/QuellenTypen` representing media source records. Those classes mostly store metadata as `String` fields and expose them through getter/setter methods.

The repository does not currently have a service layer, persistence layer, CLI framework, database layer, or UI layer. The app code is currently minimal and acts as a seed or prototype rather than a full application architecture.

When making changes, assume the codebase is organized around domain objects and lightweight Java classes rather than layered application patterns.

## Key conventions specific to this repository

- Keep source files under `src` and align package names with the physical folder structure (`QuellenTypen`).
- Preserve the existing POJO pattern: private fields, explicit `getX()` / `setX()` methods, and simple `String`-based metadata values for fields such as dates, URLs, and categories.
- `bin` is generated output. Do not edit compiled classes there; rebuild from source when needed.
- The project currently uses German domain terminology (`titel`, `beitragsArt`, `abruffDatum`, `herausgebendeOrganisation`), so maintain that naming where the code already follows it unless a deliberate refactor is underway.
- Stub methods like `add()`, `update()`, and `delete()` are placeholders; do not assume there is a full CRUD framework behind them.
- The repository is small and convention-driven rather than highly modular. Prefer small, direct edits that fit the existing class layout instead of introducing new frameworks or architecture layers.
