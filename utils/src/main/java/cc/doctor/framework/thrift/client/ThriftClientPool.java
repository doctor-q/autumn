package cc.doctor.framework.thrift.client;

import cc.doctor.framework.thrift.ThriftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by doctor on 2017/7/15.
 */
public class ThriftClientPool {
    private static final Logger log = LoggerFactory.getLogger(ThriftClientPool.class);
    private String host;
    private int port;
    private Integer initSize;
    private Integer maxActive;
    private Semaphore clientSemphone;
    private List<PooledClient> activePooledClients = new LinkedList<>();
    private List<PooledClient> idlePooledClients = new LinkedList<>();

    public class PooledClient extends ThriftService.Client {
        private ThriftService.Client client;

        public PooledClient(ThriftService.Client client) {
            super(client.getInputProtocol(), client.getOutputProtocol());
            this.client = client;
        }

        public ThriftService.Client getClient() {
            return client;
        }

        public void close() {
            if (idlePooledClients.size() < initSize) {
                idlePooledClients.add(this);
            } else {
                this.client = null;
            }
            activePooledClients.remove(this);
            clientSemphone.release();
        }
    }

    public ThriftClientPool(String host, int port, Integer initSize, Integer maxActive) {
        this.host = host;
        this.port = port;
        this.initSize = initSize;
        this.maxActive = maxActive;
        clientSemphone = new Semaphore(maxActive, true);
    }

    public void init() {
        for (int i = 0; i < initSize; i++) {
            PooledClient pooledClient = new PooledClient(ThriftClientFactory.createClient(host, port));
            idlePooledClients.add(pooledClient);
        }
    }

    public PooledClient get() {
        try {
            clientSemphone.acquire();
            if (idlePooledClients.size() > 0) {
                PooledClient client = idlePooledClients.get(0);
                activePooledClients.add(client);
                idlePooledClients.remove(0);
                return client;
            } else {
                PooledClient pooledClient = new PooledClient(ThriftClientFactory.createClient(host, port));
                activePooledClients.add(pooledClient);
                return pooledClient;
            }
        } catch (InterruptedException e) {
            log.error("", e);
        }
        return null;
    }

    public void close() {
        for (PooledClient activePooledClient : activePooledClients) {
            activePooledClient.close();
        }
    }
}
