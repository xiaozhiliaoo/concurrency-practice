/*
 * Merger.java
 *
 * Created on June 9, 2006, 8:32 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter12.counting;

/**
 * Merger network component of Bitonic network.
 * @author Maurice Herlihy
 */
public class Merger implements Network {
  // two half-size merger networks
  Merger[]   half;
  // output i from each half-size mergers goes to layer[i]
  Balancer[] layer;
  final int size;
  
  public Merger(int _size) {
    size = _size;
    layer = new Balancer[size / 2];
    for (int i = 0; i < size / 2; i++) {
      layer[i] = new Balancer();
    }
    if (size > 2) {
      half = new Merger[]{new Merger(size/2), new Merger(size/2)};
    }    
  }

  public int traverse(int input) {
    int output = 0;
    if (size > 2) {
      output = half[input % 2].traverse(input / 2);
    }
    return output + layer[output].traverse(0);
  }

}
