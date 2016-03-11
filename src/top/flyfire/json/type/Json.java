package top.flyfire.json.type;

import top.flyfire.json.JsonPointer;
import top.flyfire.json.resolver.JsonData;
import top.flyfire.json.type.array.JsonArray;
import top.flyfire.json.type.object.JsonObject;
import top.flyfire.json.type.primitive.JsonPrimitive;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/3/6.
 */
public interface Json {

    static Json json2Object(String jsonString){
        JsonData jsonData = new JsonData(jsonString);
        JsonData.JsonDataPeeker peeker = jsonData.peeker();
        Json json = null;
        int pointer = peeker.peek();
        if(JsonPointer.isObject(pointer)){
            JsonObject jsonObject = new JsonObject();
            if(peeker.startObject()) {
                do {
                    jsonObject.set(peeker.readProperty(),peeker.readValue());
                }while(peeker.hasNextObjectElement());
            }
            peeker.endObject();
            json = jsonObject;
        }else if(JsonPointer.isArray(pointer)){
            JsonArray jsonArray = new JsonArray();
            json = jsonArray;
        }else{
            JsonPrimitive jsonPrimitive = new JsonPrimitive();
            json = jsonPrimitive;
        }
        return json;
    }

}
