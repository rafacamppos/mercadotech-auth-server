#!/bin/bash
set -e

PROM_CONTAINER="auth-prometheus"
GRAFANA_CONTAINER="auth-grafana"
NETWORK="auth-network"
PROM_IMAGE="custom-prometheus"
GRAFANA_IMAGE="grafana/grafana-oss:10.4.2"

# create network if it does not exist
if ! docker network ls --format '{{.Name}}' | grep -q "^${NETWORK}$"; then
  docker network create ${NETWORK}
fi

# build prometheus image
docker build -t ${PROM_IMAGE} monitoring/prometheus

# start prometheus container if not running
if [ -z "$(docker ps -q -f name=${PROM_CONTAINER})" ]; then
  if [ "$(docker ps -a -q -f name=${PROM_CONTAINER})" ]; then
    docker rm ${PROM_CONTAINER}
  fi
  docker run -d --name ${PROM_CONTAINER} \
    --network ${NETWORK} \
    -p 9090:9090 \
    ${PROM_IMAGE}
fi

# start grafana container if not running
if [ -z "$(docker ps -q -f name=${GRAFANA_CONTAINER})" ]; then
  if [ "$(docker ps -a -q -f name=${GRAFANA_CONTAINER})" ]; then
    docker rm ${GRAFANA_CONTAINER}
  fi
  docker run -d --name ${GRAFANA_CONTAINER} \
    --network ${NETWORK} \
    -p 3000:3000 \
    ${GRAFANA_IMAGE}
fi

cat <<MSG
Prometheus running at http://localhost:9090
Grafana running at http://localhost:3000 (default credentials admin/admin)
MSG
