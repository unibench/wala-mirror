/*******************************************************************************
 * Copyright (c) 2002 - 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.wala.types;

import java.io.Serializable;
import java.io.UTFDataFormatException;
import java.util.Map;

import com.ibm.wala.util.collections.HashMapFactory;
import com.ibm.wala.util.debug.Assertions;
import com.ibm.wala.util.strings.Atom;
import com.ibm.wala.util.strings.ImmutableByteArray;
import com.ibm.wala.util.strings.StringStuff;

/**
 * We've introduced this class to canonicalize Atoms that represent package names.
 * 
 * NB: All package names should use '/' and not '.' as a separator. eg. Ljava/lang/Class
 */
public final class TypeName implements Serializable {

  /* Serial version */
  private static final long serialVersionUID = -3256390509887654326L;

  /**
   * canonical mapping from TypeNameKey -> TypeName
   */
  private final static Map<TypeNameKey, TypeName> map = HashMapFactory.make();

  private static TypeName findOrCreate(TypeNameKey t) {
    TypeName result = map.get(t);
    if (result == null) {
      result = new TypeName(t);
      map.put(t, result);

    }
    return result;
  }

  /**
   * The key object holds all the information about a type name
   */
  private final TypeNameKey key;

  public static TypeName findOrCreate(ImmutableByteArray name, int start, int length) throws IllegalArgumentException {
    Atom className = Atom.findOrCreate(StringStuff.parseForClass(name, start, length));
    ImmutableByteArray p = StringStuff.parseForPackage(name, start, length);
    Atom packageName = (p == null) ? null : Atom.findOrCreate(p);
    short dim = StringStuff.parseForArrayDimensionality(name, start, length);
    boolean innermostPrimitive = StringStuff.classIsPrimitive(name, start, length);
    if (innermostPrimitive && (dim == 0)) {
      dim = -1;
    }
    TypeNameKey t = new TypeNameKey(packageName, className, dim, innermostPrimitive);
    return findOrCreate(t);
  }

  public static TypeName findOrCreate(ImmutableByteArray name) throws IllegalArgumentException {
    if (name == null) {
      throw new IllegalArgumentException("name is null");
    }
    return findOrCreate(name, 0, name.length());
  }

  public static TypeName findOrCreate(String name) throws IllegalArgumentException {
    ImmutableByteArray b = ImmutableByteArray.make(name);
    return findOrCreate(b);
  }

  public static TypeName findOrCreateClass(Atom packageName, Atom className) {
    if (packageName == null) {
      throw new IllegalArgumentException("null packageName");
    }
    if (className == null) {
      throw new IllegalArgumentException("null className");
    }
    TypeNameKey T = new TypeNameKey(packageName, className, (short) 0, false);
    return findOrCreate(T);
  }

  private static TypeName findOrCreate(Atom packageName, Atom className, short dim, boolean innermostPrimitive) {
    TypeNameKey T = new TypeNameKey(packageName, className, dim, innermostPrimitive);
    return findOrCreate(T);
  }

  /**
   * This should be the only constructor
   */
  private TypeName(TypeNameKey key) {
    this.key = key;
  }

  @Override
  public boolean equals(Object obj) {
    return this == obj;
  }

  @Override
  public int hashCode() {
    return key.hashCode();
  }

  @Override
  public String toString() {
    return key.toString();
  }

  public String toUnicodeString() {
    return key.toUnicodeString();
  }

  /**
   * @param s a String like Ljava/lang/Object
   * @return the corresponding TypeName
   * @throws IllegalArgumentException if s is null
   */
  public static TypeName string2TypeName(String s) throws IllegalArgumentException {
    if (s == null) {
      throw new IllegalArgumentException("s is null");
    }
    byte[] val = s.getBytes();

    return findOrCreate(new ImmutableByteArray(val));
  }

  public static TypeName findOrCreateClassName(String packageName, String className) {
    Atom p = Atom.findOrCreateUnicodeAtom(packageName);
    Atom c = Atom.findOrCreateUnicodeAtom(className);
    return findOrCreateClass(p, c);
  }

