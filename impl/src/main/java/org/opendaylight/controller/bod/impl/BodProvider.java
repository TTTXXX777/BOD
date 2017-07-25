/*
 * Copyright © 2017 BNI, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.bod.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.sal.binding.api.RpcProviderRegistry;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;

import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.setbbuconfig.rev170705.PacketSetBbuConfigService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.setrruconfig.rev170705.PacketSetRruConfigService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.setswconfig.rev170705.PacketSetSwConfigService;

import org.opendaylight.controller.LPM.impl.SetupManagerService;
import org.opendaylight.controller.LPM.impl.SendPacket;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.RpcRegistration;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bod.rev150105.BodService;

import org.opendaylight.controller.ted.impl.LinkProperty;
import org.opendaylight.controller.ted.impl.LinkPropertyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class BodProvider {

    private static final Logger LOG = LoggerFactory.getLogger(BodProvider.class);

    //md-sal service provider
    private final DataBroker dataBroker;
    private final RpcProviderRegistry rpcRegistry;
    private final NotificationPublishService notificationService;
    private final LinkPropertyService linkPropertyService;
    private final SetupManagerService setupManagerService;

    //BodService 
    private RpcRegistration<BodService> bodService = null;

    public BodProvider(final DataBroker dataBroker, 
                final RpcProviderRegistry rpcRegistry,
                final NotificationPublishService notificationService,
                final LinkPropertyService linkPropertyService,
                final SetupManagerService setupManagerService) {
        this.dataBroker = dataBroker;
        this.rpcRegistry = rpcRegistry;
        this.notificationService = notificationService;
        this.linkPropertyService = linkPropertyService;
        this.setupManagerService = setupManagerService;
    }

    /**
     * Method called when the blueprint container is created.
     */
    public void init() {
        LOG.info("BodProvider Session Initiated");
        
        SendPacket.packetSetBbuConfigService = rpcRegistry.getRpcService(PacketSetBbuConfigService.class);
        SendPacket.packetSetRruConfigService = rpcRegistry.getRpcService(PacketSetRruConfigService.class);
        SendPacket.packetSetSwConfigService = rpcRegistry.getRpcService(PacketSetSwConfigService.class);
        
        bodService = rpcRegistry.addRpcImplementation(BodService.class, new BodImpl(setupManagerService, linkPropertyService));

    }

    /**
     * Method called when the blueprint container is destroyed.
     */
    public void close() {
        LOG.info("BodProvider Closed");
        
        if (bodService != null) {
            bodService.close();
        }
    }
}
