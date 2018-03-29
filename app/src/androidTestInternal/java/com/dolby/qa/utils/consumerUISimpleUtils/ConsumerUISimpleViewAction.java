package com.dolby.qa.utils.consumerUISimpleUtils;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.util.Log;

import com.dolby.qa.utils.commonUtils.CommonUtils;

import static com.dolby.qa.utils.Constants.LAUNCH_TIMEOUT;
import static com.dolby.qa.utils.Constants.PACKAGE_NAME_UNDER_TEST;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_CUSTOM;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DS1_CUSTOM_2;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConsumerUISimpleViewAction extends CommonUtils implements IConsumerUISimpleViewAction {

    public String LOG_TAG="QAM-"+ConsumerUISimpleViewAction.class.getSimpleName();
    private UiDevice mDevice ;

    public ConsumerUISimpleViewAction(UiDevice mDevice) {
        super(mDevice);
        this.mDevice = mDevice;
    }

    public ConsumerUISimpleViewAction() {
        super();
        this.mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    public void navigateToConsumerUIDirectly(){
        if (!mDevice.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"dsLogoText"))){
            startActivityFromHomeScreen(PACKAGE_NAME_UNDER_TEST);
        }else {
            // already in consumer ui and do nothing
        }

        if (mDevice.wait(Until.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"dsLogoText")),
                LAUNCH_TIMEOUT/2)){
            assertTrue(LOG_TAG+": fail to navigate to consumer ui directly !",
                    mDevice.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"dsLogoText")));
        }else {
            assertTrue(LOG_TAG+": fail to navigate to consumer ui directly !",
                    false);
        }
    }

    public void turnOffDaxFromConsumerUI(){
        //dap status should be off
        if (!mDevice.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"powerOffText"))){
            mDevice.findObject(By.res(PACKAGE_NAME_UNDER_TEST,"powerButtonOn")).click();
        }
        // check dap is off in consumer ui
        if (mDevice.wait(Until.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"powerOffText")),
                LAUNCH_TIMEOUT/2)){
            //check the current profile in Consumer UI is what we want
            assertTrue(LOG_TAG+": fail to turn of dap in consumer ui ! ",
                    mDevice.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"powerOffText")));
        }else {
            assertTrue(LOG_TAG+": fail to turn of dap in consumer ui !",
                    false);
        }
    }

    public void turnOnDaxFromConsumerUI(){
        //dap status should be on
        if (mDevice.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"powerOffText"))){
            mDevice.findObject(By.res(PACKAGE_NAME_UNDER_TEST,"powerButtonOn")).click();
        }
        //check dap is on in consumer ui
        if (mDevice.wait(Until.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"profileName")),
                LAUNCH_TIMEOUT/5)){
            //check the current profile in Consumer UI is what we want
            assertFalse(LOG_TAG+": fail to turn on dap in consumer ui ! ",
                    mDevice.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"powerOffText")));
        }else {
            assertTrue(LOG_TAG+": fail to turn on dap in consumer ui !",
                    false);
        }
    }

    public void selectProfileFromConsumerUI(String profileName){
        //dap status should be on
        if (mDevice.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"powerOffText"))){
            mDevice.findObject(By.res(PACKAGE_NAME_UNDER_TEST,"powerButtonOn")).click();
        }
        //check dap is off
        if (mDevice.wait(Until.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"profileName")),
                LAUNCH_TIMEOUT/5)){
            try {
                //get scroll object and for phone and tablet it is diff
                UiScrollable mProfileTable;
                //judge the device under test is phone or tablet
                boolean mDeviceType=isTablet(InstrumentationRegistry.getContext());
                if (mDeviceType){
                    mProfileTable=new UiScrollable(new UiSelector().className("android.widget.ListView"));
                }
                else {
                    mProfileTable=new UiScrollable(new UiSelector().className("android.widget.HorizontalScrollView"));
                }

                //scroll to the view with profile name
                if ((profileName.equals(SOUND_SETTINGS_DAX2_CUSTOM)||profileName.equals(SOUND_SETTINGS_DS1_CUSTOM_2))&&(!mDeviceType)){
                    mProfileTable.getChildByText(new UiSelector().className("android.widget.TextView"),"MUSIC",true)
                            .click();
                }
                UiObject obj=mProfileTable.getChildByText(new UiSelector().className("android.widget.TextView")
                        ,mDeviceType?profileName:profileName.toUpperCase(),true);
                Log.d(LOG_TAG,"object with profile name ("+profileName.toUpperCase()+") display : "+obj.exists());
                if (obj.exists()){
                    obj.clickAndWaitForNewWindow();
                }
                //check the current profile in Consumer UI is what we want
                assertTrue(LOG_TAG+" wrong current profile name in Consumer UI ",
                        mDevice.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"profileName").text(profileName)));

            }catch (UiObjectNotFoundException e){
                Log.d(LOG_TAG,"failed to select profile in consumer ui !");
                e.printStackTrace();
                assertTrue(LOG_TAG+": failed to select profile in consumer ui !",false);
            }
        }else {
            assertTrue(LOG_TAG+": fail to turn on dap in consumer ui !",
                    false);
        }
    }

}
