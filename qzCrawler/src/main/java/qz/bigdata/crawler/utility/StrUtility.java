package qz.bigdata.crawler.utility;

/**
 * Created by fys on 2015/1/15.
 */
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class StrUtility {

    private static Logger m_debug = Logger.getLogger("Debuglogger");

    /**
     * ��content��ȡbegin֮��end֮ǰ���ַ�
     * ע�⣬���beginû���֣��᷵��null, ���endû������һֱ���ַ�ĩβ
     * @param content
     * @param begin           ���Ϊnull����ӵ�0���ַ�ʼ��������0���ַ�)
     * @param end             ���Ϊnull����һֱ��ȡ���ַ��β
     * @return
     */
    public static String GetSubString(String content, String begin, String end) {
        if( content == null ) {
            return null;
        }

        int startIndex;
        if( begin == null) {
            startIndex = 0;
        }
        else {
            startIndex = content.indexOf(begin);
            if( startIndex == -1 ) return null;
            else                   startIndex +=begin.length();
        }

        if( end != null ) {
            int endIndex = content.indexOf(end, startIndex);
            if( endIndex == -1 ) return content.substring(startIndex);
            else                 return content.substring(startIndex, endIndex);
        }
        else {
            return content.substring(startIndex);
        }
    }

    //������ҳ
    public static boolean SavePage(byte[] bytes, String content, String savePath) {
        String name = StrUtility.GetSubString(content, "<title>", "</title>");	//��ȡ��������Ϊ����ʱ���ļ���
        if (name != null)
            name = name.trim() + ".html";
        else
            return false;

        name = FixFileName(name);
        try {
            FileOutputStream fos = new FileOutputStream(new File(savePath, name));
            fos.write(bytes);
            fos.close();
        }
        catch(FileNotFoundException e) {
            m_debug.debug("�޷������ļ���Ϊ\"" + name + "\"���ļ�");
            return false;
        } catch (IOException e) {
            m_debug.debug(e.getMessage());
            return false;
        }

        return true;
    }

    //ȥ���ļ����еķǷ��ַ�
    public static String FixFileName(String name) {
        String res = "";
        for(char c : name.toCharArray()) {
            if( "/\\:*?\"<>|".contains("" + c) ) {
                res += " ";
            } else {
                res += c;
            }
        }
        return res;
    }

    public static String ToStandardTime(int second) {
        if( second < 60 ) return second + "s";

        int min = second / 60;
        second %= 60;
        if( min < 60 ) return min + "m" + second + "s";

        int hour = min / 60;
        min %= 60;
        return hour + "h" + min + "m" + second + "s";
    }
}

