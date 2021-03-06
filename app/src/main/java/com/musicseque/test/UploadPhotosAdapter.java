//package com.musicseque.test;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.util.SparseBooleanArray;
//import android.view.HapticFeedbackConstants;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestOptions;
//import com.musicseque.R;
//import com.musicseque.artist.artist_interface.ClickAdapterListenerInterface;
//import com.musicseque.models.ImageModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class UploadPhotosAdapter extends RecyclerView.Adapter<UploadPhotosAdapter.MyHolder> {
//    private final UploadImage uploadImage;
//    Context context;
//    ArrayList<ImageModel> imageAL;
//
//    private SparseBooleanArray mSelectedItemsIds;
//
//
//    public UploadPhotosAdapter(Context context, ArrayList<ImageModel> imageAL, UploadImage uploadImage) {
//        this.context = context;
//        this.imageAL = imageAL;
//        this.uploadImage = uploadImage;
//
//        mSelectedItemsIds = new SparseBooleanArray();
//    }
//
//    @NonNull
//    @Override
//    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(
//                context);
//        View v = inflater.inflate(R.layout.row_images, parent, false);
//        MyHolder vh = new MyHolder(v);
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
//        ImageModel imageModel = imageAL.get(position);
//        holder.pBar.setVisibility(View.VISIBLE);
//        Glide.with(context)
//                .load(imageModel.getBase_url() + imageModel.getImage_url())
//                .apply(new RequestOptions()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL))
//                .into(holder.ivImage);
//
//
//        if (imageModel.isSelected()) {
//            holder.ivSelected.setVisibility(View.VISIBLE);
//        } else {
//            holder.ivSelected.setVisibility(View.GONE);
//        }
//
////        holder.itemView
////                .setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4
////                        : Color.TRANSPARENT);
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return imageAL.size();
//    }
//
//
//    public class MyHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
//        ProgressBar pBar;
//        ImageView ivImage;
//        RelativeLayout rl;
//        ImageView ivSelected;
//
//        public MyHolder(View itemView) {
//            super(itemView);
//            pBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
//            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
//            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
//            ivSelected = (ImageView) itemView.findViewById(R.id.ivSelected);
//        }
//
//        @Override
//        public boolean onLongClick(View view) {
//
//            return true;
//        }
//    }
//
//
//    public void toggleSelection(int position) {
//        selectView(position, !mSelectedItemsIds.get(position));
//    }
//
//
//    //Remove selected selections
//    public void removeSelection() {
//        mSelectedItemsIds = new SparseBooleanArray();
//        notifyDataSetChanged();
//    }
//
//
//    //Put or delete selected position into SparseBooleanArray
//    public void selectView(int position, boolean value) {
//        if (value)
//        {
//            mSelectedItemsIds.put(position, value);
//            imageAL.get(position).setSelected(true);
//        }
//
//        else
//        {
//            mSelectedItemsIds.delete(position);
//            imageAL.get(position).setSelected(false);
//        }
//
//
//
//        notifyItemChanged(position);
//    }
//
//    //Get total selected count
//    public int getSelectedCount() {
//        return mSelectedItemsIds.size();
//    }
//
//    //Return all selected ids
//    public SparseBooleanArray getSelectedIds() {
//        return mSelectedItemsIds;
//    }
//
//
//    public interface UploadImage {
//        public void callMethod();
//    }
//
//}
