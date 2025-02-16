package com.aftarobot.mlibrary.api;

import android.util.Log;

import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Client;
import com.aftarobot.mlibrary.data.UserDTO;
import com.aftarobot.mlibrary.data.Wallet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public interface BeneficiaryListener {
        void onResponse(List<Beneficiary> beneficiaries);

        void onError(String message);
    }

    public interface ClientListener {
        void onResponse(List<Client> clients);

        void onError(String message);
    }

    public interface WalletListener {
        void onResponse(List<Wallet> clients);

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
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
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

    public void getUserByIDnumber(String idNumber, final UserListener listener) {
        DatabaseReference ref = db.getReference(FBApi.USERS);
        Query query = ref.orderByChild("idNumber").equalTo(idNumber).limitToFirst(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: dataSnapshot:\n" + dataSnapshot);
                List<UserDTO> users = new ArrayList<>();
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
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

    public void getBeneficiaryByIDnumber(String idNumber, final BeneficiaryListener listener) {
        DatabaseReference ref = db.getReference(FBApi.BENEFICIARIES);
        Query query = ref.orderByChild("idNumber").equalTo(idNumber).limitToFirst(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: dataSnapshot:\n" + dataSnapshot);
                List<Beneficiary> beneficiaries = new ArrayList<>();
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    Beneficiary u = shot.getValue(Beneficiary.class);
                    beneficiaries.add(u);
                }

                listener.onResponse(beneficiaries);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    public void getWallets(final WalletListener listener) {
        DatabaseReference ref = db.getReference(FBApi.WALLETS);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "getWallets onDataChange: dataSnapshot:\n" + dataSnapshot.getChildrenCount());
                List<Wallet> wallets = new ArrayList<>();
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    Wallet u = shot.getValue(Wallet.class);
                    Objects.requireNonNull(u).setWalletID(shot.getKey());
                    wallets.add(u);
                }

                listener.onResponse(wallets);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    public void getWallet(String walletID, final WalletListener listener) {
        DatabaseReference ref = db.getReference(FBApi.WALLETS)
                .child(walletID);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "getWallet onDataChange: dataSnapshot:\n" + dataSnapshot);
                List<Wallet> wallets = new ArrayList<>();
                Wallet u = dataSnapshot.getValue(Wallet.class);
                Objects.requireNonNull(u).setWalletID(dataSnapshot.getKey());
                wallets.add(u);
                Log.w(TAG, "onDataChange: getWallet: ".concat(GSON.toJson(u)) );
                listener.onResponse(wallets);
        }

        @Override
        public void onCancelled (DatabaseError databaseError){
            listener.onError(databaseError.getMessage());
        }
    });
}
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void getBeneficiaries(final BeneficiaryListener listener) {
        DatabaseReference ref = db.getReference(FBApi.BENEFICIARIES);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "getBeneficiaries onDataChange: dataSnapshot:\n" + dataSnapshot.getChildrenCount());
                List<Beneficiary> beneficiaries = new ArrayList<>();
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    Beneficiary u = shot.getValue(Beneficiary.class);
                    u.setBeneficiaryId(shot.getKey());
                    beneficiaries.add(u);
                }

                listener.onResponse(beneficiaries);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    public void getClients(final ClientListener listener) {
        DatabaseReference ref = db.getReference(FBApi.CLIENTS);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "getClients onDataChange: dataSnapshot:\n" + dataSnapshot.getChildrenCount());
                List<Client> clients = new ArrayList<>();
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    Client u = shot.getValue(Client.class);
                    u.setClientId(shot.getKey());
                    clients.add(u);
                }

                listener.onResponse(clients);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }


    public static final String TAG = FBListApi.class.getSimpleName();
}
