PERCORSO="$(cd "$(dirname "$0")" && pwd)"
PERCORSO_PACKAGE=$1
PERORSO_TEST=$2
PERCORSO_CSV=$3
NOME_CLASSE=$4
NOME_PACKAGE=$5

sleep 3

cp -n -r $PERCORSO_PACKAGE $PERCORSO/src/main/java
cp -n -r $PERCORSO_TEST $PERCORSO/test
cd $PERCORSO

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

echo "facciamo partire i test"

mvn dependency:copy-dependencies

export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:evosuite-tests:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar
javac evosuite-tests/$NOME_PACKAGE/*.java

java org.junit.runner.JUnitCore $NOME_PACKAGE.${NOME_CLASSE}_ESTest

sleep 2

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests -Dcriterion=LINE

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests -Dcriterion=BRANCH

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests -Dcriterion=EXCEPTION

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests -Dcriterion=WEAKMUTATION

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests -Dcriterion=OUTPUT

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests -Dcriterion=METHOD

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests -Dcriterion=METHODNOEXCEPTION

$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:evosuite-tests -Dcriterion=CBRANCH

rm -r $PERCORSO/test/*
rm -r $PERCORSO/src/main/java/*
rm -r $PERCORSO/target
rm -r $PERCORSO/evosuite-report
rm $PERCORSO/pom.xml
