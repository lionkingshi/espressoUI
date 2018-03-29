package com.dolby.qa.utils.SettingsSoundUIUtils;

import android.content.Intent;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

import static com.dolby.qa.utils.Constants.LAUNCH_TIMEOUT;
import static com.dolby.qa.utils.Constants.PACKAGE_NAME_SETTINGS_SOUND;
import static com.dolby.qa.utils.Constants.PACKAGE_NAME_UNDER_TEST;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_LAUNCH_APP;
import static com.dolby.qa.utils.Constants.TAG;
import static org.junit.Assert.assertTrue;

public class SettingsSoundUIViewAction extends CommonSettingsSoundUIUtils implements ISettingsSoundUIViewAction {

    public String LOG_TAG = TAG;

    private UiDevice mDevice;

    public SettingsSoundUIViewAction(UiDevice mDevice) {
        super();
        this.mDevice = mDevice;
    }

    public void navigateToSettingsSoundDolbyUI(){
        // navigate to sound settings first ui
        if (!mDevice.hasObject(By.textContains(SOUND_SETTINGS_LAUNCH_APP))){
            // if in the sound settings second ui , press back button to navigate to first ui
            if (mDevice.hasObject(By.res(PACKAGE_NAME_SETTINGS_SOUND,"dolby_power"))){
                pressBackButton();
            }else {
                launchSettingsSoundFirstUIDirectly();
            }
        }else {
            // already in the expected ui and do nothing
        }

        //check launch sound settings first ui successfully
        if (mDevice.wait(
                Until.hasObject(By.textContains(SOUND_SETTINGS_LAUNCH_APP)),
                LAUNCH_TIMEOUT/2)){
            assertTrue(LOG_TAG+"fail to launch sound settings first ui !",
                    mDevice.hasObject(By.text(SOUND_SETTINGS_LAUNCH_APP)));
            // for dax3 project , the profile item not exist already
//            assertTrue(LOG_TAG+"fail to launch sound settings first ui !",
//                    mDevice.hasObject(By.text(SOUND_SETTINGS_PROFILE)));
        }else {
            assertTrue(LOG_TAG+"fail to launch settings sound first ui !",
                    false);
        }
    }

    public void clickSettingsSoundDolbyUILaunchAppItem(){
        navigateToConsumerUIFromSettingsSoundUI();
    }

    private void launchSettingsSoundFirstUIDirectly(){
        //navigate to the Sound Settings Dolby UI panel
        // send intent to start the ui
        startSettingSoundFirstUIFromHomeScreen();
        waitForSecond(1);

        //if navigate to sound setting second ui , press back to go to first ui
        if (mDevice.hasObject(By.res(PACKAGE_NAME_SETTINGS_SOUND,"dolby_power"))){
            pressBackButton();
        }
    }

    private void startSettingSoundFirstUIFromHomeScreen(){
        // Start from the home screen
        mDevice.pressHome();

        // Launch the app
        Intent settingsSoundIntent=new Intent(Settings.ACTION_SOUND_SETTINGS);
        settingsSoundIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        InstrumentationRegistry.getContext().startActivity(settingsSoundIntent);

        mDevice.wait(Until.hasObject(By.pkg(PACKAGE_NAME_SETTINGS_SOUND).depth(0)),
                LAUNCH_TIMEOUT);
    }

    private void navigateToConsumerUIFromSettingsSoundUI(){
        // click launch app button in sound settings ui to make consumer ui launch
        if (mDevice.wait(Until.hasObject(By.textContains(SOUND_SETTINGS_LAUNCH_APP)),
                LAUNCH_TIMEOUT/50)){
            mDevice.findObject(By.textContains(SOUND_SETTINGS_LAUNCH_APP)).click();
            waitForSecond(3);
            // add code to select dax ui we want to launch
            // because we use diff build variant
            selectAPP();
        }else {
            assertTrue(LOG_TAG+": failed to turn back to sound settings ui from consumer ui !",false);
        }

        // check launch consumer ui successfully
        if (mDevice.wait(Until.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"dsLogoText")),
                LAUNCH_TIMEOUT)){
            assertTrue(LOG_TAG+": fail to navigate to consumer ui form sound settings ui !",
                    mDevice.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"dsLogoText")));
        }else {
            assertTrue(LOG_TAG+": fail to navigate to consumer ui form sound settings ui !",
                    false);
        }
    }

}
