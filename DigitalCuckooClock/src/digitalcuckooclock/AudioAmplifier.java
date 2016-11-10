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
import org.apache.log4j.Logger;

/*
 * @author Ali One Informatica
 */

public class AudioAmplifier {
    /* Get actual class name to be printed on */
    static Logger log = Logger.getLogger(AudioAmplifier.class.getName());
    
    
    public void setLow(){
        GPIO.amplifierPin.low();
    }
     
    public void setHigh(){
        GPIO.amplifierPin.high();
    }
    
}
