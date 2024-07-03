# Changelog

## [1.1.1] - 2024-07-03

### Fixed

- The `java.class.path` property containing `/` separators on Windows while Forge expects `\ `

## [1.1.0] - 2024-06-09

### Changed

- The `java.class.path` property will now be replaced to contain the *filtered* classpath
  without any classpath entries that are also in unions (#1 by @Jab125)
