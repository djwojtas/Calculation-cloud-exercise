package pl.edu.agh.kis.woznwojc;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Interface for classes that are storing records sorted by given key (priority), and releases them in that order when needed.
 *
 * @param <T> Parameter that determines what type of objects queue contains.
 *
 * @author Wojciech Wozniczka
 */
interface CalculationQueue<T>
{
    /**
     * Method that adds new record to {@link CalculationQueue}
     *
     * @param elem Object that will be stored as record in queue
     */
    void add(T elem);

    /**
     * Method that checks how many records are enqueued in {@link CalculationQueue}
     *
     * @return number of enqueued records
     */
    int size();

    /**
     * Method that returns record with highest priority and delete it from {@link CalculationQueue}
     *
     * @return highest priority record
     */
    T pull();
}

/**
 * Implementation of {@link CalculationQueue} with T={@link Command}.
 *
 * @author Wojciech Wozniczka
 */
class WojcWoznCalculationQueue implements CalculationQueue<Command>
{
    /**
     * Priority queue used to store records
     */
    private PriorityQueue<Command> pQueue;

    /**
     * Constructor that overloads {@link #pQueue} compare method with one that can compare {@link Command} objects.
     */
    WojcWoznCalculationQueue()
    {
        pQueue = new PriorityQueue<>(new Comparator<Command>()
        {
            public int compare(Command x, Command y)
            {
                if (x.priority < y.priority) return -1;
                if (x.priority > y.priority) return 1;
                return 0;
            }
        });
    }

    public void add(Command elem)
    {
        pQueue.add(elem);
    }

    public int size()
    {
        return pQueue.size();
    }

    public Command pull()
    {
        return pQueue.poll();
    }
}
