package com.aboukalil.phoneinsider1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aboukalil.phoneinsider1.R;

public class Login extends AppCompatActivity {
    private EditText et_username, et_password;
    Button btn_login ,btn_extract_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = (EditText) findViewById(R.id.l_username);
        et_password = (EditText) findViewById(R.id.l_password);

        btn_login = (Button) findViewById(R.id.login);
        btn_extract_info = (Button) findViewById(R.id.getInfo);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String _username = et_username.getText().toString();
                String _password = et_password.getText().toString();

                try {
                    // fetch the Password form database for respective user name
                    DatabaseHelper db = new DatabaseHelper(Login.this);
                    boolean userExist = db.checkUserExist(_username, _password);
                    // check if the Stored password matches with  Password entered by user
                    if (_username.equals("") || _password.equals("")) {
                        Toast.makeText(getApplicationContext(), "Fill All Fields", Toast.LENGTH_LONG).show();
                    } else if (userExist)
                    {
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Login.this, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_extract_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login.this,ExtractInfo.class);
                startActivity(i);
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
    }

}
