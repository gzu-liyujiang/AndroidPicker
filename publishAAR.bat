@rem JAVA_HOME -> jdk1.8
@echo publish aar to local repository
cd /d ./
gradlew.bat clean publishReleasePublicationToLocalRepository --info --warning-mode all
pause

