package ex4;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class GraphTestRunner {

    public static void main(String[] args) {
        Result res = JUnitCore.runClasses(GraphTest.class);
        for (Failure fail : res.getFailures()) {
            System.out.println(fail.toString());
        } // for
        System.out.println("Esito test: " + res.wasSuccessful());
    } // main

}