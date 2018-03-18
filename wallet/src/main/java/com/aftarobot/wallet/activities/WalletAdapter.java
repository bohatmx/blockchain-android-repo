package com.aftarobot.wallet.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.aftarobot.mlibrary.data.Photo;
import com.aftarobot.mlibrary.data.Wallet;
import com.aftarobot.wallet.R;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletViewHolder> {

    public interface WalletListener {
        void onWalletTapped(Wallet payment);

    }

    private WalletListener mListener;
    private List<Wallet> payments;
    private Context context;
    public static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMMM yyyy HH:mm", Locale.getDefault());


    public WalletAdapter(List<Wallet> payments, Context context, WalletListener listener) {
        this.payments = payments;
        this.mListener = listener;
        this.context = context;
    }
    

    @Override
    public WalletViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallet_item, parent, false);
        return new WalletViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final WalletViewHolder holder, final int position) {

        final Wallet p = payments.get(position);
        holder.account.setText(p.getAccountID());
        holder.name.setText(p.getName());

        if (p.getPhotos() != null && !p.getPhotos().isEmpty()) {
            Photo px = p.getPhotos().get(p.getPhotos().size() - 1);
            Glide.with(context).load(Uri.parse(px.getUrl())).into(holder.image);
        } else {
            Glide.with(context).load(ContextCompat.getDrawable(context,R.drawable.back5));
        }


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onWalletTapped(p);
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onWalletTapped(p);
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
        return payments == null ? 0 : payments.size();
    }

    public class WalletViewHolder extends RecyclerView.ViewHolder {
        protected TextView account, name;
        CircleImageView image;


        public WalletViewHolder(View itemView) {
            super(itemView);
            account = itemView.findViewById(R.id.txtAccount);
            name = itemView.findViewById(R.id.txtName);
            image = itemView.findViewById(R.id.image);
        }

    }


    static final String LOG = WalletAdapter.class.getSimpleName();
}
