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

public class Dequeue<Item> implements Iterable<Item> {
  private int n; // Number of elements in the dequeue
  private DequeueNode<Item> start;
  private DequeueNode<Item> end;

  private class DequeueNode<Item> {
    private DequeueNode<Item> next;
    private DequeueNode<Item> prev;
    private Item item;

    /* default constructor */ 
    public DequeueNode() {
      next = null;
      prev = null;
      item = null;
    } 

    /* initializing constructor */
    public DequeueNode(Item i) {
      item = i;
      next = null;
      prev = null;
    }
  }

  /* construct an empty dequeue */
  public Dequeue() {
    start = null;
    end = null;
    n = 0;
  }

  /* is the dequeue empty */
  public boolean isEmpty() {
    return (n == 0);
  }
  
  /* return number of items in the dequeue */
  public int size() {
    return n;
  }

  /* insert the item at the front */
  public void addFirst(Item item) {
    if (item == null) 
     throw new NullPointerException("item can not be null"); 

    DequeueNode<Item> tmpItem = new DequeueNode<Item>(item);  

    /* if this is the first item  */
    if (n == 0) {
      start = tmpItem;
      end = tmpItem;
    }
    else {
      start.prev = tmpItem;
      tmpItem.next = start;
      start = tmpItem;
    }

    ++n;
    return;
  }

  /* insert the item at the end */
  public void addLast(Item item) {
    if (item == null) 
      throw new NullPointerException("item can not be null"); 
  
    DequeueNode<Item> tmpItem = new DequeueNode<Item>(item); 
  
    if (n == 0) {
      start = tmpItem;
      end = tmpItem;
    }
    else {
      end.next = tmpItem;
      tmpItem.prev = end;
      end = tmpItem;
    }

    ++n;
    return;
  }

  
  /* delete and return the item at the front */
  public Item removeFirst() {
    if (n == 0)
      throw new java.util.NoSuchElementException("dequeue is empty");
    
    Item tmp = start.item;
    start = start.next;
    if (n == 1) 
      end = null;

    --n;

    return tmp;
  }

  /* delete and return the item at the end */
  public Item removeLast() {
    if (n == 0)
      throw new java.util.NoSuchElementException("dequeue is empty");
  
    Item tmp = end.item;
    end = end.prev;
    if (n == 1)
      start = null;
    --n;

    return tmp;
  }
  
    
  private class DequeueIterator implements Iterator<Item> {
    private DequeueNode<Item> itCurrent;

    public DequeueIterator() {
      itCurrent = start;
    }

    public boolean hasNext() {
      return (itCurrent != null); 
    }

    public Item next() {
      if (itCurrent == null)
        throw new java.util.NoSuchElementException("no more elements");

      Item i = itCurrent.item;
      itCurrent = itCurrent.next;
      return i; 
    }

    public void remove() { 
      throw new UnsupportedOperationException("operation not supported");
    }
  }

  /* return an iterator over items in order from front to end */ 
  public Iterator<Item> iterator() {
    return new DequeueIterator();
  } 

  /* unit testing */
  public static void main(String[] args) {

    Dequeue<String> d = new Dequeue<String>();
    d.addFirst("Hello");
    d.addFirst("  ");
    d.addFirst("World");

    if (args[0].equals("1")) {
      StdOut.printf("Got %s\n", d.removeFirst());
      StdOut.printf("Got %s\n", d.removeFirst());
      StdOut.printf("Got %s\n", d.removeFirst());
    } else {
      for (String s : d) {
        StdOut.printf("Iter %s\n", s);
      }
    }

    Iterator<String> di = d.iterator();
    try {
      di.remove();
    }  
    catch (java.util.NoSuchElementException e) {
      StdOut.print("Got an expected exception\n");
    }
  }
}

