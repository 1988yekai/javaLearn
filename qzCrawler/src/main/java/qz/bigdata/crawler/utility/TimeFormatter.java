package qz.bigdata.crawler.utility;

/**
 * Created by fys on 2015/1/15.
 */
import com.ibm.icu.text.SimpleDateFormat;
import org.apache.log4j.Logger;

public class TimeFormatter {

    private static final Logger logger = Logger.getLogger(TimeFormatter.class);
    private static final SimpleDateFormat formate_ymd=new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat formate_year=new SimpleDateFormat("yyyy");
    public static String formateRawTime(String rawTime){

        if(rawTime == null){
            return rawTime;
        }

        if(rawTime.length() == 19 && rawTime.matches("\\d{4}-\\d\\d-\\d\\d\\s\\d\\d:\\d\\d:\\d\\d")){
            return rawTime;
        }

        String formatedTime = null;
        String [] arr = rawTime.split("\\s");

        if(arr.length == 2){
            formatedTime = copeWithYear(arr[0])+" "+copeWithTime(arr[1]);
        }else if(arr.length == 1){

            if(rawTime.contains("-")){
                formatedTime = copeWithYear(rawTime)+" "+"00:00:00";
            }else if(rawTime.contains(":")){

                String year=formate_ymd.format(new java.util.Date());
                formatedTime = year+" "+copeWithTime(rawTime);
            }else{
                logger.warn("time formate is wrong");
            }
        }else{
            logger.warn("time formate is wrong");
        }

        return formatedTime;
    }

    private static String copeWithYear(String year){
        String formatedYear = null;
        String [] ymd = year.split("-");
        switch(ymd.length){

            case 2:
                formatedYear = copeWithMD(ymd,0,1);
                break;

            case 3:
                formatedYear = copeWithYMD(ymd,0,1,2);
                break;

            default:
                logger.warn("rawtime year formate is wrong");
                break;
        }
        return formatedYear;
    }

    private static String copeWithMD(String [] md,int m,int d){

        String mdStr = addToMD(md,m,d);

        String year=formate_year.format(new java.util.Date());

        return year+"-"+mdStr;
    }

    private static String addToMD(String []md,int m,int d){

        if(md[m].length() == 1){
            md[m] = "0"+md[m];
        }

        if(md[d].length() == 1){
            md[d] = "0"+md[d];
        }

        return md[m]+"-"+md[d];
    }

    private static String copeWithYMD(String [] ymd,int y,int m,int d){
        if(ymd[y].length() == 2){
            ymd[y] = "20"+ymd[y];
        }

        String md = addToMD(ymd,m,d);

        return ymd[y]+"-"+md;
    }

    private static String copeWithTime(String time) {

        String[] hms = time.split(":");
        String formatedTime = null;
        switch (hms.length) {
            case 2:
                formatedTime = copeWithHM(hms, 0, 1);
                break;
            case 3:
                formatedTime = copeWithHMS(hms, 0, 1, 2);
                break;

            default:
                logger.warn("rawtime time formate is wrong");
                break;
        }

        return formatedTime;
    }

    private static String copeWithHM(String [] hm,int h,int m){

        String hmStr = addToHM(hm,h,m);

        return hmStr+":00";
    }

    private static String addToHM(String[] hm, int h, int m) {
        if (hm[h].length() == 1) {
            hm[h] = "0" + hm[h];
        }

        if (hm[m].length() == 1) {
            hm[m] = "0" + hm[m];
        }

        return hm[h] + ":" + hm[m];
    }

    private static String copeWithHMS(String []hms,int h,int m,int s){

        String hmStr = addToHM(hms,h,m);
        if(hms[s].length() == 1){
            hms[s] = "0"+hms[s];
        }

        return hmStr+":"+hms[s];
    }
}

