package com.musicseque.artist.artist_adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.musicseque.Fonts.Noyhr;
import com.musicseque.MainActivity;
import com.musicseque.R;
import com.musicseque.artist.activity.OtherProfileActivity;
import com.musicseque.artist.activity.SearchArtistActivity;
import com.musicseque.artist.artist_models.ArtistModel;
import com.musicseque.dagger_data.DaggerRetrofitComponent;
import com.musicseque.dagger_data.SharedPrefDependency;
import com.musicseque.service.LocationService;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.TextViewWithoutPaddings;
import com.musicseque.utilities.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;




public class SearchArtistAdapter extends RecyclerView.Adapter<SearchArtistAdapter.ViewHolder> {
    ArrayList<ArtistModel> arrayList = new ArrayList<ArtistModel>();
    Context context;
    String mUserId;
    DecimalFormat df2;
    public SearchArtistAdapter(SearchArtistActivity context, ArrayList<ArtistModel> arrayList,String user_id) {
        this.arrayList = arrayList;
        this.context = context;
        mUserId=user_id;
           df2 = new DecimalFormat(".##");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(
                context);
        View v = inflater.inflate(R.layout.row_search_artist, parent, false);
        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        ArtistModel artistModel = arrayList.get(i);

            viewHolder.rlMain.setVisibility(View.VISIBLE);
            viewHolder.tvArtistName.setText(artistModel.getFirstName()+ " "+artistModel.getLastName());

            double mDistance=Utils.calculateDistance(Double.parseDouble(LocationService.mLatitude),Double.parseDouble(LocationService.mLongitude),Double.parseDouble(artistModel.getUserLatitude()),Double.parseDouble(artistModel.getUserLongitude()));
            viewHolder.tvDistance.setText(df2.format(mDistance)+" miles");
        viewHolder.tvProfileType.setText(artistModel.getExpertise()+","+artistModel.getGenreTypeName());
        viewHolder.tvCountry.setText(artistModel.getCity()+","+artistModel.getCountryName());
            if (artistModel.getSocialImageUrl() == null || artistModel.getSocialImageUrl().equalsIgnoreCase("null") || artistModel.getSocialImageUrl().equalsIgnoreCase("")) {
                Glide.with(context).load(artistModel.getServerpath() + artistModel.getProfilePic()).into(viewHolder.ivArtistImage);

            }
            else
            {
                Glide.with(context).load(artistModel.getSocialImageUrl()).into(viewHolder.ivArtistImage);

            }

        if (artistModel.getNewStatus().equalsIgnoreCase("Available")) {
            viewHolder.ivIndicator.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_green));
        } else if (artistModel.getNewStatus().equalsIgnoreCase("Offline")) {
            viewHolder.ivIndicator.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_invisible));
        } else {
            viewHolder.ivIndicator.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_red));
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, OtherProfileActivity.class).putExtra("id",artistModel.getUserId()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivArtistImage)
        ImageView ivArtistImage;
        @BindView(R.id.ivIndicator)
        ImageView ivIndicator;
        @BindView(R.id.tvArtistName)
        Noyhr tvArtistName;
        @BindView(R.id.tvProfileType)
        Noyhr tvProfileType;
        @BindView(R.id.tvDistance)
        Noyhr tvDistance;
        @BindView(R.id.tvDay)
        TextView tvDay;
        @BindView(R.id.tvCountry)
        TextView tvCountry;
        @BindView(R.id.tvMonth)
        com.musicseque.Fonts.Noyhr tvMonth;

        @BindView(R.id.rlMain)
        LinearLayout rlMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
