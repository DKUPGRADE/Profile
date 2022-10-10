 package com.example.profile.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profile.Adapter.ServerModAdapter;
import com.example.profile.Model.ModModel;
import com.example.profile.Other.Api;
import com.example.profile.Other.RetroFitClient;
import com.example.profile.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


 public class User_home extends Fragment {

     RecyclerView rec_server_available_list;
     ArrayList<ModModel> ModModelArrayList = new ArrayList<>();
     Activity activity;

     ServerModAdapter serverModAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        activity=getActivity();

        rec_server_available_list=view.findViewById(R.id.rec_server_available_list);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        rec_server_available_list.setLayoutManager(layoutManager);
        rec_server_available_list.setHasFixedSize(true);
        serverModAdapter = new ServerModAdapter(getActivity(), ModModelArrayList);
        rec_server_available_list.setAdapter(serverModAdapter);
        LoadNew();



        return  view;
    }
     private void LoadNew() {
         Long tsLong = System.currentTimeMillis() / 1000;
         String ts = tsLong.toString();


         Call<List<ModModel>> itemView = (RetroFitClient.getRetrofitInstance().create(Api.class)).getAllMods(ts);
         itemView.enqueue(new Callback<List<ModModel>>() {
             public void onFailure(@NonNull Call<List<ModModel>> call, @NonNull Throwable th) {
                 System.out.println(th.getMessage());

             }

             @RequiresApi(api = Build.VERSION_CODES.M)
             public void onResponse(@NonNull Call<List<ModModel>> call, @NonNull Response<List<ModModel>> response) {
                 ModModelArrayList = (ArrayList<ModModel>) response.body();
//                for (int i = 0; i < ModModelArrayList.size(); i++) {
//                    ModModel modModel = new ModModel(ModModelArrayList.get(i).getId(),ModModelArrayList.get(i).getTitle(),ModModelArrayList.get(i).getPathExtension());
//                    ModModelArrayList.add(modModel);
//                }

                 serverModAdapter.add_list(ModModelArrayList);

             }
         });
     }
}