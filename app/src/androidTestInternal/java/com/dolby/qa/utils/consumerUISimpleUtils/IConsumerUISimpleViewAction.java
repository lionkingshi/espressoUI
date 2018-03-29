package com.dolby.qa.utils.consumerUISimpleUtils;

import com.dolby.qa.utils.commonUtils.ICommonUtils;

public interface IConsumerUISimpleViewAction extends ICommonUtils {

    void navigateToConsumerUIDirectly();

    void turnOffDaxFromConsumerUI();

    void turnOnDaxFromConsumerUI();

    void selectProfileFromConsumerUI(String profileName);

}
