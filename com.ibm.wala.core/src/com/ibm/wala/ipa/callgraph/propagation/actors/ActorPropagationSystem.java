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
import java.util.LinkedList;
import java.util.List;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.fixpoint.AbstractOperator;
import com.ibm.wala.fixpoint.IFixedPointSystem;
import com.ibm.wala.fixpoint.UnaryOperator;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.propagation.IPropagationSystem;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKeyFactory;
import com.ibm.wala.ipa.callgraph.propagation.PointerAnalysis;
import com.ibm.wala.ipa.callgraph.propagation.PointerKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerKeyFactory;
import com.ibm.wala.ipa.callgraph.propagation.PointsToSetVariable;
import com.ibm.wala.ipa.callgraph.propagation.PropagationCallGraphBuilder;
import com.ibm.wala.ipa.callgraph.propagation.PropagationSystem;
import com.ibm.wala.ipa.callgraph.propagation.UnarySideEffect;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.MonitorUtil.IProgressMonitor;
import com.ibm.wala.util.intset.IntIterator;
import com.ibm.wala.util.intset.IntSet;
import com.ibm.wala.util.intset.MutableIntSet;
import com.ibm.wala.util.intset.MutableMapping;
import com.ibm.wala.util.intset.OrdinalSetMapping;

public class ActorPropagationSystem implements IPropagationSystem {

  private final OrdinalSetMapping<InstanceKey> instanceKeys = new ConcurrentMutableMapping<InstanceKey>();

  public IFixedPointSystem<PointsToSetVariable> getFixedPointSystem() {
    // TODO will have to figure out what we consider our fixed point system
    return null;
  }

  public boolean solve(IProgressMonitor monitor) throws CancelException {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isSolved() {
    // TODO Auto-generated method stub
    return false;
  }

  public PointerAnalysis extractPointerAnalysis(PropagationCallGraphBuilder propagationCallGraphBuilder) {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean newConstraint(PointerKey lhs, UnaryOperator<PointsToSetVariable> op, PointerKey rhs) {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean newConstraint(PointerKey exceptionVar, InstanceKey e) {
    // TODO Auto-generated method stub
    return false;
  }

  public void newConstraint(PointerKey lhs, AbstractOperator<PointsToSetVariable> op, PointerKey rhs) {
    // TODO Auto-generated method stub

  }

  public InstanceKey getInstanceKey(int i) {
    return instanceKeys.getMappedObject(i);
  }

  public void registerFixedSet(PointsToSetVariable val, UnarySideEffect s) {
    // TODO Auto-generated method stub

  }

  public List<InstanceKey> getInstances(IntSet set) {
    LinkedList<InstanceKey> result = new LinkedList<InstanceKey>();
    for (IntIterator it = set.intIterator(); it.hasNext();) {
      int j = it.next();
      result.add(getInstanceKey(j));
    }
    return result;
  }

  // bogus
  public boolean newFieldWrite(PointerKey lhs, UnaryOperator<PointsToSetVariable> op, PointerKey rhs, PointerKey container) {
    // TODO Auto-generated method stub
    return false;
  }

  // bogus
  public boolean newFieldRead(PointerKey lhs, UnaryOperator<PointsToSetVariable> op, PointerKey rhs, PointerKey container) {
    // TODO Auto-generated method stub
    return false;
  }

  public MutableIntSet cloneInstanceKeysForClass(IClass klass) {
    // TODO Auto-generated method stub
    return null;
  }

  public IntSet getInstanceKeysForClass(IClass klass) {
    // TODO Auto-generated method stub
    return null;
  }

  public void recordImplicitPointsToSet(PointerKey key) {
    // TODO Auto-generated method stub

  }

  public PointsToSetVariable findOrCreatePointsToSet(PointerKey key) {
    // TODO
    return null;
  }

  public OrdinalSetMapping<InstanceKey> getInstanceKeys() {
    return instanceKeys;
  }

  public int findOrCreateIndexForInstanceKey(InstanceKey key) {
    int result = instanceKeys.getMappedIndex(key);
    if (result == -1) {
      result = instanceKeys.add(key);
    }
    return result;
  }

  public void newSideEffect(AbstractOperator<PointsToSetVariable> op, PointerKey arg0, PointerKey arg1) {
    // TODO Auto-generated method stub

  }

  public void newSideEffect(AbstractOperator<PointsToSetVariable> op, PointerKey[] arg0) {
    // TODO Auto-generated method stub

  }

  public void newSideEffect(UnaryOperator<PointsToSetVariable> op, PointerKey arg0) {
    // TODO Auto-generated method stub

  }

  public Iterator<PointerKey> iteratePointerKeys() {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean isUnified(PointerKey result) {
    // TODO figure out a way to remove this method
    return false;
  }

  public void setMinEquationsForTopSort(int minEquationsForTopSort) {
    // TODO figure out a way to remove this method
  }

  public void setTopologicalGrowthFactor(double topologicalGrowthFactor) {
    // TODO figure out a way to remove this method
  }

  public void setMaxEvalBetweenTopo(int maxEvalBetweenTopo) {
    // TODO figure out a way to remove this method
  }
}
