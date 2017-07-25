 /*
 * Copyright © 2017 BNI, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.bod.impl;

//add for ted
import org.opendaylight.controller.ted.impl.LinkProperty;
import org.opendaylight.controller.ted.impl.LinkPropertyService;

import org.opendaylight.controller.LPM.impl.SetupManagerService;

import java.util.concurrent.Future;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bod.rev150105.BodService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bod.rev150105.SendCommandInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bod.rev150105.SendCommandOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bod.rev150105.SendCommandOutputBuilder;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;


import org.opendaylight.controller.bod.impl.AlgorithmsImpl;

import java.util.*;
import java.lang.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BodImpl implements BodService {

	private static final Logger LOG = LoggerFactory.getLogger(BodImpl.class);
	
	private final LinkPropertyService linkPropertyService;
	private final SetupManagerService setupManagerService;
	public TopoResource topoResource;

	/**
	 * @param linkPropertyService
	 * @param setupManagerService
	 */
	public BodImpl(SetupManagerService setupManagerService, LinkPropertyService linkPropertyService) {
		this.linkPropertyService = linkPropertyService;
		this.setupManagerService = setupManagerService;
	}

	@Override
	public Future<RpcResult<SendCommandOutput>> sendCommand(SendCommandInput input) {
		SendCommandOutputBuilder sendCommandBuilder = new SendCommandOutputBuilder();

		TopoResource topoResource = new TopoResource(linkPropertyService);
		//PhysicalTopo and LOgicalTopo 
		if (topoResource.initTopoResource()){

			if(input.getOperation().equals("PhysicalTopo")){
				Set<String> nbPhyTopoShow = topoResource.getPhysicalTopoShow();
				String nima = new String();
				int i = 1;
				for(String s : nbPhyTopoShow){
					nima  += "<" + i++ +">" + s ;
				}
 				sendCommandBuilder.setResult("PhysicalTopo: "+ nima); 
			}

			if(input.getOperation().equals("LogicalTopo")){
				Set<String> nbLogTopoShow = topoResource.getLogicalTopoShow();
				String nima2 = new String();
				int j = 1;
				for(String s : nbLogTopoShow) {
					nima2 += "<" + j++ + ">" + s;
				}
				sendCommandBuilder.setResult("LogicalTopo: "+ nima2);
			}		    	
		}

		if (input.getOperation().equals("BBUConv")) {
			AlgorithmsImpl al = new AlgorithmsImpl(topoResource);
			if (al.startBBUConv()) {
				if (setupManagerService.setupPath(al.src, al.olddst, al.dst, al.connectNe)) {
					Set<String> nbLogTopoShow = topoResource.getLogicalTopoShow();
					String nima = new String();
					int j = 1;
					for(String s : nbLogTopoShow) {
						nima += "<" + j++ + ">" + s;
					}
					sendCommandBuilder.setResult("The LogicalTopo after BBUConv: "+ nima);
				}
			}
		}

		if (input.getOperation().equals("Comp")) {
			AlgorithmsImpl al = new AlgorithmsImpl(topoResource);
			if (al.startComp() && al.compStatus.equals(1)) {
				if (setupManagerService.setupPath(al.src, al.olddst, al.dst, al.connectNe)) {
					Set<String> nbLogTopoShow = topoResource.getLogicalTopoShow();
					String nima = new String();
					int j = 1;
					for(String s : nbLogTopoShow) {
						nima += "<" + j++ + ">" + s;
					}
					sendCommandBuilder.setResult("The LogicalTopo after Comp: "+ nima);
				}
			}
		}

		if (input.getOperation().equals("startSetupPath")) {
			AlgorithmsImpl al = new AlgorithmsImpl(topoResource);
			if (al.startSetupPath()) {
				if (setupManagerService.startSetupPath(input.getSrcRRU1(),input.getSrcRRU2(), input.getDstBBU1(), input.getDstBBU2(), al.connectNe)) {
					Set<String> nbLogTopoShow = topoResource.getLogicalTopoShow();
					String nima = new String();
					int j = 1;
					for(String s : nbLogTopoShow) {
						nima += "<" + j++ + ">" + s;
					}
					sendCommandBuilder.setResult("The LogicalTopo after startSetupPath: "+ nima);
				}
			}
		}

		return RpcResultBuilder.success(sendCommandBuilder.build()).buildFuture();
	}

}