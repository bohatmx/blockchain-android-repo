package com.aftarobot.beneficiary;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.aftarobot.mlibrary.data.Beneficiary;

import java.util.List;


public class BeneficiaryAdapter extends RecyclerView.Adapter<BeneficiaryAdapter.BennieViewHolder> {

    public interface BeneficiaryListener {
        void onRequestTapped(Beneficiary beneficiary);
    }

    private BeneficiaryListener mListener;
    private List<Beneficiary> beneficiaries;


    public BeneficiaryAdapter(List<Beneficiary> beneficiaries, BeneficiaryListener listener) {
        this.beneficiaries = beneficiaries;
        this.mListener = listener;
    }
    

    @Override
    public BennieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.beneficiary_item, parent, false);
        return new BennieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final BennieViewHolder holder, final int position) {

        final Beneficiary p = beneficiaries.get(position);
        holder.name.setText(p.getFullName());
        holder.idNumber.setText(p.getIdNumber());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRequestTapped(p);
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
        return beneficiaries == null ? 0 : beneficiaries.size();
    }

    public class BennieViewHolder extends RecyclerView.ViewHolder {
        protected TextView name, idNumber;
        protected View layout;


        public BennieViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtName);
            idNumber = itemView.findViewById(R.id.txtIdNumber);
            layout = itemView.findViewById(R.id.layout0);
        }

    }


    static final String TAG = BeneficiaryAdapter.class.getSimpleName();
}
