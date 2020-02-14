package com.aboukalil.phoneinsider1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.aboukalil.phoneinsider1.R;


public class ChangePassword extends AppCompatActivity {

    Button changePassword ;
    private EditText  old_password ,new_password ,c_new_password;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        old_password = (EditText) findViewById(R.id.OLD_password);
        new_password = (EditText) findViewById(R.id.NEW_password);
        c_new_password = (EditText) findViewById(R.id.c_NEW_password);
        changePassword = (Button) findViewById(R.id.signup);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _old_password = old_password.getText().toString();
                String _new_password = new_password.getText().toString();
                String c_new_password = new_password.getText().toString();

                if (_old_password.equals("") || _new_password.equals("") || c_new_password.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Fill All Fields", Toast.LENGTH_LONG).show();
                }
                else
                {
                    try {
                        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                        String myPassword = db.getPassword();
                        if (_old_password.equals(myPassword) && _new_password.equals(c_new_password) && _new_password.length() > 4)
                        {
                            String _username = db.getUsername();
                            int result = db.changePassword(_username, _old_password, _new_password);
                            if(result==1)
                            {

                                Toast.makeText(ChangePassword.this, "Password Changed Successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ChangePassword.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(ChangePassword.this, "User Name or Old Password does not match", Toast.LENGTH_LONG).show();
                            }
                        } else if (!_old_password.equals(myPassword)) {
                            Toast.makeText(ChangePassword.this, "Old Password is Incorrect", Toast.LENGTH_LONG).show();
                        }
                        else if(!_new_password.equals(c_new_password))
                        {
                            Toast.makeText(ChangePassword.this, "New Password  and Confirm Password do not match", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(ChangePassword.this, "New Password must be at least 5 characters", Toast.LENGTH_LONG).show();
                        }
                    }catch(Exception e)
                    {
                        Toast.makeText(ChangePassword.this, "Error : "+e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onDestroy() {
        super.onDestroy();
    }
}





