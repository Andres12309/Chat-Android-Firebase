package com.studentsac.iachat.providers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.studentsac.iachat.R;
import com.studentsac.iachat.activities.CompleteInfoActivity;
import com.studentsac.iachat.models.User;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mfirebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;

    UsersProvider mUsersProvider;

    final String TAG = "LoginActivity";
    int RC_SIGN_IN =1;
    String TAG1 = "GoogleSignIn";

    Button btnGoogle;

    private ProgressDialog mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);
        btnGoogle = findViewById(R.id.btnGoogle);

        mUsersProvider = new UsersProvider();

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresar();
            }
        });

        mProgressBar = new ProgressDialog(MainActivity.this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        mfirebaseAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG1, "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    Log.w(TAG1, "Google sign in failed", e);
                }
            } else {
                Log.d(TAG1, "Error, login no exitoso:" + task.getException().toString());
                Toast.makeText(this, "Ocurrio un error. " + task.getException().toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mfirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mfirebaseAuth.getCurrentUser();
                    Log.d(TAG, "signInWithCredential:success");
                    User mUser = new User();
                    mUser.setId(user.getUid());
                    mUser.setUsername(user.getDisplayName());
                    mUser.setEmail(user.getEmail());
                    mUsersProvider.create(mUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(MainActivity.this, CompleteInfoActivity.class);
                            startActivity(intent);
                        }
                    });
                    MainActivity.this.finish();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                }
            }
        });
    }

    public void ingresar(){
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart(){
        FirebaseUser user = mfirebaseAuth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(MainActivity.this, CompleteInfoActivity.class));
        }
        super.onStart();
    }

    /*private void saveData(){
        FirebaseUser user = mfirebaseAuth.getCurrentUser();
        Map<String,Object> map = new HashMap<>();
        map.put("nombre",user.getDisplayName());
        mFirestore.collection("Usuarios").document().set(map);

    }*/


    private void showError(EditText input, String s){
        input.setError(s);
        input.requestFocus();
    }

}