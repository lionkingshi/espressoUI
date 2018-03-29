package com.dolby.qa.daxuitest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.dolby.daxappui.MainActivity;
import com.dolby.qa.utils.consumerUIUtils.ConsumerUIWrapper;
import com.dolby.qa.utils.customAnnotation.DaxUITest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.dolby.qa.utils.Constants.DAX_CUSTOM_IEQ_STATUS_DEFAULT;
import static com.dolby.qa.utils.Constants.DAX_DEA_CUSTOM_DEFAULT_VALUE;
import static com.dolby.qa.utils.Constants.DAX_DEA_DEFAULT_VALUE;
import static com.dolby.qa.utils.Constants.DAX_DEA_MAX_VALUE;
import static com.dolby.qa.utils.Constants.DAX_DEA_MIN_VALUE;
import static com.dolby.qa.utils.Constants.DAX_DEA_MOVIE_DEFAULT_VALUE;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_BALANCED;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_DETAILED;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_OFF;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_WARM;
import static com.dolby.qa.utils.Constants.DAX_MUSIC_IEQ_STATUS_DEFAULT;

@RunWith(AndroidJUnit4.class)
public class DaxConsumerUITest extends ConsumerUIWrapper {
    @Rule
    public ActivityTestRule<MainActivity> mLocalActivityRule;

    {
        mLocalActivityRule = new ActivityTestRule<>(MainActivity.class);
        mActivityRule=mLocalActivityRule;
    }


    @Before
    public void daxTestCaseSetUp(){
        super.daxTestCaseSetUp();
        turnOnDax();
        resetDaxAllFeatureValue();
    }

    //each test individually store corresponding log information
    @After
    public void daxTestCaseTearDown(){
//        resetDaxAllFeatureValue();
        super.daxTestCaseTearDown();
    }

    @Test
    @DaxUITest
    public void validUIDisplayAfterFlashing(){
        //check the UI display after interaction with user
        //profile-based UI display

        //the dynamic profile should be display after first flashing
        // step 1 would be ignored because it made test case no isolation with each other
        //checkDynamicFeatureNotChanged();
        // replace the check with : first navigate to dynamic profile and then check display
        tabToDynamicProfileView();
        checkUIDynamicMainView();

        //step 2 : click movie profile
        tabToMovieProfileView();
        checkMovieFeatureNotChanged();

        //step 3: click music profile
        tabToMusicProfileView();
        checkMusicFeatureNotChanged();

        //step 4: click custom profile
        tabToCustomProfileView();
        checkCustomFeatureNotChanged();

        //return to dynamic profile in order to other test case
        tabToDynamicProfileView();
        checkUIDynamicMainView();
    }

    @Test
    @DaxUITest
    public void validUIDisplayAfterPowerClickAction(){
        //step 1 and 2
        tabToCustomProfileView();
        swipeDeaSliderInCustomProfile(DAX_DEA_MIN_VALUE);

        //step 3
        turnOffDax();
        checkUIOffView();

        //step 4
        turnOnDax();
        checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT, DAX_DEA_MIN_VALUE);

        //step 5
        resetAllFeatureValueInCustomProfile();
        turnOffDax();
        checkUIOffView();

