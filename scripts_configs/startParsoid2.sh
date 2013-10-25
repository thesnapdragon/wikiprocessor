#!/bin/bash

ip=$(ifconfig eth0 | grep 'inet addr:' | cut -d: -f2 | awk '{ print $1}') 2>/tmp/err1
ssh -o 'StrictHostKeyChecking no' -i /home/cloud/.ssh/id_rsa chef@192.168.200.97 "tmp=\$(mktemp /opt/wikiprocessor/parsoids/XXXXXXX.ip); echo $ip > \$tmp;" 2>/tmp/err2
su - cloud -c 'screen -dmS parsoid /home/cloud/startParsoid.sh'
