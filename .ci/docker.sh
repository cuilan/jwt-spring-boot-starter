#!/bin/bash

set -ex

PROFILE=${PROFILE:-"test"}
JAVA_OPTS=${JAVA_OPTS:-""}
SRC_PATH=${SRC_PATH:-"target/*.jar"}
BUILD_HOST=${BUILD_HOST:-"unix:///var/run/docker.sock"}
DOCKERFILE=${DOCKERFILE:-"./Dockerfile"}

while getopts "e:o:s:h:f:" OPT; do
    case $OPT in
    e)
        PROFILE=$OPTARG
        ;;
    o)
        JAVA_OPTS=$OPTARG
        ;;
    s)
        SRC_PATH=$OPTARG
        ;;
    h)
        BUILD_HOST=$OPTARG
        ;;
    f)
        DOCKERFILE=$OPTARG
        ;;
    *) ;;
    esac
done

# build
cat ${DOCKERFILE} |
    docker -H ${BUILD_HOST} build -t ${IMAGE_NAME} --build-arg PROFILE="${PROFILE}" \
        --build-arg JAVA_OPTS="${JAVA_OPTS}" \
        --build-arg SRC_PATH="${SRC_PATH}" -f - .

# login
#docker -H ${BUILD_HOST} login ${REG_HOST} -u ${REG_USER} -p ${REG_PWD}
# tag
docker -H ${BUILD_HOST} tag ${IMAGE_NAME} ${LATEST_IMAGE_NAME}
# push
#docker -H ${BUILD_HOST} push ${IMAGE_NAME}
#docker -H ${BUILD_HOST} push ${LATEST_IMAGE_NAME}
#docker -H ${BUILD_HOST} rmi ${IMAGE_NAME}
#docker -H ${BUILD_HOST} rmi ${LATEST_IMAGE_NAME}

echo "Build image success --> ${IMAGE_NAME}"
