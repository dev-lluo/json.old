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
        JsonData jsonData = new JsonData("{'aadfdf':123,abc:{'aadfdf':123,abc:123}}");
        JsonData.JsonDataPeeker peeker = jsonData.peeker();
        if(JsonPointer.isObject(peeker.peek())){
            JsonObject jsonObject = new JsonObject();
            if(peeker.startObject()) {
                do {
                    jsonObject.set(peeker.readProperty(),peeker.readValue());
                }while(peeker.hasNextObjectElement());
            }
            peeker.endObject();

            System.out.println(jsonObject.get("aadfdf"));
        }
    }
}
