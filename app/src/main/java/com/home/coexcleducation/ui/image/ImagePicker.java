package com.home.coexcleducation.ui.image;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.home.coexcleducation.R;
import com.home.coexcleducation.utils.CoexclLogs;
import com.home.coexcleducation.utils.Helper;
import com.home.coexcleducation.utils.Utilty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


/**
 * Created by ${Vignesh} on 2019-08-28.
 */

public class ImagePicker {
    private String TAG = getClass().getName();
    Context mContext;
    File tempFile;
    Uri mImageTakefromCameraURI;
    Uri mCroppedImageURI;

    FirebaseRemoteConfig mFirebaseRemoteConfig;

    public ImagePicker(Context mContext) {
        this.mContext = mContext;
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    }

    public void pickImage(boolean showRemoveOption, OnImagePickerListener onImagePickerListener)  {

        PackageManager lPackageManager = mContext.getPackageManager();
        final Dialog lDialog = new Dialog(mContext, R.style.DialogCustomTheme);
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        lDialog.setContentView(R.layout.choose_dialog_box);
        lDialog.setCanceledOnTouchOutside(true);
        lDialog.setCancelable(true);

        Window lWindow = lDialog.getWindow();
        lWindow.setGravity(Gravity.CENTER);
        lWindow.setLayout((int) (((Activity) mContext).getWindow().peekDecorView().getWidth() * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView lAlertMsg =  lDialog.findViewById(R.id.Alert_Message);
        TextView lUpBtnText =  lDialog.findViewById(R.id.Dialog_UpBtn);
        TextView lDownBtnText =  lDialog.findViewById(R.id.Dialog_bottomBtn);
        TextView lRemoveBtnText =  lDialog.findViewById(R.id.Dialog_Remove);
        LinearLayout lGallary =  lDialog.findViewById(R.id.downlayout);
        LinearLayout lCamera =  lDialog.findViewById(R.id.uplayout);
        LinearLayout lRemove =  lDialog.findViewById(R.id.removelayout);

        lAlertMsg.setText("Choose Image Source");
        lUpBtnText.setText(mFirebaseRemoteConfig.getString("take_new_photo"));
        lDownBtnText.setText(mFirebaseRemoteConfig.getString("select_new_photo"));
        lRemoveBtnText.setText(mFirebaseRemoteConfig.getString("remove_photo"));

        if (showRemoveOption) {
            lRemove.setVisibility(android.view.View.VISIBLE);
            lGallary.setBackground(ContextCompat.getDrawable(mContext, R.drawable.border_bt));
        }
        else {
            lRemove.setVisibility(android.view.View.GONE);
            lGallary.setBackground(ContextCompat.getDrawable(mContext, R.drawable.botton_corner_white_4r));
        }


        lGallary.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                lDialog.dismiss();
                onImagePickerListener.selectedAction("gallery");
            }
        });

