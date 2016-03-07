package top.flyfire.json.resolver;

import top.flyfire.json.JsonConst;
import top.flyfire.json.resolver.exception.NotEnoughSpaceException;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/3/6.
 */
public class JsonData {

    private static final int DEFAULT_CAP = 500;

    private static final int DEFAULT_GROWTH = 500;

    private char[] value;

    private int length;

    public JsonData(){
        this.value = new char[JsonData.DEFAULT_CAP];
        this.length = 0;
    }

    public JsonData(String jsonString){
        this.value = jsonString.toCharArray();
        this.length = this.value.length;
        this.increase();
    }

    public JsonDataPeeker peeker(){
        return new JsonDataPeeker();
    }



    public JsonData append(String jsonString){
        char[] buffer = jsonString.toCharArray();
        this.append(buffer);
        return this;
    }

    public JsonData append(JsonData jsonData){
        this.append(jsonData.value);
        return this;
    }


    /*
     * get the length of jsondata
     * @return length
     */
    public int length(){
        return this.length;
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
        int expect_length = this.length+JsonData.DEFAULT_GROWTH;
        if(expect_length>0&&expect_length<=Integer.MAX_VALUE) {
            char[] new_container = new char[expect_length];
            System.arraycopy(this.value,0,new_container,0,this.length);
            this.value = new_container;
            return expect_length;
        }else{
            throw new NotEnoughSpaceException(this);
        }
    }


    public class JsonDataPeeker {
        private int destPos;
        public JsonDataPeeker(){
            this.destPos = 0;
        }

        public int peek(){
            char dest = JsonData.this.value[destPos];
            if(JsonConst.isArrayStart(dest)){
                return JsonConst.ARRAY;
            }else if(JsonConst.isObjectStart(dest)){
                return JsonConst.OBJECT;
            }else{
                return JsonConst.PRIMITIVE;
            }
        }

    }

}
