package pl.edu.agh.kis.woznwojc;

import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Iterator;

/** Class that handles process of caching
 *
 *  @author Wojciech Woźniczka
 */
public class Cache
{
    TreeMap<String, cacheRecord> cache;
    int maxSize;
    int recordTTL;

    Cache(int maxCacheSize, int cacheTTLms)
    {
        maxSize = maxCacheSize;
        recordTTL = cacheTTLms;

        cache = new TreeMap<String, cacheRecord>();
    }

    void add(String key, String val)
    {
        if(cache.size() <= maxSize)
            cache.put(key, new cacheRecord(System.currentTimeMillis(), val, key));
    }

    String get(String key)
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

    String getClose(String key)
    {
        String lower = cache.lowerKey(key);
        if(lower != null)
        {
            cacheRecord result = cache.get(lower);
            result.lastAcc = System.currentTimeMillis();
            return (result.key + " " + result.record);
        }
        else
            return null;
    }

    void cleanUp()
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
        Cache test = new Cache(100, 100);
        test.getClose("lol");

        test.add("1234", "test1");
        test.add("2134", "test2");

        System.out.println(test.getClose("2133"));
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