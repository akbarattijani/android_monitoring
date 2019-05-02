package ip.signature.com.signatureapps.database;

import android.content.ContentValues;

/**
 * @author AKBAR <dicky.akbar@dwidasa.com>
 */
public class ContentValue {

    private ContentValues content;
    public ContentValue() {
        content = new ContentValues();
    }

    public ContentValues getContent() {
        return content;
    }

    public ContentValue put(String key, String value) {
        this.content.put(key, value);
        return this;
    }

    public ContentValue put(String key, Integer value) {
        this.content.put(key, value);
        return this;
    }

    public ContentValue put(String key, Double value) {
        this.content.put(key, value);
        return this;
    }

    public ContentValue put(String key, Float value) {
        this.content.put(key, value);
        return this;
    }

    public ContentValue put(String key, Boolean value) {
        this.content.put(key, value);
        return this;
    }

    public ContentValue put(String key, Long value) {
        this.content.put(key, value);
        return this;
    }

    public ContentValue put(String key, Byte value) {
        this.content.put(key, value);
        return this;
    }

    public ContentValue put(String key, byte[] value) {
        this.content.put(key, value);
        return this;
    }
}
