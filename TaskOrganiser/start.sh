#!/bin/zsh
export UID=$(id -u)
export GID=$(id -g)
./build.sh
docker-compose up