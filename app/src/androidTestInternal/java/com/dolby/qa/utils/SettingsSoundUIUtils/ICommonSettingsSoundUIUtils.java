package com.dolby.qa.utils.SettingsSoundUIUtils;


import android.support.annotation.Nullable;
import android.support.test.uiautomator.UiObject;

import com.dolby.qa.utils.commonUtils.ICommonUtils;

public interface ICommonSettingsSoundUIUtils extends ICommonUtils {
    @Nullable
    UiObject getDolbySettingsSoundBarObject();
}
