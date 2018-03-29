package com.dolby.qa.utils.consumerUIUtils;

interface IConsumerUIViewAssertions {

    void checkUIMainView(
            int profileType,
            int isDeaNum,
            int isIeqNum,
            boolean isFlatGEQ,
            int [] config);

    void checkUIOffView();

    void checkUINavigationView();

     void checkUIHeadphoneView();

     void checkUIExploreDolbyView();

     void checkUITutorialStartView();

     void checkUITutorialSecondView();

     void checkUITutorialEndView();

     void checkUIResetView();
}
