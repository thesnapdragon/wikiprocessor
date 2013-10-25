#!/bin/bash

until node /home/cloud/Parsoid/js/tests/post.js 8081; do
    echo "Parsoid server crashed with exit code $?.  Respawning.." >&2
    sleep 1
done
