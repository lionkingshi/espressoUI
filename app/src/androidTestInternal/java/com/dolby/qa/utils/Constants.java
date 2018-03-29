package com.dolby.qa.utils;

public class Constants {
    public static final String TAG=" <<<<UIAutomation : ";
    public static final int PROFILE_NUM = 4 ;
    public static final int IEQ_NUM = 4 ;
    //application id and package name definition
    public static final String PACKAGE_NAME_UNDER_TEST = "com.dolby.daxappui.internal";
    public static final String PACKAGE_NAME_SETTINGS_SOUND="com.android.settings";
    public static final String PACKAGE_NAME_SYSTEM_UI="com.android.systemui";

    //device under test is a tablet or phone
    public static final String DEVICE_TYPE_TABLET="tablet";
    public static final String DEVICE_TYPE_PHONE="phone";
    // production info
    public static final String PRODUCT_VERSION_DS1="DS1";
    public static final String PRODUCT_VERSION_DAX2="DAX";
    // flag to current connected endpoint to device under test
    // only support one plug-in connected to device
    public static final int ENDPOINT_SPEAKER =1;
    public static final int ENDPOINT_HEADPHONE =2;
    public static final int ENDPOINT_USB_HEADPHONE =3;
    public static final int ENDPOINT_BLUE_TOOTH =4;
    public static final int CONFIG_ARRAY_LENGTH =5;
    //flag to indicate current device has a mono or stereo speaker
    public static final int SPEAKER_TYPE_MONO = 1;
    public static final int SPEAKER_TYPE_STEREO = 2;

    // flag to indicate values of dap features
    public static final boolean DAX_STATUS_ON = true;
    public static final boolean DAX_STATUS_OFF = false;

    public static final boolean DAX_DE_STATUS_ON = true;
    public static final boolean DAX_DE_STATUS_OFF = false;
    public static final boolean DAX_DE_STATUS_DEFAULT =DAX_DE_STATUS_OFF;
    public static final boolean DS1_CUSTOM1_DE_STATUS_DEFAULT =DAX_DE_STATUS_OFF;
    public static final boolean DS1_CUSTOM2_DE_STATUS_DEFAULT =DAX_DE_STATUS_OFF;

    // for dax3 project , added dea default value is
    public static final int DAX_DEA_DEFAULT_VALUE = 6;
    public static final int DEA_INDEX_IN_CONFIG_ARRAY = 0;
    public static final int SIZE_NUM_OF_CONFIG_ARRAY = 3;
    public static final int DAX_DEA_MAX_VALUE = 16;
    public static final int DAX_DEA_MIN_VALUE = 0;
    public static final int DAX_DEA_CUSTOM_DEFAULT_VALUE = DAX_DEA_DEFAULT_VALUE;
    public static final int DAX_DEA_MOVIE_DEFAULT_VALUE = DAX_DEA_DEFAULT_VALUE;


    public static final boolean DAX_SV_STATUS_ON=true;
    public static final boolean DAX_SV_STATUS_OFF=false;
    public static final boolean DAX_SV_STATUS_DEFAULT=DAX_SV_STATUS_OFF;
    public static final boolean DS1_CUSTOM1_SV_STATUS_DEFAULT=DAX_SV_STATUS_OFF;
    public static final boolean DS1_CUSTOM2_SV_STATUS_DEFAULT=DAX_SV_STATUS_OFF;



    public static final int DAX_IEQ_PRESET_BALANCED = 0;
    public static final int DAX_IEQ_PRESET_WARM = 1;
    public static final int DAX_IEQ_PRESET_DETAILED = 2;
    public static final int DAX_IEQ_PRESET_OFF = 3;
    public static final int DAX_MUSIC_IEQ_STATUS_DEFAULT = DAX_IEQ_PRESET_BALANCED;
    public static final int DS1_MUSIC_IEQ_STATUS_DEFAULT = DAX_IEQ_PRESET_BALANCED;
    public static final int DAX_CUSTOM_IEQ_STATUS_DEFAULT = DAX_IEQ_PRESET_OFF;
    public static final int DS1_CUSTOM1_IEQ_STATUS_DEFAULT = DAX_IEQ_PRESET_OFF;
    public static final int DS1_CUSTOM2_IEQ_STATUS_DEFAULT = DAX_IEQ_PRESET_OFF;


