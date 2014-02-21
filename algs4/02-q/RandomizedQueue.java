/*
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org/>
*/

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
  
  private int s;
  private int capacity;
  private Item[] q;

  /* construct an empty randomized queue */ 
  public RandomizedQueue() {
    s = 0;
    capacity = 0;
  }

  /* is the queue empty */
  public boolean isEmpty() {
    return (s == 0);
  }

  /* number of items in the queue */
  public int size() {
    return s;
  }

  /* add item */
  public void enqueue(Item item) {
    if (item == null)
      throw new NullPointerException("item can not be null");
   
    if (s == capacity) 
      expand();

    q[s++] = item;
  }

  /* delete and return a random item */
  public Item dequeue() {
    if (s == 0)
      throw new java.util.NoSuchElementException("queue is empty");

    int i = StdRandom.uniform(0, s);
    Item tmpItem = q[i];
    
    --s;
    q[i] = q[s];
    q[s] = null;

    if ((s == 0) || (s < (capacity / 4)))
      shrink(); 
    
    return tmpItem;
  }
  
  /* return (but not delete) a random item */
  public Item sample() {
    if (s == 0)
      throw new java.util.NoSuchElementException("queue is empty");

    return q[StdRandom.uniform(0, s)];
  }

  private class QIterator implements Iterator<Item> {
    private int[] seq;
    private int current;
    private int capacityIterator;

    public QIterator() {
      current = 0;
      capacityIterator = size();
      seq = new int[capacityIterator];
      
      int j;
      for (j = 0; j < capacityIterator; ++j)
        seq[j] = j;
      
      // Knuth shuffle
      for (j = 0; j < capacityIterator; ++j) {
        int r = StdRandom.uniform(j + 1);
      
        // Swap seq[j] with seq[r]
        int tmp = seq[j];
        seq[j] = seq[r];
        seq[r] = tmp;
      }
    }
    
    public boolean hasNext() {
      return (current != capacityIterator);
    }

    public Item next() {
      if (current == capacityIterator)
        throw new java.util.NoSuchElementException("no more elements");
      
      return q[seq[current++]]; 
    }

    public void remove() {
      throw new UnsupportedOperationException("operation not supported");
    }
  } 
 
  /* return an independent iterator over items in random order */
  public Iterator<Item> iterator() {
    return new QIterator();
  }

  /* expand the array to double the capacity */
  private void expand() {
    int newCapacity; 
    if (capacity == 0) newCapacity = 1;
    else newCapacity = capacity * 2;
    
    copyQ(newCapacity);
    capacity = newCapacity;
  }
  
  /* shrink the array to half the current capacity */
  private void shrink() {
    if (s == 0) {
      q = null;
      capacity = 0;
      return;
    }
    int newCapacity = capacity / 2;

    copyQ(newCapacity);
    capacity = newCapacity;
  }

  /* create a new array and copy contents */
  private void copyQ(int newCapacity) {
    Item[] tmpQ = (Item[]) new Object[newCapacity];

    for (int i = 0; i < s; ++i) 
      tmpQ[i] = q[i];
    
    q = tmpQ;
  }

  public static void main(String[] args) {
    int n = 42;
    RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
    for (int i = 0; i < n; ++i)
      rq.enqueue(i);

    for (int z : rq) {
      StdOut.printf("Got %d. size is %d, capacity is %d\n", z, rq.size(), 
                    rq.capacity); 
    }
    StdOut.printf("\n");

    for (int z : rq) {
      StdOut.printf("Got %d. size is %d, capacity is %d\n", z, rq.size(), 
                    rq.capacity); 
    }
    StdOut.printf("\n");

    for (int i = 0; i < n; ++i) {
      StdOut.printf("%d: %d. ", i, rq.dequeue());
      StdOut.printf("size is %d ", rq.size());
      StdOut.printf("capacity is %d\n", rq.capacity);
    }
  } 
}

