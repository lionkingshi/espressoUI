package com.dolby.qa.utils.SystemUIUtils;


import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import com.dolby.qa.utils.commonUtils.CommonUtils;

import static com.dolby.qa.utils.Constants.LAUNCH_TIMEOUT;
import static com.dolby.qa.utils.Constants.PACKAGE_NAME_SYSTEM_UI;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_CUSTOM;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_DYNAMIC;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_GAME;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_MOVIE;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_MUSIC;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_VOICE;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DS1_CUSTOM_1;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DS1_CUSTOM_2;
import static com.dolby.qa.utils.Constants.SYSTEM_UI_POWER_OFF;
import static org.junit.Assert.assertTrue;

public class CommonSystemUIUtils extends CommonUtils implements ICommonSystemUIUtils {
    private String LOG_TAG="QACU-"+CommonSystemUIUtils.class.getSimpleName();

    private UiDevice mDevice ;

    public CommonSystemUIUtils(UiDevice mDevice) {
        super(mDevice);
        this.mDevice = mDevice;
    }

    public CommonSystemUIUtils() {
        super();
        this.mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }


    @Nullable
    public UiObject getProfileItemObjectInSystemFirstUI(){
        waitForSystemFirstUIRender();

        try {
            UiObject parentObj=mDevice.findObject(new UiSelector().className("android.support.v4.view.ViewPager")
                    .childSelector(new UiSelector().index(0))
                    .childSelector(new UiSelector().index(0))
                    .childSelector(new UiSelector().index(1)));
//                    .childSelector(new UiSelector().className("android.widget.LinearLayout").index(1)));
            UiObject profileNameObj=parentObj.getChild(new UiSelector().className("android.widget.TextView"));
            assertTrue(LOG_TAG+" no dolby system ui in current quick setting list ",profileNameObj.exists());
            if (profileNameObj.exists()){
                return profileNameObj;
            }
        }catch (UiObjectNotFoundException e){
            e.printStackTrace();
            assertTrue(LOG_TAG+" : fail to get quick settings profile name object !",false);
        }
        return null;
    }

    @Nullable
    public UiObject getIconItemObjectInSystemFirstUI(){

        waitForSystemFirstUIRender();

        try {
            UiObject parentObj=mDevice.findObject(new UiSelector().className("android.support.v4.view.ViewPager")
                    .childSelector(new UiSelector().index(0))
                    .childSelector(new UiSelector().index(0))
                    .childSelector(new UiSelector().index(0)));
            UiObject iconObject = parentObj.getChild(new UiSelector().className("android.widget.ImageView"));
            assertTrue(LOG_TAG+" no dolby system ui in current quick setting list ",iconObject.exists());
            if (iconObject.exists()){
                return iconObject;
            }
        }catch (UiObjectNotFoundException e){
            e.printStackTrace();
            assertTrue(LOG_TAG+" : fail to get quick settings icon object !",false);
        }
        return null;
    }

    // check dolby quick settings first ui display
    public boolean judgeDolbyIconInQuickSettingsUILeftTopPosition(String profileName, boolean isChecked){
        // wait for system ui display
        waitForSystemFirstUIRender();
//        waitForSecond(2);
        //check quick settings have already have dolby quick settings or not
        String mTopPositionText = getProfileNameInSystemFirstUIAndIfFailedAbort();
        boolean dolbyQuickSettingsIsTopPositionOrNot = mTopPositionText.equals(SOUND_SETTINGS_DAX2_CUSTOM);
        dolbyQuickSettingsIsTopPositionOrNot |= mTopPositionText.equals(SOUND_SETTINGS_DAX2_DYNAMIC);
        dolbyQuickSettingsIsTopPositionOrNot |= mTopPositionText.equals(SOUND_SETTINGS_DAX2_GAME);
        dolbyQuickSettingsIsTopPositionOrNot |= mTopPositionText.equals(SOUND_SETTINGS_DAX2_VOICE);
        dolbyQuickSettingsIsTopPositionOrNot |= mTopPositionText.equals(SOUND_SETTINGS_DAX2_MUSIC);
        dolbyQuickSettingsIsTopPositionOrNot |= mTopPositionText.equals(SOUND_SETTINGS_DAX2_MOVIE);
        dolbyQuickSettingsIsTopPositionOrNot |= mTopPositionText.equals(SYSTEM_UI_POWER_OFF);
        dolbyQuickSettingsIsTopPositionOrNot |= mTopPositionText.equals(SOUND_SETTINGS_DS1_CUSTOM_1);
        dolbyQuickSettingsIsTopPositionOrNot |= mTopPositionText.equals(SOUND_SETTINGS_DS1_CUSTOM_2);
        if (isChecked){
            assertTrue(LOG_TAG+": dolby quick settings is not in the first position !",
                    dolbyQuickSettingsIsTopPositionOrNot);
            assertTrue(LOG_TAG+": expect profile ("+profileName+" ) and actual profile ("+mTopPositionText+") ! ",
                    mTopPositionText.equals(profileName));
        }
        return dolbyQuickSettingsIsTopPositionOrNot;
    }

