package cloneTest;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2016-6-24.
 */
public class Employee implements Cloneable{
    private String name;
    private Double salary;
    private Date hireDate;

    public Employee() {
    }

    public Employee(String name, Double salary) {
        this.name = name;
        this.salary = salary;
        this.hireDate = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
    public Employee clone() throws CloneNotSupportedException{
        //call object clone;
        Employee clone = (Employee)super.clone();
        //clone mustable fields
        clone.hireDate = (Date) hireDate.clone();
        return clone;
    }
    public void setHireDate(int year, int month, int day) {
        Date newHireDate = new GregorianCalendar(year,month-1,day).getTime();
        this.hireDate.setTime(newHireDate.getTime());
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", salary=" + salary +
                ", hireDate=" + hireDate.toLocaleString() +
                '}';
    }
}
