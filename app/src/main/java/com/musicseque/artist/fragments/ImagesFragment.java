package com.musicseque.artist.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musicseque.Fonts.Noyhr;
import com.musicseque.R;

import com.musicseque.artist.artist_adapters.ShowPhotosAdapter;
import com.musicseque.artist.artist_adapters.UploadPhotosAdapter;
import com.musicseque.models.ImageModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagesFragment extends BaseFragment implements UploadPhotosAdapter.UploadImage {
    View view;
    @BindView(R.id.recyclerImages)
    RecyclerView recyclerImages;

    @BindView(R.id.tvNoImages)
    Noyhr tvNoImages;

    ArrayList<ImageModel> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_images, null);
        ButterKnife.bind(this, view);

        initOtherViews();
        initViews();
        listeners();
        return view;
    }


    private void initOtherViews() {

    }

    private void initViews() {
        recyclerImages.setLayoutManager(new GridLayoutManager(getActivity(), 4));


        if(getArguments().getString("data").equalsIgnoreCase(""))
        {
            arrayList=new ArrayList<>();
        }
        else
        {
            arrayList = new Gson().fromJson(getArguments().getString("data"), new TypeToken<ArrayList<ImageModel>>() {
            }.getType());
        }


        if (arrayList.size() > 0) {
            tvNoImages.setVisibility(View.GONE);
            recyclerImages.setVisibility(View.VISIBLE);
            recyclerImages.setAdapter(new ShowPhotosAdapter(getActivity(), arrayList));

        } else {
            recyclerImages.setVisibility(View.GONE);

            tvNoImages.setVisibility(View.VISIBLE);
        }
    }


    private void listeners() {

    }

    @Override
    public void callMethod() {

    }
}
