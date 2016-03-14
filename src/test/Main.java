package test;

import top.flyfire.json.JsonPointer;
import top.flyfire.json.resolver.JsonData;
import top.flyfire.json.type.Json;
import top.flyfire.json.type.object.JsonObject;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/3/6.
 */
public class Main {
    public static void main(String[] args){
//        long start  = 0;
//        start = System.currentTimeMillis();
//        Json json = Json.json2Object("{'aadfdf':\"123\",abc:{'aadfdf':123,abc:123},bcd:[123,456]}");
//        Json json2 = Json.json2Object("{'aadfdf':\"123\",abc:{'aadfdf':123,abc:123},bcd:[123,456]}");
//        System.out.println(System.currentTimeMillis()-start);
//        System.out.println(((JsonObject)json).get("aadfdf"));
//        System.out.println(((JsonObject)json).get("abc"));
//        System.out.println(((JsonObject)json).get("bcd"));
//        System.out.println("success!!!");
//        System.out.println(json.equals(json2));
       // Json json = Json.json2Object("[1,3,3,4,5,6]");

       // System.out.println(json);
        testLongJson();
    }


    public static void testLongJson(){
        long start  = 0;
        start = System.currentTimeMillis();
        String a = "{\"a\":{\"a1\":123,\"a2\":{\"a\":\"123\"},\"a3\":true,\"a4\":false,\"a5\":\"2016-03-14T05:33:56.343Z\"},\"b\":{\"b1\":true,\"b2\":[1,\"2\",\"4\",\"134\",7,true,\"2016-03-14T05:34:43.577Z\"],\"b3\":\"adsfdal;fsdfalasdfskdjflaksdjfalsdfjalsdf;asdf103489-1329411111111112303333333222222444444444444444444444444444444444444444444444444444444444444444444444444444fwgdddddddddddddddddddddddddrtwertqrgsdfgsdfg\",\"b4\":{\"a\":\"123123123123123sdfasdfasdfa\",\"b\":\"14519234dlkfasdf我的到了发时间到了发生的是的法拉盛的风景啊是的啊时代发生的发啊时代发生的发\",\"c\":\"ladfsdf90-412=3=1234341234)lkdsflasd\"}},\"c\":[null,\"134123480dkldslkasdflasdflasdfasdfasdfa\",\"2016-03-14T05:36:30.723Z\"],\"c3\":[1,22343,4,5,6]}";
        System.out.println(a.length());
//       System.out.println(a);
        Json json = Json.json2Object(a);
//        System.out.println(System.currentTimeMillis()-start);
        String str = json.toString();
        System.out.println(str);
    }

}
