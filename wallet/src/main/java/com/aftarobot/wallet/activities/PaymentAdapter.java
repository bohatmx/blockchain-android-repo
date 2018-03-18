package com.aftarobot.wallet.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.aftarobot.wallet.R;
import com.aftarobot.wallet.data.payments.Record;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    public interface PaymentListener {
        void onClientTapped(Record payment);
        void onEmailTapped(Record payment);

    }

    private PaymentListener mListener;
    private List<Record> payments;
    private String accountID;
    public static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMMM yyyy HH:mm", Locale.getDefault());


    public PaymentAdapter(List<Record> payments, String accountID, PaymentListener listener) {
        this.payments = payments;
        this.mListener = listener;
        this.accountID = accountID;
    }
    

    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_item, parent, false);
        return new PaymentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PaymentViewHolder holder, final int position) {

        final Record p = payments.get(position);
        holder.fromAcct.setText(p.getFrom());
        holder.toAcct.setText(p.getTo());
        holder.amount.setText(p.getAmount());
        holder.date.setText(sdf.format(p.getCreatedAt()));

        holder.fromAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClientTapped(p);
            }
        });
        holder.amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEmailTapped(p);
            }
        });

        if (p.getFrom() != null) {
            if (p.getFrom().equalsIgnoreCase(accountID)) {
                holder.amount.setTextColor(Color.parseColor("red"));
            } else {
                holder.amount.setTextColor(Color.parseColor("black"));
            }
        }


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
        return payments == null ? 0 : payments.size();
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder {
        protected TextView fromAcct, toAcct, date, amount;
        protected View layout;


        public PaymentViewHolder(View itemView) {
            super(itemView);
            fromAcct = itemView.findViewById(R.id.txtFromAcct);
            toAcct = itemView.findViewById(R.id.txtToAcct);
            amount = itemView.findViewById(R.id.txtAmount);
            date = itemView.findViewById(R.id.txtDate);
        }

    }


    static final String LOG = PaymentAdapter.class.getSimpleName();
}
