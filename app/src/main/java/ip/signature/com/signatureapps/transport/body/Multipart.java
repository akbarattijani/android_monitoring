package ip.signature.com.signatureapps.transport.body;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AKBAR <dicky.akbar@dwidasa.com>
 */

public class Multipart {
    private Map<String, Object> params;

    public Multipart() {
        params = new HashMap<>();
    }

    public Multipart addForm(String key, String value) {
        params.put(key, value);
        return this;
    }

    public Multipart addFile(String key, File file) {
        params.put(key, file);
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
