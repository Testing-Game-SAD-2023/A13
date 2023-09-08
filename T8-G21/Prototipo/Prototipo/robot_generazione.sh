PERCORSO="$(cd "$(dirname "$0")" && pwd)"
NOME_CLASSE=$1
NOME_PACKAGE=$2

sleep 3

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

echo "iniziamo a generare i test"

$EVOSUITE -class $NOME_PACKAGE.$NOME_CLASSE -projectCP target/classes

echo "facciamo partire i test"

mvn dependency:copy-dependencies

export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:evosuite-tests:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar


javac evosuite-tests/$NOME_PACKAGE/*.java

java org.junit.runner.JUnitCore $NOME_PACKAGE.${NOME_CLASSE}_ESTest

rm $PERCORSO/pom.xml
