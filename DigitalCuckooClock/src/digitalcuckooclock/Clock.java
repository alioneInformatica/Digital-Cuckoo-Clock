/*
 * This project is kindly offered under the GNU GPL v3 license. 
 * Please read carefully the terms and conditions of it. 
 * Any action or activity not expressly granted may be liable to prosecution.
 */

package digitalcuckooclock;

import com.pi4j.wiringpi.Gpio;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/*
 * @author Ali One Informatica
 */

public class Clock {
    /* Get actual class name to be printed on */
    static Logger log = Logger.getLogger(Clock.class.getName());
    
    LCD lcd = new LCD();
    Sound sound = new Sound();
    
    static private boolean pause=false;
    private static String alarmTime = "17:37"; // Default value: null
    private Boolean alarmStart = false; // We use alarmStart variable to check if alarm is still running
    
    
    
    public void start(){
        /* Check every seconds if alarmTime is equal to current time.
         *
         * N.B.: We run this scheduled Thread every seconds end not every minutes because 
         * java.util.concurrent.TimeUnit execute the Runnable every minutes BUT with the seconds 
         * saved by the first execution.
        */
        lcd.clear();
        
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(new Runnable(){
            @Override
            public void run(){
                if(pause==false){
                    // Get date time
                    Date date = new Date();

                    // Get current time
                    DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
                    String time_str = dateFormatTime.format(date);

                    // Get current date
                    DateFormat dateFormatDate = new SimpleDateFormat("dd/MM/yyyy");
                    String date_str = dateFormatDate.format(date);

                    // Clear LCD, display time and display date
                    lcd.write(4, 0, time_str);
                    lcd.write(3, 1, date_str);
                    
                    // If alarm time is set, show on the LCD a symbol. Otherwise not show anything.
                    if(!alarmTime.equals("null")){
                        lcd.write(15, 0, ".");
                    }else{
                        lcd.write(15, 0, " ");
                    }

                    // Check Alarm
                    checkAlarm();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
        
    }
    
    public void checkAlarm(){
        if(!alarmTime.equals("null")){
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String time = sdf.format(cal.getTime());

            if(time.equals(alarmTime) && alarmStart==false){ // Nothing is repeated if alarm is still running
                // play cuckoo sound
                sound.playSound();

                alarmStart=true;
            }

            if(!time.equals(alarmTime)){ // Reset alarmStart variable for the next alarm
                alarmStart=false;
            }
        }
    }
    
    
    public static void pause(){
        pause=true;
    }
    
    public static void resume(){
        pause=false;
    }
    
    public static String getAlarm(){
        if(alarmTime.equals("null")){
            return "00:00";
        }else{
            return alarmTime;
        }
    }
    
    public static void setAlarm(String alarmClock){
        alarmTime=alarmClock;
    }
    
}
