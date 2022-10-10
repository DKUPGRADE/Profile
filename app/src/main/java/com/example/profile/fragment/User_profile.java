package com.example.profile.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.profile.Other.FileUtils;
import com.example.profile.Model.FileModel;
import com.example.profile.Model.Profile;
import com.example.profile.Other.Api;
import com.example.profile.Other.RetroFitClient;
import com.example.profile.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class User_profile extends Fragment {

    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    String cameraPermission[];
    Uri imageuri;
    String storagePermission[];
    String name, image, uid;
    TextView profile_name,email;
    ImageView edit, profile;
    ProgressDialog pd;
    CircleImageView circleImageView;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;
    String profileOrCoverPhoto;
    private static final String TAG = "Home101";
    String m_androidId;
    private String selectedImage;
    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile, container, false);

//        ((AppCompatActivity) getActivity()). getSupportActionBar(). hide();

        circleImageView = view.findViewById(R.id.CircleImageView);
        profile_name = view.findViewById(R.id.name);
        edit = view.findViewById(R.id.edit);
        profile = view.findViewById(R.id.CircleImageView);
        email = view.findViewById(R.id.email);
        progressBar = view.findViewById(R.id.ProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        m_androidId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);


        Log.d(TAG, "onCreate: " + image);
//        Glide.with(Home.this).load(image).into(circleImageView);
//        profile_name.setText(name);

        getdata();
        pd = new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Updating Profile Picture");
                profileOrCoverPhoto = "image";
                showImagePicDialog();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Updating Name");
                showNamephoneupdate("name");
            }
        });


        return view;

    }


    private void showNamephoneupdate(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update" + key);

        // creating a layout to write the new name
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);
        final EditText editText = new EditText(getActivity());
        editText.setHint("Enter" + key);
        layout.addView(editText);
        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    pd.show();

                    addservername(value);

