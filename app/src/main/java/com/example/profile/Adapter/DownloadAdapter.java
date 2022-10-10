package com.example.profile.Adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.download.library.DownloadImpl;
import com.download.library.DownloadListenerAdapter;
import com.download.library.Extra;
import com.example.profile.Model.DownloadModel;
import com.example.profile.R;
import com.google.android.material.card.MaterialCardView;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHoder> {

    List<DownloadModel> downloadModelArrayList = new ArrayList<>();
    Dialog downloading_dialog;
    Activity activity;
    private static final String TAG = "ServerModAdapter";
    TextView tv_trans_or_delete, tv_progress0;
    int isDownload_completed = 0;
    Uri treeUri_minecraft;
    String pack_url, world_url;

    public DownloadAdapter(Activity activity, List<DownloadModel> list, Uri treeUri_minecraft,String pack_url,String world_url) {
        downloadModelArrayList = list;
        this.activity = activity;
        this.pack_url = pack_url;
        this.world_url = world_url;
        this.treeUri_minecraft = treeUri_minecraft;
    }

    public void add_list( List<DownloadModel> list) {
        downloadModelArrayList = list;
        notifyDataSetChanged();
    }


    public ViewHoder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHoder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_download_item, viewGroup, false));
    }

    @SuppressLint("NewApi")
    public void onBindViewHolder(final ViewHoder viewHoder, @SuppressLint("RecyclerView") final int i) {
           viewHoder.tv_download.setText("Download "+downloadModelArrayList.get(i).getPackType());

            String file_name32=downloadModelArrayList.get(i).getPath();

//        if(downloadModelArrayList.get(i).getPackType().equals("behavior_packs")||downloadModelArrayList.get(i).getPackType().equals("resource_packs")){
//            Glide.with(activity).load(pack_url).skipMemoryCache(true).into(viewHoder.myimage);
//            Log.d("TAG104", "onBindViewHolder:pack_url "+pack_url);
//        }else{
//            Glide.with(activity).load(world_url).skipMemoryCache(true).into(viewHoder.myimage);
//            Log.d("TAG104", "onBindViewHolder:world_url "+world_url);
//        }

        if (new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server/" + file_name32 + ".zip").exists()) {
            viewHoder.tv_install.setVisibility(View.VISIBLE);
            viewHoder.tv_install.setText("Install "+downloadModelArrayList.get(i).getPackType());
            viewHoder.tv_download.setVisibility(View.GONE);
        }

        viewHoder.tv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadmethod(file_name32,viewHoder,i);
            }
        });

        viewHoder.tv_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                install_method(file_name32,downloadModelArrayList.get(i).getPackType());
//                downloadmethod(downloadModelArrayList.get(i).getPath(),viewHoder);
            }
        });
    }

    private void install_method(String file_name, String pack_type) {

        if (Build.VERSION.SDK_INT >= 30) {
            DocumentFile file212 = DocumentFile.fromTreeUri(activity, treeUri_minecraft);
            DocumentFile file =file212.findFile(pack_type);

            if (file.findFile(file_name) != null) {
                if (file.findFile(file_name).exists()) {
//                            Info_Dialog();
//                            downloading_dialog.dismiss();
                    Toast.makeText(activity, "mod already exists", Toast.LENGTH_SHORT).show();
                } else {
                    downloading_dialog();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                new ZipFile(new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server/" + file_name + ".zip")).extractAll(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server");
                                copyFile_to_minecraft(0,file_name,pack_type);
                            } catch (ZipException e) {
                                e.printStackTrace();
                                downloading_dialog.dismiss();
                            }
                        }

                    }, 3000);

                }
            } else {
                downloading_dialog();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            new ZipFile(new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server/" + file_name + ".zip")).extractAll(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server");
                            copyFile_to_minecraft(0,file_name,pack_type);
                        } catch (ZipException e) {
                            e.printStackTrace();
                            downloading_dialog.dismiss();
                        }

