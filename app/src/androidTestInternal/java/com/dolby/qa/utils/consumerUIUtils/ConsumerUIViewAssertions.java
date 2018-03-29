package com.dolby.qa.utils.consumerUIUtils;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.dolby.daxappui.R;
import com.dolby.qa.utils.commonUtils.UIConfiguration;

import junit.framework.AssertionFailedError;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.LayoutAssertions.noOverlaps;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isBelow;
import static android.support.test.espresso.assertion.PositionAssertions.isBottomAlignedWith;
import static android.support.test.espresso.assertion.PositionAssertions.isCompletelyAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isCompletelyBelow;
import static android.support.test.espresso.assertion.PositionAssertions.isCompletelyLeftOf;
import static android.support.test.espresso.assertion.PositionAssertions.isCompletelyRightOf;
import static android.support.test.espresso.assertion.PositionAssertions.isLeftAlignedWith;
import static android.support.test.espresso.assertion.PositionAssertions.isLeftOf;
import static android.support.test.espresso.assertion.PositionAssertions.isRightAlignedWith;
import static android.support.test.espresso.assertion.PositionAssertions.isTopAlignedWith;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.GONE;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.INVISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.dolby.qa.utils.Constants.DAX_BE_STATUS_DEFAULT;
import static com.dolby.qa.utils.Constants.DAX_CUSTOM_IEQ_STATUS_DEFAULT;
import static com.dolby.qa.utils.Constants.DAX_DEA_DEFAULT_VALUE;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_BALANCED;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_DETAILED;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_OFF;
import static com.dolby.qa.utils.Constants.DAX_IEQ_PRESET_WARM;
import static com.dolby.qa.utils.Constants.DAX_MUSIC_IEQ_STATUS_DEFAULT;
import static com.dolby.qa.utils.Constants.DAX_STATUS_ON;
import static com.dolby.qa.utils.Constants.DAX_SV_STATUS_DEFAULT;
import static com.dolby.qa.utils.Constants.DAX_VL_STATUS_DEFAULT;
import static com.dolby.qa.utils.Constants.DEA_INDEX_IN_CONFIG_ARRAY;
import static com.dolby.qa.utils.Constants.DS1_CUSTOM1_DE_STATUS_DEFAULT;
import static com.dolby.qa.utils.Constants.DS1_CUSTOM2_DE_STATUS_DEFAULT;
import static com.dolby.qa.utils.consumerUIUtils.utils.LocalCustomMatcher.childAtPosition;
import static com.dolby.qa.utils.consumerUIUtils.utils.LocalCustomMatcher.withProgress;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;


public final class ConsumerUIViewAssertions {
    private static final String TAG = "QAV-"+ConsumerUIViewAssertions.class.getSimpleName();

    private static String product_version ;
    private static boolean isDaxProductVersion ;
    private static boolean isTablet ;
    private static String[] mProfileName ;
    private static boolean isSVEnabled ;
    private static UiDevice mDevice;

    public ConsumerUIViewAssertions() {
        Log.d("Constructor",TAG);
    }

    public static void setUiConfiguration(final UIConfiguration mUIConf) {
        product_version = mUIConf.getProductVersion();
        isDaxProductVersion = mUIConf.isDax2();
        isTablet = mUIConf.isTablet() ;
        mProfileName=mUIConf.getProfileName();
        isSVEnabled = mUIConf.judgeSVInCustomProfileIsEnableOrNot();
        Log.d(TAG,"product version,is tablet device ,sv is enabled :"+product_version+":"+isTablet+":"+isSVEnabled);
        mDevice= UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

//    public static void checkMainViewDisplayAndStatus(UIConfiguration mUIConf){
//        //int profileId = mUIConf.get
//    }

//    public boolean checkIsTabletDevice(){
//        return isTablet;
//    }

    public static void checkMainViewDisplayAndStatus(int profileType,boolean isCheckedSV,boolean isCheckedDE,int isIeqNum,
                                                 boolean isFlatGEQ,boolean isCheckedBE,boolean isCheckedVL,int [] config) {
        // step 1 : check the main subview position
        // phone and tablet have diff view hierarchy
        if (isTablet) checkTabletMainViewPosition();else checkPhoneMainViewPosition();
        //step 2: check the container view display
        checkContainerViewDisplayAndStatus(profileType,isCheckedSV,isCheckedDE,isIeqNum,isFlatGEQ,isCheckedBE,isCheckedVL,config);
    }

    private static void checkPhoneMainViewPosition(){
        Matcher<View> container_view_matcher=allOf(withId(R.id.containerView),isDisplayed());
        onView(container_view_matcher).check(matches(isCompletelyDisplayed()));

        //if user did not click the navigation bar , NavigationView should not exposed to the end user
        onView(withId(R.id.nav_view)).check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.nav_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.nav_view)).check(noOverlaps());

        //tool bar should display correctly
        onView(withId(R.id.toolbar)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.toolbar)).check(noOverlaps());
//        onView(withId(R.id.dolbyToolbar)).check(matches(isCompletelyDisplayed()));
//        onView(withId(R.id.dolbyToolbar)).check(noOverlaps());
        if (isDaxProductVersion){
            onView(withId(R.id.dsLogoText)).check(matches(isCompletelyDisplayed()));
            onView(withId(R.id.dsLogoText)).check(matches(withText(R.string.app_name)));
        }else {
            onView(withId(R.id.dsLogoText)).check(matches(isCompletelyDisplayed()));
            onView(withId(R.id.dsLogoText)).check(matches(withText(R.string.app_name)));
        }
        onView(allOf(withId(R.id.dsLogoText),isDisplayed())).check(isLeftOf(withId(R.id.fragPower)));
        checkDaxOnOffStatus(DAX_STATUS_ON);


        //check containerView have 3 sub view : profile table , title bar and profile view pager
        onView(container_view_matcher).check(noOverlaps());
        onView(allOf(isDisplayed(), withId(R.id.titleBarShadow))).check(isCompletelyBelow(allOf(isDisplayed(),withId(R.id.profiletable))));
        onView(allOf(isDisplayed(), withId(R.id.titleBarShadow))).check(isCompletelyAbove(allOf(isDisplayed(),withId(R.id.profileViewpager))));

        //check profile view pager have 2 sub view : profileTop and profileFeatures View
        onView(allOf(isDisplayed(),withId(R.id.profileViewpager))).check(noOverlaps());
        Matcher<View> fragment_profile_view_matcher =
                allOf(withId(R.id.profileTop),isDisplayed());
        Matcher<View> fragment_global_settings_matcher =
                allOf(withId(R.id.profileFeatures),isDisplayed());
        onView(fragment_profile_view_matcher).check(matches(isDisplayed()));
        onView(fragment_global_settings_matcher).check(matches(isDisplayed()));
        onView(fragment_profile_view_matcher).check(isCompletelyAbove(fragment_global_settings_matcher));
    }

    private static void checkTabletMainViewPosition(){
        //if user did not click the navigation bar , NavigationView should not exposed to the end user
        onView(withId(R.id.nav_view)).check(matches(not(isCompletelyDisplayed())));
        onView(withId(R.id.nav_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.nav_view)).check(noOverlaps());

        //tool bar should display correctly
        onView(withId(R.id.toolbar)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.toolbar)).check(noOverlaps());
        onView(withId(R.id.toolbar)).check(isAbove(withId(R.id.container)));
        onView(withId(R.id.dolbyToolbar)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.dolbyToolbar)).check(noOverlaps());
        onView(allOf(withId(R.id.fragPower),isDisplayed())).check(matches(isCompletelyDisplayed()));
        checkDaxOnOffStatus(DAX_STATUS_ON);


        //check containerView have 3 sub view
        Matcher<View> contaniner_view_matcher=allOf(withId(R.id.containerView),isDisplayed());
        onView(contaniner_view_matcher).check(matches(isCompletelyDisplayed()));
        onView(contaniner_view_matcher).check(noOverlaps());
        onView(allOf(withId(R.id.container),isDisplayed())).check(noOverlaps());


        //sub view 1 : titleBar
        onView(withId(R.id.titleBar)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.dsLogoText)).check(matches(isCompletelyDisplayed()));
