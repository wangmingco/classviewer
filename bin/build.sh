#!/usr/bin/env bash

cd ..

mvn install:install-file -Dfile=/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/lib/tools.jar -DgroupId=com.sun -DartifactId=tools -Dversion=1.8.0_144 -Dpackaging=jar
mvn install:install-file -Dfile=/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/lib/sa-jdi.jar -DgroupId=com.sun -DartifactId=sa-jdi -Dversion=1.8.0_144 -Dpackaging=jar
mvn install:install-file -Dfile=/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre/lib/ext/jfxrt.jar -DgroupId=com.sun -DartifactId=jfxrt -Dversion=1.8.0_144 -Dpackaging=jar

mvn clean compile package -Pmac
cp -r ./target/classviewer-1.0.jar ./src/main/resources

mvn clean compile package -Pmac assembly:single
