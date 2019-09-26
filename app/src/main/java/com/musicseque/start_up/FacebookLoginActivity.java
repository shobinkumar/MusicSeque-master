package com.musicseque.start_up;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.musicseque.MainActivity;
import com.musicseque.R;
import com.musicseque.utilities.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class FacebookLoginActivity extends Activity {
    private static final List<String> PERMISSIONS = Arrays.asList("email");
    CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken token;
    private String socialID = "";
    private String socialFirstName = "";
    private String socialLastName = "";
    private String socialemail = "";
    private String ImageUrl = "";
    private String name = "";
    boolean isEmail = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, PERMISSIONS);


    }

    @Override
    protected void onResume() {
        super.onResume();
        LoginManager.getInstance().logOut();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                if (currentAccessToken == null) {
                    LoginManager.getInstance().logOut();
                    loginToFacebook();
                } else {
                    if (!currentAccessToken.isExpired()) {
                        getFacebookResult(currentAccessToken);
                    }
                }
            }
        };
    }

    private void loginToFacebook() {
        // TODO Auto-generated method stub
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        getFacebookResult(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        finish();

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        LoginManager.getInstance().logOut();
                    }
                });
    }

    public void getFacebookResult(AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code

                        try {
                            socialID = object.getString("id");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            name = object.getString("name");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            socialFirstName = object.getString("first_name");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            socialLastName = object.getString("last_name");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (object.has("email")) {
                                socialemail = object.getString("email");
                                isEmail = true;

                            } else {
                                socialemail = socialID + "@gmail.com";
                                isEmail = false;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            JSONObject picObject = object.getJSONObject("picture");
                            JSONObject dataObject = picObject.getJSONObject("data");
                            ImageUrl = dataObject.getString("url");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        sendDataBackToLogin(true);
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,location,last_name,email,gender,birthday,picture.width(400).height(400)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (resultCode == RESULT_CANCELED) {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }


    private void sendDataBackToLogin(boolean isSuccess) {
        Intent intent = new Intent();

        if (isSuccess) {
            intent.putExtra("name", name);

            intent.putExtra("email", socialemail);
            intent.putExtra("social_id", socialID);
            intent.putExtra("img_url", ImageUrl);
            intent.putExtra("isEmail", isEmail);
            setResult(RESULT_OK, intent);
            finish();

        } else {
            setResult(RESULT_CANCELED, intent);
            finish();


        }

    }


}
















//        FacebookSdk.sdkInitialize(this);
//        callbackManager = CallbackManager.Factory.create();
//
//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(
//                    AccessToken oldAccessToken,
//                    AccessToken currentAccessToken) {
//
//
//                // Set the access token using
//                // currentAccessToken when it's loaded or set.
//            }
//        };
//
//
//        LoginManager.getInstance().logOut();
//        List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "public_profile", "user_friends");
//        callbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().logInWithReadPermissions(this, permissionNeeds);
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(com.facebook.login.LoginResult loginResult) {
//                token = loginResult.getAccessToken();
//                GraphRequest request = GraphRequest.newMeRequest
//                        (loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
//
//                                try {
//                                    socialID = object.getString("id");
//                                    name = object.getString("name");
//                                    if(object.has("email"))
//                                        {
//                                        socialemail=object.getString("email");
//
//                                    }
//                                    else
//                                    {
//                                           socialemail=socialID+"@gmail.com";
//                                    }
////                                    socialgender = object.getString("gender");
//                                    JSONObject picObject = object.getJSONObject("picture");
//                                    JSONObject dataObject = picObject.getJSONObject("data");
//                                    ImageUrl = dataObject.getString("url");
//                                    sendDataBackToLogin(true);
//
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                    Toast.makeText(getApplicationContext(), "Unable to connect with facebook", Toast.LENGTH_SHORT).show();
//
//                                } catch (Exception e) {
//                                    Toast.makeText(getApplicationContext(), "Unable to connect with facebook", Toast.LENGTH_SHORT).show();
//                                }
//
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,email,gender,birthday,picture.width(400).height(400)");
//                request.setParameters(parameters);
//                request.executeAsync();
//
//
//            }
//
//            @Override
//            public void onCancel() {
//                System.out.println("onCancel");
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                System.out.println("onError");
//                //    Log.v("LoginActivity", exception.getCause().toString());
//            }
//        });
//
//
//    }
//
//    private void sendDataBackToLogin(boolean isSuccess) {
//        Intent intent = new Intent();
//
//        if (isSuccess) {
//            intent.putExtra("name", name);
//
//            intent.putExtra("email", socialemail);
//            intent.putExtra("social_id", socialID);
//            intent.putExtra("img_url", ImageUrl);
//            setResult(RESULT_OK, intent);
//            finish();
//
//        } else
//        {
//            setResult(RESULT_CANCELED, intent);
//            finish();
//
//
//        }
//
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//
//        if(resultCode==RESULT_OK)
//        {
//            super.onActivityResult(requestCode, resultCode, data);
//            callbackManager.onActivityResult(requestCode, resultCode, data);
//        }
//        else if(resultCode==RESULT_CANCELED)
//        {
//            Intent intent = new Intent();
//            setResult(RESULT_CANCELED, intent);
//            finish();
//        }
//
//    }
//
//    private void getUserProfile(AccessToken currentAccessToken) {
//        GraphRequest request = GraphRequest.newMeRequest(
//                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        Log.d("TAG", object.toString());
//                        try {
//                            String first_name = object.getString("first_name");
//                            String last_name = object.getString("last_name");
//                            String email = object.getString("email");
//                            String id = object.getString("id");
//                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "first_name,last_name,email,id");
//        request.setParameters(parameters);
//        request.executeAsync();
//
//    }
//
//}
