package com.musicseque.artist.band.locations

import java.util.HashMap

import android.content.Context
import android.util.AttributeSet
import android.widget.AutoCompleteTextView

/** Customizing AutoCompleteTextView to return Place Description
 * corresponding to the selected item
 */
class CustomAutoCompleteTextView(context: Context, attrs: AttributeSet) : AutoCompleteTextView(context, attrs) {

    /** Returns the place description corresponding to the selected item  */
    override fun convertSelectionToString(selectedItem: Any): CharSequence? {
        /** Each item in the autocompetetextview suggestion list is a hashmap object  */
        val hm = selectedItem as HashMap<String, String>
        return hm["description"]
    }
}