package com.ugcs.ucs.client;

import java.util.EventObject;

import com.ugcs.ucs.proto.DomainProto.EventWrapper;

@SuppressWarnings("serial")
public class ServerNotification extends EventObject {

	private final EventWrapper event;
	private final int subscriptionId;

	public ServerNotification(Object source, EventWrapper event, int subscriptionId) {
		super(source);

		this.event = event;
		this.subscriptionId = subscriptionId;
	}

	public EventWrapper getEvent() {
		return event;
	}

	public int getSubscriptionId() {
		return subscriptionId;
	}
}
