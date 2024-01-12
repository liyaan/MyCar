package com.liyaan.car.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ImgUtil {

    public static final int TAKE_PHOTO = 1;//拍照
    public static final int CHOOSE_PHOTO = 2;//选择相册
    public static final int REQUEST_CODE_CAMERA = 3;//相机权限请求
    public static final int REQUEST_CODE_ALBUM = 4;//相册权限请求
    public static Uri imageUri;//相机拍照图片保存地址

    /**
     * 选择图片，从图库、相机
     *
     * @param activity 上下文
     */
    public static void choicePhoto(final Activity activity,String path) {
        //采用的是系统Dialog作为选择弹框
        new AlertDialog.Builder(activity).setTitle("上传头像")//设置对话框标题
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {//添加确定按钮
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ArrayList<String> permissions = new ArrayList<>();
                        if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            permissions.add(Manifest.permission.CAMERA);
                        }

                        if (permissions.size() == 0) {//有权限,跳转
                            //打开相机-兼容7.0
                            ImgUtil.openCamera(activity, path);
                        } else {
                            activity.requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_CAMERA);
                        }
//                        if (Build.VERSION.SDK_INT >= 23) {//检查相机权限
//                            ArrayList<String> permissions = new ArrayList<>();
//                            if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                                permissions.add(Manifest.permission.CAMERA);
//                            }
//
//                            if (permissions.size() == 0) {//有权限,跳转
//                                //打开相机-兼容7.0
//                                openCamera(activity);
//                            } else {
//                                activity.requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_CAMERA);
//                            }
//                        } else {
//                            //打开相机-兼容7.0
//                            openCamera(activity);
//                        }
                    }
                }).
                setNegativeButton("系统相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //如果有权限申请，请在Activity中onRequestPermissionsResult权限返回里面重新调用openAlbum()
                        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ALBUM);
                        } else {
                            openAlbum(activity);
                        }
                    }
                }).show();//在按键响应事件中显示此对话框
    }
    public static final String PATH_IMAGE = Environment.getExternalStorageDirectory().toString()+"/images";
    /**
     * 打开相机
     * 兼容7.0
     *
     * @param activity
     */
    public static void openCamera(Activity activity, String path) {
        // 创建File对象，用于存储拍照后的图片
        File outputImage = new File(activity.getExternalFilesDir("/images"), path);
        try {
            File file =new  File(activity.getExternalFilesDir("/images").getAbsolutePath());
            if (!file.exists()) {
                file.mkdir();
            }
            if (file.exists()){
//                if (outputImage.exists()) {
//                    outputImage.delete();
//                }
                outputImage.createNewFile();
                if (Build.VERSION.SDK_INT < 24) {
                    imageUri = Uri.fromFile(outputImage);
                } else {
                    //Android 7.0系统开始 使用本地真实的Uri路径不安全,使用FileProvider封装共享Uri
                    //参数二:fileprovider绝对路径 com.dyb.testcamerademo：项目包名
                    imageUri = FileProvider.getUriForFile(activity, "com.liyaan.car.fileprovider", outputImage);
                }
//            imageUri = Uri.fromFile(outputImage);
                // 启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                activity.startActivityForResult(intent, TAKE_PHOTO);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * 打开图库
     * @param activity
     */
    public static void openAlbum(Activity activity) {
        //调用系统图库的意图
        Intent choosePicIntent = new Intent(Intent.ACTION_PICK, null);
//        choosePicIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        choosePicIntent.setType("image/*");
        activity.startActivityForResult(choosePicIntent, CHOOSE_PHOTO);

        //打开系统默认的软件
        //Intent intent = new Intent("android.intent.action.GET_CONTENT");
        //intent.setType("image/*");
        //activity.startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    /**
     * 通过uri获取路径filepath
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;

        final String scheme = uri.getScheme();
        String data = null;

        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 得到byte[]
     * LeanCloud上传文件是需要byte[]数组的
     * 这里对传入的图片Uri压缩，并转换为byte[]后返回
     *
     * @param activity 上下文
     * @param uri      传入图片的Uri
     * @return byte[]
     */
    public static byte[] getImgByteFromUri(Activity activity, Uri uri) throws IOException {
        //先进行尺寸压缩
        Bitmap bitmap = getBitmapFormUri(activity, uri);

        //再进行质量压缩
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);//100表示不压缩，直接放到out里面
        int options = 90;//压缩比例
        while (out.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            out.reset(); // 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, out);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        Log.e("压缩-提交", out.toByteArray().length + "");

        byte[] bs = out.toByteArray();//转换为byte提交

        return bs;
    }

    public static Uri getCompressUri(Activity activity, Uri uri) throws IOException {
        //先进行尺寸压缩
        Bitmap bitmap = getBitmapFormUri(activity, uri);

        Uri uriCompress = Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, null, null));

//        //再进行质量压缩
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);//100表示不压缩，直接放到out里面
//        int options = 90;//压缩比例
//        while (out.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
//            out.reset(); // 重置baos即清空baos
//            bitmap.compress(Bitmap.CompressFormat.JPEG, options, out);// 这里压缩options%，把压缩后的数据存放到baos中
//            options -= 10;// 每次都减少10
//        }
//        Log.e("压缩-提交", out.toByteArray().length + "");
//
//        byte[] bs = out.toByteArray();//转换为byte提交

        return uriCompress;
    }


    /**
     * 图片尺寸压缩
     * <p>
     * 宽度高度不一样：依靠规定的高或宽其一最大值来做界限
     * 高度宽度一样：依照规定的宽度压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以750x450为标准
        float hh = 800f;//这里设置高度为750f
        float ww = 800f;//这里设置宽度为450f
        float sq = 800f;//这里设置正方形为300f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        Log.e("缩放", originalWidth + "..." + originalHeight);
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大，根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高，根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        } else if (originalWidth == originalHeight && originalWidth > sq) {//如果高度和宽度一样，根据任意一边大小缩放
            //be = (int) (originalHeight / sq);
            be = (int) (originalWidth / sq);
        }
        if (be <= 0) {//如果缩放比比1小，那么保持原图不缩放
            be = 1;
        }
        Log.e("缩放", be + "");
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return bitmap;//再进行质量压缩
    }

    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) { // api >= 19
            return getRealPathFromUriAboveApi19(context, uri);
        } else { // api < 19
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
}
