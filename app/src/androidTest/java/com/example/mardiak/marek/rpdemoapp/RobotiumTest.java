package com.example.mardiak.marek.rpdemoapp;

import android.graphics.Point;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mardiak.marek.rpdemoapp.myOpenGl.MyGlSurfaceView;
import com.robotium.solo.Condition;
import com.robotium.solo.Solo;

import junit.framework.Assert;

/**
 * Created by mm on 4/6/2016.
 */
public class RobotiumTest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;
    private String[] titles;

    public RobotiumTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        titles = getActivity().getResources().getStringArray(R.array.titles);
    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testOpenDrawer() {
        Assert.assertTrue(getActivity().getString(R.string.app_name).equals(solo.getCurrentActivity().getTitle()));
        openDrawerBySwipe();
        for (String title : titles) {
            Assert.assertTrue(solo.searchText(title));
        }
    }

    public void testEmployees() throws InterruptedException {
        goToFragment(1);
        solo.waitForView(Spinner.class);
        View spinner = solo.getView(R.id.depSpinner);
        solo.clickOnView(spinner);
        Assert.assertNotNull(solo.getView(R.id.person_avatar));
        //TODO how to open menu without selecting item?
        //Assert.assertTrue(solo.searchText(getActivity().getResources().getString(R.string.action_import_json)));
    }

    public void testOpenGl() throws InterruptedException {
        goToFragment(2);
        solo.waitForView(MyGlSurfaceView.class);
        Assert.assertNotNull(solo.getView(R.id.gl_content_frame));
    }

    public void testMathematicians() throws InterruptedException {
        goToFragment(3);
        solo.waitForView(WebView.class);
        Assert.assertNotNull(solo.getView(R.id.mathematician_webview));
        solo.waitForView(AutoCompleteTextView.class);
        View autocompleteTextView = solo.getView(R.id.action_bar_search);
        solo.clickOnView(autocompleteTextView);
        solo.typeText((EditText) autocompleteTextView, "new");

        View searchedItem = solo.getView(R.id.search_item_mathematician_name);
        Assert.assertNotNull(searchedItem);
        Assert.assertTrue(solo.searchText("Newton"));
        solo.clickOnView(searchedItem);
    }

    public void testJNI() throws InterruptedException {
        goToFragment(4);
        waitForTextToDisappear("Hi from C world!!!!", 500);
    }


    private void goToFragment(int fragmentTitle) throws InterruptedException {
        openDrawerBySwipe();
        ListView listView = (ListView) solo.getView(R.id.drawer);
        View listElement = listView.getChildAt(fragmentTitle);
        solo.clickOnView(listElement);
        Assert.assertTrue(solo.searchText(titles[fragmentTitle]));
    }

    private void openDrawerBySwipe() {
        Point deviceSize = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(deviceSize);

        int screenWidth = deviceSize.x;
        int screenHeight = deviceSize.y;
        int fromX = 0;
        int toX = screenWidth / 2;
        int fromY = screenHeight / 2;
        int toY = fromY;

        solo.drag(fromX, toX, fromY, toY, 1);
        solo.waitForView(DrawerLayout.class);
    }

    private void waitForTextToDisappear(final String text, int wait) {
        Condition textToFound = new Condition() {

            @Override
            public boolean isSatisfied() {
                return solo.searchText(text);
            }
        };
        assertTrue("Text present: " + text, solo.waitForCondition(textToFound, wait));
    }
}
