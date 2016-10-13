#!/bin/sh
# -r: replace existing application
# -d: allow version code downgrade

adb install -r -d ./app-release.apk

