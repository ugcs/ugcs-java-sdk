The UgCS server receives the telemetry of the connected vehicles, records it, and sends it on to
the clients which subscribe to it.

## A telemetry value

Each value is a [Telemetry](Objects-Telemetry-and-Logs#telemetry):

| Field | |
|---|---|
| `telemetryField` | Which value it is: a [TelemetryField](Objects-Telemetry-and-Logs#telemetryfield) with its `code`, `semantic` ([Semantic](Enums#semantic), e.g. `S_LATITUDE`, `S_ALTITUDE_AMSL`, `S_GROUND_SPEED`, `S_VOLTAGE`), and the `subsystem` and `subsystemId` of the vehicle it comes from. |
| `value` | A [Value](Objects-Commands#value): one of its typed fields is set. |
| `time` | When it was measured, in milliseconds since 1970-01-01 UTC. |

A field is identified by its subsystem, subsystem id and code together: the flight controller
(`S_FLIGHT_CONTROLLER`), a camera (`S_CAMERA`), a gimbal, a payload... have their own fields.
[IsTelemetryFieldRegisteredRequest](API-Telemetry#istelemetryfieldregisteredrequest) tells whether
a vehicle has a field.

## Live telemetry

Register a listener, then subscribe. `ClientSession.subscribeTelemetryEvent()` subscribes to the
telemetry of all vehicles:

```java
client.addNotificationListener(event -> {
    if (!event.getEvent().hasTelemetryEvent())
        return;
    TelemetryEvent telemetry = event.getEvent().getTelemetryEvent();
    for (Telemetry t : telemetry.getTelemetryList())
        System.out.println(telemetry.getVehicle().getName() + " "
                + t.getTelemetryField().getCode() + " = " + t.getValue());
});
int subscriptionId = session.subscribeTelemetryEvent();
// ...
session.unsubscribe(subscriptionId);
```

Each [TelemetryEvent](Objects-Events#telemetryevent) has the vehicle and a batch of values: only
some of the vehicle's fields - the position is not in every batch. Keep the latest value of each
field.

For the telemetry of one vehicle, subscribe with the vehicle in the
[TelemetrySubscription](Objects-Events#telemetrysubscription):

```java
SubscribeEventRequest request = SubscribeEventRequest.newBuilder()
        .setClientId(session.getClientId())
        .setSubscription(EventSubscriptionWrapper.newBuilder()
                .setTelemetrySubscription(TelemetrySubscription.newBuilder()
                        .setVehicle(vehicle)))
        .build();
SubscribeEventResponse response = client.execute(request);
int subscriptionId = response.getSubscriptionId();
```

A [TelemetryBatchSubscription](Objects-Events#telemetrybatchsubscription), with its
`pollingPeriodMilliseconds` and `vehicles`, delivers
[TelemetryBatchEvent](Objects-Events#telemetrybatchevent)s which group the telemetry of several
vehicles.

## The latest values

[GetTelemetrySnapshotRequest](API-Telemetry#gettelemetrysnapshotrequest) returns the latest
telemetry of the vehicles you give, without a subscription:

```java
GetTelemetrySnapshotRequest request = GetTelemetrySnapshotRequest.newBuilder()
        .setClientId(session.getClientId())
        .addVehicles(vehicle)
        .build();
GetTelemetrySnapshotResponse response = client.execute(request);
for (TelemetrySnapshot snapshot : response.getSnapshotsList())
    for (Telemetry t : snapshot.getTelemetryList())
        System.out.println(t.getTelemetryField().getCode() + " = " + t.getValue());
```

## Recorded telemetry

| Request | What it does |
|---|---|
| [GetTelemetryRequest](API-Telemetry#gettelemetryrequest) | A vehicle's telemetry from `fromTime` (to `toTime`), at most `limit` values (7200 by default). |
| [CountTelemetryRequest](API-Telemetry#counttelemetryrequest) | How many values there are in a time range. |
| [ExportTelemetryRequest](API-Telemetry#exporttelemetryrequest) | A vehicle's telemetry as a file, in a [TelemetryFormat](Enums#telemetryformat): `TF_TLM` (see [.tlm file format](.tlm-file-format)), `TF_CSV` or `TF_KML`. The range is `from` (included) to `to` (not included). |
| [ImportTelemetryRequest](API-Telemetry#importtelemetryrequest) | Imports telemetry in the [.tlm format](.tlm-file-format); the response lists the vehicles it was for. |
| [DeleteTelemetryRequest](API-Telemetry#deletetelemetryrequest) | Deletes a vehicle's telemetry in a time range. |
| [GetVehicleTracksRequest](API-Tracks-and-Logs#getvehicletracksrequest) | The tracks of vehicles, newest first: at most `limit` (7 by default). |
| [GetVehicleLogRequest](API-Tracks-and-Logs#getvehiclelogrequest) | The log messages of vehicles, with a `level` ([SeverityLevel](Enums#severitylevel), `SL_INFORMATIONAL` by default) and at most `limit` (15 by default). |

`ExportTelemetryStreamRequest` and `ImportTelemetryStreamRequest` do the same as the export and
import, with the data sent as a stream of chunks (see [[Wire Protocol]]).
