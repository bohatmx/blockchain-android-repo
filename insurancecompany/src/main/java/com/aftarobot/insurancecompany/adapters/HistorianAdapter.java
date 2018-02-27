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
import com.aftarobot.mlibrary.data.HistorianRecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HistorianAdapter extends RecyclerView.Adapter<HistorianAdapter.HistorianViewHolder> {

    public interface HistorianListener {
        void onClaimTapped(HistorianRecord claim);

    }

    private HistorianListener mListener;
    private List<HistorianRecord> historianRecords;


    public HistorianAdapter(List<HistorianRecord> historianRecords, HistorianListener listener) {
        this.historianRecords = historianRecords;
        this.mListener = listener;
    }
    

    @Override
    public HistorianViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trans_item, parent, false);
        return new HistorianViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final HistorianViewHolder holder, final int position) {

        final HistorianRecord p = historianRecords.get(position);

        int i = p.getTransactionType().lastIndexOf(".");
        String type = p.getTransactionType().substring(i + 1);
        holder.type.setText(type);
        try {
            Date date = sdfIn.parse(p.getTransactionTimestamp());
            Date localDate = new Date(date.getTime() + TWO_HOURS);
            holder.timeStamp.setText(sdfOut.format(localDate));
        } catch (ParseException e) {
            holder.timeStamp.setText("");
            e.printStackTrace();
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClaimTapped(p);
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
        return historianRecords == null ? 0 : historianRecords.size();
    }

    public class HistorianViewHolder extends RecyclerView.ViewHolder {
        protected TextView type, timeStamp;
        protected View layout;


        public HistorianViewHolder(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.txtType);
            timeStamp = itemView.findViewById(R.id.txtTimestamp);
            layout = itemView.findViewById(R.id.layout0);
        }

    }


    static final String LOG = HistorianAdapter.class.getSimpleName();
    static final long TWO_HOURS = 1000 * 60 * 60 * 2;
    SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'", Locale.getDefault());
    SimpleDateFormat sdfOut = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault());
}
