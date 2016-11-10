/*
 * This project is kindly offered under the GNU GPL v3 license. 
 * Please read carefully the terms and conditions of it. 
 * Any action or activity not expressly granted may be liable to prosecution.
 */

package digitalcuckooclock;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.apache.log4j.Logger;

/*
 * @author Ali One Informatica
 */

public class Sound {
    /* Get actual class name to be printed on */
    static Logger log = Logger.getLogger(Sound.class.getName());
    
    AudioAmplifier audioAmplifier = new AudioAmplifier();
    Leds leds = new Leds();
    ServoUccellino servoBird = new ServoUccellino();
    
    public synchronized void playSound(){
        
        new Thread(new Runnable() {
        public void run() {
            File cuckooSoundFile = new File("/home/pi/Desktop/programma/Cuckoo2.wav");
            
            try {
                // Cuckoo bird in final position
                servoBird.setLow();
                
                // Turn on audio amplifier
                audioAmplifier.setLow();
                
                // Turn on leds
                leds.setHigh();
                
                Thread.sleep(40);
                
                // Play cuckoo sound
                AudioInputStream cuckooAudioInputStream = AudioSystem.getAudioInputStream(cuckooSoundFile);
                Clip cuckooSoundClip = AudioSystem.getClip();
                
                cuckooSoundClip.open(cuckooAudioInputStream);
                cuckooSoundClip.start();
                
                while (!cuckooSoundClip.isRunning()){
                    Thread.sleep(10);
                }
                while (cuckooSoundClip.isRunning()){
                    Thread.sleep(10);
                }
                
                // Stop cuckoo sound
                cuckooSoundClip.close();
                
                // Cuckoo bird in start position
                servoBird.setHigh();
                
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException | InterruptedException ex) {
                log.error(ex);
            }
            
            // Turn off leds
            leds.setLow();
            
            // Turn off audio amplifier
            audioAmplifier.setHigh();
        }
        }).start();
            
            
    }
    
}
