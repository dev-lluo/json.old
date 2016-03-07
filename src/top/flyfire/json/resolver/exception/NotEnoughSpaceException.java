package top.flyfire.json.resolver.exception;

import top.flyfire.json.JsonException;
import top.flyfire.json.resolver.JsonData;
import top.flyfire.json.type.Json;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/3/6.
 */
public class NotEnoughSpaceException extends JsonException {
    public NotEnoughSpaceException() {
        super();
    }

    public NotEnoughSpaceException(JsonData jsonData) {
        super(jsonData.toString());
    }
}
