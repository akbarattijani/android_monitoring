package ip.signature.com.signatureapps.listener;

/**
 * @author AKBAR <dicky.akbar@dwidasa.com>
 */

public interface ScheduleListener {
    boolean onRun(int requestCode);
    void onDone(int requestCode);
    void onFail(int requestCode);
}
