package com.dolby.qa.utils.SystemUIUtils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import com.dolby.dax.DolbyAudioEffect;
import com.dolby.qa.utils.consumerUISimpleUtils.ConsumerUISimpleViewAction;
import com.dolby.qa.utils.consumerUISimpleUtils.ConsumerUISimpleViewAssertions;
import com.dolby.qa.utils.consumerUISimpleUtils.IConsumerUISimpleViewAction;
import com.dolby.qa.utils.consumerUISimpleUtils.IConsumerUISimpleViewAssertions;

import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_CUSTOM;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_DYNAMIC;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_MOVIE;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_MUSIC;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DS1_CUSTOM_1;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DS1_CUSTOM_2;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DS1_GAME;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DS1_MOVIE;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DS1_MUSIC;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DS1_VOICE;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_POWER_OFF;
import static com.dolby.qa.utils.Constants.SYSTEM_UI_POWER_OFF;

public class SystemUIWrapper {
    protected String LOG_TAG="QASW-"+SystemUIWrapper.class.getSimpleName();

    public UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    private boolean isDax2 = true ;

    private ISystemUIViewAction viewAction_system_ui;
    private ISystemUIViewAssertions viewAssertions_system_ui;
    private IConsumerUISimpleViewAction viewAction_consumer_ui;
    private IConsumerUISimpleViewAssertions viewAssertions_consumer_ui;

    protected void testCaseSetup(){
        // create view action and assertions instance
        viewAction_system_ui = new SystemUIViewAction(mDevice);
        viewAssertions_system_ui = new SystemUIViewAssertions(mDevice);
        viewAction_consumer_ui = new ConsumerUISimpleViewAction(mDevice);
        viewAssertions_consumer_ui = new ConsumerUISimpleViewAssertions();
        //if device is lock , first unlock it
        viewAction_system_ui.unlockDeviceScreenQuickly();
        //judge current project is dax2 or ds1
        isDax2 = isDAX2UI();
    }

    protected void testCaseTeardown(){

    }

    protected void dolbySystemFirstUIStatusShouldSyncWithSystemSecondUI(){
        if (isDax2){
            transferBetweenSystemFirstAndSecondUI(SOUND_SETTINGS_DAX2_DYNAMIC);
//            transferBetweenSystemFirstAndSecondUI(SOUND_SETTINGS_DAX2_GAME);
            transferBetweenSystemFirstAndSecondUI(SOUND_SETTINGS_DAX2_MOVIE);
            transferBetweenSystemFirstAndSecondUI(SYSTEM_UI_POWER_OFF);
            transferBetweenSystemFirstAndSecondUI(SOUND_SETTINGS_DAX2_MUSIC);
//            transferBetweenSystemFirstAndSecondUI(SOUND_SETTINGS_DAX2_VOICE);
            transferBetweenSystemFirstAndSecondUI(SOUND_SETTINGS_DAX2_CUSTOM);
        }else {
            //turn on dap
            transferBetweenSystemFirstAndSecondUI(SOUND_SETTINGS_DS1_GAME);
            transferBetweenSystemFirstAndSecondUI(SOUND_SETTINGS_DS1_MOVIE);
            transferBetweenSystemFirstAndSecondUI(SOUND_SETTINGS_DS1_MUSIC);
            transferBetweenSystemFirstAndSecondUI(SYSTEM_UI_POWER_OFF);
            transferBetweenSystemFirstAndSecondUI(SOUND_SETTINGS_DS1_CUSTOM_1);
            transferBetweenSystemFirstAndSecondUI(SOUND_SETTINGS_DS1_CUSTOM_2);
            transferBetweenSystemFirstAndSecondUI(SOUND_SETTINGS_DS1_VOICE);
        }
    }

