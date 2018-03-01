package com.aftarobot.insurancecompany;


import android.content.Intent;

import com.aftarobot.insurancecompany.activities.ClaimsActivity;
import com.aftarobot.insurancecompany.activities.ClientsActivity;
import com.aftarobot.insurancecompany.activities.CompanyNavActivity;
import com.aftarobot.insurancecompany.activities.HistorianActivity;
import com.aftarobot.insurancecompany.activities.PolicyActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class CompanyNavActivityTest {
    CompanyNavActivity activity;
    @Before
    public void setup() {
        activity = Robolectric.setupActivity(CompanyNavActivity.class);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey("A...s")
                .setApplicationId("1:10...7c527e8")
                .setDatabaseUrl("https://myapp.firebaseio.com/")
                .build();
        FirebaseApp testApp = FirebaseApp.initializeApp(activity.getApplicationContext(), options, "test app");
        FirebaseDatabase testDatabase = FirebaseDatabase.getInstance(testApp);
        DatabaseReference firebaseReference = testDatabase.getReference();
    }

    @Test
    public void testPolicyClick() {
        activity.findViewById(R.id.layout1).performClick();

        Intent expectedIntent = new Intent(activity, PolicyActivity.class);
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }
    @Test
    public void testClaimsClick() {
        FirebaseApp.initializeApp(activity.getApplicationContext());
        activity.findViewById(R.id.layout3).performClick();

        Intent expectedIntent = new Intent(activity, ClaimsActivity.class);
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }
    @Test
    public void testClientsClick() {
        activity.findViewById(R.id.layout2).performClick();

        Intent expectedIntent = new Intent(activity, ClientsActivity.class);
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }
    @Test
    public void testTransactionsMenuClick() {
        activity.findViewById(R.id.nav_trans).performClick();

        Intent expectedIntent = new Intent(activity, HistorianActivity.class);
        Intent actual = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }
}
