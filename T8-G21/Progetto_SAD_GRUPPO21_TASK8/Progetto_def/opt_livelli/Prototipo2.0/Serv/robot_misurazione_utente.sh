#!/bin/sh
PERCORSO="$(cd "$(dirname "$0")" && pwd)"
PERCORSO_PACKAGE=$1
PERCORSO_TEST=$4
PERCORSO_CSV=$5
NOME_CLASSE=$3
NOME_PACKAGE=$2

echo "percorso package :$PERCORSO_PACKAGE \n"
echo "nome package :$NOME_PACKAGE \n"
echo "nome classe :$NOME_CLASSE \n"
echo "percorso test :$PERCORSO_TEST \n"
echo "percorso csv :$PERCORSO_CSV\n"
echo "PERCORSO  :$PERCORSO\n"


cp -n -r $PERCORSO_PACKAGE $PERCORSO/src/main/java

mkdir -p $PERCORSO_TEST
cp -r -f $PERCORSO/src/test/java/Tests/Test$NOME_CLASSE.java $PERCORSO_TEST

cd $PERCORSO

cd ..

cp -n evosuite-1.0.6.jar Prototipo2.0

cp -n evosuite-standalone-runtime-1.0.6.jar Prototipo2.0

cd $PERCORSO

java -jar evosuite-1.0.6.jar

# cat  >>pom.txt << EOF
# <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
#   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
#   <modelVersion>4.0.0</modelVersion>

#   <groupId>$NOME_PACKAGE</groupId>
#   <artifactId>$NOME_CLASSE</artifactId>
#   <version>1.0-SNAPSHOT</version>
#   <packaging>jar</packaging>
#   <name>$NOME_CLASSE</name>
#   <url>http://maven.apache.org</url>

#   <properties>
#     <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
#   </properties>

#   <dependencies>
#     <dependency>
#       <groupId>junit</groupId>
#       <artifactId>junit</artifactId>
#       <version>4.13.1</version>
#       <scope>test</scope>
#     </dependency>
#   </dependencies>
# </project>


# EOF

# mv pom.txt pom.xml

export EVOSUITE="java -jar $(pwd)/evosuite-1.0.6.jar"

mvn clean install

echo "facciamo partire i test"

mvn dependency:copy-dependencies

export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:target/test-classes:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar
#javac target/test-classes/$NOME_PACKAGE/*.java

#java org.junit.runner.JUnitCore $NOME_PACKAGE.${NOME_CLASSE}_test

sleep 2

$EVOSUITE -measureCoverage -class $NOME_CLASSE -Djunit=Test${NOME_CLASSE} -projectCP $PERCORSO/target/classes:$PERCORSO/target/test-classes -Dcriterion=LINE

$EVOSUITE -measureCoverage -class $NOME_CLASSE -Djunit=Test${NOME_CLASSE} -projectCP $PERCORSO/target/classes:$PERCORSO/target/test-classes -Dcriterion=BRANCH

$EVOSUITE -measureCoverage -class $NOME_CLASSE -Djunit=Test${NOME_CLASSE} -projectCP $PERCORSO/target/classes:$PERCORSO/target/test-classes -Dcriterion=EXCEPTION

$EVOSUITE -measureCoverage -class $NOME_CLASSE -Djunit=Test${NOME_CLASSE} -projectCP $PERCORSO/target/classes:$PERCORSO/target/test-classes -Dcriterion=WEAKMUTATION

$EVOSUITE -measureCoverage -class $NOME_CLASSE -Djunit=Test${NOME_CLASSE} -projectCP $PERCORSO/target/classes:$PERCORSO/target/test-classes -Dcriterion=OUTPUT

$EVOSUITE -measureCoverage -class $NOME_CLASSE -Djunit=Test${NOME_CLASSE} -projectCP $PERCORSO/target/classes:$PERCORSO/target/test-classes -Dcriterion=METHOD

$EVOSUITE -measureCoverage -class $NOME_CLASSE -Djunit=Test${NOME_CLASSE} -projectCP $PERCORSO/target/classes:$PERCORSO/target/test-classes -Dcriterion=METHODNOEXCEPTION

$EVOSUITE -measureCoverage -class $NOME_CLASSE -Djunit=Test${NOME_CLASSE} -projectCP $PERCORSO/target/classes:$PERCORSO/target/test-classes -Dcriterion=CBRANCH

cd $PERCORSO

mv -f evosuite-report/statistics.csv $PERCORSO_CSV

#wget http://localhost:3081/api/

#rm -r $PERCORSO/src/test/*
#rm -r $PERCORSO/src/main/java/*
#rm -r $PERCORSO/target
rm -r $PERCORSO/evosuite-report
#rm $PERCORSO/index.html