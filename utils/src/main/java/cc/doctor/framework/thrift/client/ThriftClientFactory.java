package cc.doctor.framework.thrift.client;

import cc.doctor.framework.thrift.Request;
import cc.doctor.framework.thrift.Response;
import cc.doctor.framework.thrift.ThriftService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * Created by doctor on 2017/7/15.
 */
public class ThriftClientFactory {
    // todo support more protocol
    public static ThriftService.Client createClient(String host, int port) {
        if (host == null) {
            return null;
        }
        TTransport transport = new TFramedTransport(new TSocket(host, port));
        try {
            transport.open();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        TProtocol protocol = new TBinaryProtocol(transport);
        return new ThriftService.Client(protocol);
    }
}
