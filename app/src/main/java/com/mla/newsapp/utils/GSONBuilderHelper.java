package com.mla.newsapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Primitives;

import java.lang.reflect.Type;

/**
 * Created by kumar.samdwar on 15/05/15.
 */
public class GSONBuilderHelper {
    private GsonBuilder mGsonBuilder;
    private Gson mGson;

    public GSONBuilderHelper(){
        mGsonBuilder =  new GsonBuilder();
        mGson = mGsonBuilder.create();
    }

    public <T> T getJsonFromString(String json, Class<T> classOfT) throws JsonSyntaxException {
        Object object = mGson.fromJson(json, (Type) classOfT);
        return Primitives.wrap(classOfT).cast(object);
    }
}
