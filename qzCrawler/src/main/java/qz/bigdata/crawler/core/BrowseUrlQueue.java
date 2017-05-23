package qz.bigdata.crawler.core;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by fys on 2015/1/11.
 */
public class BrowseUrlQueue<T> {
    private LinkedBlockingDeque<T> queue = new LinkedBlockingDeque<T>();
    private Boolean reduceDuplicated = true;


    public T poll()
    {
        return queue.poll();
    }

    public void put(T url)
            throws InterruptedException
    {
        if(this.reduceDuplicated)
        {
            if(this.queue.contains(url)) {
                return;
            }
        }
        queue.put(url);
    }

    public void putFirst(T url)
            throws InterruptedException
    {
        if(this.reduceDuplicated)
        {
            if(this.queue.contains(url)) {
                return;
            }
        }
        queue.putFirst(url);
    }

    public int size()
    {
        return this.queue.size();
    }

    public Boolean find(String url, Boolean ignoreCase)
    {
        return false;
    }

    public BrowseUrlQueue<T> cloneQueue ()
            throws InterruptedException
    {
        BrowseUrlQueue<T> queue1 = new BrowseUrlQueue<T>();
        int size = this.size();
        for(int i=0; i< size; i++) {
            T t = this.poll();
            queue1.put(t);
            this.put(t);
        }
        return queue1;
    }
}
