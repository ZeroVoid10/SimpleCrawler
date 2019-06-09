package xyz.zerovoid.simplecrawler.item;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import xyz.zerovoid.simplecrawler.util.Request;

/**
 * What Parser will return to Pipeline after parse Page.
 * @since 0.2.0
 */
public class Items {
    
    protected Map<String, Object> itemContianer = 
        new HashMap<String, Object>();
    protected Request request;
    protected Set<Request> newRequest = new HashSet<Request>();

    public Items(Request request) {
        this.request = request;
    }

    public <T> T get(String key) {
        Object value = itemContianer.get(key);
        if (value == null) {
            return null;
        }
        return (T)value;
    }

    public Map<String, Object> getAll() {
        return itemContianer;
    }

    public <T> Items put(String key, T value) {
        itemContianer.put(key, value);
        return this;
    }

    public Request getRequest() {
        return this.request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Items addRequest(Request request) {
        newRequest.add(request);
        return this;
    }

    public Items addRequest(String url) {
        newRequest.add(new Request(url));
        return this;
    }

    public Set<Request> getNewRequest() {
        return newRequest;
    }

    @Override
    public String toString() {
        return "Items<T>: {" + itemContianer + ", " + request + "};";
    }
}