//        onView(withId(R.id.dsLogoText)).
//                check(isPartiallyLeftOf(allOf(withChild(withId(R.id.fragProfilePanel)),isDisplayed())));

        //sub view 2:  titleBarShadow
        //titleBarShadow bar position
        onView(withId(R.id.titleBarShadow)).check(isCompletelyBelow(withId(R.id.titleBar)));
//        onView(withId(R.id.titleBarShadow)).
//                check(isCompletelyAbove(allOf(withChild(withId(R.id.fragProfilePanel)),isDisplayed())));

        //sun view 3 : profile panel
        onView(allOf(withChild(withId(R.id.fragProfilePanel)),isDisplayed())).check(matches(isCompletelyDisplayed()));
        onView(allOf(withChild(withId(R.id.fragProfilePanel)),isDisplayed())).check(noOverlaps());
        onView(allOf(withId(R.id.fragProfilePanel),isDisplayed())).
                check(isCompletelyLeftOf(allOf(withId(R.id.fragProfilePanelTablet),isDisplayed())));

        onView(allOf(withId(R.id.fragProfilePanelTablet),isDisplayed())).
                check(noOverlaps());
        onView(allOf(withId(R.id.fragProfilePanelTablet),isDisplayed())).
                check(noOverlaps(isAssignableFrom(Switch.class)));
    }

    private static void checkContainerViewDisplayAndStatus(
            int profileType,
            boolean isCheckedSV,
            boolean isCheckedDE,
            int isIeqNum,
            boolean isFlatGEQ,
            boolean isCheckedBE,
            boolean isCheckedVL,
            int [] config)
    {
        //step ONE : check the profile top UI display and status
        boolean m_profile_reset_button_flag;
        m_profile_reset_button_flag =
                judgeProfileResetFlagExist(
                        profileType,
                        config[DEA_INDEX_IN_CONFIG_ARRAY],
                        isIeqNum,
                        isFlatGEQ,
                        config);

        checkProfileTopViewStatus(profileType,isFlatGEQ, m_profile_reset_button_flag, config);

        //step TWO : check the profile feature UI display and status
        checkProfileFeatureViewStatus(
                profileType, isCheckedSV, isCheckedDE, isIeqNum, isCheckedBE, isCheckedVL, config);
    }

    //assert Profile Top View Status
    private static void checkProfileTopViewStatus(int profileType, boolean isFlatGEQ, boolean isResetButtonExist, int [] config){
        //step ONE : check top-level profile top sub view position
        if (isTablet) checkTabletFragProfileViewPosition();else checkPhoneProfileTopViewPosition();

        //STEP two : check profile top sub view status
        checkProfileTopSubViewStatus(profileType, isFlatGEQ, isResetButtonExist, config);
    }

    // phone : check top-level profile sub view position
    private static void checkPhoneProfileTopViewPosition(){
        //profile top view have three sub view : profile table , geqTextLayout and GeqViewLayout
        //defined in the profile.xml
        onView(allOf(withId(R.id.profileTop),isDisplayed())).check(noOverlaps());

        //profile table position
//        onView(allOf(withId(R.id.profiletable),isDisplayed())).
////                check(isTopAlignedWith(allOf(withId(R.id.profileTop),isDisplayed())));
//        onView(allOf(withId(R.id.profiletable),isDisplayed())).
//                check(isLeftAlignedWith(allOf(withId(R.id.profileTop),isDisplayed())));
//        onView(allOf(withId(R.id.profiletable),isDisplayed())).
//                check(isRightAlignedWith(allOf(withId(R.id.profileTop),isDisplayed())));
        onView(allOf(withId(R.id.profiletable),isDisplayed())).
                check(noOverlaps());
        //geqTextLayout position
//        onView(allOf(withId(R.id.geqTextLayout),isDisplayed())).
//                check(isLeftAlignedWith(allOf(withId(R.id.profileTop),isDisplayed())));
//        onView(allOf(withId(R.id.geqTextLayout),isDisplayed())).
//                check(isRightAlignedWith(allOf(withId(R.id.profileTop),isDisplayed())));
        onView(allOf(withId(R.id.geqTextLayout),isDisplayed())).
                check(isBelow(allOf(withId(R.id.profiletable),isDisplayed())));
        onView(allOf(withId(R.id.geqTextLayout),isDisplayed())).
                check(isAbove(allOf(withId(R.id.GeqViewLayout),isDisplayed())));
        onView(allOf(withId(R.id.geqTextLayout),isDisplayed())).
                check(noOverlaps());
        //GeqViewLayout position
        ViewInteraction profile_view_pager_view_interaction =
                onView(allOf(withId(R.id.GeqViewLayout),isDisplayed()));
        //mprofileViewPager.check(isLeftAlignedWith(withId(R.id.fragProfile)));
        //mprofileViewPager.check(isRightAlignedWith(withId(R.id.fragProfile)));
        profile_view_pager_view_interaction.
                check(isBottomAlignedWith(allOf(withId(R.id.profileTop),isDisplayed())));
        profile_view_pager_view_interaction.
                check(noOverlaps());
    }

    // tablet : check top-level profile sub view position
    private static void checkTabletFragProfileViewPosition(){
        //fragprofile view group have three sub view : profile table , title bar shadow and profile viewpager
        //defined in the fragprofilefilepresets.xml
        onView(allOf(withId(R.id.fragProfilePanelTablet),isDisplayed())).check(noOverlaps());

        //profile view pager position
        ViewInteraction profile_view_pager_view_interaction = onView(allOf(withId(R.id.profileLayout),isDisplayed()));
        //mprofileViewPager.check(isLeftAlignedWith(withId(R.id.fragProfile)));
        //mprofileViewPager.check(isRightAlignedWith(withId(R.id.fragProfile)));
        //mprofileViewPager.check(isBottomAlignedWith(withId(R.id.fragProfilePanelTablet)));
        profile_view_pager_view_interaction.check(noOverlaps());
    }

    //check
    private static void checkProfileTopSubViewStatus(
            int profileType,
            boolean isFlatGEQ,
            boolean isResetButtonExist,
            int [] config)
    {
        //sub view 1
        checkProfileTableViewStatus(profileType, isResetButtonExist);

        //sub view 2
        checkProfileGeqTextViewStatus();

        //sub view 3
        checkProfileGeqViewStatus(isFlatGEQ, config);
    }

    private static void checkProfileTableViewStatus(int profileType, boolean isResetButtonExist){
        String mCurrentProfileName = mProfileName[profileType];
        ViewInteraction mprofileName;
        Matcher<View> mProfileResetButtonMatcher;
        ViewInteraction mProfileResetButton;

        if (isTablet){
            onView(allOf(withId(R.id.profileType),isDisplayed())).check(noOverlaps());
            //position check
            // for tablet , no profile icon view exist
            mprofileName=onView(allOf(withId(R.id.profileName),withParent(withId(R.id.profileType)),isDisplayed()));
            mProfileResetButtonMatcher=allOf(withId(R.id.profileResetButton),
                    childAtPosition(
                            allOf(withId(R.id.profileType),isDisplayed()),
                            1));
            mProfileResetButton=onView(mProfileResetButtonMatcher);
            mprofileName.check(isCompletelyLeftOf(mProfileResetButtonMatcher));
        }else
        {
            onView(withId(R.id.profileViewpager)).check(noOverlaps());
            //position check
            mprofileName=onView(allOf(withId(R.id.profileName),isDisplayed()));
            mProfileResetButtonMatcher=allOf(withId(R.id.profileResetButton),
                    childAtPosition(
                            allOf(withId(R.id.profileType),isDisplayed()),
                            2));
            mProfileResetButton=onView(mProfileResetButtonMatcher);
//        mprofileIcon.check(isLeftAlignedWith(withId(R.id.profileViewpager)));
            mProfileResetButton.check(isRightAlignedWith(withId(R.id.profileViewpager)));
            mprofileName.check(isCompletelyRightOf(allOf(withId(R.id.profileIcon),isDisplayed())));
            mprofileName.check(isCompletelyLeftOf(mProfileResetButtonMatcher));
        }

        //status check
        //check profile name
//        mprofileName.check(noEllipsizedText());

        switch (mCurrentProfileName){
            case "Dynamic" :
                mprofileName.check(matches(withText(R.string.dynamic)));
                break;
            case "Movie" :
                mprofileName.check(matches(withText(R.string.movie)));
                break;
            case "Music":
                mprofileName.check(matches(withText(R.string.music)));
                break;
            case "Custom" :
                mprofileName.check(matches(withText(R.string.custom)));
                break;
//            case "Custom 1":
//                mprofileName.check(matches(withText(R.string.custom_1)));
//                break;
//            case "Custom 2":
//                mprofileName.check(matches(withText(R.string.custom_2)));
//                break;
            default:
                break ;
        }
        //check reset button status
        if(isResetButtonExist){
            mProfileResetButton.check(matches(allOf(withEffectiveVisibility(VISIBLE),isDisplayed())));
        } else {
            mProfileResetButton.check(matches(allOf(withEffectiveVisibility(INVISIBLE),not(isDisplayed()))));
        }
    }

    private static void checkProfileGeqTextViewStatus(){
        onView(allOf(isDisplayed(),withId(R.id.geqTextLayout))).check(noOverlaps());
    }

    private static void checkProfileGeqViewStatus(boolean isFlatGEQ, int[] config){
        onView(allOf(isDisplayed(),withId(R.id.GeqViewLayout))).check(noOverlaps());
    }


    private static void checkProfileFeatureViewStatus(int profileType,boolean isCheckedSV,boolean isCheckedDE,int isIeqNum,
                                                      boolean isCheckedBE,boolean isCheckedVL,int [] config){
        String mCurrentProfileName = mProfileName[profileType];
        switch (mCurrentProfileName){
            case "Dynamic":
                checkDynamicProfileFeatureViewStatus(isCheckedBE,config);
                break;
            case "Movie":
                checkMovieProfileFeatureViewStatus(isCheckedDE,isCheckedBE,config);
                break;
            case "Music":
                checkMusicProfileFeatureViewStatus(isIeqNum,isCheckedBE,config);
                break;
            case "Custom":
                checkCustomProfileFeatureViewStatus(isCheckedSV,isCheckedVL,isCheckedBE,isCheckedDE,isIeqNum,config);
                break;
            default:
                break;
        }
    }

    private static void checkDynamicProfileFeatureViewStatus(boolean isCheckedBE, int [] config){
        Matcher<View>[] mProfileFeatureViewSubViewArray = new Matcher[4];

        for (int position_subview=0;position_subview<3;position_subview++){
            mProfileFeatureViewSubViewArray[position_subview] =
                    childAtPosition(allOf(withChild(withId(R.id.ieqLayout)),isDisplayed()),position_subview);
//                    allOf(withId(R.id.profileResetButton),childAtPosition(allOf(withId(R.id.profileFeatures),isDisplayed()),position_subview));
        }

        //check ieq and sv/vl view non-exist
//        onView(mProfileFeatureViewSubViewArray[2]).check(matches(allOf(withEffectiveVisibility(GONE),not(isDisplayed()))));
        onView(mProfileFeatureViewSubViewArray[2]).check(matches(not(isDisplayed())));
        onView(mProfileFeatureViewSubViewArray[1]).check(matches(allOf(withEffectiveVisibility(GONE),not(isDisplayed()))));
        onView(mProfileFeatureViewSubViewArray[0]).check(matches(allOf(withEffectiveVisibility(VISIBLE),isDisplayed())));

//        //check de view non-exist
//        onView(childAtPosition(allOf(withId(R.id.featureBeDeSwitches),isDisplayed()),1)).
//                check(matches(allOf(withEffectiveVisibility(GONE),not(isDisplayed()))));

        //check be and profile description exist
//        checkBassEnhancerStatus(isCheckedBE);
        checkProfileDescription();
    }

    private static void checkMovieProfileFeatureViewStatus(boolean isCheckedDE, boolean isCheckedBE,int [] config){
        Matcher<View>[] mProfileFeatureViewSubViewArray = new Matcher[3];

        for (int position_subview=0;position_subview<3;position_subview++){
            mProfileFeatureViewSubViewArray[position_subview] =
                    childAtPosition(allOf(withChild(withId(R.id.featureDeSwitches)),isDisplayed()),position_subview);
        }

        //check ieq  sv/vl and profile description view non-exist
        onView(mProfileFeatureViewSubViewArray[0]).check(matches((not(isDisplayed()))));
        onView(mProfileFeatureViewSubViewArray[1]).check(matches(allOf(withEffectiveVisibility(GONE),not(isDisplayed()))));
        onView(mProfileFeatureViewSubViewArray[2]).check(matches(allOf(withEffectiveVisibility(VISIBLE),isDisplayed())));
//        onView(mProfileFeatureViewSubViewArray[3]).check(matches(allOf(withEffectiveVisibility(INVISIBLE),not(isDisplayed()))));

        //check be and de view exist
        checkFeatureBeDeSwitches(isCheckedBE,isCheckedDE,config);
    }

    private static void checkMusicProfileFeatureViewStatus(int isIeqNum, boolean isCheckedBE,int [] config){
        Matcher<View>[] mProfileFeatureViewSubViewArray = new Matcher[3];

        for (int position_subview=0;position_subview<3;position_subview++){
            mProfileFeatureViewSubViewArray[position_subview] =
                    childAtPosition(allOf(withChild(withId(R.id.ieqLayout)),isDisplayed()),position_subview);
        }

        //check profile description and sv/vl view non-exist
        onView(mProfileFeatureViewSubViewArray[0]).check(matches(not(isDisplayed())));
        onView(mProfileFeatureViewSubViewArray[2]).check(matches(not(isDisplayed())));
        onView(mProfileFeatureViewSubViewArray[1]).check(matches(allOf(withEffectiveVisibility(VISIBLE),isDisplayed())));
//        onView(mProfileFeatureViewSubViewArray[0]).check(matches(allOf(withEffectiveVisibility(VISIBLE),isDisplayed())));
//        onView(mProfileFeatureViewSubViewArray[0]).check(isCompletelyAbove(mProfileFeatureViewSubViewArray[2]));

        //check de view non-exist
//        onView(childAtPosition(allOf(withId(R.id.featureBeDeSwitches),isDisplayed()),1)).
//                check(matches(allOf(withEffectiveVisibility(INVISIBLE),not(isDisplayed()))));

        //check ieq status
        checkIeqLayout(isIeqNum);
        //check be view status
//        checkBassEnhancerStatus(isCheckedBE);
    }

    private static void checkCustomProfileFeatureViewStatus(boolean isCheckedSV, boolean isCheckedVL,
                                                            boolean isCheckedBE, boolean isCheckedDE,
                                                            int isIeqNum, int [] config){
        Matcher<View>[] mProfileFeatureViewSubViewArray = new Matcher[3];

        for (int position_subview=0;position_subview<3;position_subview++){
            mProfileFeatureViewSubViewArray[position_subview] =
                    childAtPosition(allOf(withChild(withId(R.id.ieqLayout)),isDisplayed()),position_subview);
        }

        //check profile description view non-exist
        onView(mProfileFeatureViewSubViewArray[0]).check(matches(not(isDisplayed())));
        onView(mProfileFeatureViewSubViewArray[1]).check(matches(allOf(withEffectiveVisibility(VISIBLE),isDisplayed())));
        onView(mProfileFeatureViewSubViewArray[2]).check(matches(allOf(withEffectiveVisibility(VISIBLE),isDisplayed())));
//        onView(mProfileFeatureViewSubViewArray[2]).check(matches(allOf(withEffectiveVisibility(VISIBLE),isDisplayed())));
        onView(mProfileFeatureViewSubViewArray[1]).check(isCompletelyAbove(mProfileFeatureViewSubViewArray[2]));
//        onView(mProfileFeatureViewSubViewArray[1]).check(isCompletelyAbove(mProfileFeatureViewSubViewArray[2]));

        //check ieq status
        checkIeqLayout(isIeqNum);
        // check sv and vl status
//        checkFeatureSvVlSwitches(isCheckedSV,isCheckedVL);
        //check be and de view status
        checkFeatureBeDeSwitches(isCheckedBE,isCheckedDE,config);
    }

    private static void checkIeqLayout(int isIeqNum){
        // sub view position
        checkIeqLayoutSubViewPosition( );
        //ieq text name same as string defined in strings.xml
        onView(allOf(withId(R.id.ieqText),isDisplayed())).check(matches(withText(R.string.ieq_text)));
//        onView(allOf(withId(R.id.ieqText),isDisplayed())).check(noEllipsizedText());
        //check ieq name text same as dap status
        checkIeqNameStatus(isIeqNum);
    }

    @SuppressWarnings("unchecked")
    private static void checkIeqLayoutSubViewPosition(){
        // parent view has no overlap
        onView(allOf(withId(R.id.ieqLayout),isDisplayed())).check(noOverlaps());
        onView(allOf(isCompletelyDisplayed(),withId(R.id.ieqName))).check(isCompletelyAbove(allOf(isCompletelyDisplayed(),withId(R.id.equalizerView))));
        onView(allOf(isCompletelyDisplayed(),withId(R.id.ieqText))).check(isCompletelyAbove(allOf(isCompletelyDisplayed(),withId(R.id.equalizerView))));
        onView(allOf(isCompletelyDisplayed(),withId(R.id.ieqText))).check(isCompletelyLeftOf(allOf(isCompletelyDisplayed(),withId(R.id.ieqName))));
        //if the layout changed , code would be modified
        onView(allOf(withId(R.id.equalizerView),isCompletelyDisplayed())).check(noOverlaps());
        ViewInteraction[] ieqArray;
        ieqArray=new ViewInteraction [4];
        Matcher[] ieqMatcherArray;
        ieqMatcherArray = new Matcher[4];
        for (int index=0;index<3;index++){
            //ieqarray[index]=onData(anything()).atPosition(index).inAdapterView(allOf(withId(R.id.equalizerListView),isDisplayed()));
            //ieqarray[index]=onData(anything()).atPosition(index).inAdapterView(allOf(withId(R.id.equalizerListView),isDisplayed()));
            ieqMatcherArray[index]=allOf(instanceOf(LinearLayout.class),childAtPosition(allOf(withId(R.id.equalizerListView),isDisplayed()),index));
            ieqArray[index]=onView(ieqMatcherArray[index]);
        }
        ieqMatcherArray[3]=allOf(withId(R.id.equalizerListOff),isDisplayed());
        ieqArray[3]=onView(ieqMatcherArray[3]);


        ieqArray[0].check(isTopAlignedWith(ieqMatcherArray[1]));
        ieqArray[0].check(isBottomAlignedWith(ieqMatcherArray[1]));
        ieqArray[0].check(isCompletelyLeftOf(ieqMatcherArray[1]));

        ieqArray[1].check(isTopAlignedWith(ieqMatcherArray[2]));
        ieqArray[1].check(isBottomAlignedWith(ieqMatcherArray[2]));
        ieqArray[1].check(isCompletelyLeftOf(ieqMatcherArray[2]));

        ieqArray[2].check(isTopAlignedWith(ieqMatcherArray[3]));
        ieqArray[2].check(isBottomAlignedWith(ieqMatcherArray[3]));
        ieqArray[2].check(isCompletelyLeftOf(ieqMatcherArray[3]));
    }

    private static void checkIeqNameStatus(int isIeqNum){
        String mIeqText="text";
        switch (isIeqNum){
            case DAX_IEQ_PRESET_BALANCED:
//                // commented code is one solution to check
//                if (mDevice.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"ieqName"))){
//                    mIeqText=mDevice.findObject(By.res(PACKAGE_NAME_UNDER_TEST,"ieqName")).getText();
//                    assertEquals("Open",mIeqText);
//                }else {
//                    assertTrue("ieq name is not expected !",false);
//                }
                try {
                    onView(allOf(withId(R.id.ieqName),isCompletelyDisplayed())).check(
                            matches(withText(R.string.balanced)));
                }catch (AssertionFailedError e){
                    // check again
                    onView(allOf(withId(R.id.ieqName),isDisplayed())).check(
                            matches(withText(R.string.detailed)));
                }
                break;
            case DAX_IEQ_PRESET_WARM:
//                if (mDevice.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"ieqName"))){
//                    mIeqText=mDevice.findObject(By.res(PACKAGE_NAME_UNDER_TEST,"ieqName")).getText();
//                    assertEquals(mIeqText,"Rich");
//                }else {
//                    assertTrue("ieq name is not expected !",false);
//                }
                try {
                    onView(allOf(withId(R.id.ieqName),isDisplayed())).check(
                            matches(withText(R.string.warm)));
                }catch (AssertionFailedError e){
                    onView(allOf(withId(R.id.ieqName),isDisplayed())).check(
                            matches(withText(R.string.warm)));
                }
                break;
            case DAX_IEQ_PRESET_DETAILED:
//                if (mDevice.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"ieqName"))){
//                    mIeqText=mDevice.findObject(By.res(PACKAGE_NAME_UNDER_TEST,"ieqName")).getText();
//                    assertEquals("Focused",mIeqText);
//                }else {
//                    assertTrue("ieq name is not expected !",false);
//                }
                try {
                    onView(allOf(withId(R.id.ieqName),isDisplayed())).check(
                            matches(withText(R.string.detailed)));
                }catch (AssertionFailedError e){
                    onView(allOf(withId(R.id.ieqName),isDisplayed())).check(
                            matches(withText(R.string.detailed)));
                }
                break;
            case DAX_IEQ_PRESET_OFF:
//                if (mDevice.hasObject(By.res(PACKAGE_NAME_UNDER_TEST,"ieqName"))){
//                    mIeqText=mDevice.findObject(By.res(PACKAGE_NAME_UNDER_TEST,"ieqName")).getText();
//                    assertEquals("Off",mIeqText);
//                }else {
//                    assertTrue("ieq name is not expected !",false);
//                }

                try {
                    onView(allOf(withId(R.id.ieqName),isDisplayed())).check(
                            matches(withText(R.string.off)));
                }catch (AssertionFailedError e){
                    onView(allOf(withId(R.id.ieqName),isDisplayed())).check(
                            matches(withText(R.string.off)));
                }
                break;
            default:
                break;
        }
    }

    private static void checkFeatureSvVlSwitches(boolean isCheckedSV, boolean isCheckedVL){
        // sub view position
        checkFeatureSvVlSwitchesSubViewPosition();
        // check sv status
        checkSurroundVirtualizerStatus(isCheckedSV,null);
        //check vl status
        checkVolumeLevelerStatus(isCheckedVL);
    }

    private static void checkFeatureSvVlSwitchesSubViewPosition(){
//        onView(allOf(isCompletelyDisplayed(),withId(R.id.featureSvVlSwitches))).
//                check(noOverlaps());
//
//        onView(allOf(isCompletelyDisplayed(),withId(R.id.svView))).
//                check(noOverlaps()).
//                check(isCompletelyLeftOf(allOf(isCompletelyDisplayed(),withId(R.id.vlView))));
//        onView(allOf(isCompletelyDisplayed(),withId(R.id.svView))).
//                check(noOverlaps());
//
//        onView(allOf(isCompletelyDisplayed(),withId(R.id.svText))).check(
//                isCompletelyAbove(allOf(isCompletelyDisplayed(),withId(R.id.svButton))));
//
//        onView(allOf(isCompletelyDisplayed(),withId(R.id.vlText))).check(
//                isCompletelyAbove(allOf(isCompletelyDisplayed(),withId(R.id.vlButton))));
    }

    //assert Surround Virtualizer button status
    //if device have a mono speaker and is plugged with the headphone , sv should be visible
    //if device have a mono speaker and is plugged with no endpoint , sv should be visible but could not click
    private static void checkSurroundVirtualizerStatus(boolean isCheckedSV,int [] config){
//        boolean isSVEnable = judgeSVInCustomProfileIsEnableOrNot();
//        if(isSVEnable){
//            onView(allOf(withId(R.id.svButton),isDisplayed())).check(matches(isEnabled()));
//            //assert the svButton status : notEnable and with text "OFF"
//            if(isCheckedSV){
//                onView(allOf(withId(R.id.svButton),isDisplayed())).check(matches(allOf(isClickable(),isChecked())));
//            } else {
//                onView(allOf(withId(R.id.svButton),isDisplayed())).check(matches(allOf(isClickable(),isNotChecked())));
//            }
//        }else {
//            onView(allOf(withId(R.id.svButton),isDisplayed())).check(matches(not(isEnabled())));
//            //onView(allOf(withId(R.id.svButton),isDisplayed())).check(matches(allOf(isClickable(),isNotChecked(),withSwitchText("OFF"))));
//            onView(allOf(withId(R.id.svButton),isDisplayed())).check(matches(allOf(isClickable(),isNotChecked())));
//        }
//
//        onView(allOf(withId(R.id.svText),isDisplayed())).check(matches(withText(R.string.surround_virtualizer)));
////        onView(allOf(withId(R.id.svText),isDisplayed())).check(noEllipsizedText());
    }

    private static boolean judgeSVInCustomProfileIsEnableOrNot(){
        return isSVEnabled;
    }

    private static void checkVolumeLevelerStatus(boolean isCheckedVL){
//        ViewInteraction vl_text_view=onView(allOf(withId(R.id.vlText),isCompletelyDisplayed()));
//        ViewInteraction vl_button_view=onView(allOf(withId(R.id.vlButton),isCompletelyDisplayed()));
//        //check status and dispaly
////        vl_text_view.check(noEllipsizedText());
//        vl_text_view.check(matches(withText(R.string.volume_leveler)));
//        //assert the vlButton status : Enable and with text "OFF" or "ON"
//        if(isCheckedVL){
//            vl_button_view.check(matches(allOf(isClickable(),isEnabled(),isChecked())));
//        } else {
//            vl_button_view.check(matches(allOf(isClickable(),isEnabled(),isNotChecked())));
//        }
    }

    private static void checkFeatureBeDeSwitches(boolean isCheckedBE, boolean isCheckedDE, int [] config){
        // sub view position
        checkFeatureBeDeSwitchesSubViewPosition();
        // check sv status
//        checkBassEnhancerStatus(isCheckedBE);
        //check vl status
        checkDialogueEnhancerStatus(isCheckedDE,config);
    }

    private static void checkFeatureBeDeSwitchesSubViewPosition() {
        onView(allOf(isCompletelyDisplayed(),withId(R.id.featureDeSwitches))).
                check(noOverlaps());

        onView(allOf(isDisplayed(),withId(R.id.deView))).
                check(noOverlaps());

        onView(allOf(withId(R.id.deText),isDisplayed())).check(
                isCompletelyAbove(allOf(withId(R.id.deButton),isDisplayed())));
//        onView(allOf(isCompletelyDisplayed(),withId(R.id.svView))).
//                check(noOverlaps());
    }


    private static void checkBassEnhancerStatus(boolean isCheckedBE){
//        // bass enabled view check
//        if (isDaxProductVersion){
//            ViewInteraction be_View=onView(allOf(withId(R.id.beView),isDisplayed()));
//            //CHECK!!!
//            be_View.check(noOverlaps());
//            //check position
//            ViewInteraction be_text_view=onView(allOf(withId(R.id.beText),isDisplayed()));
//            ViewInteraction be_button_view=onView(allOf(withId(R.id.beButton),isDisplayed()));
//            be_text_view.check(isLeftAlignedWith(allOf(withId(R.id.beView),isDisplayed())));
//            be_button_view.check(isBottomAlignedWith(allOf(withId(R.id.beView),isDisplayed())));
//            be_button_view.check(isCompletelyBelow(allOf(withId(R.id.beText),isDisplayed())));
//            //check status and display
//            be_text_view.check(matches(withText(R.string.bass_enhancer)));
//            //assert the beButton status : Enable and with text "OFF" or "ON"
//            if(isCheckedBE){
//                be_button_view.check(matches(allOf(isClickable(),isEnabled(),isChecked())));
//            } else {
//                be_button_view.check(matches(allOf(isClickable(),isEnabled(),isNotChecked())));
//            }
//        }else {
//            onView(withId(R.id.beView)).check(matches(allOf(withEffectiveVisibility(INVISIBLE),not(isDisplayed()))));
//            onView(withId(R.id.beText)).check(matches(allOf(withEffectiveVisibility(INVISIBLE),not(isDisplayed()))));
//            onView(withId(R.id.beButton)).check(matches(allOf(withEffectiveVisibility(INVISIBLE),not(isDisplayed()))));
//        }
//
//        // check the be view and vl view position relationship
//        ViewInteraction vl_View=onView(allOf(withId(R.id.vlView),isDisplayed()));
//        if (isDaxProductVersion) vl_View.check(isLeftOf(allOf(withId(R.id.beView), isDisplayed())));
    }

    private static void checkDialogueEnhancerStatus(boolean isCheckedDE, int[] config){
        ViewInteraction de_View=onView(allOf(withId(R.id.deView),isDisplayed()));
        //CHECK!!!
        de_View.check(noOverlaps());
        //check position
//        de_text_matcher = allOf(withId(R.id.deText),childAtPosition(de_view_matcher,0));
//        de_button_matcher=allOf(withId(R.id.deButton),childAtPosition(de_view_matcher,1));
        ViewInteraction de_text_view=onView(allOf(withId(R.id.deText),isDisplayed()));
        ViewInteraction de_button_view=onView(allOf(withId(R.id.deButton),isDisplayed()));
        de_text_view.check(isLeftAlignedWith(allOf(withId(R.id.deView),isDisplayed())));
//        de_button_view.check(isBottomAlignedWith(allOf(withId(R.id.deView),isDisplayed())));
        de_button_view.check(isCompletelyBelow(allOf(withId(R.id.deText),isDisplayed())));
        //check status and display
        de_text_view.check(matches(withText(R.string.dialogue_enhancer)));
        //assert the beButton status : Enable and with text "OFF" or "ON"
//        if(isCheckedDE){
//            de_button_view.check(matches(allOf(isClickable(),isEnabled(),isChecked())));
//        } else {
//            de_button_view.check(matches(allOf(isClickable(),isEnabled(),isNotChecked())));
//        }
        onView(allOf(withId(R.id.deButton),isDisplayed())).check(
                matches(withProgress(config[DEA_INDEX_IN_CONFIG_ARRAY]))
        );
    }

    private static void checkProfileDescription(){
        onView(allOf(isCompletelyDisplayed(),withId(R.id.profileDescription))).
                check(matches(withText(R.string.dolby_dynamic_profile)));
    }


