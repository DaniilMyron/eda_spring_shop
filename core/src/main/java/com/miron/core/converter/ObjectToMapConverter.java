package com.miron.core.converter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ObjectToMapConverter {
    public static Map<Integer, Integer> convertJSONObjectToMap(final JSONObject productsCountOnId) throws JSONException {
        var map = new HashMap<Integer, Integer>();
        Iterator<String> iterator = productsCountOnId.keys();
        while(iterator.hasNext()){
            var key = iterator.next();
            map.put(Integer.parseInt(key), productsCountOnId.getInt(key));
        }
        return map;
    }
}
