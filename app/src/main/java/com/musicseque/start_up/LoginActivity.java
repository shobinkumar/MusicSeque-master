package com.musicseque.start_up;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
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
import com.musicseque.dagger_data.DaggerRetrofitComponent;
import com.musicseque.dagger_data.RetrofitComponent;
import com.musicseque.dagger_data.SharedPrefDependency;
import com.musicseque.event_manager.activity.MainActivityEventManager;
import com.musicseque.interfaces.MyInterface;
import com.musicseque.retrofit_interface.RetrofitAPI;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends Activity implements View.OnClickListener, MyInterface {
    private static final int RC_SIGN_IN = 1;
    private static final int FB_SIGN_IN = 2;

    boolean isFacebookEmail = true;


    EditText et_username, et_password;
    private TextView tvForgotPassword;
    ImageView iv_fb, ivGoogle;
    Button btn_login, btn_signup;

    ProgressDialog progressDialog;
    private String userName = "", password = "";
    ;

    String mName = "", mEmail = "", mSocialId = "", mImageURL = "";

    GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private RetrofitComponent retrofitComponent;
    private String mProfileType = "";
    private FirebaseAuth mAuth;
    private Dialog dialog, dialogUserType;
    private Dialog dialogReset;
    private Dialog dialogVerifyAccount;

    @BindView(R.id.cbRememberMe)
    CheckBox cbRememberMe;

    String mToken = "";

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


        if (sharedPreferences.getBoolean(Constants.IS_REMEMBER, false)) {
            et_username.setText(sharedPreferences.getString(Constants.EMAIL_ID, ""));
            et_password.setText(sharedPreferences.getString(Constants.PASSWORD, ""));
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
        retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getApplicationContext())).build();
        sharedPreferences = retrofitComponent.getShared();
        editor = retrofitComponent.getEditor();

    }

    private void initViews() {

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        iv_fb = (ImageView) findViewById(R.id.iv_fb);
        ivGoogle = (ImageView) findViewById(R.id.ivGoogle);

//        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//            @Override
//            public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                mToken = task.getResult().getToken();
//            }
//        });


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
                    editor.putBoolean(Constants.IS_REMEMBER, true).commit();
                } else {
                    editor.putBoolean(Constants.IS_REMEMBER, false).commit();
                }
            }
        });


    }

    private void googleLoginInitialization() {

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // .requestIdToken("631151135806-ruoi38mhngnuciop3u6bqbkdb0dc31r8.apps.googleusercontent.com")
                // .requestIdToken("797258071407-u5o6ivco8p7qukqm8s6ovbnljns3djq4.apps.googleusercontent.com")
                // .requestIdToken("631151135806-76c34v9bb1486bqb0itjui37iusr19kd.apps.googleusercontent.com")

                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);


    }

    private void checkAccountVerified() {


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
            case R.id.btn_login:
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
                        editor.putString(Constants.EMAIL_ID, userName).commit();
                        editor.putString(Constants.PASSWORD, password).commit();
                        try {
                            jsonBody.put("UserName", userName);
                            jsonBody.put("Password", password);
                            jsonBody.put("PopToken", mToken);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        RetrofitAPI.callAPI(jsonBody.toString(), Constants.FOR_LOGIN, LoginActivity.this);


                    } else {
                        Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_no_internet));
                    }


                }

                break;
            case R.id.btn_signup:
                // Utils.showProgressDialog();
                Intent intent = new Intent(this, SignupActivity.class);
                startActivity(intent);

                break;

            case R.id.iv_fb:
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

//    @OnClick({R.id.cbRememberMe})
//    public void click(View view)
//    {
//        switch (view.getId())
//        {
//            case R.id.cbRememberMe:
//
//
//
//        }
//    }

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


        //   mAuth.signOut();

        // Google sign out
//        googleSignInClient.signOut().addOnCompleteListener(this,
//                new OnCompleteListener<Void>() {  //signout Google
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        //     FirebaseAuth.getInstance().signOut(); //signout firebase
//
//                    }
//                });


