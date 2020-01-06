package xyz.theasylum.zendarva.Utility;

import java.util.HashMap;

public class Profile {
    static HashMap<Object, Long> profileData = new HashMap<>();

    public static void start(Object obj){

        profileData.put(obj,System.nanoTime());
    }
    public static void stop(Object obj){
        long start = profileData.get(obj);
        System.out.println(System.nanoTime()-start);
    }
}
