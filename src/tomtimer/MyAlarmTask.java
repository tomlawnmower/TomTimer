package tomtimer;

import java.util.TimerTask;

/**
 *
 * @author Tom
 */

class MyAlarmTask extends TimerTask
{
    TimerEntry timer;
    SoundPlayer soundPlayer;

    public MyAlarmTask(TimerEntry timer)
    {
        super();
        this.timer = timer;
        soundPlayer = new SoundPlayer(timer);
    }

    @Override
    public void run() //this is called when the timer fires
    {
        (new Thread(soundPlayer)).start(); //start playing the alarm sound
        timer.timerFired(); //trigger the timer fired
    }

    public void stop()
    {
        soundPlayer.stop();
    }
}//end class