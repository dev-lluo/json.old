package top.flyfire.json.resolver;

import top.flyfire.json.EscapeUtil;
import top.flyfire.json.JsonPointer;
import top.flyfire.json.JsonSign;
import top.flyfire.json.resolver.exception.NotEnoughSpaceException;
import top.flyfire.json.resolver.exception.UnExpectStructExpection;
import top.flyfire.json.type.Json;
import top.flyfire.json.type.array.JsonArray;
import top.flyfire.json.type.object.JsonObject;
import top.flyfire.json.type.primitive.JsonPrimitive;

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
                throw new UnExpectStructExpection(-1,value);
            }else if(pointer.value==value){
                pointer = pointer.next;
            }else{
                throw new UnExpectStructExpection(pointer.value,value);
            }
        }

        public int peek(){
            if(this.isEmpty()){
                return -1;
            }else{
                return this.pointer.value;
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
            this.destPos = -1;
            this.stack = new Stack();
        }

        public int peek(){
            this.roll();
            char dest = JsonData.this.value[destPos];
            if(JsonSign.isArrayStart(dest)){
                this.back();
                return JsonPointer.ARRAY;
            }else if(JsonSign.isObjectStart(dest)){
                this.back();
                return JsonPointer.OBJECT;
            }else{
                this.back();
                return JsonPointer.PRIMITIVE;
            }
        }

        private void roll(){
            this.destPos++;
        }

        private void back(){
            this.destPos--;
        }


        public Json readPrimitive(){
            JsonPrimitive jsonPrimitive = new JsonPrimitive();
            while(this.destPos<JsonData.this.length) {
                this.roll();
                char dest = JsonData.this.value[destPos];
                int pointer = stack.peek();

                if(JsonPointer.isEscape(pointer)){//如果前置指针为转义 ；则判断当前符号能不能进行转义；不能则抛出异常；能则转义拼接
                    if(EscapeUtil.canEsc(dest)){
                        stack.pop(pointer);
                        jsonPrimitive.append(EscapeUtil.esc(dest));
                    }else{
                        throw new RuntimeException("unknown escape[\\"+dest+"]");
                    }
                }else if(JsonPointer.isSQuote(pointer)){//如果前置指针为单引号
                    if(JsonSign.isSQuote(dest)){//且当前目标符号为单引号；则Primitive结束读取;否则拼接
                        stack.pop(pointer);
                        jsonPrimitive.append(dest);
                        break;
                    }else{
                        jsonPrimitive.append(dest);
                    }
                }else if(JsonPointer.isDQuote(pointer)){//如果前置指针为双引号
                    if(JsonSign.isDQuote(dest)){//且当前目标符号为双引号；则Primitive结束读取;否则拼接
                        stack.pop(pointer);
                        jsonPrimitive.append(dest);
                        break;
                    }else{
                        jsonPrimitive.append(dest);
                    }
                }else{
                    if(jsonPrimitive.isEmpty()) {
                        if (JsonSign.isSQuote(dest)) {
                            stack.push(JsonPointer.S_QUOTE);
                            jsonPrimitive.append(dest);
                        } else if (JsonSign.isDQuote(dest)) {
                            stack.push(JsonPointer.D_QOUTE);
                            jsonPrimitive.append(dest);
                        } else if (JsonSign.isColon(dest)||JsonSign.isEscape(dest)||JsonSign.isObjectStart(dest)||JsonSign.isObjectEnd(dest)||JsonSign.isArrayStart(dest)||JsonSign.isArrayEnd(dest)) {//Primitive不允许以Json结构关键或转义字符开始
                            throw new UnExpectStructExpection("primitive["+jsonPrimitive+"] start with ["+dest+"] ???");
                        } else {
                            jsonPrimitive.append(dest);
                        }
                    }else{
                        if (JsonSign.isSQuote(dest)||JsonSign.isDQuote(dest)||JsonSign.isEscape(dest)||JsonSign.isObjectStart(dest)||JsonSign.isArrayStart(dest)) {//无结构的Primitive不允许包含Json结构开始关键或转义字符
                            throw new UnExpectStructExpection("non-struct primitive["+jsonPrimitive+"] include ["+dest+"] ???");
                        } else if (JsonSign.isComma(dest)) {//无结构的Primitive遇到逗号；标识结束
                            this.back();
                            break;
                        } else if((JsonPointer.isObject(pointer)&&JsonSign.isObjectEnd(dest))||(JsonPointer.isArray(pointer)&&JsonSign.isArrayEnd(dest))){//无结构的Primitive遇到当前父结构结束标记；标识结束
                            this.back();
                            break;
                        } else {
                            jsonPrimitive.append(dest);
                        }
                    }
                }

            }
            return jsonPrimitive;
        }

        public boolean startObject(){
            this.roll();
            this.stack.push(JsonPointer.OBJECT);
            this.roll();
            char dest = JsonData.this.value[destPos];
            this.back();
            if(JsonSign.isObjectEnd(dest)){
                return false;
            }else{
                return true;
            }
        }

        public String readProperty(){
            JsonData jsonData = new JsonData();
            while(this.destPos<JsonData.this.length) {
                this.roll();
                char dest = JsonData.this.value[destPos];
                int pointer = stack.peek();

                if(JsonPointer.isEscape(pointer)){//如果前置指针为转义 ；则判断当前符号能不能进行转义；不能则抛出异常；能则转义拼接
                    if(EscapeUtil.canEsc(dest)){
                        stack.pop(pointer);
                        jsonData.append(EscapeUtil.esc(dest));
                    }else{
                        throw new RuntimeException("unknown escape[\\"+dest+"]");
                    }
                }else if(JsonPointer.isSQuote(pointer)){//如果前置指针为单引号
                    if(JsonSign.isSQuote(dest)){//且当前目标符号为单引号；则Property结束读取;否则拼接
                        stack.pop(pointer);
                        break;
                    }else{
                        jsonData.append(dest);
                    }
                }else if(JsonPointer.isDQuote(pointer)){//如果前置指针为双引号
                    if(JsonSign.isDQuote(dest)){//且当前目标符号为双引号；则Property结束读取;否则拼接
                        stack.pop(pointer);
                        break;
                    }else{
                        jsonData.append(dest);
                    }
                }else{
                    if(jsonData.isEmpty()) {
                        if (JsonSign.isSQuote(dest)) {
                            stack.push(JsonPointer.S_QUOTE);
                        } else if (JsonSign.isDQuote(dest)) {
                            stack.push(JsonPointer.D_QOUTE);
                        } else if (JsonSign.isColon(dest)||JsonSign.isEscape(dest)||JsonSign.isObjectStart(dest)||JsonSign.isObjectEnd(dest)||JsonSign.isArrayStart(dest)||JsonSign.isArrayEnd(dest)) {//Property不允许以Json结构关键或转义字符开始
                            throw new UnExpectStructExpection("property start with ["+dest+"] ???");
                        } else {
                            jsonData.append(dest);
                        }
                    }else{
                        if (JsonSign.isSQuote(dest)||JsonSign.isDQuote(dest)||JsonSign.isEscape(dest)||JsonSign.isObjectStart(dest)||JsonSign.isObjectEnd(dest)||JsonSign.isArrayStart(dest)||JsonSign.isArrayEnd(dest)) {//无结构的Property不允许包含Json结构关键或转义字符
                            throw new UnExpectStructExpection("non-struct property include ["+dest+"] ???");
                        } else if (JsonSign.isColon(dest)) {//无结构的Property遇到冒号；标识结束
                            this.back();
                            break;
                        } else {
                            jsonData.append(dest);
                        }
                    }
                }

            }
            return jsonData.toString();
        }

        public Json readValue(){
            Json json = null;
            this.roll();
            char dest = JsonData.this.value[destPos];
            if(JsonSign.isColon(dest)){
                int pointer = this.peek();
                if(JsonPointer.isObject(pointer)){
                    JsonObject jsonObject = new JsonObject();
                    if(this.startObject()) {
                        do {
                            jsonObject.set(this.readProperty(), this.readValue());
                        }while(this.hasNextObjectElement());
                    }
                    this.endObject();
                    json = jsonObject;
                }else if(JsonPointer.isArray(pointer)){
                    JsonArray jsonArray = new JsonArray();
                    if(this.startArray()) {
                        do {
                            jsonArray.add(this.readArrayCell());
                        }while(this.hasNextArrayElement());
                    }
                    this.endArray();
                    json = jsonArray;
                }else if(JsonPointer.isPrimitive(pointer)){
                    json = this.readPrimitive();
                }else{
                    throw new RuntimeException();
                }
            }else{
                throw new UnExpectStructExpection("please insert colon between property and value...");
            }
            return json;
        }

        public boolean hasNextObjectElement(){
            this.roll();
            char dest = JsonData.this.value[destPos];
            if(JsonSign.isObjectEnd(dest)){
                return false;
            }else if(JsonSign.isComma(dest)){
                return true;
            }else{
                throw new UnExpectStructExpection();
            }
        }

        public void endObject(){
            this.stack.pop(JsonPointer.OBJECT);
        }


        public boolean startArray(){
            this.roll();
            this.stack.push(JsonPointer.ARRAY);
            this.roll();
            char dest = JsonData.this.value[destPos];
            this.back();
            if(JsonSign.isArrayEnd(dest)){
                return false;
            }else{
                return true;
            }
        }

        public Json readArrayCell(){
            Json json = null;
            int pointer = this.peek();
            if(JsonPointer.isObject(pointer)){
                JsonObject jsonObject = new JsonObject();
                if(this.startObject()) {
                    do {
                        jsonObject.set(this.readProperty(), this.readValue());
                    }while(this.hasNextObjectElement());
                }
                this.endObject();
                json = jsonObject;
            }else if(JsonPointer.isArray(pointer)){
                JsonArray jsonArray = new JsonArray();
                if(this.startArray()) {
                    do {
                        jsonArray.add(this.readArrayCell());
                    }while(this.hasNextArrayElement());
                }
                this.endArray();
                json = jsonArray;
            }else if(JsonPointer.isPrimitive(pointer)){
                json = this.readPrimitive();
            }else{
                throw new RuntimeException();
            }
            return json;
        }

        public boolean hasNextArrayElement(){
            this.roll();
            char dest = JsonData.this.value[destPos];
            if(JsonSign.isArrayEnd(dest)){
                return false;
            }else if(JsonSign.isComma(dest)){
                return true;
            }else{
                throw new UnExpectStructExpection();
            }
        }

        public void endArray(){
            this.stack.pop(JsonPointer.ARRAY);
        }

    }

}
