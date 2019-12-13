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
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.SharedPref;

import java.util.ArrayList;

import static com.musicseque.utilities.Constants.PROFILE_TYPE;

public class NotificationAdapter extends RecyclerView.Adapter {

    public static final int FOR_REQUEST_SENT = 0;
    public static final int FOR_REQUEST_ACCEPT = 1;
    public static final int FOR_REQUEST_REJECT = 3;
    public static final int REMOVE_MEMBER = 2;


    public static final int FOR_VENUE_ACCEPT_REQUEST = 4;
    public static final int FOR_VENUE_REJECT_REQUEST = 5;


    //Venue
    public static final int ARTIST_SEND_BOOKING_REQ = 6;

    public static final int FOR_FOLLOWED_ARTIST = 7;
    public static final int FOR_UNFOLLOWED_ARTIST = 8;


    public static final int YOU_SENT_REQUEST = 10;
    public static final int YOUR_REQUEST_ACCEPT = 11;
    public static final int YOUR_REQUEST_REJECT = 12;
    public static final int YOUR_REMOVE_MEMBER = 13;
    public static final int YOU_FOLLOWED_ARTIST = 14;
    public static final int YOU_UNFOLLOWED_ARTIST = 15;
    private String mProfileType = "";
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
        mProfileType = SharedPref.getString(PROFILE_TYPE, "");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (mProfileType.equalsIgnoreCase("Venue Manager")) {
            switch (viewType) {
                case FOR_VENUE_ACCEPT_REQUEST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);

                case FOR_VENUE_REJECT_REQUEST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);
                case ARTIST_SEND_BOOKING_REQ:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);

                case FOR_FOLLOWED_ARTIST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);
                case FOR_UNFOLLOWED_ARTIST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);
            }
        } else {
            switch (viewType) {

                case FOR_REQUEST_SENT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sent_request, parent, false);
                    return new RequestViewHolder(view);


                case YOU_SENT_REQUEST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);




                case FOR_REQUEST_ACCEPT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);

                case YOUR_REQUEST_ACCEPT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);



                case FOR_REQUEST_REJECT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reject_request, parent, false);
                    return new RejectViewHolder(view);

                case YOUR_REQUEST_REJECT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);



                    case REMOVE_MEMBER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);

                case YOUR_REMOVE_MEMBER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);



                case FOR_FOLLOWED_ARTIST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);

                case YOU_FOLLOWED_ARTIST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);


                case FOR_UNFOLLOWED_ARTIST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);

                case YOU_UNFOLLOWED_ARTIST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);



                case ARTIST_SEND_BOOKING_REQ:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);

                case FOR_VENUE_ACCEPT_REQUEST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);

                case FOR_VENUE_REJECT_REQUEST:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accept_request, parent, false);
                    return new AcceptViewHolder(view);


            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int listPosition) {

        NotificationModel object = dataSet.get(listPosition);
        if (object != null) {


            if (SharedPref.getString(PROFILE_TYPE, "").equalsIgnoreCase("Venue Manager")) {

                    switch (object.getIsRequestStatus()) {
                        case FOR_VENUE_ACCEPT_REQUEST:
                            AcceptViewHolder viewHolderVenueAccept = ((AcceptViewHolder) holder);


                            String sourceStringVenueAccept = " You accepted  request for the event " + "<b>" + object.getEvent_title() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderVenueAccept.tvNotificationAcceptRequest.setText(Html.fromHtml(sourceStringVenueAccept));

                            break;
                        case FOR_VENUE_REJECT_REQUEST:
                            AcceptViewHolder viewHolderVenueReject = ((AcceptViewHolder) holder);


                            String sourceStringVenueReject = " You rejected  request for the event " + "<b>" + object.getEvent_title() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderVenueReject.tvNotificationAcceptRequest.setText(Html.fromHtml(sourceStringVenueReject));

                            break;


                        case ARTIST_SEND_BOOKING_REQ:
                            AcceptViewHolder viewHolderArtistSendReq = ((AcceptViewHolder) holder);


                            String sourceStringArtistSendReq = object.getArtistFullName() + " send the request for event " + object.getEvent_title() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderArtistSendReq.tvNotificationAcceptRequest.setText(Html.fromHtml(sourceStringArtistSendReq));

                            break;
                        case FOR_FOLLOWED_ARTIST:
                            AcceptViewHolder viewHolderFollowArtist = ((AcceptViewHolder) holder);


                            String mFollowArtist ="You followed "+ "<b>" + object.getArtistFullName() + "</b> "  + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderFollowArtist.tvNotificationAcceptRequest.setText(Html.fromHtml(mFollowArtist));


                            break;


                        case FOR_UNFOLLOWED_ARTIST:
                            AcceptViewHolder viewHolderUnFollowArtist = ((AcceptViewHolder) holder);


                            String mUnFollowArtist ="You unfollowed "+ "<b>" + object.getArtistFullName() + "</b> "  + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderUnFollowArtist.tvNotificationAcceptRequest.setText(Html.fromHtml(mUnFollowArtist));


                            break;
                    }

            } else {

                if (object.getSender() == 0) {
                    switch (object.getIsRequestStatus()) {
                        case FOR_REQUEST_SENT:


                            RequestViewHolder viewHolder = ((RequestViewHolder) holder);
                            String sourceString = "<b>" + object.getArtistFullName() + "</b> " + " sent you a request to join his band " + "<b>" + object.getVenue_band_name() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolder.tvNotification.setText(Html.fromHtml(sourceString));


                            viewHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    memInterface.commonMethod(object, "accept");
                                }
                            });
                            viewHolder.btnReject.setOnClickListener(new View.OnClickListener() {
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


                            String sourceStringReject = "You reject the request to join the band  " + "<b>" + object.getVenue_band_name() + "</b> " + " made by " + "<b>" + object.getArtistFullName() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderReject.tvNotificationRejectRequest.setText(Html.fromHtml(sourceStringReject));


                            break;

                        case REMOVE_MEMBER:
                            AcceptViewHolder viewHolderOwnerRemoveMember = ((AcceptViewHolder) holder);


                            String sourceStringOwnerRemove = "<b>" + object.getArtistFullName() + "</b> " + " removed you from band  " + "<b>" + object.getVenue_band_name() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderOwnerRemoveMember.tvNotificationAcceptRequest.setText(Html.fromHtml(sourceStringOwnerRemove));


                            break;


                        case FOR_FOLLOWED_ARTIST:
                            AcceptViewHolder viewHolderFollowArtist = ((AcceptViewHolder) holder);


                            String mFollowArtist = "<b>" + object.getArtistFullName() + "</b> " + " followed you  " + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderFollowArtist.tvNotificationAcceptRequest.setText(Html.fromHtml(mFollowArtist));


                            break;


                        case FOR_UNFOLLOWED_ARTIST:
                            AcceptViewHolder viewHolderUnFollowArtist = ((AcceptViewHolder) holder);


                            String mUnFollowArtist = "<b>" + object.getArtistFullName() + "</b> " + " unfollowed you  " + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderUnFollowArtist.tvNotificationAcceptRequest.setText(Html.fromHtml(mUnFollowArtist));


                            break;




                        //For Owner


                    }
                } else {
                    switch (object.getIsRequestStatus()) {
                        case 0:
                            AcceptViewHolder viewHolderOwnerSent = ((AcceptViewHolder) holder);

                            String sourceStringAccept = "You sent a request to " + "<b>" + object.getArtistFullName() + "</b> " + " to add in band " + "<b>" + object.getVenue_band_name() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderOwnerSent.tvNotificationAcceptRequest.setText(Html.fromHtml(sourceStringAccept));

                            break;



                        case 1:
                            AcceptViewHolder viewHolderOwnerAccept = ((AcceptViewHolder) holder);


                            String sourceStringOwnerAccept = "<b>" + object.getArtistFullName() + "</b> " + " is now a member of your band " + "<b>" + object.getVenue_band_name() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderOwnerAccept.tvNotificationAcceptRequest.setText(Html.fromHtml(sourceStringOwnerAccept));

                            break;
                        case 3:
                            AcceptViewHolder viewHolderOwnerReject = ((AcceptViewHolder) holder);


                            String sourceStringReject = "<b>" + object.getArtistFullName() + "</b> " + " reject your request to join  band " + "<b>" + object.getVenue_band_name() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderOwnerReject.tvNotificationAcceptRequest.setText(Html.fromHtml(sourceStringReject));

                            break;

                        case 2:
                            AcceptViewHolder viewHolderOwnerRemoveMember = ((AcceptViewHolder) holder);


                            String sourceStringOwnerRemove ="You removed the "+ "<b>" + object.getArtistFullName() + "</b> " + "  from band  " + "<b>" + object.getVenue_band_name() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderOwnerRemoveMember.tvNotificationAcceptRequest.setText(Html.fromHtml(sourceStringOwnerRemove));


                            break;




                        case FOR_FOLLOWED_ARTIST:
                            AcceptViewHolder viewHolderFollowArtist = ((AcceptViewHolder) holder);


                            String mFollowArtist ="You followed "+ "<b>" + object.getArtistFullName() + "</b> "  + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderFollowArtist.tvNotificationAcceptRequest.setText(Html.fromHtml(mFollowArtist));


                            break;


                        case FOR_UNFOLLOWED_ARTIST:
                            AcceptViewHolder viewHolderUnFollowArtist = ((AcceptViewHolder) holder);


                            String mUnFollowArtist ="You unfollowed "+ "<b>" + object.getArtistFullName() + "</b> "  + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderUnFollowArtist.tvNotificationAcceptRequest.setText(Html.fromHtml(mUnFollowArtist));


                            break;


                        case ARTIST_SEND_BOOKING_REQ:
                            AcceptViewHolder viewHolderOwnerSendReq = ((AcceptViewHolder) holder);


                            String sourceStringOwnerSendREq = "You send a request to  " + "<b>" + object.getVenue_band_name() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderOwnerSendReq.tvNotificationAcceptRequest.setText(Html.fromHtml(sourceStringOwnerSendREq));


                            break;



                        case FOR_VENUE_ACCEPT_REQUEST:
                            AcceptViewHolder viewHolderVenueAccept = ((AcceptViewHolder) holder);


                            String strVenueAcceptReq = "<b>" + object.getVenue_band_name() + "</b>" +" Venue Manager accept your request for   " + "<b>" + object.getEvent_title() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderVenueAccept.tvNotificationAcceptRequest.setText(Html.fromHtml(strVenueAcceptReq));


                            break;


                        case FOR_VENUE_REJECT_REQUEST:
                            AcceptViewHolder viewHolderOwnerRejectReq = ((AcceptViewHolder) holder);


                            String strVenueRejectReq = "<b>" + object.getVenue_band_name() + "</b>" +" Venue Manager reject your request for   " + "<b>" + object.getEvent_title() + "</b>" + "<br><br>" + object.getCreated_date() + "</br></br>";

                            viewHolderOwnerRejectReq.tvNotificationAcceptRequest.setText(Html.fromHtml(strVenueRejectReq));


                            break;



                    }
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

        if (mProfileType.equalsIgnoreCase("Venue Manager")) {
            switch (dataSet.get(position).getIsRequestStatus()) {
                case FOR_VENUE_ACCEPT_REQUEST:
                    return FOR_VENUE_ACCEPT_REQUEST;
                case FOR_VENUE_REJECT_REQUEST:
                    return FOR_VENUE_REJECT_REQUEST;
                case ARTIST_SEND_BOOKING_REQ:
                    return ARTIST_SEND_BOOKING_REQ;

                    case FOR_FOLLOWED_ARTIST:
                    return FOR_FOLLOWED_ARTIST;
                case FOR_UNFOLLOWED_ARTIST:
                    return FOR_UNFOLLOWED_ARTIST;
            }

        } else {
            if (dataSet.get(position).getSender() == 0) {
                switch (dataSet.get(position).getIsRequestStatus()) {
                    case FOR_REQUEST_SENT:
                        return FOR_REQUEST_SENT;
                    case FOR_REQUEST_ACCEPT:
                        return FOR_REQUEST_ACCEPT;
                    case FOR_REQUEST_REJECT:
                        return FOR_REQUEST_REJECT;
                    case REMOVE_MEMBER:
                        return REMOVE_MEMBER;
                    case FOR_FOLLOWED_ARTIST:
                        return FOR_FOLLOWED_ARTIST;
                    case FOR_UNFOLLOWED_ARTIST:
                        return FOR_UNFOLLOWED_ARTIST;
                    default:
                        return -1;
                }
            } else {
                switch (dataSet.get(position).getIsRequestStatus()) {
                    case FOR_REQUEST_SENT:
                        return YOU_SENT_REQUEST;
                    case FOR_REQUEST_ACCEPT:
                        return YOUR_REQUEST_ACCEPT;
                    case FOR_REQUEST_REJECT:
                        return YOUR_REQUEST_REJECT;

                    case REMOVE_MEMBER:
                        return YOUR_REMOVE_MEMBER;


                    case FOR_FOLLOWED_ARTIST:
                        return YOU_FOLLOWED_ARTIST;
                    case FOR_UNFOLLOWED_ARTIST:
                        return YOU_UNFOLLOWED_ARTIST;


                    case ARTIST_SEND_BOOKING_REQ:
                        return ARTIST_SEND_BOOKING_REQ;


                    case FOR_VENUE_ACCEPT_REQUEST:
                        return FOR_VENUE_ACCEPT_REQUEST;
                    case FOR_VENUE_REJECT_REQUEST:
                        return FOR_VENUE_REJECT_REQUEST;




                    default:
                        return -1;
                }
            }
        }

        return -1;
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