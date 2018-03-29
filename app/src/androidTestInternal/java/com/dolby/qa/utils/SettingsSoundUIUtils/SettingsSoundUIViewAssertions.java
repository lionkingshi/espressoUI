package com.dolby.qa.utils.SettingsSoundUIUtils;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

import static com.dolby.qa.utils.Constants.LAUNCH_TIMEOUT;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_LAUNCH_APP;
import static org.junit.Assert.assertTrue;

public class SettingsSoundUIViewAssertions extends CommonSettingsSoundUIUtils implements ISettingsSoundUIViewAssertions {
    private String LOG_TAG="QASSV-"+SettingsSoundUIViewAction.class.getSimpleName();
    private UiDevice mDevice;

    public SettingsSoundUIViewAssertions(UiDevice mDevice) {
        super(mDevice);
        this.mDevice = mDevice;
    }

    public SettingsSoundUIViewAssertions() {
        super();
        this.mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }


    public void checkSettingsSoundDolbyUIDisplay(){
        checkSettingsSoundDolbyUIDisplay(null);
    }

    public void checkSettingsSoundDolbyUIDisplay(String profileName){
        checkSettingsSoundDolbyUIDisplay(profileName,false);
    }


    private void checkSettingsSoundDolbyUIDisplay(String profileName, boolean fromSecondSoundSettingUI){
        if (mDevice.wait(
                Until.hasObject(By.textContains(SOUND_SETTINGS_LAUNCH_APP)),
                LAUNCH_TIMEOUT/50)){
            if (fromSecondSoundSettingUI){
                assertTrue(LOG_TAG+"failed to turn back to sound settings first ui from second ui !",
                        mDevice.hasObject(By.textContains(SOUND_SETTINGS_LAUNCH_APP)));
                if (profileName != null){
                    assertTrue(LOG_TAG+" settings sound status in first ui did not sync with status in second ui !",
                            mDevice.hasObject(By.textContains(profileName)));
                }
            }else {
                assertTrue(LOG_TAG+"failed to turn back to sound settings ui from consumer ui !",
                        mDevice.hasObject(By.textContains(SOUND_SETTINGS_LAUNCH_APP)));
                if (profileName != null){
                    assertTrue(LOG_TAG+"settings sound status did not sync with status in consumer ui !",
                            mDevice.hasObject(By.textContains(profileName)));
                }
            }
        }else {
            if (fromSecondSoundSettingUI){
                assertTrue(LOG_TAG+": failed to turn back to sound settings first ui from second ui !",false);
            }else {
                assertTrue(LOG_TAG+": failed to turn back to sound settings ui from consumer ui !",false);
            }
        }
    }

}
