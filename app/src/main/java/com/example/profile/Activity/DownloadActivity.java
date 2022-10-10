package com.example.profile.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.download.library.DownloadImpl;
import com.download.library.DownloadListenerAdapter;
import com.download.library.Extra;
import com.example.profile.Adapter.BackupAdapter;
import com.example.profile.Adapter.DownloadAdapter;
import com.example.profile.Model.BackupModel;
import com.example.profile.Model.DownloadModel;
import com.example.profile.Model.SubCategory;
import com.example.profile.Other.Const;
import com.example.profile.Other.RepeatMethods;
import com.example.profile.R;
import com.google.android.material.tabs.TabLayout;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.profile.Other.Const.ANDROID_DOCID;
import static com.example.profile.Other.Const.EXTERNAL_STORAGE_PROVIDER_AUTHORITY;

public class DownloadActivity extends AppCompatActivity {

    String file_name, mod_name, pack_type, user_id, mod_id,pack_url,world_url;
    Button btn_download, btn_install;
    Dialog downloading_dialog;
    private static final String TAG = "DownloadActivity";
    Activity activity;

    Uri treeUri_minecraft;
    Uri uri22;
    int isDownload_completed = 0;
    LinearLayout lay_upload_related;

    TextView tv_trans_or_delete, tv_progress0, toolbar_title;

    RecyclerView rec_pack_cat, rec_download;
    ArrayList<BackupModel> backupModelArrayList = new ArrayList<>();
    ArrayList<DownloadModel> downloadModelArrayList = new ArrayList<>();
    BackupAdapter backupAdapter;
    DownloadAdapter downloadAdapter;
    ImageView back;
    TextView tv_minecraftWorld, tv_behaviour_pack, tv_resource_pack;
    ViewPager viewPager;
    TabLayout tabLayout;
    public static int int_onresume = 0;
    boolean check_ScrollingUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        activity = DownloadActivity.this;


        file_name = getIntent().getStringExtra("file_name");
        mod_name = getIntent().getStringExtra("mod_name");
        pack_type = getIntent().getStringExtra("pack_type");
        user_id = getIntent().getStringExtra("user_id");
        mod_id = getIntent().getStringExtra("mod_id");
        pack_url = getIntent().getStringExtra("pack_url");
        world_url = getIntent().getStringExtra("world_url");

//        btn_download = findViewById(R.id.btn_download);
//        toolbar_title = findViewById(R.id.toolbar_title);
//        btn_install = findViewById(R.id.btn_install);
        lay_upload_related = findViewById(R.id.lay_upload_related);
        rec_pack_cat = findViewById(R.id.rec_pack_cat);
        rec_download = findViewById(R.id.rec_download);
        tv_minecraftWorld = findViewById(R.id.tv_minecraftWorld);
        tv_resource_pack = findViewById(R.id.tv_resource_pack);
        tv_behaviour_pack = findViewById(R.id.tv_behaviour_pack);

//        back = findViewById(R.id.back);
        getSupportActionBar().setTitle(mod_name);
        @SuppressLint("HardwareIds") String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        if (user_id.equals(m_androidId)) {
            lay_upload_related.setVisibility(View.VISIBLE);
        } else {
            lay_upload_related.setVisibility(View.GONE);

        }
//        toolbar_title.setText(mod_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT >= 26) {
            uri22 = DocumentsContract.buildDocumentUri(
                    EXTERNAL_STORAGE_PROVIDER_AUTHORITY,
                    ANDROID_DOCID
            );
        }

        if (Build.VERSION.SDK_INT >= 26) {
            treeUri_minecraft = DocumentsContract.buildTreeDocumentUri(
                    EXTERNAL_STORAGE_PROVIDER_AUTHORITY,
                    ANDROID_DOCID
            );

        }



//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DownloadActivity.this, profileActivity.class);
//
//
//                startActivity(intent);
//            }
//        });

        LoadDownload();

