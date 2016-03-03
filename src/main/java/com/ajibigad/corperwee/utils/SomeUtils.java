package com.ajibigad.corperwee.utils;

/**
 * Created by Julius on 03/03/2016.
 */
public class SomeUtils {

    public static boolean isAllNotNull(Iterable<?> list){
        for(Object obj : list){
            if(obj == null)
                return false;
        }
        return true;
    }
}
