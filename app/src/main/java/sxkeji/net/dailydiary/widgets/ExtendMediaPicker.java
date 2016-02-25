package sxkeji.net.dailydiary.widgets;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import java.io.File;

import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.utils.LogUtils;
import sxkeji.net.dailydiary.utils.MediaUtils;
import sxkeji.net.dailydiary.utils.PerssionUtils;
import sxkeji.net.dailydiary.utils.UIUtils;


public class ExtendMediaPicker implements View.OnClickListener {

    private static final int REQUEST_CODE_CROP_PHOTO = 2000;
    private static final int REQUEST_CODE_PICK_IMAGE = 2001;
    private static final int REQUEST_CODE_TAKE_PHOTO = 2002;

    private Uri imageUri;
    private File tempFile;
    private boolean mCropImage;
    private Fragment mFragment;
    private Activity mActivity;
    private OnMediaPickerListener mMediaPickerListener;

    private boolean isActivity;
    private View popViwe;
    private PopupWindow pop;

    public ExtendMediaPicker(Fragment fragment) {
        this.mFragment = fragment;
        this.tempFile = createTempFile();
        this.isActivity = false;
    }

    public ExtendMediaPicker(Activity fragment) {
        this.mActivity = fragment;
        this.tempFile = createTempFile();
        this.isActivity = true;
    }


    public void showPickerView(boolean isCropImage, View mIvUserImage) {
        this.mCropImage = isCropImage;
        popViwe = View.inflate(UIUtils.getApplication(),
                R.layout.pop_chose_photo, null);
        popViwe.findViewById(R.id.tv_camer).setOnClickListener(this);
        popViwe.findViewById(R.id.tv_gallery).setOnClickListener(this);
        popViwe.findViewById(R.id.tv_cancle).setOnClickListener(this);


        pop = new PopupWindow(popViwe, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                true);


        pop.setOutsideTouchable(true);

        pop.setFocusable(true);


        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        pop.setAnimationStyle(R.style.AnimBottom);

//        pop.showAsDropDown(mIvUserImage);
        pop.showAtLocation(mIvUserImage, Gravity.BOTTOM, 0, 0);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {

            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_PICK_IMAGE:
                String path = MediaUtils.getPath(isActivity == false ? mFragment.getContext() : mActivity, data.getData());
                cropImageUri(Uri.fromFile(new File(path)), 300, 300,
                        REQUEST_CODE_CROP_PHOTO);
                break;
            case REQUEST_CODE_TAKE_PHOTO:

                cropImageUri(imageUri, 300, 300, REQUEST_CODE_CROP_PHOTO);
                break;
            case REQUEST_CODE_CROP_PHOTO:
                String imagePath = tempFile.getAbsolutePath();
                LogUtils.i("crop" + imagePath);

                if (mMediaPickerListener != null) {


                    mMediaPickerListener.onSelectedMediaChanged(imagePath);
                }
                break;
        }
    }

    @SuppressLint("InlinedApi")
    private void openSystemPickImage() {
        Intent photoPickerIntent = null;
        if (Build.VERSION.SDK_INT < 19) {
            photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        }
        photoPickerIntent.setType("image/*");
        photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
        if (!isActivity) {
            mFragment.startActivityForResult(photoPickerIntent,
                    REQUEST_CODE_PICK_IMAGE);
        } else {
            mActivity.startActivityForResult(photoPickerIntent,
                    REQUEST_CODE_PICK_IMAGE);
        }
    }

    private void openSystemCamera() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dirPath = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            imageUri = Uri.fromFile(dirPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);


            if (!isActivity) {
                mFragment.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
            } else {
                mActivity.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
            }

        } else {
            UIUtils.showToastSafe(UIUtils.getApplication(),
                    "Before you take photos please insert SD card");
        }
    }

    private File createTempFile() {
        File file = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return new File(Environment.getExternalStorageDirectory(),
                    +System.currentTimeMillis() + "image.png");
        } else {


            if (!isActivity) {
                file = new File(mFragment.getContext().getFilesDir(), +System.currentTimeMillis() + "image.png");
            } else {
                file = new File(mActivity.getFilesDir(), +System.currentTimeMillis() + "image.png");

            }

            return file;
        }
    }

    /**
     * 截取图像部分区域
     *
     * @param uri
     * @param outputX
     * @param outputY
     * @return
     */
    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        if (!mCropImage && mMediaPickerListener != null) {
            mMediaPickerListener.onSelectedMediaChanged(uri.getPath());
            return;
        }

        try {
            // android1.6以后只能传图库中图片
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            intent.putExtra("return-data", false);
            intent.putExtra("outputFormat",
                    Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);


            if (!isActivity) {
                mFragment.startActivityForResult(intent, requestCode);

            } else {
                mActivity.startActivityForResult(intent, requestCode);

            }


        } catch (ActivityNotFoundException ex) {
            UIUtils.showToastSafe(UIUtils.getApplication(), "Can not find image crop app");
        }
    }

    public void setOnMediaPickerListener(OnMediaPickerListener listener) {
        this.mMediaPickerListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_camer:
                if (PerssionUtils.checkPerssion(mActivity, Manifest.permission.CAMERA) && PerssionUtils.checkPerssion(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    openSystemCamera();
                    disMissPop();
                }
                break;
            case R.id.tv_gallery:
                if (PerssionUtils.checkPerssion(mActivity, Manifest.permission.CAMERA) && PerssionUtils.checkPerssion(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    openSystemPickImage();
                    disMissPop();
                }
                break;
            case R.id.tv_cancle:
                disMissPop();
                break;
        }
    }

    public interface OnMediaPickerListener {

        void onSelectedMediaChanged(String mediaUri);
    }


    private void disMissPop() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }
    }
}
