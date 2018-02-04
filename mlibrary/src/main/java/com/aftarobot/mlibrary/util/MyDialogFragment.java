package com.aftarobot.mlibrary.util;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aftarobot.mlibrary.R;
import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.Data;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.Policy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class MyDialogFragment extends DialogFragment {
    private View view;
    private TextView txtType, txtMessage;
    private Button btnClose;
    
    public interface Listener {
        void onCloseButtonClicked();
    }
    private Listener listener;
    private Data data;
    public MyDialogFragment() {} // required

    // Create fragment with call parameters
    public static MyDialogFragment newInstance() {
        MyDialogFragment fragment = new MyDialogFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment, container, false);
        txtMessage = view.findViewById(R.id.txtMessage);
        txtType = view.findViewById(R.id.txtType);
        btnClose = view.findViewById(R.id.btnClose);
        getDialog().setTitle("Business Network Messages");

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCloseButtonClicked();
            }
        });
        setMessage();
        return view;
    }


    public void setMessage() {
        if (data instanceof DeathCertificate) {
            DeathCertificate dc = (DeathCertificate)data;
            txtType.setText("Death Certificate");
            txtMessage.setText(GSON.toJson(dc));
        }
        if (data instanceof Burial) {
            Burial burial = (Burial) data;
            txtType.setText("Burial Registration");
            txtMessage.setText(GSON.toJson(burial));
        }
        if (data instanceof Claim) {
            Claim claim = (Claim) data;
            txtType.setText("Claim Notification");
            txtMessage.setText(GSON.toJson(claim));
        }
        if (data instanceof Policy) {
            Policy policy = (Policy) data;
            txtType.setText("Policy Issuance");
            txtMessage.setText(GSON.toJson(policy));
        }
    }
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
