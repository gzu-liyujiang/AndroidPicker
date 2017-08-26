#!/usr/bin/env bash

p="$1";
if [ -z "${p}" ]; then
    echo "can use parameter 'upload' to upload to jcenter, too";
    p="null";
fi
echo "parameter is ${p}";

if [ ${p} = "upload" ]; then
    # http://jcenter.bintray.com
    ./gradlew publishToMavenLocal bintrayUpload --stacktrace
else
    #/home/liyujiang/.m2/repository
    ./gradlew publishToMavenLocal --stacktrace
fi