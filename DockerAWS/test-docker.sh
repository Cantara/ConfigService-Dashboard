#!/bin/bash

set -e

cp -p "../target/$(ls ../target/*SNAPSHOT.jar | grep -v /orig | head -1)" app.jar

docker build -t cantara-configservice-dashboard/configservice-dashboard .
docker run -it --rm -p 8087:8087 --env-file application_override.properties cantara-configservice-dashboard/configservice-dashboard
