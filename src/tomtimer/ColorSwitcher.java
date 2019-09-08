/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tomtimer;

import java.awt.Color;
import java.awt.SystemTray;
import javax.swing.JPanel;

/**
 *
 * @author awiatech
 */
public class ColorSwitcher implements Runnable
{
    private JPanel panel;
    private mainGui gui; //reference to the main GUI
    private boolean minimize = false; //check if we should reminmize the timer gui
    private boolean stop = false; //stop flashing when user clicks reset or delete
    private Color defaultColor;
    
    int duration = 3000; //milliseconds. how long to keep toggling
    int toggleSpeed = 200; //milliseconds. how long between each toggle
    private boolean toggle = false;
    
    public ColorSwitcher(mainGui g, JPanel jp)
    {
        panel = jp;
        gui = g;
        defaultColor = jp.getBackground();
    }
    
    @Override
    public void run()
    {
        int numToggles = duration / toggleSpeed;
        int ct = 0;
        stop = false;
        
        //Toggle Red/Green
        while(ct < numToggles && stop == false)
        {
            if (toggle == false)
            {
                panel.setBackground(Color.RED);
                toggle = true;
            }
            else
            {
                panel.setBackground(Color.GREEN);
                toggle = false;
            }
            ct++;
            try
            {
                Thread.sleep(toggleSpeed);
            } catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }
        panel.setBackground(Color.GREEN); //last color must be green
        
        //Minimize the gui
        if(minimize == true && stop == false)
        {
            gui.setVisible(false);
            try
            {
                SystemTray.getSystemTray().add(gui.icon);
                gui.isMinimized = true;
            } catch (Exception e1) 
            {
                e1.printStackTrace();
            }
        }
        
        //reset to default color if user clicks Reset/Stop
        if (stop == true)
            panel.setBackground(defaultColor);
        
        gui.repaint(); // gui does not update if computer wakes up from screen off.
    }
    
    public void setMinimize(boolean m)
    {
        minimize = m;
    }
    
    public void stop()
    {
        this.stop = true;
    }
    
}

