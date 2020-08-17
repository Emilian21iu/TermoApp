package com.example.starnet.termoapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginActivity extends AppCompatActivity  {

    private EditText email_field;
    private EditText pass_fiels;
    private Button login_btn;
    private TextView forgotPassword;
    private  TextView registerid;

    private FirebaseAuth auth;
private FirebaseAuth.AuthStateListener authStateListener;

    private FirebaseAuth mAuth;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    private static  final String TAG ="SignInActivity";


    FirebaseAuth firebaseAuth;
    CallbackManager callbackManager;
    LoginButton loginButton;

    TwitterLoginButton loginButton1;

    private ProgressDialog progressDialog;
    private int counter=5;
    private TextView Info;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_login);
        setup();
        printKeyHash();


        mAuth = FirebaseAuth.getInstance();

        firebaseAuth=FirebaseAuth.getInstance();
        callbackManager=CallbackManager.Factory.create();
        loginButton=(LoginButton)findViewById(R.id.facebook);
        loginButton.setReadPermissions("email");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(
                        getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                        getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }

            }
        };

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignIn();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( LoginActivity.this,PasswordActivity.class));
            }
        });


        registerid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( LoginActivity.this,RegisterActivity.class));
            }
        });



        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

       mGoogleSignInClient=GoogleSignIn.getClient(this,gso);
        signInButton=(SignInButton)findViewById(R.id.google);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent=mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent,101);
            }
        });



        loginButton1 = findViewById(R.id.login_button);
        loginButton1.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();

                String userName = result.data.getUserName();
                long userID = result.data.getId();

                String token = authToken.token;
                String secret = authToken.secret;
                login(session);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
        Toast.makeText(LoginActivity.this,"Authentication Failed!",Toast.LENGTH_LONG).show();

            }
        });



    }


    private void signIn(){

loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
    @Override
    public void onSuccess(LoginResult loginResult) {
        handleFacebookAccessToken(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }
});

    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                      Toast.makeText(LoginActivity.this,"" +e.getMessage(),Toast.LENGTH_SHORT).show();
                      Log.e("ERROR_EDMT",""+e.getMessage());
                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String email=authResult.getUser().getEmail();
                Toast.makeText(LoginActivity.this,"You are signed with email : "+email,Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void printKeyHash(){

        try{
            PackageInfo info=getPackageManager().getPackageInfo("com.example.starnet.termoapp", PackageManager.GET_SIGNATURES);
            for(Signature signature: info.signatures){
                MessageDigest messageDigest =MessageDigest.getInstance("SHA");
                messageDigest
                        .update(signature.toByteArray());
                Log.e("KEYHASH", Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


public  void login(TwitterSession session)
{
    String userName=session.getUserName();
    Intent intent=new Intent(LoginActivity.this,HomePage.class);
    intent.putExtra("username",userName);
    startActivity(intent);
}


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton1.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);

        FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {


        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           // Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                            finish();
                            Toast.makeText(getApplicationContext(),"User logged in successfully",Toast.LENGTH_SHORT).show();
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                           // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"Could not log in user",Toast.LENGTH_SHORT).show();
                           // Snackbar.make(findViewById(R.id.google), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                           // updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private void startSignIn(){
        String email = email_field.getText().toString();
        String pass = pass_fiels.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
            Toast.makeText(LoginActivity.this,"Empty field, please complete it",Toast.LENGTH_SHORT).show();
        }else{
            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(LoginActivity.this,"Sign in problem!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }


    private void validate(String userName, String userPassword) {

        progressDialog.setMessage("You can subscribe to my channel until you are verified!");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    //Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    checkEmailVerification();
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    counter--;
                    Info.setText("No of attempts remaining: " + counter);
                    progressDialog.dismiss();
                    if (counter == 0) {
                       login_btn.setEnabled(false);
                    }
                }
            }
        });
    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();

        startActivity(new Intent(LoginActivity.this, MainActivity.class));

//        if(emailflag){
//            finish();
//            startActivity(new Intent(MainActivity.this, SecondActivity.class));
//        }else{
//            Toast.makeText(this, "Verify your email", Toast.LENGTH_SHORT).show();
//            firebaseAuth.signOut();
//        }
    }



    private void setup(){
        email_field = findViewById(R.id.email_field);
        pass_fiels = findViewById(R.id.password_field);
        login_btn = findViewById(R.id.login_btn);
        auth = FirebaseAuth.getInstance();
        forgotPassword=(TextView)findViewById(R.id.forgotid);
        registerid=(TextView)findViewById(R.id.registerdoc);

    }


}
