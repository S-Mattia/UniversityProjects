all:
	javac ex3/AbstractQueue.java
	javac ex3/PriorityQueue.java
	javac ex3/ComparatorString.java
	javac ex3/prova.java
	javac -cp junit/hamcrest-core-1.3.jar:junit/junit-4.12.jar:. ex3/PriorityQueueTest.java
	javac -cp junit/hamcrest-core-1.3.jar:junit/junit-4.12.jar:. ex3/PriorityQueueTestRunner.java

	javac ex4/AbstractEdge.java
	javac ex4/AbstractGraph.java
	javac ex4/Edge.java
	javac ex4/Graph.java
	javac ex4/prova.java
	javac -cp junit/hamcrest-core-1.3.jar:junit/junit-4.12.jar:. ex4/GraphTest.java
	javac -cp junit/hamcrest-core-1.3.jar:junit/junit-4.12.jar:. ex4/GraphTestRunner.java
	javac ex4/Prim.java
	clear

clean:
	rm ex3/*.class
	rm ex4/*.class

allWin:
	javac ex3/AbstractQueue.java
	javac ex3/PriorityQueue.java
	javac ex3/ComparatorString.java
	javac ex3/prova.java
	javac -cp junit/hamcrest-core-1.3.jar;junit/junit-4.12.jar; ex3/PriorityQueueTest.java
	javac -cp junit/hamcrest-core-1.3.jar;junit/junit-4.12.jar; ex3/PriorityQueueTestRunner.java

	javac ex4/AbstractEdge.java
	javac ex4/AbstractGraph.java
	javac ex4/Edge.java
	javac ex4/Graph.java
	javac ex4/prova.java
	javac -cp junit/hamcrest-core-1.3.jar;junit/junit-4.12.jar; ex4/GraphTest.java
	javac -cp junit/hamcrest-core-1.3.jar;junit/junit-4.12.jar; ex4/GraphTestRunner.java
	javac ex4/prim.java
	cls


cleanWin:
	del ex3\*.class
	del ex4\*.class

runTest:
	java -cp junit/hamcrest-core-1.3.jar:junit/junit-4.12.jar:. ex3/PriorityQueueTestRunner
	java -cp junit/hamcrest-core-1.3.jar:junit/junit-4.12.jar:. ex4/GraphTestRunner

runTestWin:
	java -cp junit/hamcrest-core-1.3.jar;junit/junit-4.12.jar; ex3/PriorityQueueTestRunner
	java -cp junit/hamcrest-core-1.3.jar;junit/junit-4.12.jar; ex4/GraphTestRunner

runMain:
	echo java ex4/Prim File/italian_dist_graph.csv