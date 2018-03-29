package com.dolby.qa.utils.consumerUIUtils;

import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.dolby.daxappui.MainActivity;
import com.dolby.qa.utils.commonUtils.UIConfiguration;

import static com.dolby.qa.utils.Constants.DAX3_PROFILE_CUSTOM;
import static com.dolby.qa.utils.Constants.DAX_CUSTOM_IEQ_STATUS_DEFAULT;
import static com.dolby.qa.utils.Constants.DAX_DEA_DEFAULT_VALUE;
import static com.dolby.qa.utils.Constants.DAX_DEA_MIN_VALUE;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_DETAILED;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_WARM;
import static com.dolby.qa.utils.Constants.DAX_MUSIC_IEQ_STATUS_DEFAULT;
import static com.dolby.qa.utils.Constants.DAX_PROFILE_DYNAMIC;
import static com.dolby.qa.utils.Constants.DAX_PROFILE_MOVIE;
import static com.dolby.qa.utils.Constants.DAX_PROFILE_MUSIC;
import static com.dolby.qa.utils.Constants.DAX_STATUS_OFF;
import static com.dolby.qa.utils.Constants.DEA_INDEX_IN_CONFIG_ARRAY;
import static com.dolby.qa.utils.Constants.PACKAGE_NAME_UNDER_TEST;
import static com.dolby.qa.utils.Constants.SIZE_NUM_OF_CONFIG_ARRAY;
import static com.dolby.qa.utils.Constants.TAG;

public abstract class ConsumerUIWrapper implements IConsumerUIViewAction, IConsumerUIViewAssertions{
    private static final String LOG_TAG = TAG;

    protected ActivityTestRule<MainActivity> mActivityRule;

    private UIConfiguration uiConfiguration;

    public ConsumerUIWrapper() {
        Log.d("QA-Constructor",LOG_TAG);
    }

    protected void daxTestCaseSetUp(){
        Log.d(LOG_TAG,"each test case set up with annotation @before");
        // get current device config : endpoint and speaker type
        uiConfiguration=new UIConfiguration(mActivityRule);
        ConsumerUIViewAction.setUiConfiguration(uiConfiguration);
        ConsumerUIViewAssertions.setUiConfiguration(uiConfiguration);
    }

    //each test individually store corresponding log information
    protected void daxTestCaseTearDown(){
        Log.d(LOG_TAG,"each test case tear down with annotation @after");
        if (uiConfiguration!=null){
            uiConfiguration.clearConfig();
            uiConfiguration=null;
        }
    }

    public void tabToDynamicProfileView(){
        ConsumerUIViewAction.goToFirstProfileView();
    }

    public void tabToMovieProfileView(){
        ConsumerUIViewAction.goToSecondProfileView();
    }

    public void tabToMusicProfileView(){
        ConsumerUIViewAction.goToThirdProfileView();
    }

    public void tabToCustomProfileView(){
        ConsumerUIViewAction.goToFourthProfileView();
    }

    public void selectIeqInCustomProfile(int preset){
//        tabToCustomProfileView();
        ConsumerUIViewAction.selectIntelligentEqualizer(preset);
    }

    public void clickDeaSliderInCustomProfile(int position){
        ConsumerUIViewAction.clickDialogueEnhancerAmountButton(position);
    }

    public void swipeDeaSliderInCustomProfile(int position){
        ConsumerUIViewAction.swipeDialogueEnhancerAmountButton(position);
    }

    public void clickResetInCustomProfile(){
//        tabToCustomProfileView();
        ConsumerUIViewAction.clickProfileBasedResetButton();
    }

    public void selectIeqInMusicProfile(int preset){
//        tabToMusicProfileView();
        ConsumerUIViewAction.selectIntelligentEqualizer(preset);
    }

    public void clickResetInMusicProfile(){
//        tabToMusicProfileView();
        ConsumerUIViewAction.clickProfileBasedResetButton();
    }

    public void clickDeaSliderInMovieProfile(int position){
//        tabToMusicProfileView();
        ConsumerUIViewAction.clickDialogueEnhancerAmountButton(position);
    }

    public void swipeDeaSliderInMovieProfile(int position){
        ConsumerUIViewAction.swipeDialogueEnhancerAmountButton(position);
    }

    public void clickResetInMovieProfile(){
//        tabToMusicProfileView();
        ConsumerUIViewAction.clickProfileBasedResetButton();
    }

    public void clickResetInDynamicProfile(){
//        tabToMusicProfileView();
        ConsumerUIViewAction.clickProfileBasedResetButton();
    }

