/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tomtimer;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 *
 * @author Tom
 */
public class SoundPlayer implements Runnable
{
    //global settings
    public static File globalSoundFile = null;
    public static boolean globalLoopOn = false;
    
    //local settings for each SoundPlayer
    private TimerEntry timer = null; // the timer that this sound player was created for
    public File mySoundFile = null;
    Clip clip = null; //used internally
    public boolean loopOn = false; //determine if the alarm sound should loop until user stops it
    public boolean keepPlaying = true; //internal helper when playing the default beep sound
    public boolean testAudioOnce = false; //only plays audio once.

    public SoundPlayer(TimerEntry t)
    {
        timer = t;
    }

    public SoundPlayer(boolean test) //test is used when: user clicks test audio from menu, to play the alarm sound once.
    {
        testAudioOnce = test;
        mySoundFile = globalSoundFile;
    }
    
    private void setSoundRepeatMode(TimerEntry t)
    {
        loopOn = globalLoopOn; //default case
        
        // use timer-specific settings if it is not default.
        if (t.soundRepeatMode == RepeatMode.SINGLE)
            loopOn = false;
        if (t.soundRepeatMode == RepeatMode.REPEAT)
            loopOn = true;
    }

    @Override
    public void run()
    {
        //configure the sound repeat mode
        if (timer != null)
        {
            setSoundRepeatMode(timer);
            mySoundFile = timer.getSoundFile();
            if (mySoundFile == null)
                mySoundFile = globalSoundFile;
            
            //disable sound loop IF timer is set to auto restart upon triggering
            if (loopOn == true && timer.timerRepeatMode == RepeatMode.REPEAT)
                loopOn = false;
        }
        
        try
        {
            if (mySoundFile == null) //play the default alarm sound
            {
                if(loopOn && testAudioOnce == false)
                    keepPlaying = true;
                do {
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    Thread.sleep(2000); //sleep 1 sec
                } while (loopOn && keepPlaying);
                return;
            }
            
            //play a user-specified alarm sound
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(mySoundFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            if (loopOn && testAudioOnce == false)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            
            // at this point, there is a native thread created 'behind the scenes'
            // unless this is added, it never goes away:
            clip.addLineListener(new LineListener()
            {
                @Override
                public void update(LineEvent evt)
                {
                    if (evt.getType() == LineEvent.Type.STOP)
                    {
                        //clip.drain();
                        evt.getLine().close();
                    }
                }
            });
        } catch (Exception ex)
        {
            mySoundFile = null; //reset audio
        }
    }

    public void stop()
    {
        if (clip != null)
        {
            //clip.drain();
            clip.stop();
            clip.close();
        }

        keepPlaying = false;
    }
}//end class SoundPlayer
