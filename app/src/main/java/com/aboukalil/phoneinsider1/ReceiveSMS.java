package com.aboukalil.phoneinsider1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import com.aboukalil.phoneinsider1.SMS.Message;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static com.aboukalil.phoneinsider1.SMS.isInteger;

public class ReceiveSMS extends BroadcastReceiver
{
    public ReceiveSMS() {}

    @Override
    public void onReceive(Context context, Intent intent)
    {

        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
        {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                for (SmsMessage currentMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent))
                {
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getMessageBody();
                    if (message.contains("SMSInsider") || message.contains("smsinsider"))
                    {
                        ReceiveSMS r = new ReceiveSMS();
                        try {
                            r.verify(phoneNumber, message, context);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else
            {
                // Get Bundle object contained in the SMS intent passed in
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    SmsMessage[] msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++)
                    {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        String message = currentMessage.getDisplayMessageBody();
                        if (message.contains("SMSInsider") || message.contains("smsinsider"))
                        {
                            ReceiveSMS r = new ReceiveSMS();
                            try {
                                r.verify(phoneNumber, message, context);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (NoSuchFieldException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

    }

    public void verify(String phone , String body, Context cntxt) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        String[] arrayString = body.split("\\|");
        String signal = arrayString[0].replaceAll("(\\s)", "");
        String password = arrayString[1].replaceAll("(\\s)", "");
        String main_task = arrayString[2].replaceAll("(\\s)", "");
        String sub_task ="";

        DatabaseHelper db = new DatabaseHelper(cntxt);
        String myPassword = db.getPassword();
        if(signal.equalsIgnoreCase("smsinsider") && password.equals(myPassword))
        {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy 'at' HH:mm:ss");
            String date = sdf.format(new Date());
            SMS smsObj = new SMS();
            String result;
            if(main_task.equalsIgnoreCase("SMS") || main_task.equalsIgnoreCase("Message") || main_task.equalsIgnoreCase("getSMS") || main_task.equalsIgnoreCase("getMessage"))
            {
                sub_task = arrayString[3];
                result = getSMS(sub_task,cntxt);
                smsObj.Send_message(phone,result);
                db.addReportNode(date,main_task+" : "+sub_task,phone);
            }
            else if(main_task.equalsIgnoreCase("getContact") || main_task.equalsIgnoreCase("Contact"))
            {
                sub_task = arrayString[3];
                result = getContact(sub_task,cntxt);
                smsObj.Send_message(phone,result);
                db.addReportNode(date,main_task+" : "+sub_task,phone);
            }
            /*
            else if(main_task.equalsIgnoreCase("Location") || main_task.equalsIgnoreCase("getLocation"))
            {
                Location_Activity la = new Location_Activity(cntxt);
                result = la.getCurrentLocation();
                smsObj.Send_message(phone,result);
                db.addReportNode(date,main_task,phone);
            }*/
            else if(main_task.equalsIgnoreCase("changeMode") || main_task.equalsIgnoreCase("Mode"))
            {
                Mode m = new Mode();
                sub_task =arrayString[3].replaceAll("(\\s)", "");
                result =  m.changeMode(sub_task , cntxt);
                smsObj.Send_message(phone,result);
                db.addReportNode(date,main_task+" : "+sub_task,phone);
            }
            else if(main_task.equalsIgnoreCase("wifi"))
            {
                Network network = new Network();
                sub_task =arrayString[3].replaceAll("(\\s)", "");
                if(sub_task.equalsIgnoreCase("open") || sub_task.equalsIgnoreCase("on"))
                {
                    if(network.getWiFiState(cntxt)== 0 || network.getWiFiState(cntxt)== 1)
                    {
                        network.setMobileWiFi(cntxt,true);
                        result = "WiFi opened";
                    }
                    else
                    {
                        result = "WiFi is already open";
                    }
                    smsObj.Send_message(phone,result);
                    db.addReportNode(date,main_task+" : "+sub_task,phone);
                }
                else if(sub_task.equalsIgnoreCase("close") || sub_task.equalsIgnoreCase("off"))
                {
                    if(network.getWiFiState(cntxt)== 2 || network.getWiFiState(cntxt)== 3)
                    {
                        network.setMobileWiFi(cntxt,false);
                        result = "WiFi closed";
                    }
                    else
                    {
                        result = "WiFi is already closed";
                    }
                    smsObj.Send_message(phone,result);
                    db.addReportNode(date,main_task+" : "+sub_task,phone);
                }
            }
            else if(main_task.equalsIgnoreCase("data"))
            {
                Network network = new Network();
                sub_task =arrayString[3].replaceAll("(\\s)", "");
                if(sub_task.equalsIgnoreCase("open") || sub_task.equalsIgnoreCase("on"))
                {
                    if(!network.isMobileDataEnabled(cntxt))
                    {
                        network.setMobileData(cntxt ,true);
                        result = "Mobile Data opened";
                    }
                    else
                    {
                        result = "Mobile Data is already open";
                    }
                    smsObj.Send_message(phone,result);
                    db.addReportNode(date,main_task+" : "+sub_task,phone);
                }
                else if(sub_task.equalsIgnoreCase("close") || sub_task.equalsIgnoreCase("off"))
                {
                    if(network.isMobileDataEnabled(cntxt))
                    {
                        network.setMobileData(cntxt ,false);
                        result = "Mobile Data closed";
                    }
                    else
                    {
                        result = "Mobile Data is already closed";
                    }
                    smsObj.Send_message(phone,result);
                    db.addReportNode(date,main_task+" : "+sub_task,phone);
                }
            }
        }
    }

    private String getSMS(String sub_task ,Context cntxt)
    {
        List<Message> messages =  new ArrayList<>();
        SMS sms = new SMS();
        //contact name or number
        String input = sub_task;
        // if input start with '+' then store the input without '+' to check  in n to chech in a number or not
        String n ="" ;
        String result="";
        // all number of this contact
        List<String> numbers = new ArrayList<>();

        if (!input.equals(""))
        {
            if(input.charAt(0)=='+')
            {
                //input start with '+' remove the '+' and replace with empty
                n = input.replace("+","");
            }
            boolean isNumber1 = isInteger(n);     // check input is a number
            boolean isNumber2 = isInteger(input);

            if(!isNumber1 && !isNumber2)
            {
                //the input is a name
                try
                {
                    Contact c = new Contact();
                    numbers = c.getNumber(input , cntxt);
                }
                catch (Exception e) { }
            }
            else
            {
                //input is a number
                numbers.add(input);
            }
            messages = sms.getAllSms(cntxt);  // get all inbox message
            if (messages.size() !=0) {
                List<String> contact_messages = sms.get_sms_of_contact(numbers , messages);
                int count_sms = contact_messages.size();
                if(count_sms>3)
                    count_sms=3;

                if(contact_messages.size() !=0)
                {
                    for(int i=0 ; i<count_sms ; i++) // print message
                        result +="Message "+(i+1)+" :\n"+contact_messages.get(i)+"\n";
                }
                else {
                    result = "There is no message from : " + input;
                }
            }
            else { result ="InBox is empty"; }
        }
        else {
            result="Fill Contact Name or number to get messages";
        }
        return  result;
    }

    private String getContact(String sub_task,Context cntxt)
    {
        Contact c = new Contact();
        String result ="";
        List<String> numbers = new ArrayList<>();
        numbers  = c.getNumber(sub_task,cntxt);

        if (numbers.size()==0)
        {
            result = "No number with this name";
        }
        else
        {
            for(int i=0; i<numbers.size();i++)
            {
                String number =  numbers.get(i);
                number = number.replaceAll("(\\s)", "");
                if(!result.contains(number))
                {
                    result += number+" ,";
                }
            }
        }
        return result;
    }
}
