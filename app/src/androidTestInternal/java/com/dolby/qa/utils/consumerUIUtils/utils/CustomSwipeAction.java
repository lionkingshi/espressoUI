package com.dolby.qa.utils.consumerUIUtils.utils;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.PrecisionDescriber;
import android.support.test.espresso.action.Swiper;
import android.support.test.espresso.util.HumanReadables;
import android.view.View;
import android.view.ViewConfiguration;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;

//import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
//
//import android.view.View;
//import android.view.ViewConfiguration;
//import android.support.test.espresso.PerformException;
//import android.support.test.espresso.UiController;
//import android.support.test.espresso.ViewAction;
//import android.support.test.espresso.util.HumanReadables;
//import org.hamcrest.Matcher;

/**
 * Enables swiping across a view.
 */
public final class CustomSwipeAction implements ViewAction {

    /** Maximum number of times to attempt sending a swipe action. */
    private static final int MAX_TRIES = 3;

    /** The minimum amount of a view that must be displayed in order to swipe across it. */
    private static final int VIEW_DISPLAY_PERCENTAGE = 50;

    final CoordinatesProvider startCoordinatesProvider;
    final CoordinatesProvider endCoordinatesProvider;
    final Swiper swiper;
    final PrecisionDescriber precisionDescriber;

    public CustomSwipeAction(Swiper swiper, CoordinatesProvider startCoordinatesProvider,
                              CoordinatesProvider endCoordinatesProvider, PrecisionDescriber precisionDescriber) {
        this.swiper = swiper;
        this.startCoordinatesProvider = startCoordinatesProvider;
        this.endCoordinatesProvider = endCoordinatesProvider;
        this.precisionDescriber = precisionDescriber;
    }

    @Override
    public Matcher<View> getConstraints() {
        return isDisplayingAtLeast(VIEW_DISPLAY_PERCENTAGE);
    }

    @Override
    public void perform(UiController uiController, View view) {
        float[] startCoordinates = startCoordinatesProvider.calculateCoordinates(view);
        float[] endCoordinates = endCoordinatesProvider.calculateCoordinates(view);
        float[] precision = precisionDescriber.describePrecision();

        Swiper.Status status = Swiper.Status.FAILURE;

        for (int tries = 0; tries < MAX_TRIES && status != Swiper.Status.SUCCESS; tries++) {
            try {
                status = swiper.sendSwipe(uiController, startCoordinates, endCoordinates, precision);
            } catch (RuntimeException re) {
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(re)
                        .build();
            }

            int duration = ViewConfiguration.getPressedStateDuration();
            // ensures that all work enqueued to process the swipe has been run.
            if (duration > 0) {
                uiController.loopMainThreadForAtLeast(duration);
            }
        }

        if (status == Swiper.Status.FAILURE) {
            throw new PerformException.Builder()
                    .withActionDescription(getDescription())
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(new RuntimeException(String.format(
                            "Couldn't swipe from: %s,%s to: %s,%s precision: %s, %s . Swiper: %s "
                                    + "start coordinate provider: %s precision describer: %s. Tried %s times",
                            startCoordinates[0],
                            startCoordinates[1],
                            endCoordinates[0],
                            endCoordinates[1],
                            precision[0],
                            precision[1],
                            swiper,
                            startCoordinatesProvider,
                            precisionDescriber,
                            MAX_TRIES)))
                    .build();
        }
    }

    @Override
    public String getDescription() {
        return swiper.toString().toLowerCase() + " swipe";
    }
}

