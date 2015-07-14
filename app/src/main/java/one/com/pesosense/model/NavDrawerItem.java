package one.com.pesosense.model;

/**
 * Created by mykelneds on 6/19/15.
 */

public class NavDrawerItem {
    private boolean showNotify;
    private String title;
    private int icon;
    private int selectedIcon;

    public NavDrawerItem() {

    }

    public NavDrawerItem(boolean showNotify, String title, int icon, int selectedIcon) {
        this.showNotify = showNotify;
        this.title = title;
        this.icon = icon;
        this.selectedIcon = selectedIcon;

    }

    //

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(int selectedIcon) {
        this.selectedIcon = selectedIcon;
    }
}
