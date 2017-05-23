package unicodeTest;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yek on 2017-4-18.
 */
public class UnicodeTest1 {

        /**
         * 解码 Unicode \\uXXXX
         * @param str
         * @return
         */
        public static String decodeUnicode(String str) {
            Charset set = Charset.forName("UTF-16");
            Pattern p = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
            Matcher m = p.matcher( str );
            int start = 0 ;
            int start2 = 0 ;
            StringBuffer sb = new StringBuffer();
            while( m.find( start ) ) {
                start2 = m.start() ;
                if( start2 > start ){
                    String seg = str.substring(start, start2) ;
                    sb.append( seg );
                }
                String code = m.group( 1 );
                int i = Integer.valueOf( code , 16 );
                byte[] bb = new byte[ 4 ] ;
                bb[ 0 ] = (byte) ((i >> 8) & 0xFF );
                bb[ 1 ] = (byte) ( i & 0xFF ) ;
                ByteBuffer b = ByteBuffer.wrap(bb);
                sb.append( String.valueOf( set.decode(b) ).trim() );
                start = m.end() ;
            }
            start2 = str.length() ;
            if( start2 > start ){
                String seg = str.substring(start, start2) ;
                sb.append( seg );
            }
            return sb.toString() ;
        }

    /**
     * 解码 Unicode &#20854;
     * @param str
     * @return
     */
    public static String decodeUnicode2(String str) {
        Charset set = Charset.forName("UTF-16");
        Pattern p = Pattern.compile("&#([\\d]+);");
        Matcher m = p.matcher( str );
        int start = 0 ;
        int start2 = 0 ;
        StringBuffer sb = new StringBuffer();
        while( m.find( start ) ) {
            start2 = m.start() ;
            if( start2 > start ){
                String seg = str.substring(start, start2) ;
                sb.append( seg );
            }
            String code = m.group( 1 );
            int i = Integer.valueOf( code , 10 );
            byte[] bb = new byte[ 4 ] ;
            bb[ 0 ] = (byte) ((i >> 8) & 0xFF );
            bb[ 1 ] = (byte) ( i & 0xFF ) ;
            ByteBuffer b = ByteBuffer.wrap(bb);
            sb.append( String.valueOf( set.decode(b) ).trim() );
            start = m.end() ;
        }
        start2 = str.length() ;
        if( start2 > start ){
            String seg = str.substring(start, start2) ;
            sb.append( seg );
        }
        return sb.toString() ;
    }

    public static void main(String[] args) {
        String temp = "&#20854;1&#20182;&#19987;我是&#19994;aab&#25216;&#26415;&#26381;&#21153;1aabb11";
        System.out.println(decodeUnicode2(temp));
    }
}
