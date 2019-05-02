package ip.signature.com.signatureapps.listener;

public class AlertDialogListener {
    public interface TwoButton {
        void onClickButtonLeft();
        void onClickButtonRight();
    }

    public interface OneButton {
        void onClickButton();
    }
}
