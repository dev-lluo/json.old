package top.flyfire.json.resolver;

import top.flyfire.json.JsonPointer;
import top.flyfire.json.resolver.exception.NotEnoughSpaceException;
import top.flyfire.json.resolver.exception.UnExpectStructExpection;

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

    public JsonData append(char cell){
        this.append(new char[]{cell});
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


    private class Stack {
        StackCell pointer;

        public void pop(int value){
            if(this.isEmpty()){
                throw new UnExpectStructExpection(value);
            }else if(pointer.value==value){
                pointer = pointer.next;
            }else{
                throw new UnExpectStructExpection(value);
            }
        }

        public void push(int value){
            pointer = new StackCell(value,pointer);
        }

        public boolean isEmpty(){
            return pointer == null;
        }
    }

    private class StackCell{
        private int value;
        private StackCell next;

        private  StackCell(int value,StackCell next){
            this.value = value;
            this.next = next;
        }
    }
    public class JsonDataPeeker {
        private int destPos;
        private Stack stack ;
        public JsonDataPeeker(){
            this.destPos = 0;
            this.stack = new Stack();
        }

        public int peek(){
            char dest = JsonData.this.value[destPos];
            if(JsonPointer.isArrayStart(dest)){
                return JsonPointer.ARRAY;
            }else if(JsonPointer.isObjectStart(dest)){
                return JsonPointer.OBJECT;
            }else{
                return JsonPointer.PRIMITIVE;
            }
        }

        private void roll(){
            this.destPos++;
        }

        public void startObject(){
            this.stack.push(JsonPointer.OBJECT);
        }

        public String readProperty(){
            JsonData jsonData = new JsonData();
            this.roll();

            return jsonData.toString();
        }

        public String readValue(){
            return null;
        }

        public void endObject(){
            this.stack.pop(JsonPointer.OBJECT);
        }

    }

}
