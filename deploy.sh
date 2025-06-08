#!/bin/bash

# Check if environment parameter is provided
if [ -z "$1" ]
then
  echo "Usage: ./deploy.sh <dev|test|prod>"
  exit 1
fi

# Set environment profile
PROFILE=$1

# Run Maven package command
echo "Building with profile: $PROFILE"
mvn clean package -P $PROFILE -DskipTests

if [ $? -eq 0 ]; then
  echo "Build successful!"
  echo "You can find the artifact in the target directory."
else
  echo "Build failed!"
  exit 1
fi