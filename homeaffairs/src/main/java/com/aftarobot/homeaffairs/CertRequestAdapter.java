package com.aftarobot.homeaffairs;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.aftarobot.mlibrary.data.DeathCertificateRequest;

import java.util.List;


public class CertRequestAdapter extends RecyclerView.Adapter<CertRequestAdapter.RequestViewHolder> {

    public interface RequestListener {
        void onRequestTapped(DeathCertificateRequest request);
    }

    private RequestListener mListener;
    private List<DeathCertificateRequest> requests;


    public CertRequestAdapter(List<DeathCertificateRequest> requests, RequestListener listener) {
        this.requests = requests;
        this.mListener = listener;
    }
    

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_item, parent, false);
        return new RequestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RequestViewHolder holder, final int position) {

        final DeathCertificateRequest p = requests.get(position);
        holder.idNumber.setText(p.getIdNumber());
        holder.date.setText(p.getDateTime());

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
        return requests == null ? 0 : requests.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {
        protected TextView idNumber, date;
        protected View layout;


        public RequestViewHolder(View itemView) {
            super(itemView);
            idNumber = itemView.findViewById(R.id.txtId);
            date = itemView.findViewById(R.id.txtDate);
            layout = itemView.findViewById(R.id.layout0);
        }

    }


    static final String LOG = CertRequestAdapter.class.getSimpleName();
}