//        Intent signInIntent = googleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
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
            jsonBody.put("PopToken", mToken);
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
                        editor.putString(Constants.USER_NAME, obj.getString("FirstName") + " " + obj.getString("LastName")).commit();
                        editor.putString(Constants.USER_ID, obj.getString("Id")).commit();
                        editor.putString(Constants.PROFILE_TYPE, obj.getString("ProfileTypeName")).commit();
                        //  editor.putString(Constants.PROFILE_ID, obj.getString("ProfileTypeId")).commit();
                        editor.putString(Constants.EMAIL_ID, obj.getString("Email")).commit();

                        if (obj.getString("ProfilePic").equalsIgnoreCase("")) {
                            editor.putString(Constants.PROFILE_IMAGE, "").commit();
                        } else {
                            editor.putString(Constants.PROFILE_IMAGE, obj.getString("ImgUrl") + obj.getString("ProfilePic")).commit();
                        }
                        editor.putString(Constants.COUNTRY_CODE, obj.getString("CountryCode")).commit();
                        editor.putString(Constants.MOBILE_NUMBER, obj.getString("Phone")).commit();
                        editor.putString(Constants.COUNTRY_NAME, obj.getString("CountryName")).commit();
                        editor.putString(Constants.COUNTRY_ID, obj.getString("CountryId")).commit();
                        editor.putString(Constants.LOGIN_TYPE, "Simple").commit();
                        editor.putString(Constants.IS_FIRST_LOGIN, obj.getString("IsFirstLogin")).commit();
                        editor.putString(Constants.VISIBILITY_STATUS, obj.getString("NewStatus")).commit();
                        editor.putBoolean(Constants.IS_REMEMBER, true).commit();
                        try {
                            editor.putString(Constants.UNIQUE_CODE, obj.getString("UniqueCode")).commit();
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
                        } else if (obj.getString("ProfileTypeName").equalsIgnoreCase("EventManager")) {
                            Intent intent;
                            if (obj.getString("IsFirstLogin").equalsIgnoreCase("Y"))
                                intent = new Intent(LoginActivity.this, MainActivityEventManager.class).putExtra("frag", "com.musicseque.event_manager.fragment.EventManagerFormFragment");
                            else
                                intent = new Intent(LoginActivity.this, MainActivityEventManager.class);

                            startActivity(intent);
                            finish();
                        }
                        else if(obj.getString("ProfileTypeName").equalsIgnoreCase("VenueManager"))
                        {
                            Intent intent;
                            if (obj.getString("IsFirstLogin").equalsIgnoreCase("Y"))
                                intent = new Intent(LoginActivity.this, MainActivity.class).putExtra("frag", "com.musicseque.venue_manager.fragment.CreateVenueFragment");
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
                            editor.putString(Constants.USER_ID, jsonObject.getString("Id")).commit();

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
                                            editor.putString(Constants.EMAIL_ID, mEmail).commit();
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

                            editor.putString(Constants.USER_NAME, jsonObject.getString("FirstName")).commit();
                            editor.putString(Constants.USER_ID, jsonObject.getString("Id")).commit();
                            //  editor.putString(Constants.PROFILE_TYPE, jsonObject.getString("ProfileTypeName")).commit();
                            editor.putString(Constants.PROFILE_ID, jsonObject.getString("ProfileTypeId")).commit();
                            editor.putString(Constants.EMAIL_ID, jsonObject.getString("UserName")).commit();
                            editor.putString(Constants.PROFILE_IMAGE, jsonObject.getString("SocialImageUrl")).commit();
                            editor.putString(Constants.PROFILE_TYPE, jsonObject.getString("ProfileTypeName")).commit();
                            editor.putString(Constants.LOGIN_TYPE, "Social").commit();
                            editor.putString(Constants.VISIBILITY_STATUS, jsonObject.getString("NewStatus")).commit();
                            editor.putString(Constants.IS_FIRST_LOGIN, jsonObject.getString("IsFirstLogin")).commit();
                            editor.putBoolean(Constants.IS_REMEMBER, false).commit();
                            try {
                                editor.putString(Constants.UNIQUE_CODE, jsonObject.getString("UniqueCode")).commit();
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
                        editor.putString(Constants.USER_ID, jsonObject.getString("Id")).commit();
                        editor.putString(Constants.PROFILE_TYPE, jsonObject.getString("ProfileTypeName")).commit();
                        editor.putString(Constants.PROFILE_ID, jsonObject.getString("ProfileTypeId")).commit();
                        editor.putString(Constants.PROFILE_IMAGE, jsonObject.getString("SocialImageUrl")).commit();
                        editor.putString(Constants.VISIBILITY_STATUS, jsonObject.getString("NewStatus")).commit();
                        editor.putString(Constants.IS_FIRST_LOGIN, jsonObject.getString("IsFirstLogin")).commit();
                        editor.putBoolean(Constants.IS_REMEMBER, false).commit();
                        try {
                            editor.putString(Constants.UNIQUE_CODE, jsonObject.getString("UniqueCode")).commit();
                        } catch (Exception e) {

                        }
                        editor.putString(Constants.LOGIN_TYPE, "Social").commit();

                        dialogUserType.dismiss();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class).putExtra("frag", "com.musicseque.artist.fragments.ProfileFragment");
                        startActivity(intent);
                        finish();
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
            case Constants.FOR_VERIFY_ACCOUNT:

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.getString("Status").equalsIgnoreCase("Success")) {
                        dialogVerifyAccount.dismiss();
                        //editor.putBoolean(Constants.IS_ACCOUNT_VERIFIED,true).commit();
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
                jsonBody.put("UserId", sharedPreferences.getString(Constants.USER_ID, ""));
                jsonBody.put("ProfileTypeId", sharedPreferences.getString(Constants.PROFILE_ID, ""));
                jsonBody.put("FirstName", sharedPreferences.getString(Constants.USER_NAME, ""));
                jsonBody.put("LastName", "");
                jsonBody.put("UserName", sharedPreferences.getString(Constants.EMAIL_ID, ""));
                jsonBody.put("SocialImageUrl", mImageURL);
                jsonBody.put("PopToken", mToken);

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


        etEmail.setText(sharedPreferences.getString(Constants.EMAIL_ID, ""));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, pType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pro_type.setText(pType[i]);
                list.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);


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

                            editor.putString(Constants.EMAIL_ID, Utils.getEDitText(etEmail)).commit();
                            editor.putString(Constants.PROFILE_ID, userTypeId(mProfileType)).commit();

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

                            editor.putString(Constants.EMAIL_ID, Utils.getEDitText(etEmail)).commit();
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
                            jsonBody.put("Email", sharedPreferences.getString(Constants.EMAIL_ID, ""));
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
        etEmail.setText(sharedPreferences.getString(Constants.EMAIL_ID, ""));


        btnResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkConnected(LoginActivity.this)) {
                    try {
                        initializeLoader();

                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("Email", Utils.getEDitText(etEmail));
                        // jsonBody.put("OTPType", "Email verification");

                        editor.putString(Constants.EMAIL_ID, Utils.getEDitText(etEmail)).commit();
                        String requestBody = jsonBody.toString();
                        RetrofitAPI.callAPI(requestBody, Constants.FOR_RESEND_OTP, LoginActivity.this);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Utils.showToast(LoginActivity.this, getResources().getString(R.string.err_no_internet));
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

                    if (Utils.isNetworkConnected(LoginActivity.this)) {
                        try {
                            initializeLoader();

                            editor.putString(Constants.EMAIL_ID, Utils.getEDitText(etEmail)).commit();
                            JSONObject jsonBody = new JSONObject();
                            jsonBody.put("Email", Utils.getEDitText(etEmail));
                            jsonBody.put("Otp", mOTP);


                            String requestBody = jsonBody.toString();
                            RetrofitAPI.callAPI(requestBody, Constants.FOR_VERIFY_ACCOUNT, LoginActivity.this);

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
                dialogVerifyAccount.dismiss();


            }


        });


        dialogVerifyAccount.show();
    }

    void saveDataInSharedPref(String name, String email) {
        editor.putString(Constants.USER_NAME, name).commit();
        editor.putString(Constants.EMAIL_ID, email).commit();

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


}

