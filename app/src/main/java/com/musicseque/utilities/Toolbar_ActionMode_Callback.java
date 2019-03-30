package com.musicseque.utilities;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.musicseque.R;
import com.musicseque.artist.artist_adapters.UploadPhotosAdapter;
import com.musicseque.artist.fragments.UploadPhotoFragment;
import com.musicseque.models.ImageModel;

import java.util.ArrayList;

/**
 * Created by SONU on 22/03/16.
 */
public class Toolbar_ActionMode_Callback implements ActionMode.Callback {

    private Fragment context;
    private UploadPhotosAdapter recyclerView_adapter;

    private ArrayList<ImageModel> message_models;
    private boolean isListViewFragment;


    public Toolbar_ActionMode_Callback(Fragment context, UploadPhotosAdapter recyclerView_adapter, ArrayList<ImageModel> message_models, boolean isListViewFragment) {
        this.context = context;
        this.recyclerView_adapter = recyclerView_adapter;
        this.message_models = message_models;
        this.isListViewFragment = isListViewFragment;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_main, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

        //Sometimes the meu will not be visible so for that we need to set their visibility manually in this method
        //So here show action menu according to SDK Levels
        if (Build.VERSION.SDK_INT < 11) {
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_delete), MenuItemCompat.SHOW_AS_ACTION_NEVER);

        } else {
            menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        }

        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:

                //Check if current action mode is from ListView Fragment or RecyclerView Fragment

                //If current fragment is recycler view fragment
                //Fragment recyclerFragment = new MainActivity().getFragment(1);//Get recycler view fragment
                if (context != null) {
                    UploadPhotoFragment uploadPhotoFragment = (UploadPhotoFragment) context;
                    uploadPhotoFragment.deleteRows();//delete selected rows
                }
                //If recycler fragment not null

                break;


        }
        return false;
    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {

        //When action mode destroyed remove selected selections and set action mode to null
        //First check current fragment action mode

        recyclerView_adapter.removeSelection();  // remove selection
        UploadPhotoFragment uploadPhotoFragment = (UploadPhotoFragment) context;
        if (uploadPhotoFragment != null)
            uploadPhotoFragment.setNullToActionMode();//Set action mode null

    }
}
