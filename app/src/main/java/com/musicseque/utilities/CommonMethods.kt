package com.musicseque.utilities

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.musicseque.R

object CommonMethods {
    @JvmStatic
    fun showLargeImages(context: Context?, url: String?) {
        val dialogImages = Dialog(context, R.style.CustomDialog)
        dialogImages.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogImages.setContentView(R.layout.dialog_show_image)
        val ivDelete = dialogImages.findViewById<View>(R.id.ivDelete) as ImageView
        val ivImage = dialogImages.findViewById<View>(R.id.ivImage) as ImageView
        val progressBar = dialogImages.findViewById<View>(R.id.progressBar) as ProgressBar
        progressBar.visibility = View.GONE
        Glide.with(context!!).load(url).into(ivImage)
        ivDelete.setOnClickListener { dialogImages.dismiss() }
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogImages.window.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialogImages.window.attributes = lp
        dialogImages.show()
    }
}