package test;

import top.flyfire.json.JsonPointer;
import top.flyfire.json.resolver.JsonData;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/3/6.
 */
public class Main {
    public static void main(String[] args){
        JsonData jsonData = new JsonData("{a:'123'}");
        JsonData.JsonDataPeeker peeker = jsonData.peeker();
        if(JsonPointer.isObject(peeker.peek())){
            peeker.startObject();
            peeker.endObject();
        }
    }
}