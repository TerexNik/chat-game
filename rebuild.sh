#!/bin/bash

JAR_FILE="target/Game-1.0.jar"

# Compile and package the Maven project
mvn clean package -DskipTests

# Check if compilation was successful
if [ $? -eq 0 ]; then
    # Run the Java application
    java -jar "$JAR_FILE"
else
    echo "Build failed"
fi