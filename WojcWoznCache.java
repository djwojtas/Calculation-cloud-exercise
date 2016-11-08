package pl.edu.agh.kis.woznwojc;

import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Iterator;

/**
 * Interface for simple Caching classes
 *
 * @author Wojciech Woźniczka
 */
interface Cache
{
    void add(String key, String val);
    String get(String key);
    void cleanUp();
}

/**
 * Implementation of {@link Cache}. Provides basic interface functionality along with {@link #maxSize maximum number of elements}
 * and {@link #recordTTL maximum record time to live}
 *
 * @author Wojciech Woźniczka
 *
 * @see Cache
 */
public class WojcWoznCache implements Cache
{
    TreeMap<String, CacheRecord> cache;
    private int maxSize;
    private int recordTTL;

    /**
     * Constructor that creates <b>TreeMap</b> as basic storage data structure along with setting {@link #maxSize} and
     * {@link #recordTTL}
     *
     * @param maxCacheSize determines maximum number of elements in cache
     * @param cacheTTLms determines maximum record time to live
     */
    WojcWoznCache(int maxCacheSize, int cacheTTLms)
    {
        maxSize = maxCacheSize;
        recordTTL = cacheTTLms;

        cache = new TreeMap<>();
    }

    public void add(String key, String val)
    {
        if(cache.size() <= maxSize)
            cache.put(key, new CacheRecord(System.currentTimeMillis(), val, key));
    }

    public String get(String key)
    {
        CacheRecord result = cache.get(key);
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
        Iterator<Entry<String, CacheRecord>> iter = cache.entrySet().iterator();
        Entry<String, CacheRecord> entry;

        while (iter.hasNext())
        {
            entry = iter.next();

            if(System.currentTimeMillis() - entry.getValue().lastAcc > recordTTL)
            {
                iter.remove();
            }
        }
    }
}

/**
 * Class that represent {@link WojcWoznCache} record. Stores all values that can be used later by {@link FunctionHandler#computeFunction(Command)}
 *
 * @see FunctionHandler#computeFunction(Command)
 */
class CacheRecord implements Comparable<CacheRecord>
{
    /**
     * Key that was used to store record in {@link WojcWoznCache#cache Treemap}
     */
    String key;

    /**
     * Last record access in ms (access mean being {@link Cache#add(String, String) add} or {@link Cache#get(String)} get)
     */
    Long lastAcc;

    /**
     * Stored value
     */
    String record;

    /**
     * Constructor that set all fields of class using arguments.
     *
     * @param recordLastAcc last record access in ms (access mean being {@link Cache#add(String, String) add} or {@link Cache#get(String)} get)
     * @param recordVal Value that need to be stored under {@link #key}
     * @param recordKey Key that was used to store record in {@link WojcWoznCache#cache Treemap}
     */
    CacheRecord(long recordLastAcc, String recordVal, String recordKey)
    {
        key = recordKey;
        lastAcc = recordLastAcc;
        record = recordVal;
    }

    /**
     * compare function ovveride to implement possibility of sorting by time of last access
     *
     * @param record record to compare
     * @return comparation result
     */
    @Override
    public int compareTo(CacheRecord record) {
        return (lastAcc < record.lastAcc ? -1 :
                (lastAcc == record.lastAcc ? 0 : 1));
    }
}