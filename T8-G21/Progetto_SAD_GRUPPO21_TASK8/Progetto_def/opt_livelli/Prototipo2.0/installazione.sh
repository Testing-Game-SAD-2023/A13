#installiamo i programmi necessari
apt update
apt-get -y install software-properties-common
apt-add-repository universe
apt-get update
apt-get -y install maven openjdk-8-jdk git unzip

sleep 3

echo "le versioni di java e javac sono le seguenti, è necessaria la versione 1.8: "

update-alternatives --set java /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java
update-alternatives --set javac /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/javac

java -version
javac -version

sleep 2

 
wget https://github.com/EvoSuite/evosuite/releases/download/v1.0.6/evosuite-1.0.6.jar

wget https://github.com/EvoSuite/evosuite/releases/download/v1.0.6/evosuite-standalone-runtime-1.0.6.jar

java -jar evosuite-1.0.6.jar

sleep 2

export EVOSUITE="java -jar $(pwd)/evosuite-1.0.6.jar"

$EVOSUITE
