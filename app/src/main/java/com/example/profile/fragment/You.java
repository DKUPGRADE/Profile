package com.example.profile.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.UriPermission;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profile.Activity.BackupAndRestoreActivity;
import com.example.profile.Activity.Re_Be_PackActivity;
import com.example.profile.Adapter.BackupAdapter;
import com.example.profile.Model.BackupModel;
import com.example.profile.Other.RepeatMethods;
import com.example.profile.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.STORAGE_SERVICE;
import static android.provider.DocumentsContract.EXTRA_INITIAL_URI;
import static com.example.profile.Other.Const.ANDROID_DOCID;
import static com.example.profile.Other.Const.EXTERNAL_STORAGE_PROVIDER_AUTHORITY;


public class You extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    Uri treeUri22;
    Uri uri22;
    RecyclerView rec_available_list;
    BackupAdapter backupAdapter;
    ArrayList<BackupModel> backupModelArrayList = new ArrayList<>();
    private static final String TAG = "MainActivity101";
    public static int ac = 0;


    TextView Resource_pack, Behaviour_pack,Minecraftworld_pack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_you, container, false);


        rec_available_list = view.findViewById(R.id.rec_available_list);

        Behaviour_pack = view.findViewById(R.id.tv_b_pack);
        Resource_pack = view.findViewById(R.id.tv_r_pack);
        Minecraftworld_pack = view.findViewById(R.id.tv_m_pack);


        take_permissions();

        Behaviour_pack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), Re_Be_PackActivity.class);
//                intent.putExtra("pack_category", "behavior_packs");
//                startActivity(intent);

                show_all_modelist("behavior_packs");
            }
        });

        Resource_pack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), Re_Be_PackActivity.class);
//                intent.putExtra("pack_category", "resource_packs");
//                startActivity(intent);
                show_all_modelist("resource_packs");
            }
        });

        Minecraftworld_pack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), Re_Be_PackActivity.class);
//                intent.putExtra("pack_category", "resource_packs");
//                startActivity(intent);
                show_all_modelist("minecraftWorlds");
            }
        });


        uri22 = DocumentsContract.buildDocumentUri(
                EXTERNAL_STORAGE_PROVIDER_AUTHORITY,
                ANDROID_DOCID
        );


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            treeUri22 = DocumentsContract.buildTreeDocumentUri(
                    EXTERNAL_STORAGE_PROVIDER_AUTHORITY,
                    ANDROID_DOCID
            );

        }

        return view;
    }

    private void take_permissions() {

        if (!(Build.VERSION.SDK_INT >= 30)) {

            if (ContextCompat.checkSelfPermission(
                    requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    + ContextCompat.checkSelfPermission(
                    getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermission();
                }

            } else {
                // do next work
                show_all_modelist("minecraftWorlds");
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    + ContextCompat.checkSelfPermission(
                    getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                checkPermission();
            } else {
                if (checkIfGotAccess()) {
                    // do next work
                    show_all_modelist("minecraftWorlds");

                } else {
                    openDirectory11();
                }
            }


        }
    }
//    private void show_all_modelist() {
//
//        RepeatMethods repeatMethods = new RepeatMethods(Re_Be_PackActivity.this);
//        backupModelArrayList = repeatMethods.show_all_modelist_new(treeUri22, pack_category);
//
//
//        GridLayoutManager layoutManager = new GridLayoutManager(Re_Be_PackActivity.this, 1);
//        rec_rpbp.setLayoutManager(layoutManager);
//        rec_rpbp.setHasFixedSize(true);
//        backupAdapter = new BackupAdapter(Re_Be_PackActivity.this, backupModelArrayList, pack_category, 0, null);
//        rec_rpbp.setAdapter(backupAdapter);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_CODE) {// When request is cancelled, the results array are empty
            if ((grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                // Permissions are granted
                Toast.makeText(getActivity(), "Permissions granted.", Toast.LENGTH_SHORT).show();

                if (Build.VERSION.SDK_INT >= 30) {

                    if (checkIfGotAccess()) {
                        // do work
                        show_all_modelist("minecraftWorlds");
                        Log.d("p101", "in onRequestPermissionsResult");

                    } else {
                        openDirectory11();
                    }

                } else {
                    //  do work
                    show_all_modelist("minecraftWorlds");


                }
            } else {
                // Permissions are denied

                Toast.makeText(getActivity(), "Permissions denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (data == null || data.getData() == null) {
                return;
            }

            Uri directoryUri = data.getData();
            if (directoryUri.getPath().endsWith("com.mojang.minecraftpe/files/games/com.mojang")) {
                getActivity().getContentResolver().takePersistableUriPermission(
                        directoryUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
            } else {
                Toast.makeText(getActivity(), "please select correct path", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    openDirectory11();
                }
            }
            if (checkIfGotAccess()) {
                // do work
                show_all_modelist("minecraftWorlds");
            }

        } else {
            // ask again for permission
        }
    }
    private void show_all_modelist(String pack_category) {

        RepeatMethods repeatMethods = new RepeatMethods(getActivity());
        backupModelArrayList = repeatMethods.show_all_modelist_new(treeUri22,pack_category);


        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        rec_available_list.setLayoutManager(layoutManager);
        rec_available_list.setHasFixedSize(true);
        backupAdapter = new BackupAdapter(getActivity(), backupModelArrayList, pack_category,0,null);
        rec_available_list.setAdapter(backupAdapter);
    }


    protected void checkPermission() {
        if (ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Do something, when permissions not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Read and Write External" +
                        " Storage permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_CODE);

                    }
                });
                builder.setNeutralButton("Cancel", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                //   Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        }

    }


    public void openDirectory11() {

        Intent intent =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            intent = getPrimaryVolume().createOpenDocumentTreeIntent()
                    .putExtra(EXTRA_INITIAL_URI, uri22);
            startActivityForResult(intent, 100);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private StorageVolume getPrimaryVolume() {
        StorageManager sm = (StorageManager) getActivity().getSystemService(STORAGE_SERVICE);
        return sm.getPrimaryStorageVolume();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Boolean checkIfGotAccess() {
        List<UriPermission> permissionList = null;
        permissionList = getActivity().getContentResolver().getPersistedUriPermissions();
        for (int i = 0; i < permissionList.size(); i++) {
            UriPermission it = permissionList.get(i);
            if (it.getUri().equals(treeUri22) && it.isReadPermission())
                return true;
        }
        return false;
    }
}