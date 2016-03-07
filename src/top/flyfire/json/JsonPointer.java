package top.flyfire.json;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/3/6.
 */
public interface JsonPointer {
    char ARRAY_START = '[';
    static boolean isArrayStart(char ch){
        return ARRAY_START == ch;
    }

    char ARRAY_END = ']';
    static boolean isArrayEnd(char ch){
        return ARRAY_END == ch;
    }

    char OBJECT_START = '{';
    static boolean isObjectStart(char ch){
        return OBJECT_START == ch;
    }
    char OBJECT_END = '}';
    static boolean isObjectEnd(char ch){
        return OBJECT_END == ch;
    }

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
    static boolean isDOuqte(int pointer){
        return D_QOUTE == pointer;
    }
}
