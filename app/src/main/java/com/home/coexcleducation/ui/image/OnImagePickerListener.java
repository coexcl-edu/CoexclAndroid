package com.home.coexcleducation.ui.image;

import android.net.Uri;


/**
 * Created by ${Vignesh} on 2019-08-29.
 */
public interface OnImagePickerListener {
    void onImagepicked(Uri uri);
    void selectedAction(String action);
}
