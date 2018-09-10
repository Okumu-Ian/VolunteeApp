package eeyan.icelabs.bigman.volunteeapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends
        AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks{

    private EditText edt_email,edt_pass;
    private SignInButton signInButton;
    private AppCompatButton compatButton;
    private TextView textView,textView2;
    private String str_email,str_password;
    private GoogleApiClient apiClient;
    private int RC_SIGN_IN = 10;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser user;
    private ProgressBar progressBar;
    private ProgressDialog dialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getUser();
        initUI();
    }

    private void getUser()
    {
        configureSignIn();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null)
        {
            startActivity(new Intent(LoginActivity.this,MainFeed.class));
            finish();
        }
    }

    private void initUI()
    {
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Please wait");
        mFirebaseAuth = FirebaseAuth.getInstance();
        edt_email = (EditText) findViewById(R.id.fb_login_email);
        edt_pass = (EditText) findViewById(R.id.fb_login_password);
        signInButton = (SignInButton) findViewById(R.id.signinButton);
        compatButton = (AppCompatButton) findViewById(R.id.btn_login);
        textView = (TextView) findViewById(R.id.txt_forgot_pass);
        textView2 = (TextView) findViewById(R.id.textforgotpass);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        compatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkBlanks(edt_email,edt_pass,view,compatButton);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compatButton.setText("Register");
                view.setVisibility(View.GONE);
            }
        });


    }

    private void authenticate(final AppCompatButton button, final View view)
    {
        progressBar.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!button.getText().toString().equals("Register"))
                {
                    siginWithFirebase(str_email,str_password,view);
                    //finish();
                }else{
                    registerWithFirebase(str_email,str_password,view);
                    //finish();
                }
                progressBar.setVisibility(View.GONE);
            }
        },2500);

    }

    private void checkBlanks(EditText a, EditText b, View view,AppCompatButton button)
    {
        if (a.getText().length()==0)
            a.setError("Kindly fill in a valid email address!");
        else if(b.getText().length()==0)
            b.setError("Kindly fill in this field");
        else if (b.getText().length()<6)
            b.setError("All passwords must be greater than 6 characters\nIt should contain letters and digits");
        else if (a.getText().length()<6)
            Snackbar.make(view,"Kindly enter a valid email address.",Snackbar.LENGTH_LONG)
                    .setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).
                    setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                    .show();
        else
            {
                str_email = a.getText().toString();
                str_password = b.getText().toString();
                authenticate(button,view);
            }
    }



    private void configureSignIn()
    {

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        apiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions)
                .build()
                ;
    }

    private void signIn()
    {
        Intent mIntent;
        mIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(mIntent,RC_SIGN_IN);

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this, "Error connecting to google. Try Again",Snackbar.LENGTH_LONG);
                        }else
                        {
                            startActivity(new Intent(LoginActivity.this,MainFeed.class));
                            finish();
                        }
                    }
                });

    }

    private void registerWithFirebase(String email, String password, final View view)
    {
        dialog.show();
        mFirebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (task.isSuccessful())
                        {
                            startActivity(new Intent(LoginActivity.this,MainFeed.class));
                        }else
                            {
                                Snackbar.make(view,"Kindly check your details and try again",Snackbar.LENGTH_LONG).show();
                            }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(view,"Check your internet connection and continue",Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void siginWithFirebase(String email, String password, final View view)
    {
        mFirebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            startActivity(new Intent(LoginActivity.this,MainFeed.class));
                        }else
                            Snackbar.make(view,"Failed, try again.",Snackbar.LENGTH_LONG).show();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            }else
                Toast.makeText(this, "Error! Kindly try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
