package com.example.profile.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.profile.Adapter.MyItem;
import com.example.profile.Model.FileModel;
import com.example.profile.Other.FileUploadService;
import com.example.profile.Other.RepeatMethods;
import com.example.profile.R;
import com.example.profile.fragment.User_home;
import com.example.profile.fragment.You;
import com.google.android.material.textfield.TextInputEditText;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BackupAndRestoreActivity extends AppCompatActivity {

    Button btn_backup, btn_restore, btn_delete_pack, btn_upload, btn_upload_img;
    Activity activity;
    Uri treeUri;
    String mod_file_path, mod_name_file_path, mod_image_file_path;
    TextView toolbar_title;
    private static final String TAG = "BackupAndRestoreActivit";
    Uri treeUri_minecraft;
    Uri uri22;
    String file_name500;
    private final String EXTERNAL_STORAGE_PROVIDER_AUTHORITY = "com.android.externalstorage.documents";

    private final String ANDROID_DOCID = "primary:Android/data/com.mojang.minecraftpe/files/games/com.mojang";
    Dialog dialog1010, transfer_loading_dialog, delete_loading_Dialog;
    String mod_name;
    TextView tv_progress0, tv_trans_or_delete;
    public static TextView tv_progress_delete1212;
    String isBackupExist121;
    int upload_complete = 0;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    String title_for_path, pack_category, fromWhere, mod_id;
    int PICK_IMAGE_MULTIPLE = 1;
    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    ArrayList<Uri> Actual_mArrayUri = new ArrayList<Uri>();
    ImageView imageView, back, btn_add_tag;
    RecyclerView rec_img;
    FlexibleAdapter<IFlexible> adapter;
    ArrayList<Integer> actualPositionList = new ArrayList<>();
    TagContainerLayout mTagContainerLayout;
    List<String> tags = new ArrayList<>();
    String all_tag;
    ScrollView scroll_view;
    CardView btn_select_img;
    TextInputEditText et_description,et_add_tag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_and_restore);


        profileActivity.ac = 1;
        activity = BackupAndRestoreActivity.this;

        btn_backup = findViewById(R.id.btn_backup_pack);
        btn_restore = findViewById(R.id.btn_restore);
//        toolbar_title = findViewById(R.id.toolbar_title);
        btn_delete_pack = findViewById(R.id.btn_delete_pack);
        btn_upload = findViewById(R.id.btn_upload);
        et_description = findViewById(R.id.et_description);
        btn_select_img = findViewById(R.id.select_image);
        imageView = findViewById(R.id.imageView);
//        back = findViewById(R.id.back);
        rec_img = findViewById(R.id.rec_img);
        scroll_view = findViewById(R.id.scroll_view);
