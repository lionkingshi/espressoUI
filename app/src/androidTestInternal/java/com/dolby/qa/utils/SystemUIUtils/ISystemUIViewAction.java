package com.dolby.qa.utils.SystemUIUtils;

import com.dolby.qa.utils.commonUtils.ICommonUtils;

public interface ISystemUIViewAction extends ICommonUtils {

    void navigateToSystemFirstUI();

    void clickDolbyIconItemInSystemFirstUI();

    void clickDolbyProfileItemInSystemFirstUI();

    void navigateToSystemSecondUI();

    void turnOffDaxInSystemSecondUI();

    void turnOnDaxInSystemSecondUI();

    void selectProfileInSystemSecondUI(String profileName);

    void clickMoreSettingsItemInSystemSecondUI();

    void clickDoneItemFromSystemSecondUI();

}
