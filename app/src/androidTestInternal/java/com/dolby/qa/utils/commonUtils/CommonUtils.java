package com.dolby.qa.utils.commonUtils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import static com.dolby.qa.utils.Constants.FLAG_TEST_THREAD_SLEEP;
import static com.dolby.qa.utils.Constants.LAUNCH_TIMEOUT;
import static com.dolby.qa.utils.Constants.PACKAGE_NAME_UNDER_TEST;
import static com.dolby.qa.utils.Constants.SLEEP_TIME_UNIT_SECOND;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CommonUtils implements ICommonUtils {
    private String LOG_TAG="QACU-"+CommonUtils.class.getSimpleName();

    private UiDevice mDevice ;

    public CommonUtils(UiDevice mDevice) {
        this.mDevice = mDevice;
    }

    public CommonUtils() {
        this.mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    public void unlockDeviceScreenQuickly(){
        try{
            if (!mDevice.isScreenOn()){
                mDevice.wakeUp();
                int myheight=mDevice.getDisplayHeight();
                int mxwidth=mDevice.getDisplayWidth();
                boolean result=mDevice.swipe(mxwidth/2,2*myheight/3,mxwidth/2,myheight/6,50);
                Log.d(LOG_TAG,"unlock the sleep screen succeed : "+result);
            }
        }catch (RemoteException re){
            Log.d(LOG_TAG,"unlock the sleep screen failed !");
            re.printStackTrace();
            assertTrue(LOG_TAG+": unlock the sleep screen failed !",false);
        }
    }

    public  void startActivityFromHomeScreen(String packageName) {
        assertThat(packageName,notNullValue());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);
        //Log.d(LOG_TAG,"launcherPackage : "+launcherPackage);

        // Launch the app
        final Context context = InstrumentationRegistry.getContext();
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(packageName);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)),
                LAUNCH_TIMEOUT);
    }

    public  boolean isTablet(Context context){
        //judge the device is smart phone or tablet
        int mDeviceScreenSize=context.getResources().getConfiguration().screenLayout;
        int mMaskAfter=mDeviceScreenSize& Configuration.SCREENLAYOUT_SIZE_MASK;
        boolean xlarge = (Configuration.SCREENLAYOUT_SIZE_XLARGE == mMaskAfter);
        boolean large  = (Configuration.SCREENLAYOUT_SIZE_LARGE == mMaskAfter);
        Log.d(LOG_TAG,"xlarge value :"+xlarge+" large value : "+large + " device screen value :"+mDeviceScreenSize + "and after mask :"+mMaskAfter);
        return (xlarge || large );
    }

    public  void selectAPP(){
        // check Consumer UI ds status same as quick settings
        // add code to select dax ui we want to launch
        // because we use diff build variant
//        try {
//            TimeUnit.SECONDS.sleep(2);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (mDevice.hasObject(By.text(PACKAGE_NAME_UNDER_TEST)) || mDevice.hasObject(By.res("android:id/button_always"))){
            if (mDevice.hasObject(By.text(PACKAGE_NAME_UNDER_TEST))){
                try{
                    mDevice.findObject(By.text(PACKAGE_NAME_UNDER_TEST)).click();
                }catch (NullPointerException e){
                    e.printStackTrace();
                    Log.d(LOG_TAG,"failed to select diff build variant app .(locate error)!");
                }
            }
            // click again to make sure its selection
            if (mDevice.hasObject(By.text(PACKAGE_NAME_UNDER_TEST))){
                try {
                    mDevice.findObject(By.text(PACKAGE_NAME_UNDER_TEST)).click();
                }catch (NullPointerException e){
                    e.printStackTrace();
                    Log.d(LOG_TAG,"failed to select diff build variant app .(locate error)!");
                }
            }
            if (mDevice.hasObject(By.res("android:id/button_always"))) try {
                mDevice.findObject(By.res("android:id/button_always")).click();
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.d(LOG_TAG,"failed to select diff build variant app .(locate error)!");
            }
            // click again to make sure its selection
            if (mDevice.hasObject(By.res("android:id/button_always"))) try {
                mDevice.findObject(By.res("android:id/button_always")).click();
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.d(LOG_TAG,"failed to select diff build variant app .(locate error)!");
            }
        }
    }


    public void killApps(){
        try{
            mDevice.pressHome();
            mDevice.pressRecentApps();


            // Clear all isn't always visible unless you scroll all apps down
            // if dax ui app is visible and directly kill the app
            if (mDevice.hasObject(By.descContains("Dolby Atmos"))){
                if (mDevice.wait(
                        Until.hasObject(By.res("com.android.systemui:id/dismiss_task")),
                        SLEEP_TIME_UNIT_SECOND*1000*2)
                        ){
                    mDevice.findObject(By.descContains("Dolby Atmos").res("com.android.systemui:id/dismiss_task")).click();
                    // click again to make sure app is killed
                    // animation exist will make that object could be found but after animation over, object dismiss
                    // which would result in below click operation failed
                    if (mDevice.hasObject(By.descContains("Dolby Atmos").res("com.android.systemui:id/dismiss_task"))){
//                        mDevice.findObject(By.descContains("Dolby Atmos").res("com.android.systemui:id/dismiss_task")).click();
                        try {
                            mDevice.findObject(By.descContains("Dolby Atmos").res("com.android.systemui:id/dismiss_task")).click();
                        }catch (NullPointerException e){
                            Log.d("kill-app","while stopping the dolby app again in recent apps , it has already dismissed !");
                        }
                    }
                }
            }else {
                // kill all apps including dax ui app
                int height = mDevice.getDisplayHeight();
                int width = mDevice.getDisplayWidth();
                mDevice.swipe(width/2,height/2, width/2, height, 50);

                if(mDevice.wait(
                        Until.hasObject(By.res("com.android.systemui:id/button").text("CLEAR ALL")),
                        SLEEP_TIME_UNIT_SECOND*1000*2)){
                    mDevice.findObject(By.res("com.android.systemui:id/button").text("CLEAR ALL")).click();
                }
            }
        }catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void pressBackButton(){
        boolean result ;
        result = mDevice.pressBack();
        if (result){
            waitForSecond(2);
        }else{
            assertTrue("Sometimes BACK key presses will generate no events " +
                            " if already on home page or there is nothing to go back to",
                    true);
        }
    }

    public void waitForSecond(int seconds) {
        if (FLAG_TEST_THREAD_SLEEP){
            try {
                TimeUnit.SECONDS.sleep(seconds);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            // not make test thread sleep
        }

    }

}
