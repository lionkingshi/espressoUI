package com.dolby.qa.utils.consumerUIUtils;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;
import android.util.Log;

import com.dolby.daxappui.R;
import com.dolby.qa.utils.commonUtils.UIConfiguration;
import com.dolby.qa.utils.consumerUIUtils.utils.LocalCustomMatcher;

import junit.framework.AssertionFailedError;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.dolby.qa.utils.Constants.DAX_DEA_MAX_VALUE;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_BALANCED;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_DETAILED;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_OFF;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_WARM;
import static com.dolby.qa.utils.Constants.FLAG_ALLOW_SLEEP_TIME;
import static com.dolby.qa.utils.Constants.FLAG_PRINT_DEBUG_INFO;
import static com.dolby.qa.utils.Constants.PACKAGE_NAME_UNDER_TEST;
import static com.dolby.qa.utils.Constants.SLEEP_TIME_UNIT_SECOND;
import static com.dolby.qa.utils.consumerUIUtils.utils.LocalCustomMatcher.withBrother;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertTrue;

public final class ConsumerUIViewAction {
    private static final String TAG = "QAA-"+ConsumerUIViewAction.class.getSimpleName();
//    @SuppressLint("StaticFieldLeak")
    private static UIConfiguration uiConfiguration;
    private static UiDevice mDevice;

    public ConsumerUIViewAction() {
        Log.d("Constructor",TAG);
    }

