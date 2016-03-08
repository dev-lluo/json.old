package top.flyfire.json;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/3/6.
 */
public interface JsonPointer {

    int PRIMITIVE = 0;
    static boolean isPrimitive(int pointer){
        return PRIMITIVE==pointer;
    }

    int ARRAY = 1;
    static boolean isArray(int pointer){
        return ARRAY==pointer;
    }

    int OBJECT = 2;
    static boolean isObject(int pointer){
        return OBJECT==pointer;
    }

    int S_QUOTE = 3;
    static boolean isSQuote(int pointer){
        return S_QUOTE == pointer;
    }

    int D_QOUTE = 4;
    static boolean isDQuote(int pointer){
        return D_QOUTE == pointer;
    }

    int ESCAPE = 5;
    static boolean isEscape(int pointer){
        return ESCAPE == pointer;
    }
}
