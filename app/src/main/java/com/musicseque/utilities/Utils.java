package com.musicseque.utilities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import com.musicseque.R;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;

public class Utils {


    static Dialog pDialog;

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    public static void initializeProgressDialog(Context context) {

        try {

            pDialog = new Dialog(context);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.setContentView(R.layout.progress_dialog_view);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ProgressWheel wheel = new ProgressWheel(context);
            wheel.setBarColor(Color.RED);
            pDialog.setCancelable(false);

        } catch (Exception e) {
            Log.e("show", "" + e);
        }

    }


    public static void hideProgressDialog() {

        if (pDialog != null && pDialog.isShowing()) {
            try {
                pDialog.dismiss();
                pDialog = null;
            } catch (Exception e) {
                Log.e("hide", "" + e);
            }
        }
    }


    public static void showProgressDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();


        } else {
            pDialog.show();
        }

    }


    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static String getEDitText(EditText etValue) {
        return etValue.getText().toString().trim();
    }
    public static String getTextView(TextView etValue) {
        return etValue.getText().toString().trim();
    }
    public static void showToast(Context context, String mValue) {
        Toast.makeText(context, mValue, Toast.LENGTH_LONG).show();
    }
    public static boolean isValidPassword(final String password) {


        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    public static void initializeAndShow(Context context)
    {
        initializeProgressDialog(context);
        showProgressDialog();
    }

public static void askPermissions(Activity activity)
{



    Dexter.withActivity(activity)
            .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        // do you work now
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied()) {
                        // permission is denied permenantly, navigate user to app settings
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    token.continuePermissionRequest();
                }
            })
            .onSameThread()
            .check();

}
     public static String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
    public static File saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }


public static void setTypefaces(CheckBox view, Context context)
{
    Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Noyhr-Extralight.otf");
    view.setTypeface(tf);
}


    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return (dist)*0.621371;
    }

    private static  double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    public static Address getCompleteAddressString(double LATITUDE, double LONGITUDE,Context context) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context.getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
             addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {

                String address=addresses.get(0).getAddressLine(0);

/*address= address.replace(addresses.get(0).getAdminArea(),"");
address= address.replace(addresses.get(0).getCountryName(),"");
address=address.replace(addresses.get(0).getLocality(),"");
address=address.replace(addresses.get(0).getPostalCode(),"");*/
                String []a=address.split(",");
                address="";
                String society="";
                address=a[0];
                for(int i=1;i<a.length;i++)
                {
                    if(a[i].trim().length()>1) {
                        society+= a[i].toString() + ", ";
                    }
                }
                if(society.trim().endsWith(","))
                {
                    society= society.substring(0,society.lastIndexOf(",")).trim();
                }
                String city ="";
                if(!isEmpty(addresses.get(0).getLocality())) {
                    city=addresses.get(0).getLocality().trim();
                }
                String zip ="";
                if(!isEmpty(addresses.get(0).getPostalCode())) {
                    zip = addresses.get(0).getPostalCode().trim();
                }



            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return addresses.get(0);
    }
        }