//                                copyFile_to_minecraft( 0);
                    }

                }, 3000);

            }

        } else {

            String file_path09 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.mojang.minecraftpe/files/games/com.mojang/" + pack_type + "/" + file_name;

            Log.d("TAG012", "install_method: "+Environment.getExternalStorageDirectory().getAbsolutePath());
            Log.d("TAG012", "install_method: "+pack_type);
            Log.d("TAG012", "install_method: "+file_name);

            if (new File(file_path09).exists()) {

//                        Info_Dialog();
                Toast.makeText(activity, "mod already exist", Toast.LENGTH_SHORT).show();
            } else {
                downloading_dialog();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            new ZipFile(new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server/" + file_name + ".zip")).extractAll(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.mojang.minecraftpe/files/games/com.mojang/" + pack_type);
                            Log.d("TAG012", "install_method: "+activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath());


                            activity.runOnUiThread(() -> {
                                Toast.makeText(activity, "mod restore successfully", Toast.LENGTH_SHORT).show();
                                downloading_dialog.dismiss();
                            });

                        } catch (ZipException e) {
                            Log.d("TAG012", "install_method: "+activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath());

                            activity.runOnUiThread(() -> {
                                Toast.makeText(activity, "mod not restore successfully", Toast.LENGTH_SHORT).show();
                                downloading_dialog.dismiss();
                            });
                            e.printStackTrace();
                        }
                    }

                }, 3000);

            }


        }
    }


    public int getItemCount() {
        return downloadModelArrayList.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {

        Button tv_download,tv_install;
        ImageView myimage;

        public ViewHoder(View view) {
            super(view);
            tv_download = view.findViewById(R.id.tv_download);
            tv_install = view.findViewById(R.id.tv_install);
//            myimage = view.findViewById(R.id.myimage);

        }
    }


    public void downloadmethod(String path, ViewHoder viewHoder,int i) {
        downloading_dialog();
        DownloadImpl.getInstance(activity)
                .url("https://upgradeinfotech.in/projects/mcpe_backup_pro/api/upload/" + path + "/" + path + ".zip")
                .target(new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server/" + path + ".zip"))
                .setUniquePath(false)
                .setForceDownload(true)
                .enqueue(new DownloadListenerAdapter() {
                    @Override
                    public void onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, Extra extra) {
                        super.onStart(url, userAgent, contentDisposition, mimetype, contentLength, extra);

                    }

                    @Override
                    public void onProgress(String url, long downloaded, long length, long usedTime) {
                        super.onProgress(url, downloaded, length, usedTime);
//                        progressDialog.setMessage("Downloading file. Please wait... ");
                        long dl_progress = (downloaded * 100 / length);
                        Log.d("down12", "onProgress " + Math.round(dl_progress));

                        if (Math.round(dl_progress) == 100) {
                            isDownload_completed = 1;
                        }
                    }

                    @Override
                    public boolean onResult(Throwable throwable, Uri path, String url, Extra extra) {
                        downloading_dialog.dismiss();
                        if (isDownload_completed == 1) {
                            viewHoder.tv_install.setVisibility(View.VISIBLE);
                            viewHoder.tv_install.setText("Install "+downloadModelArrayList.get(i).getPackType());

                            viewHoder.tv_download.setVisibility(View.GONE);
                            Toast.makeText(activity, "Download Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            File file2324 = new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server/" + path + ".zip");
                            if (file2324.exists()) {
                                file2324.delete();
                            }
                            Toast.makeText(activity, "Download failed try again", Toast.LENGTH_SHORT).show();
                        }

                        return super.onResult(throwable, path, url, extra);
                    }

                    @Override
                    public void onDownloadStatusChanged(Extra extra, int status) {
                        super.onDownloadStatusChanged(extra, status);
                        Log.d(TAG, "onDownloadStatusChanged: ssssssssssssssss" + status);
                    }
                });
    }


    public void downloading_dialog() {
        downloading_dialog = new Dialog(activity);
        downloading_dialog.setCancelable(false);
        downloading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        downloading_dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        downloading_dialog.setContentView(R.layout.load_dialog);
        tv_progress0 = downloading_dialog.findViewById(R.id.tv_progress0);
        tv_trans_or_delete = downloading_dialog.findViewById(R.id.tv_trans_or_delete);
        downloading_dialog.show();
    }


    private void copyFile_to_minecraft(int from_where,String file_name,String pack_type) {
        // AssetManager assetManager = getAssets();
        try {
            String mainfilepath = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server/" + file_name;
            File file0 = new File(mainfilepath);

            if (file0.exists()) {
                OutputStream out;
                DocumentFile pickedDir0101 = DocumentFile.fromTreeUri(activity, treeUri_minecraft);
                DocumentFile pickedDir =pickedDir0101.findFile(pack_type);

                DocumentFile newFile111;
                if (from_where == 0) {
                    newFile111 = pickedDir.findFile(file_name);

                    if (newFile111 == null) {
                        newFile111 = pickedDir.createDirectory(file_name);
                    }
                } else {
                    String file09 = "copy-" + file_name;
                    newFile111 = pickedDir.findFile(file09);

                    if (newFile111 == null) {
                        newFile111 = pickedDir.createDirectory(file09);
                    }
                }


                String[] files = file0.list();

                for (int i = 0; i < files.length; i++) {
                    // System.out.println(files[i]);

                    Log.d("files", "" + files[i]);
                    File subfile = new File(mainfilepath + "/" + files[i]);
                    if (subfile.isDirectory()) {
                        assert pickedDir != null;
                        DocumentFile existing = newFile111.findFile(files[i]);
                        if (existing != null) {
                            fillTheDirectory_11(existing, mainfilepath, files[i]);
                        } else {
                            DocumentFile newFile00 = newFile111.createDirectory(files[i]);
                            fillTheDirectory_11(newFile00, mainfilepath, files[i]);
                        }
                        //   createDirectory(pickedDir);
                    } else if (subfile.isFile()) {
                        try {
                            InputStream inn1 = new FileInputStream(subfile);

                            assert pickedDir != null;
                            DocumentFile existing = newFile111.findFile(files[i]);
                            if (existing != null)
                                existing.delete();
                            String string = files[i];
                            String[] parts = string.split("\\.");
                            DocumentFile newFile = newFile111.createFile("*/" + parts[1], files[i]);
                            // DocumentFile newFile = pickedDir.createDirectory("Download");
                            assert newFile != null;
                            OutputStream out0 = activity.getContentResolver().openOutputStream(newFile.getUri());
                            byte[] buffer = new byte[1024];
                            int read;
                            while ((read = inn1.read(buffer)) != -1) {
                                out0.write(buffer, 0, read);
                            }
                            inn1.close();
                            out0.flush();
                            out0.close();

                        } catch (Exception fnfe1) {
                            fnfe1.printStackTrace();
                        }
                    }

                }


                Log.d(TAG, "copyFile_latest: All file transfer completed ");

                activity.runOnUiThread(() -> {
                    tv_progress0.setText("Completed");
                    downloading_dialog.dismiss();
                    if (from_where == 0) {
                        Toast.makeText(activity, "restore successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "clone successful", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        } catch (Exception e) {

            activity.runOnUiThread(() -> {
                downloading_dialog.dismiss();
                if (from_where == 0) {
                    Toast.makeText(activity, "restore failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "clone failed", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void fillTheDirectory_11(DocumentFile newFile111, String mainfilepath, String fileName) {
        File file0 = new File(mainfilepath + "/" + fileName);
        //   boolean a=file0.isDirectory();

        String[] files = file0.list();
        for (int i = 0; i < files.length; i++) {

            Log.d("dirfiles", "" + files[i]);
            String path = mainfilepath + "/" + fileName;
            File subfile = new File(path + "/" + files[i]);
            if (subfile.isDirectory()) {
                //   assert pickedDir != null;
                DocumentFile existing = newFile111.findFile(files[i]);
                if (existing != null) {
                    fillTheDirectory_11(existing, path, files[i]);
                } else {
                    DocumentFile newFile00 = newFile111.createDirectory(files[i]);
                    fillTheDirectory_11(newFile00, path, files[i]);
                }
                //   createDirectory(pickedDir);
            } else if (subfile.isFile()) {
                try {
                    InputStream inn1 = new FileInputStream(subfile);

                    // assert pickedDir != null;
                    DocumentFile existing = newFile111.findFile(files[i]);
                    if (existing != null)
                        existing.delete();
                    String string = files[i];
                    String[] parts = string.split("\\.");
                    String part1 = parts[0]; // 004
                    String part2 = parts[1]; // 034556
                    DocumentFile newFile = newFile111.createFile("*/" + parts[1], files[i]);
                    // DocumentFile newFile = pickedDir.createDirectory("Download");
                    if (newFile != null) {
                        OutputStream out0 = activity.getContentResolver().openOutputStream(newFile.getUri());
                        byte[] buffer = new byte[1024];
                        int read;
                        while ((read = inn1.read(buffer)) != -1) {
                            out0.write(buffer, 0, read);
                        }
                        inn1.close();
                        out0.flush();
                        out0.close();
                    }


                    String text_progress = subfile.getName();

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_progress0.setText(text_progress);
                        }
                    });


                } catch (Exception fnfe1) {
                    fnfe1.printStackTrace();
                }
            }
//            complete_count++;
        }

    }

}


