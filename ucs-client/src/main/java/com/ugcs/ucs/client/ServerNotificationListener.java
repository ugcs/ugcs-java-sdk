package com.ugcs.ucs.client;

import java.util.EventListener;

public interface ServerNotificationListener extends EventListener {

	void notificationReceived(ServerNotification event);
}
