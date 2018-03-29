package com.dolby.qa.utils.SettingsSoundUIUtils;

import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.dolby.qa.utils.commonUtils.CommonUtils;

import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_CUSTOM;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_DYNAMIC;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_GAME;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_MOVIE;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_MUSIC;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DAX2_VOICE;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DS1_CUSTOM_1;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_DS1_CUSTOM_2;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_POWER_OFF;
import static com.dolby.qa.utils.Constants.SOUND_SETTINGS_PROFILE;
import static org.junit.Assert.assertTrue;


public class CommonSettingsSoundUIUtils extends CommonUtils implements ICommonSettingsSoundUIUtils {

    private String LOG_TAG="QASSC-"+CommonSettingsSoundUIUtils.class.getSimpleName();

    private UiDevice mDevice ;

    public CommonSettingsSoundUIUtils(UiDevice mDevice) {
        super(mDevice);
        this.mDevice = mDevice;
    }

    public CommonSettingsSoundUIUtils() {
        super();
        this.mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Nullable
    @Override
    public UiObject getDolbySettingsSoundBarObject(){
        try {
            // get profile name object from sound->settings ui
            UiObject profileNameObj=
                    mDevice.findObject(new UiSelector().text(SOUND_SETTINGS_PROFILE))
                            .getFromParent(new UiSelector().className("android.widget.TextView")
                                    .index(1));
            assertTrue(LOG_TAG+" no dolby description in current settings sound list ",
                    profileNameObj.exists());
            if (profileNameObj.exists()){
                // check profile name in bound of specified strings
                String mProfileName=profileNameObj.getText();
                boolean rightProfileName=mProfileName.equals(SOUND_SETTINGS_DAX2_DYNAMIC);
                rightProfileName|=mProfileName.equals(SOUND_SETTINGS_DAX2_MOVIE);
                rightProfileName|=mProfileName.equals(SOUND_SETTINGS_DAX2_MUSIC);
                rightProfileName|=mProfileName.equals(SOUND_SETTINGS_DAX2_GAME);
                rightProfileName|=mProfileName.equals(SOUND_SETTINGS_DAX2_VOICE);
                rightProfileName|=mProfileName.equals(SOUND_SETTINGS_DAX2_CUSTOM);
                rightProfileName|=mProfileName.equals(SOUND_SETTINGS_POWER_OFF);
                rightProfileName|=mProfileName.equals(SOUND_SETTINGS_DS1_CUSTOM_1);
                rightProfileName|=mProfileName.equals(SOUND_SETTINGS_DS1_CUSTOM_2);
//                Log.d(LOG_TAG,"get profile name is one of six profiles : "+rightProfileName + " and profile name is :"+mProfileName);
                assertTrue(LOG_TAG+"current profile name is not expected in sound settings ui panel !",
                        rightProfileName);
                return profileNameObj;
            }else{
                return null;
            }
        }catch (UiObjectNotFoundException e){
//            Log.d(LOG_TAG,"get settings sound profile name object failed !");
            e.printStackTrace();
            assertTrue(LOG_TAG+": get settings sound profile name object failed !",false);
            return null;
        }
    }

}
