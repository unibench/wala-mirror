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

import java.util.Iterator;

import com.ibm.wala.classLoader.IField;
import com.ibm.wala.classLoader.NewSiteReference;
import com.ibm.wala.classLoader.ProgramCounter;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.propagation.FilteredPointerKey;
import com.ibm.wala.ipa.callgraph.propagation.HeapModel;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKeyFactory;
import com.ibm.wala.ipa.callgraph.propagation.PointerKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerKeyFactory;
import com.ibm.wala.ipa.callgraph.propagation.PointsToMap;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.types.TypeReference;

public class ActorsHeapModel implements HeapModel {
  
  private final PointsToMap pointsToMap;
  private final InstanceKeyFactory iKeyFactory;
  private final PointerKeyFactory pointerKeys;
  private final IClassHierarchy ch;

  public ActorsHeapModel(IClassHierarchy iClassHierarchy, PointsToMap pointsToMap, InstanceKeyFactory iKeyFactory, PointerKeyFactory pointerKeys) {
    this.ch = iClassHierarchy;
    this.pointsToMap = pointsToMap;
    this.iKeyFactory = iKeyFactory;
    this.pointerKeys = pointerKeys;
  }

  public Iterator<PointerKey> iteratePointerKeys() {
    return pointsToMap.iterateKeys();
  }

  public InstanceKey getInstanceKeyForAllocation(CGNode node, NewSiteReference allocation) {
    return iKeyFactory.getInstanceKeyForAllocation(node, allocation);
  }

  public InstanceKey getInstanceKeyForMultiNewArray(CGNode node, NewSiteReference allocation, int dim) {
    return iKeyFactory.getInstanceKeyForMultiNewArray(node, allocation, dim);
  }

  public <T> InstanceKey getInstanceKeyForConstant(TypeReference type, T S) {
    return iKeyFactory.getInstanceKeyForConstant(type, S);
  }

  public InstanceKey getInstanceKeyForPEI(CGNode node, ProgramCounter peiLoc, TypeReference type) {
    return iKeyFactory.getInstanceKeyForPEI(node, peiLoc, type);
  }

  public InstanceKey getInstanceKeyForClassObject(TypeReference type) {
    return iKeyFactory.getInstanceKeyForClassObject(type);
  }

  /*
   * @see com.ibm.wala.ipa.callgraph.propagation.PointerKeyFactory#getPointerKeyForLocal(com.ibm.detox.ipa.callgraph.CGNode, int)
   */
  public PointerKey getPointerKeyForLocal(CGNode node, int valueNumber) {
    return pointerKeys.getPointerKeyForLocal(node, valueNumber);
  }

  public FilteredPointerKey getFilteredPointerKeyForLocal(CGNode node, int valueNumber, FilteredPointerKey.TypeFilter filter) {
    return pointerKeys.getFilteredPointerKeyForLocal(node, valueNumber, filter);
  }

  /*
   * @see com.ibm.wala.ipa.callgraph.propagation.PointerKeyFactory#getPointerKeyForReturnValue(com.ibm.detox.ipa.callgraph.CGNode)
   */
  public PointerKey getPointerKeyForReturnValue(CGNode node) {
    return pointerKeys.getPointerKeyForReturnValue(node);
  }

  /*
   * @see
   * com.ibm.wala.ipa.callgraph.propagation.PointerKeyFactory#getPointerKeyForExceptionalReturnValue(com.ibm.detox.ipa.callgraph
   * .CGNode)
   */
  public PointerKey getPointerKeyForExceptionalReturnValue(CGNode node) {
    return pointerKeys.getPointerKeyForExceptionalReturnValue(node);
  }

  /*
   * @see
   * com.ibm.wala.ipa.callgraph.propagation.PointerKeyFactory#getPointerKeyForStaticField(com.ibm.wala.classLoader.FieldReference)
   */
  public PointerKey getPointerKeyForStaticField(IField f) {
    return pointerKeys.getPointerKeyForStaticField(f);
  }

  /*
   * @see
   * com.ibm.wala.ipa.callgraph.propagation.PointerKeyFactory#getPointerKeyForInstance(com.ibm.wala.ipa.callgraph.propagation.
   * InstanceKey, com.ibm.wala.classLoader.FieldReference)
   */
  public PointerKey getPointerKeyForInstanceField(InstanceKey I, IField field) {
    assert field != null;
    return pointerKeys.getPointerKeyForInstanceField(I, field);
  }

  /*
   * @see
   * com.ibm.wala.ipa.callgraph.propagation.PointerKeyFactory#getPointerKeyForArrayContents(com.ibm.wala.ipa.callgraph.propagation
   * .InstanceKey)
   */
  public PointerKey getPointerKeyForArrayContents(InstanceKey I) {
    return pointerKeys.getPointerKeyForArrayContents(I);
  }

  public IClassHierarchy getClassHierarchy() {
    return ch;
  }
}
