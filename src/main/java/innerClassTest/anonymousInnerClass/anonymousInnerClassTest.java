package innerClassTest.anonymousInnerClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class AnonymousInnerClassTest {
    public static void main(String[] args) {
        TalkingClock1 clock1 = new TalkingClock1();
        clock1.start(6000,true);
        JOptionPane.showMessageDialog(null,"Quit program?");
    }
}

class TalkingClock1 {
    public void start(int interval, final boolean beep) {
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Date now = new Date();
                System.out.println("At the tone, the time is " + now);
                if (beep) {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        };
        Timer t = new Timer(interval,listener);
        t.start();
    }
}