    public static void setUiConfiguration(final UIConfiguration muiConfiguration) {
        uiConfiguration = muiConfiguration;
        mDevice=UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    public static void goToFirstProfileView(){
        //click middle profile
        if (!uiConfiguration.isTablet()){
            try {
                onView(allOf(withText(uiConfiguration.getProfileName()[0]),
                        not(withId(R.id.profileName)))).
                        check(matches(isCompletelyDisplayed()));
            } catch (AssertionFailedError e) {
                if (FLAG_PRINT_DEBUG_INFO) e.printStackTrace();
                onView(allOf(withText(uiConfiguration.getProfileName()[2]),
                        isDescendantOfA(withId(R.id.profiletable)))
                ).perform(click());
            }
        }

        //click first profile - for dax3 : dynamic ; for dax3lite : movie
        onView(allOf(withText(uiConfiguration.getProfileName()[0]),
                isDescendantOfA(withId(uiConfiguration.isTablet() ? R.id.presetsListView : R.id.profiletable)))
        ).perform(click());
    }

    public static void goToSecondProfileView(){
        //click middle profile
        if (!uiConfiguration.isTablet()){
            try {
                onView(allOf(withText(uiConfiguration.getProfileName()[1]),
                        not(withId(R.id.profileName)))).
                        check(matches(isCompletelyDisplayed()));
            } catch (AssertionFailedError e) {
                if (FLAG_PRINT_DEBUG_INFO) e.printStackTrace();
                onView(allOf(withText(uiConfiguration.getProfileName()[3]),
                        isDescendantOfA(withId(R.id.profiletable)))
                ).perform(click());
            }
        }

        //click second profile - for dax3 :movie ; for dax3lite : music
        onView(allOf(withText(uiConfiguration.getProfileName()[1]),
                isDescendantOfA(withId(uiConfiguration.isTablet() ? R.id.presetsListView : R.id.profiletable)))
        ).perform(click());

        // confirm the expected view display and no other interaction
        if (mDevice.wait(Until.hasObject(
                By.res(PACKAGE_NAME_UNDER_TEST,"profileType").
                        hasChild(
                                By.res(PACKAGE_NAME_UNDER_TEST,"profileName").text(uiConfiguration.getProfileName()[1]))),
                SLEEP_TIME_UNIT_SECOND*1000*2)){
            if (FLAG_PRINT_DEBUG_INFO) Log.d(TAG, "navigate to the second view");
            onView(allOf(
                    withId(R.id.profileName),
                    withText(uiConfiguration.getProfileName()[1]),
                    withParent(withId(R.id.profileType)))).
                    check(matches(isDisplayed()));
        }
    }

    public static void goToThirdProfileView(){
        //click middle profile
        if (!uiConfiguration.isTablet()){
            try {
                onView(allOf(withText(uiConfiguration.getProfileName()[2]),
                        not(withId(R.id.profileName)))).
                        check(matches(isCompletelyDisplayed()));
            } catch (AssertionFailedError e) {
                if (FLAG_PRINT_DEBUG_INFO) e.printStackTrace();
                onView(allOf(withText(uiConfiguration.getProfileName()[3]),
                        isDescendantOfA(withId(R.id.profiletable)))
                ).perform(click());
            }
        }

        //click third profile - for dax3 :music ; for dax3lite : ?
        onView(allOf(withText(uiConfiguration.getProfileName()[2]),
                isDescendantOfA(withId(uiConfiguration.isTablet() ? R.id.presetsListView : R.id.profiletable)))
        ).perform(click());

        // wait for navigate to the profile
        if (mDevice.wait(
                Until.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"profileName").textContains(uiConfiguration.getProfileName()[2])),
                SLEEP_TIME_UNIT_SECOND*2000)){
            // do nothing
        }else{
            assertTrue("failed to navigate to specified profile !",false);
        }
    }

    public static void goToFourthProfileView(){
        //click middle profile
        if (!uiConfiguration.isTablet()){
            try {
                onView(allOf(withText(uiConfiguration.getProfileName()[3]),
                        not(withId(R.id.profileName)))).
                        check(matches(isCompletelyDisplayed()));
            } catch (AssertionFailedError e) {
                if (FLAG_PRINT_DEBUG_INFO) e.printStackTrace();
                onView(allOf(withText(uiConfiguration.getProfileName()[2]),
                        isDescendantOfA(withId(R.id.profiletable)))
                ).perform(click());
            }
        }
        //click fourth profile - for dax3 : custom ; for dax3lite : ?
        onView(allOf(withText(uiConfiguration.getProfileName()[3]),
                isDescendantOfA(withId(uiConfiguration.isTablet() ? R.id.presetsListView : R.id.profiletable)))
        ).perform(click());
    }

    public static void goToFifthProfileView(){
        //comment following code since for dax3 there are only 4 profile
//        //click middle profile
//        if (!uiConfiguration.isTablet()){
//            try {
//                onView(allOf(withText(uiConfiguration.getProfileName()[4]),
//                        not(withId(R.id.profileName)))).
//                        check(matches(isCompletelyDisplayed()));
//            } catch (AssertionFailedError e) {
//                if (FLAG_PRINT_DEBUG_INFO) e.printStackTrace();
//                onView(allOf(withText(uiConfiguration.getProfileName()[3]),
//                        isDescendantOfA(withId(R.id.profiletable)))
//                ).perform(click());
//            }
//        }
//
//        //click fifth profile - for dax2 :voice ; for ds : custom 1
//        onView(allOf(withText(uiConfiguration.getProfileName()[4]),
//                isDescendantOfA(withId(uiConfiguration.isTablet() ? R.id.presetsListView : R.id.profiletable)))
//        ).perform(click());
    }

    public static void goToSixthProfileView(){
        //comment following code since for dax3 there are only 4 profile
//        //click middle profile
//        if (!uiConfiguration.isTablet()){
//            try {
//                onView(allOf(withText(uiConfiguration.getProfileName()[5]),
//                        not(withId(R.id.profileName)))).
//                        check(matches(isCompletelyDisplayed()));
//            } catch (AssertionFailedError e) {
//                if (FLAG_PRINT_DEBUG_INFO) e.printStackTrace();
//                onView(allOf(withText(uiConfiguration.getProfileName()[3]),
//                        isDescendantOfA(withId(R.id.profiletable)))
//                ).perform(click());
//                try {
//                    onView(allOf(withText(uiConfiguration.getProfileName()[5]),
//                            not(withId(R.id.profileName)))).
//                            check(matches(isCompletelyDisplayed()));
//                } catch (AssertionFailedError e1) {
//                    if (FLAG_PRINT_DEBUG_INFO) e1.printStackTrace();
//                    onView(allOf(withText(uiConfiguration.getProfileName()[4]),
//                            isDescendantOfA(withId(R.id.profiletable)))
//                    ).perform(click());
//                }
//            }
//        }
//
//        //click sixth profile - for dax2 :custom ; for ds : custom 2
//        onView(allOf(withText(uiConfiguration.getProfileName()[5]),
//                isDescendantOfA(withId(uiConfiguration.isTablet() ? R.id.presetsListView : R.id.profiletable)))
//        ).perform(click(),longClick());
    }

    public static void clickVolumeLevelerButton(){
//        onView(allOf(withId(R.id.vlButton),isDisplayed())).perform(click());
    }

    public static void clickBassEnhancerButton(){
//        onView(allOf(withId(R.id.beButton),isDisplayed())).perform(click());
    }

    public static void clickSurroundVirtualizerButton(){
//        onView(allOf(withId(R.id.svButton),isDisplayed())).perform(click());
    }

    public static void clickDialogueEnhancerButton(){
        onView(allOf(withId(R.id.deButton),isDisplayed())).perform(click());
    }

    public static void clickDialogueEnhancerAmountButton(int position){
        onView(allOf(withId(R.id.deButton),isDisplayed())).
                perform(LocalCustomMatcher.setProgress(position*DAX_DEA_MAX_VALUE/100));
    }

    public static void swipeDialogueEnhancerAmountButton(int position){
        onView(allOf(withId(R.id.deButton),isDisplayed())).
                perform(LocalCustomMatcher.scrubSeekBarAction(position));
    }

    public static void selectIntelligentEqualizer(int presetIeq){
        switch (presetIeq) {
            case DAX_IEQ_PRESET_OFF:
                onView(allOf(isDisplayed(),withId(R.id.equalizerListOff))).perform(click());
                break;
            case DAX_IEQ_PRESET_DETAILED:
            case DAX_IEQ_PRESET_WARM:
            case DAX_IEQ_PRESET_BALANCED:
                if (FLAG_PRINT_DEBUG_INFO) Log.d(TAG, "the ieq preset value :" + presetIeq);
                // the equalizerlistview widget is adapter view and contain three same child view
                // use ondata to find the child widget we want
                // fist find the parent view and then find the child view by position
                onData(anything()).inAdapterView(allOf(withId(R.id.equalizerListView),isDisplayed())).
                        onChildView(allOf(withId(R.id.equalizerListViewLayout),isDisplayed())).
                        atPosition(presetIeq).
                        perform(click());
                break;
            default:
                if (FLAG_PRINT_DEBUG_INFO) Log.d(TAG,"the ieq preset value out of bound !"+presetIeq);
                    break;
        }
    }

    public static void clickProfileBasedResetButton(){
        try {
//            onView(allOf(withId(R.id.profileResetButton),withParent(isDisplayed()),isClickable())).check(matches(isCompletelyDisplayed()));
            onView(allOf(withId(R.id.profileResetButton),
                            withBrother(allOf(withId(R.id.profileName),isDisplayed())),
                            isClickable())).
                    check(matches(isCompletelyDisplayed()));

            onView(allOf(withId(R.id.profileResetButton),isCompletelyDisplayed())).perform(click());
            if (FLAG_PRINT_DEBUG_INFO) Log.d(TAG,"profile based reset button exist and then click it !");
            // click reset button again
            // even if reset button not exist , AssertionFailure threw by check function would be handled by catch code which did not do any .
            onView(allOf(withId(R.id.profileResetButton),withParent(isDisplayed()),isClickable())).check(matches(isCompletelyDisplayed()));
            onView(allOf(withId(R.id.profileResetButton),isCompletelyDisplayed())).perform(click());
        }catch (AssertionFailedError e){
            if (FLAG_PRINT_DEBUG_INFO) Log.d(TAG,"profile based reset button did not exist !");
        }
    }


    public static void clickUniversalBasedResetButton(){
        // for dax3 , cancel universal mode
//        try {
//            onView(withId(R.id.globalSettingsResetButton)).check(matches(isCompletelyDisplayed()));
//            onView(allOf(withId(R.id.globalSettingsResetButton),isCompletelyDisplayed())).perform(click());
//        }catch (AssertionFailedError e){
//            if (FLAG_PRINT_DEBUG_INFO) Log.d(TAG,"global settings reset button did not exist !");
//        }
    }

    public static void openNavigationView(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
    }

    private static void closeNavigationView(){
        // not click the item , and ui code would not close drawer navigation layout
        // so invoke the next code would close navigation view
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
    }

    public static void exitNavigationView(){
        closeNavigationView();
    }

    public static void navigateToHeadphoneView(){
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_headphonetuning));
    }

    public static void exitHeadphoneNavigation(){
        // code in MainActivity.java indicates the view would be replaced by headphone_tuning.xml layout
        // and meanwhile close the drawerLayout after 250 ms using handler.postDelayed() method
        // so next code would close the drawer navigation view again which certainly have no effect
        // but the active view is replaced view
        // so call press back method could close the replaced view
        // onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
        Espresso.pressBack();
    }

    public static void navigateToDemoView(){
        // for dax3 , dismiss demo clip playback in consumer ui
//        // a new activity would be launched after clicking the item
//        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_demo));
    }

    public static void exitDemoNavigation(){
//        // this is a flaky action if new activity attached view is rendered slower than espresso
//        onView(allOf(withChild(withId(R.id.playing_page)),isDisplayed())).perform(pressBack());
//        // closeNavigationView(); //this code invoke would close demo view
    }

    public static void clickDemoVideoView(){
//        onView(allOf(withId(R.id.videoView),isDisplayed())).perform(click());
    }

    public static void clickDemoBackButton(){
        // if view would be display after clicking other view
        // this is a way to handle it in case next view finding would succeed
//        try {
//            onView(withId(R.id.back_button)).check(matches(isDisplayed()));
//        }catch (AssertionFailedError e){
//            if (FLAG_PRINT_DEBUG_INFO) e.printStackTrace();
//            clickDemoVideoView();
//            onView(allOf(withId(R.id.back_button),isDisplayed())).perform(click());
//        }
    }

    public static void clickDemoReplayButton(){
//        try {
//            onView(withId(R.id.replay_button)).check(matches(isDisplayed()));
//        }catch (AssertionFailedError e){
//            if (FLAG_PRINT_DEBUG_INFO) e.printStackTrace();
//            clickDemoVideoView();
//            onView(allOf(withId(R.id.replay_button),isDisplayed())).perform(click());
//        }
    }

    public static void clickDemoPauseAndPlayButton(){
//        try {
//            onView(withId(R.id.pause_play_button)).check(matches(isDisplayed()));
//        }catch (AssertionFailedError e){
//            if (FLAG_PRINT_DEBUG_INFO) e.printStackTrace();
//            clickDemoVideoView();
//            onView(allOf(withId(R.id.pause_play_button),isDisplayed())).perform(click());
//        }
    }

    public static void navigateToExploreDolbyView(){
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_exploredolby));
    }

    public static void exitExploreDolbyNavigation(){
        // refer to exitHeadphoneNavigation explain
        // onView(allOf(withChild(withId(R.id.exploreatmoslogo)),isDisplayed())).perform(pressBack());
        Espresso.pressBack();
    }

    public static void clickExploreDolbyLearnMore(){
        // after clicking the button , view would be a website view consuming time
        if (FLAG_ALLOW_SLEEP_TIME){
            // one way is to wait a time to expected view display
            waitForSecond(SLEEP_TIME_UNIT_SECOND);
            onView(allOf(withId(R.id.txtuplearnmore),isDisplayed())).perform(click());
        }else {
            // another way is to use uiautomator wait method
            if (mDevice.wait(
                    Until.hasObject(By.clazz("android.widget.TextView").
                            res(PACKAGE_NAME_UNDER_TEST,"txtuplearnmore")),
                    SLEEP_TIME_UNIT_SECOND*1000*2))
            {
                if (FLAG_PRINT_DEBUG_INFO) Log.d(TAG, "prepare to enter learn more website!");
                onView(allOf(withId(R.id.txtuplearnmore),isDisplayed())).perform(click());
                if (FLAG_PRINT_DEBUG_INFO) Log.d(TAG, "entering learn more website!");
            }else {
                if (FLAG_PRINT_DEBUG_INFO)
                    Log.e(TAG, "failed : UIDevice wait for button object timeout with text -->learn more ");
            }
        }
    }

    public static void exitLearnMoreWebsite(){
        if (FLAG_ALLOW_SLEEP_TIME) {
            waitForSecond(SLEEP_TIME_UNIT_SECOND);
            if (FLAG_PRINT_DEBUG_INFO) Log.d(TAG,"prepare to exit learn more website!");
            mDevice.pressBack();
            if (FLAG_PRINT_DEBUG_INFO) Log.d(TAG,"exiting learn more website!");
            waitForSecond(SLEEP_TIME_UNIT_SECOND);
        } else {
            if (mDevice.wait(
                    Until.hasObject(By.clazz("android.widget.EditText")),
                    SLEEP_TIME_UNIT_SECOND*1000*2)){
                mDevice.pressBack();
            }else {
                if (FLAG_PRINT_DEBUG_INFO)
                    Log.e(TAG, "failed : UIDevice wait for learn more website timeout ");
            }
        }
    }

    public static void clickExploreDolbyAccess(){
        // after clicking the button , a website view consuming time display
        if (FLAG_ALLOW_SLEEP_TIME){
            // one way is to wait a time for expected view display
            waitForSecond(SLEEP_TIME_UNIT_SECOND);
            onView(allOf(withId(R.id.exploreatmosaccess),isDisplayed())).perform(click());
        }else {
            // another way is to use uiautomator wait method
            if (mDevice.wait(
                    Until.hasObject(By.clazz("android.widget.TextView").
                            res(PACKAGE_NAME_UNDER_TEST,"exploreatmosaccess")),
                    SLEEP_TIME_UNIT_SECOND*1000*2))
            {
                if (FLAG_PRINT_DEBUG_INFO) Log.d(TAG, "prepare to enter dolby access website!");
                onView(allOf(withId(R.id.btndowngotoaccess),isDisplayed())).perform(click());
                if (FLAG_PRINT_DEBUG_INFO) Log.d(TAG, "entering dolby access website!");
            }else {
                if (FLAG_PRINT_DEBUG_INFO)
                    Log.e(TAG, "failed : UIDevice wait for button object timeout with text-->access app .");
            }
        }
    }

    public static void exitDolbyAccessWebsite(){
        if (FLAG_ALLOW_SLEEP_TIME) {
            waitForSecond(SLEEP_TIME_UNIT_SECOND);
            if (FLAG_PRINT_DEBUG_INFO) Log.d(TAG,"prepare to exit access app website!");
            mDevice.pressBack();
            if (FLAG_PRINT_DEBUG_INFO) Log.d(TAG,"exiting access app website!");
            waitForSecond(SLEEP_TIME_UNIT_SECOND);
        } else {
            if (mDevice.wait(
                    Until.hasObject(By.clazz("android.widget.EditText")),
                    SLEEP_TIME_UNIT_SECOND*1000*2)){
                mDevice.pressBack();
            }else {
                if (FLAG_PRINT_DEBUG_INFO)
                    Log.e(TAG, "failed : UIDevice wait for access app website timeout ");
            }
        }
    }


    public static void navigateToTutorialView(){
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_tutorial));
    }

    public static void swipeLeftTutorialNavigation(){
        onView(allOf(withId(R.id.tutorialViewPager),isDisplayed())).perform(swipeLeft());
//        onView(allOf(withId(R.id.tutorialViewPager),isCompletelyDisplayed())).perform(scrollToPage());
    }

    public static void swipeRightTutorialNavigation(){
        onView(allOf(withId(R.id.tutorialViewPager),isDisplayed())).perform(swipeRight());
    }

    public static boolean waitForFirstTutorialNavigationView(){
        return mDevice.wait(
                Until.hasObject(
                        By.res(PACKAGE_NAME_UNDER_TEST,"ieq_tooltip")),
                SLEEP_TIME_UNIT_SECOND*1000*2);
    }

    public static Boolean waitForSecondTutorialNavigationView(){
        return mDevice.wait(
                Until.hasObject(
                        By.res(PACKAGE_NAME_UNDER_TEST,"de_tooltip")),
                SLEEP_TIME_UNIT_SECOND*1000*2);
    }

    public static boolean waitForEndTutorialNavigationView(){
        return mDevice.wait(
                Until.hasObject(
                        By.res(PACKAGE_NAME_UNDER_TEST,"vl_tooltip")),
                SLEEP_TIME_UNIT_SECOND*1000*2);
    }

    public static void exitTutorialNavigation(){
//        onView(allOf(withId(R.id.drawer_layout_tutorial),isDisplayed())).perform(pressBack());
        onView(allOf(withId(R.id.tutorialViewPager),isDisplayed())).perform(pressBack());
    }

    public static void navigateToResetView(){
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_reset));
    }

    public static void exitResetNavigation(){
        Espresso.pressBack();
    }

    public static void clickResetButtonInResetNavigation(){
        onView(allOf(withId(R.id.resetButtonView),isDisplayed())).perform(click());
    }

    public static void clickPowerButton(){
        onView(withId(R.id.powerButtonOn)).perform(click());
    }

    public static void turnOnUI(){
//        waitForSecond(1);
        try {
            onView(allOf(withText(R.string.power_off_text),isDisplayed())).check(doesNotExist());
//            onView(allOf(withText(R.string.ds1_power_off_text),isDisplayed())).check(doesNotExist());
        }catch (AssertionFailedError e){
            // if power_off_text exist for dax2, throws exception and then click power button to make ui on
            try {
                onView(allOf(withId(R.id.powerButtonOn),isDisplayed())).perform(click());
                if (FLAG_PRINT_DEBUG_INFO) {
                    Log.d(TAG, "dax2 ui turn on !");
                    e.printStackTrace();
                }
            }catch (PerformException pe){
                // if power_off_text exist for ds1, throws exception and then click power button to make ui on
                onView(allOf(withId(R.id.powerButtonOn),isDisplayed())).perform(click());
                if (FLAG_PRINT_DEBUG_INFO) {
                    Log.d(TAG, "ds1 ui turn on !");
                    e.printStackTrace();
                }
            }
        }
    }

    public static void turnOffUI(){
        try {
            // if power_off_text did not exist for dax2 ui , click button to make ui off
            onView(allOf(withText(R.string.power_off_text),isDisplayed())).check(doesNotExist());
            try {
//                onView(allOf(withText(R.string.ds1_power_off_text),isDisplayed())).check(doesNotExist());
                onView(allOf(withText(R.string.power_off_text),isDisplayed())).check(doesNotExist());
                onView(allOf(withId(R.id.powerButtonOn),isDisplayed())).perform(click());
            }catch (AssertionFailedError e1){
                if (FLAG_PRINT_DEBUG_INFO) {
                    Log.d(TAG, "ds1 ui is off !");
                    e1.printStackTrace();
                }
            }
        }catch (AssertionFailedError e){
            if (FLAG_PRINT_DEBUG_INFO) {
                Log.d(TAG, "dax2 ui is off !");
                e.printStackTrace();
            }
        }
    }

    public static void pressDeviceHomeBack(){
        Log.d("test--",mDevice.getCurrentPackageName());
        mDevice.pressBack();
        if (FLAG_PRINT_DEBUG_INFO) Log.d(TAG, "click home press back!");
    }

    public static void killApps(){
        try{
            mDevice.pressHome();
            mDevice.pressRecentApps();


            // Clear all isn't always visible unless you scroll all apps down
            // if dax ui app is visible and directly kill the app
            if (mDevice.hasObject(By.descContains("Dolby Atmos"))){
                if (mDevice.wait(
                        Until.hasObject(By.res("com.android.systemui:id/dismiss_task")),
                                SLEEP_TIME_UNIT_SECOND*1000*2)
                ){
                    mDevice.findObject(By.descContains("Dolby Atmos").res("com.android.systemui:id/dismiss_task")).click();
                    // click again to make sure app is killed
                    // animation exist will make that object could be found but after animation over, object dismiss
                    // which would result in below click operation failed
                    if (mDevice.hasObject(By.descContains("Dolby Atmos").res("com.android.systemui:id/dismiss_task"))){
//                        mDevice.findObject(By.descContains("Dolby Atmos").res("com.android.systemui:id/dismiss_task")).click();
                        try {
                            mDevice.findObject(By.descContains("Dolby Atmos").res("com.android.systemui:id/dismiss_task")).click();
                        }catch (NullPointerException e){
                            Log.d(TAG,"while stopping the dolby app again in recent apps , it has already dismissed !");
                        }
                    }
                }
            }else {
                // kill all apps including dax ui app
                int height = mDevice.getDisplayHeight();
                int width = mDevice.getDisplayWidth();
                mDevice.swipe(width/2,height/2, width/2, height, 50);

                if(mDevice.wait(
                        Until.hasObject(By.res("com.android.systemui:id/button").text("CLEAR ALL")),
                        SLEEP_TIME_UNIT_SECOND*1000*2)){
                    mDevice.findObject(By.res("com.android.systemui:id/button").text("CLEAR ALL")).click();
                }
            }
        }catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    protected static void startActivityFromHomeScreen(String packageName) {
//        assertThat(packageName,notNullValue());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
//        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                5000);
        //Log.d(LOG_TAG,"launcherPackage : "+launcherPackage);

        // Launch the app
        final Context context = InstrumentationRegistry.getContext();
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(packageName);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)),
                5000);
    }

    private static void waitForSecond(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
