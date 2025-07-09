package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private LinkedList<Entry<K, V>>[] buckets;
    private int capacity;
    private int size;
    private V nullKeyValue;
    private boolean hasNullKey;

    public MyHashMap() {
        this.capacity = INITIAL_CAPACITY;
        this.buckets = createBucketArray(capacity);
        this.hasNullKey = false;
        this.size = 0;
    }

    private LinkedList<Entry<K, V>>[] createBucketArray(int capacity) {
        return (LinkedList<Entry<K, V>>[]) new LinkedList[capacity];
    }

    private int getBucketIndex(K key) {
        int hash = key.hashCode();
        int index = hash % capacity;
        return index < 0 ? index + capacity : index;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            if (!hasNullKey) {
                size++;
            }
            nullKeyValue = value;
            hasNullKey = true;
            return;
        }
        int index = getBucketIndex(key);

        if (buckets[index] == null) {
            buckets[index] = new LinkedList<>();
        }
        for (Entry<K, V> entry : buckets[index]) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        buckets[index].add(new Entry<>(key, value));
        size++;
        if ((float) size / capacity >= LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return hasNullKey ? nullKeyValue : null;
        }
        int index = getBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];
        if (buckets[index] != null) {
            for (Entry<K, V> entry : buckets[index]) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resize() {
        capacity *= 2;
        LinkedList<Entry<K, V>>[] oldBuckets = buckets;
        buckets = createBucketArray(capacity);
        size = hasNullKey ? 1 : 0;

        for (LinkedList<Entry<K, V>> bucket : oldBuckets) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    put(entry.key, entry.value);
                }
            }
        }
    }
}
