package com.musicseque.utilities

import android.content.Context
import com.musicseque.interfaces.MyInterface
import com.musicseque.models.CityModel
import com.musicseque.models.CountryModel
import com.musicseque.models.StateModel
import org.json.JSONArray
import org.json.JSONException

class CountryStateCityClass {
    companion object {

        lateinit var arrStateName: Array<String?>
        var alState = java.util.ArrayList<StateModel>()
        var alStateName = java.util.ArrayList<String>()

        lateinit var arrCityName: Array<String?>
        var alCity = java.util.ArrayList<CityModel>()
        var alCityName = java.util.ArrayList<String>()
        var countryAL = ArrayList<CountryModel>()
        var countryNameAL = ArrayList<String>()
        var countryCodeAL = ArrayList<String>()

        lateinit var arrCountryName: Array<String?>
        lateinit var arrCountryCode: Array<String?>

        fun countriesDetail(res: String) {
            try {
                val jsonArray = JSONArray(res.toString())
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val model = CountryModel()
                    model.countryId = jsonObject.getString("CountryId")
                    model.countryName = jsonObject.getString("CountryName")
                    model.countryCode = jsonObject.getString("CountryCode")
                    countryCodeAL.add(jsonObject.getString("CountryCode"))
                    countryNameAL.add(jsonObject.getString("CountryName"))
                    countryAL.add(model)
                }

                arrCountryName = countryNameAL.toTypedArray()
                arrCountryCode=countryCodeAL.toTypedArray()

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        fun stateList(response: String) {
            try {
                val jsonArray = JSONArray(response.toString())
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val model = StateModel()
                    model.countryId = jsonObject.getString("CountryId")
                    model.stateId = jsonObject.getString("StateId")
                    model.stateName = jsonObject.getString("StateName")

                    alStateName.add(jsonObject.getString("StateName"))
                    alState.add(model)
                }

                arrStateName = alStateName.toTypedArray()


            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }


        fun cityList(response: String) {
            val jsonArray = JSONArray(response.toString())
            var i = 0
            while (i < jsonArray.length()) {
                val jsonObjectState = jsonArray.getJSONObject(i)
                val model = CityModel()
                model.cityName = jsonObjectState.getString("CityName")
                model.cityId = jsonObjectState.getString("CityId")
                alCity.add(model)
                alCityName.add(jsonObjectState.getString("CityName"))
                i++
            }
            arrCityName = alCityName.toTypedArray()
        }



        fun sendPostData(params: String, Type: Int, myInterface: MyInterface, context: Context)
        {
            APIHit.sendPostData(params, Type, myInterface,context)

        }

        fun sendGetData(Type: Int, myInterface: MyInterface, context: Context) {
            APIHit.sendGetData(Type, myInterface,context)

        }

    }



}