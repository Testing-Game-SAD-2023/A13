PERCORSO="$(cd "$(dirname "$0")" && pwd)"
NOME_CLASSE=$1
NOME_PACKAGE=$2
PERCORSO_PACKAGE=$3
NUMERO_LIVELLI=$4

echo $NOME_CLASSE
echo $NOME_PACKAGE
echo $PERCORSO_PACKAGE
echo $PERCORSO

sleep 3
cp -n -r $PERCORSO_PACKAGE $PERCORSO/src/main/java
cd $PERCORSO
echo "$NUMERO_LIVELLI" > "livelli.txt"

echo "$NOME_CLASSE
$NOME_PACKAGE" > "output.txt"

cd ..

cp -n evosuite-1.0.6.jar Prototipo2.0

cp -n evosuite-standalone-runtime-1.0.6.jar Prototipo2.0

cd $PERCORSO

java -jar evosuite-1.0.6.jar

cat  >>pom.txt << EOF
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>$NOME_PACKAGE</groupId>
  <artifactId>$NOME_CLASSE</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>
  <name>$NOME_CLASSE</name>
  <url>http://maven.apache.org</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.13.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>


EOF

mv pom.txt pom.xml

export EVOSUITE="java -jar $(pwd)/evosuite-1.0.6.jar"

mvn compile

echo "iniziamo a generare i test"

echo "Test 1"

$EVOSUITE -class $NOME_PACKAGE.$NOME_CLASSE -projectCP target/classes

echo "facciamo partire il test"

mvn dependency:copy-dependencies

export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:evosuite-tests:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar

