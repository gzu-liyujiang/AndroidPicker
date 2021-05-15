@rem build and proguard apk
cd /d ./
gradlew.bat clean assembleRelease --info --warning-mode all
pause

