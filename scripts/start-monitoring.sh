#!/bin/bash

echo "📦 Iniciando ambiente Prometheus + Grafana..."

# Cria a rede docker se ainda não existir
docker network inspect monitoring >/dev/null 2>&1 || docker network create monitoring

# Cria pastas necessárias
mkdir -p prometheus grafana/provisioning/datasources

# Cria arquivo Prometheus.yml com scraping da aplicação local na porta 8080
cat > prometheus/prometheus.yml <<EOF
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'prometheus'
    scrape_interval: 5s
    static_configs:
      - targets: ['prometheus:9090']

  - job_name: 'prometheus'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
    static_configs:
      - targets: ['host.docker.internal:8080']
EOF

# Cria datasource do Grafana
cat > grafana/provisioning/datasources/prometheus.yml <<EOF
apiVersion: 1

datasources:
  - name: Prometheus
    type: prometheus
    access: proxy
    url: http://prometheus:9090
    isDefault: true
EOF

# Cria docker-compose.yml
cat > docker-compose.yml <<EOF
version: '3.8'

services:
  prometheus:
    image: prom/prometheus
    container_name: prometheus
    volumes:
      - ./prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
    ports:
      - "9090:9090"
    networks:
      - monitoring

  grafana:
    image: grafana/grafana
    container_name: grafana
    ports:
      - "3000:3000"
    volumes:
      - ./grafana/provisioning:/etc/grafana/provisioning
    networks:
      - monitoring
    environment:
      - GF_SECURITY_ADMIN_USER=admin
      - GF_SECURITY_ADMIN_PASSWORD=admin

networks:
  monitoring:
    driver: bridge
EOF

# Sobe os containers
docker-compose up -d

echo "✅ Prometheus e Grafana em execução!"
echo "🔗 Prometheus: http://localhost:9090"
echo "🔗 Grafana: http://localhost:3000 (login: admin / admin)"