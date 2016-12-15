package bean;

import java.awt.*;

/**
 * Created by yek on 2016-8-18.
 */
public class HelloWorld {
    public HelloWorld() {
        System.out.println("This is HolloWorld constructor!");
    }

    private String name;

    public void setName(String name) {
        this.name = name;
        System.out.println("This is setter method!");
    }

    public void sayHello() {
        System.out.println("Name is " + this.name);
    }

    //test
    public static void main(String[] args) {
        System.out.println("beep");
        Toolkit.getDefaultToolkit().beep();
    }
}
