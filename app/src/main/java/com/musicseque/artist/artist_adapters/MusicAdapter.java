//package com.musicseque.artist.artist_adapters;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.musicseque.Fonts.Noyhr;
//import com.musicseque.R;
//import com.musicseque.artist.activity.MusicDetailActivity;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
//    RecyclerView recyclerView;
//    Context context;
//
//    public MusicAdapter(RecyclerView recyclerView, Context context) {
//        this.recyclerView = recyclerView;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View itemView = LayoutInflater.from(viewGroup.getContext())
//                .inflate(R.layout.row_music_item, viewGroup, false);
//
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 1;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.ivImage)
//        CircleImageView ivImage;
//        @BindView(R.id.tvSongName)
//        Noyhr tvSongName;
//        @BindView(R.id.tvSingerName)
//        Noyhr tvSingerName;
//        @BindView(R.id.tvMusicTime)
//        Noyhr tvMusicTime;
//
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//
//
//        @OnClick
//        void onClick(View view) {
//            int selectedPosition = recyclerView.getChildAdapterPosition(view);
//            context.startActivity(new Intent(context, MusicDetailActivity.class));
//        }
//    }
//
//
//}
