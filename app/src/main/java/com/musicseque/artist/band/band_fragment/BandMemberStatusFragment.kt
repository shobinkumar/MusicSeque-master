package com.musicseque.artist.band.band_fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.artist.band.band_activity.SearchBandMemberActivity
import com.musicseque.artist.band.band_adapter.BandMemberStatusAdapter
import com.musicseque.artist.band.band_model.BandMemberStatusModel
import com.musicseque.artist.fragments.BaseFragment
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.fragment_band_members.*
import kotlinx.android.synthetic.main.fragment_band_members.view.*
import org.json.JSONArray
import org.json.JSONObject

class BandMemberStatusFragment : BaseFragment(), MyInterface, View.OnClickListener {


    var alModel = ArrayList<BandMemberStatusModel>()
    var mBandId:String=""
    var result:String="{\"Status\":\"Success\",\"Message\":\"\",\"result\":[{\"SearchTypeValue\":null,\"BandId\":0,\"BandManagerId\":0,\"ArtistUserId\":2339,\"ArtistUserName\":\"shobinkumar25@gmail.com\",\"ArtistFirstName\":\"Shobin\",\"ArtistLastName\":\"Kumar\",\"ArtistProfileTypeId\":1,\"ArtistProfileTypeName\":\"Artist\",\"ArtistCountryCodeId\":\"1\",\"ArtistCountryCode\":\"+9311\",\"ArtistCountryName\":\"Afghanistan\",\"ArtistCity\":\"Trst\",\"ArtistGenreTypeName\":\"Rap\",\"ArtistProfilePic\":\"d4d5d59e-f82e-4a58-a3ab-5959ffffd947.jpg\",\"ArtistProfilePicServerPath\":\"http://musicapi.htistelecom.in/uploads/profileImages/\",\"ArtistSocialType\":\"\",\"ArtistSocialImageUrl\":\"\",\"ArtistNewStatus\":\"Available\",\"ArtistTypeId\":0,\"ArtistType\":\"\",\"ArtistEmail\":\"test@gmail.com\",\"ArtistPostCode\":\"test\",\"ArtistBio\":\"hdgxghxhxuhhhuxhhxhxuxuyhxhhzyxhktzitzurzufzitiizixgigxitxtxitixitxitxititzitxodyoydoydoyxiydiydxiyiysiysitzitziyztziitstsiiyzoxyodyoydoydodyodyodyy don't Yoda oyddyiitztizoyxoyxoyxoyxoxyyoxyixoydyxoyxiyoxyziyixoyxoyxoyxyoxyoxyodyodyodoydy don't want don't want your dad to see do I get you have to go in next truly a different direction my way back now we can your your contact detail about dinner ok sir your dad itixtiyoyxoydy don't want do do dinner done oydoyxoyxyixyiyiyiyixyixyixoyxoxyyoxoyxoy\",\"ArtistWebsite\":\"test\",\"ArtistExperienceId\":1,\"ArtistExperienceYear\":\"1-2 years\",\"ArtistDisplayName\":\"\",\"ArtistExpertise\":\"\",\"ArtistFollowers\":0,\"ArtistReviews\":0,\"ArtistLocationId\":587,\"ArtistUserLatitude\":\"30.7073169\",\"ArtistUserLongitude\":\"76.7039629\",\"ArtistLoginUserLatitude\":\"30.7074905\",\"ArtistLoginUserLongitude\":\"76.7040155\",\"ArtistIsBandMember\":\"P\"},{\"SearchTypeValue\":null,\"BandId\":0,\"BandManagerId\":0,\"ArtistUserId\":2367,\"ArtistUserName\":\"htiste71@gmail.com\",\"ArtistFirstName\":\"HTIS TELECOM\",\"ArtistLastName\":\"\",\"ArtistProfileTypeId\":1,\"ArtistProfileTypeName\":\"Artist\",\"ArtistCountryCodeId\":\"1\",\"ArtistCountryCode\":\"+9311\",\"ArtistCountryName\":\"Afghanistan\",\"ArtistCity\":\"Ludhiana\",\"ArtistGenreTypeName\":\"Rap\",\"ArtistProfilePic\":\"\",\"ArtistProfilePicServerPath\":\"http://musicapi.htistelecom.in/uploads/profileImages/\",\"ArtistSocialType\":\"G\",\"ArtistSocialImageUrl\":\"https://lh3.googleusercontent.com/-uZ29xsa1Buw/AAAAAAAAAAI/AAAAAAAAAAA/ACHi3rebvLArDmfmYHMKCFAuC-oBUpp6ow/s96-c/photo.jpg\",\"ArtistNewStatus\":\"Available\",\"ArtistTypeId\":0,\"ArtistType\":\"\",\"ArtistEmail\":\"htiste71@gmail.com\",\"ArtistPostCode\":\"141003\",\"ArtistBio\":\"I am the developer here. Hdhdfuuuffuufuufujfhfjfhfhfhfhhfhfhffhhfhhchfhhhhhhhhfhfdhhfhfhhfhfhhfhfhfhhfhfhfufufuufufuffuuffuhufufhffhhhfhfhfhhhffuuffuyfuffhfhfhfhhfhfhfhfhfhfhfhfhfhfufhffhhfhfhfhfghhfhfhhffhuffuufuffuuffugyufufffhhhffhfhfhfhfhhfhhdfhhgghhfhfhhfhhfhffhhhfhfhfhfhfhfhhhffhfhuf\",\"ArtistWebsite\":\"\",\"ArtistExperienceId\":1,\"ArtistExperienceYear\":\"1-2 years\",\"ArtistDisplayName\":\"\",\"ArtistExpertise\":\"Lead Vocals\",\"ArtistFollowers\":0,\"ArtistReviews\":0,\"ArtistLocationId\":266,\"ArtistUserLatitude\":\"30.707472\",\"ArtistUserLongitude\":\"76.7037831\",\"ArtistLoginUserLatitude\":\"30.7074905\",\"ArtistLoginUserLongitude\":\"76.7040155\",\"ArtistIsBandMember\":\"P\"},{\"SearchTypeValue\":null,\"BandId\":0,\"BandManagerId\":0,\"ArtistUserId\":2376,\"ArtistUserName\":\"shobinjindal91@gmail.com\",\"ArtistFirstName\":\"Shobin Jindal\",\"ArtistLastName\":\"\",\"ArtistProfileTypeId\":1,\"ArtistProfileTypeName\":\"Artist\",\"ArtistCountryCodeId\":\"0\",\"ArtistCountryCode\":\"\",\"ArtistCountryName\":\"\",\"ArtistCity\":\"\",\"ArtistGenreTypeName\":\"Rock\",\"ArtistProfilePic\":\"\",\"ArtistProfilePicServerPath\":\"http://musicapi.htistelecom.in/uploads/profileImages/\",\"ArtistSocialType\":\"G\",\"ArtistSocialImageUrl\":\"https://lh6.googleusercontent.com/-J-o_iI6gP48/AAAAAAAAAAI/AAAAAAAAEyc/zhTzDoc17i8/s96-c/photo.jpg\",\"ArtistNewStatus\":\"Available\",\"ArtistTypeId\":0,\"ArtistType\":\"\",\"ArtistEmail\":\"shobinjindal91@gmail.com\",\"ArtistPostCode\":\"\",\"ArtistBio\":\"\",\"ArtistWebsite\":\"\",\"ArtistExperienceId\":0,\"ArtistExperienceYear\":\"\",\"ArtistDisplayName\":\"\",\"ArtistExpertise\":\"Drums\",\"ArtistFollowers\":0,\"ArtistReviews\":0,\"ArtistLocationId\":137,\"ArtistUserLatitude\":\"30.7074429\",\"ArtistUserLongitude\":\"76.7038281\",\"ArtistLoginUserLatitude\":\"30.7074905\",\"ArtistLoginUserLongitude\":\"76.7040155\",\"ArtistIsBandMember\":\"P\"},{\"SearchTypeValue\":null,\"BandId\":0,\"BandManagerId\":0,\"ArtistUserId\":2378,\"ArtistUserName\":\"anujamishra1988@gmail.com\",\"ArtistFirstName\":\"Anuja Mishra\",\"ArtistLastName\":\"\",\"ArtistProfileTypeId\":1,\"ArtistProfileTypeName\":\"Artist\",\"ArtistCountryCodeId\":\"0\",\"ArtistCountryCode\":\"\",\"ArtistCountryName\":\"\",\"ArtistCity\":\"\",\"ArtistGenreTypeName\":\"\",\"ArtistProfilePic\":\"\",\"ArtistProfilePicServerPath\":\"http://musicapi.htistelecom.in/uploads/profileImages/\",\"ArtistSocialType\":\"F\",\"ArtistSocialImageUrl\":\"https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=284821955742340&height=400&width=400&ext=1557120497&hash=AeSb4g9Fw83r9cBH\",\"ArtistNewStatus\":\"Available\",\"ArtistTypeId\":0,\"ArtistType\":\"\",\"ArtistEmail\":\"anujamishra1988@gmail.com\",\"ArtistPostCode\":\"\",\"ArtistBio\":\"\",\"ArtistWebsite\":\"\",\"ArtistExperienceId\":0,\"ArtistExperienceYear\":\"\",\"ArtistDisplayName\":\"\",\"ArtistExpertise\":\"\",\"ArtistFollowers\":0,\"ArtistReviews\":0,\"ArtistLocationId\":507,\"ArtistUserLatitude\":\"30.7074398\",\"ArtistUserLongitude\":\"76.7037829\",\"ArtistLoginUserLatitude\":\"30.7074905\",\"ArtistLoginUserLongitude\":\"76.7040155\",\"ArtistIsBandMember\":\"P\"},{\"SearchTypeValue\":null,\"BandId\":0,\"BandManagerId\":0,\"ArtistUserId\":2414,\"ArtistUserName\":\"amandeep.kaur@horizontelecom.in\",\"ArtistFirstName\":\"HTIS\",\"ArtistLastName\":\"TELECOM\",\"ArtistProfileTypeId\":1,\"ArtistProfileTypeName\":\"Artist\",\"ArtistCountryCodeId\":\"1\",\"ArtistCountryCode\":\"+9311\",\"ArtistCountryName\":\"Afghanistan\",\"ArtistCity\":\"Chandigarh\",\"ArtistGenreTypeName\":\"Classic\",\"ArtistProfilePic\":\"c014bd3e-8b24-4807-8ef5-35df7597246d.jpg\",\"ArtistProfilePicServerPath\":\"http://musicapi.htistelecom.in/uploads/profileImages/\",\"ArtistSocialType\":\"\",\"ArtistSocialImageUrl\":\"\",\"ArtistNewStatus\":\"Offline\",\"ArtistTypeId\":0,\"ArtistType\":\"\",\"ArtistEmail\":\"amandeep.kaur@horizontelecom.in\",\"ArtistPostCode\":\"160032\",\"ArtistBio\":\"Htis Htis Htis Htis Htis Htis s s ss s s d d d r. Dr. Dr f g g. Hh h h. R r e a a a SD. Dd. F ff. Ffjhdgehdhd dhdhhdhd Amaury beautiful and beautiful and beautiful and beautiful and very beautiful colors and colors look great for the beautiful colors you have in the US and the colors are the same as the ones I used to make a good\",\"ArtistWebsite\":\"\",\"ArtistExperienceId\":2,\"ArtistExperienceYear\":\"3-5 years\",\"ArtistDisplayName\":\"\",\"ArtistExpertise\":\"Backing Vocals\",\"ArtistFollowers\":0,\"ArtistReviews\":0,\"ArtistLocationId\":552,\"ArtistUserLatitude\":\"30.70743\",\"ArtistUserLongitude\":\"76.7039454\",\"ArtistLoginUserLatitude\":\"30.7074905\",\"ArtistLoginUserLongitude\":\"76.7040155\",\"ArtistIsBandMember\":\"P\"}]}"
   lateinit var views:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        views = inflater.inflate(R.layout.fragment_band_members, container, false)
        initOtherViews()
        initViews()
        listeners()
        hitAPI();
        return views

//        val adapter = BandMemberStatusAdapter(users, activity)
//        recyclerBandMember.adapter = adapter

    }

    private fun listeners() {
        views.tvAddBandMember.setOnClickListener(this)
    }

    private fun initOtherViews() {

    }

    private fun initViews() {
        views.recyclerBandMember.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBandId=arguments!!.getString("band_id")
    }

    private fun hitAPI() {
        Utils.initializeAndShow(activity)
        if (Utils.isNetworkConnected(activity)) {
            val obj = JSONObject();
            obj.put("BandManagerId",sharedPref.getString(Constants.USER_ID,""))
            obj.put("BandId",mBandId)

            RetrofitAPI.callAPI(obj.toString(), Constants.FOR_BAND_MEMBER_STATUS, this);
        } else {
            Utils.showToast(activity, getString(R.string.err_no_internet))
        }

    }

    override fun sendResponse(response: Any?, TYPE: Int) {

        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.FOR_BAND_MEMBER_STATUS -> {

                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success")) {

                    val data = obj.getJSONArray("result")
                    tvAddBandMember.visibility = View.GONE
                    recyclerBandMember.visibility = View.VISIBLE
                    val gson = Gson()
                    val itemType = object : TypeToken<ArrayList<BandMemberStatusModel>>() {}.type
                    alModel = gson.fromJson<ArrayList<BandMemberStatusModel>>(data.toString(), itemType)

        val adapter = BandMemberStatusAdapter(alModel, activity!!)
        recyclerBandMember.adapter = adapter


                } else {
                    tvAddBandMember.visibility = View.VISIBLE
                    recyclerBandMember.visibility = View.GONE

                }


            }


        }


    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tvAddBandMember -> {
                val intent=Intent(activity,SearchBandMemberActivity::class.java)
                startActivity(intent)




            }
        }
    }


}

