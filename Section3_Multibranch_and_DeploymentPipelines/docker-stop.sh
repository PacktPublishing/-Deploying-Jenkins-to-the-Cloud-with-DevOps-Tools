#!/usr/bin/env bash

# Starts all applications. Run 'mvn docker:build' in platform and webapp before this script to create docker images.

# Remove existing containers
docker-compose stop
