package net.jcip.examples;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * BetterAttributeStore
 * <p/>
 * Reducing lock duration
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class BetterAttributeStore {
    @GuardedBy("this")
    private final Map<String, String> attributes = new HashMap<String, String>();

    private final Map<String,String> attributes2 = new ConcurrentHashMap<>();
    /**
     *
     * 紫颜色的字体就是共享变量，其余都是线程局部变量
     *
     * @param name
     * @param regexp
     * @return
     */
    public boolean userLocationMatches(String name, String regexp) {
        String key = "users." + name + ".location";
        String location;
        synchronized (this) {
            location = attributes.get(key);
        }
        if (location == null) {
            return false;
        } else {
            return Pattern.matches(regexp, location);
        }
    }
}
