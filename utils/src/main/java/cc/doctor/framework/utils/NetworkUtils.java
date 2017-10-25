package cc.doctor.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by doctor on 2017/3/15.
 */
public class NetworkUtils {
    private static final Logger log = LoggerFactory.getLogger(NetworkUtils.class);

    public static InetAddress getOneUnLoopHost() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (!networkInterface.isLoopback() && !networkInterface.isPointToPoint() && !networkInterface.isVirtual() && networkInterface.isUp()) {
                    List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
                    for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                        if (interfaceAddress != null && interfaceAddress.getAddress() instanceof Inet4Address) {
                            return interfaceAddress.getAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            log.error("", e);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(NetworkUtils.getOneUnLoopHost());
    }
}