javac evosuite-tests/$NOME_PACKAGE/*.java

java org.junit.runner.JUnitCore $NOME_PACKAGE.${NOME_CLASSE}_ESTest

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests

mv evosuite-report I0

mv evosuite-tests I0

sleep 1

echo "Test 2"

$EVOSUITE -class $NOME_PACKAGE.$NOME_CLASSE -projectCP target/classes -Dcriterion=LINE

echo "facciamo partire il test"

mvn dependency:copy-dependencies

export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:evosuite-tests:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar

javac evosuite-tests/$NOME_PACKAGE/*.java

java org.junit.runner.JUnitCore $NOME_PACKAGE.${NOME_CLASSE}_ESTest

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests

mv evosuite-report I1

mv evosuite-tests I1

echo "Test 3"

$EVOSUITE -class $NOME_PACKAGE.$NOME_CLASSE -projectCP target/classes -Dcriterion=BRANCH

echo "facciamo partire il test"

mvn dependency:copy-dependencies

export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:evosuite-tests:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar

javac evosuite-tests/$NOME_PACKAGE/*.java

java org.junit.runner.JUnitCore $NOME_PACKAGE.${NOME_CLASSE}_ESTest

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests

mv evosuite-report I2

mv evosuite-tests I2

echo "Test 4"

$EVOSUITE -class $NOME_PACKAGE.$NOME_CLASSE -projectCP target/classes -Dcriterion=WEAKMUTATION

echo "facciamo partire il test"

mvn dependency:copy-dependencies

export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:evosuite-tests:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar

javac evosuite-tests/$NOME_PACKAGE/*.java

java org.junit.runner.JUnitCore $NOME_PACKAGE.${NOME_CLASSE}_ESTest

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests

mv evosuite-report I3

mv evosuite-tests I3

echo "Test 5"

$EVOSUITE -class $NOME_PACKAGE.$NOME_CLASSE -projectCP target/classes -Dcriterion=OUTPUT

echo "facciamo partire il test"

mvn dependency:copy-dependencies

export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:evosuite-tests:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar

javac evosuite-tests/$NOME_PACKAGE/*.java

java org.junit.runner.JUnitCore $NOME_PACKAGE.${NOME_CLASSE}_ESTest

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests

mv evosuite-report I4

mv evosuite-tests I4

echo "Test 6"

$EVOSUITE -class $NOME_PACKAGE.$NOME_CLASSE -projectCP target/classes -Dcriterion=METHOD

echo "facciamo partire il test"

mvn dependency:copy-dependencies

export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:evosuite-tests:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar

javac evosuite-tests/$NOME_PACKAGE/*.java

java org.junit.runner.JUnitCore $NOME_PACKAGE.${NOME_CLASSE}_ESTest

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests

mv evosuite-report I5

mv evosuite-tests I5

echo "Test 7"

$EVOSUITE -class $NOME_PACKAGE.$NOME_CLASSE -projectCP target/classes -Dcriterion=LINE:BRANCH

echo "facciamo partire il test"

mvn dependency:copy-dependencies

export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:evosuite-tests:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar

javac evosuite-tests/$NOME_PACKAGE/*.java

java org.junit.runner.JUnitCore $NOME_PACKAGE.${NOME_CLASSE}_ESTest

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests

mv evosuite-report I6

mv evosuite-tests I6

echo "Test 8"

$EVOSUITE -class $NOME_PACKAGE.$NOME_CLASSE -projectCP target/classes -Dcriterion=OUTPUT:LINE

echo "facciamo partire il test"

mvn dependency:copy-dependencies

export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:evosuite-tests:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar

javac evosuite-tests/$NOME_PACKAGE/*.java

java org.junit.runner.JUnitCore $NOME_PACKAGE.${NOME_CLASSE}_ESTest

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests

mv evosuite-report I7

mv evosuite-tests I7

echo "Test 9"

$EVOSUITE -class $NOME_PACKAGE.$NOME_CLASSE -projectCP target/classes -Dcriterion=WEAKMUTATION:BRANCH

echo "facciamo partire il test"

mvn dependency:copy-dependencies

export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:evosuite-tests:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar

javac evosuite-tests/$NOME_PACKAGE/*.java

java org.junit.runner.JUnitCore $NOME_PACKAGE.${NOME_CLASSE}_ESTest

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests

mv evosuite-report I8

mv evosuite-tests I8

echo "Test 10"

$EVOSUITE -class $NOME_PACKAGE.$NOME_CLASSE -projectCP target/classes -Dcriterion=WEAKMUTATION:OUTPUT

echo "facciamo partire il test"

mvn dependency:copy-dependencies

export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:evosuite-tests:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar

javac evosuite-tests/$NOME_PACKAGE/*.java

java org.junit.runner.JUnitCore $NOME_PACKAGE.${NOME_CLASSE}_ESTest

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests

mv evosuite-report I9

mv evosuite-tests I9

echo "Test 11"

$EVOSUITE -class $NOME_PACKAGE.$NOME_CLASSE -projectCP target/classes -Dcriterion=CBRANCH:LINE

echo "facciamo partire il test"

mvn dependency:copy-dependencies

export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:evosuite-tests:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar

javac evosuite-tests/$NOME_PACKAGE/*.java

java org.junit.runner.JUnitCore $NOME_PACKAGE.${NOME_CLASSE}_ESTest

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests

mv evosuite-report I10

mv evosuite-tests I10

echo "Test 12"

$EVOSUITE -class $NOME_PACKAGE.$NOME_CLASSE -projectCP target/classes -Dcriterion=LINE:BRANCH:EXCEPTION

echo "facciamo partire il test"

mvn dependency:copy-dependencies

export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:evosuite-tests:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar

javac evosuite-tests/$NOME_PACKAGE/*.java

java org.junit.runner.JUnitCore $NOME_PACKAGE.${NOME_CLASSE}_ESTest

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests

mv evosuite-report I11

mv evosuite-tests I11

echo "Test 13"

$EVOSUITE -class $NOME_PACKAGE.$NOME_CLASSE -projectCP target/classes -Dcriterion=WEAKMUTATION:LINE:OUTPUT

echo "facciamo partire il test"

mvn dependency:copy-dependencies

export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:evosuite-tests:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar

javac evosuite-tests/$NOME_PACKAGE/*.java

java org.junit.runner.JUnitCore $NOME_PACKAGE.${NOME_CLASSE}_ESTest

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests

mv evosuite-report I12

mv evosuite-tests I12

javac generazione_livelli.java

chmod +x generazione_livelli.java
chmod +x generazione_livelli.class
java -cp $PERCORSO generazione_livelli















