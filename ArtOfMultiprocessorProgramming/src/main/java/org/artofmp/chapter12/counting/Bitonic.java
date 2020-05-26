/*
 * Bitonic.java
 *
 * Created on June 9, 2006, 9:28 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter12.counting;

/**
 * Bitonic counting network.
 * @author Maurice Herlihy
 */
public class Bitonic implements Network {
  // two half-size bitonic networks
  Bitonic[]   half;
  // output i from each half-size mergers goes to layer[i]
  Merger layer;
  final int size;
  
  public Bitonic(int _size) {
    size = _size;
    layer = new Merger(size);
    if (size > 2) {
      half = new Bitonic[]{new Bitonic(size/2), new Bitonic(size/2)};
    }    
  }

  public int traverse(int input) {
    int output = 0;
    if (size > 2) {
      output = half[input % 2].traverse(input / 2);
    }
    return output + layer.traverse(output);
  }

}
