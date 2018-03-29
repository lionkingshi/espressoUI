package com.dolby.qa.utils.commonUtils;
import android.content.Context;

public interface ICommonUtils {

    void unlockDeviceScreenQuickly();

    void startActivityFromHomeScreen(String packageName);

    boolean isTablet(Context context);

    void selectAPP();

    void pressBackButton();

    void waitForSecond(int seconds);

    void killApps();

}
