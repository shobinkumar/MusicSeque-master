package com.musicseque.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.musicseque.R;
import com.musicseque.interfaces.RemoveMemberInterface;
import com.musicseque.models.NotificationModel;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter {

    public static final int FOR_REQUEST_SENT = 1;
    public static final int FOR_REQUEST_ACCEPT = 2;
    public static final int FOR_REQUEST_REJECT = 3;
    private final RemoveMemberInterface  memInterface;


    private ArrayList<NotificationModel> dataSet;
    Context mContext;
    int total_types;
    MediaPlayer mPlayer;
    private boolean fabStateVolume = false;


    public NotificationAdapter(ArrayList<NotificationModel> data, Context context, RemoveMemberInterface memberInterface) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
        memInterface=memberInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case FOR_REQUEST_ACCEPT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                return new AcceptViewHolder(view);
            case FOR_REQUEST_SENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sent_request, parent, false);
                return new RequestViewHolder(view);
            case FOR_REQUEST_REJECT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reject_request, parent, false);
                return new RejectViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        NotificationModel object = dataSet.get(listPosition);
        if (object != null) {
            switch (object.getType()) {
                case FOR_REQUEST_SENT:

                    ((RequestViewHolder) holder).btnAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            memInterface.sendMemberId("accept");
                        }
                    });
                    ((RequestViewHolder) holder).btnReject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            memInterface.sendMemberId("reject");
                        }
                    });
                    break;
                case FOR_REQUEST_ACCEPT:

                    break;
                case FOR_REQUEST_REJECT:


                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).getType()) {
            case FOR_REQUEST_SENT:
                return FOR_REQUEST_SENT;
            case FOR_REQUEST_ACCEPT:
                return FOR_REQUEST_ACCEPT;
            case FOR_REQUEST_REJECT:
                return FOR_REQUEST_REJECT;
            default:
                return -1;
        }
    }


    public static class RequestViewHolder extends RecyclerView.ViewHolder {


        TextView tvNotification;
        Button btnAccept;
        Button btnReject;

        public RequestViewHolder(View itemView) {
            super(itemView);

            tvNotification = (TextView) itemView.findViewById(R.id.tvNotification);
            btnAccept = (Button) itemView.findViewById(R.id.btnAccept);
            btnReject = (Button) itemView.findViewById(R.id.btnReject);
        }
    }


    public static class AcceptViewHolder extends RecyclerView.ViewHolder {
        TextView tvNotificationAcceptRequest;

        public AcceptViewHolder(View itemView) {
            super(itemView);
            tvNotificationAcceptRequest = (TextView) itemView.findViewById(R.id.tvNotificationAcceptRequest);


        }
    }


    public static class RejectViewHolder extends RecyclerView.ViewHolder {

        TextView tvNotificationRejectRequest;

        public RejectViewHolder(View itemView) {
            super(itemView);

            tvNotificationRejectRequest = (TextView) itemView.findViewById(R.id.tvNotificationRejectRequest);
        }
    }
}