  /**
   * @return the name of the array element type for an array
   */
  public TypeName parseForArrayElementName() {
    short newDim = (short) (key.dim - 1);
    if (newDim == 0 && key.innermostPrimitive) {
      newDim = -1;
    }
    return findOrCreate(key.packageName, key.className, newDim, key.innermostPrimitive);
  }

  /**
   * @return a type name that represents an array of this element type
   */
  public TypeName getArrayTypeForElementType() {
    short newDim = (short) (key.dim + 1);
    if (newDim == 0) {
      newDim = 1;
    }
    return findOrCreate(key.packageName, key.className, newDim, key.innermostPrimitive);
  }

  /**
   * @return the dimensionality of the type. By convention, class types have dimensionality 0, primitves -1, and arrays the number
   *         of [ in their descriptor.
   */
  public final int getDimensionality() {
    return key.dim;
  }

  /**
   * Does 'this' refer to a class?
   */
  public final boolean isClassType() {
    return key.dim == 0;
  }

  /**
   * Does 'this' refer to an array?
   */
  public final boolean isArrayType() {
    return key.dim > 0;
  }

  /**
   * Does 'this' refer to a primitive type
   */
  public final boolean isPrimitiveType() {
    return key.dim == -1;
  }

  /**
   * Return the innermost element type reference for an array
   */
  public final TypeName getInnermostElementType() {
    short newDim = key.innermostPrimitive ? (short) -1 : 0;
    return findOrCreate(key.packageName, key.className, newDim, key.innermostPrimitive);
  }

  /**
   * A key into the dictionary; this is just like a type name, but uses value equality instead of object equality.
   */
  private final static class TypeNameKey implements Serializable {
    /**
     * The package, like "java/lang". null means the unnamed package.
     */
    private final Atom packageName;

    /**
     * The class name, like "Object" or "Z"
     */
    private final Atom className;

    /**
     * Dimensionality: -1 => primitive 0 => class >0 => array
     */
    private final short dim;

    /**
     * Is the innermost element type a primitive? TODO: encode in dim?
     */
    private final boolean innermostPrimitive;

    /**
     * This should be the only constructor
     */
    private TypeNameKey(Atom packageName, Atom className, short dim, boolean innermostPrimitive) {
      this.packageName = packageName;
      this.className = className;
      this.dim = dim;
      this.innermostPrimitive = innermostPrimitive;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof TypeNameKey) {
        TypeNameKey other = (TypeNameKey) obj;
        return className == other.className && packageName == other.packageName && dim == other.dim
            && innermostPrimitive == other.innermostPrimitive;
      } else {
        return false;
      }
    }

    /**
     * TODO: cache for efficiency?
     */
    @Override
    public int hashCode() {
      int result = className.hashCode() * 5009 + dim * 5011 + (innermostPrimitive ? 5021 : 5023);
      if (packageName != null) {
        result += packageName.hashCode();
      }
      return result;
    }

    @Override
    public String toString() {
      StringBuffer result = new StringBuffer();
      for (short i = 0; i < dim; i++) {
        result.append("[");
      }
      if (!innermostPrimitive) {
        result.append("L");
      }
      if (packageName != null) {
        result.append(packageName.toString());
        result.append("/");
      }
      result.append(className.toString());

      return result.toString();
    }

    public String toUnicodeString() {
      try {
        StringBuffer result = new StringBuffer();
        for (short i = 0; i < dim; i++) {
          result.append("[");
        }
        if (!innermostPrimitive) {
          result.append("L");
        }
        if (packageName != null) {
          result.append(packageName.toUnicodeString());
          result.append("/");
        }
        result.append(className.toUnicodeString());

        return result.toString();
      } catch (UTFDataFormatException e) {
        e.printStackTrace();
        Assertions.UNREACHABLE();
        return null;
      }
    }
  }

  /**
   * @return the Atom naming the package for this type.
   */
  public Atom getPackage() {
    return key.packageName;
  }

  /**
   * @return the Atom naming the class for this type (without package)
   */
  public Atom getClassName() {
    return key.className;
  }
}
