package me.eonexe.equinox.manager;

import me.eonexe.equinox.Equinox;
import me.eonexe.equinox.features.modules.client.HUD;
import me.eonexe.equinox.features.Notifications;

import java.util.ArrayList;

public
class NotificationManager {
    private final ArrayList < Notifications > notifications = new ArrayList ( );

    public
    void handleNotifications ( int posY ) {
        for (int i = 0; i < this.getNotifications ( ).size ( ); ++ i) {
            this.getNotifications ( ).get ( i ).onDraw ( posY );
            posY -= Equinox.moduleManager.getModuleByClass ( HUD.class ).renderer.getFontHeight ( ) + 5;
        }
    }

    public
    void addNotification ( String text , long duration ) {
        this.getNotifications ( ).add ( new Notifications ( text , duration ) );
    }

    public
    ArrayList < Notifications > getNotifications ( ) {
        return this.notifications;
    }
}
