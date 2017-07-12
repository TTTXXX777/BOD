 /*
 * Copyright © 2017 BNI, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.bod.impl;

import org.osgi.framework.Bundle; //added for communications between bundles;
import org.osgi.framework.BundleContext; //added for communications between bundles;
import org.osgi.framework.BundleReference; //added for communications between bundles;
import org.osgi.framework.FrameworkUtil;//added for communications between bundles;
import org.osgi.framework.ServiceReference; //added for communications between bundles;

//add for ted
import org.opendaylight.controller.ted.impl.LinkProperty;
import org.opendaylight.controller.ted.impl.LinkPropertyService;

import java.util.concurrent.Future;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bod.rev150105.BodService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bod.rev150105.SendCommandInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bod.rev150105.SendCommandOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bod.rev150105.SendCommandOutputBuilder;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;


//add for set-config

import org.opendaylight.controller.bod.impl.InstanceIdentifierUtils;
import org.opendaylight.controller.bod.impl.AlgorithmsImpl;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.node.NodeConnector;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import java.util.*;
import java.lang.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BodImpl implements BodService {

	private static final Logger LOG = LoggerFactory.getLogger(BodImpl.class);
	
	private final LinkPropertyService linkPropertyService;
	public TopoResource topoResource;

	/**
	 * 
	 * @param linkPropertyService
	 */
	public BodImpl(LinkPropertyService linkPropertyService) {
		//this.packetSetConfigService = packetSetConfigService;
		this.linkPropertyService = linkPropertyService;
	}

	// private static BundleContext getBundleContext() {
 //        ClassLoader tlcl = Thread.currentThread().getContextClassLoader();
 //        Bundle bundle = null;

 //        if (tlcl instanceof BundleReference) {
 //            bundle = ((BundleReference) tlcl).getBundle();
 //        } else {
 //            LOG.info("Unable to determine the bundle context based on thread context classloader.");
 //            bundle = FrameworkUtil.getBundle(BodImpl.class);
 //        }
 //        return (bundle == null ? null : bundle.getBundleContext());
 //    }

	@Override
	public Future<RpcResult<SendCommandOutput>> sendCommand(SendCommandInput input) {
		SendCommandOutputBuilder sendCommandBuilder = new SendCommandOutputBuilder();
		LinkProperty linkProperty = new LinkProperty();

	
		TopoResource topoResource = new TopoResource(linkProperty);
		//getPhysicalTopoShow
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
				SetupManager sm = new SetupManager();
				if (sm.setupPath(al.src, al.olddst, al.dst, al.connectNe)) {
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
		return RpcResultBuilder.success(sendCommandBuilder.build()).buildFuture();
	}

	// ===================================================================================================
	
	/*private void sendSetconfig(SwitchConfigFlag flags, java.lang.Integer sendlen) {
        LOG.info("==========TIANXIANG sendSetconfig begin=====================");

        PacketSetConfigInputBuilder setConfigBuilder = new PacketSetConfigInputBuilder();
        // BundleContext ctx = getBundleContext();
        // ServiceReference linkPropertyServiceReference = ctx.getServiceReference(LinkPropertyService.class);
        //LinkProperty linkProperty = (LinkProperty)ctx.getService(linkPropertyServiceReference);
        LinkProperty linkProperty = new LinkProperty();  
        NodeConnectorRef nodeRef = linkProperty.getIngress();
        InstanceIdentifier<Node> egressNodePath = InstanceIdentifierUtils.getNodePath(nodeRef.getValue()); 
        //Construct input for RPC call to packet processing service
   
        setConfigBuilder.setNode(new NodeRef(egressNodePath));
        setConfigBuilder.setFlags(flags);
        setConfigBuilder.setMissSendLen(sendlen);
        packetSetConfigService.packetSetConfig(setConfigBuilder.build());

        LOG.info("==============TIANXIANG sendbulid=================={}"+setConfigBuilder);
        LOG.info("==============TIANXIANG sendSetconfig end====================");
	}
	*/
}