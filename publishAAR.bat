@echo publish aar to local repository
cd /d ./
gradlew.bat clean publishReleasePublicationToLocalRepository --info --warning-mode all
pause

