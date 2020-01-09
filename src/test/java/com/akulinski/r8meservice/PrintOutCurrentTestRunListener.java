package com.akulinski.r8meservice;


import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class PrintOutCurrentTestRunListener extends RunListener {

    @Override
    public void testRunStarted(Description description) throws Exception {
        // TODO all methods return null
        System.out.println("testRunStarted " + description.getClassName() + " " + description.getDisplayName() + " "
            + description.toString());
    }

    public void testStarted(Description description) throws Exception {
        System.out.println("testStarted "
            + description.toString());
    }

    public void testFinished(Description description) throws Exception {
        System.out.println("testFinished "
            + description.toString());
    }

    public void testRunFinished(Result result) throws Exception {
        System.out.println("testRunFinished " + result.toString()
            + " time:"+result.getRunTime()
            +" R"+result.getRunCount()
            +" F"+result.getFailureCount()
            +" I"+result.getIgnoreCount()
        );
    }
}
