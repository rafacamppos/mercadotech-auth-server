#!/bin/bash
set -e

CONTAINER_NAME="auth-postgres"
IMAGE="postgres:15"

if [ "$(docker ps -a -q -f name=${CONTAINER_NAME})" ]; then
  echo "Container ${CONTAINER_NAME} already exists"
  exit 0
fi

docker run -d \
  --name ${CONTAINER_NAME} \
  -e POSTGRES_DB=authdb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  ${IMAGE}
