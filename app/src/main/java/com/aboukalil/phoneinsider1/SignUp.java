package com.aboukalil.phoneinsider1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aboukalil.phoneinsider1.R;

public class SignUp extends AppCompatActivity {

    private Button btn_register;
    private EditText et_username, et_password, et_confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        DatabaseHelper db = new DatabaseHelper(SignUp.this);

        if (db.CountUser())
        {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
            finish();
        }
        else
        {
            et_username = (EditText) findViewById(R.id.s_username);
            et_password = (EditText) findViewById(R.id.s_password);
            et_confirm_password = (EditText) findViewById(R.id.s_c_password);
            btn_register = (Button) findViewById(R.id.signup);
            btn_register.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String username = et_username.getText().toString();
                    String password = et_password.getText().toString();
                    String c_password = et_confirm_password.getText().toString();

                    if (username.equals("") || password.equals("") || c_password.equals("")) {
                        Toast.makeText(SignUp.this, "Fill All Fields", Toast.LENGTH_LONG).show();
                    } else if (!password.equals(c_password)) {
                        Toast.makeText(SignUp.this, "passwords should be equals", Toast.LENGTH_LONG).show();
                    } else if (password.length() < 5) {
                        Toast.makeText(SignUp.this, "Password must be at least 5 characters", Toast.LENGTH_LONG).show();
                    } else {
                        DatabaseHelper db = new DatabaseHelper(SignUp.this);
                        if(db.signUp(username, password))
                        {
                            Toast.makeText(SignUp.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignUp.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                            Toast.makeText(SignUp.this, "Registered Failed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    public void onDestroy() {
        super.onDestroy();
    }
}
