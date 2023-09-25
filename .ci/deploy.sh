#!/bin/bash

set -ex

export APP_NAME=jwt
source .ci/docker.test.env
bash .ci/docker.sh -h unix:///Users/zhangyan/.lima/docker-amd64/sock/docker.sock -f .ci/Dockerfile -e test -s build/libs/*.jar
