/*
 * This project is kindly offered under the GNU GPL v3 license. 
 * Please read carefully the terms and conditions of it. 
 * Any action or activity not expressly granted may be liable to prosecution.
 */

package digitalcuckooclock;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.pi4j.component.servo.ServoDriver;
import com.pi4j.component.servo.ServoProvider;
import com.pi4j.component.servo.impl.RPIServoBlasterProvider;
import java.io.IOException;

/*
 * @author Ali One Informatica
 */

public class ServoUccellino {
    
    /* Get actual class name to be printed on */
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ServoUccellino.class.getName());
     
    public void setLow(){
        GPIO.servoPin.low();
    }
     
    public void setHigh(){
        GPIO.servoPin.high();
    }
    
}
