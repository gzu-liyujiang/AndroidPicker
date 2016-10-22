#!/usr/bin/env bash

#/home/liyujiang/.m2/repository
#./gradlew publishToMavenLocal --info

# http://jcenter.bintray.com
./gradlew bintrayUpload --stacktrace
