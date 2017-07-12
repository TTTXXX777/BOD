/*
 * Copyright © 2017 BNI, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.bod.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opendaylight.controller.ted.impl.LinkProperty;
import org.opendaylight.controller.ted.impl.LinkPropertyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopoResource {

	public static LinkProperty linkProperty;

    public static Set<Map<Short,List<Integer>>> physicalTopo = new HashSet<Map<Short,List<Integer>>>();; 
    public static Map<Short,Long> logicalTopo = new HashMap<Short,Long>();
	
    // for viewing phyVertexSetView
    public static Map<Short,List<Integer>> rru_Resource;
    public static Map<Short,Long> sw_Resource;
    public static Map<Short,List<Integer>> bbu_Resource;

	public static Set<Short> phyBBUSet;
	public static Set<Short> phyRRUSet;
	public static Set<Short> phySWSet;
	public static Set<String> nbPhyTopoShow;
	public static Set<String> nbLogTopoShow;

	private static final Logger LOG = LoggerFactory.getLogger(TopoResource.class);
	public TopoResource(LinkProperty linkProperty) {
		this.linkProperty = linkProperty;
	}

	public static boolean initTopoResource(){
		LOG.info("==========Bod TopoResource Start initTopoResource()========");
		
		if (linkProperty != null) {
			physicalTopo =  linkProperty.getPhysicalTopo();
			logicalTopo =  linkProperty.getLogicalTopo();
			sw_Resource =  linkProperty.getSWResource();
			bbu_Resource = linkProperty.getBBUResource();
			rru_Resource = linkProperty.getRRUResource();

			phyBBUSet = new HashSet<Short>();
			phyRRUSet = new HashSet<Short>();
			phySWSet = new HashSet<Short>();
			nbPhyTopoShow = new HashSet<String>();
			nbLogTopoShow = new HashSet<String>();

			for(Short key : bbu_Resource.keySet()){
				phyBBUSet.add(key);
			}
			for(Short key : rru_Resource.keySet()){
				phyRRUSet.add(key);
			}
			for(Short key : sw_Resource.keySet()){
				phySWSet.add(key);
			}	
		}
		LOG.info("===========Bod TopoResource initTopoResource() Ending==============");
		return true;
	}


	//physicalTopoShow
	public static Set<String> getPhysicalTopoShow() {
		LOG.info("==========Bod Start getPhysicalTopoResource()========");
		if (physicalTopo.size() != 6) {
			LOG.info("==========Bod not enough physicalTopo========");
		}
		
		if (physicalTopo!=null){			
			for(Map<Short,List<Integer>> physicallink: physicalTopo) {
				for(Short key: physicallink.keySet()) {
					if(key<=3 && key>=0){
						nbPhyTopoShow.add("RRU:" + key.toString() + "<--->" + physicallink.get(key).get(1).toString() + "(" + physicallink.get(key).get(2).toString() + "),");
					}
					else if(key<=7 && key>=4){							
						nbPhyTopoShow.add("BBU:" + key.toString() + "<--->" + physicallink.get(key).get(1).toString() + "(" + physicallink.get(key).get(2).toString() + "),");
					}
				}
			}	
		}
		LOG.info("===========Bod getPhysicalTopoResource() Ending==============");	
		return nbPhyTopoShow;
	}

	//logicalTopoShow
	public static Set<String> getLogicalTopoShow() {
		LOG.info("==========Bod Start getLogicalTopoResource()========");
		if (logicalTopo.size() != 2) {
			LOG.info("==========Bod not enough logicalTopo========");
		}
		if(logicalTopo != null) {
			
			for(Short key: logicalTopo.keySet()) {
				if(key<=3 && key>=0) {
					nbLogTopoShow.add("RRU:" + key.toString() + "<--->" + "BBU:" + logicalTopo.get(key).toString() + ",");
				}
			}

		}
		LOG.info("===========Bod getLogicalTopoResource() Ending==============");	      				
		return nbLogTopoShow;
	} 
}
