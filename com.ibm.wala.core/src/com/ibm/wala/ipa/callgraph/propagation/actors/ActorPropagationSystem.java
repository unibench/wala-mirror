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
import com.ibm.wala.ipa.callgraph.propagation.FilteredPointerKey;
import com.ibm.wala.ipa.callgraph.propagation.IPropagationSystem;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKeyFactory;
import com.ibm.wala.ipa.callgraph.propagation.LocalPointerKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerAnalysis;
import com.ibm.wala.ipa.callgraph.propagation.PointerKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerKeyFactory;
import com.ibm.wala.ipa.callgraph.propagation.PointsToMap;
import com.ibm.wala.ipa.callgraph.propagation.PointsToSetVariable;
import com.ibm.wala.ipa.callgraph.propagation.PropagationCallGraphBuilder;
import com.ibm.wala.ipa.callgraph.propagation.PropagationSystem;
import com.ibm.wala.ipa.callgraph.propagation.UnarySideEffect;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.MonitorUtil.IProgressMonitor;
import com.ibm.wala.util.debug.Assertions;
import com.ibm.wala.util.intset.IntIterator;
import com.ibm.wala.util.intset.IntSet;
import com.ibm.wala.util.intset.MutableIntSet;
import com.ibm.wala.util.intset.MutableMapping;
import com.ibm.wala.util.intset.OrdinalSetMapping;

public class ActorPropagationSystem implements IPropagationSystem {

  /**
   * bijection from InstanceKey <=>Integer
   */
  private final MutableMapping<InstanceKey> instanceKeys = MutableMapping.make();
  /**
   * object that tracks points-to sets
   */
  protected final PointsToMap pointsToMap = new PointsToMap();
  
//  Mailbox<String> 

  public IFixedPointSystem<PointsToSetVariable> getFixedPointSystem() {
    // TODO will have to figure out what we consider our fixed point system
    return null;
  }

  public boolean solve(IProgressMonitor monitor) throws CancelException {
    // TODO Auto-generated method stub
    
    // spawn the finisher actor
    
//    while(true) { // wait for the mailbox  
//      ... do what the message wants
//    }
    
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
  
  
  // THE CONSTRAINT STUFF

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

  public void registerFixedSet(PointsToSetVariable val, UnarySideEffect s) {
    // TODO Auto-generated method stub

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
  
  public void newSideEffect(AbstractOperator<PointsToSetVariable> op, PointerKey arg0, PointerKey arg1) {
    // TODO Auto-generated method stub

  }

  public void newSideEffect(AbstractOperator<PointsToSetVariable> op, PointerKey[] arg0) {
    // TODO Auto-generated method stub

  }

  public void newSideEffect(UnaryOperator<PointsToSetVariable> op, PointerKey arg0) {
    // TODO Auto-generated method stub
  }
  
  
  
  
  
  
  public InstanceKey getInstanceKey(int i) {
    return instanceKeys.getMappedObject(i);
  }

  public List<InstanceKey> getInstances(IntSet set) {
    LinkedList<InstanceKey> result = new LinkedList<InstanceKey>();
    for (IntIterator it = set.intIterator(); it.hasNext();) {
      int j = it.next();
      result.add(getInstanceKey(j));
    }
    return result;
  }
  
  /**
   * record that a particular points-to-set is represented implicitly.
   */
  public void recordImplicitPointsToSet(PointerKey key) {
    if (key == null) {
      throw new IllegalArgumentException("null key");
    }
    if (key instanceof LocalPointerKey) {
      LocalPointerKey lpk = (LocalPointerKey) key;
      if (lpk.isParameter()) {
        System.err.println(lpk);
        System.err.println("Constant? " + lpk.getNode().getIR().getSymbolTable().isConstant(lpk.getValueNumber()));
        System.err.println(lpk.getNode().getIR());
        Assertions.UNREACHABLE("How can parameter be implicit?");
      }
    }
    pointsToMap.recordImplicit(key);
  }

  /**
   * If key is unified, returns the representative
   * 
   * @param key
   * @return the dataflow variable that tracks the points-to set for key
   */
  public PointsToSetVariable findOrCreatePointsToSet(PointerKey key) {

    if (key == null) {
      throw new IllegalArgumentException("null key");
    }

    if (pointsToMap.isImplicit(key)) {
      System.err.println("Did not expect to findOrCreatePointsToSet for implicitly represented PointerKey");
      System.err.println(key);
      Assertions.UNREACHABLE();
    }
    PointsToSetVariable result = pointsToMap.getPointsToSet(key);
    if (result == null) {
      result = new PointsToSetVariable(key);
      pointsToMap.put(key, result);
      // TODO add our actor stuff
    } else {
      // check that the filter for this variable remains unique
      if (!pointsToMap.isUnified(key) && key instanceof FilteredPointerKey) {
        PointerKey pk = result.getPointerKey();
        if (!(pk instanceof FilteredPointerKey)) {
          // add a filter for all future evaluations.
          // this is tricky, but the logic is OK .. any constraints that need
          // the filter will see it ...
          // CALLERS MUST BE EXTRA CAREFUL WHEN DEALING WITH UNIFICATION!
          result.setPointerKey(key);
          pk = key;
        }
        FilteredPointerKey fpk = (FilteredPointerKey) pk;
        if (fpk.getTypeFilter() == null) {
          Assertions.UNREACHABLE("fpk.getTypeFilter() is null");
        }
        if (!fpk.getTypeFilter().equals(((FilteredPointerKey) key).getTypeFilter())) {
          Assertions.UNREACHABLE("Cannot use filter " + ((FilteredPointerKey) key).getTypeFilter() + " for " + key
              + ": previously created different filter " + fpk.getTypeFilter());
        }
      }
    }
    return result;
  }

  public int findOrCreateIndexForInstanceKey(InstanceKey key) {
    int result = instanceKeys.getMappedIndex(key);
    // TODO create instance here
    if (result == -1) {
      result = instanceKeys.add(key);
    }
    return result;
  }

  /*
   * @see com.ibm.wala.ipa.callgraph.propagation.HeapModel#iteratePointerKeys()
   */
  public Iterator<PointerKey> iteratePointerKeys() {
    return pointsToMap.iterateKeys();
  }

  public boolean isUnified(PointerKey result) {
    return pointsToMap.isUnified(result);
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

  public int getMappedIndexForInstanceKey(InstanceKey ik) {
    return instanceKeys.getMappedIndex(ik);
  }
}
