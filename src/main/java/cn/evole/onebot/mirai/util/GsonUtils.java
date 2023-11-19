package cn.evole.onebot.mirai.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.Map;

public class GsonUtils {
    private static final ThreadLocal<Gson>gsonTl= new ThreadLocal<>();
    public static Gson getGson() {
        Gson gson =  (new GsonBuilder()).enableComplexMapKeySerialization().setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting().disableHtmlEscaping().create();
        if (gson == null){
            gson = new Gson();
            gsonTl.set(gson);
        }
        return gson;
    }
}
