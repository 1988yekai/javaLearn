package interfaceTest;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.*;

/**
 * Created by Administrator on 2016-6-24.
 */
public class TimerTest {
    public static void main(String[] args) {
        ActionListener listener = new TimePrinter();

        Timer timer = new Timer(10000,listener);
        timer.start();

        JOptionPane.showMessageDialog(null,"quit program?");
        System.exit(0);
    }

}

class TimePrinter implements ActionListener{
    public void actionPerformed(ActionEvent e) {
        Date now = new Date();
        System.out.println("At the tone, the time is" + now);
        Toolkit.getDefaultToolkit().beep();
    }
}
