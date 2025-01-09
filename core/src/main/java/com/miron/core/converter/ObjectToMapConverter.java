package com.miron.core.converter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ObjectToMapConverter {
    public static Map<Integer, Integer> convertJSONObjectToMap(final JSONObject productsCountOnId) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        Iterator<String> iterator = productsCountOnId.keys();
        while(iterator.hasNext()){
            String key = iterator.next();
            map.put(Integer.parseInt(key), productsCountOnId.getInt(key));
        }
        return map;
    }
}
