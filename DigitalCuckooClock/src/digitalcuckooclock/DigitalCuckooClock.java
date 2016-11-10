/*
 * This project is kindly offered under the GNU GPL v3 license. 
 * Please read carefully the terms and conditions of it. 
 * Any action or activity not expressly granted may be liable to prosecution.
 */

package digitalcuckooclock;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.Lcd;
import com.pi4j.wiringpi.SoftPwm;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/*
 * @author Ali One Informatica
 */

public class DigitalCuckooClock {
    
    public final static int LCD_ROWS = 2;
    public final static int LCD_COLUMNS = 16;
    public final static int LCD_BITS = 4;
    
    private static int PIN_NUMBER = 5;
    
    /* Get actual class name to be printed on */
    static Logger log = Logger.getLogger(DigitalCuckooClock.class.getName());

    public static void main(String[] args) throws InterruptedException {
        // Setup logging system
        URL url = DigitalCuckooClock.class.getResource("log4j.properties");
        PropertyConfigurator.configure(url);
        log.info("Programma in esecuzione");
        
        // Initialize and setup GPIO pins
        GPIO g = new GPIO();
        
        // Initialize LCD
        LCD.init(); 
        
        // Start clock
        Clock clk = new Clock();
        clk.start();
        
        // Check if buttons have been pressed
        Buttons cb = new Buttons();
        cb.check();
        
        
        while(true){
            Thread.sleep(200);
        }
        
    }
    
}
