#!/bin/bash

screen -dmS h2db java -cp deps/com.springsource.org.h2-1.0.71.jar org.h2.tools.Server -tcp -tcpPort 9123 -tcpAllowOthers -baseDir /opt/wikiprocessor/db/testDB
