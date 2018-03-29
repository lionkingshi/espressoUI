package com.dolby.qa.utils.consumerUIUtils;

public interface IConsumerUIViewAction {
     // select 4 profile by name
     void tabToDynamicProfileView();

     void tabToMovieProfileView();

     void tabToMusicProfileView();

     void tabToCustomProfileView();

     // actions in custom profile
     void selectIeqInCustomProfile(int preset);

     void clickDeaSliderInCustomProfile(int position);

     void swipeDeaSliderInCustomProfile(int position);

     void clickResetInCustomProfile();

     // actions in music profile
     void selectIeqInMusicProfile(int preset);

     void clickResetInMusicProfile();

     // actions in movie profile
     void clickDeaSliderInMovieProfile(int position);

     void swipeDeaSliderInMovieProfile(int position);

     void clickResetInMovieProfile();

     // actions in dynamic profile
     void clickResetInDynamicProfile();

     // turn on or off ui
     void turnOnDax();

     void turnOffDax();

     // actions in navigation view
     void openNavigationView();

     void exitNavigationView();

     void navigateToHeadphoneView();

     void exitHeadphoneNavigation();

     void navigateToExploreDolbyView();

     void exitExploreDolbyNavigation();

     void clickExploreDolbyLearnMore();

     void exitLearnMoreWebsite();

     void clickExploreDolbyAccess();

     void exitDolbyAccessWebsite();

     void navigateToTutorialView();

     void exitTutorialNavigation();

     void swipeLeftTutorialNavigation();

     void swipeRightTutorialNavigation();

     void navigationToResetView();

     void exitResetNavigation();

     void clickResetInResetNavigation();
}
