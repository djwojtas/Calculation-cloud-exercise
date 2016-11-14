package pl.edu.agh.kis.woznwojc;

import java.util.*;

/**
 * Implementation of {@link Cache}. Provides basic interface functionality along with {@link #maxSize maximum number of elements}
 *
 * @author Wojciech Woźniczka
 *
 * @see Cache
 */
public class queueCache implements Cache
{
    private TreeMap<String, CacheRecord> cache;
    private PriorityQueue<CacheRecord> deleteQueue;
    private long currentSize = 0;
    private long maxSize;

    /**
     * Constructor that creates <b>TreeMap</b> as basic storage data structure and <b>PriorityQueue</b> for cleaning cache. It also sets {@link #maxSize}
     *
     * @param maxCacheSize determines maximum number of elements in cache
     */
    queueCache(int maxCacheSize)
    {
        maxSize = maxCacheSize;

        deleteQueue = new PriorityQueue<>(
            new Comparator<CacheRecord>()
            {
                public int compare(CacheRecord x, CacheRecord y)
                {
                    if (x.lastAcc < y.lastAcc) return -1;
                    if (x.lastAcc > y.lastAcc) return 1;
                    return 0;
                }
            });

        cache = new TreeMap<>();
    }

    public void add(String key, String val)
    {
        if(cache.size() <= maxSize)
        {
            CacheRecord record = new CacheRecord(System.nanoTime(), val, key);
            cache.put(key, record);
            deleteQueue.add(record);

            long recordSize = key.length()*4 + val.length()*2 + 8;

            if(recordSize <= maxSize)
            {
                currentSize += recordSize;
            }
        }
    }

    public String get(String key)
    {
        CacheRecord result = cache.get(key);
        if(result != null)
        {
            deleteQueue.remove(result);
            result.lastAcc = System.nanoTime();
            deleteQueue.add(result);

            return result.record;
        }
        else
            return null;
    }

    public void cleanUp()
    {
        if(currentSize > maxSize)
        {
            while(currentSize*0.9 > maxSize)
            {
                CacheRecord toDel = deleteQueue.poll();
                cache.remove(toDel.key);

                currentSize -= toDel.key.length()*4 + toDel.record.length()*2 + 8;
            }
        }
    }
}