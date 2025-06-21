#!/bin/bash
set -e

CONTAINER_NAME="auth-redis"
IMAGE="redis:7"

if [ "$(docker ps -a -q -f name=${CONTAINER_NAME})" ]; then
  echo "Container ${CONTAINER_NAME} already exists"
  exit 0
fi

docker run -d \
  --name ${CONTAINER_NAME} \
  -p 6379:6379 \
  ${IMAGE}
