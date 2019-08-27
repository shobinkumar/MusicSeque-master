package com.musicseque.artist.artist_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.musicseque.Fonts.Noyhr;
import com.musicseque.R;
import com.musicseque.artist.activity.other_artist_activity.SearchArtistActivity;
import com.musicseque.artist.artist_models.ArtistModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchBandMemberAdapter extends RecyclerView.Adapter<SearchBandMemberAdapter.ViewHolder> {
    ArrayList<ArtistModel> arrayList = new ArrayList<ArtistModel>();
    Context context;
    String mUserId;
    DecimalFormat df2;
    public SearchBandMemberAdapter(SearchArtistActivity context, ArrayList<ArtistModel> arrayList, String user_id) {
        this.arrayList = arrayList;
        this.context = context;
        mUserId=user_id;
        df2 = new DecimalFormat(".##");
    }

    @NonNull
    @Override
    public SearchBandMemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(
                context);
        View v = inflater.inflate(R.layout.row_band_member, parent, false);
        SearchBandMemberAdapter.ViewHolder vh = new SearchBandMemberAdapter.ViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchBandMemberAdapter.ViewHolder viewHolder, int i)
    {

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
