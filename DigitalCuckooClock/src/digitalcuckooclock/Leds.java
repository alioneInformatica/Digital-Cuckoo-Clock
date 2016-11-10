/*
 * This project is kindly offered under the GNU GPL v3 license. 
 * Please read carefully the terms and conditions of it. 
 * Any action or activity not expressly granted may be liable to prosecution.
 */

package digitalcuckooclock;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/*
 * @author Ali One Informatica
 */

public class Leds {
    /* Get actual class name to be printed on */
    static Logger log = Logger.getLogger(Leds.class.getName());  
    
    public void setLow(){
        GPIO.ledPin.low();
    }
    
    public void setHigh(){
         GPIO.ledPin.high();
    }
    
    public void flash(){
        Thread tflashLeds = new Thread() {
            public void run() {
                try {
                    for(int i=0; i<4; i++){
                        GPIO.ledPin.high();
                        Thread.sleep(100);
                        GPIO.ledPin.low();
                        Thread.sleep(100);
                    }
                } catch (InterruptedException ex) {
                    log.error(ex);
                }
            }
        };
        tflashLeds.start();
    }
    
}
