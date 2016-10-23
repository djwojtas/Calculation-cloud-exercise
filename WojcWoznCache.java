package pl.edu.agh.kis.woznwojc;

import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Iterator;


interface Cache
{
    void add(String key, String val);
    String get(String key);
    void cleanUp();
}


/** Class that handles process of caching
 *
 *  @author Wojciech Woźniczka
 */
public class WojcWoznCache implements Cache
{
    TreeMap<String, cacheRecord> cache;
    int maxSize;
    int recordTTL;

    WojcWoznCache(int maxCacheSize, int cacheTTLms)
    {
        maxSize = maxCacheSize;
        recordTTL = cacheTTLms;

        cache = new TreeMap<String, cacheRecord>();
    }

    public Cache newCache(int maxCacheSize, int cacheTTLms)
    {
        return new WojcWoznCache(maxCacheSize, cacheTTLms);
    }

    public void add(String key, String val)
    {
        if(cache.size() <= maxSize)
            cache.put(key, new cacheRecord(System.currentTimeMillis(), val, key));
    }

    public String get(String key)
    {
        cacheRecord result = cache.get(key);
        if(result != null)
        {
            result.lastAcc = System.currentTimeMillis();
            return result.record;
        }
        else
            return null;
    }

    public void cleanUp()
    {
        Iterator<Entry<String, cacheRecord>> iter = cache.entrySet().iterator();
        Entry<String, cacheRecord> entry;

        while (iter.hasNext())
        {
            entry = iter.next();

            if(System.currentTimeMillis() - entry.getValue().lastAcc > recordTTL)
            {
                iter.remove();
            }
        }
    }

    public static void main(String[] args)
    {
        Cache test = new WojcWoznCache(100, 100);


        test.add("1234", "test1");
        test.add("2134", "test2");

    }
}

class cacheRecord
{
    String key;
    long lastAcc;
    String record;

    cacheRecord(long recordLastAcc, String recordVal, String recordKey)
    {
        key = recordKey;
        lastAcc = recordLastAcc;
        record = recordVal;
    }
}