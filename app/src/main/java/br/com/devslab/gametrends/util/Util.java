package br.com.devslab.gametrends.util;

import java.util.List;

public class Util {

    public static Boolean isEmptyOrNull(Object o){

        Boolean condition = Boolean.FALSE;

        if(o == null){
            condition = Boolean.TRUE;
        }else if(o instanceof List){
            List list = (List) o;
            if(list.isEmpty()){
                condition = Boolean.TRUE;
            }
        }

        return condition;
    }
}
