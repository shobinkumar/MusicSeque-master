package com.musicseque.artist.band.band_activity


import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.activities.BaseActivity
import com.musicseque.artist.band.band_adapter.SearchBandMemberAdapter
import com.musicseque.artist.band.band_model.BandMemberStatusModel

import com.musicseque.interfaces.MyInterface
import com.musicseque.interfaces.RemoveMemberInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_search_artist1.*
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONException
import android.widget.SimpleAdapter
import com.musicseque.artist.band.locations.PlaceJSONParser
import android.os.AsyncTask
import android.os.AsyncTask.execute

import android.util.Log
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class SearchBandMemberActivity1 : BaseActivity() {


    var activity: Activity = this



    lateinit var adapter: SearchBandMemberAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.musicseque.R.layout.activity_search_artist1)
        initOtherViews()
        initViews()
        listener()

        callEditTextListener()
    }


    private fun initOtherViews() {
       // mBandId = intent.getStringExtra("band_id")

    }

    private fun initViews() {

        tv_title. text = "Invite Band Member"
        img_first_icon.visibility = View.VISIBLE
        img_right_icon.visibility = View.GONE
        recyclerArtist.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    }

    private fun listener() {
        img_first_icon.setOnClickListener {

            finish()
        }
    }

    private fun callEditTextListener() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
              var  placesTask =  PlacesTask ();
                placesTask.execute(s.toString());
            }

        })
    }


    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)

            // Creating an http connection to communicate with url
            urlConnection = url.openConnection() as HttpURLConnection

            // Connecting to url
            urlConnection!!.connect()

            // Reading data from url
            iStream = urlConnection!!.getInputStream()

            val br = BufferedReader(InputStreamReader(iStream))

            val sb = StringBuffer()

          //  var line = ""
            while ((br.readLine()) != null) {
                sb.append(br.readLine())
            }

            data = sb.toString()

            br.close()

        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private inner class PlacesTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg place: String): String {
            // For storing data from web service
            var data = ""

            // Obtain browser key from https://code.google.com/apis/console
            val key = "AIzaSyDGyjFhJIBztwCt42V4uLXqjW4s3Vh-05M"

            var input = ""

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8")
            } catch (e1: UnsupportedEncodingException) {
                e1.printStackTrace()
            }

            // place type to be searched
            val types = "types=geocode"

            // Sensor enabled
            val sensor = "sensor=false"

            // Building the parameters to the web service
            val parameters = "$input&$types&$sensor&$key"

            // Output format
            val output = "json"

            // Building the url to the web service
           // val url = "https://maps.googleapis.com/maps/api/place/autocomplete/$output?$parameters"
            val url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=1600+Amphitheatre&key=AIzaSyDGyjFhJIBztwCt42V4uLXqjW4s3Vh-05M";
            try {
                // Fetching the data from we service
                data = downloadUrl(url)
            } catch (e: Exception) {
                Log.d("Background Task", e.toString())
            }

            return data
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            // Creating ParserTask
            var parserTask = ParserTask()

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result)
        }
    }

    /** A class to parse the Google Places in JSON format  */
    private inner class ParserTask : AsyncTask<String, Int, List<HashMap<String, String>>>() {

          var jObject= JSONObject()

        override fun doInBackground(vararg jsonData: String): List<HashMap<String, String>>? {

            var places: List<HashMap<String, String>>? = null

            val placeJsonParser = PlaceJSONParser()

            try {
                jObject = JSONObject(jsonData[0])

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject)

            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            }

            return places
        }

        override fun onPostExecute(result: List<HashMap<String, String>>) {

            val from = arrayOf("description")
            val to = intArrayOf(android.R.id.text1)

            // Creating a SimpleAdapter for the AutoCompleteTextView
            val adapter = SimpleAdapter(baseContext, result, android.R.layout.simple_list_item_1, from, to)

            // Setting the adapter
            etSearch.setAdapter(adapter)
        }
    }


}
