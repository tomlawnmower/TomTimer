/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tomtimer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Tom
 */
public class TimerEntry implements java.io.Serializable
{
    //General timer properties
    int id; //unique iD, do not need to save to file
    TimerType timerType = TimerType.NULL; //will either be ALARM or COUNTDOWN
    boolean isActive = true; //true when the timer is running and will trigger eventually.
    Calendar triggerTime; // reference to exactly when the timer will fire. Used to fire timer if application is closed/reopened, and the trigger time has already passed.
    String triggerTimeStr; // the exact time the timer will fire, displayed on the GUI. This is a text version of the Calendar triggerTime.
    String note; // the note associated with Timer, displayed on the GUI
    
    //timer / sound repeat
    RepeatMode timerRepeatMode = RepeatMode.DEFAULT;
    RepeatMode soundRepeatMode = RepeatMode.DEFAULT;
    File soundFile = null; //the user specified alarm sound for this timer.
    
    //Alarm timer properties
    int alarmHours = 0; //the hour of the day, 0-23, (but user only inputs 1-12)
    int alarmMinutes = 0; //the minute of the day, 0 - 59
    
    //Countdown timer properties
    int hours = 0;
    int minutes = 0;
    int seconds = 0;
    
    //Timer data structures
    Timer myTimer; //the timer
    TimerTask myTimerTask; //the task that the timer will call run the timer fires

    //GUI elements
    JPanel panel; //the JPanel where all timer's GUI's elements are put into
    JLabel myLabel; //used to display text on the gui
    JButton buttonDelete; // delete timer button
    JButton buttonStartReset; //start/reset timer button
    Color defaultColor; //the default color, specified by the system
    mainGui gui; //reference to the main GUI
    ColorSwitcher colorSwitcher;
    
    // Constructor for Countdown Timer
    public TimerEntry(mainGui g, int id, int h, int m, int s, String note)
    {
        this.id = id;
        hours = h;
        minutes = m;
        seconds = s;
        this.note = note;
        gui = g;
        this.timerType = TimerType.COUNTDOWN;
        GUIConstruction();
    }

    // Constructor for Alarm Timer
    public TimerEntry(mainGui g, int id, int alarmH, int alarmM, String note)
    {
        this.id = id;
       this.alarmHours = alarmH;
       this.alarmMinutes = alarmM;
       this.note = note;
        gui = g;
        this.timerType = TimerType.ALARM;
        GUIConstruction();
    }
    
    public void startTimer()
    {
        long delay = 0;
        
        if (timerType == TimerType.COUNTDOWN)
        {
            delay = hours * 60 * 60 * 1000;
            delay = delay + minutes * 60 * 1000;
            delay = delay + seconds * 1000; //delay used for the Java timer
         
            //calculate the trigger time of the timer as Calendar object
            triggerTime = Calendar.getInstance();
            //to delete? triggerTime = (Calendar) triggerTime.clone();
            triggerTime.add(Calendar.HOUR_OF_DAY, hours);
            triggerTime.add(Calendar.MINUTE, minutes);
            triggerTime.add(Calendar.SECOND, seconds);
        }
        
        if (timerType == TimerType.ALARM)
        {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2;
            cal2 = (Calendar) cal1.clone();
            cal2.set(Calendar.HOUR_OF_DAY, alarmHours);
            cal2.set(Calendar.MINUTE, alarmMinutes);
            if (cal1.before(cal2) == false) //if the set time is in the past, compared to the current time, since alarms are all in the future
                cal2.add(Calendar.DATE, 1); //add 1 day (24 hrs)
            delay = cal2.getTimeInMillis() - cal1.getTimeInMillis(); //delay used for Java timer
           
           triggerTime = (Calendar) cal2.clone();
        }
        
        //create and start the timer
        myTimer = new Timer();
        myTimerTask = new MyAlarmTask(this);
        myTimer.schedule(myTimerTask, delay);
        panel.setBackground(Color.GREEN);
        triggerTimeStr = calculateTriggerTimeString();
        myLabel.setText(triggerTimeStr + note);
        this.isActive = true; //mark timer as started
        gui.saveData();
    }

