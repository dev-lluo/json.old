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
        long start  = 0;
        start = System.currentTimeMillis();
        Json json = Json.json2Object("{'aadfdf':\"123\",abc:{'aadfdf':123,abc:123},bcd:[123,456]}");
        System.out.println(System.currentTimeMillis()-start);
        System.out.println(((JsonObject)json).get("aadfdf"));
        System.out.println(((JsonObject)json).get("abc"));
        System.out.println(((JsonObject)json).get("bcd"));
        System.out.println("success!!!");
    }
}
