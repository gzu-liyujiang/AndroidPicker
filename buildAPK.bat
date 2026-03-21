chcp 65001

@rem JAVA_HOME -> jdk17
@echo build and proguard apk
cd /d ./
start gradlew.bat clean assembleRelease --info --warning-mode all