    private void transferBetweenSystemFirstAndSecondUI(String profileName){
        // navigate to system first ui
        viewAction_system_ui.navigateToSystemFirstUI();
        //
        viewAction_system_ui.clickDolbyProfileItemInSystemFirstUI();
        //
        viewAction_system_ui.turnOnDaxInSystemSecondUI();
        if (profileName.equals(SYSTEM_UI_POWER_OFF)){
            viewAction_system_ui.selectProfileInSystemSecondUI(SOUND_SETTINGS_DS1_MUSIC);
            viewAction_system_ui.turnOffDaxInSystemSecondUI();
        }else {
            viewAction_system_ui.selectProfileInSystemSecondUI(profileName);
        }
        //
        viewAction_system_ui.clickDoneItemFromSystemSecondUI();
        viewAssertions_system_ui.checkSystemFirstUIDisplay(profileName,true);
        // click dolby icon once
        viewAction_system_ui.clickDolbyIconItemInSystemFirstUI();
        if (profileName.equals(SYSTEM_UI_POWER_OFF)){
            viewAssertions_system_ui.checkSystemFirstUIDisplay(SOUND_SETTINGS_DS1_MUSIC,true);
        }else {
            viewAssertions_system_ui.checkSystemFirstUIDisplay(SYSTEM_UI_POWER_OFF,true);
        }
        // navigate to system second ui and verify the status
        viewAction_system_ui.clickDolbyProfileItemInSystemFirstUI();
        if (profileName.equals(SYSTEM_UI_POWER_OFF)){
            viewAssertions_system_ui.checkSystemSecondUIDisplay(SOUND_SETTINGS_DS1_MUSIC);
        }else {
            viewAssertions_system_ui.checkSystemSecondUIDisplay(SYSTEM_UI_POWER_OFF);
        }
        // turn back to system first ui
        viewAction_system_ui.clickDoneItemFromSystemSecondUI();
        if (profileName.equals(SYSTEM_UI_POWER_OFF)){
            viewAssertions_system_ui.checkSystemFirstUIDisplay(SOUND_SETTINGS_DS1_MUSIC,true);
        }else {
            viewAssertions_system_ui.checkSystemFirstUIDisplay(SYSTEM_UI_POWER_OFF,true);
        }
        // click dolby icon once and again
        viewAction_system_ui.clickDolbyIconItemInSystemFirstUI();
        if (profileName.equals(SYSTEM_UI_POWER_OFF)){
            viewAssertions_system_ui.checkSystemFirstUIDisplay(SYSTEM_UI_POWER_OFF,true);
        }else {
            viewAssertions_system_ui.checkSystemFirstUIDisplay(profileName,true);
        }
        // click dolby icon again and twice
        viewAction_system_ui.clickDolbyIconItemInSystemFirstUI();
        viewAction_system_ui.clickDolbyIconItemInSystemFirstUI();
        if (profileName.equals(SYSTEM_UI_POWER_OFF)){
            viewAssertions_system_ui.checkSystemFirstUIDisplay(SYSTEM_UI_POWER_OFF,true);
        }else {
            viewAssertions_system_ui.checkSystemFirstUIDisplay(profileName,true);
        }
        //return the home screen
        mDevice.pressHome();
    }

