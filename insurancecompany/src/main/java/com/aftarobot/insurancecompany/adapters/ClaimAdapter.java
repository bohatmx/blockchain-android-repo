package com.aftarobot.insurancecompany.adapters;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.aftarobot.insurancecompany.R;
import com.aftarobot.mlibrary.data.Claim;

import java.util.List;


public class ClaimAdapter extends RecyclerView.Adapter<ClaimAdapter.ClaimViewHolder> {

    public interface ClaimListener {
        void onClaimTapped(Claim claim);

    }

    private ClaimListener mListener;
    private List<Claim> claims;


    public ClaimAdapter(List<Claim> claims, ClaimListener listener) {
        this.claims = claims;
        this.mListener = listener;
    }
    

    @Override
    public ClaimViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.claim_item, parent, false);
        return new ClaimViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ClaimViewHolder holder, final int position) {

        final Claim p = claims.get(position);
        holder.claimId.setText(p.getClaimId());
        holder.date.setText(p.getDateTime());
        holder.policyNumber.setText(p.getPolicyNumber());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClaimTapped(p);
            }
        });

    }

    private void animateIn(View view) {
        view.setVisibility(View.VISIBLE);
        view.setPivotY(0);
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1.0f);
        an.setDuration(300);
        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.start();
    }

    private void animateOut(final View view) {
        view.setPivotY(0f);
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.5f);
        an.setDuration(200);

        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animateIn(view);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        an.start();

    }

    @Override
    public int getItemCount() {
        return claims == null ? 0 : claims.size();
    }

    public class ClaimViewHolder extends RecyclerView.ViewHolder {
        protected TextView claimId, policyNumber, date;
        protected View layout;


        public ClaimViewHolder(View itemView) {
            super(itemView);
            claimId = itemView.findViewById(R.id.txtClaimId);
            policyNumber = itemView.findViewById(R.id.txtPolicyNumber);
            date = itemView.findViewById(R.id.txtDate);
            layout = itemView.findViewById(R.id.layout0);
            claimId.setVisibility(View.GONE);
        }

    }


    static final String LOG = ClaimAdapter.class.getSimpleName();
}
