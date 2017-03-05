package com.example.galan.tubes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Signup extends AppCompatActivity {
    EditText edit_username;
    EditText edit_email;
    EditText edit_pass;
    Button btn_sign;
    EditText userid;
    private static final String REGISTER_URL = "http://pandumalik.esy.es/UserRegistration/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edit_username = (EditText) findViewById(R.id.id_username);
        edit_email = (EditText) findViewById(R.id.id_email);
        edit_pass = (EditText) findViewById(R.id.id_pass);
        btn_sign = (Button) findViewById(R.id.btn_signup);
        userid = (EditText) findViewById(R.id.iduser);

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = edit_username.getText().toString();
        String iduser = userid.getText().toString();
        String email = edit_email.getText().toString();
        String password = edit_pass.getText().toString();
        register(username, password, email, iduser);
    }

    private void register(String username, String password, String email, String iduser) {
        String urlSuffix = "?username=" + username + "&password=" + password + "&email=" + email + "&iduser=" + iduser;
        class RegisterUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Signup.this, "Please Wait", "Creating Your Account", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(Signup.this, Login.class);
                startActivity(myIntent);
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferReader = null;
                try {
                    URL url = new URL(REGISTER_URL + s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String result;
                    result = bufferReader.readLine();
                    return result;

                } catch (Exception e) {
                    return null;
                }
            }

        }
        RegisterUser ur = new RegisterUser();
        ur.execute(urlSuffix);
    }
}