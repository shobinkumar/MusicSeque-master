package com.musicseque.utilities;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.musicseque.R;
import com.musicseque.start_up.LoginActivity;

public class CommonMethods {

    public static  void showLargeImages(Context context,String url)
    {
        Dialog dialogImages = new Dialog(context,R.style.CustomDialog);
        dialogImages.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogImages.setContentView(R.layout.dialog_show_image);


        ImageView ivDelete=(ImageView)dialogImages.findViewById(R.id.ivDelete);
        ImageView ivImage=(ImageView)dialogImages.findViewById(R.id.ivImage);
        ProgressBar progressBar=(ProgressBar)dialogImages.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        Glide.with(context).load(url).into(ivImage);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogImages.dismiss();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogImages.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialogImages.getWindow().setAttributes(lp);

        dialogImages.show();
    }

}
