/*
 * Copyright © 2017 BNI, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.bod.impl; 

import java.util.*;
import java.lang.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlgorithmsImpl extends Thread{

	public static TopoResource topoResource;
	public static Short src;
	public static Long dst;
	public static Long olddst;
	public static Integer connectNe;

	private static final Logger LOG = LoggerFactory.getLogger(AlgorithmsImpl.class);
	//comp module 
	public AlgorithmsImpl(TopoResource topoResource) {
		this.topoResource = topoResource;
	}
	public static boolean startBBUConv(){
		LOG.info("==========Bod AlgorithmsImpl Start SetBBUConv==========");
		src = 2;
		olddst = topoResource.logicalTopo.get(src);
		for(Map<Short,List<Integer>> physicallink: topoResource.physicalTopo) {
			for(Short key: physicallink.keySet()) {
				if (key == src) {
					connectNe = physicallink.get(src).get(1);
					break; 
				}
			}
		}
		dst = 4l;
		return true;
	}
}