//        btn_upload_img = findViewById(R.id.btn_upload_img);
        btn_add_tag = findViewById(R.id.btn_add_tag);
        et_add_tag = findViewById(R.id.et_add_tag);


        mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tagcontainerLayout);
        mTagContainerLayout.setTags(tags);


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (!(Build.VERSION.SDK_INT >= 30)) {
            mod_file_path = getIntent().getStringExtra("file_path");
        } else {
            treeUri = Uri.parse(getIntent().getStringExtra("string_treeUri"));
        }
        file_name500 = getIntent().getStringExtra("file_name");
        mod_name = getIntent().getStringExtra("mod_name");
        isBackupExist121 = getIntent().getStringExtra("isBackupExist");
        pack_category = getIntent().getStringExtra("pack_category");
        fromWhere = getIntent().getStringExtra("fromWhere");
        mod_id = getIntent().getStringExtra("mod_id");


        getSupportActionBar().setTitle(mod_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        toolbar_title.setText(mod_name);
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(activity, profileActivity.class);
//                startActivity(intent);
//            }
//        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uri22 = DocumentsContract.buildDocumentUri(
                    EXTERNAL_STORAGE_PROVIDER_AUTHORITY,
                    ANDROID_DOCID
            );
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            treeUri_minecraft = DocumentsContract.buildTreeDocumentUri(
                    EXTERNAL_STORAGE_PROVIDER_AUTHORITY,
                    ANDROID_DOCID
            );

        }


        mod_name_file_path = getIntent().getStringExtra("mod_name_file_path");
        mod_image_file_path = getIntent().getStringExtra("mod_image_file_path");


        btn_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transfer_loading_Dialog();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (Build.VERSION.SDK_INT >= 30) {
                            copyFile_latest(treeUri);
                        } else {
                            try {
                                File mod_file = new File(mod_file_path);
                                String mod_file_name = mod_file.getName();

                                String outputPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/" + pack_category + "/" + mod_file_name;
                                //create output directory if it doesn't exist
                                File dir = new File(outputPath);
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }

                                copyFile(mod_name_file_path, outputPath);
                                copyFile(mod_image_file_path, outputPath);
                                new ZipFile(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/" + pack_category + "/" + mod_file_name + "/" + mod_file_name + ".zip").addFolder(mod_file);

                                runOnUiThread(() -> {
                                    transfer_loading_dialog.dismiss();
                                    Toast.makeText(activity, "Backup taken successfully", Toast.LENGTH_SHORT).show();
                                });

                            } catch (ZipException e) {
                                e.printStackTrace();

                                runOnUiThread(() -> {
                                    Toast.makeText(activity, "Fail to take Backup", Toast.LENGTH_SHORT).show();
                                    transfer_loading_dialog.dismiss();
                                });

                            }
                        }
                    }
                }, 3000);
            }
        });

        btn_restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 30) {
                    DocumentFile file1000 = DocumentFile.fromTreeUri(activity, treeUri_minecraft);
                    DocumentFile file = file1000.findFile(pack_category);


                    if (file.findFile(file_name500) != null) {
                        if (file.findFile(file_name500).exists()) {
                            Info_Dialog();
                        } else {
                            transfer_loading_Dialog();
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    copyFile_to_minecraft(treeUri, 0);
                                }

                            }, 3000);

                        }
                    } else {
                        transfer_loading_Dialog();
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                copyFile_to_minecraft(treeUri, 0);
                            }

                        }, 3000);

                    }

                } else {

                    File mod_file = new File(mod_file_path);
                    String mod_file_name = mod_file.getName();
                    String file_path09 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.mojang.minecraftpe/files/games/com.mojang/" + pack_category + "/" + mod_file_name;
                    if (new File(file_path09).exists()) {
                        Info_Dialog();
                    } else {
                        transfer_loading_Dialog();
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    new ZipFile(new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/" + pack_category + "/" + mod_file_name + "/" + mod_file_name + ".zip")).extractAll(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.mojang.minecraftpe/files/games/com.mojang/" + pack_category);
                                    runOnUiThread(() -> {
                                        Toast.makeText(activity, "mod restore successfully", Toast.LENGTH_SHORT).show();
                                        transfer_loading_dialog.dismiss();
                                    });

                                } catch (ZipException e) {
                                    runOnUiThread(() -> {
                                        Toast.makeText(activity, "mod not restore successfully", Toast.LENGTH_SHORT).show();
                                        transfer_loading_dialog.dismiss();
                                    });
                                    e.printStackTrace();
                                }
                            }

                        }, 3000);

                    }


                }

            }
        });

        btn_delete_pack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 30) {
                    DocumentFile pickedDir1000 = DocumentFile.fromTreeUri(BackupAndRestoreActivity.this, treeUri_minecraft);
                    DocumentFile pickedDir = pickedDir1000.findFile(pack_category);
//                    DocumentFile pickedDir = DocumentFile.fromTreeUri(activity, treeUri).findFile("minecraftWorlds");

                    delete_loading_Dialog();

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            deleteDirectory(pickedDir.findFile(file_name500));

                            runOnUiThread(() -> {
                                Log.d(TAG, "Info_Dialog: delete complete");
                                Toast.makeText(activity, "successfully deleted", Toast.LENGTH_SHORT).show();
                                delete_loading_Dialog.dismiss();
                            });

                        }

                    }, 3000);
                } else {
                    delete_loading_Dialog();

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (deleteDirectory_below11(new File(mod_file_path))) {
                                runOnUiThread(() -> {
                                    delete_loading_Dialog.dismiss();
                                    Toast.makeText(activity, "successfully deleted", Toast.LENGTH_SHORT).show();
                                });

                            } else {
                                runOnUiThread(() -> {
                                    delete_loading_Dialog.dismiss();
                                    Toast.makeText(activity, "error in deleting", Toast.LENGTH_SHORT).show();
                                });
                            }
                        }

                    }, 3000);

                }


            }
        });
