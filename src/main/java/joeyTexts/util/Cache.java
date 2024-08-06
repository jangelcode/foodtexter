package joeyTexts.util;

import java.util.HashMap;
import java.util.HashSet;

public class Cache {

    private static final HashSet<String> cache = new HashSet<>();

    public static boolean checkInCache(String phoneNumber) {
        if (!cache.contains(phoneNumber)) {
            addNumberToCache(phoneNumber);
            return false;
        }
        return true;
    }

    public static void addNumberToCache(String phoneNumber) {
        cache.add(phoneNumber);
    }
}
