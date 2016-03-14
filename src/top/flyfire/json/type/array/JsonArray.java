package top.flyfire.json.type.array;

import top.flyfire.json.resolver.JsonData;
import top.flyfire.json.resolver.exception.NotEnoughSpaceException;
import top.flyfire.json.type.Json;

import java.util.Collection;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/3/6.
 */
public class JsonArray implements Json {
    private static final int DEFAULT_CAP = 10;

    private static final int DEFAULT_GROWTH = 10;

    private int length;

    private Json[] value;

    public JsonArray() {
        this.length = 0;
        this.value = new Json[JsonArray.DEFAULT_CAP];
    }

    private int increase(){
        int expect_length = this.length+JsonArray.DEFAULT_GROWTH;
        if(expect_length>0&&expect_length<=Integer.MAX_VALUE) {
            Json[] new_container = new Json[expect_length];
            System.arraycopy(this.value,0,new_container,0,this.length);
            this.value = new_container;
            return expect_length;
        }else{
            throw new NotEnoughSpaceException();
        }
    }

    public void add(Json json){
        if(this.length==this.value.length) {
            this.increase();
        }
        this.value[this.length++] = json;
    }

    public Json get(int i){
        if(i<this.length){
            return this.value[i];
        }else{
            throw new IndexOutOfBoundsException("length:"+this.length+",index:"+i);
        }
    }

    public int length(){
        return this.length;
    }

    @Override
    public String toString() {
        if(this.length==0){
            return "[]";
        }else {
            JsonData data = new JsonData("[");
            data.append(this.value[0].toString());
            for (int i = 1; i < this.length; i++) {
                data.append(',');
                data.append(this.value[i].toString());
            }
            data.append("]");
            return data.toString();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null||(obj instanceof  JsonArray)){
            return false;
        }else{
            if(this==obj){
                return true;
            }else{
                JsonArray anotherArray = (JsonArray)obj;
                if(anotherArray.length()!=this.length()){
                    return false;
                }else{
                    for(int i = 0,len = this.length();i<len;i++){
                        Json thisCell = this.value[i];
                        Json anotherCell = anotherArray.value[i];
                        if((thisCell==null&&anotherCell!=null)||(thisCell!=null&&anotherCell==null)){
                            return false;
                        }else if(!thisCell.equals(anotherCell)){
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
    }
}
