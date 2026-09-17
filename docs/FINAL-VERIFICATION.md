# Final verification sequence

Run from the directory containing `pom.xml`.

## 1. Environment

    java -version
    mvn -version

Expected: JDK 21 and Maven 3.9+.

## 2. Compile

    mvn clean compile

## 3. Unit + TestNG through Surefire

    mvn clean test

Expected: 29 Surefire executions, 0 failures/errors.

## 4. Full Maven lifecycle

    mvn clean verify

Expected: 29 Surefire executions + 4 Failsafe executions, JaCoCo HTML/XML/CSV generated, normal coverage gate passed.

## 5. Native TestNG XML suite

    mvn test-compile exec:java -Ptestng-suite

This is an optional demonstration of native TestNG suite execution. It is deliberately separate from the normal Surefire run.

## 6. Coverage demo — green

    mvn clean verify -Pcoverage-demo

Expected: `BUILD SUCCESS` with the `null_promo_code_returns_zero` test present.

## 7. Coverage demo — red

Temporarily remove only `null_promo_code_returns_zero` from `PromoDiscountCalculatorTest`.

    mvn clean verify -Pcoverage-demo

Expected: `BUILD FAILURE` from `jacoco:check (coverage-demo-check)` for `PromoDiscountCalculator`.

## 8. Restore

Restore the removed test and rerun step 6.

## 9. Report files

    find target/surefire-reports -name 'TEST-*.xml' | sort
    find target/failsafe-reports -name 'TEST-*.xml' | sort
    find target/site/jacoco -maxdepth 1 -type f | sort

Expected files include Surefire XML, Failsafe XML, `index.html`, `jacoco.xml` and `jacoco.csv`.
