//package com.musicseque.artist.artist_adapters;
//
//import android.content.Context;
//import android.support.annotation.LayoutRes;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import com.musicseque.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ProfileSpinnerAdapter extends ArrayAdapter<String> {
//
//    private final LayoutInflater mInflater;
//    private final Context mContext;
//    private final List<String> items;
//    private final int mResource;
//
//    public ProfileSpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
//                              @NonNull ArrayList<String> objects) {
//        super(context, resource, 0, objects);
//
//        mContext = context;
//        mInflater = LayoutInflater.from(context);
//        mResource = resource;
//        items = objects;
//    }
//    @Override
//    public View getDropDownView(int position, @Nullable View convertView,
//                                @NonNull ViewGroup parent) {
//        return createItemView(position, convertView, parent);
//    }
//
//    @Override
//    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return createItemView(position, convertView, parent);
//    }
//
//    private View createItemView(int position, View convertView, ViewGroup parent){
//        final View view = mInflater.inflate(mResource, parent, false);
//
//        TextView tvData = (TextView) view.findViewById(R.id.tvData);
//
//
//
//
//        tvData.setText(items.get(position));
//
//
//        return view;
//    }
//}