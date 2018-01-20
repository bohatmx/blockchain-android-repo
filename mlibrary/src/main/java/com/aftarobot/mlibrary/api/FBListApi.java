package com.aftarobot.mlibrary.api;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by aubreymalabie on 1/20/18.
 */

public class FBListApi {
    private FirebaseDatabase db;

    public FBListApi() {
        db = FirebaseDatabase.getInstance();
    }
}
