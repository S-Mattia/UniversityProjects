all: 
	gcc src/main.c src/sort_records.c src/merge_sort.c src/quick_sort.c src/comparator.c -o ./bin/main
	gcc src/sorting_algorithms_test.c src/merge_sort.c src/quick_sort.c ../Unity/unity.c -o ./bin/test

clean:
	rm ./bin/main
	rm ./bin/test

runTest:
	./bin/test

runMain:
	echo "\n\n----- ./bin/main ./File/records.csv ./File/out.txt  <field 1-3> <sorting_alg 1-2> -----\n\n"
	
allWin: 
	gcc src\main.c src\sort_records.c src\merge_sort.c src\quick_sort.c src\comparator.c -o bin/main
	gcc src\sorting_algorithms_test.c src\merge_sort.c src\quick_sort.c ..\Unity\unity.c -o bin\test

cleanWin:
	del bin\main.exe
	del bin\test.exe

runTestWin:
	bin/test.exe

runMainWin:
	echo "bin\main.exe File\records.csv File\out.txt  <field 1-3> <sorting_alg 1-2>"
	
	
