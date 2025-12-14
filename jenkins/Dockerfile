FROM jenkins/jenkins:lts

USER root

# Установка Docker CLI и docker-compose
RUN apt-get update && \
    apt-get install -y curl && \
    curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-linux-$(uname -m)" -o /usr/local/bin/docker-compose && \
    chmod +x /usr/local/bin/docker-compose && \
    docker-compose --version && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

USER jenkins

