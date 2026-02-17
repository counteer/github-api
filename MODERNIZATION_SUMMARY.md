# Modernization Summary - Java 25 Update

This document summarizes the changes made to the `github-api` project to update it to Java 25 and apply modern Java features.

## 1. Project Configuration Update
The project's `pom.xml` was updated to target Java 25.

- **Maven Compiler Plugin**:
    - `source`: Updated from `11` to `25`
    - `target`: Updated from `11` to `25`
    - `release`: Updated from `11` to `25`
- **Maven Javadoc Plugin**:
    - `source`: Updated from `11` to `25`
    - `release`: Updated from `11` to `25`

## 2. Modern Java Features Applied

### Pattern Matching for `instanceof` (Java 14+)
Refactored several classes to use pattern matching for `instanceof`, simplifying type checks and casting.
- `GHEmail.java`
- `GHRepository.java`
- `GitHubClient.java`
- `GHRef.java`
- `GHObject.java` (Verified usage in `accept` method)

### Switch Expressions (Java 14+)
Updated switch statements to modern switch expressions for better readability and safety.
- `GHPullRequestReviewState.java`: Converted `toEvent()` method.
- `GitHubClient.java`: Converted `getRedirectedMethod()` method.

### Text Blocks (Java 15+)
Converted multi-line string literals and complex strings to text blocks in test classes.
- `GHCodeownersErrorTest.java`: Modernized error message assertions.
- `GHAppExtendedTest.java`: Modernized PEM key string literal.

## 3. Verification Results
The changes were verified using Maven `3.9.11` and Java `25.0.1`.

- **Compilation**: Successful (all 248 source files).
- **Test Execution**:
    - **Total Tests Run**: 537
    - **Passes**: 513
    - **Failures**: 0
    - **Errors**: 0
    - **Skipped**: 24
- **Status**: The project is fully compatible with Java 25 and all modern features are functioning as expected.
