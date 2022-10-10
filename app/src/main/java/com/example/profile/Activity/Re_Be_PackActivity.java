package com.example.profile.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.ImageView;

import com.example.profile.Adapter.BackupAdapter;
import com.example.profile.Model.BackupModel;
import com.example.profile.Other.RepeatMethods;
import com.example.profile.R;
import com.example.profile.fragment.You;

import java.util.ArrayList;

import static com.example.profile.Other.Const.ANDROID_DOCID;
import static com.example.profile.Other.Const.EXTERNAL_STORAGE_PROVIDER_AUTHORITY;

public class Re_Be_PackActivity extends AppCompatActivity {

    Uri treeUri22, uri22;
    private static final String TAG = "RpBpActivity";
    ArrayList<BackupModel> backupModelArrayList = new ArrayList<>();
    BackupAdapter backupAdapter;
    RecyclerView rec_rpbp;
    String pack_category;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_be_pack);

//        getSupportActionBar().hide();
        rec_rpbp = findViewById(R.id.rec_rpbp);
//        back = findViewById(R.id.back);

        pack_category = getIntent().getStringExtra("pack_category");
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



//        back.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent(Re_Be_PackActivity.this, You.class);
//                                        startActivity(intent);
//                                    }
//                                }
//        );


        show_all_modelist();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void show_all_modelist() {

        RepeatMethods repeatMethods = new RepeatMethods(Re_Be_PackActivity.this);
        backupModelArrayList = repeatMethods.show_all_modelist_new(treeUri22, pack_category);


        GridLayoutManager layoutManager = new GridLayoutManager(Re_Be_PackActivity.this, 1);
        rec_rpbp.setLayoutManager(layoutManager);
        rec_rpbp.setHasFixedSize(true);
        backupAdapter = new BackupAdapter(Re_Be_PackActivity.this, backupModelArrayList, pack_category, 0, null);
        rec_rpbp.setAdapter(backupAdapter);
    }

}