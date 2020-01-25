package com.musicseque.start_up;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.preference.TwoStatePreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.musicseque.Fonts.NoyhrEditText;
import com.musicseque.MainActivity;
import com.musicseque.R;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.retrofit_interface.RetrofitAPI;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.SharedPref;
import com.musicseque.utilities.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.musicseque.utilities.Constants.FOR_RESEND_OTP;
import static com.musicseque.utilities.Constants.FOR_VERIFY_ACCOUNT;


public class LoginActivity extends Activity implements View.OnClickListener, MyInterface {
    private static final int RC_SIGN_IN = 1;
    private static final int FB_SIGN_IN = 2;

    boolean isFacebookEmail = true;


    EditText et_username, et_password;
    private TextView tvForgotPassword;
    ImageView iv_fb, ivGoogle;
    Button btn_login, btn_signup;

    private String userName = "", password = "";
    ;

    String mName = "", mEmail = "", mSocialId = "", mImageURL = "";

    GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;

    private String mProfileType = "";
    private FirebaseAuth mAuth;
    private Dialog dialog, dialogUserType;
    private Dialog dialogReset;
    private Dialog dialogVerifyAccount;



    String mToken = "";
    private CheckBox  cbRememberMe;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initOtherViews();
        initViews();
        listeners();
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        googleLoginInitialization();
        boolean mEmailVerified = getIntent().getBooleanExtra("isEmailVerified", true);
        if (mEmailVerified) {

        } else {
            dialogVerifyAccount();
        }


        if (SharedPref.getBoolean(Constants.IS_REMEMBER, false)) {
            et_username.setText(SharedPref.getString(Constants.EMAIL_ID, ""));
            et_password.setText(SharedPref.getString(Constants.PASSWORD, ""));
            cbRememberMe.setChecked(true);
        } else {
            et_username.setText("");
            et_password.setText("");
            cbRememberMe.setChecked(false);
        }


        // checkAccountVerified();
        // MyApplication.getInstance().getRandomUserApplicationComponent();


        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.musicseque", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

    }


    private void initOtherViews() {
        // Utils.initializeProgressDialog(this);
//        retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getApplicationContext())).build();
//        SharedPref = retrofitComponent.getShared();
//        SharedPref = retrofitComponent.getSharedPref();

    }

    private void initViews() {

        et_username = (EditText) findViewById(R.id.etEmailLogin);
        et_password = (EditText) findViewById(R.id.etPasswordLogin);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        btn_login = (Button) findViewById(R.id.btnLogin);
        btn_signup = (Button) findViewById(R.id.btnSignup);
        iv_fb = (ImageView) findViewById(R.id.ivFB);
        ivGoogle = (ImageView) findViewById(R.id.ivGoogle);
        cbRememberMe=(CheckBox)findViewById(R.id.cbRememberMe);
        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            // Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        mToken = task.getResult().getToken();

                        // Log and toast

                    }
                });


    }

    private void listeners() {
        tvForgotPassword.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        iv_fb.setOnClickListener(this);
        ivGoogle.setOnClickListener(this);
        cbRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SharedPref.putBoolean(Constants.IS_REMEMBER, true);
                } else {
                    SharedPref.putBoolean(Constants.IS_REMEMBER, false);
                }
            }
        });


    }

    private void googleLoginInitialization() {

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)


                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);


    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        initializeLoader();

        try {

            mName = acct.getDisplayName();
            mEmail = acct.getEmail();
            try {
                if (acct.getPhotoUrl().toString() == null) {
                    mImageURL = "";
                } else {
                    mImageURL = acct.getPhotoUrl().toString();
                }
            } catch (Exception e) {
                mImageURL = "";
            }


            mSocialId = acct.getId();
            saveDataInSharedPref(mName, mEmail);

            //Check signup or login
            checkAccountExists(mEmail, mSocialId, Constants.FOR_GOOGLE);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }


