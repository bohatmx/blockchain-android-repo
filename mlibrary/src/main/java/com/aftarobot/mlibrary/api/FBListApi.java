package com.aftarobot.mlibrary.api;

import android.util.Log;

import com.aftarobot.mlibrary.data.UserDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 1/20/18.
 */

public class FBListApi {
    private FirebaseDatabase db;

    public FBListApi() {
        db = FirebaseDatabase.getInstance();
    }
    public interface UserListener {
        void onResponse(List<UserDTO> users);
        void onError(String message);
    }
    public void getUserByEmail(String email, final UserListener listener) {
        DatabaseReference ref = db.getReference(FBApi.USERS);
        Query query = ref.orderByChild("email").equalTo(email).limitToFirst(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: dataSnapshot:\n" + dataSnapshot);
                List<UserDTO> users = new ArrayList<>();
                for (DataSnapshot shot: dataSnapshot.getChildren()) {
                    UserDTO u = shot.getValue(UserDTO.class);
                    u.setUserID(shot.getKey());
                    users.add(u);
                }

                listener.onResponse(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    public static final String TAG = FBListApi.class.getSimpleName();
}
