# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v20.4.3-1.20.4] - 2024-03-08
### Added
- Add support for defining custom min & max enchantment cost calculations provided by a custom math expression parser

## [v20.4.2-1.20.4] - 2024-03-02
### Fixed
- Fix mod enchantment data not being available in a newly created world

## [v20.4.1-1.20.4] - 2024-03-02
### Changed
- Move packages other than `api` to dedicated `impl` sub-package
- Simplify some api method names
### Fixed
- Fix custom `EnchantmentCategory` instances occasionally being registered too late

## [v20.4.0-1.20.4] - 2024-03-01
- Initial release
