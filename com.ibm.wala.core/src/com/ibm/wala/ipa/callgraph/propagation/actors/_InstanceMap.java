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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.util.intset.OrdinalSetMapping;

public class _InstanceMap implements OrdinalSetMapping<InstanceKey> {

  private static final int INITIAL_SIZE = 100;
  private static final int GROWTH_FACTOR = 10;

  InstanceKey[] instanceKeysArray;
  Instance[] instancesArray;
  int size; // the number of elements currently in the array

  /**
   * A mapping from object to Integer.
   */
  final ConcurrentHashMap<InstanceKey, Integer> map = new ConcurrentHashMap<InstanceKey, Integer>();

  public _InstanceMap() {
    instanceKeysArray = new InstanceKey[INITIAL_SIZE];
    instancesArray = new Instance[INITIAL_SIZE];
    size = 0;
  }

  public Iterator<InstanceKey> iterator() {
    return map.keySet().iterator();
  }

  @SuppressWarnings("unchecked")
  public InstanceKey getMappedObject(int n) throws NoSuchElementException {
    if(n < size)
      return (InstanceKey) instanceKeysArray[n];
    else {
      synchronized(this) {
        if(n < size)
          return (InstanceKey) instanceKeysArray[n];
        else
          throw new IndexOutOfBoundsException();
      }
    }
  }

  public int getMappedIndex(InstanceKey o) {
    Integer integer = map.get(o);
    if(integer == null)
      return -1;
    else
      return integer;
  }

  public boolean hasMappedIndex(InstanceKey o) {
    return map.containsKey(o);
  }

  public int getMaximumIndex() {
    return size - 1;
  }

  public int getSize() {
    return map.size();
  }

  public synchronized int add(InstanceKey o) {
      int index = size;
      if (index >= instanceKeysArray.length) {
        InstanceKey[] newArray = new InstanceKey[GROWTH_FACTOR * instanceKeysArray.length];
        Instance[] newInstancesArray = new Instance[GROWTH_FACTOR * instanceKeysArray.length];
        System.arraycopy(instanceKeysArray, 0, newArray, 0, instanceKeysArray.length);
        System.arraycopy(instancesArray, 0, newInstancesArray, 0, instanceKeysArray.length);
        instanceKeysArray = newArray;
        instancesArray = newInstancesArray;
      }
      instanceKeysArray[index] = o;
      size++;
      // might be a bad thread interleaving here
      map.put(o, index);
      return index;
  }
  
  public synchronized int addPair(InstanceKey ok, Instance o) {
    add(ok);
    instancesArray[size-1] = o;
    return size;
  }
}
