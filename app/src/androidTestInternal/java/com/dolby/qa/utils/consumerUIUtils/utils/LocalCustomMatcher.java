package com.dolby.qa.utils.consumerUIUtils.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.util.HumanReadables;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Locale;

import static android.support.test.espresso.action.ViewActions.actionWithAssertions;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static com.dolby.qa.utils.Constants.DAX_DEA_MAX_VALUE;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Created on 7/26/2016.
 */
public class LocalCustomMatcher {
    public static final String DEB_TAG = LocalCustomMatcher.class.getSimpleName();

    static Matcher<View> withSwitchText(final String substring) {
        return withSwitchText(is(substring));
    }

    static Matcher<View> withSwitchText(final Matcher<String> stringMatcher){
        //checkNotNull(stringMatcher);
        return new BoundedMatcher<View, Switch>(Switch.class) {

            @Override
            public boolean matchesSafely(Switch view){
                boolean ischeck=view.isChecked();
                final CharSequence textOff=view.getTextOff();
                final CharSequence texton=view.getTextOn();
                if(ischeck){
                    return (texton != null)&& (stringMatcher.matches(texton.toString()));
                }
                else{
                    return (textOff != null)&& (stringMatcher.matches(textOff.toString()) );
                }

            }

            @Override
            public void describeTo(Description description){
                description.appendText("with swithch off state text: ");
                stringMatcher.describeTo(description);
            }
        };
    }

    static Matcher<Object> backgroundShouldHaveColor(int expectedColor){
        return viewShouldHaveBackgroundColor(equalTo(expectedColor));
    }

    static Matcher<Object> viewShouldHaveBackgroundColor(final Matcher<Integer> expectedObject){
        final int[] color=new int[1];
        return new BoundedMatcher<Object, LinearLayout>(LinearLayout.class) {

            @Override
            public boolean matchesSafely(final LinearLayout vg){
                color[0]=Color.TRANSPARENT;//Color.CYAN;
                Drawable background=vg.getBackground();
                if (background instanceof ColorDrawable){
                    color[0]=((ColorDrawable)background).getColor();
                    Log.d("debug-tag","background color 1 is +++++++++++++++"+color[0]);
                }
                Log.d("debug-tag","background color 2 is "+color[0]);
                if(expectedObject.matches(color[0])){
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void describeTo(Description description){
                description.appendText("Color did not match "+color[0]);
            }
        };
    }

    // customize matcher to find view by position in parent view
    public static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position)
    {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
                }
            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                            && view.equals(((ViewGroup) parent).getChildAt(position));
                }
            };
    }

    public static Matcher<View> withBrother(final Matcher<View> brotherMatcher) {
        //checkNotNull(childMatcher);
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with brother: ");
                brotherMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return false;
                }
                ViewGroup group = (ViewGroup) view.getParent();
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (brotherMatcher.matches(group.getChildAt(i))) {
                        return true;
                    }
                }
                return false;
            }
        };
    }


    static ViewAction setTextInTextView(final String value){
        return new ViewAction() {
            @SuppressWarnings("unchecked")
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(),isAssignableFrom(TextView.class));
            }

            @Override
            public String getDescription() {
                return "Replace Text View ";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((TextView) view).setText(value);
            }
        };
    }

    static ViewAction clearTextViewText() {
        return new ViewAction() {
            @SuppressWarnings("unchecked")
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(),isAssignableFrom(TextView.class));
            }

            @Override
            public String getDescription() {
                return "Clear Text View ";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((TextView) view).setText("");
            }
        };
    }

    public static ViewAction setProgress(final int position){
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(SeekBar.class);
            }

            @Override
            public String getDescription() {
                return "Set a progress in a SeekBar!";
            }

            @Override
            public void perform(UiController uiController, View view) {
                SeekBar mLocalSeekBar = (SeekBar)view;
                mLocalSeekBar.setProgress(position);
            }
        };
    }

    public static Matcher<View> withProgress(final int expectedProgress){
        return new BoundedMatcher<View,SeekBar>(SeekBar.class){
            @Override
            protected boolean matchesSafely(SeekBar item) {
                Log.d(DEB_TAG, "assert seek bar progress is expected :" +
                        expectedProgress+":"+item.getProgress());
                return item.getProgress() == expectedProgress;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("SeekBar expected progress : ");
                description.appendText(" " + expectedProgress);
            }
        };
    }

    public static ViewAction scrubSeekBarAction(int progress) {
        return actionWithAssertions(new CustomSwipeAction(
                Swipe.FAST,
                new SeekBarThumbCoordinatesProvider(-1),
                new SeekBarThumbCoordinatesProvider(progress),
                Press.PINPOINT

        ));
    }


    private static class SeekBarThumbCoordinatesProvider implements CoordinatesProvider {
        int mProgress;

        public SeekBarThumbCoordinatesProvider(int progress) {
            mProgress = progress;
        }

        private static float[] getVisibleLeftTop(View view) {
            final int[] xy = new int[2];
            view.getLocationOnScreen(xy);
            return new float[]{(float) xy[0], (float) xy[1]};
        }
        @Override
        public float[] calculateCoordinates(View view) {
            if (!(view instanceof SeekBar)) {
                throw new PerformException.Builder()
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new RuntimeException(String.format("SeekBar expected"))).build();
                }

            SeekBar seekBar = (SeekBar) view;
            int width = seekBar.getWidth() - seekBar.getPaddingLeft() - seekBar.getPaddingRight();
            String mSeekBarPosition = String.format(
                    Locale.getDefault(),
                    "the seek bar position: width=%d; paddingleft=%d; paddingright=%d",
                    seekBar.getWidth(),seekBar.getPaddingLeft(),seekBar.getPaddingRight());
            Log.d(DEB_TAG,mSeekBarPosition);
            double progress = 0 ;
            float xPosition = 0 ;
            if (mProgress == -1){
                progress = seekBar.getProgress();
                xPosition =
                        (seekBar.getPaddingLeft() + (float)(width * progress / seekBar.getMax()));
                Log.d(DEB_TAG,"seek bar start position :"+xPosition);
            }else {
                progress = mProgress;
                xPosition =
                        (seekBar.getPaddingLeft() + (float)(width * progress / seekBar.getMax()));
                Log.d(DEB_TAG,"seek bar end position :"+xPosition);
            }
            float[] xy = getVisibleLeftTop(seekBar);
            Log.d(DEB_TAG,"seek bar left top position :"+xy[0]+":"+xy[1]);
            // !!!!!!!! please pay attention to the value : -0.002f
            if (DAX_DEA_MAX_VALUE == seekBar.getProgress()){
                xPosition += (-0.02f*seekBar.getWidth());
            }
            return new float[]{xy[0] + xPosition, xy[1]};
        }
     }
}
