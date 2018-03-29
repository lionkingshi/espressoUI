package com.dolby.qa.utils.SystemUIUtils;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import static com.dolby.qa.utils.Constants.LAUNCH_TIMEOUT;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_MUSIC;
import static com.dolby.qa.utils.Constants.SYSTEM_UI_POWER_OFF;
import static org.junit.Assert.assertTrue;

public class SystemUIViewAction extends CommonSystemUIUtils implements ISystemUIViewAction {
    private String LOG_TAG="QASSA-"+SystemUIViewAction.class.getSimpleName();
    private UiDevice mDevice;

    public SystemUIViewAction(UiDevice mDevice) {
        super();
        this.mDevice = mDevice;
    }

    public void navigateToSystemFirstUI(){
        // open quick settings ui (dolby system ui )
        openQuickSettings();

        // judge dolby quick settings is in the first position or not
        boolean dolbyQuickSettingsIsLeftTopPositionOrNot = judgeDolbyIconInQuickSettingsUILeftTopPosition(null,false);

        //if dolby quick settings did not sit at first position in quick setting list , reset it
        if (!dolbyQuickSettingsIsLeftTopPositionOrNot){
            //navigate to quick settings edit mode
            resetQuickSettingsPanel();
            //display dolby quick settings bar
            dragDolbyQuickSettingsToLeftTopPosition();
        }
    }

    public void clickDolbyIconItemInSystemFirstUI(){
        UiObject iconObject = getIconItemObjectInSystemFirstUI();
        if (iconObject.exists()){
            try {
                iconObject.click();
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
                assertTrue(LOG_TAG+": fail to click icon object in system first ui !",false);
            }
        }else {
            assertTrue(LOG_TAG+": fail to locate the icon object in system first ui !",false);
        }
    }

    public void clickDolbyProfileItemInSystemFirstUI(){
        UiObject profileObject = getProfileItemObjectInSystemFirstUI();
        if (profileObject.exists()){
            try {
                profileObject.clickAndWaitForNewWindow(LAUNCH_TIMEOUT);
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
                assertTrue(LOG_TAG+": fail to click profile name object in system first ui !",false);
            }
        }else {
            assertTrue(LOG_TAG+": fail to locate the profile name object in system first ui !",false);
        }
    }

    public void navigateToSystemSecondUI(){
        // sure that in system first ui
        waitForSystemFirstUIRender();
        //click profile name item
        clickDolbyProfileItemInSystemFirstUI();
        // sure that is system second ui
        waitForSystemSecondUIRender();
    }

    public void turnOffDaxInSystemSecondUI(){
        // sure that is system second ui
        waitForSystemSecondUIRender();
        // turn off dax
        selectProfileInSystemSecondUI(SYSTEM_UI_POWER_OFF);
    }

    public void turnOnDaxInSystemSecondUI(){
        // sure that is system second ui
        waitForSystemSecondUIRender();
        // turn off dax
        selectProfileInSystemSecondUIOrTurnOnDax(SOUND_SETTINGS_DAX2_MUSIC,false);
    }

    public void selectProfileInSystemSecondUI(String profileName){
        selectProfileInSystemSecondUIOrTurnOnDax(profileName,true);
    }

    public void clickMoreSettingsItemInSystemSecondUI(){
        // sure that is system second ui
        waitForSystemSecondUIRender();
        // click the more settings text
        if (mDevice.hasObject(By.text("MORE SETTINGS").clazz("android.widget.TextView"))){
            try {
                mDevice.findObject(new UiSelector().textContains("MORE SETTINGS")).clickAndWaitForNewWindow(LAUNCH_TIMEOUT);
                // click the more settings text and restore to consumer ui
                selectAPP();
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
                assertTrue(LOG_TAG+": fail to locate the more settings text in system second ui !",false);
            }
        }
    }

    public void clickDoneItemFromSystemSecondUI(){
        // sure that is system second ui
        waitForSystemSecondUIRender();
        // click the done text
        if (mDevice.hasObject(By.text("DONE").clazz("android.widget.TextView"))){
            try {
                mDevice.findObject(new UiSelector().textContains("DONE")).clickAndWaitForNewWindow(LAUNCH_TIMEOUT);
                // click the done text and restore to system first ui
                waitForSystemFirstUIRender();
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
                assertTrue(LOG_TAG+": fail to locate the done text in system second ui !",false);
            }
        }

    }

    private void selectProfileInSystemSecondUIOrTurnOnDax(String profileName, boolean performSelectAction){
        try{
            // dolby off and on ui display is diff
            if (profileName.equals(SYSTEM_UI_POWER_OFF)){
                // if quick setting ui is off ,first turn it on
                if (mDevice.hasObject(By.textContains("ON").checked(true))){
                    mDevice.findObject(new UiSelector().className("android.widget.Switch").textContains("ON"))
                            .clickAndWaitForNewWindow(LAUNCH_TIMEOUT);
                }

                //check quick settings ui off display
                if (mDevice.wait(Until.hasObject(By.textContains("OFF").checked(false)),
                        LAUNCH_TIMEOUT/50)){
                    assertTrue(LOG_TAG+" :fail to turn off dax in system second ui !",
                            mDevice.hasObject(By.textContains("OFF").checked(false)));
                }else {
                    assertTrue(LOG_TAG+" :fail to turn off dax in system second ui !",false);
                }
            }else {
                // if quick setting ui is off ,first turn it on
                if (mDevice.hasObject(By.textContains("OFF").checked(false))){
                    mDevice.findObject(new UiSelector().className("android.widget.Switch").textContains("OFF").checked(false))
                            .clickAndWaitForNewWindow(LAUNCH_TIMEOUT);
                }

                if (mDevice.wait(Until.hasObject(By.textContains("ON").checked(true)),
                        LAUNCH_TIMEOUT/50)){
                    assertTrue(LOG_TAG+" :fail to turn on dax in system second ui !",
                            mDevice.hasObject(By.textContains("ON").checked(true)));
                    //perform click action by 6 profile name
                    if (performSelectAction){
                        mDevice.findObject(new UiSelector().textContains(profileName)).click();
                        mDevice.findObject(new UiSelector().textContains(profileName)).click();
                        if (mDevice.wait(Until.hasObject(By.textContains(profileName)),
                                LAUNCH_TIMEOUT/50)){
                            assertTrue(LOG_TAG+" :fail to select profile name in system second ui !",
                                    mDevice.hasObject(By.textContains(profileName)));
                        }else {
                            assertTrue(LOG_TAG+" :fail to select profile name in system second ui !",false);
                        }
                    }
                }else {
                    assertTrue(LOG_TAG+" :fail to turn on dax in system second ui !",false);
                }
            }
        }catch (UiObjectNotFoundException e){
            e.printStackTrace();
            assertTrue(LOG_TAG+": fail to select profile in system second ui ! "+profileName,false);
        }
    }

}
