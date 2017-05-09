package com.example.topog.planetplay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        LoginManager.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        String token = sp.getString("token",null);
        if (token != null)
        {
            Bundle bundle = new Bundle();
            bundle.putString("ID",token);
            Start(bundle);
        }


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                SharedPreferences.Editor editor = sp.edit();
                final String[] fbuser = new String[2];

                GraphRequestAsyncTask request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        fbuser[0] = object.optString("name");
                        fbuser[1] = object.optString("name");
                    }
                }).executeAsync();

                Log.i("lindos", fbuser[0]);

                editor.putString("token",loginResult.getAccessToken().getUserId());
                editor.commit();
                Bundle bundle = new Bundle();
                bundle.putString("ID",loginResult.getAccessToken().getUserId());
                Start(bundle);
            }
            @Override
            public void onCancel() {
                Log.i("status","onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("status","onError");

            }
        });



        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.topog.planetplay", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


    }
    public void Start(Bundle bundle)
    {
        Intent intent = new Intent(this,Home.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void ClickMe(View view) {
        username =(EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        if (view.getId() == R.id.Create_Account)
        {
            Intent intent = new Intent(this,CreateAccount.class);
            startActivity(intent);
            finish();
        }else if(view.getId() == R.id.login)
        {
            String user = String.valueOf(username.getText());
            String pass = String.valueOf(password.getText());
            new HttpRequestLogin().execute(user,pass);
        }
    }
    public void Login (String result)
    {
        if (result.equals("user exists"))
        {
            Intent intent = new Intent(this, Home.class);
            Bundle bundle = new Bundle();
            bundle.putString("Fb","no");
            intent.putExtras(bundle);
            startActivity(intent);
            finish();


        }else
        {
            Toast.makeText(this,"wrong credentials",Toast.LENGTH_SHORT).show();
        }
    }


    private class HttpRequestLogin extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {
            String urlServer = "http://10.2.224.209:82/Login.php?user_name=";
            urlServer += params[0] + "&pass=" +params[1];

            StringBuffer result =new StringBuffer("");

            try {
                URL url = new URL(urlServer);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line= rd.readLine()) != null) result.append(line);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result.toString();
        }
        @Override
        protected void onPostExecute(String result)
        {

            Login(result);
        }

    }
}

