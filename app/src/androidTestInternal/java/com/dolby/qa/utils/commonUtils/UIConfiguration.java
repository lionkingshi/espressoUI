package com.dolby.qa.utils.commonUtils;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.dolby.daxappui.DAXApplication;
import com.dolby.daxappui.MainActivity;
import com.dolby.daxappui.R;

import static com.dolby.qa.utils.Constants.CONFIG_ARRAY_LENGTH;
import static com.dolby.qa.utils.Constants.DAX_BE_STATUS_DEFAULT;
import static com.dolby.qa.utils.Constants.DAX_CUSTOM_IEQ_STATUS_DEFAULT;
import static com.dolby.qa.utils.Constants.DAX_DE_STATUS_DEFAULT;
import static com.dolby.qa.utils.Constants.DAX_GEQ_FLAT_STATUS_DEFAULT;
import static com.dolby.qa.utils.Constants.DAX_MUSIC_IEQ_STATUS_DEFAULT;
import static com.dolby.qa.utils.Constants.DAX_SV_STATUS_DEFAULT;
import static com.dolby.qa.utils.Constants.DAX_VL_STATUS_DEFAULT;
import static com.dolby.qa.utils.Constants.DEVICE_TYPE_PHONE;
import static com.dolby.qa.utils.Constants.DEVICE_TYPE_TABLET;
import static com.dolby.qa.utils.Constants.ENDPOINT_BLUE_TOOTH;
import static com.dolby.qa.utils.Constants.ENDPOINT_HEADPHONE;
import static com.dolby.qa.utils.Constants.ENDPOINT_SPEAKER;
import static com.dolby.qa.utils.Constants.ENDPOINT_USB_HEADPHONE;
import static com.dolby.qa.utils.Constants.FLAG_PRINT_CONFIG_INFO;
import static com.dolby.qa.utils.Constants.IEQ_NUM;
import static com.dolby.qa.utils.Constants.PRODUCT_VERSION_DAX2;
import static com.dolby.qa.utils.Constants.PRODUCT_VERSION_DS1;
import static com.dolby.qa.utils.Constants.PROFILE_NUM;
import static com.dolby.qa.utils.Constants.SPEAKER_TYPE_MONO;
import static com.dolby.qa.utils.Constants.SPEAKER_TYPE_STEREO;

public class UIConfiguration {
    private static final String LOG_TAG="QAC-"+UIConfiguration.class.getSimpleName();

    private boolean deviceTypeIsTablet;
    private ActivityTestRule<MainActivity> mainActivity_ActivityTestRule;
    private static int [] mDeviceConfig =new int[CONFIG_ARRAY_LENGTH];

    private int profileId;
    private boolean isCheckedSV;
    private boolean isCheckedDE;
    private int isIeqNum;
    private boolean isFlatGEQ;
    private boolean isCheckedBE;
    private boolean isCheckedVL;

    public UIConfiguration() {
        Log.d("Constructor",LOG_TAG);
    }

    public UIConfiguration(ActivityTestRule<MainActivity> mainActivity_ActivityTestRule) {
        Log.d(LOG_TAG,"UIConfiguration constructor!");
        this.mainActivity_ActivityTestRule=mainActivity_ActivityTestRule;
        deviceTypeIsTablet = mainActivity_ActivityTestRule.getActivity().getApplicationContext().
                getResources().getBoolean(R.bool.tabletLayout);

        if (FLAG_PRINT_CONFIG_INFO) Log.d(LOG_TAG,"the device is a tablet ? : "+ deviceTypeIsTablet);
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileType) {
        this.profileId = profileType;
    }

    public void setCheckedSV(boolean checkedSV) {
        this.isCheckedSV = checkedSV;
    }

    public void setCheckedDE(boolean checkedDE) {
        this.isCheckedDE = checkedDE;
    }

    public void setIsIeqNum(int isIeqNum) {
        this.isIeqNum = isIeqNum;
    }

    public void setFlatGEQ(boolean flatGEQ) {
        this.isFlatGEQ = flatGEQ;
    }

    public void setCheckedBE(boolean checkedBE) {
        this.isCheckedBE = checkedBE;
    }

    public void setCheckedVL(boolean checkedVL) {
        this.isCheckedVL = checkedVL;
    }

    public void setFeatureDefaultValues(){
        this.isCheckedBE = DAX_BE_STATUS_DEFAULT;
        this.isCheckedVL = DAX_VL_STATUS_DEFAULT;
        this.isCheckedDE = DAX_DE_STATUS_DEFAULT;
        this.isCheckedSV = DAX_SV_STATUS_DEFAULT ;
        this.isFlatGEQ = DAX_GEQ_FLAT_STATUS_DEFAULT;
        String[] mProfileName=getProfileName();
        if (mProfileName[profileId].equals("Music")){
            this.isIeqNum= DAX_MUSIC_IEQ_STATUS_DEFAULT;
        } else {
            if (mProfileName[profileId].contains("Custom")){
                this.isIeqNum= DAX_CUSTOM_IEQ_STATUS_DEFAULT;
            }
        }
    }

