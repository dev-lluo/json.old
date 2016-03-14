package top.flyfire.json.type.object;

import top.flyfire.json.JsonException;
import top.flyfire.json.resolver.JsonData;
import top.flyfire.json.resolver.exception.NotEnoughSpaceException;
import top.flyfire.json.type.Json;


/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/3/6.
 */
public class JsonObject implements Json {

    private static final int DEFAULT_CAP = 10;

    private static final int DEFAULT_GROWTH = 10;

    private static final int MAX_ENTRY_SIZE = 10;

    private Entry[] entries;

    public JsonObject() {
        this.entries = new Entry[DEFAULT_CAP];
    }

    private int increase(){
        int expect_length = this.entries.length+JsonObject.DEFAULT_GROWTH;
        if(expect_length>0&&expect_length<=Integer.MAX_VALUE) {
            Entry[] new_container = new Entry[expect_length];
            Entry[] old_container = this.entries;
            this.entries = new_container;
           for(int i = 0;i<old_container.length;i++){
               Entry entry = old_container[i];
               if(entry==null)continue;
               do{
                   this.set(entry.getProperty(),entry.getValue());
                   entry = entry.getNext();
               }while (entry!=null);
           }
            return expect_length;
        }else{
            throw new NotEnoughSpaceException();
        }
    }

    public void set(String property,Json json){
        int hash = property.hashCode();
        hash = (hash ^ (hash >> 31)) - (hash >> 31);
        int index = hash%this.entries.length;
        this.entries[index] = new Entry(property,json,this.entries[index]);
        if(this.entries[index].getSize()==JsonObject.MAX_ENTRY_SIZE){
            this.increase();
        }
    }

    public Json get(String property){
        int hash = property.hashCode();
        hash = (hash ^ (hash >> 31)) - (hash >> 31);
        int index = hash%this.entries.length;
        Entry entry = this.entries[index];
        while (entry!=null){
            if(property.equals(entry.getProperty())){
                return entry.getValue();
            }
        }
        throw new JsonException("unknown property["+property+"]");
    }


    public class Entry {
        private String property;

        private Json value;

        private Entry next;

        private int size;

        public Entry(String property, Json value, Entry next) {
            this.property = property;
            this.value = value;
            this.next = next;
            if(this.next==null){
                this.size = 1;
            }else{
                this.size = ++next.size;
            }
        }

        public int getSize() {
            return size;
        }

        public Entry getNext() {
            return next;
        }

        public void setNext(Entry next) {
            this.next = next;
        }

        public Json getValue() {
            return value;
        }

        public void setValue(Json value) {
            this.value = value;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        @Override
        public String toString() {
            JsonData data = new JsonData(this.property);
            data.append(':');
            data.append(this.value.toString());
            if(this.next!=null)data.append(',');
            return data.toString();
        }
    }

    @Override
    public String toString() {
        if(this.entries.length==0){
            return "{}";
        }else{
            JsonData data = new JsonData("{");
            for(int i = 0;i<this.entries.length;i++){
                Entry entry = this.entries[i];
                if(entry!=null){
                    if(data.length()>1)data.append(",");
                    do {
                        data.append(entry.toString());
                    }while ((entry=entry.getNext())!=null);
                }
            }
            data.append("}");
            return data.toString();
        }
    }
}