    //add action listeners to buttons and the timer panel
    private void addButtonActions()
    {
        buttonDelete.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (myTimer != null)
                {
                    myTimer.cancel();
                    ((MyAlarmTask) myTimerTask).stop();
                }
                gui.removeTimerEntry(getId());
                gui.saveData();
            }
        });

        buttonStartReset.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (isActive == true)
                {
                    panel.setBackground(defaultColor);
                    ((MyAlarmTask) myTimerTask).stop();
                    myTimer.cancel();
                    isActive = false;
                    colorSwitcher.stop();
                    buttonStartReset.setText("Start");
                    gui.saveData();
                }
                else
                {
                    panel.setBackground(Color.GREEN);
                    startTimer();
                    buttonStartReset.setText("Reset");
                    isActive = true;
                }
            }
        });

        //mouse listener to bring up settings (right click on any non-button of timer panel opens settings)
        panel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e) {
                int button = e.getButton();
                if (button == MouseEvent.BUTTON3) //only activate for right click
                    gui.editTimerSettings(id);
            }
        });

    } //end constructor


    // return the JPanel of this timer, used to add timer entry to main GUI 
    public JPanel getJPanel()
    {
        return panel;
    }
    
    // actions to perform when the timer triggers
    public void timerFired()
    {
        boolean isMinimized = false; //remember if window was minimized for auto-repeating timers
        panel.setBackground(Color.RED);
        gui.setState(java.awt.Frame.NORMAL);
        if (gui.isMinimized) //if the timer is minimized to system tray
        {
            isMinimized = true;
            gui.restoreWindow();
        }
        gui.bringToFront();
        
        
        //check if need to auto restart the timer
        if (timerRepeatMode == RepeatMode.SINGLE)
        {
            gui.saveData();
            return; //don't auto restart
        }
            
        if (timerRepeatMode == RepeatMode.DEFAULT
                && gui.globalTimerAutoRestartMode == false) 
        {
            gui.saveData();
            return; //don't auto restart
        }
        
        //otherwise automatically restart the timer:
        //((MyAlarmTask) myTimerTask).stop(); //dont force stop, just let thread play audio until it stops and terminates. guaranteed to not loop audio here.
        myTimer.cancel();
        startTimer();
        colorSwitcher.setMinimize(isMinimized);
        (new Thread(colorSwitcher)).start(); //flash between red and green for a specific length of time.
    }

    //calculate the time that the timer will fire, in AM/PM format
    // using the triggerTime Calendar object.
    private String calculateTriggerTimeString()
    {
        String output, temp;
        int hr = triggerTime.get(Calendar.HOUR); //get it in 1-12 am/pm format
        int min = triggerTime.get(Calendar.MINUTE);
        int am_pm = triggerTime.get(Calendar.AM_PM);
        if (hr == 0) //noon and midnight are represented by 0, not 12.
            hr = 12;

        temp = Integer.toString(min); //pad minutes to 2 digits
        if (temp.length() == 1)
            temp = "0" + temp;
        output = Integer.toString(hr) + ":" + temp;
        
        if (am_pm == Calendar.AM)
            output = output + "am. ";
        else
            output = output + "pm. ";

        return output;
    }

    //GET and SET methods
    
    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setGui(mainGui g)
    {
        gui = g;
    }

    public int getHours()
    {
        return hours;
    }

    public int getMinutes()
    {
        return minutes;
    }

    public int getSeconds()
    {
        return seconds;
    }

    public String getNote()
    {
        return note;
    }
    
    public void setNote(String newNote)
    {
        note = newNote;
        myLabel.setText(triggerTimeStr + note);
    }
    
    public File getSoundFile()
    {
        return soundFile;
    }
    
    public void setSoundFile(File f)
    {
        soundFile = f;
    }

    public  void GUIConstruction()
    {
        FlowLayout myLayout = new FlowLayout(FlowLayout.LEFT);
        myLayout.setHgap(2);
        myLayout.setVgap(3);
        panel = new JPanel(myLayout);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonDelete = new JButton("X");

        buttonDelete.setMargin(new Insets(0,0,0,0));
        panel.add(buttonDelete);
        
        colorSwitcher = new ColorSwitcher(gui, panel);
        
        String textStr = ""; //display the user inputted time (either alarm or countdown)
        if (timerType == TimerType.ALARM)
        {
            boolean isAM = true;
            int tempH = alarmHours;
            String tempStr;
            
            if (tempH > 12) //convert hours to AM/PM mode
            {
                isAM = false;
                tempH = tempH - 12;
            }
            
            if (tempH == 12)
                isAM = false; //but don't subtract 12
            
            if (tempH == 0)
                tempH = 12;

            //pad minutes to 2 digits if necessary
            tempStr = Integer.toString(alarmMinutes); 
            if (tempStr.length() == 1)
                tempStr = "0" + tempStr;
            
            //create the string representation of the time, add am/pm
            textStr = textStr + tempH + ":" + tempStr;
            if(isAM)
                textStr = textStr + "am";
            else
                textStr = textStr + "pm"; 
        }
        
        if (timerType == TimerType.COUNTDOWN)
        {
            if (hours != 0)
                textStr = textStr + hours + "h";
            if (minutes != 0)
            {
                if (hours != 0)
                    textStr = textStr + " ";
                textStr = textStr + minutes + "m";
            }
            if (seconds != 0)
            {
                if (hours != 0 || minutes != 0)
                    textStr = textStr + " ";
                textStr = textStr + seconds + "s";
            }
            //textStr = hours +"h "+ minutes +"m " + seconds + "s";
        }
        
        panel.add(new JLabel(textStr));
        if (isActive == true)
            buttonStartReset = new JButton("Reset");
        else
            buttonStartReset = new JButton("Start");
        buttonStartReset.setMargin(new Insets(0,0,0,0));
        panel.add(buttonStartReset);

        myLabel = new JLabel(note);
        panel.add(myLabel);

        double width = panel.getMaximumSize().getWidth();
        double height = panel.getPreferredSize().getHeight();
        panel.setMaximumSize(new Dimension((int) width, (int) height));

        defaultColor = panel.getBackground();
        addButtonActions(); //add action listeners to buttons.
    }
    
    public void checkIfNeedToResumeTimer()
    {
        if (isActive == false)
            return; //timer was not active, no need to resume
        //else, resume timer
        
        //calculate if we should trigger it now, or set a new timer according to trigger time
        Calendar currentTime = Calendar.getInstance();
        long delay = triggerTime.getTimeInMillis() - currentTime.getTimeInMillis(); //delay used for Java timer
        if (delay < 0)
            delay = 0; //instantly trigger the timer b/c it has already triggered in the past.
        
        //create and start the timer
        myTimer = new Timer();
        myTimerTask = new MyAlarmTask(this);
        myTimer.schedule(myTimerTask, delay);
        panel.setBackground(Color.GREEN);
        triggerTimeStr = calculateTriggerTimeString();
        myLabel.setText(triggerTimeStr + note);
        this.isActive = true; //mark timer as started
    }
    
    
    
    private void writeObject(ObjectOutputStream o)
            throws IOException
    {
        o.writeObject(timerType);
        o.writeObject(isActive);
        o.writeObject(triggerTime);
        o.writeObject(note);
        
        o.writeObject(timerRepeatMode);
        o.writeObject(soundRepeatMode);
        o.writeObject(soundFile);
        
        o.writeObject(alarmHours);
        o.writeObject(alarmMinutes);

        o.writeObject(hours);
        o.writeObject(minutes);
        o.writeObject(seconds);
        
    }

    private void readObject(ObjectInputStream o)
            throws IOException, ClassNotFoundException
    {
        timerType = (TimerType) o.readObject();
        isActive = (Boolean) o.readObject();
        triggerTime = (Calendar) o.readObject();
        note = (String) o.readObject();
        
        timerRepeatMode = (RepeatMode) o.readObject();
        soundRepeatMode = (RepeatMode) o.readObject();
        soundFile = (File) o.readObject();
        
        alarmHours = (Integer) o.readObject();
        alarmMinutes = (Integer) o.readObject();
        
        hours = (Integer) o.readObject();
        minutes = (Integer) o.readObject();
        seconds = (Integer) o.readObject();
    }

}
