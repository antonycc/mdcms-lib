#!/usr/bin/env bash
# Purpose: Run the mvn clean install inside a container to mimic the BitBucketPipeline build
# Performance metrics:
#   Native:
#      mvn clean package -DskipTests -DskipITs
#      190.33s user 29.02s system 111% cpu 3:16.62 total
#   In this container:
#       ./mvn-package-in-docker.sh
#       1.31s user 2.06s system 0% cpu 12:53.97 total
# Usage:
#    ./mvn-package-in-docker.sh

docker run --interactive --tty --mount type=bind,source="$(pwd)",target=/workspace maven:3.6-openjdk-11-slim bash -c 'cd /workspace && mvn package -DskipTests -DskipITs'
