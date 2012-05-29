/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.wala.ipa.callgraph.propagation.actors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import com.ibm.wala.util.collections.HashMapFactory;
import com.ibm.wala.util.intset.OrdinalSetMapping;

public class ConcurrentMutableMapping<T> implements OrdinalSetMapping<T> {

  private static final int INITIAL_SIZE = 100;
  private static final int GROWTH_FACTOR = 10;

  Object[] a;
  int size; // the number of elements currently in the array

  /**
   * A mapping from object to Integer.
   */
  final ConcurrentHashMap<T, Integer> map = new ConcurrentHashMap<T, Integer>();

  public ConcurrentMutableMapping() {
    a = new Object[INITIAL_SIZE];
    size = 0;
  }

  public Iterator<T> iterator() {
    return map.keySet().iterator();
  }

  @SuppressWarnings("unchecked")
  public T getMappedObject(int n) throws NoSuchElementException {
    if(n < size)
      return (T) a[n];
    else {
      synchronized(this) {
        if(n < size)
          return (T) a[n];
        else
          throw new IndexOutOfBoundsException();
      }
    }
  }

  public int getMappedIndex(T o) {
    Integer integer = map.get(o);
    if(integer == null)
      return -1;
    else
      return integer;
  }

  public boolean hasMappedIndex(T o) {
    return map.containsKey(o);
  }

  public int getMaximumIndex() {
    return size - 1;
  }

  public int getSize() {
    return map.size();
  }

  public int add(T o) {
    synchronized (this) {
      int index = size;
      if (index >= a.length) {
        Object[] newArray = new Object[GROWTH_FACTOR * a.length];
        System.arraycopy(a, 0, newArray, 0, a.length);
        a = newArray;
      }
      a[index] = o;
      size++;
      // might be a bad thread interleaving here
      map.put(o, index);
      return index;
    }
  }
}
