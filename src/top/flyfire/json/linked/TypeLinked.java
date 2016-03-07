package top.flyfire.json.linked;

import top.flyfire.json.type.Json;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/3/6.
 */
public interface TypeLinked {
    Json java2Json();
    <T> T json2Java();
}
