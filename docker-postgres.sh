#!/bin/bash
  
docker run --detach \
  --name postgres-db \
  --env POSTGRES_PASSWORD=spring-boot-heroku-example \
  --env POSTGRES_USER=spring-boot-heroku-example \
  --publish 5432:5432 \
  postgres:9.4.4
