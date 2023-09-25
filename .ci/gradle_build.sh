#!/bin/bash

set -ex

JAVA_IMAGE_NAME=cuilan/javabuild8:hotspot

GRADLE_REPOSITORY=$HOME/.gradle
CONTAINER_GRADLE_CACHE_DIR=/data/gradle_cache

# gradle build
#docker run --rm \
#        --volume .:/work \
#        --volume ${GRADLE_REPOSITORY}:${CONTAINER_GRADLE_CACHE_DIR} ${JAVA_IMAGE_NAME} \
#        /usr/local/bin/cgradle build -p /work/

# gradle clean
#docker run --rm \
#         --volume .:/work \
#         --volume ${GRADLE_REPOSITORY}:${CONTAINER_GRADLE_CACHE_DIR} ${JAVA_IMAGE_NAME} \
#         /usr/local/bin/cgradle clean -p /work/

# gradlew build
docker run --rm \
         --volume .:/work \
         --volume ${GRADLE_REPOSITORY}:${CONTAINER_GRADLE_CACHE_DIR} ${JAVA_IMAGE_NAME} \
         /work/gradlew build -p /work/

# gradlew clean
#docker run --rm \
        # --volume .:/work \
        # --volume ${GRADLE_REPOSITORY}:${CONTAINER_GRADLE_CACHE_DIR} ${JAVA_IMAGE_NAME} \
        # /work/gradlew clean -p /work/
