package com.dolby.qa.utils.SystemUIUtils;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

import static com.dolby.qa.utils.Constants.LAUNCH_TIMEOUT;
import static com.dolby.qa.utils.Constants.PACKAGE_NAME_SYSTEM_UI;
import static com.dolby.qa.utils.Constants.SYSTEM_UI_POWER_OFF;
import static org.junit.Assert.assertTrue;

public class SystemUIViewAssertions extends CommonSystemUIUtils implements ISystemUIViewAssertions {

    private String LOG_TAG="QASUV-"+SystemUIViewAssertions.class.getSimpleName();
    private UiDevice mDevice;

    public SystemUIViewAssertions(UiDevice mDevice) {
        super(mDevice);
        this.mDevice = mDevice;
    }

    public SystemUIViewAssertions() {
        super();
        this.mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Override
    public void checkSystemFirstUIDisplay(String profileName, boolean fromSecondSystemUI) {
        // wait for system ui display
        if (mDevice.wait(Until.hasObject(By.res(PACKAGE_NAME_SYSTEM_UI,"slider")),
                LAUNCH_TIMEOUT/2)){
            assertTrue(LOG_TAG+": fail to open quick settings!",
                    mDevice.hasObject(By.res(PACKAGE_NAME_SYSTEM_UI,"slider")));
            // check system first ui : profile name object display expected string
            if (fromSecondSystemUI){
                assertTrue(LOG_TAG+"failed to turn back to system first ui from second ui !",
                        judgeDolbyIconInQuickSettingsUILeftTopPosition(profileName,true));

                assertTrue(LOG_TAG+" : status in first ui did not sync with status in second ui !",
                        mDevice.hasObject(By.textContains(profileName)));

            }else {
                assertTrue(LOG_TAG+"failed to turn back to sound settings ui from consumer ui !",
                        judgeDolbyIconInQuickSettingsUILeftTopPosition(profileName,true));
                assertTrue(LOG_TAG+"settings sound status did not sync with status in consumer ui !",
                        mDevice.hasObject(By.textContains(profileName)));
            }
        }else {
            if (fromSecondSystemUI){
                assertTrue(LOG_TAG+": failed to turn back to system first ui from second ui !",false);
            }else {
                assertTrue(LOG_TAG+": failed to turn back to system ui from consumer ui !",false);
            }
        }
    }

    @Override
    public void checkSystemSecondUIDisplay(String profileName) {
        // sure that is system second ui
        waitForSystemSecondUIRender();

        // dolby off and on ui display is diff
        if (profileName.equals(SYSTEM_UI_POWER_OFF)){
            //check quick settings ui off display
            if (mDevice.wait( Until.hasObject(By.textContains("OFF").checked(false)),
                    LAUNCH_TIMEOUT/50)){

                assertTrue(LOG_TAG+"turn off ds status failed ! ",
                        mDevice.hasObject(By.textContains("OFF").checked(false)));

                // for ds1 and dax2 , the off string is diff
                assertTrue(LOG_TAG+"turn off ds status failed ! ",
                        mDevice.hasObject(By.textContains("Dolby Atmos is turned off."))||
                                mDevice.hasObject(By.textContains("Dolby Audio is turned off.")));

                assertTrue(LOG_TAG+"turn off ds status failed ! ",
                        mDevice.hasObject(By.textContains("DONE")));

                assertTrue(LOG_TAG+"turn off ds status failed ! ",
                        mDevice.hasObject(By.textContains("MORE SETTINGS")));

//                assertFalse(LOG_TAG+": profile name still exist! ",
//                        mDevice.hasObject(By.textContains(profileName)));
            }else {
                assertTrue(LOG_TAG+": fail to navigate to system second ui !",false);
            }
        }else {
            if (mDevice.wait(Until.hasObject(By.textContains("ON").checked(true)),
                    LAUNCH_TIMEOUT/50)){
                //judge slide bar status and other panel
                assertTrue(LOG_TAG+": ds slide switch status is wrong !",
                        mDevice.hasObject(By.textContains("ON").checked(true)));

                assertTrue(LOG_TAG+"turn on ds status failed ! ",
                        mDevice.hasObject(By.textContains("DONE")));

                assertTrue(LOG_TAG+"turn on ds status failed ! ",
                        mDevice.hasObject(By.textContains("MORE SETTINGS")));

                assertTrue(LOG_TAG+"profile name did not exist ! ",
                        mDevice.hasObject(By.textContains(profileName)));
            }else {
                assertTrue(LOG_TAG+": fail to navigate to system second ui !",false);
            }
        }
    }


}

