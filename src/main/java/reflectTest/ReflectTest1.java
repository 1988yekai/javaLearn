package reflectTest;

import org.junit.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by Administrator on 2016-6-23.
 */
public class ReflectTest1 {
    @Test
    public void test1() {

        int[] a = {1, 2, 3, 4, 5, 6};
        a = (int[]) goodCopyOf(a, 2);
        System.out.println(Arrays.toString(a));
    }

    public Object goodCopyOf(Object a, int newLength) {
        Class c1 = a.getClass();
        if (!c1.isArray()) return null;
        Class conponentType = c1.getComponentType();
        System.out.println("conponentType:" + conponentType);
        int length = Array.getLength(a);
        Object newArray = Array.newInstance(conponentType, length);
        System.arraycopy(a, 0, newArray, 0, Math.min(length, newLength));
        return newArray;
    }

    @Test
    public void test2() throws Exception{
        Method squrMethod = getClass().getMethod("squr",double.class);
        double result = (Double)squrMethod.invoke(null,2);
        System.out.println(result);
    }

    public static double squr(double x) {
        return x * x;
    }
}