//        if (new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server/" + file_name + ".zip").exists()) {
//            btn_install.setVisibility(View.VISIBLE);
//            btn_download.setVisibility(View.GONE);
//        }

//        btn_download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                downloadmethod();
//            }
//        });
//
//        btn_install.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (Build.VERSION.SDK_INT >= 30) {
//                    DocumentFile file = DocumentFile.fromTreeUri(activity, treeUri_minecraft);
//
//                    if (file.findFile(file_name) != null) {
//                        if (file.findFile(file_name).exists()) {
////                            Info_Dialog();
////                            downloading_dialog.dismiss();
//                            Toast.makeText(activity, "mod already exists", Toast.LENGTH_SHORT).show();
//                        } else {
//                            downloading_dialog();
//                            new Timer().schedule(new TimerTask() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        new ZipFile(new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server/" + file_name + ".zip")).extractAll(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server");
//                                        copyFile_to_minecraft(0);
//                                    } catch (ZipException e) {
//                                        e.printStackTrace();
//                                        downloading_dialog.dismiss();
//                                    }
//                                }
//
//                            }, 3000);
//
//                        }
//                    } else {
//                        downloading_dialog();
//                        new Timer().schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                try {
//                                    new ZipFile(new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server/" + file_name + ".zip")).extractAll(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server");
//                                    copyFile_to_minecraft(0);
//                                } catch (ZipException e) {
//                                    e.printStackTrace();
//                                    downloading_dialog.dismiss();
//                                }
//
////                                copyFile_to_minecraft( 0);
//                            }
//
//                        }, 3000);
//
//                    }
//
//                } else {
//
//                    String file_path09 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.mojang.minecraftpe/files/games/com.mojang/" + pack_type + "/" + file_name;
//                    if (new File(file_path09).exists()) {
////                        Info_Dialog();
//                        Toast.makeText(activity, "mod already exist", Toast.LENGTH_SHORT).show();
//                    } else {
//                        downloading_dialog();
//                        new Timer().schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                try {
//                                    new ZipFile(new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server/" + file_name + ".zip")).extractAll(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.mojang.minecraftpe/files/games/com.mojang/" + pack_type);
//                                    runOnUiThread(() -> {
//                                        Toast.makeText(activity, "mod restore successfully", Toast.LENGTH_SHORT).show();
//                                        downloading_dialog.dismiss();
//                                    });
//
//                                } catch (ZipException e) {
//                                    runOnUiThread(() -> {
//                                        Toast.makeText(activity, "mod not restore successfully", Toast.LENGTH_SHORT).show();
//                                        downloading_dialog.dismiss();
//                                    });
//                                    e.printStackTrace();
//                                }
//                            }
//
//                        }, 3000);
//
//                    }
//
//
//                }
//            }
//        });

//        rec_pack_cat.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if (dy > 0) {
//                    rec_download.setVisibility(View.GONE);
//                    // Scrolling up
//                } else  if (dy < 0) {
//                    rec_download.setVisibility(View.VISIBLE);
//                    // Scrolling down
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
//                    // Do something
//                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    // Do something
//                } else {
//                    // Do something
//                }
//            }
//        });



