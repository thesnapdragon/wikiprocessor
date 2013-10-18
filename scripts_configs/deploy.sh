#!/bin/bash

server=192.168.200.97
deps=/home/chef/Egyetem/Onlab/repository/dependency
src=/home/chef/Egyetem/Onlab/repository

scp -r $deps/*.jar $server:/home/chef/deps/
scp $src/databaseconnectorbundle/generated/databaseconnectorbundle.jar $server:/home/chef/bundles/
scp $src/parserbundle/generated/parserbundle.jar $server:/home/chef/bundles/
scp $src/wikibotbundle/generated/wikibotbundle.jar $server:/home/chef/bundles/
scp $src/loggerbundle/generated/loggerbundle.jar $server:/home/chef/bundles/
scp $src/statisticsbundle/generated/statisticsbundle.jar $server:/home/chef/bundles/
scp log4j.properties $server:/home/chef/
scp index.html $server:/home/chef/
