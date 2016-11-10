/*
 * This project is kindly offered under the GNU GPL v3 license. 
 * Please read carefully the terms and conditions of it. 
 * Any action or activity not expressly granted may be liable to prosecution.
 */

package digitalcuckooclock;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author Ali One Informatica
 */

public class Buttons {
    
    /* Get actual class name to be printed on */
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Buttons.class.getName());
    
    LCD lcd = new LCD();
    private String pressedButtonWithState="";
    private int pressButtonTimeEnteringSetup = 3; //In seconds
    private int changeSetupTime=6; //In seconds
    private boolean setAlarmClock = false;
    private boolean setMinuteAlarmClock = false;
    final GpioController gpio = GpioFactory.getInstance();
    
    public void check(){
        // create GPIO listener
        GpioPinListenerDigital listener  = new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                String pinName = event.getPin().getName();
                
                if(pinName.equalsIgnoreCase("left")){ //Left button
                    if(event.getState().isHigh()){
                        pressedButtonWithState="leftHigh";
                    }else{
                        pressedButtonWithState="leftLow";
                    }
                }else if(pinName.equalsIgnoreCase("right")){ //Right button
                    if(event.getState().isHigh()){
                        pressedButtonWithState="rightHigh";
                    }else{
                        pressedButtonWithState="rightLow";
                    }
                }
                
                if(setAlarmClock==false && setMinuteAlarmClock==false){ // If user wants to setup alarm clock or disable it
                    if(pressedButtonWithState.equals("rightHigh")){ //User wants to setup alarm clock
                        long start=0;
                        long now;
                        boolean firstExec = true;
                        boolean reached = false;
                        while(pressedButtonWithState.equals("rightHigh")){ //Check that the button has been pressed for the appointed time
                            if(firstExec==true){
                                start = System.currentTimeMillis();
                                firstExec = false;
                            }else{
                                now = System.currentTimeMillis();
                                int timeInSeconds = (int) (now - start) / 1000;
                                if(timeInSeconds>=pressButtonTimeEnteringSetup){ //After x seconds entering in setting alarm clock page
                                    if(reached==false){
                                        pageSetAlarmClock();
                                        reached=true;
                                    }  
                                }
                            }
                        }
                    }else if(pressedButtonWithState.equals("leftHigh")){ //User wants to disable alarm clock
                        long start=0;
                        long now;
                        boolean firstExec = true;
                        boolean reached = false;
                        while(pressedButtonWithState.equals("leftHigh")){ //Check that the button has been pressed for the appointed time
                            if(firstExec==true){
                                start = System.currentTimeMillis();
                                firstExec = false;
                            }else{
                                now = System.currentTimeMillis();
                                int timeInSeconds = (int) (now - start) / 1000;
                                if(timeInSeconds>=pressButtonTimeEnteringSetup){ //After x seconds entering in setting alarm clock page
                                    if(reached==false){
                                        pageDisableAlarmClock();
                                        reached=true;
                                    }  
                                }
                            }
                        }
                    }
                }else if(setAlarmClock==true && setMinuteAlarmClock==false){ // Change hour to alarm clock time
                    String sveglia = Clock.getAlarm();
                    int h = getIntegerFrom2Digit(sveglia.substring(0, 2));
                    int m = getIntegerFrom2Digit(sveglia.substring(3, 5));
                    
                    if(pressedButtonWithState.equals("leftHigh")){ //Left button for increment
                        h=h+1;
                        if(h==24){
                            h=0;
                        }
                        updateAlarm(h, m);
                    }else if(pressedButtonWithState.equals("rightHigh")){ //Right button for decrement
                        h=h-1;
                        if(h==-1){
                            h=23;
                        }
                        updateAlarm(h, m);
                    }else{
                        long start=0;
                        long now;
                        boolean firstExec = true;
                        boolean reached = false;
                        while(pressedButtonWithState.equals("leftLow") || pressedButtonWithState.equals("rightLow")){ //Check that the buttons have been pressed for the appointed time
                            if(firstExec==true){
                                start = System.currentTimeMillis();
                                firstExec = false;
                            }else{
                                now = System.currentTimeMillis();
                                int timeInSeconds = (int) (now - start) / 1000;
                                if(timeInSeconds>=changeSetupTime){ //After x seconds entering in setting minute page
                                    if(reached==false){
                                        pageMinuteAlarmClock();
                                        reached=true;
                                    }  
                                }
                            }
                        }
                    }
                }else if(setAlarmClock==true && setMinuteAlarmClock==true){ // Change minutes to alarm clock time
                    String sveglia = Clock.getAlarm();
                    int h = getIntegerFrom2Digit(sveglia.substring(0, 2));
                    int m = getIntegerFrom2Digit(sveglia.substring(3,5));
                    
                    if(pressedButtonWithState.equals("leftHigh")){ //Left button for increment
                        m=m+1;
                        if(m==60){
                            m=0;
                        }
                        updateAlarm(h, m);
                    }else if(pressedButtonWithState.equals("rightHigh")){ //Right button for decrement
                        m=m-1;
                        if(m==-1){
                            m=59;
                        }
                        updateAlarm(h, m);
                    }else{
                        long start=0;
                        long now;
                        boolean firstExec = true;
                        boolean reached = false;
                        while(pressedButtonWithState.equals("leftLow") || pressedButtonWithState.equals("rightLow")){ //Check that the buttons have been pressed for the appointed time
                            if(firstExec==true){
                                start = System.currentTimeMillis();
                                firstExec = false;
                            }else{
                                now = System.currentTimeMillis();
                                int timeInSeconds = (int) (now - start) / 1000;
                                if(timeInSeconds>=changeSetupTime){ //After x seconds exit setup page
                                    if(reached==false){
                                        exitSetupPage();
                                        reached=true;
                                    }  
                                }
                            }
                        }
                    }
                    
                }
                
                
                
            }      
        };
        
        // Provision gpio input pins with its internal pull down resistor enabled
        GpioPinDigitalInput[] pins = {
            gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, "left"), //Sezionale
            gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, "right"), //Pozzetto griglia
        };
        
        // Debounce
        pins[0].setDebounce(1000);
        pins[1].setDebounce(1000);
        
        // Create and register gpio pin listener
        gpio.addListener(listener, pins);
    }
    
    
    private int getIntegerFrom2Digit(String stringa){
        String firstLetter = stringa.substring(0, 1);
        int integer;
        if(firstLetter.equals("0")){
            integer = Integer.parseInt(stringa.substring(1, 2));
        }else{
            integer = Integer.parseInt(stringa.substring(0, 2));
        }
        return integer;
    }
    
    private void updateAlarm(int h, int m){
        String h2Digit = String.format("%02d", h);
        String m2Digit = String.format("%02d", m);
        String newAlarm = h2Digit+":"+m2Digit;
        Clock.setAlarm(newAlarm);
        lcd.write(0, 1, newAlarm);
    }
    
    private void checkNotChangedHour(){ //If user does not change the hour of alarm clock time, go to minutes set up page
        Thread tcnch = new Thread() {
            public void run() {
                long start=0;
                long now;
                boolean firstExec = true;
                boolean reached = false;
                String time = Clock.getAlarm();
                while(setAlarmClock==true && setMinuteAlarmClock==false){
                    String newTime = Clock.getAlarm();
                    if(firstExec==true){
                        start = System.currentTimeMillis();
                        firstExec = false;
                    }else{
                        now = System.currentTimeMillis();
                        int timeInSeconds = (int) (now - start) / 1000;
                        if(timeInSeconds>=10 && newTime.equals(time)){ //After x seconds entering in setting minute page
                            if(reached==false){
                                String sveglia = Clock.getAlarm();
                                int h = getIntegerFrom2Digit(sveglia.substring(0, 2));
                                int m = getIntegerFrom2Digit(sveglia.substring(3,5));
                                updateAlarm(h, m);
                                pageMinuteAlarmClock();
                                reached=true;
                            }  
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        log.error(ex);
                    }
                }
            }    
        };
        tcnch.start();
    }
    
    private void checkNotChangedMinutes(){ //If user does not change the minutes of alarm clock time, exit
        Thread tcncm = new Thread() {
            public void run() {
                long start=0;
                long now;
                boolean firstExec = true;
                boolean reached = false;
                String time = Clock.getAlarm();
                while(setAlarmClock==true && setMinuteAlarmClock==true){
                    String newTime = Clock.getAlarm();
                    if(firstExec==true){
                        start = System.currentTimeMillis();
                        firstExec = false;
                    }else{
                        now = System.currentTimeMillis();
                        int timeInSeconds = (int) (now - start) / 1000;
                        if(timeInSeconds>=10 && newTime.equals(time)){ //After x seconds entering in setting minute page
                            if(reached==false){
                                String sveglia = Clock.getAlarm();
                                int h = getIntegerFrom2Digit(sveglia.substring(0, 2));
                                int m = getIntegerFrom2Digit(sveglia.substring(3,5));
                                updateAlarm(h, m);
                                exitSetupPage();
                                reached=true;
                            }  
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        log.error(ex);
                    }
                }
            }
        };
        tcncm.start();
    }
    
    
    private void pageDisableAlarmClock(){
        Clock.pause();
        lcd.clear();
        lcd.write(0, 0, "Sveglia");
        lcd.write(0, 1, "Disattivata");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            log.error(ex);
        }
        lcd.clear();
        Clock.setAlarm("null");
        Clock.resume();
    }
    
    private void pageSetAlarmClock(){
        setAlarmClock=true;
        Clock.pause();
        String sveglia = Clock.getAlarm();
        lcd.clear();
        lcd.write(0, 0, "Imposta ora");
        lcd.write(0, 1, sveglia);
        checkNotChangedHour();
    }
    
    private void pageMinuteAlarmClock(){
        setMinuteAlarmClock=true;
        String sveglia = Clock.getAlarm();
        lcd.clear();
        lcd.write(0, 0, "Imposta minuti");
        lcd.write(0, 1, sveglia);
        checkNotChangedMinutes();
    }
    
    private void exitSetupPage(){
        setAlarmClock=false;
        setMinuteAlarmClock=false;
        
        Leds leds = new Leds();
        
        lcd.clear();
        leds.flash();
        lcd.write(0, 0, "Sveglia impost.");
        String sveglia = Clock.getAlarm();
        lcd.write(0, 1, sveglia);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            log.error(ex);
        }
        lcd.clear();
        Clock.resume();
    }
    
    
    
}
