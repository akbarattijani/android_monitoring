package ip.signature.com.signatureapps.transport.body;


/**
 * @author AKBAR <dicky.akbar@dwidasa.com>
 */

public class BodyBuilder {
    private Object body;
    private String mediaType = "";
    private String boundary = "";

    public BodyBuilder setBody(Object body) {
        this.body = body;
        return this;
    }

    public BodyBuilder setMediaType(String mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    /**
     * Set Boundary only use for multipart request
     *
     * @param boundary "===" + System.currentTimeMillis() + "==="
     * @return this
     */
    public BodyBuilder setBoundary(String boundary) {
        this.boundary = boundary;
        return this;
    }

    public Object getBody() {
        return this.body;
    }

    public String getMediaType() {
        return this.mediaType;
    }

    /**
     * Get only when multipart request
     *
     * @return boundary value
     */
    public String getBoundary() {
        return boundary;
    }
}