//        et_add_tag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scroll_view.scrollTo(0, scroll_view.getBottom());
//            }
//        });
        btn_upload.setOnClickListener(view -> {
            if (et_description.getText().toString().isEmpty() || et_description.getText().toString().equals("")) {
                Toast.makeText(activity, "Add Description Please", Toast.LENGTH_SHORT).show();
                return;
            }

            upload_complete = 0;
            transfer_loading_Dialog();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {


                    if (Build.VERSION.SDK_INT >= 30) {
                        DocumentFile craftfilepath = DocumentFile.fromTreeUri(activity, treeUri);
                        String file_name12 = DocumentFile.fromSingleUri(activity, treeUri).getName();
                        DocumentFile file0 = craftfilepath.findFile(file_name12);

                        File file020 = new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + pack_category + "/" + file_name500);

                        if (file020.exists()) {
                            try {
                                File file_030 = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + pack_category + "/" + file_name12 + ".zip");
                                if (!file_030.exists()) {
                                    new ZipFile(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + pack_category + "/" + file_name12 + ".zip").addFolder(file020);
                                }

                                if (fromWhere.equals("1")) {
                                    add_extra_resources();
                                } else {
                                    add_mod_name_to_server();
                                }

                            } catch (ZipException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (fromWhere.equals("1")) {
                            add_extra_resources();
                        } else {
                            add_mod_name_to_server();
                        }
                    }
                }
            }, 3000);
        });

        btn_select_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mArrayUri.clear();
                Intent intent = new Intent();

                // setting type to select to be image
                intent.setType("image/*");

                // allowing multiple image to be selected
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });


        btn_add_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag_text = et_add_tag.getText().toString();
                if (tag_text != null && !tag_text.equals("")) {
                    mTagContainerLayout.addTag(tag_text);
                    et_add_tag.setText("");
                } else {
                    Toast.makeText(activity, "Text can't be blank ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {

            @Override
            public void onTagClick(int position, String text) {
                // ...
            }

            @Override
            public void onTagLongClick(final int position, String text) {
                // ...
            }

            @Override
            public void onSelectedTagDrag(int position, String text) {
                // ...
            }

            @Override
            public void onTagCrossClick(int position) {
                mTagContainerLayout.removeTag(position);
                // ...
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // When an Image is picked
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            // Get the Image from data

            btn_select_img.setVisibility(View.GONE);
            rec_img.setVisibility(View.VISIBLE);
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();

                int cout = data.getClipData().getItemCount();
                actualPositionList.clear();
                for (int i = 0; i < cout; i++) {
                    actualPositionList.add(i);
                    // adding imageuri in array
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();

                    mArrayUri.add(imageurl);
                }

//                uploadImages();
//                btn_upload_img.setVisibility(View.VISIBLE);
                // setting 1st selected image into image switcher


                setRecyclerview();
                imageView.setImageURI(mArrayUri.get(0));
//                position = 0;
            } else {
                Uri imageurl = data.getData();
                mArrayUri.add(imageurl);

//                uploadImages();
//                btn_upload_img.setVisibility(View.VISIBLE);
                setRecyclerview();
                imageView.setImageURI(mArrayUri.get(0));
//                position = 0;
            }
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    public void setRecyclerview() {

        List<IFlexible> myItems = getDatabaseList();

// Initialize the Adapter

        GridLayoutManager layoutManager = new GridLayoutManager(BackupAndRestoreActivity.this, 3);
        rec_img.setLayoutManager(layoutManager);
        rec_img.setHasFixedSize(true);
        Object lis = null;
//         adapter = new FlexibleAdapter<>(myItems, new FlexibleAdapter.OnItemClickListener() {
//             @Override
//             public boolean onItemClick(View view, int position) {
//                 Toast.makeText(activity, " "+position, Toast.LENGTH_SHORT).show();
//                 return false;
//             }
//         });

        adapter = new FlexibleAdapter<>(myItems, new FlexibleAdapter.OnItemMoveListener() {
            @Override
            public void onActionStateChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                Log.d(TAG, "onActionStateChanged: actionState :" + actionState);
            }

            @Override
            public boolean shouldMoveItem(int fromPosition, int toPosition) {
                Log.d(TAG, "shouldMoveItem: fromPosition:" + fromPosition + " toPosition:" + toPosition);
                return true;
            }

            @Override
            public void onItemMove(int fromPosition, int toPosition) {
                Log.d(TAG, "onItemMove: fromPosition:" + fromPosition + " toPosition:" + toPosition);
                ArrayList<Integer> dummyActualPositionList = new ArrayList<>();
//                         dummyActualPositionList=actualPositionList;
                for (int i = 0; i < actualPositionList.size(); i++) {
                    dummyActualPositionList.add(actualPositionList.get(i));
                }
                if (fromPosition == toPosition + 1 || fromPosition == toPosition - 1) {
                    actualPositionList.set(fromPosition, dummyActualPositionList.get(toPosition));
                    actualPositionList.set(toPosition, dummyActualPositionList.get(fromPosition));
                } else if (fromPosition > toPosition) {

                    actualPositionList.set(toPosition, dummyActualPositionList.get(fromPosition));

                    for (int i = toPosition + 1; i <= fromPosition; i++) {
                        actualPositionList.set(i, dummyActualPositionList.get(i - 1));
                    }
                } else if (fromPosition < toPosition) {
                    actualPositionList.set(toPosition, dummyActualPositionList.get(fromPosition));

                    for (int i = fromPosition; i < toPosition; i++) {
                        actualPositionList.set(i, dummyActualPositionList.get(i + 1));
                    }
                }

            }

//                     @Override
//                     public void onActionStateChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
//                         Log.d(TAG, "onActionStateChanged: "+actionState);
////                         Toast.makeText(activity, "onActionStateChanged "+actionState, Toast.LENGTH_SHORT).show();
//                     }
//
//                     @Override
//                     public void onItemSwipe(int position, int direction) {
//                         Log.d(TAG, "onItemSwipe position:"+position+" direction:"+direction);
////                         Toast.makeText(activity, "onItemSwipe position:"+position+" direction:"+direction, Toast.LENGTH_SHORT).show();
//                     }
        });

        adapter.getCurrentItems();

// Initialize the RecyclerView and attach the Adapter to it as usual


        //... and much more

        rec_img.setAdapter(adapter);

        adapter //Add FastScroller
                .setLongPressDragEnabled(true) //Enable long press to drag items
                .setHandleDragEnabled(true) //Enable handle drag (handle view must be set in the VH)
                .setSwipeEnabled(true) //Enable swipe items
                .setDisplayHeadersAtStartUp(true) //Show Headers at startUp!
                .setStickyHeaders(true); //Make headers sticky (headers need to be shown)!0
    }

    private void add_extra_resources() {

        File zipFile12 = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/" + pack_category + "/" + file_name500 + "/" + file_name500 + ".zip");
//        File zipFile12=new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/"+pack_category+"/" + file_name500 + ".zip");

        Long tsLong0 = System.currentTimeMillis() / 1000;
        String ts0 = tsLong0.toString();

        RequestQueue queue = Volley.newRequestQueue(BackupAndRestoreActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, "https://upgradeinfotech.in/projects/mcpe_backup_pro/api/add_extra_resource.php?v=" + ts0, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("error101", "response " + response);
                if (response.equals("New record created successfully")) {
//                    upload_all();

                    upload_all_file_to_server();
                    Toast.makeText(activity, "Mod Uploaded sucessfully", Toast.LENGTH_SHORT).show();


                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            transfer_loading_dialog.dismiss();
                            Toast.makeText(activity, "failed to upload", Toast.LENGTH_SHORT).show();
                        }
                    });
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
                Long tsLong = System.currentTimeMillis() / 1000;
                title_for_path = tsLong.toString();


                String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                Map<String, String> params = new HashMap<>();
                params.put("mod_id", mod_id);
                params.put("path_extension", file_name500);
                params.put("file_size", getFileSizeMegaBytes(zipFile12));
                params.put("pack_type", pack_category);
//                params.put("v", ts);
                return params;
            }
        };
        queue.add(request);


    }


    private void copyFile_latest(Uri treeUri) {
        try {
            DocumentFile craftfilepath1000 = DocumentFile.fromTreeUri(activity, treeUri);
            DocumentFile craftfilepath = craftfilepath1000.findFile(pack_category);
            String file_name12 = DocumentFile.fromSingleUri(activity, treeUri).getName();
            DocumentFile file0 = craftfilepath.findFile(file_name12);

            if (file0.exists()) {
                OutputStream out;

                File file010 = new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + pack_category);
                if (!file010.exists()) {
                    file010.mkdir();
                }

                String mainfilepath777 = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + pack_category + "/" + file_name12;
                File file777 = new File(mainfilepath777);

                if (!file777.exists()) {
                    file777.mkdir();
                }

                DocumentFile pickedDir = DocumentFile.fromFile(file777);
                DocumentFile newFile111 = pickedDir;
                DocumentFile[] files = file0.listFiles();

                for (int i = 0; i < files.length; i++) {
                    // System.out.println(files[i]);

                    Log.d("files", "" + files[i]);
                    DocumentFile subfile = DocumentFile.fromSingleUri(activity, files[i].getUri());

                    if (subfile.isDirectory()) {
                        assert pickedDir != null;
                        DocumentFile existing = newFile111.findFile(files[i].getName());
                        if (existing != null) {
                            fillTheDirectory333(files[i], existing, craftfilepath.getUri(), files[i].getName());
                        } else {
                            DocumentFile newFile00 = newFile111.createDirectory(files[i].getName());
                            fillTheDirectory333(files[i], newFile00, subfile.getUri(), files[i].getName());
                        }
                    } else if (subfile.isFile()) {
                        try {
                            InputStream inn1 = activity.getContentResolver().openInputStream(subfile.getUri());

                            assert pickedDir != null;
                            DocumentFile existing = newFile111.findFile(files[i].getName());
                            if (existing != null)
                                existing.delete();
                            String string = files[i].getName();
                            String[] parts = string.split("\\.");
                            DocumentFile newFile = newFile111.createFile("*/" + parts[1], files[i].getName());
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

                            Log.d(TAG, "copyFile_latest: transfer complete " + subfile.getName());

                            runOnUiThread(() -> tv_progress0.setText(subfile.getName()));


                        } catch (Exception fnfe1) {
                            fnfe1.printStackTrace();
                        }
                    }

                }

                Log.d(TAG, "copyFile_latest: All file transfer completed ");

                runOnUiThread(() -> {
                    tv_progress0.setText("Completed");
                    transfer_loading_dialog.dismiss();
                    Toast.makeText(activity, "backup taken successfully", Toast.LENGTH_SHORT).show();
                });
            }

        } catch (Exception e) {
            runOnUiThread(() -> {
                transfer_loading_dialog.dismiss();
                Toast.makeText(activity, "failed to take backup", Toast.LENGTH_SHORT).show();
            });
            e.printStackTrace();
        }

    }


    private void fillTheDirectory333(DocumentFile file_copy_from, DocumentFile file_copy_to, Uri uri_copy_from, String fileName) {

        DocumentFile craftfilepath = DocumentFile.fromTreeUri(activity, uri_copy_from);

        DocumentFile file0 = file_copy_from;


        if (file0 == null) {
            return;
        }
        DocumentFile[] files = file0.listFiles();
        for (int i = 0; i < files.length; i++) {

            Log.d("dirfiles", "" + files[i].getName());
//            String path = mainfilepath + "/" + fileName;
//            File subfile = new File(path + "/" + files[i]);

            if (files[i].isDirectory()) {
                DocumentFile existing = file_copy_to.findFile(files[i].getName());
                if (existing != null) {
                    fillTheDirectory333(files[i], existing, files[i].getUri(), files[i].getName());
                } else {
                    DocumentFile newFile00 = file_copy_to.createDirectory(files[i].getName());
                    fillTheDirectory333(files[i], newFile00, files[i].getUri(), files[i].getName());
                }

            } else if (files[i].isFile()) {

                try {
//                    InputStream inn1 = new FileInputStream(subfile);
                    InputStream inn1 = activity.getContentResolver().openInputStream(files[i].getUri());

                    // assert pickedDir != null;
                    DocumentFile existing = file_copy_to.findFile(files[i].getName());
                    if (existing != null)
                        existing.delete();
                    String string = files[i].getName();
                    String[] parts = string.split("\\.");
                    String part1 = parts[0]; // 004
                    String part2 = parts[1]; // 034556
                    DocumentFile newFile = file_copy_to.createFile("*/" + parts[1], files[i].getName());
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


                    Log.d(TAG, "fillTheDirectory333: transfer complete " + files[i].getName());

                    String text_progress = files[i].getName();

                    runOnUiThread(() -> tv_progress0.setText(text_progress));

                } catch (Exception fnfe1) {
                    fnfe1.printStackTrace();
                }

            }
        }

    }


    private void copyFile(String inputPath, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {
            File file36 = new File(inputPath);

            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath + "/" + file36.getName());

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }


    private void copyFile_to_minecraft(Uri treeUri, int from_where) {
        // AssetManager assetManager = getAssets();
        try {
            String mainfilepath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + pack_category + "/" + file_name500;
            File file0 = new File(mainfilepath);

            if (file0.exists()) {
                OutputStream out;
                DocumentFile pickedDir1000 = DocumentFile.fromTreeUri(this, treeUri_minecraft);
                DocumentFile pickedDir = pickedDir1000.findFile(pack_category);

                DocumentFile newFile111;
                if (from_where == 0) {
                    newFile111 = pickedDir.findFile(file_name500);

                    if (newFile111 == null) {
                        newFile111 = pickedDir.createDirectory(file_name500);
                    }
                } else {
                    String file09 = "copy-" + file_name500;
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
                    transfer_loading_dialog.dismiss();
                    if (from_where == 0) {
                        Toast.makeText(activity, "restore successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "clone successful", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        } catch (Exception e) {

            runOnUiThread(() -> {
                transfer_loading_dialog.dismiss();
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

    public void Info_Dialog() {
        dialog1010 = new Dialog(activity);
        dialog1010.setCancelable(true);
        dialog1010.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1010.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog1010.setContentView(R.layout.coin_dialog);
        TextView tv_clone = dialog1010.findViewById(R.id.tv_clone);
        TextView tv_override = dialog1010.findViewById(R.id.tv_override);
        TextView tv_cancel = dialog1010.findViewById(R.id.tv_cancel);


        tv_clone.setOnClickListener(v -> {
            if (!activity.isFinishing() && dialog1010.isShowing())
                dialog1010.dismiss();

            if (Build.VERSION.SDK_INT >= 30) {

                transfer_loading_Dialog();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        copyFile_to_minecraft(treeUri, 1);
                    }
                }, 3000);
            } else {

                transfer_loading_Dialog();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        File mod_file = new File(mod_file_path);
                        String mod_file_name = mod_file.getName();
//                String file_path09 =Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.mojang.minecraftpe/files/games/com.mojang/minecraftWorlds/"+mod_file_name;
                        String file_path09 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.mojang.minecraftpe/files/games/com.mojang/minecraftWorlds";

                        File oldFolder = new File(file_path09, mod_file_name);
                        File newFolder = new File(file_path09, "old-" + mod_file_name);
                        boolean success = oldFolder.renameTo(newFolder);

                        if (success) {
                            try {
                                new ZipFile(new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/" + mod_file_name + "/" + mod_file_name + ".zip")).extractAll(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.mojang.minecraftpe/files/games/com.mojang/minecraftWorlds");
                                String file_path102 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.mojang.minecraftpe/files/games/com.mojang/minecraftWorlds/" + mod_file_name;

                                if (new File(file_path102).exists()) {
                                    File oldFolder2 = new File(file_path09, mod_file_name);
                                    File newFolder2 = new File(file_path09, "copy-" + mod_file_name);

                                    boolean success2 = oldFolder2.renameTo(newFolder2);

                                    if (success2) {
                                        File oldFolder3 = new File(file_path09, "old-" + mod_file_name);
                                        File newFolder3 = new File(file_path09, mod_file_name);
                                        boolean success3 = oldFolder3.renameTo(newFolder3);

                                        if (success3) {
                                            runOnUiThread(() -> {
                                                transfer_loading_dialog.dismiss();
                                                Toast.makeText(activity, "Successfully Cloned", Toast.LENGTH_SHORT).show();
                                            });
                                        } else {
                                            runOnUiThread(() -> {
                                                transfer_loading_dialog.dismiss();
                                                Toast.makeText(activity, "Clone Failed", Toast.LENGTH_SHORT).show();
                                            });
                                        }
                                    }
                                }

                            } catch (ZipException e) {
                                transfer_loading_dialog.dismiss();
                                Toast.makeText(activity, "clone failed", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        } else {
                            transfer_loading_dialog.dismiss();
                            Toast.makeText(activity, "clone failed", Toast.LENGTH_SHORT).show();
                        }

                        try {
//                        new ZipFile(new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/" + mod_file_name + "/" + mod_file_name + ".zip")).extractAll(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.mojang.minecraftpe/files/games/com.mojang/minecraftWorlds");
                            new ZipFile(new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/" + mod_file_name + "/" + mod_file_name + ".zip")).extractAll(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/" + mod_file_name);

                            if (new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/" + mod_file_name + "/" + mod_file_name).exists()) {

                            }
                        } catch (ZipException e) {
                            e.printStackTrace();
                        }
                    }

                }, 3000);


            }
        });

        tv_override.setOnClickListener(v -> {
            if (!activity.isFinishing() && dialog1010.isShowing())
                dialog1010.dismiss();

            if (Build.VERSION.SDK_INT >= 30) {
                DocumentFile pickedDir = DocumentFile.fromTreeUri(this, treeUri_minecraft);

                delete_loading_Dialog();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        deleteDirectory(pickedDir.findFile(file_name500));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "Info_Dialog: delete complete");
                                delete_loading_Dialog.dismiss();
                                transfer_loading_Dialog();
                            }
                        });


                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                copyFile_to_minecraft(treeUri, 0);
                            }

                        }, 3000);
                    }

                }, 3000);
            } else {
                delete_loading_Dialog();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {

                        boolean isDireDeleted = deleteDirectory_below11(new File(mod_file_path));

                        if (isDireDeleted) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "Info_Dialog: delete complete");
                                    delete_loading_Dialog.dismiss();
                                    transfer_loading_Dialog();
                                }
                            });
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    try {
                                        File mod_file = new File(mod_file_path);
                                        String mod_file_name = mod_file.getName();
                                        new ZipFile(new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/" + mod_file_name + "/" + mod_file_name + ".zip")).extractAll(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.mojang.minecraftpe/files/games/com.mojang/minecraftWorlds");
                                        runOnUiThread(() -> transfer_loading_dialog.dismiss());
                                    } catch (ZipException e) {
                                        Toast.makeText(activity, "mod not override successfully", Toast.LENGTH_SHORT).show();
                                        runOnUiThread(() -> transfer_loading_dialog.dismiss());
                                        e.printStackTrace();
                                    }
                                }

                            }, 3000);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    delete_loading_Dialog.dismiss();
                                }
                            });
                            Toast.makeText(activity, "mod not override successfully", Toast.LENGTH_SHORT).show();
                        }

                    }

                }, 3000);
            }

        });

        tv_cancel.setOnClickListener(v -> {
            if (!activity.isFinishing() && dialog1010.isShowing())
                dialog1010.dismiss();
        });

        dialog1010.show();
    }


    public void transfer_loading_Dialog() {
        transfer_loading_dialog = new Dialog(activity);
        transfer_loading_dialog.setCancelable(false);
        transfer_loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        transfer_loading_dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        transfer_loading_dialog.setContentView(R.layout.load_dialog);
        tv_progress0 = transfer_loading_dialog.findViewById(R.id.tv_progress0);
        tv_trans_or_delete = transfer_loading_dialog.findViewById(R.id.tv_trans_or_delete);
        transfer_loading_dialog.show();
    }


    public void delete_loading_Dialog() {
        delete_loading_Dialog = new Dialog(activity);
        delete_loading_Dialog.setCancelable(false);
        delete_loading_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        delete_loading_Dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        delete_loading_Dialog.setContentView(R.layout.load_dialog);
        tv_progress_delete1212 = delete_loading_Dialog.findViewById(R.id.tv_progress0);
        TextView tv_trans_or_delete = delete_loading_Dialog.findViewById(R.id.tv_trans_or_delete);
        tv_trans_or_delete.setText("Deleting");
        delete_loading_Dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public static boolean deleteDirectory(DocumentFile path) {

        if (path.exists()) {
            DocumentFile[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i] != null)
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        try {
                            tv_progress_delete1212.setText(files[i].getName());
                            Log.d(TAG, "deleteDirectory: " + files[i].getName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        files[i].delete();
                    }
            }
        }
        return (path.delete());
    }


    public static boolean deleteDirectory_below11(File path) {

        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i] != null)
                    if (files[i].isDirectory()) {
                        deleteDirectory_below11(files[i]);
                    } else {
                        try {
//                            tv_progress_delete1212.setText(files[i].getName());
                            Log.d(TAG, "deleteDirectory: " + files[i].getName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        files[i].delete();
                    }
            }
        }
        return (path.delete());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        File file020 = new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + file_name500 + ".zip");
        if (file020.exists()) {
            file020.delete();
        }

        Intent intent = new Intent(activity, profileActivity.class);
        startActivity(intent);
        finish();
    }

    public void add_mod_name_to_server() {
        List<String> list232 = mTagContainerLayout.getTags();

        all_tag = "";
        for (int i = 0; i < list232.size(); i++) {
            if (all_tag.equals("")) {
                all_tag = list232.get(i);
            } else {
                all_tag = all_tag + "," + list232.get(i);
            }

        }

        File zipFile12;
        if (Build.VERSION.SDK_INT >= 30) {
            zipFile12 = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + pack_category + "/" + file_name500 + ".zip");
        } else {
            zipFile12 = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/" + pack_category + "/" + file_name500 + "/" + file_name500 + ".zip");
        }
