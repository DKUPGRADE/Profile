package com.example.profile.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.profile.Activity.DownloadActivity;
import com.example.profile.Model.ModModel;
import com.example.profile.R;

import java.util.ArrayList;
import java.util.List;

public class ServerModAdapter extends  RecyclerView.Adapter<ServerModAdapter.ViewHoder>  {

    List<ModModel> server_mod_list = new ArrayList<>();

    Activity activity;
    private static final String TAG = "ServerModAdapter";

    public ServerModAdapter(Activity activity, List<ModModel> list) {
        server_mod_list = list;
        this.activity = activity;
    }

    public void add_list( List<ModModel> list) {
        server_mod_list = list;
        notifyDataSetChanged();
    }


    public ServerModAdapter.ViewHoder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ServerModAdapter.ViewHoder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_mod, viewGroup, false));
    }

    @SuppressLint("NewApi")
    public void onBindViewHolder(final ServerModAdapter.ViewHoder viewHoder, @SuppressLint("RecyclerView") final int i) {
//        viewHoder.layout_main12.setBackgroundColor(Color.parseColor("#D7E6F1"));

        String file_name=server_mod_list.get(i).getDisplayImage();

        String pack_url="https://upgradeinfotech.in/projects/mcpe_backup_pro/api/upload/"+file_name+"/pack_icon.png";

        String world_url="https://upgradeinfotech.in/projects/mcpe_backup_pro/api/upload/"+file_name+"/world_icon.jpeg";




        viewHoder.tv_name.setText(server_mod_list.get(i).getTitle());
//        viewHoder.tv_file_name.setText(file_name);
        viewHoder.tv_pack_type.setVisibility(View.VISIBLE);
        viewHoder.tv_pack_type.setText(server_mod_list.get(i).getPack_type());

        viewHoder.btn_backup.setText("Download");

        String m_androidId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        if(server_mod_list.get(i).getUserId().equals(m_androidId)){
            viewHoder.tv_owner.setVisibility(View.VISIBLE);
        }else{
            viewHoder.tv_owner.setVisibility(View.GONE);
        }

//        if (backup_list.get(i).getIsBackupExists() == 0) {
//            viewHoder.layout_main12.setBackgroundColor(Color.parseColor("#B3ACA9"));
//            viewHoder.tv_exists_in_minecraft.setText("exists in minecraft");
//            viewHoder.tv_exists_in_minecraft.setTextColor(Color.GREEN);
//            viewHoder.tv_exists_in_backup.setText("backup not available");
//            viewHoder.tv_exists_in_backup.setTextColor(Color.RED);
//        } else if (backup_list.get(i).getIsBackupExists() == 1) {
//            viewHoder.layout_main12.setBackgroundColor(Color.parseColor("#B3ACA9"));
//            viewHoder.tv_exists_in_minecraft.setText("exists in minecraft");
//            viewHoder.tv_exists_in_minecraft.setTextColor(Color.GREEN);
//            viewHoder.tv_exists_in_backup.setText("backup available");
//            viewHoder.tv_exists_in_backup.setTextColor(Color.GREEN);
//        } else {
//            viewHoder.layout_main12.setBackgroundColor(Color.parseColor("#EDD8B8"));
//            viewHoder.tv_exists_in_minecraft.setText("not exists in minecraft");
//            viewHoder.tv_exists_in_minecraft.setTextColor(Color.RED);
//            viewHoder.tv_exists_in_backup.setText("backup available");
//            viewHoder.tv_exists_in_backup.setTextColor(Color.GREEN);
//        }


        if(server_mod_list.get(i).getPack_type().equals("behavior_packs") || server_mod_list.get(i).getPack_type().equals("resource_packs")){
            Glide.with(activity).load(pack_url).skipMemoryCache(true).into(viewHoder.img_mod);

        }else{
            Glide.with(activity).load(world_url).skipMemoryCache(true).into(viewHoder.img_mod);
        }

        viewHoder.btn_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotodownloadactivity(i,pack_url,world_url);
//                copyFile_latest(Uri.parse(backup_list.get(i).getFilepath()));

            }
        });

        viewHoder.img_mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotodownloadactivity(i,pack_url,world_url);
//                copyFile_latest(Uri.parse(backup_list.get(i).getFilepath()));

            }


        });

    }

    public void gotodownloadactivity(int i,String pack_url,String world_url) {
        Intent intent = new Intent(activity, DownloadActivity.class);
        intent.putExtra("file_name", server_mod_list.get(i).getDisplayImage());
        intent.putExtra("mod_name", server_mod_list.get(i).getTitle());
        intent.putExtra("pack_type", server_mod_list.get(i).getPack_type());
        intent.putExtra("user_id", server_mod_list.get(i).getUserId());
        intent.putExtra("mod_id", server_mod_list.get(i).getId());
        intent.putExtra("pack_url",pack_url);
        intent.putExtra("world_url", world_url);
        activity.startActivity(intent);
    }

    public int getItemCount() {
        return server_mod_list.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_exists_in_minecraft, tv_exists_in_backup,tv_file_name,tv_owner,tv_pack_type;
        ImageView img_mod;
        Button btn_backup;
        LinearLayout layout_main12;

        public ViewHoder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
//            tv_exists_in_minecraft = view.findViewById(R.id.tv_exists_in_minecraft);
//            tv_exists_in_backup = view.findViewById(R.id.tv_exists_in_backup);
//            tv_file_name = view.findViewById(R.id.tv_file_name);
            tv_owner = view.findViewById(R.id.tv_owner);
            img_mod = view.findViewById(R.id.img_mod);
            btn_backup = view.findViewById(R.id.btn_backup);
//            layout_main12 = view.findViewById(R.id.layout_main12);
            tv_pack_type = view.findViewById(R.id.tv_pack_type);

        }
    }

}
