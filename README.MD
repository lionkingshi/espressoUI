# espressoUI
custom UI Espresso Automation architecture and private viewAction and viewMatcher 
# UI Automator and Espresso
Current Android project provide [UI Automation][] and [Espresso][] framework for UI test 
while the former focus on one single app behaviour and 
the latter focus on cross-app functional UI testing across system and installed apps.

Meanwhile Espresso test framework provided efficient and extensible method to search view and perform operation 
just overrided your custom ViewMatcher and ViewAction . 
Below code is an example to show how to swipe seekbar to expected progress and assert the progress is expected :

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
            double progress = 0 ;
            float xPosition = 0 ;
            if (mProgress == -1){
                progress = seekBar.getProgress();
                xPosition =
                        (seekBar.getPaddingLeft() + (float)(width * progress / seekBar.getMax()));
            }else {
                progress = mProgress;
                xPosition =
                        (seekBar.getPaddingLeft() + (float)(width * progress / seekBar.getMax()));           
            }
            float[] xy = getVisibleLeftTop(seekBar);
            // !!!!!!!! please pay attention to the value : -0.002f
            if (DAX_DEA_MAX_VALUE == seekBar.getProgress()){
                xPosition += (-0.02f*seekBar.getWidth());
            }
            return new float[]{xy[0] + xPosition, xy[1]};
        }
     }
  
[UI Automation]: https://developer.android.com/training/testing/ui-automator.html
[Espresso]: https://developer.android.com/training/testing/espresso/index.html

# Test Code Layer
It's necessary to at least seperate user interaction logic from detailed view operations. 
So the fisrt thing is to define all atmotic view actions and view assertions , 
then wrapped them to provide a interface for high level use . 
The last thing is to write every test case steps one by one using wrapped method . 

![test code layer](test_code_layer.png)

# parallel running 
To run same test cases on multiple configuration device simultaneously , [Spoon][] is used. Once all tests have completed , a static HTML summary is generated with detailed information about each device and test . 
All we need do is to add spoon client library to your module's 'build.gradle' : 

'''gradle
    // add spoon library
    androidTestCompile 'com.squareup.spoon:spoon-client:1.7.1'
'''

[Spoon]: https://github.com/square/spoon
