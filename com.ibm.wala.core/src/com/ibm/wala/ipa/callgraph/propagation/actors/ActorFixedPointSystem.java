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

import com.ibm.wala.fixpoint.IFixedPointStatement;
import com.ibm.wala.fixpoint.IFixedPointSystem;
import com.ibm.wala.ipa.callgraph.propagation.PointsToSetVariable;

public class ActorFixedPointSystem implements IFixedPointSystem<PointsToSetVariable> {

  public void removeStatement(IFixedPointStatement<PointsToSetVariable> statement) {
    // TODO Auto-generated method stub

  }

  public void addStatement(IFixedPointStatement<PointsToSetVariable> statement) {
    // TODO Auto-generated method stub

  }

  public Iterator getStatements() {
    // TODO Auto-generated method stub
    return null;
  }

  public Iterator getVariables() {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean containsStatement(IFixedPointStatement<PointsToSetVariable> s) {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean containsVariable(PointsToSetVariable v) {
    // TODO Auto-generated method stub
    return false;
  }

  public Iterator getStatementsThatUse(PointsToSetVariable v) {
    // TODO Auto-generated method stub
    return null;
  }

  public Iterator getStatementsThatDef(PointsToSetVariable v) {
    // TODO Auto-generated method stub
    return null;
  }

  public int getNumberOfStatementsThatUse(PointsToSetVariable v) {
    // TODO Auto-generated method stub
    return 0;
  }

  public int getNumberOfStatementsThatDef(PointsToSetVariable v) {
    // TODO Auto-generated method stub
    return 0;
  }

  public void reorder() {
    // TODO Auto-generated method stub

  }

}
