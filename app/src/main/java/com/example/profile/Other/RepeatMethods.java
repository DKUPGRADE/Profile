package com.example.profile.Other;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;


import com.example.profile.Model.BackupModel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RepeatMethods {

    private static final String TAG = "RepeatMethods";
    ArrayList<BackupModel> backupModelArrayList = new ArrayList<>();
    Activity activity;
    String pack_category;

    public RepeatMethods(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<BackupModel> show_all_modelist_new(Uri treeUri22, String pack_category) {
        this.pack_category = pack_category;

//        pickedDir.listFiles()[0]

        if (Build.VERSION.SDK_INT >= 30) {

            DocumentFile pickedDir1000 = DocumentFile.fromTreeUri(activity, treeUri22);
            DocumentFile pickedDir = pickedDir1000.findFile(pack_category);

            if (pickedDir.exists()) {
                DocumentFile[] files = pickedDir.listFiles();

                for (int i = 0; i < files.length; i++) {
                    Log.d(TAG, "show_all_modelist: " + files[i].getName());

                    if (files[i] != null) {
                        DocumentFile[] subFiles = files[i].listFiles();
                        String mod_name = null;
                        String img_path = null;
                        String name_file_path = null;

                        for (int j = 0; j < subFiles.length; j++) {

                            if (pack_category.equals("minecraftWorlds")) {
                                if (subFiles[j].getName().contains("levelname.txt")) {
                                    mod_name = read_mod_name_from_text_file11(subFiles[j]);
                                    name_file_path = String.valueOf(subFiles[j].getUri());
                                } else if (subFiles[j].getName().contains("world_icon.jpeg")) {
                                    img_path = String.valueOf(subFiles[j].getUri());
                                }
                            } else {
                                if (subFiles[j].getName().contains("manifest.json")) {
                                    mod_name = read_mod_name_from_json11(subFiles[j]);

                                    try {
                                        JSONObject obj = new JSONObject(mod_name);
                                        mod_name = obj.getJSONObject("header").getString("name");

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    name_file_path = String.valueOf(subFiles[j].getUri());
                                } else if (subFiles[j].getName().contains("pack_icon.png")) {
                                    img_path = String.valueOf(subFiles[j].getUri());
                                }
                            }


                        }
                        String uri = String.valueOf(files[i].getUri());
                        BackupModel backupModel = new BackupModel(mod_name, uri, img_path, name_file_path, files[i].getName(), 0);
                        backupModelArrayList.add(backupModel);
                    }
                }

            }

            add_from_backup_11_and_above();


        } else {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.mojang.minecraftpe/files/games/com.mojang/" + pack_category);

            if (file.exists()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i] != null) {
                        File[] subFiles = files[i].listFiles();
                        String mod_name = null;
                        String img_path = null;
                        String name_file_path = null;

                        for (int j = 0; j < subFiles.length; j++) {

                            if (pack_category.equals("minecraftWorlds")) {
                                if (subFiles[j].getName().contains("levelname.txt")) {
                                    mod_name = read_mod_name_from_text_file(subFiles[j]);
                                    name_file_path = subFiles[j].getPath();
                                } else if (subFiles[j].getName().contains("world_icon.jpeg")) {
                                    img_path = subFiles[j].getPath();
                                }
                            } else {
                                if (subFiles[j].getName().contains("manifest.json")) {
//                                mod_name = read_mod_name_from_text_file(subFiles[j]);

                                    mod_name = read_mod_name_from_json(subFiles[j]);

                                    try {
                                        JSONObject obj = new JSONObject(mod_name);
                                        mod_name = obj.getJSONObject("header").getString("name");

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    name_file_path = subFiles[j].getPath();
                                } else if (subFiles[j].getName().contains("world_icon.jpeg") || subFiles[j].getName().contains("pack_icon.png")) {
                                    img_path = subFiles[j].getPath();
                                }
                            }


                        }
                        BackupModel backupModel = new BackupModel(mod_name, files[i].getPath(), img_path, name_file_path, files[i].getName(), 0);
                        backupModelArrayList.add(backupModel);
                    }
                }
            }

            add_from_backup();

        }


//        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 1);
//        rec_available_list.setLayoutManager(layoutManager);
//        rec_available_list.setHasFixedSize(true);
//        backupAdapter = new BackupAdapter(MainActivity.this, backupModelArrayList,"minecraftWorlds");
//        rec_available_list.setAdapter(backupAdapter);

        return backupModelArrayList;
    }


    private void add_from_backup_11_and_above() {

        String mainfilepath777 = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + pack_category + "/";
        File file777 = new File(mainfilepath777);

        if (!file777.exists()) {
            file777.mkdir();
        }

        DocumentFile pickedDir = DocumentFile.fromFile(file777);

        if (pickedDir.exists()) {
            DocumentFile[] files = pickedDir.listFiles();

            for (int i = 0; i < files.length; i++) {
                Log.d(TAG, "show_all_modelist: " + files[i].getName());

                if (files[i].getName().equals("server") || files[i].getName().contains(".zip")) {
                    continue;
                }

                if (files[i] != null) {
                    DocumentFile[] subFiles = files[i].listFiles();
                    String mod_name = null;
                    String img_path = null;
                    String name_file_path = null;

                    for (int j = 0; j < subFiles.length; j++) {
                        if (subFiles[j].getName().contains("levelname.txt")) {
                            mod_name = read_mod_name_from_text_file11(subFiles[j]);
                            name_file_path = String.valueOf(subFiles[j].getUri());
                        } else if (subFiles[j].getName().contains("world_icon.jpeg")) {
                            img_path = String.valueOf(subFiles[j].getUri());
                        }else if (subFiles[j].getName().contains("pack_icon.png")) {
                            img_path = String.valueOf(subFiles[j].getUri());
                        }
                    }

                    boolean mode_not_exist = true;
                    for (int j = 0; j < backupModelArrayList.size(); j++) {
                        DocumentFile file1 = DocumentFile.fromSingleUri(activity, Uri.parse(backupModelArrayList.get(j).getFilepath()));
//                        File file1=new File(backupModelArrayList.get(j).getFilepath());
                        if (files[i].getName().equals(file1.getName())) {
                            backupModelArrayList.get(j).setIsBackupExists(1);
                            mode_not_exist = false;
                            break;
                        }
                    }

                    if (mode_not_exist) {
                        String uri = String.valueOf(files[i].getUri());
                        BackupModel backupModel = new BackupModel(mod_name, uri, img_path, name_file_path, files[i].getName(), 2);
                        backupModelArrayList.add(backupModel);
                    }


                }
            }

        }
    }

    private void add_from_backup() {

        File file = new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/" + pack_category + "/");

        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i] != null) {
                    File[] subFiles = files[i].listFiles();
                    String mod_name = null;
                    String img_path = null;
                    String name_file_path = null;

                    for (int j = 0; j < subFiles.length; j++) {
                        if (subFiles[j].getName().contains("levelname.txt")) {
                            mod_name = read_mod_name_from_text_file(subFiles[j]);
                            name_file_path = subFiles[j].getPath();
                        } else if (subFiles[j].getName().contains("world_icon.jpeg")) {
                            img_path = subFiles[j].getPath();
                        }
                    }

                    boolean mode_not_exist = true;
                    for (int j = 0; j < backupModelArrayList.size(); j++) {
                        File file1 = new File(backupModelArrayList.get(j).getFilepath());
                        if (files[i].getName().equals(file1.getName())) {
                            backupModelArrayList.get(j).setIsBackupExists(1);
                            mode_not_exist = false;
                            break;
                        }
                    }

                    if (mode_not_exist) {
                        BackupModel backupModel = new BackupModel(mod_name, files[i].getPath(), img_path, name_file_path, files[i].getName(), 2);
                        backupModelArrayList.add(backupModel);
                    }


                }
            }
        }

    }


    private String read_mod_name_from_text_file(File file) {
        try {
            String string = "";
            StringBuilder stringBuilder = new StringBuilder();
            String title = null;
            InputStream is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while (true) {
                try {
                    if ((string = reader.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (title != null) {
                    title = title + string;
                } else {
                    title = string;
                }
            }

            is.close();
            return title;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    private String read_mod_name_from_text_file11(DocumentFile file) {
        try {
            String string = "";
            StringBuilder stringBuilder = new StringBuilder();

            String title = null;
            InputStream is = activity.getContentResolver().openInputStream(file.getUri());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while (true) {
                try {
                    if ((string = reader.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (title != null) {
                    title = title + string;
                } else {
                    title = string;

                }

            }

            is.close();
            return title;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }


    private String read_mod_name_from_json11(DocumentFile subFile) {

        String json = null;
        try {
            InputStream is = activity.getContentResolver().openInputStream(subFile.getUri());
//            InputStream is = getActivity().getAssets().open("yourfilename.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }


        return json;
    }

    private String read_mod_name_from_json(File subFile) {

        String json = null;
        try {
//            InputStream is = this.getContentResolver().openInputStream(subFile.getUri());
            InputStream is = new FileInputStream(subFile);
//            InputStream is = getActivity().getAssets().open("yourfilename.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }


        return json;
    }

    public static class ImageFilePath {

        /**
         * Method for return file path of Gallery image
         *
         * @param context
         * @param uri
         * @return path of the selected image file from gallery
         */
        static String nopath = "Select Video Only";

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @SuppressLint("NewApi")
        public static String getPath(final Context context, final Uri uri) {

            // check here to KITKAT or new version
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/"
                                + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] { split[1] };

                    return getDataColumn(context, contentUri, selection,
                            selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

            return nopath;
        }


        public static String getDataColumn(Context context, Uri uri,
                                           String selection, String[] selectionArgs) {

            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = { column };

            try {
                cursor = context.getContentResolver().query(uri, projection,
                        selection, selectionArgs, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return nopath;
        }

        /**
         * @param uri
         *            The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        public static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri
                    .getAuthority());
        }

        /**
         * @param uri
         *            The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        public static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri
                    .getAuthority());
        }

        /**
         * @param uri
         *            The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        public static boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri
                    .getAuthority());
        }

        /**
         * @param uri
         *            The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        public static boolean isGooglePhotosUri(Uri uri) {
            return "com.google.android.apps.photos.content".equals(uri
                    .getAuthority());
        }
    }

}
