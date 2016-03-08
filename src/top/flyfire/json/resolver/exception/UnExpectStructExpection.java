package top.flyfire.json.resolver.exception;

import top.flyfire.json.JsonException;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/3/7.
 */
public class UnExpectStructExpection extends JsonException {
    public UnExpectStructExpection() {
        super();
    }

    public UnExpectStructExpection(int expect,int pointer) {
        super("UnExpectStructExpection[expect:"+expect+" , pointer:"+pointer+"]");
    }

    public UnExpectStructExpection(String message) {
        super(message);
    }
}