        //step 6
        turnOnDax();
        checkCustomFeatureNotChanged();
    }



    @Test
    @DaxUITest
    public void
    validUIDisplayAfterMusicIEQModified(){
        //step 1
        tabToMusicProfileView();
        checkMusicFeatureNotChanged();

        //step 2
        selectIeqInMusicProfile(DAX_IEQ_PRESET_OFF);
        checkUIMusicMainView(DAX_IEQ_PRESET_OFF);

        //step 4
        clickResetInMusicProfile();
        checkUIMusicMainView(DAX_MUSIC_IEQ_STATUS_DEFAULT);

        //step 5
        selectIeqInMusicProfile(DAX_IEQ_PRESET_WARM);
        checkUIMusicMainView(DAX_IEQ_PRESET_WARM);

        //step 6
        clickResetInMusicProfile();
        checkUIMusicMainView(DAX_MUSIC_IEQ_STATUS_DEFAULT);

        //step 7
        selectIeqInMusicProfile(DAX_IEQ_PRESET_DETAILED);
        checkUIMusicMainView(DAX_IEQ_PRESET_DETAILED);

        //step 8
        clickResetInMusicProfile();
        checkUIMusicMainView(DAX_MUSIC_IEQ_STATUS_DEFAULT);

        //step 9
        selectIeqInMusicProfile(DAX_IEQ_PRESET_DETAILED);
        checkUIMusicMainView(DAX_IEQ_PRESET_DETAILED);

        //step 10
        selectIeqInMusicProfile(DAX_MUSIC_IEQ_STATUS_DEFAULT);
        checkUIMusicMainView(DAX_IEQ_PRESET_BALANCED);

        //step 11
        selectIeqInMusicProfile(DAX_IEQ_PRESET_WARM);
        checkUIMusicMainView(DAX_IEQ_PRESET_WARM);

        //step 12
        selectIeqInMusicProfile(DAX_MUSIC_IEQ_STATUS_DEFAULT);
        checkUIMusicMainView(DAX_IEQ_PRESET_BALANCED);

        //step 13
        selectIeqInMusicProfile(DAX_IEQ_PRESET_OFF);
        checkUIMusicMainView(DAX_IEQ_PRESET_OFF);

        //step 14
        selectIeqInMusicProfile(DAX_MUSIC_IEQ_STATUS_DEFAULT);
        selectIeqInMusicProfile(DAX_MUSIC_IEQ_STATUS_DEFAULT);
        checkUIMusicMainView(DAX_IEQ_PRESET_BALANCED);

        //step 15
        selectIeqInMusicProfile(DAX_IEQ_PRESET_WARM);
        selectIeqInMusicProfile(DAX_IEQ_PRESET_WARM);
        checkUIMusicMainView(DAX_IEQ_PRESET_WARM);

        //step 16
        selectIeqInMusicProfile(DAX_IEQ_PRESET_OFF);
        selectIeqInMusicProfile(DAX_IEQ_PRESET_OFF);
        checkUIMusicMainView(DAX_IEQ_PRESET_OFF);

        //step 17
        selectIeqInMusicProfile(DAX_IEQ_PRESET_DETAILED);
        selectIeqInMusicProfile(DAX_IEQ_PRESET_DETAILED);
        checkUIMusicMainView(DAX_IEQ_PRESET_DETAILED);

        //step 18
        tabToMovieProfileView();
        checkUIMovieMainView(DAX_DEA_DEFAULT_VALUE);

        //step 19
        tabToMusicProfileView();
        checkUIMusicMainView(DAX_IEQ_PRESET_DETAILED);

        //step 20
        tabToDynamicProfileView();
        checkUIDynamicMainView();

        //step 21
        tabToMusicProfileView();
        checkUIMusicMainView(DAX_IEQ_PRESET_DETAILED);

        //step 22

        //step 23
        tabToMusicProfileView();
        checkUIMusicMainView(DAX_IEQ_PRESET_DETAILED);

        //step 24
        //step 25
        tabToMusicProfileView();
        checkUIMusicMainView(DAX_IEQ_PRESET_DETAILED);

        //step 26
        tabToCustomProfileView();
        checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 27
        tabToMusicProfileView();
        checkUIMusicMainView(DAX_IEQ_PRESET_DETAILED);

        //step 28
        clickResetInMusicProfile();
        checkUIMusicMainView(DAX_MUSIC_IEQ_STATUS_DEFAULT);

        //STEP 29
        checkMusicFeatureNotChanged();
    }

    @Test
    @DaxUITest
    public void
    validUIDisplayAfterMovieDeaModified(){
        tabToMovieProfileView();

        for (int progress = 0 ; progress <= DAX_DEA_MAX_VALUE; progress++){
            swipeDeaSliderInMovieProfile(progress);
            checkUIMovieMainView(progress);
        }

        for (int progress = DAX_DEA_MAX_VALUE-1 ; progress >= 0 ; progress--){
            swipeDeaSliderInMovieProfile(progress);
            checkUIMovieMainView(progress);
        }
        swipeDeaSliderInMovieProfile(DAX_DEA_DEFAULT_VALUE);
        checkMovieFeatureNotChanged();

        swipeDeaSliderInMovieProfile(DAX_DEA_MIN_VALUE);
        checkUIMovieMainView(DAX_DEA_MIN_VALUE);

        tabToDynamicProfileView();
        checkUIDynamicMainView();

        tabToMovieProfileView();
        checkUIMovieMainView(DAX_DEA_MIN_VALUE);

        tabToMusicProfileView();
        checkMusicFeatureNotChanged();
        selectIeqInMusicProfile(DAX_IEQ_PRESET_WARM);
        checkUIMusicMainView(DAX_IEQ_PRESET_WARM);

        tabToMovieProfileView();
        checkUIMovieMainView(DAX_DEA_MIN_VALUE);

        tabToCustomProfileView();
        swipeDeaSliderInCustomProfile(DAX_DEA_MAX_VALUE);
        selectIeqInCustomProfile(DAX_IEQ_PRESET_DETAILED);
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED, DAX_DEA_MAX_VALUE);

        tabToMovieProfileView();
        checkUIMovieMainView(DAX_DEA_MIN_VALUE);

        tabToMusicProfileView();
        checkUIMusicMainView(DAX_IEQ_PRESET_WARM);
        tabToCustomProfileView();
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED, DAX_DEA_MAX_VALUE);

        tabToMovieProfileView();
        checkUIMovieMainView(DAX_DEA_MIN_VALUE);

        turnOffDax();
        checkUIOffView();
        turnOnDax();
        tabToMovieProfileView();
        checkUIMovieMainView(DAX_DEA_MIN_VALUE);
    }


    @Test
    @DaxUITest
    public void
    validUIDisplayAfterCustomDeaModified(){
        tabToCustomProfileView();

        for (int progress = 0 ; progress <= DAX_DEA_MAX_VALUE; progress++){
            swipeDeaSliderInCustomProfile(progress);
            checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT,progress);
        }

        for (int progress = DAX_DEA_MAX_VALUE-1 ; progress >= 0 ; progress--){
            swipeDeaSliderInCustomProfile(progress);
            checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT,progress);
        }
        swipeDeaSliderInCustomProfile(DAX_DEA_DEFAULT_VALUE);
        checkCustomFeatureNotChanged();

        swipeDeaSliderInCustomProfile(DAX_DEA_MIN_VALUE);
        checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT,DAX_DEA_MIN_VALUE);

        tabToDynamicProfileView();
        checkUIDynamicMainView();

        tabToCustomProfileView();
        checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT,DAX_DEA_MIN_VALUE);

        tabToMusicProfileView();
        checkMusicFeatureNotChanged();
        selectIeqInMusicProfile(DAX_IEQ_PRESET_WARM);
        checkUIMusicMainView(DAX_IEQ_PRESET_WARM);

        tabToCustomProfileView();
        checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT,DAX_DEA_MIN_VALUE);

        tabToMovieProfileView();
        swipeDeaSliderInMovieProfile(DAX_DEA_MAX_VALUE);
        checkUIMovieMainView(DAX_DEA_MAX_VALUE);

        tabToCustomProfileView();
        checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT,DAX_DEA_MIN_VALUE);

        tabToMusicProfileView();
        checkUIMusicMainView(DAX_IEQ_PRESET_WARM);
        tabToMovieProfileView();
        checkUIMovieMainView(DAX_DEA_MAX_VALUE);

        tabToCustomProfileView();
        checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT,DAX_DEA_MIN_VALUE);

        turnOffDax();
        checkUIOffView();
        turnOnDax();
        tabToCustomProfileView();
        checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT,DAX_DEA_MIN_VALUE);
    }


    @Test
    @DaxUITest
    public void validUIDisplayAfterCustomIEQModified(){
        //step 1
        tabToCustomProfileView();
        resetAllFeatureValueInCustomProfile();
        checkCustomFeatureNotChanged();

        //step 2
        //step 3
        selectIeqInCustomProfile(DAX_IEQ_PRESET_BALANCED);
        checkUICustomMainView(DAX_IEQ_PRESET_BALANCED,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 4
        clickResetInCustomProfile();
        checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 5
        selectIeqInCustomProfile(DAX_IEQ_PRESET_WARM);
        checkUICustomMainView(DAX_IEQ_PRESET_WARM,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 6
        clickResetInCustomProfile();
        checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 7
        selectIeqInCustomProfile(DAX_IEQ_PRESET_DETAILED);
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 8
        clickResetInCustomProfile();
        checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 9
        selectIeqInCustomProfile(DAX_IEQ_PRESET_DETAILED);
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 10
        selectIeqInCustomProfile(DAX_CUSTOM_IEQ_STATUS_DEFAULT);
        checkUICustomMainView(DAX_IEQ_PRESET_OFF,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 11
        selectIeqInCustomProfile(DAX_IEQ_PRESET_WARM);
        checkUICustomMainView(DAX_IEQ_PRESET_WARM,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 12
        selectIeqInCustomProfile(DAX_CUSTOM_IEQ_STATUS_DEFAULT);
        checkUICustomMainView(DAX_IEQ_PRESET_OFF,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 13
        selectIeqInCustomProfile(DAX_IEQ_PRESET_BALANCED);
        checkUICustomMainView(DAX_IEQ_PRESET_BALANCED,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 14
        selectIeqInCustomProfile(DAX_CUSTOM_IEQ_STATUS_DEFAULT);
        checkUICustomMainView(DAX_IEQ_PRESET_OFF,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 15
        selectIeqInCustomProfile(DAX_IEQ_PRESET_WARM);
        selectIeqInCustomProfile(DAX_IEQ_PRESET_WARM);
        checkUICustomMainView(DAX_IEQ_PRESET_WARM,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 16
        selectIeqInCustomProfile(DAX_IEQ_PRESET_BALANCED);
        selectIeqInCustomProfile(DAX_IEQ_PRESET_BALANCED);
        checkUICustomMainView(DAX_IEQ_PRESET_BALANCED,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 17
        selectIeqInCustomProfile(DAX_IEQ_PRESET_DETAILED);
        selectIeqInCustomProfile(DAX_IEQ_PRESET_DETAILED);
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 18
        tabToMovieProfileView();
        checkUIMovieMainView(DAX_DEA_MOVIE_DEFAULT_VALUE);

        //step 19
        tabToCustomProfileView();
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 20
        tabToDynamicProfileView();
        checkUIDynamicMainView();

        //step 21
        tabToCustomProfileView();
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 22
        //step 23
        //step 24
        //step 25
        //step 26
        tabToMusicProfileView();
        checkUIMusicMainView(DAX_MUSIC_IEQ_STATUS_DEFAULT);

        //step 27
        tabToCustomProfileView();
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //step 28
        clickResetInCustomProfile();
        checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT,DAX_DEA_CUSTOM_DEFAULT_VALUE);

        //STEP 29
        checkCustomFeatureNotChanged();
    }

    @Test
    @DaxUITest
    public void validNavigationExploreDolbyDisplay(){
        //custom profile selected and feature status changed
        tabToCustomProfileView();
        selectIeqInCustomProfile(DAX_IEQ_PRESET_DETAILED);
        swipeDeaSliderInCustomProfile(DAX_DEA_MIN_VALUE);
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED,DAX_DEA_MIN_VALUE);

        //navigation to demo item
        checkNavigationItemExploreDolbyView();

        //UI main display and status should be same as one before entry to navigation bar
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED,DAX_DEA_MIN_VALUE);
        resetAllFeatureValueInCustomProfile();
    }


//    @Ignore
    @Test
    @DaxUITest
    public void validNavigationTutorialDisplay(){
        //custom profile selected and feature status changed
        tabToCustomProfileView();
        selectIeqInCustomProfile(DAX_IEQ_PRESET_DETAILED);
        swipeDeaSliderInCustomProfile(DAX_DEA_MIN_VALUE);
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED,DAX_DEA_MIN_VALUE);

        //navigation to demo item
        checkNavigationItemTutorialView();

        //UI main display and status should be same as one before entry to navigation bar
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED,DAX_DEA_MIN_VALUE);
        resetAllFeatureValueInCustomProfile();
    }

    @Test
    @DaxUITest
    public void validValuesRemainsWhenKillAndEnterUIOn(){
        tabToCustomProfileView();
        selectIeqInCustomProfile(DAX_IEQ_PRESET_DETAILED);
        swipeDeaSliderInCustomProfile(DAX_DEA_MIN_VALUE);
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED,DAX_DEA_MIN_VALUE);


        pressHomeBack();
        killDolbyUI();
        startDolbyUI();
        checkUIRestoredAllValuesWhenDSOnExit();
    }

    @Test
    @DaxUITest
    public void validValuesRemainsWhenKillAndEnterUIOff(){
        tabToCustomProfileView();
        selectIeqInCustomProfile(DAX_IEQ_PRESET_DETAILED);
        swipeDeaSliderInCustomProfile(DAX_DEA_MIN_VALUE);
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED,DAX_DEA_MIN_VALUE);

        turnOffDax();
        pressHomeBack();
        killDolbyUI();
        startDolbyUI();
        checkUIRestoredAllValuesWhenDSOffExit();
    }

    private void checkUIRestoredAllValuesWhenDSOnExit(){
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED,DAX_DEA_MIN_VALUE);

        resetAllFeatureValueInCustomProfile();
        tabToDynamicProfileView();
    }


    private void checkUIRestoredAllValuesWhenDSOffExit(){
        checkUIOffView();
        turnOnDax();
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED,DAX_DEA_MIN_VALUE);
        resetAllFeatureValueInCustomProfile();
        // the first test case verify after firstLY flashing the device ,
        // dynamic profile should be first profile
        // this case is the last one ,so we tab to dynamic profile for test case reuse
        tabToDynamicProfileView();
    }

    private void checkNavigationItemExploreDolbyView() {
        //open the navigation view and check display to user
        openNavigationView();
        checkUINavigationView();

        //click the item : explore dolby and then check display to user
        navigateToExploreDolbyView();
        checkUIExploreDolbyView();

        clickExploreDolbyLearnMore();
        exitLearnMoreWebsite();

        clickExploreDolbyAccess();
        exitDolbyAccessWebsite();

        checkUIExploreDolbyView();

        // then press back button and check navigation view display to user
        exitExploreDolbyNavigation();
    }

    private void checkNavigationItemTutorialView() {
        //open the navigation view and check display to user
        openNavigationView();
        checkUINavigationView();

        //click the item :tutorial and then check display to user
        navigateToTutorialView();
        checkUITutorialStartView();

        //swipe the tutorial view
        // for phone there would be three subview and for tablet only two subview
        swipeLeftTutorialNavigation();
        checkUITutorialSecondView();

        swipeLeftTutorialNavigation();
        checkUITutorialEndView();

        swipeLeftTutorialNavigation();
        checkUITutorialEndView();

        //then press back button and check navigation view display to user
        exitTutorialNavigation();
    }
}
