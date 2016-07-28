package com.ye.regexTest;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016-6-14.
 */
public class RegexTest {
    public static void main(String[] args) {
        String string1 = "文果 烟台红富士苹果 大果15颗约43.35kg 新鲜水果 栖霞苹果";
        Pattern p = Pattern.compile("[\\d]+[\\.]?[\\d]+kg");
        Matcher m = p.matcher(string1);
        if (m.find())
            System.out.println(m.group());
    }

    public static String strReg(String origin, String reg, String def){
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(origin);
        if (m.find()){
            return m.group();
        } else
            return def;
    }

    @Test
    public void test1(){
        System.out.println(strReg("约4.35kg 新鲜水果 栖霞苹果", "[\\d]+[\\.]?[\\d]+kg", "no"));
    }
}