//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = mAuth.getCurrentUser();
//
//                            mName = user.getDisplayName();
//                            mEmail = user.getEmail();
//                            mImageURL = user.getPhotoUrl().toString();
//                            mSocialId = user.getUid();
//                            saveDataInSharedPref(mName, mEmail);
//
//                            //Check signup or login
//                            checkAccountExists(mEmail, mSocialId, Constants.FOR_GOOGLE);
//
//
//                            ;
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
//                        }
//
//
//                    }
//                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvForgotPassword:

                dialogForgot();

                break;
            case R.id.btnLogin:
                userName = et_username.getText().toString();
                password = et_password.getText().toString();

                if (userName.equals("")) {
                    Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_email_id));
                } else if (!Utils.emailValidator(userName)) {
                    Utils.showToast(this, getResources().getString(R.string.err_email_invalid));

                } else if (password.equalsIgnoreCase("")) {
                    Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_password_empty));

                } else if (password.length() < 6 || !Utils.isValidPassword(password)) {
                    Utils.showToast(LoginActivity.this, "Password is invalid.");
                } else {
                    if (Utils.isNetworkConnected(LoginActivity.this)) {
                        initializeLoader();
                        // showProgressDialog();
                        JSONObject jsonBody = new JSONObject();
                        SharedPref.putString(Constants.EMAIL_ID, userName);
                        SharedPref.putString(Constants.PASSWORD, password);
                        try {

                            jsonBody.put("UserName", userName);
                            jsonBody.put("Password", password);
                            jsonBody.put("PopToken", FirebaseInstanceId.getInstance().getToken());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        RetrofitAPI.callAPI(jsonBody.toString(), Constants.FOR_LOGIN, LoginActivity.this);


                    } else {
                        Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_no_internet));
                    }


                }

                break;
            case R.id.btnSignup:
                // Utils.showProgressDialog();
                Intent intent = new Intent(this, SignupActivity.class);
                startActivity(intent);

                break;

            case R.id.ivFB:
                if (Utils.isNetworkConnected(this)) {
                    initializeLoader();

                    Intent intent1 = new Intent(LoginActivity.this, FacebookLoginActivity.class);
                    startActivityForResult(intent1, FB_SIGN_IN);
                } else {
                    Utils.showToast(this, getResources().getString(R.string.err_no_internet));
                }

                break;
            case R.id.ivGoogle:
                if (Utils.isNetworkConnected(this)) {
                    signInGoogle();

                } else {
                    Utils.showToast(this, getResources().getString(R.string.err_no_internet));
                }


                break;

        }
    }


    private void signInGoogle() {
        //  FirebaseAuth.getInstance().signOut();


        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent signInIntent = googleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.hideProgressDialog();
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (Exception e) {
                Log.e("", e.getLocalizedMessage());
            }
        } else if (requestCode == FB_SIGN_IN) {
            if (resultCode == RESULT_OK) {

                mName = data.getStringExtra("name");
                mSocialId = data.getStringExtra("social_id");
                mImageURL = data.getStringExtra("img_url");
                isFacebookEmail = data.getBooleanExtra("isEmail", false);
                mEmail = data.getStringExtra("email");
                initializeLoader();
                saveDataInSharedPref(mName, mEmail);
                checkAccountExists(mEmail, mSocialId, Constants.FOR_FACEBOOK);
            }
        } else if (requestCode == RESULT_CANCELED) {
            Utils.hideProgressDialog();
        }
    }


    private void checkAccountExists(String mEmail, String mSocialId, String accountType) {

        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("SocialId", mSocialId);
            // jsonBody.put("email_id", mEmail);
            jsonBody.put("SocialType", accountType);
            jsonBody.put("PopToken", FirebaseInstanceId.getInstance().getToken());
            jsonBody.put("SocialImageUrl", mImageURL);
            String requestBody = jsonBody.toString();
            RetrofitAPI.callAPI(requestBody, Constants.FOR_ACCOUNT_EXISTS, LoginActivity.this);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    //Interface implementation to get Response from API
    @Override
    public void sendResponse(Object response, int TYPE) {
        Utils.hideProgressDialog();

        switch (TYPE) {
            case Constants.FOR_LOGIN:
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    String tracking = obj.getString("Status");
                    String Message = obj.getString("Message");


                    Log.e("tag", "status is " + tracking);
                    if (tracking.equalsIgnoreCase("Success")) {


                        String ProfileTypeName = obj.getString("ProfileTypeName");
                        String Id = obj.getString("Id");
                        SharedPref.putString(Constants.USER_NAME, obj.getString("FirstName") + " " + obj.getString("LastName"));
                        SharedPref.putString(Constants.USER_ID, obj.getString("Id"));
                        SharedPref.putString(Constants.PROFILE_TYPE, obj.getString("ProfileTypeName"));
                        //  SharedPref.putString(Constants.PROFILE_ID, obj.getString("ProfileTypeId"));
                        SharedPref.putString(Constants.EMAIL_ID, obj.getString("Email"));

                        if (obj.getString("ProfilePic").equalsIgnoreCase("")) {
                            SharedPref.putString(Constants.PROFILE_IMAGE, "");
                        } else {
                            SharedPref.putString(Constants.PROFILE_IMAGE, obj.getString("ImgUrl") + obj.getString("ProfilePic"));
                        }
                        SharedPref.putString(Constants.COUNTRY_CODE, obj.getString("CountryCode"));
                        SharedPref.putString(Constants.MOBILE_NUMBER, obj.getString("Phone"));
                        SharedPref.putString(Constants.COUNTRY_NAME, obj.getString("CountryName"));
                        SharedPref.putString(Constants.COUNTRY_ID, obj.getString("CountryId"));
                        SharedPref.putString(Constants.LOGIN_TYPE, "Simple");
                        SharedPref.putString(Constants.IS_FIRST_LOGIN, obj.getString("IsFirstLogin"));
                        SharedPref.putString(Constants.VISIBILITY_STATUS, obj.getString("NewStatus"));
                        SharedPref.putBoolean(Constants.IS_REMEMBER, true);
                        try {
                            SharedPref.putString(Constants.UNIQUE_CODE, obj.getString("UniqueCode"));
                        } catch (Exception e) {

                        }

                        if (obj.getString("ProfileTypeName").equalsIgnoreCase("Artist")) {
                            Intent intent;

                            if (obj.getString("IsFirstLogin").equalsIgnoreCase("Y"))
                                intent = new Intent(LoginActivity.this, MainActivity.class).putExtra("frag", "com.musicseque.artist.fragments.ProfileFragment");
                            else
                                intent = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(intent);
                            finish();
                        } else if (obj.getString("ProfileTypeName").equalsIgnoreCase("Event Manager")) {
                            Intent intent;
                            if (obj.getString("IsFirstLogin").equalsIgnoreCase("Y"))
                                intent = new Intent(LoginActivity.this, MainActivity.class).putExtra("frag", "com.musicseque.event_manager.fragment.EventManagerFormFragment");
                            else
                                intent = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(intent);
                            finish();
                        } else if (obj.getString("ProfileTypeName").equalsIgnoreCase("Venue Manager")) {
                            Intent intent;
                            if (obj.getString("IsFirstLogin").equalsIgnoreCase("Y"))
                                intent = new Intent(LoginActivity.this, MainActivity.class).putExtra("frag", "com.musicseque.venue_manager.fragment.CreateVenueFragment");
                            else
                                intent = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(intent);
                            finish();
                        } else if (obj.getString("ProfileTypeName").equalsIgnoreCase("Music Lover")) {
                            Intent intent;
                            if (obj.getString("IsFirstLogin").equalsIgnoreCase("Y"))
                                intent = new Intent(LoginActivity.this, MainActivity.class).putExtra("frag", "com.musicseque.music_lover.fragments.FragmentProfileMusicLover");
                            else
                                intent = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(intent);
                            finish();
                        } else if (obj.getString("ProfileTypeName").equalsIgnoreCase("Event Manager")) {
                            Intent intent;
                            if (obj.getString("IsFirstLogin").equalsIgnoreCase("Y"))
                                intent = new Intent(LoginActivity.this, MainActivity.class).putExtra("frag", "com.musicseque.event_manager.fragment.EventManagerFormFragment");
                            else
                                intent = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(intent);
                            finish();
                        }

                    } else {
                        Utils.showToast(LoginActivity.this, "" + Message);
                        if (Message.contains("Error: Please Verify Email!!")) {
                            dialogVerifyAccount();
                        }
                    }

                    // Utils.showToast(LoginActivity.this,obj.getString("Message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case Constants.FOR_FORGOT_PASSWORD:
                try {

                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        dialog.dismiss();
                        dialogResetPassword();
                    } else {

                    }
                    Utils.showToast(LoginActivity.this, jsonObject.getString("Message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case Constants.FOR_RESEND_OTP:
                try {

                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {

//pawanpreetsingh
//        Pass_123
                    } else {

                    }
                    Utils.showToast(LoginActivity.this, jsonObject.getString("Message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case Constants.FOR_ACCOUNT_EXISTS:

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        if (jsonObject.getString("ProfileTypeId").equalsIgnoreCase("0")) {
                            SharedPref.putString(Constants.USER_ID, jsonObject.getString("Id"));

                            if (!isFacebookEmail) {
                                Dialog dialogEmail = new Dialog(LoginActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                                dialogEmail.setContentView(R.layout.dialog_email);
                                dialogEmail.setCancelable(false);
                                final NoyhrEditText etEmail = (NoyhrEditText) dialogEmail.findViewById(R.id.etEmail);
                                Button btnSubmit = (Button) dialogEmail.findViewById(R.id.btnSubmit);
                                btnSubmit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String email = etEmail.getText().toString().trim();

                                        if (email.equalsIgnoreCase("")) {
                                            Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_email_id));

                                        } else if (!Utils.emailValidator(email)) {
                                            Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_email_invalid));

                                        } else {
                                            mEmail = email;
                                            SharedPref.putString(Constants.EMAIL_ID, mEmail);
                                            dialogUserType();
                                            dialogEmail.dismiss();
                                        }
                                    }
                                });
                                dialogEmail.show();
                            } else {
                                dialogUserType();
                            }


                        } else {

                            SharedPref.putString(Constants.USER_NAME, jsonObject.getString("FirstName"));
                            SharedPref.putString(Constants.USER_ID, jsonObject.getString("Id"));
                            //  SharedPref.putString(Constants.PROFILE_TYPE, jsonObject.getString("ProfileTypeName"));
                            SharedPref.putString(Constants.PROFILE_ID, jsonObject.getString("ProfileTypeId"));
                            SharedPref.putString(Constants.EMAIL_ID, jsonObject.getString("UserName"));
                            SharedPref.putString(Constants.PROFILE_IMAGE, jsonObject.getString("SocialImageUrl"));
                            SharedPref.putString(Constants.PROFILE_TYPE, jsonObject.getString("ProfileTypeName"));
                            SharedPref.putString(Constants.LOGIN_TYPE, "Social");
                            SharedPref.putString(Constants.VISIBILITY_STATUS, jsonObject.getString("NewStatus"));
                            SharedPref.putString(Constants.IS_FIRST_LOGIN, jsonObject.getString("IsFirstLogin"));
                            SharedPref.putBoolean(Constants.IS_REMEMBER, false);
                            try {
                                SharedPref.putString(Constants.UNIQUE_CODE, jsonObject.getString("UniqueCode"));
                            } catch (Exception e) {

                            }

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();


                        }
                    } else {
                        Utils.showToast(LoginActivity.this, jsonObject.getString("Message"));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;
            case Constants.FOR_SOCIAL_LOGIN:

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        SharedPref.putString(Constants.USER_ID, jsonObject.getString("Id"));
                        SharedPref.putString(Constants.PROFILE_TYPE, jsonObject.getString("ProfileTypeName"));
                        SharedPref.putString(Constants.PROFILE_ID, jsonObject.getString("ProfileTypeId"));
                        SharedPref.putString(Constants.PROFILE_IMAGE, jsonObject.getString("SocialImageUrl"));
                        SharedPref.putString(Constants.VISIBILITY_STATUS, jsonObject.getString("NewStatus"));
                        SharedPref.putString(Constants.IS_FIRST_LOGIN, jsonObject.getString("IsFirstLogin"));
                        SharedPref.putBoolean(Constants.IS_REMEMBER, false);
                        try {
                            SharedPref.putString(Constants.UNIQUE_CODE, jsonObject.getString("UniqueCode"));
                        } catch (Exception e) {

                        }
                        SharedPref.putString(Constants.LOGIN_TYPE, "Social");

                        dialogUserType.dismiss();

                        if (jsonObject.getString("ProfileTypeName").equalsIgnoreCase("Artist")) {
                            Intent intent;

                            if (jsonObject.getString("IsFirstLogin").equalsIgnoreCase("Y"))
                                intent = new Intent(LoginActivity.this, MainActivity.class).putExtra("frag", "com.musicseque.artist.fragments.ProfileFragment");
                            else
                                intent = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(intent);
                            finish();
                        } else if (jsonObject.getString("ProfileTypeName").equalsIgnoreCase("Venue Manager")) {
                            Intent intent;
                            if (jsonObject.getString("IsFirstLogin").equalsIgnoreCase("Y"))
                                intent = new Intent(LoginActivity.this, MainActivity.class).putExtra("frag", "com.musicseque.venue_manager.fragment.CreateVenueFragment");
                            else
                                intent = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(intent);
                            finish();
                        } else if (jsonObject.getString("ProfileTypeName").equalsIgnoreCase("Event Manager")) {
                            Intent intent;
                            if (jsonObject.getString("IsFirstLogin").equalsIgnoreCase("Y"))
                                intent = new Intent(LoginActivity.this, MainActivity.class).putExtra("frag", "com.musicseque.event_manager.fragment.EventManagerFormFragment");
                            else
                                intent = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(intent);
                            finish();
                        } else if (jsonObject.getString("ProfileTypeName").equalsIgnoreCase("Music Lover")) {
                            Intent intent;
                            if (jsonObject.getString("IsFirstLogin").equalsIgnoreCase("Y"))
                                intent = new Intent(LoginActivity.this, MainActivity.class).putExtra("frag", "com.musicseque.music_lover.fragments.FragmentProfileMusicLover");
                            else
                                intent = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(intent);
                            finish();
                        }


                    } else {
                        Utils.showToast(LoginActivity.this, jsonObject.getString("Message"));
                        dialogUserType.dismiss();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case Constants.FOR_RESET_PASSWORD:
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        dialogReset.dismiss();

                    } else {

                    }
                    Utils.showToast(LoginActivity.this, jsonObject.getString("Message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case FOR_VERIFY_ACCOUNT:

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        dialogVerifyAccount.dismiss();
                        //SharedPref.putBoolean(Constants.IS_ACCOUNT_VERIFIED,true);
                    } else {

                    }
                    Utils.showToast(LoginActivity.this, jsonObject.getString("Message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private void hitSignUpAPI() {

        if (Utils.isNetworkConnected(LoginActivity.this)) {

//            checkProgressDialog();
//            showProgressDialog();
            initializeLoader();
            String requestBody = "";
            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("UserId", SharedPref.getString(Constants.USER_ID, ""));
                jsonBody.put("ProfileTypeId", SharedPref.getString(Constants.PROFILE_ID, ""));
                jsonBody.put("FirstName", SharedPref.getString(Constants.USER_NAME, ""));
                jsonBody.put("LastName", "");
                jsonBody.put("UserName", SharedPref.getString(Constants.EMAIL_ID, ""));
                jsonBody.put("SocialImageUrl", mImageURL);
                jsonBody.put("PopToken", FirebaseInstanceId.getInstance().getToken());
                jsonBody.put("StateId", "");
                jsonBody.put("CityId", "");
                jsonBody.put("UserAddress", "");
                jsonBody.put("PostCode", "");
                requestBody = jsonBody.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            RetrofitAPI.callAPI(requestBody, Constants.FOR_SOCIAL_LOGIN, LoginActivity.this);
        } else {
            Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_no_internet));
        }


    }

    private void checkProgressDialog() {
        // Utils.initializeProgressDialog(LoginActivity.this);

    }


    //If user does not exists then show dialog and asks the user type
    public void dialogUserType() {
        dialogUserType = new Dialog(LoginActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogUserType.setContentView(R.layout.dialog_user_type);
        dialogUserType.setCancelable(false);
        RelativeLayout rel8 = (RelativeLayout) dialogUserType.findViewById(R.id.rel8);
        final TextView pro_type = (TextView) dialogUserType.findViewById(R.id.pro_type);
        final Button btn_submit = (Button) dialogUserType.findViewById(R.id.btn_submit);
        final Button btnCancel = (Button) dialogUserType.findViewById(R.id.btnCancel);
        final EditText etEmail = (EditText) dialogUserType.findViewById(R.id.etEmail);
        final ListView list = (ListView) dialogUserType.findViewById(R.id.userTypeList);
        final CheckBox radioBtn = (CheckBox) dialogUserType.findViewById(R.id.radioBtn);
        final String[] pType = {"Artist", "Talent Manager", "Event Manager", "Music Lover", "Venue", "Store"};


        etEmail.setText(SharedPref.getString(Constants.EMAIL_ID, ""));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, pType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private String profileType = "";

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

//                if(pType[pos].equals("Artist") || pType[pos].equals("Venue") || pType[pos].equals("Music Lover"))
//                {
                pro_type.setText(pType[pos]);
                list.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                if (pos == 0) {
                    profileType = String.valueOf((pos + 1));
                } else {
                    profileType = String.valueOf((pos + 2));

                }
//                }
//                else
//                {
//                    Utils.showToast(LoginActivity.this,"You can select only artist and venue for now");
//                }


            }
        });

        rel8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.getVisibility() == View.VISIBLE) {
                    // Its visible
                    list.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);


                } else {
                    // Either gone or invisible
                    list.setVisibility(View.VISIBLE);
                    btn_submit.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.GONE);

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUserType.dismiss();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfileType = "";
                mProfileType = Utils.getTextView(pro_type);

                if (Utils.getEDitText(etEmail).equals("")) {
                    Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_email_id));


                } else if (!Utils.emailValidator(Utils.getEDitText(etEmail))) {
                    Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_email_invalid));

                } else if (mProfileType.equalsIgnoreCase("") || mProfileType.equalsIgnoreCase("Profile Type")) {
                    Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_user_type));

                } else if (!radioBtn.isChecked()) {
                    Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_terms));
                } else {

                    if (Utils.isNetworkConnected(LoginActivity.this)) {
                        try {

                            SharedPref.putString(Constants.EMAIL_ID, Utils.getEDitText(etEmail));
                            SharedPref.putString(Constants.PROFILE_ID, userTypeId(mProfileType));

                            hitSignUpAPI();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_no_internet));
                    }


                }


            }


        });


        dialogUserType.show();
    }


    //SHow dialog password dialog
    public void dialogForgot() {
        dialog = new Dialog(LoginActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_forgot_password);
        dialog.setCancelable(false);
        TextView change_pas = (TextView) dialog.findViewById(R.id.btn_reset_pas);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        final EditText etEmail = (EditText) dialog.findViewById(R.id.etEmail);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();


            }
        });
        change_pas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Utils.getEDitText(etEmail).equals("")) {
                    Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_email_id));


                } else if (!Utils.emailValidator(Utils.getEDitText(etEmail))) {
                    Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_email_invalid));

                } else {

                    if (Utils.isNetworkConnected(LoginActivity.this)) {
                        try {
                            initializeLoader();

                            JSONObject jsonBody = new JSONObject();
                            jsonBody.put("Email", Utils.getEDitText(etEmail));
                            jsonBody.put("OTPType", "ForgetPassword");

                            SharedPref.putString(Constants.EMAIL_ID, Utils.getEDitText(etEmail));
                            String requestBody = jsonBody.toString();
                            RetrofitAPI.callAPI(requestBody, Constants.FOR_FORGOT_PASSWORD, LoginActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_no_internet));
                    }


                }


            }


        });


        dialog.show();
    }

    //SHow dialog password dialog
    public void dialogResetPassword() {
        dialogReset = new Dialog(LoginActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogReset.setContentView(R.layout.dialog_reset_password);
        dialogReset.setCancelable(false);
        final NoyhrEditText etOTP = (NoyhrEditText) dialogReset.findViewById(R.id.etOTP);
        final NoyhrEditText etNewPassword = (NoyhrEditText) dialogReset.findViewById(R.id.etNewPassword);
        final NoyhrEditText etConfirmNewPassword = (NoyhrEditText) dialogReset.findViewById(R.id.etConfirmNewPassword);

        Button btnResetPassword = (Button) dialogReset.findViewById(R.id.btnResetPassword);
        Button btnCancel = (Button) dialogReset.findViewById(R.id.btnCancel);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mOTP = etOTP.getText().toString().trim();
                String pasw = etNewPassword.getText().toString().trim();
                String confPasw = etConfirmNewPassword.getText().toString().trim();

                if (Utils.getEDitText(etOTP).equals("")) {
                    Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_enter_otp));


                } else if (pasw.length() == 0) {
                    Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_password_empty));
                } else if (pasw.length() < 6 || !Utils.isValidPassword(pasw)) {
                    Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_password));
                } else if (confPasw.length() == 0) {
                    Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_confirm_password_empty));

                } else if (!pasw.equals(confPasw)) {
                    Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_password_not_match));


                } else {

                    if (Utils.isNetworkConnected(LoginActivity.this)) {
                        try {
                            initializeLoader();

                            JSONObject jsonBody = new JSONObject();
                            jsonBody.put("Email", SharedPref.getString(Constants.EMAIL_ID, ""));
                            jsonBody.put("OTP", mOTP);
                            jsonBody.put("Password", pasw);
                            jsonBody.put("OTPType", "PasswordReset");

                            String requestBody = jsonBody.toString();
                            RetrofitAPI.callAPI(requestBody, Constants.FOR_RESET_PASSWORD, LoginActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_no_internet));
                    }


                }


            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogReset.dismiss();


            }


        });


        dialogReset.show();
    }

    public void dialogVerifyAccount() {
        dialogVerifyAccount = new Dialog(LoginActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialogVerifyAccount.setContentView(R.layout.dialog_verify_account);
        dialogVerifyAccount.setCancelable(false);
        final NoyhrEditText etEmail = (NoyhrEditText) dialogVerifyAccount.findViewById(R.id.etEmail);
        final NoyhrEditText etOTP = (NoyhrEditText) dialogVerifyAccount.findViewById(R.id.etOTP);

        //  Noyhr btnChangeEmail = (Noyhr) dialogVerifyAccount.findViewById(R.id.btnChangeEmail);
        Button btnVerifyAccount = (Button) dialogVerifyAccount.findViewById(R.id.btnVerifyAccount);
        Button btnCancel = (Button) dialogVerifyAccount.findViewById(R.id.btnCancel);
        TextView btnResendOTP = (TextView) dialogVerifyAccount.findViewById(R.id.btnResendOTP);
        etEmail.setText(SharedPref.getString(Constants.EMAIL_ID, ""));


        btnResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("Email", Utils.getEDitText(etEmail));
                    // jsonBody.put("OTPType", "Email verification");

                    SharedPref.putString(Constants.EMAIL_ID, Utils.getEDitText(etEmail));
                    String requestBody = jsonBody.toString();
                    callAPI(FOR_RESEND_OTP, requestBody);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

//        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                etEmail.setEnabled(true);
//                etEmail.setText("");
//
//
//            }
//
//
//        });

        btnVerifyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mOTP = etOTP.getText().toString().trim();
                String mEmail = etEmail.getText().toString().trim();

                if (mEmail.equalsIgnoreCase("")) {
                    Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_email_id));

                } else if (!Utils.emailValidator(mEmail)) {
                    Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_email_invalid));

                } else if (Utils.getEDitText(etOTP).equals("")) {
                    Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_enter_otp));


                } else {

                    try {


                        SharedPref.putString(Constants.EMAIL_ID, Utils.getEDitText(etEmail));
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("Email", Utils.getEDitText(etEmail));
                        jsonBody.put("Otp", mOTP);


                        String requestBody = jsonBody.toString();
                        callAPI(FOR_VERIFY_ACCOUNT, requestBody);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogVerifyAccount.dismiss();


            }


        });


        dialogVerifyAccount.show();
    }

    void saveDataInSharedPref(String name, String email) {
        SharedPref.putString(Constants.USER_NAME, name);
        SharedPref.putString(Constants.EMAIL_ID, email);

    }


    String userTypeId(String user) {
        switch (user) {
            case "Artist":
                return "1";
            case "Talent Manager":
                return "3";

            case "Event Manager":
                return "4";
            case "Music Lover":
                return "5";
            case "Venue":
                return "6";
            case "Store":
                return "7";

            default:
                return "0";

        }
    }

    void initializeLoader() {
//        Utils.initializeProgressDialog(LoginActivity.this);
//        Utils.showProgressDialog();
        Utils.initializeAndShow(LoginActivity.this);

    }

    void callAPI(int type, String args) {
        if (Utils.isNetworkConnected(this)) {
            initializeLoader();
            if (type == FOR_VERIFY_ACCOUNT) {
                RetrofitAPI.callAPI(args, FOR_VERIFY_ACCOUNT, LoginActivity.this);

            } else if (type == FOR_RESEND_OTP) {
                RetrofitAPI.callAPI(args, Constants.FOR_RESEND_OTP, LoginActivity.this);

            }
        } else {
            Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_no_internet));
        }

    }

}
