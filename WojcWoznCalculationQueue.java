package pl.edu.agh.kis.woznwojc;

import java.util.Comparator;
import java.util.PriorityQueue;

interface CalculationQueue<T>
{
    void add(T elem);
    int size();
    T pull();
}

/**
 * Created by djwojtas on 2016-10-23.
 */
public class WojcWoznCalculationQueue implements CalculationQueue<Command>
{
    PriorityQueue<Command> pQueue;

    WojcWoznCalculationQueue()
    {
        pQueue = new PriorityQueue<Command>(10, new Comparator<Command>()
        {
            public int compare(Command x, Command y)
            {
                if(x.priority < y.priority) return -1;
                if(x.priority > y.priority) return 1;
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
