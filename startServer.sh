#!/bin/bash

JAR_FILE="target/Game-1.0.jar"

# Check if the JAR file already exists
if [ -f "$JAR_FILE" ]; then
    # Run the existing JAR file
    java -jar "$JAR_FILE"
else
    # Compile and package the Maven project
    mvn clean package -DskipTests

    # Check if compilation was successful
    if [ $? -eq 0 ]; then
        # Run the Java application
        java -jar "$JAR_FILE"
    else
        echo "Build failed"
    fi
fi