        lCamera.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                lDialog.dismiss();
                onImagePickerListener.selectedAction("camera");

            }
        });

        lRemove.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View arg0) {
                lDialog.dismiss();
                onImagePickerListener.selectedAction("remove");
            }
        });

        if (lPackageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            lDialog.show();
        }
        else {
            onImagePickerListener.selectedAction("cameranotavailable");
        }
    }


    public boolean isCameraAccesPermissionAcquired() {
        return (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && isImageAccessPermissionAcquired());
    }


    public boolean isImageAccessPermissionAcquired() {
        return (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }


    public boolean isImageCroppingAppAvailabe() {
        List<ResolveInfo> list = mContext.getPackageManager().queryIntentActivities(createCropIntent(), 0);
        return !list.isEmpty();
    }

    private Intent createCropIntent(){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        File lTempFile = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            lTempFile= getAppSpecificAlbumStorageDir(mContext,"picture");
            else
            lTempFile = createTemporaryFile("picture");
        } catch(Exception e) {
            CoexclLogs.printException(e);
        }
        Uri lCropUri = Uri.fromFile(lTempFile);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, lCropUri);
        return intent;
    }


    public Intent getIntentToCropImage(Uri uri) {
        Intent lCropIntent = createCropIntent();
        lCropIntent.setData(uri);
        lCropIntent.putExtra("outputX", 200);
        lCropIntent.putExtra("outputY", 200);
        lCropIntent.putExtra("aspectX", 200);
        lCropIntent.putExtra("aspectY", 200);
        lCropIntent.putExtra("scale", true);
        lCropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        lCropIntent.putExtra("return-data", true);
        ResolveInfo lResInfo = null;
        List<ResolveInfo> list = mContext.getPackageManager().queryIntentActivities(lCropIntent, 0);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).activityInfo.packageName.equalsIgnoreCase("com.google.android.apps.photos")) {
                lResInfo = list.get(i);
                break;
            }
            else {
                lResInfo = list.get(0);
            }
        }
        if(lResInfo != null) {
            mContext.grantUriPermission(lResInfo.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            lCropIntent.setComponent(new ComponentName(lResInfo.activityInfo.packageName, lResInfo.activityInfo.name));
        }
        return lCropIntent;
    }


    public Intent getIntenttoOpenCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getImageTakefromCamera());
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("return-data", true);
        return intent;
    }


    public Uri cropImageManually(Uri uri, boolean isFromCamera) {
        try {
            try {
                FileOutputStream out = null;
                File lTempFile;
                if(!isFromCamera) {
                    tempFile = new File(new Utilty().getRealPath(mContext.getContentResolver(), uri));
                    lTempFile = tempFile;
                } else
                    lTempFile = getTempFile();
                BitmapFactory.Options bounds = new BitmapFactory.Options();
                bounds.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(lTempFile.getAbsolutePath(), bounds);
                final int REQUIRED_SIZE = 150;
                int scale = 1;
                while (bounds.outWidth / scale / 2 >= REQUIRED_SIZE && bounds.outHeight / scale / 2 >= REQUIRED_SIZE) {
                    scale *= 2;
                }
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = scale;
                Bitmap bm = BitmapFactory.decodeFile(lTempFile.getAbsolutePath(), opts);
                ExifInterface exif = new ExifInterface(lTempFile.getAbsolutePath());
                String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                int rotationAngle = 0;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

                Matrix matrix = new Matrix();
                matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, opts.outWidth, opts.outHeight, matrix, true);

                out = new FileOutputStream(lTempFile);
                rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                return Uri.fromFile(lTempFile);
            } catch (Exception e) {
                CoexclLogs.printException(e);
            }
        } catch (Exception pException) {
            CoexclLogs.printException(pException);
        }
        return null;
    }

    private File createTemporaryFile(String part) throws IOException {

        File folderFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/.temp/");
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        return File.createTempFile(part, ".jpg", folderFile);
    }

    private File getAppSpecificAlbumStorageDir(Context context, String part) {
        // Get the pictures directory that's inside the app-specific directory on
        // external storage.
        String tempAlbumName="Coexcl";
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), tempAlbumName);
        if (file == null || !file.mkdirs()) {
            CoexclLogs.errorLog("ImagePicker","Directory not created");
        }
        try {
            return File.createTempFile(part, ".jpg", file);
        } catch (IOException e) {
            CoexclLogs.printException(e);
        }
        return null;
    }


    public Uri getImageTakefromCamera() {
            try {
                Uri uri;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    uri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider",
                            getTempFile());
                }
                else {
                    uri = Uri.fromFile(getTempFile());
                }

                return uri;
            } catch (Exception e) {
                CoexclLogs.printException(e);
            }
            return null;
    }

    public Uri readSelectedFileAndWriteinOurReference(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = mContext.getContentResolver().openFileDescriptor(uri, "r", null);
        if(parcelFileDescriptor != null) {
            InputStream inputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
            OutputStream outputStream = new FileOutputStream(getTempFile());
            Helper.copyStream(inputStream, outputStream);
            return FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider",getTempFile());
        }
        return uri;
    }


    public File getTempFile() throws IOException {
        if(tempFile != null) return tempFile;
        else {
            tempFile = File.createTempFile("temp_"+ System.currentTimeMillis(),".jpg", mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            return tempFile;
        }
    }

    public void clearTempFile(){
        if(tempFile != null && tempFile.exists()) {
                if(!tempFile.delete()) {
                    CoexclLogs.errorLog(TAG, "Temp File not Deleted!");
                }
            tempFile = null;
        }
    }

}