//                    if (key.equals("name")) {
//                        final DatabaseReference databaser = FirebaseDatabase.getInstance().getReference("Posts");
//                        Query query = databaser.orderByChild("uid").equalTo(uid);
//                        query.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                                    String child = databaser.getKey();
//                                    dataSnapshot1.getRef().child("uname").setValue(value);
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
                } else {
                    Toast.makeText(getActivity(), "Unable to update", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
            }
        });
        builder.create().show();
    }

    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // if access is not given then we will request for permission
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private Boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    // requesting for storage permission
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    // checking camera permission ,if given then we can click image using our camera
    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    // requesting for camera permission if not given
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    public void getdata() {

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        Call<List<Profile>> call = RetroFitClient.getRetrofitInstance().create(Api.class).getdata(m_androidId, ts);
        call.enqueue(new Callback<List<Profile>>() {
            public void onFailure(@NonNull Call<List<Profile>> call, @NonNull Throwable th) {
                Log.d("TAG54", "onFailure: " + th);
//                Toast.makeText(getContext(), "All set err", Toast.LENGTH_SHORT).show();
            }

            public void onResponse(@NonNull Call<List<Profile>> call, @NonNull Response<List<Profile>> response) {
              progressBar.setVisibility(View.GONE);
                profile_name.setText(response.body().get(0).getUser_name());
                email.setText(response.body().get(0).getEmail());
                Log.d("TAG01", "onResponse: " + response.body().get(0).getImage_url());
                if (getActivity() == null) {
                    return;
                }
                Glide.with(getActivity()).load(response.body().get(0).getImage_url()).into(circleImageView);

//                if (response.equals("Get successfully")) {
//                    Toast.makeText(Home.this, "All set", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(Home.this, "All not set", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == IMAGEPICK_GALLERY_REQUEST) {

                Uri image = data.getData();
                selectedImage = FileUtils.getPath(getActivity(), image);
                uploadFileToServer();

            }
            if (requestCode == IMAGE_PICKCAMERA_REQUEST) {
                Log.d(TAG, "onActivityResult: " + data.getData());
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                profile.setImageBitmap(bitmap);
                Uri image = getImageUri(bitmap);
                selectedImage = FileUtils.getPath(getActivity(), image);
                uploadFileToServer();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public Uri getImageUri(Bitmap inImage) {
        long tsLong = System.currentTimeMillis() / 1000;
        String ts = Long.toString(tsLong);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), inImage, ts, null);
        return Uri.parse(path);
    }

    //    public Uri setImageUri() {
//        // Store image in dcim
//        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
//        Uri imgUri = Uri.fromFile(file);
//        this.imgPath = file.getAbsolutePath();
//        return imgUri;
//    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(getActivity(), "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(getActivity(), "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    // Here we will click a photo and then go to startactivityforresult for updating data
    private void pickFromCamera() {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
//        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
//        imageuri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent camerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camerIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        startActivityForResult(camerIntent, IMAGE_PICKCAMERA_REQUEST);

//        selectedImage = FileUtils.getPath(Home.this, imageuri);
//        uploadFileToServer();

    }

    // We will select an image from gallery
    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGEPICK_GALLERY_REQUEST);
    }

    public void uploadFileToServer() {
        pd.show();
        File file = new File(Uri.parse(selectedImage).getPath());

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("sendimage", file.getName(), requestBody);


        Call<FileModel> call = RetroFitClient.getRetrofitInstance().create(Api.class).callUploadApi(filePart, RequestBody.create(MultipartBody.FORM, FirebaseAuth.getInstance().getCurrentUser().getEmail()));

//        Call<FileModel> call = service.callUploadApi(filePart);
        call.enqueue(new Callback<FileModel>() {
            @Override
            public void onResponse(@NotNull Call<FileModel> call, @NotNull Response<FileModel> response) {
                FileModel fileModel = response.body();
                addserverimage(file.getName());
                Log.d("TAG14", "onResponse: " + response);
                Toast.makeText(getActivity(), fileModel.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<FileModel> call, @NotNull Throwable t) {
                Log.d("TAG14", "onFailure: " + t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void addservername(String value) {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
//                            Toast.makeText(MainActivity.this, "Response was submit", Toast.LENGTH_SHORT).show();

        StringRequest request = new StringRequest(Request.Method.POST, "https://www.upgradeinfotech.in/projects/ModesToolbox/v1.1/user_profile_name_update.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("214", "onResponse: " + response);
                if (response.equals("Updated successfully")) {
                    profile_name.setText(value);
                    pd.cancel();
                    Toast.makeText(getActivity(), "Data Add", Toast.LENGTH_SHORT).show();

                } else {
                    pd.cancel();
                    Toast.makeText(getActivity(), "Data Not Add", Toast.LENGTH_SHORT).show();

                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), "Data Fail", Toast.LENGTH_SHORT).show();
                pd.cancel();
                Log.v("error101", "response " + error);
//                                    Toast.makeText(MainActivity.this, "Response was not submit", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                Log.d("TAG21", "getParams: " + m_androidId);
                Log.d("TAG21", "getParams: " + value);

                params.put("user_name", value);
                params.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());

//                params.put("D_ID", m_androidId);
                return params;
            }
        };
        queue.add(request);
    }

    private void addserverimage(String name) {


        RequestQueue queue = Volley.newRequestQueue(getActivity());
//   Toast.makeText(MainActivity.this, "Response was submit", Toast.LENGTH_SHORT).show();

        StringRequest request = new StringRequest(Request.Method.POST, "https://upgradeinfotech.in/projects/ModesToolbox/v1.1/user_profile_image_update.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG14", "onResponse: " + response);
                Log.d("TAG14", "onResponse: " + response);
                if (response.equals("Updated successfully")) {
                    Glide.with(getActivity())
                            .load("https://upgradeinfotech.in/projects/ModesToolbox/v1.1/upload/" + name)
                            .apply(new RequestOptions().transform(new FitCenter()))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    pd.cancel();
                                    Log.d("TAG21", "onLoadFailed: " + e);
                                    Toast.makeText(getActivity(), "Upadte error" + e, Toast.LENGTH_SHORT).show();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    pd.cancel();
//                        saveImage(((BitmapDrawable) resource).getBitmap());
                                    return false;
                                }
                            })
                            .into(profile);
                    pd.cancel();
                    Toast.makeText(getActivity(), "image Add", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "image Not Add", Toast.LENGTH_SHORT).show();

                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), "Data Fail", Toast.LENGTH_SHORT).show();

                Log.v("TAG14", "response " + error);
//                                    Toast.makeText(MainActivity.this, "Response was not submit", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                Log.d("TAG21", "getParams: " + m_androidId);

                params.put("image_url", "https://www.upgradeinfotech.in/projects/ModesToolbox/v1.1/upload/" + name);
                params.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                return params;
            }
        };
        queue.add(request);
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
                    final String[] selectionArgs = new String[]{split[1]};

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
            final String[] projection = {column};

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
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        public static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri
                    .getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        public static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri
                    .getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        public static boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri
                    .getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        public static boolean isGooglePhotosUri(Uri uri) {
            return "com.google.android.apps.photos.content".equals(uri
                    .getAuthority());
        }
    }
}