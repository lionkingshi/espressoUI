package com.dolby.qa.utils.SystemUIUtils;

import android.support.annotation.Nullable;
import android.support.test.uiautomator.UiObject;

import com.dolby.qa.utils.commonUtils.ICommonUtils;


public interface ICommonSystemUIUtils extends ICommonUtils {

    @Nullable
    UiObject getProfileItemObjectInSystemFirstUI();

    @Nullable
    UiObject getIconItemObjectInSystemFirstUI();

    // check dolby quick settings first ui display
    boolean judgeDolbyIconInQuickSettingsUILeftTopPosition(String profileName, boolean isChecked);

    void openQuickSettings();

    void dragDolbyQuickSettingsToLeftTopPosition();

    void resetQuickSettingsPanel();

    void waitForSystemFirstUIRender();

    void waitForSystemSecondUIRender();

}
