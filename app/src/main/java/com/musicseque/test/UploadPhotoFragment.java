//package com.musicseque.test;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.view.ActionMode;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//
//import android.util.SparseBooleanArray;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.musicseque.R;
//import com.musicseque.artist.activity.UploadActivity;
//
//import com.musicseque.dagger_data.DaggerRetrofitComponent;
//import com.musicseque.dagger_data.RetrofitComponent;
//import com.musicseque.dagger_data.SharedPrefDependency;
//import com.musicseque.interfaces.MyInterface;
//import com.musicseque.models.ImageModel;
//import com.musicseque.retrofit_interface.ImageUploadClass;
//import com.musicseque.retrofit_interface.RetrofitAPI;
//import com.musicseque.utilities.Constants;
//import com.musicseque.utilities.Utils;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//
//public class UploadPhotoFragment extends Fragment implements MyInterface, UploadPhotosAdapter.UploadImage {
//    public static final int REQUEST_IMAGE = 100;
//    private static final int PICK_IMAGE_REQUEST = 101;
//
//
//    @BindView(R.id.floatingButtonUploadPhoto)
//    FloatingActionButton floatingButtonUploadPhoto;
//
//
//    RecyclerView recyclerPhotos;
//    GridLayoutManager layoutManager;
//    View view;
//
//    private String mImage = "";
//
//    SharedPreferences sharedPreferences;
//    SharedPreferences.Editor editor;
//    private RetrofitComponent retrofitComponent;
//    ArrayList<ImageModel> arrayList = new ArrayList<>();
//    UploadPhotosAdapter uploadPhotosAdapter;
//   // private ActionModeCallback actionModeCallback;
//   private ActionMode mActionMode;
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_upload_photos, container, false);
//        ButterKnife.bind(this, view);
//        initOtherViews();
//        initViews();
//        listeners();
//        hitAPIPhotos();
//        implementRecyclerViewClickListeners();
//        return view;
//    }
//
//
//    private void initOtherViews() {
//        retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getActivity())).build();
//        sharedPreferences = retrofitComponent.getShared();
//        editor = retrofitComponent.getEditor();
//        //actionModeCallback = new ActionModeCallback();
//    }
//
//    private void initViews() {
//        recyclerPhotos = (RecyclerView) view.findViewById(R.id.recyclerPhotos);
//        layoutManager = new GridLayoutManager(getActivity(), 4);
//        recyclerPhotos.setLayoutManager(layoutManager);
//    }
//
//    private void listeners() {
//    }
//
//
//    @Override
//    public void sendResponse(Object response, int TYPE) {
//        Utils.hideProgressDialog();
//        if (TYPE == Constants.FOR_ARTIST_UPLOADED_IMAGES) {
//            try {
//                JSONArray jsonArray = new JSONArray(response.toString());
//                if (jsonArray.length() > 0) {
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        arrayList.add(new ImageModel(jsonObject.getString("ImgUrl"), jsonObject.getString("Image"), true, jsonObject.getInt("Id"),false));
//                    }
//
//                } else {
//                }
//                //  arrayList.add(new ImageModel("", "", false));
//                uploadPhotosAdapter = new UploadPhotosAdapter(getActivity(), arrayList, UploadPhotoFragment.this);
//                recyclerPhotos.setAdapter(uploadPhotosAdapter);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        } else if (TYPE == Constants.FOR_UPLOAD_ARTIST_IMAGE) {
//            try {
//                JSONObject jsonObject = new JSONObject(response.toString());
//                if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
//                    arrayList.add(0, new ImageModel(jsonObject.getString("ImgUrl"), jsonObject.getString("Image"), true, jsonObject.getInt("Id"),false));
//                    uploadPhotosAdapter = new UploadPhotosAdapter(getActivity(), arrayList, UploadPhotoFragment.this);
//                    recyclerPhotos.setAdapter(uploadPhotosAdapter);
//                } else {
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else if (TYPE == Constants.FOR_DELETE_ARTIST_IMAGES) {
//
//        }
//    }
//
//
//    private void hitAPIPhotos() {
//        if (Utils.isNetworkConnected(getActivity())) {
//            Utils.initializeAndShow(getActivity());
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("UserId","2339");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_ARTIST_UPLOADED_IMAGES, this);
//        } else {
//            Utils.showToast(getActivity(), getResources().getString(R.string.err_no_internet));
//        }
//
//
//    }
//
//
//    @Override
//    public void callMethod() {
//        ((UploadActivity) getActivity()).openDialogForPic();
//    }
//
//    public void uploadImage(MultipartBody.Part fileToUpload, RequestBody mUSerId) {
//        Utils.initializeAndShow(getActivity());
//        ImageUploadClass.imageUpload(fileToUpload, mUSerId, null, Constants.FOR_UPLOAD_ARTIST_IMAGE, UploadPhotoFragment.this);
//    }
//
//    @OnClick(R.id.floatingButtonUploadPhoto)
//    public void onClick(View view) {
//        ((UploadActivity) getActivity()).openDialogForPic();
//    }
//
//    private void implementRecyclerViewClickListeners() {
//        recyclerPhotos.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerPhotos, new RecyclerClick_Listener() {
//            @Override
//            public void onClick(View view, int position) {
//                //If ActionMode not null select item
//                if (mActionMode != null)
//                    onListItemSelect(position);
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//                //Select item on long click
//
//                onListItemSelect(position);
//            }
//        }));
//    }
//
//    //List item select method
//    private void onListItemSelect(int position) {
//        uploadPhotosAdapter.toggleSelection(position);//Toggle the selection
//
//        boolean hasCheckedItems = uploadPhotosAdapter.getSelectedCount() > 0;//Check if any items are already selected or not
//
//
//                if (hasCheckedItems && mActionMode == null)
//            // there are some selected items, start the actionMode
//            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode_Callback(UploadPhotoFragment.this,uploadPhotosAdapter, arrayList, false));
//        else if (!hasCheckedItems && mActionMode != null)
//            // there no selected items, finish the actionMode
//            mActionMode.finish();
//
//        if (mActionMode != null)
//            //set action mode title on item selection
//            mActionMode.setTitle(String.valueOf(uploadPhotosAdapter
//                    .getSelectedCount()) + " selected");
//
//
//    }
//    //Set action mode null after use
//    public void setNullToActionMode() {
//        if (mActionMode != null)
//            mActionMode = null;
//
//
//
//    }
//
//    //Delete selected rows
//    public void deleteRows() {
//        SparseBooleanArray selected = uploadPhotosAdapter
//                .getSelectedIds();//Get selected ids
//
//        //Loop all selected ids
//        for (int i = (selected.size() - 1); i >= 0; i--) {
//            if (selected.valueAt(i)) {
//                //If current id is selected remove the item via key
//                arrayList.remove(selected.keyAt(i));
//                uploadPhotosAdapter.notifyDataSetChanged();//notify uploadPhotosAdapter
//
//            }
//        }
//        Toast.makeText(getActivity(), selected.size() + " item deleted.", Toast.LENGTH_SHORT).show();//Show Toast
//        mActionMode.finish();//Finish action mode after use
//
//    }
//
//}
