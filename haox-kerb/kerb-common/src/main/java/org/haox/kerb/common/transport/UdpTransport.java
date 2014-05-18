package org.haox.kerb.common.transport;

import java.nio.channels.DatagramChannel;

public class UdpTransport extends KrbTransport {
    private DatagramChannel channel;

    public UdpTransport(DatagramChannel channel, boolean isActive) {
        super(isActive);
        this.channel = channel;
    }

    public void setChannel(DatagramChannel channel) {
        this.channel = channel;
    }
}
