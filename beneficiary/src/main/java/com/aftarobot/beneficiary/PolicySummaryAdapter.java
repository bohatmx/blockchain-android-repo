package com.aftarobot.beneficiary;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.aftarobot.mlibrary.util.PolicyBag;

import java.text.DecimalFormat;
import java.util.List;


public class PolicySummaryAdapter extends RecyclerView.Adapter<PolicySummaryAdapter.BagViewHolder> {

    public interface PolicyBagListener {
        void onClaimCheck(PolicyBag bag);
    }

    private PolicyBagListener mListener;
    private List<PolicyBag> policyBags;


    public PolicySummaryAdapter(List<PolicyBag> policyBags, PolicyBagListener listener) {
        this.policyBags = policyBags;
        this.mListener = listener;
    }
    

    @Override
    public BagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.policy_summary, parent, false);
        return new BagViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final BagViewHolder holder, final int position) {

        final PolicyBag p = policyBags.get(position);
        holder.companyName.setText(p.getCompany().getName());
        holder.address.setText(p.getCompany().getAddress());
        holder.policyNumber.setText(p.getPolicy().getPolicyNumber());
        holder.amount.setText(df.format(p.getPolicy().getAmount()));
        holder.desc.setText(p.getPolicy().getDescription());
        holder.idNumber.setText(p.getClient().getIdNumber());
        holder.client.setText(p.getClient().getFullName());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClaimCheck(p);
            }
        });

        animateIn(holder.layout);

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
        return policyBags == null ? 0 : policyBags.size();
    }

    public class BagViewHolder extends RecyclerView.ViewHolder {
        protected TextView companyName, address, policyNumber,  idNumber, amount, client, desc;
        protected View layout;
        protected Button button;


        public BagViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.companyName);
            address = itemView.findViewById(R.id.companyAddress);
            desc = itemView.findViewById(R.id.description);
            client = itemView.findViewById(R.id.client);
            idNumber = itemView.findViewById(R.id.clientId);
            amount = itemView.findViewById(R.id.policyValue);
            policyNumber = itemView.findViewById(R.id.policyNumber);
            layout = itemView.findViewById(R.id.layout);
            button = itemView.findViewById(R.id.btnCheckClaim);
        }

    }


    public static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,###,###,###,##0.00");
    static final String TAG = PolicySummaryAdapter.class.getSimpleName();
}
