package com.aftarobot.bank;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.aftarobot.mlibrary.data.FundsTransferRequest;

import java.text.DecimalFormat;
import java.util.List;


public class FTRAdapter extends RecyclerView.Adapter<FTRAdapter.FundsTransferRequestViewHolder> {

    public interface FundsTransferRequestListener {
        void onFundsTransferRequestTapped(FundsTransferRequest FundsTransferRequest);

    }

    private FundsTransferRequestListener mListener;
    private List<FundsTransferRequest> fundsTransferRequests;


    public FTRAdapter(List<FundsTransferRequest> FundsTransferRequests, FundsTransferRequestListener listener) {
        this.fundsTransferRequests = FundsTransferRequests;
        this.mListener = listener;
    }
    

    @Override
    public FundsTransferRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_item, parent, false);
        return new FundsTransferRequestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FundsTransferRequestViewHolder holder, final int position) {

        final FundsTransferRequest p = fundsTransferRequests.get(position);
        holder.requestId.setText(p.getFundsTransferRequestId());
        holder.date.setText(p.getDateTime());
        holder.amount.setText(df.format(p.getAmount()));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFundsTransferRequestTapped(p);
            }
        });
        animateIn(holder.amount);

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
        return fundsTransferRequests == null ? 0 : fundsTransferRequests.size();
    }

    public class FundsTransferRequestViewHolder extends RecyclerView.ViewHolder {
        protected TextView requestId, amount, date;
        protected View layout;


        public FundsTransferRequestViewHolder(View itemView) {
            super(itemView);
            requestId = itemView.findViewById(R.id.tfrId);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            layout = itemView.findViewById(R.id.layout0);
        }

    }


    public static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,##0.00");
    static final String TAG = FTRAdapter.class.getSimpleName();
}
