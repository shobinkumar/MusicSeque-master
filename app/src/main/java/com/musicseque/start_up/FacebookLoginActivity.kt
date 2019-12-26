package com.musicseque.start_up

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.musicseque.R
import java.util.*

class FacebookLoginActivity : Activity() {
    var callbackManager: CallbackManager? = null
    private var accessTokenTracker: AccessTokenTracker? = null
    private val token: AccessToken? = null
    private var socialID = ""
    private var socialFirstName = ""
    private var socialLastName = ""
    private var socialemail = ""
    private var ImageUrl = ""
    private var name = ""
    var isEmail = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_login)
        FacebookSdk.sdkInitialize(applicationContext)
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS)
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this, PERMISSIONS)
    }

    override fun onResume() {
        super.onResume()
        LoginManager.getInstance().logOut()
        accessTokenTracker = object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(
                    oldAccessToken: AccessToken,
                    currentAccessToken: AccessToken) { // Set the access token using
// currentAccessToken when it's loaded or set.
                if (currentAccessToken == null) {
                    LoginManager.getInstance().logOut()
                    loginToFacebook()
                } else {
                    if (!currentAccessToken.isExpired) {
                        getFacebookResult(currentAccessToken)
                    }
                }
            }
        }
    }

    private fun loginToFacebook() { // TODO Auto-generated method stub
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) { // App code
                        getFacebookResult(loginResult.accessToken)
                    }

                    override fun onCancel() { // App code
                        finish()
                    }

                    override fun onError(exception: FacebookException) { // App code
                        LoginManager.getInstance().logOut()
                    }
                })
    }

    fun getFacebookResult(accessToken: AccessToken?) {
        val request = GraphRequest.newMeRequest(
                accessToken
        ) { `object`, response ->
            // Application code
            try {
                socialID = `object`.getString("id")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                name = `object`.getString("name")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                socialFirstName = `object`.getString("first_name")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                socialLastName = `object`.getString("last_name")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                if (`object`.has("email")) {
                    socialemail = `object`.getString("email")
                    isEmail = true
                } else {
                    socialemail = "$socialID@gmail.com"
                    isEmail = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val picObject = `object`.getJSONObject("picture")
                val dataObject = picObject.getJSONObject("data")
                ImageUrl = dataObject.getString("url")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            sendDataBackToLogin(true)
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,first_name,location,last_name,email,gender,birthday,picture.width(400).height(400)")
        request.parameters = parameters
        request.executeAsync()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data)
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        } else if (resultCode == RESULT_CANCELED) {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        accessTokenTracker!!.stopTracking()
    }

    private fun sendDataBackToLogin(isSuccess: Boolean) {
        val intent = Intent()
        if (isSuccess) {
            intent.putExtra("name", name)
            intent.putExtra("email", socialemail)
            intent.putExtra("social_id", socialID)
            intent.putExtra("img_url", ImageUrl)
            intent.putExtra("isEmail", isEmail)
            setResult(RESULT_OK, intent)
            finish()
        } else {
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

    companion object {
        private val PERMISSIONS = Arrays.asList("email")
    }
}