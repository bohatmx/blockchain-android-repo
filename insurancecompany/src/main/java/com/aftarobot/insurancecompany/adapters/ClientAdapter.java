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
import com.aftarobot.mlibrary.data.Client;

import java.util.List;


public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    public interface ClientListener {
        void onClientTapped(Client client);
        void onEmailTapped(Client client);

    }

    private ClientListener mListener;
    private List<Client> clients;


    public ClientAdapter(List<Client> clients, ClientListener listener) {
        this.clients = clients;
        this.mListener = listener;
    }
    

    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_item, parent, false);
        return new ClientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ClientViewHolder holder, final int position) {

        final Client p = clients.get(position);
        holder.name.setText(p.getFirstName().concat(" ").concat(p.getLastName()));
        holder.email.setText(p.getEmail());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClientTapped(p);
            }
        });
        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEmailTapped(p);
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
        return clients == null ? 0 : clients.size();
    }

    public class ClientViewHolder extends RecyclerView.ViewHolder {
        protected TextView name, email;
        protected View layout;


        public ClientViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            layout = itemView.findViewById(R.id.label0);
        }

    }


    static final String LOG = ClientAdapter.class.getSimpleName();
}
