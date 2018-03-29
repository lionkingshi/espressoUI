package com.dolby.qa.utils.SettingsSoundUIUtils;

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
import static com.dolby.qa.utils.Constants.TAG;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class SettingsSoundUIWrapper {
    protected String LOG_TAG = TAG;

    public UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    private boolean isDax2 = true ;

    private ISettingsSoundUIViewAction viewAction_settings_sound_ui;
    private ISettingsSoundUIViewAssertions viewAssertions_settings_sound_ui;
    private IConsumerUISimpleViewAction viewAction_consumer_ui;
    private IConsumerUISimpleViewAssertions viewAssertions_consumer_ui;

    protected void testCaseSetup(){
        // create view action and assertions instance
        viewAction_settings_sound_ui = new SettingsSoundUIViewAction(mDevice);
        viewAssertions_settings_sound_ui = new SettingsSoundUIViewAssertions(mDevice);
        viewAction_consumer_ui = new ConsumerUISimpleViewAction(mDevice);
        viewAssertions_consumer_ui = new ConsumerUISimpleViewAssertions();
        //if device is lock , first unlock it
        viewAction_settings_sound_ui.unlockDeviceScreenQuickly();
        //judge current project is dax2 or ds1
        isDax2 = isDAX2UI();
    }

    public void testCaseTeardown(){

    }

    public void settingsSoundDolbyUILaunchConsumerUICorrectly(){
        if (isDax2){
            selectProfileInConsumerUIThenNavigateToSettingsSoundUIAndClickLaunchAppItem(SOUND_SETTINGS_DAX2_CUSTOM);

            selectProfileInConsumerUIThenNavigateToSettingsSoundUIAndClickLaunchAppItem(SOUND_SETTINGS_DAX2_DYNAMIC);

//            selectProfileInConsumerUIThenNavigateToSettingsSoundUIAndClickLaunchAppItem(SOUND_SETTINGS_DAX2_VOICE);

            selectProfileInConsumerUIThenNavigateToSettingsSoundUIAndClickLaunchAppItem(SOUND_SETTINGS_DAX2_MOVIE);

            selectProfileInConsumerUIThenNavigateToSettingsSoundUIAndClickLaunchAppItem(SOUND_SETTINGS_POWER_OFF);

            selectProfileInConsumerUIThenNavigateToSettingsSoundUIAndClickLaunchAppItem(SOUND_SETTINGS_DAX2_MUSIC);

//            selectProfileInConsumerUIThenNavigateToSettingsSoundUIAndClickLaunchAppItem(SOUND_SETTINGS_DAX2_GAME);
        }else {
            selectProfileInConsumerUIThenNavigateToSettingsSoundUIAndClickLaunchAppItem(SOUND_SETTINGS_DS1_CUSTOM_2);

            selectProfileInConsumerUIThenNavigateToSettingsSoundUIAndClickLaunchAppItem(SOUND_SETTINGS_DS1_CUSTOM_1);

            selectProfileInConsumerUIThenNavigateToSettingsSoundUIAndClickLaunchAppItem(SOUND_SETTINGS_DS1_VOICE);

            selectProfileInConsumerUIThenNavigateToSettingsSoundUIAndClickLaunchAppItem(SOUND_SETTINGS_DS1_MOVIE);

            selectProfileInConsumerUIThenNavigateToSettingsSoundUIAndClickLaunchAppItem(SOUND_SETTINGS_POWER_OFF);

            selectProfileInConsumerUIThenNavigateToSettingsSoundUIAndClickLaunchAppItem(SOUND_SETTINGS_DS1_MUSIC);

            selectProfileInConsumerUIThenNavigateToSettingsSoundUIAndClickLaunchAppItem(SOUND_SETTINGS_DS1_GAME);
        }
    }

    private void selectProfileInConsumerUIThenNavigateToSettingsSoundUIAndClickLaunchAppItem(String profileName){
        assertThat(profileName,notNullValue());

        // first step : launch consumer ui
        viewAction_consumer_ui.navigateToConsumerUIDirectly();

        // second step : check sync status between consumer ui and sound settings ui
        if (profileName.equals(SOUND_SETTINGS_POWER_OFF)){
            // check dap off sync status
            checkOffStatus(profileName);
        }
        else {
            // check dap on sync status
            checkOnStatus(profileName);
        }
    }

    private void checkOffStatus(String profileName){
        // dap status should be off
        viewAction_consumer_ui.turnOffDaxFromConsumerUI();

        // check dap is off in consumer ui
        viewAssertions_consumer_ui.checkConsumerUIDisplay(SOUND_SETTINGS_POWER_OFF);

        // navigate to Settings Sound first ui and click the launch app item to launch consumer ui
        viewAction_settings_sound_ui.navigateToSettingsSoundDolbyUI();
        viewAssertions_settings_sound_ui.checkSettingsSoundDolbyUIDisplay();
        viewAction_settings_sound_ui.waitForSecond(1);
        viewAction_settings_sound_ui.clickSettingsSoundDolbyUILaunchAppItem();

        // then check Consumer UI profile same as previous settings
        viewAssertions_consumer_ui.checkConsumerUIDisplay(SOUND_SETTINGS_POWER_OFF);
    }

    private void checkOnStatus(String profileName){
        //dap status should be on
        viewAction_consumer_ui.turnOnDaxFromConsumerUI();
        //select profile in consumer ui
        viewAction_consumer_ui.selectProfileFromConsumerUI(profileName);
        // check consumer ui status
        viewAssertions_consumer_ui.checkConsumerUIDisplay(profileName);
        // navigate to Settings Sound first ui and click Launch App to invoke consumer app display
        viewAction_settings_sound_ui.navigateToSettingsSoundDolbyUI();
        viewAction_settings_sound_ui.waitForSecond(1);
        viewAssertions_settings_sound_ui.checkSettingsSoundDolbyUIDisplay();
        viewAction_settings_sound_ui.clickSettingsSoundDolbyUILaunchAppItem();

        // then check Consumer UI profile same as previous settings
        viewAssertions_consumer_ui.checkConsumerUIDisplay(profileName);
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
