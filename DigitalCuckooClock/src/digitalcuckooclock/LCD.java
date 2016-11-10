/*
 * This project is kindly offered under the GNU GPL v3 license. 
 * Please read carefully the terms and conditions of it. 
 * Any action or activity not expressly granted may be liable to prosecution.
 */

package digitalcuckooclock;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.Lcd;
import org.apache.log4j.Logger;

/*
 * @author Ali One Informatica
 */

public class LCD {
    
    /* Get actual class name to be printed on */
    static Logger log = Logger.getLogger(LCD.class.getName());
    
    static private boolean initialized = false;
    private final static int LCD_ROWS = 2;
    private final static int LCD_COLUMNS = 16;
    private final static int LCD_BITS = 4;
    
    private static int lcdHandle;
    
    public static void init(){
        // setup wiringPi
        if (Gpio.wiringPiSetup() == -1) {
            return;
        }
        
         // initialize LCD
        lcdHandle= Lcd.lcdInit(LCD_ROWS,     // number of row supported by LCD
                               LCD_COLUMNS,  // number of columns supported by LCD
                               LCD_BITS,     // number of bits used to communicate to LCD
                               11,           // LCD RS pin
                               10,           // LCD strobe pin
                               0,            // LCD data bit 1
                               1,            // LCD data bit 2
                               2,            // LCD data bit 3
                               3,            // LCD data bit 4
                               0,            // LCD data bit 5 (set to 0 if using 4 bit communication)
                               0,            // LCD data bit 6 (set to 0 if using 4 bit communication)
                               0,            // LCD data bit 7 (set to 0 if using 4 bit communication)
                               0);           // LCD data bit 8 (set to 0 if using 4 bit communication)
        
        // verify initialization
        if (lcdHandle == -1) {
            return;
        }
        
        initialized = true;
    }
    
    public void write(int column, int row, String text){ //Column starting at 0 (left), row starting at 0 (top)
        if(initialized==true){
            Lcd.lcdHome(lcdHandle);
            Lcd.lcdPosition(lcdHandle, column, row); //Cursor on first line
            Lcd.lcdCursor(lcdHandle, 0);
            Lcd.lcdPuts (lcdHandle, text) ;
        }
    }
    
    public void clear(){
        if(initialized==true){
            try {
                // clear LCD
                Lcd.lcdClear(lcdHandle);
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                log.error(ex);
            }
        }
    }
    
}
