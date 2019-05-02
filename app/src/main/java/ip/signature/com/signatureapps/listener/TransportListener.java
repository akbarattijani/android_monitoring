package ip.signature.com.signatureapps.listener;

/**
 * @author AKBAR <dicky.akbar@dwidasa.com>
 */
public interface TransportListener {
    void onTransportDone(Object code, Object message, Object body, int id, Object... packet);
    void onTransportFail(Object code, Object message, Object body, int id, Object... packet);
}
