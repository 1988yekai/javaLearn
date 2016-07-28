package cloneTest;

/**
 * Created by Administrator on 2016-6-24.
 */
public class Main {

    public static void main(String[] args) {
        try {
            Employee employee1 = new Employee("Mary",1000.0);
            employee1.setHireDate(2014,3,21);
            Employee employee2 = employee1.clone();
            employee2.setHireDate(2015,4,3);
            System.out.println("employee1:" + employee1);
            System.out.println("employee2:" + employee2);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

}
