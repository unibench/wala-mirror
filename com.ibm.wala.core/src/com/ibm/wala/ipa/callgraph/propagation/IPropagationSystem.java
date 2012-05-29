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
package com.ibm.wala.ipa.callgraph.propagation;

import java.util.Iterator;
import java.util.List;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.fixpoint.AbstractOperator;
import com.ibm.wala.fixpoint.IFixedPointSolver;
import com.ibm.wala.fixpoint.UnaryOperator;
import com.ibm.wala.util.intset.IntSet;
import com.ibm.wala.util.intset.MutableIntSet;
import com.ibm.wala.util.intset.MutableMapping;
import com.ibm.wala.util.intset.OrdinalSetMapping;

public interface IPropagationSystem extends IFixedPointSolver<PointsToSetVariable> { 

  // bad design
  void setMinEquationsForTopSort(int minEquationsForTopSort);

  // bad design
  void setTopologicalGrowthFactor(double topologicalGrowthFactor);

  // bad design
  void setMaxEvalBetweenTopo(int maxEvalBetweenTopo);

  // good 
  PointerAnalysis extractPointerAnalysis(PropagationCallGraphBuilder propagationCallGraphBuilder);

  public boolean newConstraint(PointerKey lhs, UnaryOperator<PointsToSetVariable> op, PointerKey rhs);
  boolean newConstraint(PointerKey exceptionVar, InstanceKey e);
  void newConstraint(PointerKey lhs, AbstractOperator<PointsToSetVariable> op, PointerKey rhs);
  
  InstanceKey getInstanceKey(int i);
  void registerFixedSet(PointsToSetVariable val, UnarySideEffect s);

  List<InstanceKey> getInstances(IntSet set);
  
  public boolean newFieldWrite(PointerKey lhs, UnaryOperator<PointsToSetVariable> op, PointerKey rhs, PointerKey container);
  public boolean newFieldRead(PointerKey lhs, UnaryOperator<PointsToSetVariable> op, PointerKey rhs, PointerKey container);
  
  MutableIntSet cloneInstanceKeysForClass(IClass klass);
  
  public IntSet getInstanceKeysForClass(IClass klass);
  
  public void recordImplicitPointsToSet(PointerKey key);
  
  public PointsToSetVariable findOrCreatePointsToSet(PointerKey key);
  
//  public OrdinalSetMapping<InstanceKey> getInstanceKeys();
  public int getMappedIndexForInstanceKey(InstanceKey ik);
  
  public int findOrCreateIndexForInstanceKey(InstanceKey key);
  
  public void newSideEffect(AbstractOperator<PointsToSetVariable> op, PointerKey arg0, PointerKey arg1);
  public void newSideEffect(AbstractOperator<PointsToSetVariable> op, PointerKey[] arg0);
  public void newSideEffect(UnaryOperator<PointsToSetVariable> op, PointerKey arg0);
  
  public Iterator<PointerKey> iteratePointerKeys();
  
  
  // bad design
  public boolean isUnified(PointerKey result);

  // debug aids
  void setVerboseInterval(int verboseInterval);
  void setPeriodicMaintainInterval(int periodicMaintainInterval);
  
}
