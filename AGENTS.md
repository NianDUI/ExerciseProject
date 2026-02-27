# Repository Guidelines

## Project Structure & Module Organization
This repository is a Maven-based Spring Boot service (`top.niandui:fictionweb`) targeting Java 21.
- `src/main/java/top/niandui`: application code (controllers, services, DAOs, models, scheduled tasks, utils).
- `src/main/resources`: runtime assets and config (`application.yml`, `config/*.yml`, `mapper/*.xml`, `templates/`, `static/`).
- `src/test/java`: JUnit and integration-style tests (Spring Boot, crawler, Redis, DB, Selenium experiments).
- `sql/`: versioned SQL snapshots and migration scripts.
- `config/`: local override examples for environment-specific settings.

## Build, Test, and Development Commands
Use Maven from the repository root:
- `mvn clean compile` - compile all source code.
- `mvn spring-boot:run` - start the app with active profile from `application.yml` (default `dev`).
- `mvn test -DskipTests=false` - run tests explicitly (important: `pom.xml` sets `skipTests=true` by default).
- `mvn clean package -DskipTests=false` - build executable artifact and run test phase.

## Coding Style & Naming Conventions
- Follow Java conventions: 4-space indentation, `UpperCamelCase` classes, `lowerCamelCase` fields/methods, `UPPER_SNAKE_CASE` constants.
- Keep package boundaries aligned with layers (`controller`, `service`, `dao`, `model`, `config`).
- MyBatis mappings should stay synchronized: `src/main/resources/mapper/*-mapper.xml` mirrors DAO interfaces.
- Prefer clear, domain-oriented names (for example, `BookServiceImpl`, `ChapterSearchVO`).

## Testing Guidelines
- Primary framework: `spring-boot-starter-test` (JUnit 5).
- Place tests in mirrored package paths under `src/test/java`.
- Name test classes with `*Test` or `*Tests` suffix (for example, `FictionwebApplicationTests`).
- No enforced coverage gate is configured; at minimum, cover changed service logic and mapper interactions before merging.

## Commit & Pull Request Guidelines
- Recent history shows short, focused commit subjects, often in Chinese, usually describing one change (for example, `日志打印调整`, `获取nextUrl适配`).
- Keep commit messages concise and scoped to a single intent; avoid mixing refactor and feature work.
- PRs should include:
  - what changed and why,
  - affected modules/config files,
  - verification steps (commands run, sample endpoints/screenshots for UI/template changes),
  - linked issue/task ID when available.

## Security & Configuration Tips
- Do not commit real secrets in `application-*.yml`; use local overrides or environment variables.
- Review SQL scripts before execution and keep new files timestamped (for example, `sql/fiction_cmd_YYYYMMDD.sql`).