    public String getDeviceType(){
        return deviceTypeIsTablet ? DEVICE_TYPE_TABLET : DEVICE_TYPE_PHONE;
    }

    public String getProductVersion(){
//        String productVersion = DAXApplication.getInstance().getProductVersion();
        String productVersion = "dax3";
        if (FLAG_PRINT_CONFIG_INFO) {
            Log.d(LOG_TAG,"current product version is : "+ productVersion);
            for (int index=0;index<getProfileNum();index++){
                if (FLAG_PRINT_CONFIG_INFO) {
                    Log.d(LOG_TAG,"current profile name : "+index+" = "+ DAXApplication.getInstance().getProfileNames()[index]);
                }
            }
        }
        return productVersion.substring(0, 3).equals("DS1") ? PRODUCT_VERSION_DS1 : PRODUCT_VERSION_DAX2;
    }

    public String[] getProfileName(){
        return DAXApplication.getInstance().getProfileNames();
    }

    public int getProfileNum(){
        return PROFILE_NUM;
    }

    public boolean isTablet(){
        return mainActivity_ActivityTestRule.getActivity().getApplicationContext().getResources().getBoolean(R.bool.tabletLayout);
    }

    public boolean isDax2(){
        return getProductVersion().equals(PRODUCT_VERSION_DAX2);
    }

    public String[] getIeqName(){
        String ieqNames[] =new String[IEQ_NUM];
        ieqNames[0]=mainActivity_ActivityTestRule.getActivity().getApplicationContext().getResources().getString(R.string.detailed);
        ieqNames[1]=mainActivity_ActivityTestRule.getActivity().getApplicationContext().getResources().getString(R.string.balanced);
        ieqNames[2]=mainActivity_ActivityTestRule.getActivity().getApplicationContext().getResources().getString(R.string.warm);
        ieqNames[3]=mainActivity_ActivityTestRule.getActivity().getApplicationContext().getResources().getString(R.string.off);
        return ieqNames;
    }

    public int getIeqNum(){
        return IEQ_NUM;
    }

    public int[] getEndpointAndSpeakerConfig(){
        // get speaker type : mono or stereo
        // for dax3 project , cancel sound virtualizer button
        // so this config data has no effect meaning
//        mDeviceConfig[0]= mainActivity_ActivityTestRule.getActivity().isMonoSpeaker()
//                ? SPEAKER_TYPE_MONO :SPEAKER_TYPE_STEREO;
        mDeviceConfig[0] = SPEAKER_TYPE_STEREO;

        // get endpoint type : speaker(default) ,headset ,usb headset , blue tooth
        int mEndpointType= ENDPOINT_SPEAKER;
        //judge whether the device output type is not a speaker
        AudioManager am=(AudioManager)mainActivity_ActivityTestRule.getActivity().getSystemService(Context.AUDIO_SERVICE);
        int device=am.getDevices(AudioManager.GET_DEVICES_OUTPUTS).length;
        if (FLAG_PRINT_CONFIG_INFO) Log.d(LOG_TAG,"This devices endpoint array length : "+device);
        for(int i=0;i<device;i++){
            int type=am.getDevices(AudioManager.GET_DEVICES_OUTPUTS)[i].getType();
            if((type == AudioDeviceInfo.TYPE_WIRED_HEADPHONES) || (type == AudioDeviceInfo.TYPE_WIRED_HEADSET)){
                mEndpointType= ENDPOINT_HEADPHONE;//headphone is plugged!
            }
            if( type == AudioDeviceInfo.TYPE_USB_DEVICE ){
                mEndpointType= ENDPOINT_USB_HEADPHONE;//usb-headphone is plugged!
            }
            if ( (type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP) || (type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO) ){
                mEndpointType= ENDPOINT_BLUE_TOOTH;//bluetooth is plugged!
            }
            if (FLAG_PRINT_CONFIG_INFO) Log.d(LOG_TAG,"This devices endpoint type : "+type+"+++"+i);
        }
        mDeviceConfig[1]=mEndpointType;
        return mDeviceConfig;
    }

    public boolean judgeSVInCustomProfileIsEnableOrNot(){
        boolean misEnable=true;
        getEndpointAndSpeakerConfig();
        if((mDeviceConfig[1] == ENDPOINT_SPEAKER)&&(mDeviceConfig[0] == SPEAKER_TYPE_MONO)){
            misEnable = false;
        }
        if(mDeviceConfig[1] == ENDPOINT_BLUE_TOOTH){
            misEnable=false;
        }
        if (FLAG_PRINT_CONFIG_INFO) {
            if (misEnable){
                Log.d(LOG_TAG,"sv button could be clicked !");
            }else {
                Log.d(LOG_TAG,"sv button could not be clicked !");
            }

        }
        return misEnable;
    }

    public void clearConfig(){
        for (int i=0;i<mDeviceConfig.length;i++)
            mDeviceConfig[i]=0xff;
//        setFeatureDefaultValues();
    }

}
