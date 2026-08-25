# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: Moderate
* IDE and level of expertise: Moderate

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# Project-specific requirements

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Git

* Use lightweight tags unless the user requests an annotated tag.
* Follow the [se-education Git commit conventions](https://se-education.org/guides/conventions/git.html):
  * **Subject line**: Imperative mood (e.g., `Add ...`, `Refactor ...`, `Extract ...`), concise summary (50 characters or less), no trailing period.
  * **Body**: Separate from subject with a blank line. Explain the context/problem, use the "Let's" syntax to outline changes, and provide rationale for the change.
* Do not commit or push unless explicitly asked.

## Testing and test coverage:

* **Target**: Focus JUnit tests on the top ~50% highest-value methods (prioritizing complex, core, or critical business logic, such as input parsing, persistent storage, command execution, and data models).
* **Maintenance**: JUnit tests must be updated or added after each code change to continuously comply with this 50% coverage target.
* Ensure all tests pass (`./gradlew test`) before considering a coding task complete.

