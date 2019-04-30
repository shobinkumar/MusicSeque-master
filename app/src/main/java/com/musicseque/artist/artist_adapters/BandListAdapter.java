package com.musicseque.artist.artist_adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.musicseque.R;
import com.musicseque.artist.artist_models.BandDataModel;
import com.musicseque.artist.band.band_fragment.BandFormFragment;
import com.musicseque.artist.band.band_fragment.BandProfileDetailFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class BandListAdapter extends RecyclerView.Adapter<BandListAdapter.ViewHolder> {
    Context context;
    ArrayList<BandDataModel> al;

    public BandListAdapter(Context ctx, ArrayList<BandDataModel> alBand) {
        context = ctx;
        al = alBand;
    }

    @NonNull
    @Override
    public BandListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_band_item, viewGroup, false);
        return new BandListAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull BandListAdapter.ViewHolder viewHolder, int i) {
        BandDataModel bandDataModel = al.get(i);

        if (al.size() == 1) {
            viewHolder.btnAddBand.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btnAddBand.setVisibility(View.GONE);
        }
        if (bandDataModel.getBandImg().equalsIgnoreCase("")) {

        } else {
            Glide.with(context)
                    .load(bandDataModel.getBandImgPath() + bandDataModel.getBandImg())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(viewHolder.ivBandImage);

        }
        viewHolder.tvBandName.setText(bandDataModel.getBandName());
        viewHolder.tvBandType.setText(bandDataModel.getGenreTypeName());
        viewHolder.tvCityCountry.setText(bandDataModel.getBandCity()+","+bandDataModel.getCountryName());
        viewHolder.btnAddBand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BandFormFragment ldf = new BandFormFragment();
                Bundle args = new Bundle();
                args.putString("band_id", "0");
                ldf.setArguments(args);

                ((FragmentActivity)view. getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, ldf)
                        .commit();

            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BandProfileDetailFragment ldf = new BandProfileDetailFragment();
                Bundle args = new Bundle();
                args.putString("band_id", bandDataModel.getBandId().toString());
                ldf.setArguments(args);

                ((FragmentActivity)view. getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, ldf)
                        .commit();

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
        @BindView(R.id.btnAddBand)
        Button btnAddBand;


        public ViewHolder(@NonNull View itemView) {


            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
