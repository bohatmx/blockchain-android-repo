package com.aftarobot.mlibrary.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.BeneficiaryClaimMessageDTO;
import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.DeathCertificateRequest;
import com.aftarobot.mlibrary.data.Policy;
import com.aftarobot.mlibrary.data.UserDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
            DEATH_CERT_REQUEST = "deathCertRequests",
            CLAIMS = "claims",
            BENEFICIARIES = "beneficiaries",
            BENNIE_CLAIM_MESSAGES = "beneficiaryClaimMessages",
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

    public void addBeneficiary(final Beneficiary beneficiary, final FBListener listener) {
        DatabaseReference ref = db.getReference(BENEFICIARIES);
        ref.push().setValue(beneficiary, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.i(TAG, "onComplete: beneficiary added to firebase: ".concat(databaseReference.getKey()));
                    listener.onResponse(beneficiary);
                } else {
                    Log.e(TAG, "onComplete: ERROR: ".concat(databaseError.getMessage()));
                    listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public void updateBeneficiaryFCMToken(final Beneficiary beneficiary, final FBListener listener) {
        DatabaseReference ref = db.getReference(BENEFICIARIES)
                .child(beneficiary.getBeneficiaryId()).child("fcmToken");
        ref.setValue(beneficiary.getFcmToken())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "onSuccess: Beneficiary token updated");
                        listener.onResponse(beneficiary);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onComplete: ERROR: ".concat(e.getMessage()));
                        listener.onError(e.getMessage());
                    }
                });

    }
    public void removeBeneficiaries(final FBListener listener) {
        DatabaseReference ref = db.getReference(BENEFICIARIES);
        ref.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "onSuccess: Beneficiary token updated");
                        listener.onResponse(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onComplete: ERROR: ".concat(e.getMessage()));
                        listener.onError(e.getMessage());
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

    public void addDeathCertRequest(final DeathCertificateRequest request, final FBListener listener) {
        DatabaseReference ref = db.getReference(DEATH_CERT_REQUEST);

        ref.push().setValue(request, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.i(TAG, "onComplete: request added to firebase: "
                            .concat(request.getIdNumber()).concat(" cause:").concat(request.getCauseOfDeath()));
                    listener.onResponse(request);
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

    public void addBeneficiaryClaimMessage(final BeneficiaryClaimMessageDTO message, final FBListener listener) {
        DatabaseReference ref = db.getReference(BENNIE_CLAIM_MESSAGES);

        ref.push().setValue(message, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.i(TAG, "onComplete: message added to firebase: "
                            .concat(message.getClaim().getClaimId()).concat(" policy:").concat(message.getClaim().getPolicy()));
                    listener.onResponse(message);
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
                    Log.e(TAG, "onComplete: user deleted: ".concat(user.getEmail()));
                } else {
                    Log.e(TAG, "onComplete: ".concat(databaseError.getMessage()));
                }
            }
        });
    }
}
