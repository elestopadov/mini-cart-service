# Verification checklist

## 1. Environment

    java -version
    mvn -version

Expected: JDK 21 and Maven 3.9+.

## 2. Compile

    mvn clean compile

Expected: `BUILD SUCCESS`.

## 3. Unit tests

    mvn clean test

Expected: 29 Surefire executions, 0 failures/errors.

## 4. Full lifecycle

    mvn clean verify

Expected: 29 Surefire executions + 4 Failsafe executions, JaCoCo report generated, normal coverage gate passed.

## 5. Native TestNG suite demonstration

    mvn test-compile exec:java -Ptestng-suite

Expected: the configured TestNG XML suite runs independently of the normal Surefire provider.

## 6. Coverage demo

With all tests present:

    mvn clean verify -Pcoverage-demo

Expected: `BUILD SUCCESS`.

Temporarily remove only `null_promo_code_returns_zero` from `PromoDiscountCalculatorTest` and run the same command.

Expected: `BUILD FAILURE` from `jacoco:check (coverage-demo-check)`.

Restore the test and rerun the command. Expected: `BUILD SUCCESS`.

## 7. Reports

    find target/surefire-reports -name 'TEST-*.xml'
    find target/failsafe-reports -name 'TEST-*.xml'
    find target/site/jacoco -maxdepth 1 -name 'index.html'

Expected: all three report areas contain files.
