package mapQuchongTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yek on 2016-7-26.
 */
public class MapTest {
    public static void main(String[] args) {
        Map<PageInfo, PageInfo> map = new HashMap();
        List<PageInfo> delKeys = new ArrayList<PageInfo>();

        PageInfo pageInfo1 = new PageInfo("www.good1.com","小朋友");
        map.put(pageInfo1,pageInfo1);
        PageInfo pageInfo2 = new PageInfo("www.good1.com","小朋友");
        map.put(pageInfo2,pageInfo2);
        PageInfo pageInfo3 = new PageInfo("www.good1.com","小朋友");
        map.put(pageInfo3,pageInfo3);
        PageInfo pageInfo4 = new PageInfo("www.good2.com","小朋友2");
        map.put(pageInfo4,pageInfo4);
        PageInfo pageInfo5 = new PageInfo("www.good5.com","小朋友5");
        map.put(pageInfo5,pageInfo5);
        PageInfo pageInfo6 = new PageInfo("www.good5.com","小朋友5");
        map.put(pageInfo6,pageInfo6);
        PageInfo pageInfo7 = new PageInfo("www.good2.com","小朋友2");
        map.put(pageInfo7,pageInfo7);
        PageInfo pageInfo8 = new PageInfo("www.good8.com","小朋友8");
        map.put(pageInfo8,pageInfo8);

        Map<PageInfo, PageInfo> map1 = new HashMap();
        map1.putAll(map);

        for (Map.Entry tem : map.entrySet()){
            PageInfo key = (PageInfo)tem.getKey();
            PageInfo value = (PageInfo) tem.getValue();

            map1.remove(key);
            for (Map.Entry tem1 : map1.entrySet()){
                PageInfo key1 = (PageInfo)tem1.getKey();
                if(key.getTitle().equals(key1.getTitle())){
                    if (!delKeys.contains(key1))
                        delKeys.add(key1);
                }
            }
        }

        for (PageInfo delKey :delKeys){
            map.remove(delKey);
        }

        System.out.println(map);


    }
}
