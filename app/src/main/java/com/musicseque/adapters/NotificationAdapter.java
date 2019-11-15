package com.musicseque.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.musicseque.R;
import com.musicseque.firebase_notification.NotificationActivity;
import com.musicseque.interfaces.CommonInterface;
import com.musicseque.interfaces.RemoveMemberInterface;
import com.musicseque.models.NotificationModel;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter {

    public static final int FOR_REQUEST_SENT = 0;
    public static final int FOR_REQUEST_ACCEPT = 1;
    public static final int FOR_REQUEST_REJECT = 2;
    public static final int FOR_OWNER_REMOVE_MEMBER = 3;


    public static final int FOR_VENUE_ACCEPT_REQUEST = 4;
    public static final int FOR_VENUE_REJECT_REQUEST = 5;


    public static final int OWNER_REQUEST_SENT = 10;
    public static final int OWNER_REQUEST_ACCEPT = 11;
    public static final int OWNER_REQUEST_REJECT = 12;
    private CommonInterface memInterface;


    private ArrayList<NotificationModel> dataSet;
    Context mContext;
    int total_types;
    MediaPlayer mPlayer;
    private boolean fabStateVolume = false;


    public NotificationAdapter(Context context, ArrayList<NotificationModel> data, CommonInterface memberInterface) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
        memInterface = memberInterface;
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
            case FOR_OWNER_REMOVE_MEMBER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                return new AcceptViewHolder(view);


            case OWNER_REQUEST_SENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                return new AcceptViewHolder(view);
            case OWNER_REQUEST_ACCEPT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                return new AcceptViewHolder(view);
            case OWNER_REQUEST_REJECT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                return new AcceptViewHolder(view);


        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int listPosition) {

        NotificationModel object = dataSet.get(listPosition);
        if (object != null) {
            if (Integer.parseInt(object.getSender()) == 0) {
                switch (Integer.parseInt(object.getIsRequestStatus())) {
                    case FOR_REQUEST_SENT:


                        RequestViewHolder viewHolder = ((RequestViewHolder) holder);
                        String sourceString = "<b>" + object.getArtistFullName() + "</b> " + " sent you a request to join his band " + "<b>" + object.getVenue_band_name() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                        viewHolder.tvNotification.setText(Html.fromHtml(sourceString));


                        ((RequestViewHolder) holder).btnAccept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                memInterface.commonMethod(object, "accept");
                            }
                        });
                        ((RequestViewHolder) holder).btnReject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                memInterface.commonMethod(object, "reject");
                            }
                        });
                        break;
                    case FOR_REQUEST_ACCEPT:
                        AcceptViewHolder viewHolderAccept = ((AcceptViewHolder) holder);
                        String sourceStringAccept = "You are a member of band " + "<b>" + object.getVenue_band_name() + "</b> " + " made by " + "<b>" + object.getArtistFullName() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                        viewHolderAccept.tvNotificationAcceptRequest.setText(Html.fromHtml(sourceStringAccept));


                        break;
                    case FOR_REQUEST_REJECT:
                        RejectViewHolder viewHolderReject = ((RejectViewHolder) holder);


                        String sourceStringReject = "You reject the request to join " + "<b>" + object.getVenue_band_name() + "</b> " + " made by " + "<b>" + object.getArtistFullName() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                        viewHolderReject.tvNotificationRejectRequest.setText(Html.fromHtml(sourceStringReject));


                        break;

                    case FOR_OWNER_REMOVE_MEMBER:
                        AcceptViewHolder viewHolderOwnerRemoveMember = ((AcceptViewHolder) holder);


                        String sourceStringOwnerRemove = "<b>" + object.getArtistFullName() + "</b> " + " removed you from band  " + "<b>" + object.getVenue_band_name() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                        viewHolderOwnerRemoveMember.tvNotificationAcceptRequest.setText(Html.fromHtml(sourceStringOwnerRemove));


                        break;

                    //For Owner


                }
            } else {
                switch (Integer.parseInt(object.getIsRequestStatus())) {
                    case FOR_REQUEST_SENT:
                        AcceptViewHolder viewHolderOwnerSent = ((AcceptViewHolder) holder);

                        String sourceStringAccept = "You sent a request to " + "<b>" + object.getArtistFullName() + "</b> " + " to add in band " + "<b>" + object.getVenue_band_name() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                        viewHolderOwnerSent.tvNotificationAcceptRequest.setText(Html.fromHtml(sourceStringAccept));

                        break;
                    case FOR_REQUEST_ACCEPT:
                        AcceptViewHolder viewHolderOwnerAccept = ((AcceptViewHolder) holder);


                        String sourceStringOwnerAccept = "<b>" + object.getArtistFullName() + "</b> " + " is now a member of band " + "<b>" + object.getVenue_band_name() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                        viewHolderOwnerAccept.tvNotificationAcceptRequest.setText(Html.fromHtml(sourceStringOwnerAccept));

                        break;
                    case FOR_REQUEST_REJECT:
                        AcceptViewHolder viewHolderOwnerReject = ((AcceptViewHolder) holder);


                        String sourceStringReject = "<b>" + object.getArtistFullName() + "</b> " + " reject your request to join  band " + "<b>" + object.getVenue_band_name() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                        viewHolderOwnerReject.tvNotificationAcceptRequest.setText(Html.fromHtml(sourceStringReject));

                        break;

                    case FOR_VENUE_ACCEPT_REQUEST:
                        AcceptViewHolder viewHolderVenueAccept = ((AcceptViewHolder) holder);


                        String sourceStringVenueAccept = "<b>" + object.getVenue_band_name() + "</b> " + " accept your request for the event " + "<b>" + object.getEvent_title() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                        viewHolderVenueAccept.tvNotificationAcceptRequest.setText(Html.fromHtml(sourceStringVenueAccept));

                        break;
                    case FOR_VENUE_REJECT_REQUEST:
                        AcceptViewHolder viewHolderVenueReject = ((AcceptViewHolder) holder);


                        String sourceStringVenueReject = "<b>" + object.getVenue_band_name() + "</b> " + " reject your request for the event " + "<b>" + object.getEvent_title() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                        viewHolderVenueReject.tvNotificationAcceptRequest.setText(Html.fromHtml(sourceStringVenueReject));

                        break;


                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (Integer.parseInt(dataSet.get(position).getSender()) == 0) {
            switch (Integer.parseInt(dataSet.get(position).getIsRequestStatus())) {
                case FOR_REQUEST_SENT:
                    return FOR_REQUEST_SENT;
                case FOR_REQUEST_ACCEPT:
                    return FOR_REQUEST_ACCEPT;
                case FOR_REQUEST_REJECT:
                    return FOR_REQUEST_REJECT;
                case FOR_OWNER_REMOVE_MEMBER:
                    return FOR_OWNER_REMOVE_MEMBER;
                default:
                    return -1;
            }
        } else {
            switch (Integer.parseInt(dataSet.get(position).getIsRequestStatus())) {
                case FOR_REQUEST_SENT:
                    return OWNER_REQUEST_SENT;
                case FOR_REQUEST_ACCEPT:
                    return OWNER_REQUEST_ACCEPT;
                case FOR_REQUEST_REJECT:
                    return OWNER_REQUEST_REJECT;
                case FOR_VENUE_ACCEPT_REQUEST:
                    return FOR_VENUE_ACCEPT_REQUEST;
                case FOR_VENUE_REJECT_REQUEST:
                    return FOR_VENUE_REJECT_REQUEST;


                default:
                    return -1;
            }
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