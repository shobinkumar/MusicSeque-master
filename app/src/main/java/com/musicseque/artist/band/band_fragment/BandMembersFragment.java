//package com.musicseque.artist.band.band_fragment;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.musicseque.R;
//import com.musicseque.artist.fragments.KotlinBaseFragment;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//public class BandMembersFragment extends KotlinBaseFragment {
//    View view;
//
//    @BindView(R.id.recyclerBandMember)
//    RecyclerView recyclerBandMember;
//
//    RecyclerView.LayoutManager layoutManager;
//    //BandMemberAdapter adapter;
//
//
//    @BindView(R.id.tvAddBandMember)
//    TextView tvAddBandMember;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_band_members, null);
//        ButterKnife.bind(this, view);
//        initOtherViews();
//        initViews();
//        return view;
//    }
//
//
//    private void initOtherViews() {
//        recyclerBandMember.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//
//
//    }
//
//    private void initViews() {
//
//    }
//
//
//}
