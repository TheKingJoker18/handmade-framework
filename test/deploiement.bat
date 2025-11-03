@echo all

rem Creation des variables
set "webapps=C:\xampp\tomcat\webapps"
set "nomAppli=Handmade_Framework"
set "temp=.\temp"
set "src=.\src\com\thekingjoker18\handmade_framework\test"
set "lib=.\lib"
set "web=.\web"
set "webxml=.\webxml"

rem Creation d'un nouveau repertoire dans [temp]
mkdir "%temp%\%nomAppli%"

rem Creation des structures de l'application [nomAppli]
mkdir "%temp%\%nomAppli%\WEB-INF"
mkdir "%temp%\%nomAppli%\WEB-INF\lib"
mkdir "%temp%\%nomAppli%\WEB-INF\classes"

rem Copie de l'interieur de [web], [webxml] et [lib] dans [nomAppli]
xcopy "%web%\*" "%temp%\%nomAppli%\" /e /y
xcopy "%webxml%\*" "%temp%\%nomAppli%\WEB-INF\" /e /y
xcopy "%lib%\*" "%temp%\%nomAppli%\WEB-INF\lib\" /e /y

rem Copie des fichiers Java du répertoire [src] vers [temp]
xcopy "%src%\controller\*.java" "%temp%" /e /y
xcopy "%src%\model\*.java" "%temp%" /e /y
xcopy "%src%\servlet\*.java" "%temp%" /e /y

rem Compilation des classes dans [temp] vers [nomAppli]
javac -parameters --release 8 -d %temp%\%nomAppli%\WEB-INF\classes -cp %lib%\* %temp%\*.java

rem Ajout de l'application dans une archive war
jar cvf "%nomAppli%.war" -C "%temp%\%nomAppli%" .

rem Deploiement de l'application vers Tomcat
xcopy ".\%nomAppli%.war" "%webapps%" /y

rem Suppression de [temp]
rmdir /s /q "%temp%"

pause