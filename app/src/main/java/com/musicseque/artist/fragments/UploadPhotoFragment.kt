package com.musicseque.artist.fragments

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.musicseque.R
import com.musicseque.artist.activity.UploadActivity
import com.musicseque.artist.artist_adapters.UploadPhotosAdapter
import com.musicseque.interfaces.MyInterface
import com.musicseque.models.ImageModel
import com.musicseque.retrofit_interface.ImageUploadClass
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.*
import com.musicseque.utilities.CommonMethods.showLargeImages
import kotlinx.android.synthetic.main.fragment_upload_photos.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class UploadPhotoFragment : KotlinBaseFragment(), MyInterface, UploadPhotosAdapter.UploadImage, View.OnClickListener {

    var layoutManager: GridLayoutManager? = null
    var v: View? = null
    private var mImagesId = ""
    private var mPosition = ""

    var arrayList = ArrayList<ImageModel>()
    var uploadPhotosAdapter: UploadPhotosAdapter? = null
    private var mActionMode: ActionMode? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_upload_photos, container, false)
        initOtherViews()
        initViews()
        listeners()
        hitAPIPhotos()
        implementRecyclerViewClickListeners()
        return v
    }

    private fun initOtherViews() {

    }

    private fun initViews() {
        layoutManager = GridLayoutManager(activity, 4)
        recyclerPhotos!!.layoutManager = layoutManager
    }

    private fun listeners() {
        floatingButtonUploadPhoto.setOnClickListener(this)
    }
    override fun sendResponse(response: Any, TYPE: Int) {
        Utils.hideProgressDialog()
        if (TYPE == Constants.FOR_ARTIST_UPLOADED_IMAGES) {
            try {
                val jsonArray = JSONArray(response.toString())
                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        arrayList.add(ImageModel(jsonObject.getString("ImgUrl"), jsonObject.getString("Image"), true, jsonObject.getInt("Id"), false))
                    }
                } else {
                }
                uploadPhotosAdapter = UploadPhotosAdapter(activity, arrayList, this@UploadPhotoFragment)
                recyclerPhotos!!.adapter = uploadPhotosAdapter
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else if (TYPE == Constants.FOR_UPLOAD_ARTIST_IMAGE) {
            try {
                val jsonObject = JSONObject(response.toString())
                if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) { //                    arrayList.add(0, new ImageModel(jsonObject.getString("ImgUrl"), jsonObject.getString("Image"), true, jsonObject.getInt("Id"),false));
//                    uploadPhotosAdapter = new UploadPhotosAdapter(getActivity(), arrayList, UploadPhotoFragment.this);
//                    recyclerPhotos.setAdapter(uploadPhotosAdapter);
                    arrayList.clear()
                    hitAPIPhotos()
                } else {
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else if (TYPE == Constants.FOR_DELETE_ARTIST_IMAGES) {
            try {
                val jsonObject = JSONObject(response.toString())
                if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) { //                    String[] arr=mPosition.split(",");

                    arrayList.clear()
                    hitAPIPhotos()
                    mPosition = ""
                    mImagesId = ""

                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun hitAPIPhotos() {
        if (Utils.isNetworkConnected(activity)) {
            showDialog()
            val jsonObject = JSONObject()
            try {
                jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_ARTIST_UPLOADED_IMAGES, this)
        } else {
            Utils.showToast(activity, resources.getString(R.string.err_no_internet))
        }
    }

    override fun callMethod() {
        (activity as UploadActivity?)!!.openDialogForPic()
    }

    fun uploadImage(fileToUpload: ArrayList<MultipartBody.Part?>?, mUSerId: RequestBody?) {
        showDialog()
        ImageUploadClass.fileUploadMultiple(fileToUpload, mUSerId, null, Constants.FOR_UPLOAD_ARTIST_IMAGE, this@UploadPhotoFragment)
    }


   override fun onClick(view: View?) {
        (activity as UploadActivity?)!!.openDialogForPic()
    }

    private fun implementRecyclerViewClickListeners() {
        recyclerPhotos!!.addOnItemTouchListener(RecyclerTouchListener(activity, recyclerPhotos, object : RecyclerClick_Listener {
            override fun onClick(view: View, position: Int) { //If ActionMode not null select item
                if (mActionMode != null) onListItemSelect(position) else showLargeImages(activity, arrayList[position].base_url + arrayList[position].image_url)
            }

            override fun onLongClick(view: View, position: Int) { //Select item on long click
                onListItemSelect(position)
            }
        }))
    }

    //List item select method
    private fun onListItemSelect(position: Int) {
        uploadPhotosAdapter!!.toggleSelection(position) //Toggle the selection
        val hasCheckedItems = uploadPhotosAdapter!!.selectedCount > 0 //Check if any items are already selected or not
        if (hasCheckedItems && mActionMode == null) // there are some selected items, start the actionMode
            mActionMode = (activity as AppCompatActivity?)!!.startSupportActionMode(Toolbar_ActionMode_Callback(this@UploadPhotoFragment, uploadPhotosAdapter, arrayList, false)) else if (!hasCheckedItems && mActionMode != null) // there no selected items, finish the actionMode
            mActionMode!!.finish()
        if (mActionMode != null) //set action mode title on item selection
            mActionMode!!.title = uploadPhotosAdapter!!.getSelectedCount().toString() + " selected"
    }

    //Set action mode null after use
    fun setNullToActionMode() {
        if (mActionMode != null) mActionMode = null
    }

    //Delete selected rows
    fun deleteRows() {
        val selected = uploadPhotosAdapter!!.getSelectedIds() //Get selected ids
        //Loop all selected ids
        for (i in selected.size() - 1 downTo 0) {
            if (selected.valueAt(i)) {
                mImagesId = mImagesId + "," + arrayList[selected.keyAt(i)].id
                mPosition = mPosition + "," + selected.keyAt(i)
            }
        }
        mImagesId = mImagesId.replaceFirst(",".toRegex(), "")
        mPosition = mPosition.replaceFirst(",".toRegex(), "")
        if (Utils.isNetworkConnected(activity)) {
            showDialog()
            val jsonObject = JSONObject()
            try {
                jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                jsonObject.put("Id", mImagesId)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_DELETE_ARTIST_IMAGES, this@UploadPhotoFragment)
        } else {
            Utils.showToast(activity, getString(R.string.err_no_internet))
        }
        //  Toast.makeText(getActivity(), selected.size() + " item deleted.", Toast.LENGTH_SHORT).show();//Show Toast
        mActionMode!!.finish() //Finish action mode after use
    }

    fun checkActionMode() {}
    fun showDialog() {
        Utils.initializeAndShow(activity)
    }

    companion object {
        const val REQUEST_IMAGE = 100
        private const val PICK_IMAGE_REQUEST = 101
    }
}