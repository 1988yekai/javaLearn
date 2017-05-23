package htmlcleanerTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yek on 2017-4-13.
 */
public class StringUtils {
    /**
     * 正则表达式截取需要的内容
     *
     * @param config
     * @param patternStr
     * @param defaultValue
     * @return
     */
    public static String getConfigValue(String config, String patternStr, String defaultValue) {
        String value = defaultValue;
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(config);
        if (matcher.find()) {
            value = matcher.group(1);
        }
        return value;
    }

    public static String getLastConfigValue(String config, String patternStr, String defaultValue) {
        String value = defaultValue;
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(config);
        while (matcher.find()) {
            value = matcher.group(1);
//            System.out.println(value);
        }
        return value;
    }
}
