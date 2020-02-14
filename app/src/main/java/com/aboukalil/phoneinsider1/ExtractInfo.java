package com.aboukalil.phoneinsider1;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.aboukalil.phoneinsider1.R;


public class ExtractInfo extends AppCompatActivity implements View.OnClickListener
{
    Button send ,cancel,get_sms,get_contact,on_wifi,off_wifi,on_data,off_data,silent,normal,vibration;
    AlertDialog dialog;
    View m_view;
    TextView pageTitle;
    ActionBar actionBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract_info);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        get_sms    = (Button) findViewById(R.id.get_sms);
        get_contact= (Button) findViewById(R.id.get_contact);
        on_wifi    = (Button) findViewById(R.id.on_wifi);
        off_wifi   = (Button) findViewById(R.id.off_wifi);
        on_data    = (Button) findViewById(R.id.on_data);
        off_data   = (Button) findViewById(R.id.off_data);
        silent     = (Button) findViewById(R.id.silent);
        normal     = (Button) findViewById(R.id.normal);
        vibration  = (Button) findViewById(R.id.vibration);

        get_sms.setOnClickListener(this);
        get_contact.setOnClickListener(this);
        on_wifi.setOnClickListener(this);
        off_wifi.setOnClickListener(this);
        on_data.setOnClickListener(this);
        off_data.setOnClickListener(this);
        silent.setOnClickListener(this);
        normal.setOnClickListener(this);
        vibration.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.get_sms:
                getInfo_SMS("GetSMS","Get Message");
                break;
            case R.id.get_contact:
                getInfo_SMS("GetContact","Get Contact");
                break;
            case R.id.on_wifi:
                UpdatePhone_SMS("wifi|open","Open WiFi");
                break;
            case R.id.off_wifi:
                UpdatePhone_SMS("wifi|close","Close WiFi");
                break;
            case R.id.on_data:
                UpdatePhone_SMS("data|open","Open Mobile Data");
                break;
            case R.id.off_data:
                UpdatePhone_SMS("data|close","Close Mobile Data");
                break;
            case R.id.normal:
                UpdatePhone_SMS("ChangeMode|normal","Change Mode to Normal");
                break;
            case R.id.silent:
                UpdatePhone_SMS("ChangeMode|silent","Change Mode to Silent");
                break;
            case R.id.vibration:
                UpdatePhone_SMS("ChangeMode|vibration","Change Mode to Vibrate");
                break;
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public  void getInfo_SMS(final String task,final String title)
    {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ExtractInfo.this);
        LayoutInflater inflater = getLayoutInflater();
        m_view = inflater.inflate(R.layout.get_info,null);

        pageTitle = (TextView) m_view.findViewById(R.id.getInfo_pageTitle);
        pageTitle.setText(title);

        send = (Button) m_view.findViewById(R.id.send);
        cancel = (Button) m_view.findViewById(R.id.cancel);

        mBuilder.setView(m_view);
        dialog =  mBuilder.create();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText number = (EditText) m_view.findViewById(R.id.info_number);
                final  EditText password = (EditText) m_view.findViewById(R.id.info_password);
                final  EditText contact_name = (EditText) m_view.findViewById(R.id.contact_name);

                String phone = number.getText().toString();
                String passWord = password.getText().toString();
                String contactName = contact_name.getText().toString();

                if (!phone.equals("") && !passWord.equals("") && !contactName.equals(""))
                {
                    SMS sms = new SMS();
                    String command = "SMSInsider|"+passWord+"|"+task+"|"+contactName;
                    sms.Send_message(phone,command);
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Fill All Fields", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public  void UpdatePhone_SMS(final String task,final String title)
    {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ExtractInfo.this);
        LayoutInflater inflater = getLayoutInflater();
        m_view = inflater.inflate(R.layout.update_phone,null);

        pageTitle = (TextView) m_view.findViewById(R.id.update_phone_pageTitle);
        pageTitle.setText(title);

        send = (Button) m_view.findViewById(R.id.send);
        cancel = (Button) m_view.findViewById(R.id.cancel);

        mBuilder.setView(m_view);
        dialog =  mBuilder.create();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText number = (EditText) m_view.findViewById(R.id.info_number);
                final  EditText password = (EditText) m_view.findViewById(R.id.info_password);

                String phone = number.getText().toString();
                String passWord = password.getText().toString();

                if (!phone.equals("") && !passWord.equals(""))
                {
                    SMS sms = new SMS();
                    String command = "SMSInsider|"+passWord+"|"+task;
                    sms.Send_message(phone,command);
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Fill All Fields", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
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

}
