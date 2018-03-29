package com.dolby.qa.daxuitest;

import com.dolby.qa.interactiontest.UIInteractiveFunctionTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({DaxConsumerUITest.class, UIInteractiveFunctionTest.class})
public class AllDaxTest {}
