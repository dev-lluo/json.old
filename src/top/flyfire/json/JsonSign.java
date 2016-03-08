package top.flyfire.json;

/**
 * Created by h on 2016/3/8.
 */
public interface JsonSign {
    char ARRAY_START = '[';
    static boolean isArrayStart(char sign){
        return ARRAY_START == sign;
    }

    char ARRAY_END = ']';
    static boolean isArrayEnd(char sign){
        return ARRAY_END == sign;
    }

    char OBJECT_START = '{';
    static boolean isObjectStart(char sign){
        return OBJECT_START == sign;
    }

    char OBJECT_END = '}';
    static boolean isObjectEnd(char sign){
        return OBJECT_END == sign;
    }

    char ESCAPE = '\\';
    static boolean isEscape(char sign){
        return ESCAPE == sign;
    }

    char S_QUOTE = '\'';
    static boolean isSQuote(char sign){
        return S_QUOTE == sign;
    }

    char D_QUOTE = '\"';
    static boolean isDQuote(char sign){
        return D_QUOTE == sign;
    }

    char COLON = ':';
    static boolean isColon(char sign){
        return COLON == sign;
    }

    char COMMA = ',';
    static boolean isComma(char sign){
        return COMMA == sign;
    }
}
