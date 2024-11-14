package ex3;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class PriorityQueueTestRunner {

    public static void main(String[] args) {
        Result res = JUnitCore.runClasses(PriorityQueueTest.class);
        for (Failure fail : res.getFailures()) {
            System.out.println(fail.toString());
        } // for
        System.out.println("Test result: " + res.wasSuccessful());
    } // main

}
