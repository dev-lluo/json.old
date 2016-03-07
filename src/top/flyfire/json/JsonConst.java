package top.flyfire.json;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/3/6.
 */
public interface JsonConst {
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
    int ARRAY = 1;
    int OBJECT = 2;

}
