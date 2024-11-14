all:
	gcc src/main.c src/edit_distance_dyn.c -o ./bin/main
	gcc src/edit_distance_test.c src/edit_distance.c src/edit_distance_dyn.c ../Unity/unity.c -o ./bin/test

clean:
	rm bin/main
	rm bin/test

runTest:
	./bin/test 

runMain:
	./bin/main ./File/correctme.txt ./File/dictionary.txt

allWin:
	gcc src\main.c src\edit_distance_dyn.c -o bin\main
	gcc src\edit_distance_test.c src\edit_distance.c src\edit_distance_dyn.c ..\Unity\unity.c -o bin\test

cleanWin:
	del bin\main.exe
	del bin\test.exe

runMainWin:
	bin/main.exe File/correctme.txt File/dictionary.txt

runTestWin:
	bin/test.exe