//        File zipFile12 = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/" + pack_category + "/" + file_name500 + "/" + file_name500 + ".zip");
//        File zipFile12 = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + pack_category + "/" + file_name500 + ".zip");
//        File zipFile12=new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/"+pack_category+"/" + file_name500 + ".zip");

        Long tsLong0 = System.currentTimeMillis() / 1000;
        String ts0 = tsLong0.toString();

        RequestQueue queue = Volley.newRequestQueue(BackupAndRestoreActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, "https://upgradeinfotech.in/projects/mcpe_backup_pro/api/new_mod_add.php?v=" + ts0, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("error101", "response " + response);
                if (response.equals("New record created successfully")) {
//                    upload_all();
                    upload_all_file_to_server();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            transfer_loading_dialog.dismiss();
                            Toast.makeText(activity, "failed to upload", Toast.LENGTH_SHORT).show();
                        }
                    });
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
                Long tsLong = System.currentTimeMillis() / 1000;
                title_for_path = tsLong.toString();


                String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                Map<String, String> params = new HashMap<>();
                params.put("title", mod_name);
                params.put("path_extension", file_name500);
                params.put("title_for_path", title_for_path);
                params.put("file_size", getFileSizeMegaBytes(zipFile12));
                params.put("description", et_description.getText().toString());
                params.put("user_id", m_androidId);
                params.put("pack_type", pack_category);
                params.put("all_tag", all_tag);
