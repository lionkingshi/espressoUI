package com.dolby.qa.interactiontest;

import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;

import com.dolby.qa.utils.SettingsSoundUIUtils.SettingsSoundUIWrapper;
import com.dolby.qa.utils.customAnnotation.InteractiveTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class SettingsSoundUIFunctionTest extends SettingsSoundUIWrapper {
    @Before
    public void setUp(){
        testCaseSetup();
    }

    @After
    public void tearDown(){
        testCaseTeardown();
    }

    @Test
    @InteractiveTest
    public void SettingsSoundUIFunctionTest_launchConsumerUI(){
        settingsSoundDolbyUILaunchConsumerUICorrectly();
    }
}