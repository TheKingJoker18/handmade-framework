@echo all

rem Creation des variables
set "temp=.\temp"
set "src=.\src\com\thekingjoker18\handmade_framework\test"
set "lib=.\lib"
set "bin=.\bin"

rem Suppression du repertoire [bin] s'il existe
if exist "%bin%\" (
    rmdir /s /q "%bin%"
)

rem Creation du repertoire [temp]
mkdir "%temp%"

rem Copie des fichiers Java du répertoire [src] vers [temp]
xcopy "%src%\controller\*.java" "%temp%" /e /y
xcopy "%src%\model\*.java" "%temp%" /e /y
xcopy "%src%\servlet\*.java" "%temp%" /e /y

rem Compilation des classes dans [temp] vers [bin]
javac -parameters -d %bin% -cp %lib%\* %temp%\*.java

rem Supression de [temp]
rmdir /s /q "%temp%"

pause