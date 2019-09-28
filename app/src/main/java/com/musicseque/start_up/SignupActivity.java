package com.musicseque.start_up;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.musicseque.Fonts.NoyhrEditText;
import com.musicseque.R;

import com.musicseque.interfaces.MyInterface;
import com.musicseque.models.CountryModel;
import com.musicseque.retrofit_interface.RetrofitAPI;
import com.musicseque.utilities.Constants;
import com.musicseque.utilities.SharedPref;
import com.musicseque.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class   SignupActivity extends Activity implements View.OnClickListener, MyInterface, TextWatcher {

    private String requestBody = "", firstName = "", lstName = "", email = "", phone = "", pasw = "", confPasw = "", proType;
    CheckBox radioBtn;
    NoyhrEditText et_firstName;
    private EditText et_lastname, et_email, et_phone, et_pasw, et_conf_pasw;
    private TextView pro_type, tv_sign_up;
    String[] userType = {"Artist","Talent Manager", "Event Manager", "Music Lover", "Venue", "Store"};
    private RelativeLayout rel8;
    private ListView list;
    private String profileType = "";
    ImageView ivBack;
    TextView tvName;
    TextView tvTerms;
    private String prevString;
    String mCountryCode="";


//    SharedPreferences sharedPreferences;
//    SharedPreferences.Editor editor;
//    private RetrofitComponent retrofitComponent;
    private Spinner  spinnerCountryCode;
    private ArrayList<String>  countryCodeAL=new ArrayList<>();
    private ArrayList<CountryModel> countryAL=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initOtherViews();
        initViews();
        clickListeners();
        getCountriesList();
        Utils.setTypefaces(radioBtn,SignupActivity.this);
    }





    private void initOtherViews() {

//        retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(new SharedPrefDependency(getApplicationContext())).build();
//        sharedPreferences = retrofitComponent.getShared();
//        editor = retrofitComponent.getEditor();
    }

    private void initViews() {

        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvName = (TextView) findViewById(R.id.tvName);

        et_firstName = (NoyhrEditText) findViewById(R.id.et_firstName);
        et_lastname = (EditText) findViewById(R.id.et_lastname);
        et_email = (EditText) findViewById(R.id.et_email);
        spinnerCountryCode=(Spinner)findViewById(R.id.spinnerCountryCode);

        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pasw = (EditText) findViewById(R.id.et_pasw);
        et_conf_pasw = (EditText) findViewById(R.id.et_conf_pasw);
        pro_type = (TextView) findViewById(R.id.pro_type);
        tv_sign_up = (TextView) findViewById(R.id.tv_sign_up);
        tvTerms = (TextView) findViewById(R.id.tvTerms);

        list = (ListView) findViewById(R.id.list2);
        rel8 = (RelativeLayout) findViewById(R.id.rel8);
        radioBtn = (CheckBox) findViewById(R.id.radioBtn);


        tvTerms.setLinkTextColor(Color.WHITE); // default link color for clickable span, we can also set it in xml by android:textColorLink=""
        ClickableSpan normalLinkClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://musicsegue.com/TermsandConditions.html"));
                startActivity(browserIntent);            }

        };

        ClickableSpan noUnderLineClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://musicsegue.com/Privacy%20Policy.html"));
                startActivity(browserIntent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(Color.WHITE); // specific color for this link
            }
        };



        makeLinks(tvTerms, new String[] {
                "Terms of Use", "Privacy Policy"
        }, new ClickableSpan[] {
                normalLinkClickSpan, noUnderLineClickSpan
        });



