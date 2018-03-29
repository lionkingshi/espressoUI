package com.dolby.qa.utils.consumerUISimpleUtils;


import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

import static com.dolby.qa.utils.Constants.LAUNCH_TIMEOUT;
import static com.dolby.qa.utils.Constants.PACKAGE_NAME_UNDER_TEST;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_POWER_OFF;
import static com.dolby.qa.utils.Constants.SYSTEM_UI_POWER_OFF;
import static org.junit.Assert.assertTrue;

public class ConsumerUISimpleViewAssertions implements IConsumerUISimpleViewAssertions {
    private String LOG_TAG="QACV-"+ConsumerUISimpleViewAssertions.class.getSimpleName();
    private UiDevice mDevice ;

    public ConsumerUISimpleViewAssertions(UiDevice mDevice) {
        this.mDevice = mDevice;
    }

    public ConsumerUISimpleViewAssertions() {
        this.mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    /**
     * verify the Consumer UI on and off display
     *
     * @param profileName string of table layout tile displayed in Consumer UI view
     */
    public void checkConsumerUIDisplay(String profileName){
        if (profileName.equals(SOUND_SETTINGS_POWER_OFF)||profileName.equals(SYSTEM_UI_POWER_OFF)){
            if (mDevice.wait(Until.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"powerOffText")),
                    LAUNCH_TIMEOUT/5)){
                assertTrue(LOG_TAG+"dap status should be off but on in consumer ui !",
                        mDevice.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"powerOffText")));
            }else {
                assertTrue(LOG_TAG+": fail to navigate to consumer ui from sound settings ui!",false);
            }
        }else{
            if (mDevice.wait(
                    Until.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"profileName").text(profileName)),
                    LAUNCH_TIMEOUT/50)){
                assertTrue(LOG_TAG+"dap profile name in consumer ui is not same as sound settings ui name !",
                        mDevice.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"profileName").text(profileName)));
            }else {
                assertTrue(LOG_TAG+": fail to navigate to consumer ui from sound settings ui!",false);
            }
        }
    }
}
