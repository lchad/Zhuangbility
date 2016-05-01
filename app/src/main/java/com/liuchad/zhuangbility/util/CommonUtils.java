package com.liuchad.zhuangbility.util;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Toast;
import com.liuchad.zhuangbility.Constants;
import com.liuchad.zhuangbility.ZbApp;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by liuchad on 16/3/27.
 */
public class CommonUtils {

    public static final String ACTION_CROP = "com.android.camera.action.CROP";

    public static String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
            context.getResources().getDisplayMetrics());
    }

    /**
     * 获取手机屏幕高度
     */
    public static int getScreenHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取手机屏幕宽度
     */
    public static int getScreenWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static void showToast(String message) {
        Toast.makeText(ZbApp.getInstance(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 创建临时文件
     */
    public static File createTmpFile(Context context) {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 已挂载
            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(pic, fileName + ".jpg");
            return tmpFile;
        } else {
            File cacheDir = context.getCacheDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(cacheDir, fileName + ".jpg");
            return tmpFile;
        }
    }

    /**
     * 裁剪图片
     *
     * @param uri         图片uri
     * @param requestCode 结果码
     */
    public static void startPhotoZoom(Activity activity, Uri uri, int requestCode) {
        Intent intent = new Intent(ACTION_CROP);
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        //intent.putExtra("aspectX", 1);
        //intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        //intent.putExtra("outputX", width);
        //intent.putExtra("outputY", height);
        intent.putExtra("noFaceDetection", true);  //不支持人脸识别
        //        intent.putExtra("return-data", true);

        Uri uritempFile = Uri.parse("file://" + SDCARD + "/" + "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 检测第三方App是否已安装
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取Android版本号.
     */
    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机imei号码.
     *
     * @return Android6.0以上没有权限的情况下会返回 "-1".
     */
    public static String getIMEI(Context context) {
        if (hasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } else {
            return "-1";
        }
    }

    public static boolean hasPermission(Context context, String permission) {
        return (ContextCompat.checkSelfPermission(context,
            permission) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * 设备是否拥有摄像头.
     */
    public static boolean hasDeviceCamera(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * 获取手机品牌
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机当前剩余的闪存空间.
     *
     * @param format 期望的返回结果格式.
     */
    public static float getAvailableExternalStorageSize(int format) {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();

            return availableBlocks * blockSize / ARRAY_BASE_DIVIDEND[format];
        }
        return 0f;
    }

    /**
     * 获取当前路径，可用空间
     */
    public static long getAvailableSizeInCurrentPath(String path) {
        try {
            File base = new File(path);
            StatFs stat = new StatFs(base.getPath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                return stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
            } else {
                return stat.getBlockSize() * stat.getAvailableBlocks();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取手机当前的闪存总空间.
     */
    public static int getTotalExternalStorageSize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return (int) (totalBlocks * blockSize);
        } else {
            return -1;
        }
    }

    /**
     * 获取手机当前的闪存总空间.
     *
     * @param format 期望的返回结果格式.
     */
    public static float getTotalExternalStorageSize(int format) {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize / ARRAY_BASE_DIVIDEND[format];
        }
        return 0f;
    }

    private static final float[] ARRAY_BASE_DIVIDEND = { 1f, 1024f, 1024f * 1024f, 1024f * 1024f * 1024f };

    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(
            Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机型号.
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path,
        int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(
        BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void shareImage(Context context, Bitmap bitmap, String packageName) {
        try {
            Uri uriToImage = Uri.parse(MediaStore.Images.Media.insertImage(
                context.getContentResolver(), bitmap, null, null));
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
            shareIntent.setType("image/*");
            // 遍历所有支持发送图片的应用。找到需要的应用
            PackageManager packageManager = context.getPackageManager();
            List<ResolveInfo> resolveInfoList =
                packageManager.queryIntentActivities(shareIntent, PackageManager.GET_INTENT_FILTERS);
            ComponentName componentName = null;
            for (int i = 0; i < resolveInfoList.size(); i++) {
                if (TextUtils.equals(
                    resolveInfoList.get(i).activityInfo.packageName,
                    packageName)) {
                    componentName = new ComponentName(
                        resolveInfoList.get(i).activityInfo.packageName,
                        resolveInfoList.get(i).activityInfo.name);
                    break;
                }
            }
            // 已安装**
            if (null != componentName) {
                shareIntent.setComponent(componentName);
                context.startActivity(shareIntent);
            } else {
                CommonUtils.showToast("请先安装**");
            }
        } catch (Exception e) {
            CommonUtils.showToast("分享图片到**失败");
        }
    }

    /**
     * 刷新本地数据库,这样在QQ和微信中就能看到最新的图片了.
     */
    public static void refreshLocalDb(Context context, File dest) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, dest.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, Constants.IMAGE_JPEG);
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}