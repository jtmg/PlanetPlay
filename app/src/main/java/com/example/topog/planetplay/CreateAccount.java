package com.example.topog.planetplay;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CreateAccount extends AppCompatActivity {
//TODO nas editTexts mudar a cor que estao sublinhadas para azul ou verde(ver logo)
    private EditText user ;
    private EditText email;
    private EditText password;
    private EditText confPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }
    public void Click(View view)
    {
        user = (EditText) findViewById(R.id.user_name);
        email = (EditText) findViewById(R.id.email2);
        password = (EditText) findViewById(R.id.password2);
        confPass = (EditText) findViewById(R.id.confirm);


        if ((password.getText().toString().equals("")) || (confPass.getText().toString().equals("")) || (email.getText().toString().equals("")) || (user.getText().toString().equals("")))
        {
            Toast.makeText(this,"fill the fields",Toast.LENGTH_SHORT).show();

        }else
        {
            if (!password.getText().toString().equals(confPass.getText().toString()))
            {
                Toast.makeText(this, "passwords doesn´t match", Toast.LENGTH_SHORT).show();
            }else
            {
                new HttpRequestCreate().execute(user.getText().toString(),email.getText().toString(),password.getText().toString());
            }
        }
    }
    public void createdAccount(String result)
    {
        if (result.equals("false"))
        {
            Toast.makeText(this,"user already exist or email already in use!!!",Toast.LENGTH_SHORT).show();
        }else if (result.equals("inserido com sucesso"))
        {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
    }

    private class HttpRequestCreate extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {

            String urlServer = "http://10.2.224.209:82/Register.php?user_name=";
            urlServer += params[0] + "&email=" + params[1] + "&pass=" + params[2];

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

            createdAccount(result);
        }
    }
}
