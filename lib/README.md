# Lib Folder

JUnit 4 jars live here. They are **not** committed, because `.gitignore`
excludes `*.jar`, so after cloning this repo you must download them yourself
or the tests will not compile (`The import org.junit cannot be resolved`).

Both files are required. JUnit 4 does not bundle Hamcrest, but
`org.junit.Assert` calls into it at run time, so omitting the second jar gives
a `NoClassDefFoundError: org/hamcrest/SelfDescribing` as soon as a test runs.

```sh
curl -L -O https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar
curl -L -O https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar
```

Compile and run the test suite from the project root:

```sh
javac -cp "lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar" -d bin \
    $(find src test -name '*.java')
java -cp "bin:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar" \
    org.junit.runner.JUnitCore \
    components.DanceFormation1Test components.DanceFormationSecondaryTest
```

This directory becomes unnecessary once the project moves to Spring Boot, as
Maven will declare JUnit as a dependency instead.
