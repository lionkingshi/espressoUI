package com.dolby.qa.daxuitest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.dolby.daxappui.MainActivity;
import com.dolby.qa.utils.consumerUIUtils.ConsumerUIWrapper;
import com.dolby.qa.utils.customAnnotation.DaxUIUnitTest;
import com.squareup.spoon.Spoon;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static com.dolby.qa.utils.Constants.DAX_CUSTOM_IEQ_STATUS_DEFAULT;
import static com.dolby.qa.utils.Constants.DAX_DEA_DEFAULT_VALUE;
import static com.dolby.qa.utils.Constants.DAX_DEA_MAX_VALUE;
import static com.dolby.qa.utils.Constants.DAX_DEA_MIN_VALUE;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_BALANCED;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_DETAILED;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_OFF;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_WARM;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * this class would test all wrapped methods in ConsumerUIWrapper class
 * the method include all supported atomic UI action in current DAX3 UI (despite a phone or tablet)
 * the method include all supported UI assertion
 * which would verify the DAX3 UI display after action event occurs
 */

@RunWith(AndroidJUnit4.class)
public class DaxConsumerUITestCodeUnitTest extends ConsumerUIWrapper {

    @Rule
    public ActivityTestRule<MainActivity> mLocalActivityRule;

    {
        mLocalActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class){
            @Override
            protected void beforeActivityLaunched() {
                super.beforeActivityLaunched();
                Log.d("QA-Constructor","ActivityTestRule before activity launched");
            }

            @Override
            protected void afterActivityLaunched() {
                super.afterActivityLaunched();

                Log.d("QA-Constructor","ActivityTestRule after activity launched");
            }

            @Override
            protected void afterActivityFinished() {
                super.afterActivityFinished();
                Log.d("QA-Constructor","ActivityTestRule after activity finished");
            }
        };
        mActivityRule=mLocalActivityRule;
    }


    @Before
    public void dsTestCaseSetUp(){
       daxTestCaseSetUp();
       turnOnDax();
       resetDaxAllFeatureValue();
    }


    //each test individually store corresponding log information
    @After
    public void dsTestCaseTearDown(){
//        turnOnDax();
//        resetDaxAllFeatureValue();
        daxTestCaseTearDown();
    }

    @Test
    public void checkPreconditions(){
        assertThat(mActivityRule,notNullValue());
    }

    @Test
    @DaxUIUnitTest
    public void testTraverseAllView(){
        int i = 0;

        tabToDynamicProfileView();
        Spoon.screenshot(mActivityRule.getActivity(),"dynamic"+i++);

        tabToMovieProfileView();
        Spoon.screenshot(mActivityRule.getActivity(),"movie"+i++);
        for (int progress = 0 ; progress <= DAX_DEA_MAX_VALUE; progress++){
            swipeDeaSliderInMovieProfile(progress);
            Spoon.screenshot(mActivityRule.getActivity(),"movie"+i++);
        }
        clickResetInMovieProfile();
        Spoon.screenshot(mActivityRule.getActivity(),"movie"+i++);
        for (int progress = 0 ; progress <= DAX_DEA_MAX_VALUE/2; progress++){
            swipeDeaSliderInMovieProfile(progress*2);
            Spoon.screenshot(mActivityRule.getActivity(),"movie"+i++);
        }
        clickResetInMovieProfile();
        Spoon.screenshot(mActivityRule.getActivity(),"movie"+i++);

        tabToMusicProfileView();
        Spoon.screenshot(mActivityRule.getActivity(),"music"+i++);
        for (int ieq_num = 0; ieq_num <= DAX_IEQ_PRESET_OFF; ieq_num++){
            selectIeqInMusicProfile(ieq_num);
            Spoon.screenshot(mActivityRule.getActivity(),"music"+i++);
        }
        clickResetInMusicProfile();
        Spoon.screenshot(mActivityRule.getActivity(),"music"+i++);

        tabToCustomProfileView();
        Spoon.screenshot(mActivityRule.getActivity(),"custom"+i++);
        for (int progress = 0 ; progress <= DAX_DEA_MAX_VALUE/2; progress++){
            swipeDeaSliderInCustomProfile(progress*2);
            Spoon.screenshot(mActivityRule.getActivity(),"custom"+i++);
            selectIeqInCustomProfile(progress%4);
            Spoon.screenshot(mActivityRule.getActivity(),"custom"+i++);

        }
        clickResetInCustomProfile();
        Spoon.screenshot(mActivityRule.getActivity(),"custom"+i++);

        swipeDeaSliderInCustomProfile(DAX_DEA_MAX_VALUE);
        Spoon.screenshot(mActivityRule.getActivity(),"custom"+i++);
        selectIeqInCustomProfile(DAX_IEQ_PRESET_DETAILED);
        Spoon.screenshot(mActivityRule.getActivity(),"custom"+i++);
        turnOffDax();
        Spoon.screenshot(mActivityRule.getActivity(),"dapoff"+i++);
        turnOnDax();
        Spoon.screenshot(mActivityRule.getActivity(),"dapon"+i++);

        openNavigationView();
        waitForSecond(1);
        Spoon.screenshot(mActivityRule.getActivity(),"navigation"+i++);
        navigateToExploreDolbyView();
        waitForSecond(1);
        Spoon.screenshot(mActivityRule.getActivity(),"explore"+i++);
        pressHomeBack();
        waitForSecond(1);

        tabToMovieProfileView();
        swipeDeaSliderInMovieProfile(DAX_DEA_MIN_VALUE);
        Spoon.screenshot(mActivityRule.getActivity(),"movie"+i++);
        tabToMusicProfileView();
        selectIeqInMusicProfile(DAX_IEQ_PRESET_OFF);
        Spoon.screenshot(mActivityRule.getActivity(),"music"+i++);

        openNavigationView();
        waitForSecond(1);
        Spoon.screenshot(mActivityRule.getActivity(),"navigation"+i++);
        navigationToResetView();
        waitForSecond(1);
        Spoon.screenshot(mActivityRule.getActivity(),"reset"+i++);
        clickResetInResetNavigation();
        clickResetInResetNavigation();
        Spoon.screenshot(mActivityRule.getActivity(),"reset"+i++);
        pressHomeBack();
        waitForSecond(1);
        Spoon.screenshot(mActivityRule.getActivity(),"resetback"+i++);

        tabToDynamicProfileView();
        Spoon.screenshot(mActivityRule.getActivity(),"resetdynamic"+i++);
        tabToMusicProfileView();
        Spoon.screenshot(mActivityRule.getActivity(),"resetmusic"+i++);
        tabToMovieProfileView();
        Spoon.screenshot(mActivityRule.getActivity(),"resetmovie"+i++);
        tabToCustomProfileView();
        Spoon.screenshot(mActivityRule.getActivity(),"resetcustom"+i++);

        turnOffDax();
        Spoon.screenshot(mActivityRule.getActivity(),"resetoff"+i++);
    }

    @Test
    @DaxUIUnitTest
    public void testDynamicProfileUI(){
        tabToDynamicProfileView();
        checkUIDynamicMainView();
    }

    @Test
    @DaxUIUnitTest
    public void testMovieProfileUI(){
        tabToMovieProfileView();

        for (int progress = 0 ; progress <= DAX_DEA_MAX_VALUE; progress++){
            Log.d("<<<< seekBar ","current index "+progress);
            swipeDeaSliderInMovieProfile(progress);
            checkUIMovieMainView(progress);
        }

        for (int progress = DAX_DEA_MAX_VALUE-1 ; progress >= 0 ; progress--){
            Log.d("<<<< seekBar ","current index "+progress);
            swipeDeaSliderInMovieProfile(progress);
            checkUIMovieMainView(progress);
        }
        swipeDeaSliderInMovieProfile(DAX_DEA_DEFAULT_VALUE);
        checkMovieFeatureNotChanged();
    }

    @Test
    @DaxUIUnitTest
    public void testMusicProfileUI(){
        tabToMusicProfileView();
        checkMusicFeatureNotChanged();

        selectIeqInMusicProfile(DAX_IEQ_PRESET_DETAILED);
        checkUIMusicMainView(DAX_IEQ_PRESET_DETAILED);
        clickResetInMusicProfile();
        checkMusicFeatureNotChanged();

        selectIeqInMusicProfile(DAX_IEQ_PRESET_WARM);
        checkUIMusicMainView(DAX_IEQ_PRESET_WARM);
        clickResetInMusicProfile();
        checkMusicFeatureNotChanged();

        selectIeqInMusicProfile(DAX_IEQ_PRESET_BALANCED);
        checkUIMusicMainView(DAX_IEQ_PRESET_BALANCED);
        clickResetInMusicProfile();
        checkMusicFeatureNotChanged();

        selectIeqInMusicProfile(DAX_IEQ_PRESET_OFF);
        checkUIMusicMainView(DAX_IEQ_PRESET_OFF);
        clickResetInMusicProfile();
        checkMusicFeatureNotChanged();

        selectIeqInMusicProfile(DAX_IEQ_PRESET_DETAILED);
        checkUIMusicMainView(DAX_IEQ_PRESET_DETAILED);

        clickResetInMusicProfile();
        checkMusicFeatureNotChanged();
    }



    @Test
    @DaxUIUnitTest
    public void testCustomProfileUI(){
        tabToCustomProfileView();
        checkCustomFeatureNotChanged();

        // dea check
        for (int progress = 0 ; progress <= DAX_DEA_MAX_VALUE; progress++){
            Log.d("<<<< seekBar ","current index "+progress);
            swipeDeaSliderInMovieProfile(progress);
            checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT,progress);
        }

        for (int progress = DAX_DEA_MAX_VALUE-1 ; progress >= 0 ; progress--){
            Log.d("<<<< seekBar ","current index "+progress);
            swipeDeaSliderInMovieProfile(progress);
            checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT,progress);
        }
        swipeDeaSliderInMovieProfile(DAX_DEA_DEFAULT_VALUE);
        swipeDeaSliderInMovieProfile(DAX_DEA_DEFAULT_VALUE+1);

        clickResetInCustomProfile();
        checkCustomFeatureNotChanged();


        // ieq check
        selectIeqInCustomProfile(DAX_IEQ_PRESET_DETAILED);
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED,DAX_DEA_DEFAULT_VALUE);
        clickResetInCustomProfile();
        checkCustomFeatureNotChanged();

        selectIeqInCustomProfile(DAX_IEQ_PRESET_WARM);
        checkUICustomMainView(DAX_IEQ_PRESET_WARM,DAX_DEA_DEFAULT_VALUE);
        clickResetInCustomProfile();
        checkCustomFeatureNotChanged();

        selectIeqInCustomProfile(DAX_IEQ_PRESET_BALANCED);
        checkUICustomMainView(DAX_IEQ_PRESET_BALANCED,DAX_DEA_DEFAULT_VALUE);
        clickResetInCustomProfile();
        checkCustomFeatureNotChanged();

        selectIeqInCustomProfile(DAX_IEQ_PRESET_OFF);
        checkUICustomMainView(DAX_IEQ_PRESET_OFF,DAX_DEA_DEFAULT_VALUE);
        clickResetInCustomProfile();
        checkCustomFeatureNotChanged();

        selectIeqInCustomProfile(DAX_IEQ_PRESET_BALANCED);
        selectIeqInCustomProfile(DAX_IEQ_PRESET_OFF);
        checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT, DAX_DEA_DEFAULT_VALUE);

        checkCustomFeatureNotChanged();
    }

    @Test
    @DaxUIUnitTest
    public void testTabAllProfile(){
        for (int i = 0; i < 2; i++) {
            // tab to the first tab view
            tabToDynamicProfileView();
            checkUIDynamicMainView();

            // tab to last tab view
            tabToCustomProfileView();
            checkCustomFeatureNotChanged();

            //tab to the second tab view
            tabToMovieProfileView();
            checkMovieFeatureNotChanged();

            // tab to third tab view
            tabToMusicProfileView();
            checkMusicFeatureNotChanged();
        }
    }

    @Test
    @DaxUIUnitTest
    public void testUIOff(){
        turnOffDax();
        checkUIOffView();
        turnOnDax();
        turnOffDax();
        turnOffDax();
        checkUIOffView();
        turnOnDax();
    }

    @Test
    @DaxUIUnitTest
    public void testNavigationView(){
        openNavigationView();
        checkUINavigationView();
        exitNavigationView();
    }

    @Test
    @DaxUIUnitTest
    public void testExploreDolbyView(){
        openNavigationView();
        checkUINavigationView();
        navigateToExploreDolbyView();
        checkUIExploreDolbyView();
        clickExploreDolbyLearnMore();
        exitLearnMoreWebsite();
        clickExploreDolbyAccess();
        exitDolbyAccessWebsite();
        pressHomeBack();
    }


    @Test
    @DaxUIUnitTest
    public void testPhoneTutorialView(){
        if (!isTabletDevice()){
            Log.d("Unit-test","device under test is a phone!");
            openNavigationView();
            checkUINavigationView();
            navigateToTutorialView();
            waitForSecond(2);
            checkUITutorialStartView();

            swipeLeftTutorialNavigation();
            checkUITutorialSecondView();

            swipeLeftTutorialNavigation();
            checkUITutorialEndView();

            swipeLeftTutorialNavigation();
            checkUITutorialEndView();

            swipeRightTutorialNavigation();
            checkUITutorialSecondView();

            swipeRightTutorialNavigation();
            checkUITutorialStartView();
            pressHomeBack();
        }
    }


    @Test
    @DaxUIUnitTest
    public void testResetNavigationView(){
        resetDaxAllFeatureValue();
        openNavigationView();
        navigationToResetView();
        checkUIResetView();
        clickResetInResetNavigation();
        exitResetNavigation();
        checkDaxAllFeatureValueNotChanged();

        changeDaxAllFeatureValue();
        openNavigationView();
        navigationToResetView();
        checkUIResetView();
        clickResetInResetNavigation();
        clickResetInResetNavigation();
        exitResetNavigation();
        checkDaxAllFeatureValueNotChanged();

        tabToDynamicProfileView();
        openNavigationView();
        navigationToResetView();
        checkUIResetView();
        exitResetNavigation();
        checkUIDynamicMainView();

        tabToCustomProfileView();
        selectIeqInCustomProfile(DAX_IEQ_PRESET_DETAILED);
        swipeDeaSliderInCustomProfile(DAX_DEA_MIN_VALUE);
        openNavigationView();
        navigationToResetView();
        pressHomeBack();
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED,DAX_DEA_MIN_VALUE);
    }
//
//    @Test
//    @DaxUIUnitTest
//    public void testHeadphoneView(){
//        if (!isDax2LiteUI()){
//            openNavigationView();
//            checkUINavigationView();
//            navigateToHeadphoneView();
//            checkUIHeadphoneView();
//            pressHomeBack();
//            Log.d("Unit-test","it is not dax lite ui!");
//        }
//    }
//
//
//    @Test
//    @DaxUIUnitTest
//    public void testTabletTutorialView(){
//        if (isTabletDevice()){
//            Log.d("Unit-test","device under test is a tablet !");
//
//            openNavigationView();
//            checkUINavigationView();
//            navigateToTutorialView();
//            checkUITutorialStartView();
//
//            swipeLeftTutorialNavigation();
//            checkUITutorialSecondView();
//
//            swipeLeftTutorialNavigation();
//            checkUITutorialEndView();
//
//            swipeLeftTutorialNavigation();
//            checkUITutorialEndView();
//
//            swipeRightTutorialNavigation();
//            checkUITutorialStartView();
//
//            swipeRightTutorialNavigation();
//            checkUITutorialStartView();
//            pressHomeBack();
//        }
//    }

    private void waitForSecond(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}