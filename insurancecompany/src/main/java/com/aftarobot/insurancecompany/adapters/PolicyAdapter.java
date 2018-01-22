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
import com.aftarobot.mlibrary.data.Policy;

import java.text.DecimalFormat;
import java.util.List;


public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.PolicyViewHolder> {

    public interface PolicyListener {
        void onPolicyTapped(Policy client);

    }

    private PolicyListener mListener;
    private List<Policy> policies;


    public PolicyAdapter(List<Policy> policies, PolicyListener listener) {
        this.policies = policies;
        this.mListener = listener;
    }
    

    @Override
    public PolicyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.policy_item, parent, false);
        return new PolicyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PolicyViewHolder holder, final int position) {

        final Policy p = policies.get(position);
        holder.policyNumber.setText(p.getPolicyNumber());
        holder.type.setText(p.getDescription());
        holder.amount.setText(df.format(p.getAmount()));

        holder.policyNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPolicyTapped(p);
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
        return policies == null ? 0 : policies.size();
    }

    public class PolicyViewHolder extends RecyclerView.ViewHolder {
        protected TextView policyNumber, type, amount;


        public PolicyViewHolder(View itemView) {
            super(itemView);
            policyNumber = itemView.findViewById(R.id.policyNumber);
            type = itemView.findViewById(R.id.type);
            amount = itemView.findViewById(R.id.amount);
        }

    }


    static final String LOG = PolicyAdapter.class.getSimpleName();
    public static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###");
}