    public void turnOnDax(){
        ConsumerUIViewAction.turnOnUI();
    }

    public void turnOffDax(){
        ConsumerUIViewAction.turnOffUI();
    }

    public void openNavigationView(){
        ConsumerUIViewAction.openNavigationView();
    }

    public void exitNavigationView(){
        ConsumerUIViewAction.exitNavigationView();
    }

    public void navigateToHeadphoneView(){
        openNavigationView();
        ConsumerUIViewAction.navigateToHeadphoneView();
    }

    public void exitHeadphoneNavigation(){
        ConsumerUIViewAction.exitHeadphoneNavigation();
    }

    public void navigateToExploreDolbyView(){
        openNavigationView();
        ConsumerUIViewAction.navigateToExploreDolbyView();
    }

    public void exitExploreDolbyNavigation(){
        ConsumerUIViewAction.exitExploreDolbyNavigation();
    }

    public void clickExploreDolbyLearnMore(){
        ConsumerUIViewAction.clickExploreDolbyLearnMore();
    }

    public void exitLearnMoreWebsite(){
        ConsumerUIViewAction.exitLearnMoreWebsite();
    }

    public void clickExploreDolbyAccess(){
        ConsumerUIViewAction.clickExploreDolbyAccess();
    }

    public void exitDolbyAccessWebsite(){
        ConsumerUIViewAction.exitDolbyAccessWebsite();
    }

    public void navigateToTutorialView(){
        openNavigationView();
        ConsumerUIViewAction.navigateToTutorialView();
    }

    public void exitTutorialNavigation(){
        ConsumerUIViewAction.exitTutorialNavigation();
    }

    public void swipeLeftTutorialNavigation(){
        ConsumerUIViewAction.swipeLeftTutorialNavigation();
    }

    public void swipeRightTutorialNavigation(){
        ConsumerUIViewAction.swipeRightTutorialNavigation();
    }

    public void navigationToResetView(){
        openNavigationView();
        ConsumerUIViewAction.navigateToResetView();
    }

    public void exitResetNavigation(){
        ConsumerUIViewAction.exitResetNavigation();
    }

    public void clickResetInResetNavigation(){
        ConsumerUIViewAction.clickResetButtonInResetNavigation();
    }

    public void checkUIMainView(
            int profileType,
            int isDeaNum,
            int isIeqNum,
            boolean isFlatGEQ,
            int [] config){
        int[] mConfig = new int[SIZE_NUM_OF_CONFIG_ARRAY] ;
        mConfig[DEA_INDEX_IN_CONFIG_ARRAY] = isDeaNum ;
        ConsumerUIViewAssertions.
                checkMainViewDisplayAndStatus(
                        profileType,
                        true,
                        true,
                        isIeqNum,
                        isFlatGEQ,
                        true,
                        true,
                        mConfig);
    }

    public void checkUIOffView(){
        ConsumerUIViewAssertions.checkDaxOnOffStatus(DAX_STATUS_OFF);
    }

    public void checkUINavigationView(){
        ConsumerUIViewAssertions.checkNavigationMainViewDisplayAndStatus();
    }

    public void checkUIHeadphoneView(){
        ConsumerUIViewAssertions.checkNaviHeadphoneView();
    }

    public void checkUIExploreDolbyView(){
        ConsumerUIViewAssertions.checkNaviExploreDolbyView();
    }

    public void checkUITutorialStartView(){
        if (ConsumerUIViewAction.waitForFirstTutorialNavigationView()){
            ConsumerUIViewAssertions.checkFirstTutorialView();
        }
    }

    public void checkUITutorialSecondView(){
        if (isTabletDevice()){
            if (ConsumerUIViewAction.waitForSecondTutorialNavigationView()) {
                if (ConsumerUIViewAction.waitForEndTutorialNavigationView()) {
                    ConsumerUIViewAssertions.checkSecondTutorialView();
                }
            }
        }else {
            if (ConsumerUIViewAction.waitForSecondTutorialNavigationView()) {
                ConsumerUIViewAssertions.checkSecondTutorialView();
            }
        }
    }

    public void checkUITutorialEndView(){
        if (isTabletDevice()){
            if (ConsumerUIViewAction.waitForSecondTutorialNavigationView()) {
                if (ConsumerUIViewAction.waitForEndTutorialNavigationView()) {
                    ConsumerUIViewAssertions.checkLastTutorialView();
                }
            }
        }else {
            if (ConsumerUIViewAction.waitForEndTutorialNavigationView()) {
                ConsumerUIViewAssertions.checkLastTutorialView();
            }
        }
    }

