package com.qc.facialexpressionrecognition;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.qc.facialexpressionrecognition.Activity.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class CameraPreviewTest {

    private static final String TAG = CameraPreviewTest.class.getSimpleName();
    private Context mContext;

    @Rule
    public ActivityTestRule<MainActivity> mCameraActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        mContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void Android_UT_test_cameraPreview() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.texture)).check(matches(withId(R.id.texture)));
    }

}
