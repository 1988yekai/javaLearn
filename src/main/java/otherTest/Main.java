package otherTest;

/**
 * Created by yek on 2017-3-28.
 */
public class Main {
    public static void main(String[] args) {
        //获取昨天年月日
//        String yesterday = DateTime.getYesterday("yyyyMMdd");
        String yesterday = "20170324";
        //通过昨天年月日组装数据查询
        String start = "";
        String end = "";
        for (int h = 0; h < 24; h++) {
            String hour = h < 10 ? "0" + String.valueOf(h) : String.valueOf(h);
            String nextHour = (h + 1) < 10 ? "0" + String.valueOf(h + 1) : String.valueOf(h + 1);
//            start = "_" + yesterday + hour + "00_0000000000000000000000000000000000_00_00000_0000_0000-00-00";
//            end = "_" + yesterday + nextHour + "00_ffffffffffffffffffffffffffffffffff_ff_fffff_ffff_9999-99-99";
            start = "_" + yesterday + hour + "00_00000000000000000000000000000000";
            end = "_" + yesterday + nextHour + "00_ffffffffffffffffffffffffffffffff";
            for (int s = 0; s < 60; s++) {
                start = s < 10 ? "0" + s + start : s + start;
                end = s < 10 ? "0" + s + end : s + end;
                //请求参数

                //后处理

                System.out.println(start);
                System.out.println(end);
                start = start.substring(start.indexOf("_"));
                end = end.substring(end.indexOf("_"));
            }
        }
    }
}
