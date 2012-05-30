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
package com.ibm.wala.ipa.callgraph.actors;

import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.propagation.AbstractPointerAnalysis;
import com.ibm.wala.ipa.callgraph.propagation.FilteredPointerKey;
import com.ibm.wala.ipa.callgraph.propagation.HeapModel;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKeyFactory;
import com.ibm.wala.ipa.callgraph.propagation.PointerKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerKeyFactory;
import com.ibm.wala.ipa.callgraph.propagation.PointsToMap;
import com.ibm.wala.ipa.callgraph.propagation.PointsToSetVariable;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.util.collections.Iterator2Iterable;
import com.ibm.wala.util.intset.IntSet;
import com.ibm.wala.util.intset.MutableMapping;
import com.ibm.wala.util.intset.OrdinalSet;

public class ActorsPointerAnalysis extends AbstractPointerAnalysis {
  private final PointsToMap pointsToMap;
  private HeapModel H;

  protected ActorsPointerAnalysis(CallGraph cg, PointsToMap pointsToMap,
      MutableMapping<InstanceKey> instanceKeys, PointerKeyFactory pointerKeys, InstanceKeyFactory iKeyFactory) {
    super(cg, instanceKeys);
    this.pointsToMap = pointsToMap;
    // TODO Auto-generated constructor stub
    this.H = new ActorsHeapModel(cg.getClassHierarchy(), pointsToMap, iKeyFactory, pointerKeys);
  }

  public OrdinalSet<InstanceKey> getPointsToSet(PointerKey key) {
    PointsToSetVariable v = pointsToMap.getPointsToSet(key);

    if (v == null) {
      return OrdinalSet.empty();
    } else {
      IntSet S = v.getValue();
      return new OrdinalSet<InstanceKey>(S, instanceKeys);
    }
  }

  public HeapModel getHeapModel() {
    return H;
  }

  public Iterable<PointerKey> getPointerKeys() {
    return Iterator2Iterable.make(pointsToMap.iterateKeys());
  }

  public boolean isFiltered(PointerKey key) {
    if (pointsToMap.isImplicit(key)) {
      return false;
    }
    PointsToSetVariable v = pointsToMap.getPointsToSet(key);
    if (v == null) {
      return false;
    } else {
      return v.getPointerKey() instanceof FilteredPointerKey;
    }
  }

  public IClassHierarchy getClassHierarchy() {
    return cg.getClassHierarchy();
  }

}
