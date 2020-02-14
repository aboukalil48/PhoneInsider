package com.aboukalil.phoneinsider1;

import android.content.Context;
import android.media.AudioManager;


public class Mode
{


    public Mode(){}
    public String Normal(AudioManager am)
    {
        String state="";
        try {
            try {
                am.setRingerMode(2);
                state = "Mode changed to Normal";
            }catch (Exception e){}

        } catch (Exception e)
        {
            //e.printStackTrace();
            state = e.toString();
        }
        return state;
    }

    public String Vibrate(AudioManager am)
    {
        String state="";
        try {
            am.setRingerMode(1);
            state = "Mode changed to Vibrate";
        }catch (Exception e){}

        return state;
    }

    public String Silent(AudioManager am)
    {
        String state="";
        try {
            am.setRingerMode(0);
            state = "Mode changed to Silent";
        }catch (Exception e){}

        return state;
    }

    public String changeMode(String sub_task ,Context cntxt)
    {
        String state="";
        Mode m = new Mode();

        if(!sub_task.isEmpty())
        {
            AudioManager am = (AudioManager) cntxt.getSystemService(Context.AUDIO_SERVICE);
            if(sub_task.equalsIgnoreCase("normal") || sub_task.equalsIgnoreCase("general"))
            {
                state = m.Normal(am);
            }
            else if(sub_task.equalsIgnoreCase("vibrate") || sub_task.equalsIgnoreCase("vibration"))
            {
                state = m.Vibrate(am);
            }
            else if(sub_task.equalsIgnoreCase("silent"))
            {
                state = m.Silent(am);
            }
        }
        else
        {
            state="Write correct mode : silent , vibrate , normal or airplane";
        }
        return state;
    }

}
