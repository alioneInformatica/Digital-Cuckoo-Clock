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

/*
 * @author Ali One Informatica
 */

public class GPIO {
    
    public static final GpioController gpio = GpioFactory.getInstance();
    
    public static final GpioPinDigitalOutput ledPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "led", PinState.LOW);
    
    public static final GpioPinDigitalOutput amplifierPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "amplifier", PinState.HIGH);
    
    public static final GpioPinDigitalOutput servoPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "servo", PinState.HIGH);
}