    public static final boolean DAX_BE_STATUS_ON =true;
    public static final boolean DAX_BE_STATUS_OFF =false;
    public static final boolean DAX_BE_STATUS_DEFAULT =DAX_BE_STATUS_OFF;
    public static final boolean DS1_BE_STATUS_DEFAULT =DAX_BE_STATUS_OFF;


    public static final boolean DAX_VL_STATUS_ON = true;
    public static final boolean DAX_VL_STATUS_OFF = false;
    public static final boolean DAX_VL_STATUS_DEFAULT = DAX_VL_STATUS_ON;
    public static final boolean DS1_VL_STATUS_DEFAULT = DAX_VL_STATUS_ON;

    public static final boolean DAX_GEQ_FLAT_STATUS_ON = true ;
    public static final boolean DAX_GEQ_FLAT_STATUS_OFF = false ;
    public static final boolean DAX_GEQ_FLAT_STATUS_DEFAULT = DAX_GEQ_FLAT_STATUS_ON ;
    public static final boolean DS1_GEQ_FLAT_STATUS_DEFAULT = DAX_GEQ_FLAT_STATUS_ON ;

    // for dax profile index
    public static final int DAX_PROFILE_DYNAMIC = 0 ;
    public static final int DAX_PROFILE_MOVIE = 1 ;
    public static final int DAX_PROFILE_MUSIC = 2 ;
    public static final int DAX_PROFILE_GAME = 3 ;
    public static final int DAX_PROFILE_VOICE = 4 ;
    public static final int DAX_PROFILE_CUSTOM = 5 ;
    public static final int DAX3_PROFILE_CUSTOM = 3 ;
    // for ds profile index
    public static final int DS_PROFILE_MOVIE = 0 ;
    public static final int DS_PROFILE_MUSIC = 1 ;
    public static final int DS_PROFILE_GAME = 2 ;
    public static final int DS_PROFILE_VOICE = 3 ;
    public static final int DS_PROFILE_CUSTOM_1 = 4 ;
    public static final int DS_PROFILE_CUSTOM_2 = 5 ;

    // flag to control debug information
    // some event response consume more time than espresso runtime
    // espresso performing/assertion should wait for it if flag is set to true
    public static final boolean FLAG_ALLOW_SLEEP_TIME = false;
    public static final int SLEEP_TIME_UNIT_SECOND = 1 ;
    // flag to print other debug information
    public static final boolean FLAG_PRINT_DEBUG_INFO = true;
    // flag to print the configuration of device under test
    public static final boolean FLAG_PRINT_CONFIG_INFO = true;


    // definition used in uiautomator for system ui and settings sound ui
    // define timeout of waiting for UI launch
    public static  final int LAUNCH_TIMEOUT=5000;
    // define flag to make test thread sleep uncertain seconds
    // it is not recommended highly!
    public final static boolean FLAG_TEST_THREAD_SLEEP = false ;
    // define ui display strings
    public final static String SOUND_SETTINGS_LAUNCH_APP = "Launch App";
    public final static String SOUND_SETTINGS_PROFILE = "Profile";
    public final static String SOUND_SETTINGS_POWER_ON = "ON";
    public final static String SOUND_SETTINGS_POWER_OFF = "OFF";
    public final static String SYSTEM_UI_POWER_OFF = "Dolby";
    public final static String SOUND_SETTINGS_DAX2_DYNAMIC = "Dynamic" ;
    public final static String SOUND_SETTINGS_DAX2_MOVIE = "Movie" ;
    public final static String SOUND_SETTINGS_DAX2_MUSIC = "Music" ;
    public final static String SOUND_SETTINGS_DAX2_GAME = "Custom" ;
    public final static String SOUND_SETTINGS_DAX2_VOICE = "Custom" ;
    public final static String SOUND_SETTINGS_DAX2_CUSTOM = "Custom" ;

    public final static String SOUND_SETTINGS_DS1_MOVIE = "Movie" ;
    public final static String SOUND_SETTINGS_DS1_MUSIC = "Music" ;
    public final static String SOUND_SETTINGS_DS1_GAME = "Game" ;
    public final static String SOUND_SETTINGS_DS1_VOICE = "Voice" ;
    public final static String SOUND_SETTINGS_DS1_CUSTOM_1 = "Custom 1" ;
    public final static String SOUND_SETTINGS_DS1_CUSTOM_2 = "Custom 2" ;
}
