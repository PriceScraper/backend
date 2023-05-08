# PriceScraper Backend

[![SonarCloud](https://github.com/PriceScraper/backend/actions/workflows/build.yml/badge.svg)](https://github.com/PriceScraper/backend/actions/workflows/build.yml)
[![Conventional Commits](https://github.com/PriceScraper/backend/actions/workflows/convential-commits-action.yml/badge.svg)](https://github.com/PriceScraper/backend/actions/workflows/convential-commits-action.yml)

A web scraper to keep track of item prices in supermarkets.
Create a list of items to find the cheapest supermarket for your needs.

### Project information

**Java version:** 17\
**Spring version:** Spring boot 3\
**Architecture:** Hexagonal\
**Modules:**

- Project
    - Application
    - Core
    - Adapters
        - Incoming
            - REST
        - Outgoing
            - Jpa
            - Scrapers
        - Matchers

### Usage

#### Running the project

> mvnw.cmd spring-boot:run

#### Running tests & checking code quality

> mvnw.cmd verify