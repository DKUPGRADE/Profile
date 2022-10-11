package com.example.profile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.profile.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SignInButton signInButton;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    String email1, name, idToken,uid,image,User_uid;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//
//        bottomNavigationView.setonItemSelectedListener(MainActivity.this);
//        bottomNavigationView.setSelectedItemId(R.id.person);

        signInButton = findViewById(R.id.sign_goggle);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);



//                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//                startActivityForResult(signInIntent, RC_SIGN_IN);
//
//                mGoogleApiClient.connect(); //Adding this worked for me!
//                gotoProfile();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("c101", "onActivityResult: " + requestCode);
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }

    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("TAG101", "handleSignInResult: " + result);
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null){
            Log.d("21vb", "onCreate: "+uid);
            uid = currentUser.getUid();

        }
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String authCode = account.getServerAuthCode();

            Log.d("54dfg", "handleSignInResult: " + authCode);

//            assert account != null;
             ;
            idToken = account.getIdToken();
            name = account.getDisplayName();
            email1 = account.getEmail();
//            image = account.getPhotoUrl().toString();
            uid = account.getId();
            Log.d("21vb", "onCreate: "+uid);
           image = account.getPhotoUrl().toString();

//            Log.d("14xC", "handleSignInResult: "+currentFirebaseUser);
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//            Log.d("14xC", "handleSignInResult:idToken "+idToken);
            firebaseAuthWithGoogle(credential);


         // you can store user data to SharedPreference

        } else {
            // Google Sign In failed, update UI appropriately
            Log.d("123", "Login Unsuccessful. " + result.getStatus());
            Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        image = account.getPhotoUrl().toString();
//        if (account != null) {
//            gotoProfile();
//            Toast.makeText(this, "sign in", Toast.LENGTH_SHORT).show();
//        }
//
//        else {
//            Toast.makeText(this, "sign out", Toast.LENGTH_SHORT).show();
//
//        }


    }
    @Override
    protected void onStart()
    {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        image = account.getPhotoUrl().toString();

        if (account != null) {
            gotoProfile();
            Toast.makeText(this, "sign in", Toast.LENGTH_SHORT).show();
        }

        else {
            Toast.makeText(this, "sign out", Toast.LENGTH_SHORT).show();

        }
        super.onStart();
    }


    private void firebaseAuthWithGoogle(AuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                         User_uid =  FirebaseAuth.getInstance().getCurrentUser().getUid();
                        Log.d("Dk101", "onComplete: "+User_uid);
                        Log.d("TAG", "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                            addserver();

                        } else {
                            Log.w("TAG", "signInWithCredential" + task.getException().getMessage());
                            task.getException().printStackTrace();
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    private void gotoProfile() {
        Intent intent = new Intent(MainActivity.this, profileActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

    }

    private void addserver() {


        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, "https://upgradeinfotech.in/projects/ModesToolbox/v1.1/new_user_profile.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("user_name", "onResponse: " + response);
                if (response.equals("New record created successfully") || response.equals("Updated successfully")) {
                    Toast.makeText(MainActivity.this, "Data Add", Toast.LENGTH_SHORT).show();

                    gotoProfile();
                } else {
                    Toast.makeText(MainActivity.this, "Data Not Add", Toast.LENGTH_SHORT).show();

                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Data Fail", Toast.LENGTH_SHORT).show();

                Log.v("error101", "response " + error);
//                                    Toast.makeText(MainActivity.this, "Response was not submit", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                Log.d("TAG21", "getParams: " + m_androidId);

                params.put("email", email1);
                params.put("user_name", name);
                params.put("D_ID", m_androidId);
                params.put("image_url", image);
//                    params.put("firebase_unique_id", uid);
                Log.d("TAG21", "handleSignInResult: " + image);


                return params;
            }
        };
        queue.add(request);
    }


    }






//User_uid =  FirebaseAuth.getInstance().getCurrentUser().getUid();