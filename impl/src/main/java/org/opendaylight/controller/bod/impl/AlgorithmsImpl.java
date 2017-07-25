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

public class AlgorithmsImpl extends Thread {

	public static TopoResource topoResource;
	public static Short src;
	public static Long dst;
	public static Long olddst;

	public static Short srcRRU1;
	public static Short srcRRU2;
	public static Long dstBBU1;
	public static Long dstBBU2;


	public static Integer connectNe;
	// public static Short connectWss;
	// public static Short temp;
	public static Integer compStatus;
	public static Integer compTraffic;

	private static final Logger LOG = LoggerFactory.getLogger(AlgorithmsImpl.class);
	 
	public AlgorithmsImpl(TopoResource topoResource) {
		this.topoResource = topoResource;
	}

	public static boolean startSetupPath() {
		LOG.info("==========Bod AlgorithmsImpl Start SetupPath==========");
		srcRRU1 = 1;
		srcRRU2 = 2;
		dstBBU1 = 4l;
		dstBBU2 = 5l;
		connectNe = 12;
		// temp = 10;
		// for(Short key:topoResource.phyWSSSet) {
		// 	if(key == temp) {
		// 		connectWss = key;
		// 		break;
		// 	}		
		// }
		return true;
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

	public static boolean startComp() {
		LOG.info("==========Bod AlgorithmsImpl Start SetComp==========");
		src = 2;
		for(Short key: topoResource.rru_Resource.keySet()) {
			if(key == src) {
				compStatus = topoResource.rru_Resource.get(src).get(0);
				compTraffic = topoResource.rru_Resource.get(src).get(1);
				break;
			}
		}

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
