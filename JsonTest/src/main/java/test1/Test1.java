package test1;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Administrator on 2016-6-20.
 */
public class Test1 {
    public static void main(String[] args) {
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name","good");
        jsonObject1.put("age","12");
        System.out.println(jsonObject1);
        System.out.println(jsonObject1.get("age"));
        System.out.println("----------------------------------");
        Person person1 = new Person("zhangsan",11);
        JSONObject jsonObject2 = JSONObject.fromObject(person1);
        System.out.println(jsonObject2);
        System.out.println(jsonObject2.getInt("age"));
        System.out.println("----------------------------------");
        JSONArray jsonArray1 = new JSONArray();
        jsonArray1.add(new Person("lisi",3));
        jsonArray1.add(new Person("wangwu",4));
        System.out.println(jsonArray1.toString());
        System.out.println("----------------------------------");
        List list1 = new LinkedList();
        list1.add(new Person("lisi",77));
        list1.add(new Person("wangwu",66));
        jsonArray1 = JSONArray.fromObject(list1);
        System.out.println(jsonArray1.toString());

        System.out.println("----------------------------------");
        ListIterator iterator = jsonArray1.listIterator();
        while (iterator.hasNext()){
            System.out.println(((JSONObject)iterator.next()).get("age"));
        }

    }

    @Test
    public void test1(){
        String string1 = "({\"badgeDiscount\":0,\"badgeDiscountRate\":100,\"canSale\":1,\"canShow\":1,\"channelId\":1,\"channelIdList\":\"[1]\",\"curPriceType\":2,\"currentAreaIsSale\":1,\"currentMerchantId\":134362,\"currentPrice\":98,\"currentPriceType\":2,\"currentPriceWithoutBadge\":98,\"currentStockNum\":1000,\"currentTime\":1467192497856,\"importTaxAmt\":0.00,\"isBindPackage\":0,\"isLockPrice\":0,\"isQueuing\":0,\"isSensitiveProduct\":0,\"localStock\":1000,\"lockPrice\":98,\"marketPrice\":158,\"merchantId\":134362,\"mutexPromotion\":0,\"normalStock\":1000,\"payNum\":0,\"pmId\":40715023,\"priceChangeRemind\":0,\"productId\":34891239,\"productPrice\":98,\"productStock\":1000,\"productType\":0,\"promPrice\":98,\"promoteType\":1,\"remainTime\":2097101144,\"ruleId\":364683086,\"ruleType\":0,\"s\":\"ed3d2\",\"saleType\":4,\"samMemberPrice\":\"null\",\"shareStockNum\":0,\"shoppingcount\":0,\"soldNum\":0,\"subPmInfoPrice\":98,\"taxAmt\":0E-8,\"taxFreeAmt\":50,\"totalLimitNum\":0,\"userLimitNum\":0,\"userPriceLimitNumber\":0,\"vs\":0,\"warningStock\":0,\"yhdImportTaxAmt\":0.00,\"yhdPrice\":158})";
        String string2 = string1.substring(1,string1.length()-1);
        JSONObject json1 = JSONObject.fromObject(string2);
        System.out.println(json1.get("productPrice"));
    }

}
