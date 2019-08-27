package com.musicseque.utilities

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import java.io.File
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionRequest
import com.musicseque.BuildConfig
import com.musicseque.dagger_data.SharedPrefDependency


open class KotlinBaseFragment : Fragment() {
    private lateinit var mType: String
    private lateinit var selectedImagePath: String
    private lateinit var muri: Uri
    private lateinit var mVideoPath: String
    private val FOR_MEDIA_FILE = 1001
    private val FOR_REQUEST_CAMERA = 1002


    private lateinit var mFileType: String
    lateinit var mDir: File


//    fun getSharedPref(): SharedPreferences {
//        val retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(SharedPrefDependency(activity)).build()
//        return retrofitComponent.shared
//    }
//
//
//    internal fun getEditor(): SharedPreferences.Editor {
//        val retrofitComponent = DaggerRetrofitComponent.builder().sharedPrefDependency(SharedPrefDependency(activity)).build()
//        return retrofitComponent.editor
//    }




    public fun openPhotoDialog(type: String) {
        mType = type
        mDir = File(Environment.getExternalStorageDirectory(), "MusicSegue")
        if (!mDir.exists()) {
            mDir.mkdir()
        }

        var pictureDialogItems: Array<String>
        mFileType = type
        if (type.equals("video", ignoreCase = true)) {

            pictureDialogItems = arrayOf("Select video from gallery", "Capture video from camera")
        } else {


            pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        }


        val pictureDialog = AlertDialog.Builder(activity)
        pictureDialog.setTitle("Select Action")

        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> {


                    if (type.equals("video", ignoreCase = true))
                        galleryVideo()
                    else
                        galleryImage()


                }
                1 -> {


                    if (type.equals("video", ignoreCase = true))
                        cameraVideo()
                    else
                        cameraPhoto()
                }

            }
        }
        pictureDialog.show()
    }

    private fun galleryVideo() {
        val intent = Intent(ACTION_PICK)
        intent.setType("video/*")
        startActivityForResult(intent, FOR_MEDIA_FILE)


    }

    private fun galleryImage() {
        val openGalleryIntent = Intent(Intent.ACTION_PICK)
        openGalleryIntent.type = "image/*"
        startActivityForResult(openGalleryIntent, FOR_MEDIA_FILE)
    }

    private fun cameraVideo() {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        val mediaFile = File(Environment.getExternalStorageDirectory().absolutePath
                + "/" + System.currentTimeMillis() + "_myvideo.mp4")
        mVideoPath = mediaFile.absolutePath

        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)

        val videoUri = Uri.fromFile(mediaFile)

        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
        startActivityForResult(intent, FOR_REQUEST_CAMERA)
    }

    private fun cameraPhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val image_name = "pic_" + System.currentTimeMillis() + ".jpeg"

            val photoUploadFile = File(mDir, image_name)
            muri = Uri.fromFile(photoUploadFile)
            selectedImagePath = muri.getPath()
            val photoURI = FileProvider.getUriForFile(activity!!,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoUploadFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    photoURI)
        } else {
            val image_name = "pic_" + System.currentTimeMillis() + ".jpeg"

            val photoUploadFile = File(mDir, image_name)
            muri = Uri.fromFile(photoUploadFile)
            selectedImagePath = muri.getPath()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, muri)
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("return-data", true)
        startActivityForResult(intent, FOR_REQUEST_CAMERA)
    }

    public fun checkPermissions(type: String, s: String) {
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            openPhotoDialog(type)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        //super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FOR_MEDIA_FILE) {
                if (mType.equals("video")) {


                } else {
                    val uri = data.getData()

                    val filePath = FileUtils.compressImage(uri!!.toString(), activity)
                    val file = File(filePath)
                    sendImage(file)
                }
            } else if (requestCode == FOR_REQUEST_CAMERA) {
                if (mType.equals("video")) {


                } else {
                    val uri = Uri.parse(selectedImagePath)
                    val filePath = FileUtils.compressImage(uri.toString(), activity)
                    val file = File(filePath)
                    sendImage(file)

                }
            }
        }


    }

    private fun sendImage(file: File) {
        val intent=Intent()
        intent.putExtra("file",file.toString())
        activity?.setResult(RESULT_OK,intent)

    }

}