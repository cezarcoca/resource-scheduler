package com.jpm.scheduler;

import com.external.Gateway;

/**
 * @author ccoca
 * 
 *         Interface that should be implemented by all Gateway factory classes.
 *
 */
public interface GatewayFactory {

    Gateway getGateway();
}
