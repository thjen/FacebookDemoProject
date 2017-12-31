package com.example.q_thjen.facebookdemoproject;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;
import com.facebook.ProfileManager;
import com.facebook.ProfileTracker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ProfilePictureView image;
    LoginButton loginButton;
    Button bt_logout, bt_chucnang;
    TextView tv_name, tv_email, tv_firstName;

    CallbackManager callbackManager;

    String name, first_name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create(); // service trả về 1 lời nhắn qua callbackmanager
        setContentView(R.layout.activity_main);

//        try {
//            PackageInfo info = null;
//            try {
//                info = getPackageManager().getPackageInfo(
//                        "com.example.q_thjen.facebookdemoproject",
//                        PackageManager.GET_SIGNATURES);
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (NoSuchAlgorithmException e) {
//        }

        findView();

        image.setVisibility(View.INVISIBLE);
        bt_chucnang.setVisibility(View.INVISIBLE);
        bt_logout.setVisibility(View.INVISIBLE);
        tv_name.setVisibility(View.INVISIBLE);
        tv_firstName.setVisibility(View.INVISIBLE);
        tv_email.setVisibility(View.INVISIBLE);

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        setupLoginButton();
        setupLogouButton();
        chucNang();

    }

    private void chucNang() {

        bt_chucnang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ChucNang.class);
                startActivity(intent);

            }
        });

    }

    private void setupLogouButton() {

        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logOut();

                bt_logout.setVisibility(View.INVISIBLE);
                bt_chucnang.setVisibility(View.INVISIBLE);
                bt_logout.setVisibility(View.INVISIBLE);
                tv_name.setVisibility(View.INVISIBLE);
                tv_firstName.setVisibility(View.INVISIBLE);
                tv_email.setVisibility(View.INVISIBLE);

                tv_email.setText("");
                tv_firstName.setText("");
                tv_name.setText("");

                image.setProfileId(null);
                loginButton.setVisibility(View.VISIBLE);

            }
        });

    }

    private void setupLoginButton() {

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                loginButton.setVisibility(View.INVISIBLE);

                image.setVisibility(View.VISIBLE);
                bt_chucnang.setVisibility(View.VISIBLE);
                bt_logout.setVisibility(View.VISIBLE);
                tv_name.setVisibility(View.VISIBLE);
                tv_firstName.setVisibility(View.VISIBLE);
                tv_email.setVisibility(View.VISIBLE);

                result();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void result() {

        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback()  {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                Log.d("data", response.getJSONObject().toString());
                try {
                    email = object.getString("email");
                    name = object.getString("name");
                    first_name = object.getString("first_name");

                    image.setProfileId(object.getString("id"));
                    tv_name.setText(name);
                    tv_email.setText(email);
                    tv_firstName.setText(first_name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email,first_name");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();

    }

    private void findView() {

        image = findViewById(R.id.image);
        loginButton = findViewById(R.id.login_button);
        bt_chucnang = findViewById(R.id.bt_chucNang);
        bt_logout = findViewById(R.id.log_out);
        tv_name = findViewById(R.id.tv_name);
        tv_firstName = findViewById(R.id.tv_firstName);
        tv_email = findViewById(R.id.tv_email);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoginManager.getInstance().logOut();    // khi run app trở về trạng thái đăng nhập
    }

}
