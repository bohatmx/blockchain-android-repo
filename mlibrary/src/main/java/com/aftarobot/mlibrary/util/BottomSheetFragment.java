package com.aftarobot.mlibrary.util;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aftarobot.mlibrary.R;
import com.aftarobot.mlibrary.data.Burial;
import com.aftarobot.mlibrary.data.Claim;
import com.aftarobot.mlibrary.data.DeathCertificate;
import com.aftarobot.mlibrary.data.Policy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class BottomSheetFragment extends BottomSheetDialogFragment {
    public BottomSheetFragment() {
        // Required empty public constructor
    }
    TextView botTitle, botId, botMsg;
    View view;
    DeathCertificate deathCertificate;
    Burial burial;
    Claim claim;
    Policy policy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.bottom_sheet, container, false);
        botId = view.findViewById(com.aftarobot.mlibrary.R.id.txtId);
        botTitle = view.findViewById(com.aftarobot.mlibrary.R.id.txtTitle);
        botMsg = view.findViewById(com.aftarobot.mlibrary.R.id.txtMessage);

        setMessage();

        return view;
    }

    private void setMessage() {
        if (deathCertificate != null) {
            botTitle.setText("Certificate Issued");
            botId.setText(deathCertificate.getIdNumber());
            botMsg.setText(GSON.toJson(deathCertificate));
        }
        if (claim != null) {
            botTitle.setText("Claim Issued");
            botId.setText(claim.getClaimId());
            botMsg.setText(GSON.toJson(claim));
        }
        if (burial != null) {
            botTitle.setText("Burial Registered");
            botId.setText(burial.getIdNumber());
            botMsg.setText(GSON.toJson(burial));
        }
        if (policy != null) {
            botTitle.setText("Policy Issued");
            botId.setText(policy.getPolicyNumber());
            botMsg.setText(GSON.toJson(policy));
        }
    }

    public void setDeathCertificate(DeathCertificate deathCertificate) {
        this.deathCertificate = deathCertificate;
        if (view != null) {
            setMessage();
        }
    }

    public void setBurial(Burial burial) {
        this.burial = burial;
        if (view != null) {
            setMessage();
        }
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
        if (view != null) {
            setMessage();
        }
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
        if (view != null) {
            setMessage();
        }
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