//        et_firstName.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
//        et_lastname.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
//        et_email.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        tvName.setText("Create Account");


        et_firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();

                if (string.equals(prevString)) {
                    return;
                } else if (string.length() == 0)
                    return;
                    // 1st character
                else if (string.length() == 1) {
                    prevString = string.toUpperCase();
                    et_firstName.setText(string.toUpperCase());
                    et_firstName.setSelection(string.length());
                }
                // if the last entered character is after a space
                else if (string.length() > 0 && string.charAt(string.length() - 2) == ' ') {
                    string = string.substring(0, string.length() - 1) + Character.toUpperCase(string.charAt(string.length() - 1));
                    prevString = string;
                    et_firstName.setText(string);
                    et_firstName.setSelection(string.length());
                }
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, userType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {


                if(userType[pos].equals("Artist") || userType[pos].equals("Venue"))
                {
                    pro_type.setText(userType[pos]);
                    list.setVisibility(View.GONE);
                    if(pos==0)
                        profileType = String.valueOf((pos + 1));
                    else
                        profileType = String.valueOf((pos + 2));
                }
                else
                {
                    Utils.showToast(SignupActivity.this,"You can select only artist and venue for now");
                }



            }
        });


    }

    private void clickListeners() {
        tv_sign_up.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        rel8.setOnClickListener(this);
        et_firstName.addTextChangedListener(this);
        et_lastname.addTextChangedListener(this);

        et_email.setFilters(new InputFilter[]{
                new InputFilter.AllCaps() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        return String.valueOf(source).toLowerCase();
                    }
                }
        });

    }
    private void getCountriesList() {
        if (Utils.isNetworkConnected(SignupActivity.this)) {
            initializeLoader();
            RetrofitAPI.callGetAPI(Constants.FOR_COUNTRIES_LIST, SignupActivity.this);

        } else {
            Utils.showToast(SignupActivity.this, getResources().getString(R.string.err_no_internet));
        }

    }

    @Override
    public void onClick(View view) {
        if (view == tv_sign_up) {

            firstName = et_firstName.getText().toString();
            lstName = et_lastname.getText().toString();
            email = et_email.getText().toString().trim();
            phone = et_phone.getText().toString();
            pasw = et_pasw.getText().toString().trim();
            confPasw = et_conf_pasw.getText().toString().trim();
            proType = pro_type.getText().toString();

//            if (firstName.equals("") || lstName.equals("") || email.equals("") || phone.equals("") || pasw.equals("") || confPasw.equals("")) {
//
//                Utils.showToast(this, getResources().getString(R.string.err_fields_empty));
//            }
//
//            else

            if (firstName.equalsIgnoreCase("")) {
                Utils.showToast(this, getResources().getString(R.string.err_first_name));

            } else if (lstName.equalsIgnoreCase("")) {
                Utils.showToast(this, getResources().getString(R.string.err_last_name));

            } else if (email.equalsIgnoreCase("")) {
                Utils.showToast(this, getResources().getString(R.string.err_email_id));

            } else if (!Utils.emailValidator(email)) {
                Utils.showToast(this, getResources().getString(R.string.err_email_invalid));

            } else if (phone.length() == 0) {
                Utils.showToast(this, getResources().getString(R.string.err_phone_empty));

            } else if (phone.length() < 10) {
                Utils.showToast(this, getResources().getString(R.string.err_phone));

            } else if (pasw.length() == 0) {
                Utils.showToast(this, getResources().getString(R.string.err_password_empty));
            } else if (pasw.length() < 6 || !Utils.isValidPassword(pasw)) {
                Utils.showToast(SignupActivity.this, getResources().getString(R.string.err_password));
            } else if (confPasw.length() == 0) {
                Utils.showToast(this, getResources().getString(R.string.err_confirm_password_empty));


            } else if (!pasw.equals(confPasw)) {
                Utils.showToast(this, getResources().getString(R.string.err_password_not_match));


            } else if (!radioBtn.isChecked()) {
                Utils.showToast(SignupActivity.this, getResources().getString(R.string.err_terms));
            } else if (profileType.equalsIgnoreCase("")) {
                Utils.showToast(this, getResources().getString(R.string.err_user_type));

            } else {


                hitAPI();

            }
        } else if (view == rel8) {
            if (list.getVisibility() == View.VISIBLE) {
                // Its visible
                list.setVisibility(View.GONE);

            } else {
                // Either gone or invisible
                list.setVisibility(View.VISIBLE);
            }
        } else if (view == ivBack) {
            finish();
        }

    }

    private void hitAPI() {
        if (Utils.isNetworkConnected(SignupActivity.this)) {
            initializeLoader();
            try {
                SharedPref.putString(Constants.EMAIL_ID, email);
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("UserName",email);
                jsonBody.put("Password",pasw);
                jsonBody.put("FirstName",firstName);
                jsonBody.put("LastName",lstName);
                jsonBody.put("Phone",phone);
                jsonBody.put("ProfileTypeId",profileType);
                jsonBody.put("CountryId",mCountryCode);
                jsonBody.put("OTPType","Email verification");
                //  jsonBody.put("SocialImageUrl", "");
                requestBody = jsonBody.toString();
                Log.e("tag", "request is  " + requestBody);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RetrofitAPI.callAPI(requestBody, Constants.FOR_SIGNUP, SignupActivity.this);
        } else {
            Utils.showToast(SignupActivity.this, getResources().getString(R.string.err_no_internet));
        }


    }

    @Override
    public void sendResponse(Object response, int TYPE) {
        Utils.hideProgressDialog();
        try {

            if(TYPE==Constants.FOR_SIGNUP)
            {
                JSONObject obj = new JSONObject(response.toString());
                String tracking = obj.getString("Status");
                String Message = obj.getString("Message");
                Log.e("tag", "status is " + tracking);
                if (tracking.equals("Success")) {

                    Utils.showToast(SignupActivity.this, getResources().getString(R.string.txt_please_verify));
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    intent.putExtra("isEmailVerified",false);
                    startActivity(intent);
                    finish();

                } else {
                    Utils.showToast(SignupActivity.this, "" + Message);
                }
            }
          else if(TYPE==Constants.FOR_COUNTRIES_LIST)
            {
                try {
                    JSONArray jsonArray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CountryModel model = new CountryModel();
                        model.setCountryId(jsonObject.getString("CountryId"));
                        model.setCountryName(jsonObject.getString("CountryName"));
                        model.setCountryCode(jsonObject.getString("CountryCode"));

                        countryCodeAL.add(jsonObject.getString("CountryCode"));
                        countryAL.add(model);
                    }


                    String[] arrCountryCode = countryCodeAL.toArray(new String[countryCodeAL.size()]);

//
//                    ArrayAdapter<String> adapterCode =
//                            new ArrayAdapter<String>(SignupActivity.this, R.layout.spinner_item_country_code, arrCountryCode);
//                    spinnerCountryCode.setAdapter(adapterCode);

                    ArrayAdapter<String> adapterCode =
                            new ArrayAdapter<String>(SignupActivity.this, R.layout.spinner_item_country_code, arrCountryCode);
                    spinnerCountryCode.setAdapter(adapterCode);
                spinnerCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mCountryCode=countryAL.get(position).getCountryId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }


    }


    void initializeLoader() {
        Utils.initializeAndShow(SignupActivity.this);

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (et_firstName.hasFocus()) {
            makeFirstLetterCapital(s.toString(), et_firstName);
        } else if (et_lastname.hasFocus()) {
            makeFirstLetterCapital(s.toString(), et_lastname);
        }

    }

    void makeFirstLetterCapital(String str, EditText editText) {
        String string = str.toString();
        Log.d("STRING", string + " " + prevString);
        if (string.equals(prevString)) {
            return;
        } else if (string.length() == 0)
            return;
            // 1st character
        else if (string.length() == 1) {
            prevString = string.toUpperCase();
            editText.setText(string.toUpperCase());
            editText.setSelection(string.length());
        }
        // if the last entered character is after a space
        else if (string.length() > 0 && string.charAt(string.length() - 2) == ' ') {
            string = string.substring(0, string.length() - 1) + Character.toUpperCase(string.charAt(string.length() - 1));
            prevString = string;
            editText.setText(string);
            editText.setSelection(string.length());
        }
    }


    public void makeLinks(TextView textView, String[] links, ClickableSpan[] clickableSpans) {
        SpannableString spannableString = new SpannableString(textView.getText());
        for (int i = 0; i < links.length; i++) {
            ClickableSpan clickableSpan = clickableSpans[i];
            String link = links[i];

            int startIndexOfLink = textView.getText().toString().indexOf(link);
            spannableString.setSpan(clickableSpan, startIndexOfLink,
                    startIndexOfLink + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setHighlightColor(
                Color.TRANSPARENT); // prevent TextView change background when highlight
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }



}
