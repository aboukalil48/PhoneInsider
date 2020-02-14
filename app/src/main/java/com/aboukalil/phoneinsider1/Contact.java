package com.aboukalil.phoneinsider1;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;


public class Contact
{
    public Contact() {}


    public List<String> getNumber(String NAME , Context context) {
        List<String> numbers = new ArrayList<>();
        if (!NAME.equals("")) {
            ContentResolver cr = context.getContentResolver();
            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                    "DISPLAY_NAME = '" + NAME + "'", null, null);
            if (cursor != null && cursor.moveToFirst()) {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                while (phones != null && phones.moveToNext()) {
                    String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (!numbers.contains(number) || numbers.size() == 0)
                        numbers.add(number);
                }
                if (phones != null)
                    phones.close();
            }
            if (cursor != null)
                cursor.close();
        }
        return numbers;
    }

}
