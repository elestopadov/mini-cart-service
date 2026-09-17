# JaCoCo coverage demonstration

The project has two coverage gates.

## Normal gate

Bundle level:
- line coverage >= 80%
- branch coverage >= 70%

Run:

    mvn clean verify

## Teaching demo gate

The `coverage-demo` profile adds a class-level rule for:

    com.example.minicart.service.PromoDiscountCalculator

The rule requires 100% branch coverage.

The class has two conditional decisions:
1. `promoCode == null`
2. `subtotal < promoCode.minAmount()`

The test `null_promo_code_returns_zero` is the only test that executes the null-promo branch. The other two tests cover the valid-promo and minimum-not-met paths.

### Green state

    mvn clean verify -Pcoverage-demo

Expected: `BUILD SUCCESS`.

### Red state

Temporarily remove only `null_promo_code_returns_zero` from `PromoDiscountCalculatorTest` and run the same command.

Expected: `BUILD FAILURE` from `jacoco:check (coverage-demo-check)`, because the target class falls below 100% branch coverage.

### Restore

Restore the test and run the same command again.

Expected: `BUILD SUCCESS`.