    public void openQuickSettings(){
        // open quick settings ui
        mDevice.pressBack();
        mDevice.pressBack();
        mDevice.pressHome();
        mDevice.openQuickSettings();

        // wait for system ui display
        if (mDevice.wait(Until.hasObject(By.res(PACKAGE_NAME_SYSTEM_UI,"slider")),
                LAUNCH_TIMEOUT/2)){
            assertTrue(LOG_TAG+": fail to open quick settings!",
                    mDevice.hasObject(By.res(PACKAGE_NAME_SYSTEM_UI,"slider")));
        }else {
            assertTrue(LOG_TAG+": fail to open quick settings !",
                    false);
        }
//        waitForSecond(2);
    }

    public void dragDolbyQuickSettingsToLeftTopPosition(){
        try{
            //judge dolby quick settings display
            UiScrollable quickSetting =
                    new UiScrollable(new UiSelector().className("android.support.v7.widget.RecyclerView"));
            UiObject dolbyQuickSettingObj =
                    quickSetting.getChildByText(
                            new UiSelector().className("android.widget.TextView"),
                            "DOLBY",
                            true);

            assertTrue(LOG_TAG+"no dolby quick settings panel in quick settings edit mode !",
                    dolbyQuickSettingObj.exists());

            //drag dolby quick settings to display zone
            UiObject destinationObj=mDevice.findObject(new UiSelector().className("android.widget.TextView").text("Edit"));
            dolbyQuickSettingObj.dragTo(destinationObj,1000);

            //exit the edit mode and dolby quick settings bar display
            mDevice.pressBack();
        }catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            assertTrue(LOG_TAG+": fail to drag dolby quick settings in quick settings edit mode !",
                    false);
        }
    }

    public void resetQuickSettingsPanel(){
        try {
            //navigate to quick settings edit mode
            //mDevice.findObject(new UiSelector().className("android.widget.ImageView").descriptionContains("order")).clickAndWaitForNewWindow(LAUNCH_TIMEOUT);
            mDevice.findObject(new UiSelector().descriptionContains("order")).clickAndWaitForNewWindow(LAUNCH_TIMEOUT);
            mDevice.findObject(new UiSelector().className("android.widget.ImageButton").description("More options")).click();
            mDevice.findObject(new UiSelector().text("Reset")).click();
        }catch (UiObjectNotFoundException e){
            e.printStackTrace();
            assertTrue(LOG_TAG+"fail to reset quick setting !",false);
        }
    }

    public void waitForSystemFirstUIRender(){
        // wait for system ui first display
        if (mDevice.wait(Until.hasObject(By.res(PACKAGE_NAME_SYSTEM_UI,"slider")),
                LAUNCH_TIMEOUT/2)){
            assertTrue(LOG_TAG+": fail to open quick settings first ui !",
                    mDevice.hasObject(By.res(PACKAGE_NAME_SYSTEM_UI,"slider")));
        }else {
            assertTrue(LOG_TAG+": fail to open quick settings first ui !",
                    false);
        }
    }

    public void waitForSystemSecondUIRender(){
        // wait for system ui second display
        if (mDevice.wait(Until.hasObject(By.res("android:id/title").text("DOLBY").clazz("android.widget.TextView")),
                LAUNCH_TIMEOUT/2)){
            assertTrue(LOG_TAG+": fail to open quick settings second ui !",
                    mDevice.hasObject(By.res("android:id/title").text("DOLBY").clazz("android.widget.TextView")));
        }else {
            assertTrue(LOG_TAG+": fail to open quick settings second ui !",
                    false);
        }
    }

    private String getProfileNameInSystemFirstUIAndIfFailedAbort(){
        String mProfileName=null;

        waitForSystemFirstUIRender();

        try {
            UiObject profileNameObj = getProfileItemObjectInSystemFirstUI();
            if ((profileNameObj != null)&&(profileNameObj.exists())){
                mProfileName=profileNameObj.getText();
            }
        }catch (UiObjectNotFoundException e){
            e.printStackTrace();
            assertTrue(LOG_TAG+": fail to get quick settings profile name !",false);
        }

        return mProfileName;
    }

}
