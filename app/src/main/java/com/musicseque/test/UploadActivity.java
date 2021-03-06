//package com.musicseque.test;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.musicseque.R;
//import com.musicseque.activities.BaseActivity;
//import com.musicseque.artist.fragments.UploadPhotoFragment;
//
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//
//public class UploadActivity extends BaseActivity {
//
//
//    @BindView(R.id.llPhotos)
//    LinearLayout llPhotos;
//    @BindView(R.id.llAudio)
//    LinearLayout llAudio;
//    @BindView(R.id.llVideos)
//    LinearLayout llVideos;
//
//
//    @BindView(R.id.ivUploadPhoto)
//    ImageView ivUploadPhoto;
//    @BindView(R.id.ivUploadAudio)
//    ImageView ivUploadAudio;
//    @BindView(R.id.ivUploadVideo)
//    ImageView ivUploadVideo;
//
//    @BindView(R.id.img_first_icon)
//    ImageView img_first_icon;
//    @BindView(R.id.img_right_icon)
//    ImageView img_right_icon;
//    @BindView(R.id.tv_title)
//    TextView tv_title;
//    Fragment fragment;
//
//    @BindView(R.id.toolbarTest)
//    Toolbar toolbar;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload);
//        ButterKnife.bind(this);
//        initViews();
//        fragment = new UploadPhotoFragment();
//        openFragment(fragment);
//
//        setSupportActionBar(toolbar);
//        tv_title.setText("Upload");
//        img_right_icon.setVisibility(View.GONE);
//
//    }
//
//    private void openFragment(Fragment fragment) {
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.frameLayoutUpload, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//
//
//    }
//
//
//    private void initViews() {
//
//
//    }
//
//
//    @OnClick({R.id.llPhotos, R.id.llAudio, R.id.llVideos, R.id.img_first_icon})
//    public void onClick(View view) {
//        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayoutUpload);
//        switch (view.getId()) {
//            case R.id.llPhotos:
//                if (currentFragment instanceof UploadPhotoFragment) {
//
//                } else {
//                    fragment = new UploadPhotoFragment();
//                    openFragment(fragment);
//                    changeBackgroundColor(R.drawable.uploadphoto_active, R.drawable.uploadaudio, R.drawable.uploadvideo);
//                }
//
//                break;
//            case R.id.llAudio:
//                //  Utils.showToast(UploadActivity.this, "Coming soon");
//
//
//                break;
//
//            case R.id.llVideos:
//                //   Utils.showToast(UploadActivity.this, "Coming soon");
//
//                break;
//            case R.id.img_first_icon:
//                finish();
//                break;
//
//        }
//    }
//
//    private void changeBackgroundColor(int uploadphoto, int uploadaudio, int uploadvideo) {
//        ivUploadPhoto.setImageResource(uploadphoto);
//        ivUploadAudio.setImageResource(uploadaudio);
//        ivUploadVideo.setImageResource(uploadvideo);
//
//    }
//
//    public void openDialogForPic() {
//        openDialog("pic");
//    }
//
//
//    public void uploadPic(MultipartBody.Part fileToUpload, RequestBody mUSerId) {
//
//        UploadPhotoFragment uploadPhotoFragment = (UploadPhotoFragment) fragment;
//        uploadPhotoFragment.uploadImage(fileToUpload, mUSerId);
//        //
//
//
//    }
//}
