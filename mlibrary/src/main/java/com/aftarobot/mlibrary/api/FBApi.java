package com.aftarobot.mlibrary.api;

import android.util.Log;

import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.data.UserDTO;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by aubreymalabie on 1/20/18.
 */

public class FBApi {
    public static final String TAG = FBApi.class.getSimpleName();
    private FirebaseDatabase db;

    public FBApi() {
        db = FirebaseDatabase.getInstance();
    }

    public static final String
            USERS = "users",
            DEATH_CERTS = "certificates",
            CLAIMS = "claims",
            POLICIES = "policies",
            BURIALS = "burials";

    public interface FBListener {
        void onResponse(Data data);

        void onError(String message);
    }

    public void addUser(final UserDTO user, final FBListener listener) {
        DatabaseReference ref = db.getReference(USERS);

        ref.push().setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.i(TAG, "onComplete: user added to firebase: "
                            .concat(user.getEmail()).concat(" type:").concat(user.getUserType()));
                    listener.onResponse(user);
                } else {
                    Log.e(TAG, "onComplete: ERROR: ".concat(databaseError.getMessage()));
                    listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public void addPolicy(final Policy policy, final FBListener listener) {
        DatabaseReference ref = db.getReference(POLICIES);

        ref.push().setValue(policy, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.i(TAG, "onComplete: policy added to firebase: "
                            .concat(policy.getPolicyNumber()).concat(" cause:").concat(policy.getInsuranceCompany()));
                    listener.onResponse(policy);
                } else {
                    Log.e(TAG, "onComplete: ERROR: ".concat(databaseError.getMessage()));
                    listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public void addDeathCert(final DeathCertificate certificate, final FBListener listener) {
        DatabaseReference ref = db.getReference(DEATH_CERTS);

        ref.push().setValue(certificate, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.i(TAG, "onComplete: certificate added to firebase: "
                            .concat(certificate.getIdNumber()).concat(" cause:").concat(certificate.getCauseOfDeath()));
                    listener.onResponse(certificate);
                } else {
                    Log.e(TAG, "onComplete: ERROR: ".concat(databaseError.getMessage()));
                    listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public void addBurial(final Burial burial, final FBListener listener) {
        DatabaseReference ref = db.getReference(BURIALS);

        ref.push().setValue(burial, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.i(TAG, "onComplete: burial added to firebase: "
                            .concat(burial.getIdNumber()).concat(" parlour:").concat(burial.getFuneralParlour()));
                    listener.onResponse(burial);
                } else {
                    Log.e(TAG, "onComplete: ERROR: ".concat(databaseError.getMessage()));
                    listener.onError(databaseError.getMessage());
                }
            }
        });
    }
    public void addClaim(final Claim claim, final FBListener listener) {
        DatabaseReference ref = db.getReference(CLAIMS);

        ref.push().setValue(claim, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.i(TAG, "onComplete: claim added to firebase: "
                            .concat(claim.getClaimId()).concat(" policy:").concat(claim.getPolicy()));
                    listener.onResponse(claim);
                } else {
                    Log.e(TAG, "onComplete: ERROR: ".concat(databaseError.getMessage()));
                    listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public void deleteUser(final UserDTO user) {
        DatabaseReference w = db.getReference(FBApi.USERS)
                .child(user.getUserID());
        w.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.e(TAG, "onComplete: user deleted: ".concat(user.getEmail()) );
                } else {
                    Log.e(TAG, "onComplete: ".concat(databaseError.getMessage()) );
                }
            }
        });
    }
}
