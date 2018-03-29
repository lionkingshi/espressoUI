package com.dolby.qa.utils.SystemUIUtils;

public interface ISystemUIViewAssertions {

    void checkSystemFirstUIDisplay(String profileName, boolean fromSecondSystemUI);

    void checkSystemSecondUIDisplay(String profileName);

}
