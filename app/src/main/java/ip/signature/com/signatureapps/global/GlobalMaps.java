package ip.signature.com.signatureapps.global;

import ip.signature.com.signatureapps.fragment.MenuFragment;

public class GlobalMaps {
    private static MenuFragment menuFragment;

    public static MenuFragment getMenuFragment() {
        return menuFragment;
    }

    public static void setMenuFragment(MenuFragment menuFragment) {
        GlobalMaps.menuFragment = menuFragment;
    }
}
