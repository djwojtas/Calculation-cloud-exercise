package pl.edu.agh.kis.woznwojc;

import java.util.*;

/**
 * Implementation of {@link Cache}. Provides basic interface functionality along with {@link #maxSize maximum number of elements}
 *
 * @author Wojciech Woźniczka
 *
 * @see Cache
 */
public class SizeControlCache implements Cache
{
    TreeMap<String, CacheRecord> cache;
    TreeMap<Long, String> cleanupSortage;
    private long currentSize = 0;
    private long maxSize;

    /**
     * Constructor that creates <b>TreeMap</b> as basic storage data structure along with setting {@link #maxSize}
     *
     * @param maxCacheSize determines maximum number of elements in cache
     */
    SizeControlCache(int maxCacheSize)
    {
        maxSize = maxCacheSize;

        cache = new TreeMap<>();
        cleanupSortage = new TreeMap<>();
    }

    public void add(String key, String val)
    {
        if(cache.size() <= maxSize)
        {
            long time = System.currentTimeMillis();
            while(cleanupSortage.containsKey(time))
            {
                time = System.currentTimeMillis();
            }

            CacheRecord record = new CacheRecord(time, val, key);
            cache.put(key, record);
            cleanupSortage.put(record.lastAcc, key);

            currentSize += key.length()*4 + val.length()*2 + 40;
        }
    }

    public String get(String key)
    {
        CacheRecord result = cache.get(key);
        if(result != null)
        {
            long time = System.currentTimeMillis();
            while(!cleanupSortage.containsKey(time))
            {
                time = System.currentTimeMillis();
            }

            cleanupSortage.remove(result.lastAcc);
            result.lastAcc = time;
            cleanupSortage.put(result.lastAcc, key);

            return result.record;
        }
        else
            return null;
    }

    public void cleanUp()
    {
        if(currentSize > maxSize)
        {
            Iterator<Map.Entry<Long, String>> iter = cleanupSortage.entrySet().iterator();
            Map.Entry<Long, String> entry;

            while (iter.hasNext())
            {
                if (currentSize < maxSize*0.9) break;

                entry = iter.next();
                String value = entry.getValue();

                currentSize -= entry.getValue().length()*4 + cache.get(value).record.length()*2 + 40;
                cache.remove(value);
                iter.remove();
            }
        }
    }
}