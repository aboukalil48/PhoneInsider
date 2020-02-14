package com.aboukalil.phoneinsider1;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.List;

public class SMS
{
    public SMS(){}
    class Message
    {
        private String  address , body ;
        public Message(String address  , String body) {
            this.address = address;
            this.body = body; }
        public String getAddress() {
            return address;
        }
        public String getBody() {
            return body;
        }
    }

    // get the last 3 messages
    public List<Message> getAllSms(Context context)
    {
        List<Message> smsList = new ArrayList<>();
        try {
            Cursor c = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
            if (c != null) {
                int totalSMS = c.getCount();
                if (c.moveToFirst()) {
                    for (int j = 0; j < totalSMS; j++) {
                        String address = c.getString(c.getColumnIndexOrThrow("address"));
                        String body = c.getString(c.getColumnIndexOrThrow("body"));
                        Message m = (new Message(address, body));
                        smsList.add(m);
                        c.moveToNext();
                    }
                }
                c.close();
            }
        } catch (Exception e) {
        }
        return smsList;
    }

    public void Send_message(String phone , String message)
    {
        if(!phone.equals("") && !message.equals(""))
        {
            SmsManager sendsms = SmsManager.getDefault();
            try
            {
                sendsms.sendTextMessage(phone,null,(message),null,null);
            } catch (Exception e) { }
        }
    }


    public List<String> get_sms_of_contact(List<String> address, List<Message> listSMS) {
        List<String> message = new ArrayList<>();

        if (address.size() != 0)
            for (int i = 0; i < listSMS.size(); i++) {
                for (int j = 0; j < address.size(); j++) {
                    if (address.get(j).equals((listSMS.get(i).getAddress())) || listSMS.get(i).getAddress().contains(address.get(j)))
                    {
                        message.add(listSMS.get(i).getBody());
                    }
                }
            }
        return message;
    }

    public static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix)
    {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }
}

