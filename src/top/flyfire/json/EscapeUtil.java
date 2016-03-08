package top.flyfire.json;

/**
 * Created by h on 2016/3/8.
 */
public interface EscapeUtil {

    static boolean canEsc(char sign){
        return sign=='f'||sign=='n'||sign=='r'||sign=='t'||sign=='b'||sign=='0'||sign=='\''||sign=='\"'||sign=='\\';
    }

    static char esc(char sign){
        if(sign=='f'){
            return '\f';
        }else if(sign=='n'){
            return '\n';
        }else if(sign=='r'){
            return '\r';
        }else if(sign=='t'){
            return '\t';
        }else if(sign=='b'){
            return '\b';
        }else if(sign=='0'){
            return '\0';
        }else{
            return sign;
        }

    }
}
