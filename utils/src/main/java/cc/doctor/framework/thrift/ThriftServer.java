package cc.doctor.framework.thrift;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.Selector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by doctor on 2017/7/15.
 */
public class ThriftServer {
    public static final Logger log = LoggerFactory.getLogger(ThriftServer.class);
    private int port;
    private ThriftServiceHolder thriftServiceHolder = new ThriftServiceHolder();
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public ThriftServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            TProcessor tprocessor = new ThriftService.Processor<>(thriftServiceHolder);
            TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);
            serverTransport.registerSelector(Selector.open());
            TThreadedSelectorServer.Args tArgs = new TThreadedSelectorServer.Args(serverTransport);
            tArgs.processor(tprocessor);
            tArgs.transportFactory(new TFramedTransport.Factory());
            tArgs.protocolFactory(new TBinaryProtocol.Factory());
            final TServer server = new TThreadedSelectorServer(tArgs);
            log.info("Thrift Server start....");
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    server.serve();
                }
            });
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void registerMessageHandler(MessageHandler messageHandler) {
        thriftServiceHolder.registerMessageHandler(messageHandler);
    }

    public void registerMessageHandler(String name, MessageHandler messageHandler) {
        thriftServiceHolder.registerMessageHandler(name, messageHandler);
    }
}