//        rec_pack_cat.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0) {
//                    // Scrolling up
//                    if(check_ScrollingUp)
//                    {
//                        rec_download.startAnimation(AnimationUtils
//                                .loadAnimation(DownloadActivity.this,R.anim.trans_upwards));
//                        check_ScrollingUp = true;
//                    }
//
//                } else {
//                    // User scrolls down
//                    if(!check_ScrollingUp )
//                    {
//
//                        rec_download.startAnimation(AnimationUtils.loadAnimation(DownloadActivity.this,R.anim.trans_downwards));
//                        check_ScrollingUp = false;
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//            }
//        });
        tv_minecraftWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_all_modelist("minecraftWorlds");
            }
        });

        tv_behaviour_pack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_all_modelist("behavior_packs");
            }
        });

        tv_resource_pack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_all_modelist("resource_packs");
            }
        });


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void show_all_modelist(String pack) {

        RepeatMethods repeatMethods = new RepeatMethods(DownloadActivity.this);
        backupModelArrayList = repeatMethods.show_all_modelist_new(treeUri_minecraft, pack);


        GridLayoutManager layoutManager = new GridLayoutManager(DownloadActivity.this, 1);
        rec_pack_cat.setLayoutManager(layoutManager);
        rec_pack_cat.setHasFixedSize(true);
        backupAdapter = new BackupAdapter(DownloadActivity.this, backupModelArrayList, pack, 1, mod_id);
//        String[] packs= new String[]{"jj", "hbj", "hj"};
//        final FragmentAdapter adapter = new FragmentAdapter(DownloadActivity.this, getSupportFragmentManager(),packs);

        rec_pack_cat.setAdapter(backupAdapter);

//        viewPager.setAdapter(adapter);
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
//        tabLayout.setupWithViewPager(viewPager);
    }

    public void downloadmethod() {
        downloading_dialog();
        DownloadImpl.getInstance(DownloadActivity.this)
                .url("https://upgradeinfotech.in/projects/mcpe_backup_pro/api/upload/" + file_name + "/" + file_name + ".zip")
                .target(new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server/" + file_name + ".zip"))
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
                            Toast.makeText(activity, "Download Successfully", Toast.LENGTH_SHORT).show();
                            btn_install.setVisibility(View.VISIBLE);
                            btn_download.setVisibility(View.GONE);
                        } else {
                            File file2324 = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server/" + file_name + ".zip");
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
        downloading_dialog = new Dialog(DownloadActivity.this);
        downloading_dialog.setCancelable(false);
        downloading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        downloading_dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        downloading_dialog.setContentView(R.layout.load_dialog);
        tv_progress0 = downloading_dialog.findViewById(R.id.tv_progress0);
        tv_trans_or_delete = downloading_dialog.findViewById(R.id.tv_trans_or_delete);
        downloading_dialog.show();
    }


    private void copyFile_to_minecraft(int from_where) {
        // AssetManager assetManager = getAssets();
        try {
            String mainfilepath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/server/" + file_name;
            File file0 = new File(mainfilepath);

            if (file0.exists()) {
                OutputStream out;
                DocumentFile pickedDir = DocumentFile.fromTreeUri(this, treeUri_minecraft);

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
                            OutputStream out0 = getContentResolver().openOutputStream(newFile.getUri());
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

                runOnUiThread(() -> {
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

            runOnUiThread(() -> {
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
                        OutputStream out0 = getContentResolver().openOutputStream(newFile.getUri());
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

                    runOnUiThread(new Runnable() {
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


    private void LoadDownload() {
        Long tsLong0 = System.currentTimeMillis() / 1000;
        String ts0 = tsLong0.toString();

        RequestQueue queue = Volley.newRequestQueue(DownloadActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, "https://upgradeinfotech.in/projects/mcpe_backup_pro/api/get_download_mods.php?v=" + ts0, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("error101", "response " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

//                         public DownloadModel(String id, String packType, String path, String modId, String size)
                        DownloadModel downloadModel = new DownloadModel(jsonObject.getString("id"), jsonObject.getString("pack_type"), jsonObject.getString("path"), jsonObject.getString("mod_id"), jsonObject.getString("size"));
                        downloadModelArrayList.add(downloadModel);

                        GridLayoutManager layoutManager01 = new GridLayoutManager(DownloadActivity.this, 1);
                        rec_download.setLayoutManager(layoutManager01);
                        rec_download.setHasFixedSize(true);
                        downloadAdapter = new DownloadAdapter(DownloadActivity.this, downloadModelArrayList, treeUri_minecraft,pack_url,world_url);
                        rec_download.setAdapter(downloadAdapter);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
//                    txt_score.setText(Score + "Coins");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.v("error101", error + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("mod_id", mod_id);
                return params;
            }
        };
        queue.add(request);


    }
}