    protected void dolbySystemUIStatusShouldSyncWithConsumerUI() {
        if (isDax2){
            //turn ds on
//            selectProfileInSystemUIAndNavigateToVerifyConsumerUIDisplay(SOUND_SETTINGS_DAX2_GAME);

            selectProfileInSystemUIAndNavigateToVerifyConsumerUIDisplay(SOUND_SETTINGS_DAX2_DYNAMIC);

            selectProfileInSystemUIAndNavigateToVerifyConsumerUIDisplay(SOUND_SETTINGS_DAX2_MUSIC);

            //turn ds off
            selectProfileInSystemUIAndNavigateToVerifyConsumerUIDisplay(SYSTEM_UI_POWER_OFF);

            //turn ds on
//            selectProfileInSystemUIAndNavigateToVerifyConsumerUIDisplay(SOUND_SETTINGS_DAX2_VOICE);

            selectProfileInSystemUIAndNavigateToVerifyConsumerUIDisplay(SOUND_SETTINGS_DAX2_MOVIE);

            selectProfileInSystemUIAndNavigateToVerifyConsumerUIDisplay(SOUND_SETTINGS_DAX2_CUSTOM);

            //return the home screen
            mDevice.pressHome();
        }else {
            //turn ds on
            selectProfileInSystemUIAndNavigateToVerifyConsumerUIDisplay(SOUND_SETTINGS_DS1_GAME);

            selectProfileInSystemUIAndNavigateToVerifyConsumerUIDisplay(SOUND_SETTINGS_DS1_CUSTOM_1);

            selectProfileInSystemUIAndNavigateToVerifyConsumerUIDisplay(SOUND_SETTINGS_DS1_MUSIC);

            //turn ds off
            selectProfileInSystemUIAndNavigateToVerifyConsumerUIDisplay(SYSTEM_UI_POWER_OFF);

            //turn ds on
            selectProfileInSystemUIAndNavigateToVerifyConsumerUIDisplay(SOUND_SETTINGS_DS1_VOICE);

            selectProfileInSystemUIAndNavigateToVerifyConsumerUIDisplay(SOUND_SETTINGS_DS1_MOVIE);

            selectProfileInSystemUIAndNavigateToVerifyConsumerUIDisplay(SOUND_SETTINGS_DS1_CUSTOM_2);

            //return the home screen
            mDevice.pressHome();
        }
    }

    protected void dolbyConsumerUIStatusShouldSyncWithSystemUI(){
        if (isDax2){
            selectProfileInConsumerUIAndNavigateToVerifySystemUIDisplay(SOUND_SETTINGS_DAX2_CUSTOM);

            selectProfileInConsumerUIAndNavigateToVerifySystemUIDisplay(SOUND_SETTINGS_DAX2_DYNAMIC);

//            selectProfileInConsumerUIAndNavigateToVerifySystemUIDisplay(SOUND_SETTINGS_DAX2_VOICE);

            selectProfileInConsumerUIAndNavigateToVerifySystemUIDisplay(SOUND_SETTINGS_DAX2_MOVIE);

            selectProfileInConsumerUIAndNavigateToVerifySystemUIDisplay(SYSTEM_UI_POWER_OFF);

            selectProfileInConsumerUIAndNavigateToVerifySystemUIDisplay(SOUND_SETTINGS_DAX2_MUSIC);

//            selectProfileInConsumerUIAndNavigateToVerifySystemUIDisplay(SOUND_SETTINGS_DAX2_GAME);
        }else {
            selectProfileInConsumerUIAndNavigateToVerifySystemUIDisplay(SOUND_SETTINGS_DS1_CUSTOM_2);

            selectProfileInConsumerUIAndNavigateToVerifySystemUIDisplay(SOUND_SETTINGS_DS1_CUSTOM_1);

            selectProfileInConsumerUIAndNavigateToVerifySystemUIDisplay(SOUND_SETTINGS_DS1_VOICE);

            selectProfileInConsumerUIAndNavigateToVerifySystemUIDisplay(SOUND_SETTINGS_DS1_MOVIE);

            selectProfileInConsumerUIAndNavigateToVerifySystemUIDisplay(SYSTEM_UI_POWER_OFF);

            selectProfileInConsumerUIAndNavigateToVerifySystemUIDisplay(SOUND_SETTINGS_DS1_MUSIC);

            selectProfileInConsumerUIAndNavigateToVerifySystemUIDisplay(SOUND_SETTINGS_DS1_GAME);
        }
    }

