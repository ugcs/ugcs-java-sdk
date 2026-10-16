The server tells a client about changes as **events**: new telemetry, an object created or
changed, a lock taken, the commands of a vehicle... A client gets the events it subscribes to.

## How it works

1. Register a listener on the `Client`. It receives the events of all your subscriptions; it can
   be added at any time, also before `connect()`, and stays across reconnections.
2. Subscribe with a [SubscribeEventRequest](API-Events#subscribeeventrequest): its
   [EventSubscriptionWrapper](Objects-Events#eventsubscriptionwrapper) has one subscription set.
   The response has the `subscriptionId`.
3. Each event comes as a [ServerNotification](#servernotification), with the subscription id and an
   [EventWrapper](Objects-Events#eventwrapper) in which one event is set.
4. End the subscription with [UnsubscribeEventRequest](API-Events#unsubscribeeventrequest).

```java
client.addNotificationListener(event -> {
    if (!event.getEvent().hasObjectModificationEvent())
        return;
    ObjectModificationEvent change = event.getEvent().getObjectModificationEvent();
    System.out.println(change.getModificationType() + " " + change.getObjectType()
            + " " + change.getObjectId());
});
SubscribeEventResponse response = client.execute(SubscribeEventRequest.newBuilder()
        .setClientId(session.getClientId())
        .setSubscription(EventSubscriptionWrapper.newBuilder()
                .setObjectModificationSubscription(ObjectModificationSubscription.newBuilder()
                        .setObjectType("Route")))
        .build());
int subscriptionId = response.getSubscriptionId();
// ...
session.unsubscribe(subscriptionId);
```

(`session` is the `Session` of [Connecting and Sessions](Connecting-and-Sessions#requests-clientsession-has-no-method-for).)

## What can be subscribed to

| Subscription | Its fields | Event |
|---|---|---|
| [TelemetrySubscription](Objects-Events#telemetrysubscription) | `vehicle` | [TelemetryEvent](Objects-Events#telemetryevent): a vehicle and a batch of its telemetry. See [[Telemetry]]. |
| [TelemetryBatchSubscription](Objects-Events#telemetrybatchsubscription) | `pollingPeriodMilliseconds`, `vehicles` | [TelemetryBatchEvent](Objects-Events#telemetrybatchevent): the telemetry of several vehicles. |
| [TelemetryRemovalSubscription](Objects-Events#telemetryremovalsubscription) | `vehicle` | [TelemetryRemovalEvent](Objects-Events#telemetryremovalevent): telemetry of a vehicle deleted, with the time range. |
| [ObjectModificationSubscription](Objects-Events#objectmodificationsubscription) | `objectType`, `objectId` | [ObjectModificationEvent](Objects-Events#objectmodificationevent): an object created, changed or deleted ([ModificationType](Enums#modificationtype)), with its type, id and the object. |
| [LockSubscription](Objects-Events#locksubscription) | `objectType`, `objectId` | [LockEvent](Objects-Events#lockevent): an object's lock, with the [Lock](Objects-Common#lock). |
| [CommandSubscription](Objects-Events#commandsubscription) | `vehicle` | [CommandEvent](Objects-Events#commandevent): the commands of a vehicle. See [[Vehicle Commands]]. |
| [VehicleTrackSubscription](Objects-Events#vehicletracksubscription) | `vehicle` | [VehicleTrackEvent](Objects-Events#vehicletrackevent): a vehicle's track. |
| [EnvironmentSubscription](Objects-Events#environmentsubscription) | | [EnvironmentEvent](Objects-Events#environmentevent): no-fly zones, elevation or map features updated in an area ([EnvironmentEventType](Enums#environmenteventtype)). |
| [ServerProgressSubscription](Objects-Events#serverprogresssubscription) | | [ServerProgressEvent](Objects-Events#serverprogressevent): the progress of the server's long operations. |
| [AdsbIndicatorSubscription](Objects-Events#adsbindicatorsubscription) | | [AdsbIndicatorEvent](Objects-Events#adsbindicatorevent): the ADS-B indicator on or off. |

## `ServerNotification`

What a listener receives:

| Method | |
|---|---|
| `getSubscriptionId()` | The subscription the event is for. With several subscriptions, compare it with the ids their responses returned. |
| `getEvent()` | The [EventWrapper](Objects-Events#eventwrapper). Check which event is set with `hasTelemetryEvent()` and the like. |
| `getSource()` | The `Client`. |

## Listeners

- Listeners are called on a thread of the SDK, for every event of every subscription. Keep them
  short, and hand longer work - and new requests to the server - to a thread of your own.
- `removeNotificationListener(listener)` removes one.
