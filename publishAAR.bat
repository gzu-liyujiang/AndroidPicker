chcp 65001

@rem JAVA_HOME -> jdk17
@echo publish aar to local repository
cd /d ./
start gradlew.bat clean publishReleasePublicationToLocalRepository --info --warning-mode all

