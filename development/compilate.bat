@echo all

rem Creation des variables
set "temp=.\temp"
set "src=.\src\com\thekingjoker18\handmade_framework"
set "lib=..\test\lib"
set "bin=.\bin"
set "destination=..\test\lib"
set "nomFramework=handmade_framework"

rem Suppression du repertoire [bin] s'il existe
if exist "%bin%\" (
    rmdir /s /q "%bin%"
)

rem Creation du repertoire [temp]
mkdir "%temp%"

rem Copie des fichiers Java du répertoire [src] vers [temp]
xcopy "%src%\annotation\*.java" "%temp%" /e /y
xcopy "%src%\controller\*.java" "%temp%" /e /y
xcopy "%src%\exception\*.java" "%temp%" /e /y
xcopy "%src%\reflect\*.java" "%temp%" /e /y
xcopy "%src%\utils\*.java" "%temp%" /e /y

rem Compilation des classes dans [temp] vers [bin]
javac --release 8 -d %bin% -cp %lib%\* %temp%\*.java
rem Si "servlet-api.jar" est configurer dans CLASSPATH, enlever "-cp "%lib%\*" "

rem Ajout de l'application dans une archive jar
jar cvf "%nomFramework%.jar" -C "%bin%" .

rem Envoie de l'archive jar vers le [lib] du test
xcopy ".\%nomFramework%.jar" "%destination%" /y

rem Supression de [temp]
rmdir /s /q "%temp%"

pause