//                params.put("v", ts);
                return params;
            }
        };
        queue.add(request);

    }

    private static String getFileSizeMegaBytes(File file) {
        return df.format((double) file.length() / (1024 * 1024)) + " mb";
    }

    @NonNull
    private MultipartBody.Part prepairFiles(String partName, Uri fileUri) {
        File file = new File(fileUri.getPath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);

        return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
    }


    public List<IFlexible> getDatabaseList() {
        List<IFlexible> list = new ArrayList<>();

        for (int i = 0; i < mArrayUri.size(); i++) {
            list.add(new MyItem(String.valueOf(i),list, mArrayUri.get(i), BackupAndRestoreActivity.this));
        }

//        list.add(new MyItem("2", "World"));
        return list;
    }


    void upload_all_file_to_server() {
        List<MultipartBody.Part> list = new ArrayList<>();

        if ((Build.VERSION.SDK_INT >= 30)) {
            File file020 = new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + pack_category + "/" + file_name500);
//            uploadFileToServer(new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + pack_category + "/" + file_name500 + ".zip"));
            MultipartBody.Part imageRequest = prepairFiles("file[]", Uri.parse(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + pack_category + "/" + file_name500 + ".zip"));
            list.add(imageRequest);

            File[] listfile = file020.listFiles();
            for (int j = 0; j < listfile.length; j++) {
                if (listfile[j].getName().contains("world_icon.jpeg") || listfile[j].getName().contains("pack_icon.png")) {

                    MultipartBody.Part imageRequest0;
                    if (listfile[j].getName().contains("pack_icon.png")) {
                        imageRequest0 = prepairFiles("file[]", Uri.parse(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + pack_category + "/" + file_name500 + "/pack_icon.png"));
                    } else {
                        imageRequest0 = prepairFiles("file[]", Uri.parse(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + pack_category + "/" + file_name500 + "/world_icon.jpeg"));
                    }

                    list.add(imageRequest0);
                    break;
                }
            }


        } else {
            File file020 = new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/" + pack_category + "/" + file_name500);

            MultipartBody.Part imageRequest = prepairFiles("file[]", Uri.parse(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/" + pack_category + "/" + file_name500 + "/" + file_name500 + ".zip"));
            list.add(imageRequest);

            File[] listfile = file020.listFiles();
            if(listfile != null && listfile.length > 0){
                for (int j = 0; j < listfile.length; j++) {
                    if (listfile[j].getName().contains("world_icon.jpeg") || listfile[j].getName().contains("pack_icon.png")) {

                        MultipartBody.Part imageRequest0;
                        if (listfile[j].getName().contains("world_icon.jpeg")) {
                            imageRequest0 = prepairFiles("file[]", Uri.parse(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/" + pack_category + "/" + file_name500 + "/world_icon.jpeg"));
                            list.add(imageRequest0);
                        } else if (listfile[j].getName().contains("pack_icon.png")) {
                            imageRequest0 = prepairFiles("file[]", Uri.parse(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/backup/" + pack_category + "/" + file_name500 + "/pack_icon.png"));
                            list.add(imageRequest0);
                        }

                        break;
                    }
                }
            }        }


        Actual_mArrayUri.clear();
        String serial_wise_img = "";
        for (int i = 0; i < actualPositionList.size(); i++) {
            File file = new File(RepeatMethods.


                    ImageFilePath.getPath(BackupAndRestoreActivity.this, mArrayUri.get(actualPositionList.get(i))));
            if (!serial_wise_img.equals("")) {
                serial_wise_img = serial_wise_img + "," + file.getName();
            } else {
                serial_wise_img = file.getName();
            }
//            Actual_mArrayUri.add(mArrayUri.get(actualPositionList.get(i)));
        }

        int i = 0;
        for (Uri uri : mArrayUri) {
//            String fileName = FileUtils.getFile(this, uri).getName();
            //very important files[]
            MultipartBody.Part imageRequest = prepairFiles("file[]", Uri.parse(RepeatMethods.ImageFilePath.getPath(BackupAndRestoreActivity.this, uri)));
            list.add(imageRequest);
        }

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();


//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("dfgdfgdf", "dfgdfgdf", RequestBody.create(MultipartBody.FORM,"dfgdfgdf"));

        Retrofit builder = new Retrofit.Builder().baseUrl("https://upgradeinfotech.in/projects/mcpe_backup_pro/api/").addConverterFactory(GsonConverterFactory.create()).build();
        FileUploadService fileUploadService = builder.create(FileUploadService.class);
        Call<FileModel> call = fileUploadService.uploadImages(list, RequestBody.create(MultipartBody.FORM, file_name500), RequestBody.create(MultipartBody.FORM, serial_wise_img));
        call.enqueue(new Callback<FileModel>() {
            @Override
            public void onResponse(Call<FileModel> call, Response<FileModel> response) {
                Log.e("main", "the message is ----> " + response.body());
                runOnUiThread(() -> transfer_loading_dialog.dismiss());
                Toast.makeText(BackupAndRestoreActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<FileModel> call, Throwable throwable) {
                Toast.makeText(BackupAndRestoreActivity.this, "Fail to Upload ", Toast.LENGTH_SHORT).show();

                transfer_loading_dialog.dismiss();
                Log.e("main", "on error is called and the error is  ----> " + throwable.getMessage());
            }
        });


    }

}