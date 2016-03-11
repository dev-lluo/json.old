package top.flyfire.json.type.primitive;

import top.flyfire.json.resolver.exception.NotEnoughSpaceException;
import top.flyfire.json.type.Json;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/3/6.
 */
public class JsonPrimitive implements Json {

    private static final int DEFAULT_CAP = 500;

    private static final int DEFAULT_GROWTH = 500;

    private char[] value;

    private int length;

    public JsonPrimitive(){
        this.value = new char[JsonPrimitive.DEFAULT_CAP];
        this.length = 0;
    }

    public JsonPrimitive(String jsonString){
        this.value = jsonString.toCharArray();
        this.length = this.value.length;
        this.increase();
    }

    public JsonPrimitive append(String jsonString){
        char[] buffer = jsonString.toCharArray();
        this.append(buffer);
        return this;
    }

    public JsonPrimitive append(JsonPrimitive jsonPrimitive){
        this.append(jsonPrimitive.value);
        return this;
    }

    public JsonPrimitive append(char cell){
        this.append(new char[]{cell});
        return this;
    }

    /*
     * get the length of jsonprimitive
     * @return length
     */
    public int length(){
        return this.length;
    }

    public boolean isEmpty(){
        return this.length==0;
    }

    @Override
    public String toString() {
        return new String(this.value,0,this.length);
    }

    /*
     * append
     * @param buffer
     */
    private void append(char[] buffer){
        int require = buffer.length;
        int free = this.value.length - this.length;
        while(free<=require){
            free = this.increase()-require;
        }
        System.arraycopy(buffer,0,this.value,this.length,buffer.length);
        this.length = this.length+buffer.length;
    }

    /*
     * calculate expect length
     * if the expect length is legal
     * building a new container
     * copying the value to new container
     * replacing the value with new container
     * return the expect length
     */
    private int increase(){
        int expect_length = this.length+JsonPrimitive.DEFAULT_GROWTH;
        if(expect_length>0&&expect_length<=Integer.MAX_VALUE) {
            char[] new_container = new char[expect_length];
            System.arraycopy(this.value,0,new_container,0,this.length);
            this.value = new_container;
            return expect_length;
        }else{
            throw new NotEnoughSpaceException();
        }
    }

}
