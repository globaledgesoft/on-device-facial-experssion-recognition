package com.qc.facialexpressionrecognition;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.qc.facialexpressionrecognition.Activity.MainActivity;
import com.qc.facialexpressionrecognition.Utils.Util;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RunTimePermissionsTest {
    private Context instrumentationCtx;
    MainActivity mainActivity;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        instrumentationCtx = InstrumentationRegistry.getTargetContext();
        mainActivity = mActivityTestRule.getActivity();
    }

    /**
     * Test case to check if camera permission is granted or not
     */
    @Test
    public void Android_UT_testHasPermission() {
        boolean hasPermission;
        hasPermission = Util.hasPermission(instrumentationCtx);
        if (hasPermission)
            assertTrue(hasPermission);
        else assertFalse(hasPermission);
    }

}
