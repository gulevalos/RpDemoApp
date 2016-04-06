package com.example.mardiak.marek.rpdemoapp;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import junit.framework.Assert;

/**
 * Created by mm on 4/6/2016.
 */
public class RobotiumTest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public RobotiumTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testDisplayedText() throws InterruptedException {
        Assert.assertTrue(solo.searchText("RpDemoApp"));
        Assert.assertTrue(getActivity().getString(R.string.app_name).equals(solo.getCurrentActivity().getTitle()));
    }
}
