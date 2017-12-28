package com.example.oleg.restaurants.stores;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import com.example.oleg.restaurants.network.DownloadTaskCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by oleg on 15.11.17.
 */

public class ImageManager extends AsyncTask<String , Integer, ImageManager.Result> {

    private WeakReference<Context> context;
    private final static String PICTURES_DIR = "pictures";
    private ImageStores imageStores;
    private DownloadTaskCallback<Result> callback;
    private int position;
    private int id;

    public ImageManager(Context context, DownloadTaskCallback<Result> callback, int id ,int position) {
        this.context = new WeakReference<>(context);
        imageStores = ImageStores.initInstance();
        this.callback = callback;
        this.position = position;
        this.id = id;
    }

    @Override
    protected Result doInBackground(String ... params) {
        String url;
        try {
            if(params.length>0) {
                url = params[0];
            } else {
                throw new Exception("Error ImageManager need params url");
            }
            Bitmap photo = getImage(url);
            return new Result(photo);
        } catch (Exception e) {
            return new Result(e);
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        if (result.mResultValue!= null) {
            result.position = position;
        }
        result.manager = this;
        callback.finishDownloading(result);
    }

    Bitmap getImage(String url) throws Exception {
        Bitmap photo;
        File dir = null;
        if (context.get() == null) {
            return null;
        }
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (ContextCompat.checkSelfPermission(context.get(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                dir = new File(context.get().getExternalCacheDir().getPath(), PICTURES_DIR);
            }
        }
        if (dir == null) {
            dir = new File(context.get().getCacheDir(), PICTURES_DIR);
        }
        photo = getImageFromDir(dir.getPath());
        if (photo == null) {
            photo = getImageFromUrl(url);
            if (photo!=null) {
                File photoFile = new File(dir, String.valueOf(id));
                try {
                    photoFile.createNewFile();
                } catch (Exception e) {
                    System.out.println("Error create file");
                }
                FileOutputStream is = new FileOutputStream(photoFile);
                is.write(photo.getRowBytes());
                is.close();
            }
        }
        if (photo != null) {
            imageStores.addBitmapToMemoryCache(String.valueOf(id), photo);
        }
        return photo;
    }

    private Bitmap getImageFromDir(String path) throws Exception {
        Bitmap photo = null;
        File file = new File(path);
        if (file.isFile()) { file.delete(); }
        if (!file.isDirectory()) {
            if (!file.mkdir()){
                throw new Exception("External Error");
            }
        }
        File photoFile = new File(file, String.valueOf(id));
        if (photoFile.isFile()) {
            try(FileInputStream is = new FileInputStream(photoFile.getPath())){
                photo = BitmapFactory.decodeStream(is);
            }
        }
        return photo;
    }

    private Bitmap getImageFromUrl(String url) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }

    public static class Result{
        public Bitmap mResultValue;
        public Exception mException;
        public int position;
        public ImageManager manager;
        Result(Bitmap resultValue) {
            mResultValue = resultValue;
        }
        Result(Exception exception) {
            mException = exception;
        }
    }

}
