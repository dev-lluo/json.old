package test;

import top.flyfire.json.resolver.JsonData;

/**
 * Created by flyfire[dev.lluo@outlook.com] on 2016/3/6.
 */
public class Main {
    public static void main(String[] args){
        JsonData jsonData = new JsonData();
        for(int i = 0;i<1000;i++) {
            jsonData.append("dafsdfafs");
        }
        System.out.println(jsonData.length());
        System.out.println(jsonData);
    }
}