//    //check profile top sub view layout position
//    private static void checkProfileTopSubViewPosition(int profileType){
//        String mCurrentProfileName = mProfileName[profileType];
//        //check the sub view have no overlaps
//        onView(allOf(withId(R.id.profileTop),isDisplayed())).check(noOverlaps());
//
//        //check sub view position
//        onView(allOf(isDisplayed(),withId(R.id.profileType))).check(isCompletelyAbove(allOf(isDisplayed(),withId(R.id.geqTextLayout))));
//        onView(allOf(isDisplayed(),withId(R.id.profileType))).check(isCompletelyAbove(allOf(isDisplayed(),withId(R.id.GeqViewLayout))));
//        onView(allOf(isDisplayed(),withId(R.id.geqTextLayout))).check(isCompletelyAbove(allOf(isDisplayed(),withId(R.id.GeqViewLayout))));
//
//        //check three sub view position
//        //onView(allOf(withId(R.id.profileType),isDisplayed())).check(isLeftAlignedWith(allOf(withId(R.id.profileLayout),isDisplayed())));
//        //onView(allOf(withId(R.id.profileType),isDisplayed())).check(isRightAlignedWith(allOf(withId(R.id.profileLayout),isDisplayed())));
//        //onView(allOf(withId(R.id.profileType),isDisplayed())).check(isTopAlignedWith(allOf(withId(R.id.profileLayout),isDisplayed())));
//        switch (mCurrentProfileName){
//            case "Music":
//            case "Custom":
//            case "Custom 1":
//            case "Custom 2":
//                Matcher<View> profile_description_matcher=allOf(
//                        withId(R.id.profileDescription),
//                        childAtPosition(allOf(
//                                withId(R.id.profileLayout),isDisplayed()),
//                                1));
//                onView(profile_description_matcher).check(matches(allOf(withEffectiveVisibility(ViewMatchers.Visibility.GONE),not(isDisplayed()))));
//                onView(allOf(withId(R.id.profileType),isDisplayed())).check(isAbove(allOf(withId(R.id.music_custom_profile),isDisplayed())));
//                break;
//            case "Dynamic":
//            case "Movie":
//            case "Game":
//            case "Voice":
//                onView(allOf(withId(R.id.profileDescription),isDisplayed())).check(isBelow(allOf(withId(R.id.profileType),isDisplayed())));
//                Matcher<View> music_custom_profile_matcher=allOf(withId(R.id.music_custom_profile),
//                        childAtPosition(
//                                allOf(withId(R.id.profileLayout),isDisplayed()),
//                                2));
//                //onView(allOf(withId(R.id.profileDescription),isDisplayed())).check(isAbove(mmusiccunstonProfileMatcher));
//                onView(music_custom_profile_matcher).check(matches(allOf(withEffectiveVisibility(ViewMatchers.Visibility.GONE),not(isDisplayed()))));
//                break;
//            default:
//                break;
//        }
//    }


    private static boolean judgeProfileResetFlagExist(int profileType,boolean isCheckedSV,boolean isCheckedDE,int isIeqNum,
                                                      boolean isFlatGEQ,boolean isCheckedBE,boolean isCheckedVL,int [] config){
        boolean ret ;
        String mCurrentProfileName = mProfileName[profileType];

        switch (mCurrentProfileName){
            case "Dynamic":
                ret = !(isCheckedBE == DAX_BE_STATUS_DEFAULT);
                break;
            case "Movie":
                ret = !(isCheckedBE == DAX_BE_STATUS_DEFAULT);
                break;
            case "Music":
                ret = !((isCheckedBE == DAX_BE_STATUS_DEFAULT) && (isIeqNum == DAX_MUSIC_IEQ_STATUS_DEFAULT));
                break;
            case "Custom":
                if (!judgeSVInCustomProfileIsEnableOrNot()){
                    ret = !((isCheckedBE == DAX_BE_STATUS_DEFAULT) &&
                            (isIeqNum == DAX_CUSTOM_IEQ_STATUS_DEFAULT) &&
                            (isCheckedVL == DAX_VL_STATUS_DEFAULT));
                } else {
                    ret = !((isCheckedBE == DAX_BE_STATUS_DEFAULT) &&
                            (isIeqNum == DAX_CUSTOM_IEQ_STATUS_DEFAULT) &&
                            (isCheckedVL == DAX_VL_STATUS_DEFAULT) &&
                            (isCheckedSV == DAX_SV_STATUS_DEFAULT)
                    );
                }
                break;
            case "Custom 1":
                if (!judgeSVInCustomProfileIsEnableOrNot()){
                    ret = !((isCheckedDE == DS1_CUSTOM1_DE_STATUS_DEFAULT) && (isIeqNum == DAX_CUSTOM_IEQ_STATUS_DEFAULT));
                } else {
                    ret = !((isCheckedDE == DS1_CUSTOM1_DE_STATUS_DEFAULT) && (isCheckedSV == DAX_SV_STATUS_DEFAULT) && (isIeqNum == DAX_CUSTOM_IEQ_STATUS_DEFAULT));
                }
                break;
            case "Custom 2":
                if (!judgeSVInCustomProfileIsEnableOrNot()){
                    ret = !((isCheckedDE == DS1_CUSTOM2_DE_STATUS_DEFAULT) && (isIeqNum == DAX_CUSTOM_IEQ_STATUS_DEFAULT));
                } else {
                    ret = !((isCheckedDE == DS1_CUSTOM2_DE_STATUS_DEFAULT) && (isCheckedSV == DAX_SV_STATUS_DEFAULT) && (isIeqNum == DAX_CUSTOM_IEQ_STATUS_DEFAULT));
                }
                break;
            default:
                ret =false;
                break;
        }
        return ret;
    }

    private static boolean judgeProfileResetFlagExist(
            int profileType,
            int isDeaNum,
            int isIeqNum,
            boolean isFlatGEQ,
            int [] config)
    {
        boolean ret = true ;
        String mCurrentProfileName = mProfileName[profileType];

        switch (mCurrentProfileName)
        {
            case "Dynamic":
                ret = false;
                break;
            case "Movie":
                ret = (isDeaNum != DAX_DEA_DEFAULT_VALUE);
                break;
            case "Music":
                ret = !(isIeqNum == DAX_MUSIC_IEQ_STATUS_DEFAULT);
                break;
            case "Custom":
                ret = !((isIeqNum == DAX_CUSTOM_IEQ_STATUS_DEFAULT) && (isDeaNum == DAX_DEA_DEFAULT_VALUE));
//                if (!judgeSVInCustomProfileIsEnableOrNot()){
//                    ret = !((isCheckedBE == DAX_BE_STATUS_DEFAULT) &&
//                            (isIeqNum == DAX_CUSTOM_IEQ_STATUS_DEFAULT) &&
//                            (isCheckedVL == DAX_VL_STATUS_DEFAULT));
//                } else {
//                    ret = !((isCheckedBE == DAX_BE_STATUS_DEFAULT) &&
//                            (isIeqNum == DAX_CUSTOM_IEQ_STATUS_DEFAULT) &&
//                            (isCheckedVL == DAX_VL_STATUS_DEFAULT) &&
//                            (isCheckedSV == DAX_SV_STATUS_DEFAULT)
//                    );
//                }
                break;
//            case "Custom 1":
//                if (!judgeSVInCustomProfileIsEnableOrNot()){
//                    ret = !((isCheckedDE == DS1_CUSTOM1_DE_STATUS_DEFAULT) && (isIeqNum == DAX_CUSTOM_IEQ_STATUS_DEFAULT));
//                } else {
//                    ret = !((isCheckedDE == DS1_CUSTOM1_DE_STATUS_DEFAULT) && (isCheckedSV == DAX_SV_STATUS_DEFAULT) && (isIeqNum == DAX_CUSTOM_IEQ_STATUS_DEFAULT));
//                }
//                break;
//            case "Custom 2":
//                if (!judgeSVInCustomProfileIsEnableOrNot()){
//                    ret = !((isCheckedDE == DS1_CUSTOM2_DE_STATUS_DEFAULT) && (isIeqNum == DAX_CUSTOM_IEQ_STATUS_DEFAULT));
//                } else {
//                    ret = !((isCheckedDE == DS1_CUSTOM2_DE_STATUS_DEFAULT) && (isCheckedSV == DAX_SV_STATUS_DEFAULT) && (isIeqNum == DAX_CUSTOM_IEQ_STATUS_DEFAULT));
//                }
//                break;
            default:
                ret = true ;
                break;
        }
        return ret;
    }

    public static void checkDaxOnOffStatus(boolean flagDaxOnOrOff){
        if (flagDaxOnOrOff == DAX_STATUS_ON ){
            //ds on view should be exposed to end user
            onView(allOf(withId(R.id.powerButtonOn),withParent(withId(R.id.fragPower)))).check(matches(isCompletelyDisplayed()));
            //ds off view should not be exposed to end user
            onView(withId(R.id.poweroffLayout)).check(doesNotExist());
            //onView(withId(R.id.powerOffCenterImage)).check(doesNotExist());
            onView(withText(R.string.power_off_text)).check(doesNotExist());
        } else {
            //ds on view should not be exposed to end user
            onView(withId(R.id.powerButtonOn)).check(matches(isCompletelyDisplayed()));
            //ds off view should be exposed to end user
            onView(withId(R.id.poweroffLayout)).check(matches(isCompletelyDisplayed()));
            onView(withId(R.id.poweroffLayout)).check(noOverlaps());
            if (isDaxProductVersion){
                onView(withId(R.id.powerButtonOn)).check(isAbove(withText(R.string.power_off_text)));
                onView(withText(R.string.power_off_text)).check(matches(isCompletelyDisplayed()));
            }else {
                onView(withId(R.id.powerButtonOn)).check(isAbove(withText(R.string.power_off_text)));
                onView(withText(R.string.power_off_text)).check(matches(isCompletelyDisplayed()));
            }
            //onView(withText(R.string.power_off_text)).check(isAbove(withId(R.id.powerOffCenterImage)));
            //onView(withId(R.id.powerOffCenterImage)).check(matches(allOf(isDisplayed(),isCompletelyDisplayed())));

            //other view should be invisible to end user
            //actual running result shows it is visible to end user
            //!!!CHECK
            //onView(withId(R.id.drawer_layout)).check(matches(isCompletelyDisplayed()));
            //onView(withId(R.id.fragProfile)).check(matches(isCompletelyDisplayed()));
            //onView(withId(R.id.fragGlobalSettings)).check(matches(isCompletelyDisplayed()));
        }
    }


    public static void checkNavigationMainViewDisplayAndStatus(){
        onView(withText(R.string.reset)).check(matches(isCompletelyDisplayed()));
        onView(withText(R.string.tutorial)).check(matches(isCompletelyDisplayed()));

        if (isDaxProductVersion) {
//            onView(withText(R.string.headphone)).check(matches(isCompletelyDisplayed()));
            onView(withText(R.string.explore_dolby)).check(matches(isCompletelyDisplayed()));
        }else {
            onView(withText(R.string.headphone)).check(doesNotExist());
            onView(withText(R.string.explore_dolby)).check(doesNotExist());
        }
    }

    public static void checkNaviHeadphoneView() {
        //high level layout
        onView(withId(R.id.drawer_layout)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.toolbar)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.containerView)).check(matches(isCompletelyDisplayed()));

        //toolbar layout would be re-measure
        onView(withId(R.id.dolbyToolbar)).check(matches(not(isDisplayed())));
        onView(allOf(isAssignableFrom(ImageButton.class),isDisplayed())).
                check(isLeftOf(allOf(withText(R.string.headphone),isDisplayed())));
        onView(allOf(isAssignableFrom(ImageButton.class),isDisplayed())).
                check(isAbove(allOf(withId(R.id.tuningsListView),isDisplayed())));
        onView(allOf(withText(R.string.headphone),isDisplayed())).
                check(isAbove(allOf(withId(R.id.tuningsListView),isDisplayed())));

        // container view would be re-measure
        onView(withId(R.id.tuningsListView)).
                check(matches(isDisplayed()));
        onView(allOf(withParent(allOf(withId(R.id.tuningsListView),isDisplayed())),withText(R.string.default_headphone))).
                check(matches(isDisplayed()));

    }


    public static void checkNaviExploreDolbyView(){
        //high level layout
        onView(withId(R.id.drawer_layout)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.toolbar)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.containerView)).check(matches(isCompletelyDisplayed()));

        //toolbar layout would be re-measure
        onView(withId(R.id.dolbyToolbar)).check(matches(not(isDisplayed())));
        onView(allOf(isAssignableFrom(ImageButton.class),isDisplayed())).
                check(isLeftOf(allOf(withText(R.string.explore_dolby),isDisplayed())));
        onView(allOf(isAssignableFrom(ImageButton.class),isDisplayed())).
                check(isAbove(allOf(withId(R.id.exploreatmoslogo),isDisplayed())));
        onView(allOf(withText(R.string.explore_dolby),isDisplayed())).
                check(isAbove(allOf(withId(R.id.exploreatmoslogo),isDisplayed())));

        // container view would be re-measure
        onView(allOf(withId(R.id.containerView),isDisplayed())).check(noOverlaps());
        onView(allOf(withId(R.id.containerView),isDisplayed())).check(noOverlaps(isAssignableFrom(TextView.class)));
        // check view position
        onView(allOf(withId(R.id.exploreatmoslogo),isDisplayed())).check(isAbove(allOf(withId(R.id.txtupremark),isDisplayed())));
        onView(allOf(withId(R.id.txtupremark),isDisplayed())).check(isAbove(allOf(withId(R.id.btnupgotodolbyatmos),isDisplayed())));
        onView(allOf(withId(R.id.btnupgotodolbyatmos),isDisplayed())).check(isAbove(allOf(withId(R.id.exploredolby_border),isDisplayed())));
        onView(allOf(withId(R.id.exploredolby_border),isDisplayed())).check(isAbove(allOf(withId(R.id.txtdownname),isDisplayed())));
        onView(allOf(withId(R.id.txtdownname),isDisplayed())).check(isAbove(allOf(withId(R.id.txtdownremark),isDisplayed())));
        onView(allOf(withId(R.id.txtdownremark),isDisplayed())).check(isAbove(allOf(withId(R.id.btndowngotoaccess),isDisplayed())));
        // check dolby logo position
        onView(allOf(withId(R.id.txtupdolbyname),isDisplayed())).check(isLeftOf(allOf(withId(R.id.txtupdolbylogomark),isDisplayed())));
    }

    public static void checkFirstTutorialView(){
        // high level layout
        checkHighLevelTutorialView();

        // tutorial view 1 layout and position
        checkFirstTutorialViewPosition();
    }

    public static void checkSecondTutorialView(){
        //high level layout
        checkHighLevelTutorialView();

        // tutorial view 1 layout and position
        checkSecondTutorialViewPosition();
    }

    public static void checkLastTutorialView(){
        if (isTablet){
            checkSecondTutorialView();
        }else{
            //high level layout
            checkHighLevelTutorialView();

            // tutorial view 1 layout and position
            checkThirdTutorialViewPosition();
        }
    }

    private static void checkHighLevelTutorialView(){
        // high level layout
//        onView(withId(R.id.drawer_layout)).check(doesNotExist());
        // drawer_layout_tutorial is in background , espresso have no method to verify the background layout position
//        onView(withId(R.id.drawer_layout_tutorial)).
//                check(matches(allOf(isCompletelyDisplayed(),not(isClickable()))));
        // ... and view page have overlap
//        onView(allOf(withId(R.id.tutorialViewPager),isCompletelyDisplayed())).
//                check(isAbove(allOf(withChild(withId(R.id.dot_1)),isDisplayed())));
        onView(withId(R.id.tutorialViewPager)).
                check(matches(isCompletelyDisplayed()));
        onView(allOf(withId(R.id.dot_1),isDisplayed())).
                check(isCompletelyLeftOf(allOf(withId(R.id.dot_2),isDisplayed())));
//        if (!isTablet) {
//            onView(allOf(withId(R.id.dot_3),isDisplayed())).
//                    check(isRightOf(allOf(withId(R.id.dot_2),isDisplayed())));
//        }
    }

    private static void checkFirstTutorialViewPosition(){
        // check view pager has no overlap
        onView(allOf(withId(R.id.tutorialViewPager),isCompletelyDisplayed())).check(noOverlaps());
        //check sub view layout position
        onView(allOf(withId(R.id.geqText_tutorial),isCompletelyDisplayed(),withText(R.string.graphic_equalizer))).
                check(isCompletelyAbove(allOf(withId(R.id.geqView_tutorial),isCompletelyDisplayed()))).
                check(isCompletelyBelow(allOf(withId(R.id.geq_tooltip),isCompletelyDisplayed(),withText(R.string.tooltip_ge_text))));


        onView(allOf(withId(R.id.ieqText_tutorial),isCompletelyDisplayed(),withText(R.string.ieq_text))).
                check(isCompletelyLeftOf(allOf(withId(R.id.ieqName_tutorial),isCompletelyDisplayed(),withText(R.string.off)))).
                check(isCompletelyAbove(allOf(withId(R.id.equalizerListView_tutorial),isCompletelyDisplayed()))).
                check(isCompletelyAbove(allOf(withId(R.id.equalizerListOff_tutorial),isCompletelyDisplayed()))).
                check(isCompletelyAbove(allOf(withId(R.id.ieq_tooltip),isCompletelyDisplayed(),withText(R.string.tooltip_eq_text))));

        onView(allOf(withId(R.id.ieqName_tutorial),isCompletelyDisplayed(),withText(R.string.off))).
                check(isCompletelyAbove(allOf(withId(R.id.equalizerListView_tutorial),isCompletelyDisplayed()))).
                check(isCompletelyAbove(allOf(withId(R.id.equalizerListOff_tutorial),isCompletelyDisplayed()))).
                check(isCompletelyAbove(allOf(withId(R.id.ieq_tooltip),isCompletelyDisplayed(),withText(R.string.tooltip_eq_text))));

        onView(allOf(withId(R.id.equalizerListView_tutorial),isCompletelyDisplayed())).
                check(isCompletelyLeftOf(allOf(withId(R.id.equalizerListOff_tutorial),isCompletelyDisplayed()))).
                check(isCompletelyAbove(allOf(withId(R.id.ieq_tooltip),isCompletelyDisplayed(),withText(R.string.tooltip_eq_text))));

        onView(allOf(withId(R.id.equalizerListOff_tutorial),isCompletelyDisplayed())).
                check(isCompletelyAbove(allOf(withId(R.id.ieq_tooltip),isCompletelyDisplayed(),withText(R.string.tooltip_eq_text))));
    }

    private static void checkSecondTutorialViewPosition(){
        // check view pager has no overlap
        onView(allOf(withId(R.id.tutorialViewPager),isCompletelyDisplayed())).check(noOverlaps());

        //for dax3 project , check the second tutorial view
        onView(withId(R.id.de_tooltip)).
                check(matches(isCompletelyDisplayed())).
                check(isCompletelyAbove(withId(R.id.deText_tutorial))).
                check(matches(withText(R.string.tooltip_de_text)));

        onView(withId(R.id.deText_tutorial)).
                check(matches(isCompletelyDisplayed())).
                check(isCompletelyAbove(withId(R.id.deButton_tutorial))).
                check(matches(withText(R.string.dialogue_enhancer)));



        //check sub view layout position
//        onView(allOf(withId(R.id.svText_tutorial),isCompletelyDisplayed(),withText(R.string.surround_virtualizer))).
//                check(isAbove(allOf(withId(R.id.svButton_tutorial),isCompletelyDisplayed()))).
//                check(isAbove(allOf(withId(R.id.deButton_tutorial),isCompletelyDisplayed()))).
//                check(isLeftOf(allOf(withId(R.id.deText_tutorial),isCompletelyDisplayed(),withText(R.string.dialogue_enhancer))));
//        onView(allOf(withId(R.id.deText_tutorial),isCompletelyDisplayed(),withText(R.string.dialogue_enhancer))).
//                check(isAbove(allOf(withId(R.id.svButton_tutorial),isCompletelyDisplayed()))).
//                check(isAbove(allOf(withId(R.id.deButton_tutorial),isCompletelyDisplayed())));
//        onView(allOf(withId(R.id.svButton_tutorial),isCompletelyDisplayed())).
//                check(isLeftOf(allOf(withId(R.id.deButton_tutorial),isCompletelyDisplayed()))).
//                check(isAbove(allOf(withId(R.id.sv_tooltip),isDisplayed(),withText(R.string.tooltip_sv_text))));
//        onView(allOf(withId(R.id.deButton_tutorial),isCompletelyDisplayed())).
//                check(isAbove(allOf(withId(R.id.sv_tooltip),isCompletelyDisplayed(),withText(R.string.tooltip_sv_text))));
//        onView(allOf(withId(R.id.sv_tooltip),isCompletelyDisplayed(),withText(R.string.tooltip_sv_text))).
//                check(isAbove(allOf(withId(R.id.de_tooltip),isCompletelyDisplayed(),withText(R.string.tooltip_de_text))));
//        // for ds1 or dax2 lite version , be feature is invisible
//        if (!isDaxProductVersion){
//            onView(withId(R.id.beView)).check(matches(withEffectiveVisibility(INVISIBLE)));
//            onView(withId(R.id.beText)).check(matches(withEffectiveVisibility(INVISIBLE)));
//            onView(withId(R.id.beButton)).check(matches(withEffectiveVisibility(INVISIBLE)));
//        }
//
//        // for tablet or phone device , layout is diff
//        if (isTablet){
//            checkThirdTutorialViewPosition();
//            checkLastTwoTutorialViewPosition();
//        }
    }

    private static void checkThirdTutorialViewPosition(){
//        // check view pager has no overlap
//        onView(allOf(withId(R.id.tutorialViewPager),isCompletelyDisplayed())).check(noOverlaps());
//        //check sub view layout position
//        if (isDaxProductVersion){
//            onView(allOf(withId(R.id.be_tooltip),isCompletelyDisplayed(),withText(R.string.tooltip_be_text))).
//                    check(isAbove(allOf(withId(R.id.vl_tooltip),isCompletelyDisplayed(),withText(R.string.tooltip_vl_text))));
//            onView(allOf(withId(R.id.beText_tutorial),isCompletelyDisplayed(),withText(R.string.bass_enhancer))).
//                    check(isBelow(allOf(withId(R.id.vl_tooltip),isCompletelyDisplayed(),withText(R.string.tooltip_vl_text)))).
//                    check(isRightOf(allOf(withId(R.id.vlText_tutorial),isCompletelyDisplayed(),withText(R.string.volume_leveler)))).
//                    check(isRightOf(allOf(withId(R.id.vlButton_tutorial),isCompletelyDisplayed()))).
//                    check(isAbove(allOf(withId(R.id.beButton_tutorial),isCompletelyDisplayed())));
//            onView(allOf(withId(R.id.beButton_tutorial),isCompletelyDisplayed())).
//                    check(isRightOf(allOf(withId(R.id.vlText_tutorial),isCompletelyDisplayed(),withText(R.string.volume_leveler)))).
//                    check(isRightOf(allOf(withId(R.id.vlButton_tutorial),isCompletelyDisplayed())));
//        }
//
//        onView(allOf(withId(R.id.vl_tooltip),isCompletelyDisplayed(),withText(R.string.tooltip_vl_text))).
//                check(isAbove(allOf(withId(R.id.vlText_tutorial),isCompletelyDisplayed(),withText(R.string.volume_leveler))));
//        onView(allOf(withId(R.id.vlText_tutorial),isCompletelyDisplayed(),withText(R.string.volume_leveler))).
//                check(isAbove(allOf(withId(R.id.vlButton_tutorial),isCompletelyDisplayed())));
    }

    private static void checkLastTwoTutorialViewPosition(){
//        if (isDaxProductVersion){
//            onView(allOf(withId(R.id.de_tooltip),isCompletelyDisplayed(),withText(R.string.tooltip_de_text))).
//                    check(isAbove(allOf(withId(R.id.be_tooltip),isCompletelyDisplayed(),withText(R.string.tooltip_be_text))));
//        }
//        onView(allOf(withId(R.id.de_tooltip),isCompletelyDisplayed(),withText(R.string.tooltip_de_text))).
//                check(isAbove(allOf(withId(R.id.vl_tooltip),isCompletelyDisplayed(),withText(R.string.tooltip_vl_text))));
    }


    public static void checkNaviResetView() {
        //high level layout
        onView(withId(R.id.drawer_layout)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.toolbar)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.containerView)).check(matches(isCompletelyDisplayed()));

        //toolbar layout would be re-measure
        onView(withId(R.id.dolbyToolbar)).check(matches(not(isDisplayed())));
        onView(allOf(isAssignableFrom(ImageButton.class),isDisplayed())).
                check(isCompletelyLeftOf(allOf(withText(R.string.reset),isDisplayed())));
        onView(allOf(isAssignableFrom(ImageButton.class),isDisplayed())).
                check(isCompletelyAbove(allOf(withId(R.id.resetView),isDisplayed())));
        onView(allOf(withText(R.string.reset),isDisplayed())).
                check(isCompletelyAbove(allOf(withId(R.id.resetView),isDisplayed())));

        // container view would be re-measure
        onView(withId(R.id.resetView)).check(matches(isCompletelyDisplayed())).check(noOverlaps());

        // position
        onView(allOf(withId(R.id.resetText),isCompletelyDisplayed())).
                check(matches(withText(R.string.reset_text))).
                check(isCompletelyAbove(allOf(withId(R.id.resetProfiles),isCompletelyDisplayed()))).
                check(isCompletelyAbove(allOf(withId(R.id.resetButtonView),isDisplayed())));

        onView(allOf(withId(R.id.resetProfiles),isCompletelyDisplayed())).
                check(matches(withText(R.string.reset_profiles)));
    }

}
