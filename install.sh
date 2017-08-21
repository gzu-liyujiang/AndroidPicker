#!/bin/sh
# -r: replace existing application
# -d: allow version code downgrade

#adb install -r -d ./app/build/outputs/apk/app-release.apk

/home/liyujiang/Android/Sdk/platform-tools/adb install -r -d ./app/build/outputs/apk/app-debug.apk
