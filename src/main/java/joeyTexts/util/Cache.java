package joeyTexts.util;

import java.util.HashMap;

public class Cache {

    private static final HashMap<String, Boolean> cache = DatabaseUtil.retrievePhoneNumbers();

    public static boolean checkInCache(String phoneNumber) {
        if (!cache.containsKey(phoneNumber)) {
            cache.put(phoneNumber, false);
            return false;
        }
        return true;
    }

    public static void setDeleteMode(String phoneNumber){
        cache.put(phoneNumber, true);
    }

    public static void setDeleteModeFalse(String phoneNumber){
        cache.put(phoneNumber, false);
    }

    public static Boolean isDeleteMode(String phoneNumber){
        return cache.get(phoneNumber);
    }
}
