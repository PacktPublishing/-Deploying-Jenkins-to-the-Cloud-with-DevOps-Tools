#!/usr/bin/env bash

export COMPOSE_PROJECT_NAME=packt

# Remove existing containers
docker-compose down --volumes
docker stop $(docker ps -aq --filter name=gitlab_)
docker rm --volumes -f $(docker ps -aq --filter name=gitlab_)
docker stop jenkins_server
docker rm --volumes -f jenkins_server

# add predfined gitlab config
mkdir -p docker/container/
rm -rf docker/container/*
unzip config/gitlab_data.zip -d docker/container/

## Start new containers
docker-compose up --build