    private void selectProfileInSystemUIAndNavigateToVerifyConsumerUIDisplay(String profileName) {
        // first step : navigate to system second ui and select expected profile or turn off dax
        viewAction_system_ui.navigateToSystemFirstUI();
        viewAction_system_ui.clickDolbyProfileItemInSystemFirstUI();
        if (profileName.equals(SOUND_SETTINGS_POWER_OFF)){
            viewAction_system_ui.turnOffDaxInSystemSecondUI();
            viewAssertions_system_ui.checkSystemSecondUIDisplay(SYSTEM_UI_POWER_OFF);
        }else {
            viewAction_system_ui.turnOnDaxInSystemSecondUI();
            viewAction_system_ui.selectProfileInSystemSecondUI(profileName);
            viewAssertions_system_ui.checkSystemSecondUIDisplay(profileName);
        }

        // second step : turn back to previous UI to check profile name is same as expected
        viewAction_system_ui.clickDoneItemFromSystemSecondUI();
        // viewAction_system_ui.pressBackButton();
        viewAssertions_system_ui.checkSystemFirstUIDisplay(profileName,true);

        // third step : check Consumer UI dap status same as system ui status
        // click more settings to make consumer ui launch
        viewAction_system_ui.clickDolbyProfileItemInSystemFirstUI();
        viewAction_system_ui.clickMoreSettingsItemInSystemSecondUI();
        //check consumer ui sync with sound settings ui status
        viewAssertions_consumer_ui.checkConsumerUIDisplay(profileName);

        // fourth step : turn back to sound settings ui from consumer ui and check
        viewAction_system_ui.pressBackButton();
        viewAction_system_ui.navigateToSystemFirstUI();
        viewAssertions_system_ui.checkSystemFirstUIDisplay(profileName,false);

        //return the home screen
        mDevice.pressHome();
    }

    private void selectProfileInConsumerUIAndNavigateToVerifySystemUIDisplay(String profileName){

        // first step : launch consumer ui
        viewAction_consumer_ui.navigateToConsumerUIDirectly();

        // second step : check sync status between consumer ui and sound settings ui
        if (profileName.equals(SYSTEM_UI_POWER_OFF)){
            // check dap off sync status
            checkOffStatus(profileName);
        }
        else {
            // check dap on sync status
            checkOnStatus(profileName);
        }
        //return the home screen
        mDevice.pressHome();
    }

    private void checkOffStatus(String profileName){
        // dap status should be off
        viewAction_consumer_ui.turnOffDaxFromConsumerUI();

        // check dap is off in consumer ui
        viewAssertions_consumer_ui.checkConsumerUIDisplay(SOUND_SETTINGS_POWER_OFF);

        // navigate to System first ui
        viewAction_system_ui.navigateToSystemFirstUI();
        viewAction_system_ui.waitForSecond(1);

        // then check System UI status shall sync with Consumer UI
        viewAssertions_system_ui.checkSystemFirstUIDisplay(profileName,false);
    }

    private void checkOnStatus(String profileName){
        //dap status should be on
        viewAction_consumer_ui.turnOnDaxFromConsumerUI();
        //select profile in consumer ui
        viewAction_consumer_ui.selectProfileFromConsumerUI(profileName);
        // check consumer ui status
        viewAssertions_consumer_ui.checkConsumerUIDisplay(profileName);
        //check Consumer UI status sync with system ui
        // navigate to System first ui
        viewAction_system_ui.navigateToSystemFirstUI();
        viewAction_system_ui.waitForSecond(1);
        // then check System UI status shall sync with Consumer UI
        viewAssertions_system_ui.checkSystemFirstUIDisplay(profileName,false);
    }

    private boolean isDAX2UI(){
        /* Dummy AudioTrack for effect */
        AudioTrack mAT = new AudioTrack(AudioManager.STREAM_MUSIC,
                48000, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                AudioTrack.getMinBufferSize(48000, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT), AudioTrack.MODE_STATIC);
        DolbyAudioEffect newDapEffect = new DolbyAudioEffect(0, mAT.getAudioSessionId());
        String version = newDapEffect.getDsVersion().substring(0,3);
        if (version.equals("DS1")){
            isDax2 = false ;
        }else {
            isDax2 = true ;
        }
        if (newDapEffect != null){
            newDapEffect.release();
        }
        return  isDax2;
    }

}