    public void checkUIResetView(){
        ConsumerUIViewAssertions.checkNaviResetView();
    }


    public void checkUIDynamicMainView(){
        checkUIMainView(DAX_PROFILE_DYNAMIC,0,0,true,null);
    }

    protected void checkUIMovieMainView(int deaExpectedProgress){
        checkUIMainView(DAX_PROFILE_MOVIE,deaExpectedProgress,0,true,null);
    }

    protected void checkMovieFeatureNotChanged(){
        checkUIMovieMainView(DAX_DEA_DEFAULT_VALUE);
    }


    protected void checkUIMusicMainView(int isIeqNum){
        checkUIMainView(DAX_PROFILE_MUSIC,0,isIeqNum,true,null);
    }

    protected void checkMusicFeatureNotChanged(){
        checkUIMusicMainView(DAX_MUSIC_IEQ_STATUS_DEFAULT);
    }

    protected void checkUICustomMainView(int isIeqNum,int isDeaNum){
        checkUIMainView(DAX3_PROFILE_CUSTOM,isDeaNum,isIeqNum,true,null);
    }

    protected void checkCustomFeatureNotChanged(){
        checkUICustomMainView(DAX_CUSTOM_IEQ_STATUS_DEFAULT,DAX_DEA_DEFAULT_VALUE);
    }

    protected void setAllFeaturesInCustomProfile(){
        // tab to custom profile and click the reset button to set all feature values as default
        resetAllFeatureValueInCustomProfile();

        // click all feature button to make its values opposite to default values
        selectIeqInCustomProfile(DAX_IEQ_PRESET_DETAILED);
//        checkCustomFeatureAllChanged();
    }

    protected void resetAllFeatureValueInCustomProfile(){
        // tab to custom profile and click the reset button to set all feature values as default
        tabToCustomProfileView();
        clickResetInCustomProfile();
        checkCustomFeatureNotChanged();
    }

    protected void resetDaxAllFeatureValue(){
        // reset custom view
        tabToCustomProfileView();
        clickResetInCustomProfile();
        //reset music view
        tabToMusicProfileView();
        clickResetInMusicProfile();
        // reset movie view
        tabToMovieProfileView();
        clickResetInMovieProfile();
        // reset dynamic view
        tabToDynamicProfileView();
        clickResetInDynamicProfile();
    }

    protected void changeDaxAllFeatureValue(){
        // reset custom view
        tabToCustomProfileView();
        selectIeqInCustomProfile(DAX_IEQ_PRESET_DETAILED);
        swipeDeaSliderInCustomProfile(DAX_DEA_MIN_VALUE);
        //reset music view
        tabToMusicProfileView();
        selectIeqInCustomProfile(DAX_IEQ_PRESET_WARM);
        // reset movie view
        tabToMovieProfileView();
        swipeDeaSliderInMovieProfile(DAX_DEA_MIN_VALUE);
        // reset dynamic view
        tabToDynamicProfileView();
        clickResetInDynamicProfile();
    }

    protected void checkDaxAllFeatureValueChanged(){
        tabToCustomProfileView();
        checkUICustomMainView(DAX_IEQ_PRESET_DETAILED,DAX_DEA_MIN_VALUE);

        tabToMusicProfileView();
        checkUIMusicMainView(DAX_IEQ_PRESET_WARM);

        tabToMovieProfileView();
        checkUIMovieMainView(DAX_DEA_MIN_VALUE);

        tabToDynamicProfileView();
        checkUIDynamicMainView();
    }

    protected void checkDaxAllFeatureValueNotChanged(){

        tabToCustomProfileView();
        checkCustomFeatureNotChanged();

        tabToMusicProfileView();
        checkMusicFeatureNotChanged();

        tabToMovieProfileView();
        checkMovieFeatureNotChanged();

        tabToDynamicProfileView();
        checkUIDynamicMainView();
    }


    protected boolean getSoundVirtualizerButtonEnabledStatus(){
        return uiConfiguration.judgeSVInCustomProfileIsEnableOrNot();
    }

    protected boolean isTabletDevice(){
        return uiConfiguration.isTablet();
    }

    protected boolean isDax2LiteUI(){
        return !uiConfiguration.isDax2();
    }

    protected void pressHomeBack(){
        ConsumerUIViewAction.pressDeviceHomeBack();
    }

    protected void killDolbyUI(){
        ConsumerUIViewAction.killApps();
    }

    protected void startDolbyUI(){
        ConsumerUIViewAction.startActivityFromHomeScreen(PACKAGE_NAME_UNDER_TEST);
    }
}
