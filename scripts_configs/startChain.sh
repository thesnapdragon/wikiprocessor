#!/bin/bash

cd felix-framework-4.2.1
screen -S felix java -Xms256m -Xmx512m -jar bin/felix.jar
