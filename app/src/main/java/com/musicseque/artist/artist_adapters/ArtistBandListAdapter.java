package com.musicseque.artist.artist_adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.musicseque.R;
import com.musicseque.artist.activity.other_artist_activity.ArtistBandDetailActivity;
import com.musicseque.artist.artist_models.BandDataModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ArtistBandListAdapter extends RecyclerView.Adapter<ArtistBandListAdapter.ViewHolder> {
    Context context;
    ArrayList<BandDataModel> al;

    public ArtistBandListAdapter(Context ctx, ArrayList<BandDataModel> alBand) {
        context = ctx;
        al = alBand;
    }

    @NonNull
    @Override
    public ArtistBandListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_artist_band_item, viewGroup, false);
        return new ArtistBandListAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ArtistBandListAdapter.ViewHolder viewHolder, int i) {
        BandDataModel bandDataModel = al.get(i);


        if (bandDataModel.getBandImg().equalsIgnoreCase("")) {
            Glide.with(context)
                    .load(R.drawable.icon_img_dummy)
                    .into(viewHolder.ivBandImage);
        } else {
            Glide.with(context)
                    .load(bandDataModel.getBandImgPath() + bandDataModel.getBandImg())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(viewHolder.ivBandImage);

        }
        viewHolder.tvBandName.setText(bandDataModel.getBandName());
        viewHolder.tvBandType.setText(bandDataModel.getGenreTypeName());
        viewHolder.tvCityCountry.setText(bandDataModel.getBandCity() + "," + bandDataModel.getCountryName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context, ArtistBandDetailActivity.class).putExtra("band_id",bandDataModel.getBandId().toString()));

            }
        });


    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.rlInnerMain)
        RelativeLayout rlInnerMain;

        @BindView(R.id.ivBandImage)
        CircleImageView ivBandImage;

        @BindView(R.id.tvBandName)
        TextView tvBandName;

        @BindView(R.id.tvCityCountry)
        TextView tvCityCountry;

        @BindView(R.id.tvBandType)
        TextView tvBandType;


        @BindView(R.id.view)
        View view;


        public ViewHolder(@NonNull View itemView) {


            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
