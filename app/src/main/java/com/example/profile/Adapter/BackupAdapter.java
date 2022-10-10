package com.example.profile.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.profile.Activity.BackupAndRestoreActivity;
import com.example.profile.Model.BackupModel;
import com.example.profile.Model.FileModel;
import com.example.profile.Other.FileUploadService;
import com.example.profile.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BackupAdapter extends RecyclerView.Adapter<BackupAdapter.ViewHoder> {

    List<BackupModel> backup_list = new ArrayList<>();

    Activity activity;
    private static final String TAG = "BackupAdapter101";
    String pack_category;
    int fromWhere = 0;
    String mod_id;
    List<Uri> multipleImgList = new ArrayList<>();

    public BackupAdapter(Activity activity, List<BackupModel> list, String pack_category, int fromWhere, String mod_id) {
        backup_list = list;
        this.activity = activity;
        this.pack_category = pack_category;
        this.fromWhere = fromWhere;
        this.mod_id = mod_id;


        multipleImgList.add(Uri.parse("/storage/emulated/0/Android/data/com.mojang.minecraftpe/files/games/com.mojang/behavior_packs/SCPDystopi/pack_icon.png"));
        multipleImgList.add(Uri.parse("/storage/emulated/0/Android/data/com.mojang.minecraftpe/files/games/com.mojang/minecraftWorlds/maps_0_322/world_icon.jpeg"));

//        for (int i = 0; i < list.size(); i++) {
//            multipleImgList.add(Uri.parse(list.get(i).getImg_path()));
//        }
    }


    public BackupAdapter.ViewHoder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new BackupAdapter.ViewHoder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.re_be_mod, viewGroup, false));
    }

    @SuppressLint("NewApi")
    public void onBindViewHolder(final BackupAdapter.ViewHoder viewHoder, @SuppressLint("RecyclerView") final int i) {

        String mod_name = null;
        if (backup_list.get(i).getModeName() != null) {
            mod_name = getActualName(backup_list.get(i).getModeName());
        }

        viewHoder.tv_name.setText(mod_name);
//        viewHoder.tv_file_name.setText(backup_list.get(i).getFile_name());

        if (fromWhere == 1) {
            viewHoder.btn_backup.setText("Upload");
        }

        if (backup_list.get(i).getIsBackupExists() == 0) {
//            viewHoder.layout_main12.setBackgroundColor(Color.parseColor("#B3ACA9"));
//            viewHoder.tv_exists_in_minecraft.setText("exists in minecraft");
//            viewHoder.tv_exists_in_minecraft.setTextColor(Color.GREEN);
            viewHoder.tv_exists_in_backup.setText("Backup Not Available");
            viewHoder.tv_exists_in_backup.setTextColor(Color.RED);
        } else if (backup_list.get(i).getIsBackupExists() == 1) {
//            viewHoder.layout_main12.setBackgroundColor(Color.parseColor("#B3ACA9"));
//            viewHoder.tv_exists_in_minecraft.setText("exists in minecraft");
//            viewHoder.tv_exists_in_minecraft.setTextColor(Color.GREEN);
            viewHoder.tv_exists_in_backup.setText("Backup Available");
            viewHoder.tv_exists_in_backup.setTextColor(Color.GREEN);
        } else {
//            viewHoder.layout_main12.setBackgroundColor(Color.parseColor("#EDD8B8"));
//            viewHoder.tv_exists_in_minecraft.setText("not exists in minecraft");
//            viewHoder.tv_exists_in_minecraft.setTextColor(Color.RED);
            viewHoder.tv_exists_in_backup.setText("Backup Available");
            viewHoder.tv_exists_in_backup.setTextColor(Color.GREEN);
        }


        Glide.with(activity).load(backup_list.get(i).getImg_path()).skipMemoryCache(true).into(viewHoder.img_mod);
        Log.d("TAG21", "onBindViewHolder: "+backup_list.get(i).getImg_path());

        viewHoder.btn_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                uploadImages();


                gotobackuprestoreactivity(i);


            }
        });

        viewHoder.img_mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                uploadImages();


                gotobackuprestoreactivity(i);


            }


        });

    }

    private void gotobackuprestoreactivity(int i) {
        Intent intent = new Intent(activity, BackupAndRestoreActivity.class);

        if (!(Build.VERSION.SDK_INT >= 30)) {
            intent.putExtra("file_path", backup_list.get(i).getFilepath());
        } else {
            intent.putExtra("string_treeUri", backup_list.get(i).getFilepath());
        }
        intent.putExtra("mod_name_file_path", backup_list.get(i).getName_file_path());
        intent.putExtra("mod_image_file_path", backup_list.get(i).getImg_path());
        intent.putExtra("file_name", backup_list.get(i).getFile_name());
        intent.putExtra("mod_name", getActualName(backup_list.get(i).getModeName()));
        intent.putExtra("isBackupExist", getActualName(String.valueOf(backup_list.get(i).getIsBackupExists())));
        intent.putExtra("pack_category", pack_category);
        intent.putExtra("fromWhere", String.valueOf(fromWhere));
        intent.putExtra("mod_id", mod_id);


        activity.startActivity(intent);

        activity.finish();
    }

    private String getActualName(String modeName) {
        try {
            String final_str = modeName;
            String str = modeName;
            char ch = '§';

            int index = str.indexOf(ch);
            while (index != -1) {

                StringBuilder string = new StringBuilder(final_str);
                string.setCharAt(index + 1, ch);
                final_str = string.toString();
                index = str.indexOf(ch, index + 1);
            }
            return final_str.replaceAll(String.valueOf(ch), "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }


    }

    public int getItemCount() {
        return backup_list.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_exists_in_minecraft, tv_exists_in_backup, tv_file_name;
        ImageView img_mod;
        Button btn_backup;
        LinearLayout layout_main12;

        public ViewHoder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
//            tv_exists_in_minecraft = view.findViewById(R.id.tv_exists_in_minecraft);
            tv_exists_in_backup = view.findViewById(R.id.tv_exists_in_backup);
//            tv_file_name = view.findViewById(R.id.tv_file_name);
            img_mod = view.findViewById(R.id.img_mod);
            btn_backup = view.findViewById(R.id.btn_backup);
//            layout_main12 = view.findViewById(R.id.layout_main12);

        }
    }


    @NonNull
    private MultipartBody.Part prepairFiles(String partName, Uri fileUri) {
        File file = new File(fileUri.getPath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);

        return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
    }


    void uploadImages() {
        List<MultipartBody.Part> list = new ArrayList<>();
        int i = 0;
        for (Uri uri : multipleImgList) {
//            String fileName = FileUtils.getFile(this, uri).getName();
            //very important files[]
            MultipartBody.Part imageRequest = prepairFiles("file[]", uri);
            list.add(imageRequest);
        }


        Retrofit builder = new Retrofit.Builder().baseUrl("https://upgradeinfotech.in/projects/mcpe_backup_pro/api/").addConverterFactory(GsonConverterFactory.create()).build();
        FileUploadService fileUploadService = builder.create(FileUploadService.class);
        Call<FileModel> call = fileUploadService.uploadImages(list, null, null);
        call.enqueue(new Callback<FileModel>() {
            @Override
            public void onResponse(Call<FileModel> call, Response<FileModel> response) {
                Log.e("main", "the message is ----> " + response.body());
            }

            @Override
            public void onFailure(Call<FileModel> call, Throwable throwable) {
                Log.e("main", "on error is called and the error is  ----> " + throwable.getMessage());

            }
        });


    }


}
