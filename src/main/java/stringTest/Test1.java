package stringTest;

/**
 * Created by yek on 2017-2-28.
 */
public class Test1 {
    public static void main(String[] args) {
        String date = "关于北京西安举办工程债权融资、索赔与追讨拖欠工程款项目合...  [2014-11-07]";
        String result = StringUtils.getConfigValue(date,"\\[([\\d]{4}-[\\d]{1,2}-[\\d]{1,2})\\]","");
        System.out.println(result);
    }
}
