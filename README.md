# Mini E-commerce Cart Service

Educational Java 21 Maven project demonstrating a complete testing layer and Jenkins Freestyle reporting workflow.

## Technology stack

JDK 21 LTS, Maven 3.9+, JUnit Jupiter 5.14.4, TestNG 7.12.0, Mockito 5.23.0, AssertJ 3.27.7, Jackson 2.21.5, Lombok 1.18.48, Surefire 3.6.0, Failsafe 3.6.0, JaCoCo 0.8.15, Maven Enforcer 3.6.3.


## Project structure:
```
├── docs
│   ├── BUSINESS-RULES.md
│   ├── COVERAGE-DEMO.md
│   ├── TESTING-CHECKLIST.md
│   └── VERSION-MATRIX.md
├── pom.xml
├── README.md
├── scripts
│   └── verify-linux.sh
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── minicart
    │   │               ├── exception
    │   │               │   ├── CartNotFoundException.java
    │   │               │   ├── EmptyCartException.java
    │   │               │   ├── InvalidPromoCodeException.java
    │   │               │   └── ProductNotFoundException.java
    │   │               ├── model
    │   │               │   ├── CartItem.java
    │   │               │   ├── Cart.java
    │   │               │   ├── Category.java
    │   │               │   ├── Product.java
    │   │               │   └── PromoCode.java
    │   │               ├── repository
    │   │               │   ├── CartRepository.java
    │   │               │   ├── dto
    │   │               │   │   ├── CartDto.java
    │   │               │   │   └── CartItemDto.java
    │   │               │   ├── FileCartRepository.java
    │   │               │   └── InMemoryCartRepository.java
    │   │               └── service
    │   │                   ├── CartService.java
    │   │                   ├── DiscountService.java
    │   │                   ├── PromoDiscountCalculator.java
    │   │                   └── TaxService.java
    │   └── resources
    │       └── logback.xml
    └── test
        ├── java
        │   └── com
        │       └── example
        │           └── minicart
        │               ├── integration
        │               │   ├── CartServiceIT.java
        │               │   └── FileCartRepositoryIT.java
        │               └── unit
        │                   ├── CartServiceUnitTest.java
        │                   ├── DiscountServiceTest.java
        │                   ├── DiscountServiceTestNGTest.java
        │                   ├── InMemoryCartRepositoryTest.java
        │                   ├── ModelValidationTest.java
        │                   ├── PromoDiscountCalculatorTest.java
        │                   ├── TaxServiceTest.java
        │                   └── TestData.java
        └── resources
            ├── fixtures
            │   └── products.json
            └── testng-suite.xml

```
## Commands

    mvn clean compile
    mvn clean test
    mvn clean verify
    mvn clean verify -Pcoverage-demo
    mvn test-compile exec:java -Ptestng-suite

## Test counts

`mvn clean test` executes 21 JUnit test methods plus 8 TestNG data-provider invocations: 29 Surefire executions.

`mvn clean verify` additionally executes 4 integration-test methods through Failsafe: 33 total executions.

## Reports

- Surefire XML: `target/surefire-reports/`
- Failsafe XML: `target/failsafe-reports/`
- JaCoCo HTML: `target/site/jacoco/index.html`
- JaCoCo XML: `target/site/jacoco/jacoco.xml`
- JaCoCo CSV: `target/site/jacoco/jacoco.csv`

## Coverage

Normal bundle gate: 80% line and 70% branch.

The `coverage-demo` profile adds a strict 100% branch gate for `PromoDiscountCalculator` so students can demonstrate a real failing quality gate by removing the unique null-promo branch test.

See `docs/` for the release audit, version matrix, test plan, coverage demo and Jenkins configuration.
