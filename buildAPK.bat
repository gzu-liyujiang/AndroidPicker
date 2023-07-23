@rem JAVA_HOME -> jdk1.8
@echo build and proguard apk
cd /d ./
gradlew.bat clean assembleRelease --info --warning-mode all
pause

