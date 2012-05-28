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

import java.util.Map;

import kilim.Task;

import com.ibm.wala.classLoader.IField;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.actors.Pointer.PointerActor;
import com.ibm.wala.util.intset.MutableMapping;

public class Instance extends Task {
  private final InstanceKey instanceKey;
  Map<IField, PointerActor> fields;
  
  public Instance(InstanceKey instanceKey) {
    this.instanceKey =instanceKey; 
  }
}