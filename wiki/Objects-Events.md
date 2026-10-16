What can be subscribed to, and the events which come back. See [[Events and Subscriptions]].

## Contents

- [ObjectModificationEvent](#objectmodificationevent)
- [ObjectModificationSubscription](#objectmodificationsubscription)
- [EnvironmentEvent](#environmentevent)
- [EnvironmentSubscription](#environmentsubscription)
- [LockEvent](#lockevent)
- [LockSubscription](#locksubscription)
- [TelemetryEvent](#telemetryevent)
- [TelemetrySubscription](#telemetrysubscription)
- [TelemetryRemovalEvent](#telemetryremovalevent)
- [TelemetryRemovalSubscription](#telemetryremovalsubscription)
- [AdsbIndicatorEvent](#adsbindicatorevent)
- [AdsbIndicatorSubscription](#adsbindicatorsubscription)
- [CommandEvent](#commandevent)
- [CommandSubscription](#commandsubscription)
- [ProgressDto](#progressdto)
- [ProgressStackDto](#progressstackdto)
- [ServerProgressEvent](#serverprogressevent)
- [ServerProgressSubscription](#serverprogresssubscription)
- [VehicleTrackEvent](#vehicletrackevent)
- [VehicleTrackSubscription](#vehicletracksubscription)
- [TelemetryBatchEvent](#telemetrybatchevent)
- [VehicleTelemetry](#vehicletelemetry)
- [TelemetryBatchSubscription](#telemetrybatchsubscription)
- [EventWrapper](#eventwrapper)
- [EventSubscriptionWrapper](#eventsubscriptionwrapper)

### ObjectModificationEvent

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `modificationType` | [ModificationType](Enums#modificationtype) |  |
| 2 | `objectType` | `string` |  |
| 3 | `objectId` | `int32` |  |
| 4 | `object` | [DomainObjectWrapper](Objects-Common#domainobjectwrapper) |  |

### ObjectModificationSubscription

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `objectType` | `string` |  |
| 2 | `objectId` | `int32` |  |

### EnvironmentEvent

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `minLatitude` | `double` |  |
| 2 | `minLongitude` | `double` |  |
| 3 | `maxLatitude` | `double` |  |
| 4 | `maxLongitude` | `double` |  |
| 5 | `environmentEventType` | [EnvironmentEventType](Enums#environmenteventtype) |  |

### EnvironmentSubscription

No fields.

### LockEvent

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `objectType` | `string` |  |
| 2 | `objectId` | `int32` |  |
| 3 | `lock` | [Lock](Objects-Common#lock) |  |

### LockSubscription

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `objectType` | `string` |  |
| 2 | `objectId` | `int32` |  |

### TelemetryEvent

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 3 | `telemetry` | repeated [Telemetry](Objects-Telemetry-and-Logs#telemetry) |  |

### TelemetrySubscription

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |

### TelemetryRemovalEvent

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 2 | `fromTime` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 3 | `toTime` | `int64` | Milliseconds since 1970-01-01 UTC. |

### TelemetryRemovalSubscription

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |

### AdsbIndicatorEvent

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `on` | `bool` |  |

### AdsbIndicatorSubscription

No fields.

### CommandEvent

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 2 | `commands` | repeated [CommandDefinition](Objects-Commands#commanddefinition) |  |

### CommandSubscription

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |

### ProgressDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `title` | `string` |  |
| 2 | `progress` | `int32` |  |
| 3 | `maxProgress` | `int32` |  |

### ProgressStackDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `progresses` | repeated [ProgressDto](#progressdto) |  |
| 2 | `processKey` | `string` |  |

### ServerProgressEvent

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `progressStacks` | repeated [ProgressStackDto](#progressstackdto) |  |

### ServerProgressSubscription

No fields.

### VehicleTrackEvent

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicleTrack` | [VehicleTrack](Objects-Telemetry-and-Logs#vehicletrack) |  |

### VehicleTrackSubscription

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |

### TelemetryBatchEvent

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicleTelemetry` | repeated [VehicleTelemetry](#vehicletelemetry) |  |

### VehicleTelemetry

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 2 | `telemetry` | repeated [Telemetry](Objects-Telemetry-and-Logs#telemetry) |  |

### TelemetryBatchSubscription

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `pollingPeriodMilliseconds` | `int32` |  |
| 2 | `vehicles` | repeated [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |

### EventWrapper

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `objectModificationEvent` | [ObjectModificationEvent](#objectmodificationevent) |  |
| 2 | `lockEvent` | [LockEvent](#lockevent) |  |
| 3 | `telemetryEvent` | [TelemetryEvent](#telemetryevent) |  |
| 4 | `telemetryRemovalEvent` | [TelemetryRemovalEvent](#telemetryremovalevent) |  |
| 5 | `adsbIndicatorEvent` | [AdsbIndicatorEvent](#adsbindicatorevent) |  |
| 7 | `serverProgressEvent` | [ServerProgressEvent](#serverprogressevent) |  |
| 8 | `vehicleTrackEvent` | [VehicleTrackEvent](#vehicletrackevent) |  |
| 9 | `environmentEvent` | [EnvironmentEvent](#environmentevent) |  |
| 10 | `commandEvent` | [CommandEvent](#commandevent) |  |
| 11 | `telemetryBatchEvent` | [TelemetryBatchEvent](#telemetrybatchevent) |  |

### EventSubscriptionWrapper

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `objectModificationSubscription` | [ObjectModificationSubscription](#objectmodificationsubscription) |  |
| 2 | `lockSubscription` | [LockSubscription](#locksubscription) |  |
| 3 | `telemetrySubscription` | [TelemetrySubscription](#telemetrysubscription) |  |
| 4 | `telemetryRemovalSubscription` | [TelemetryRemovalSubscription](#telemetryremovalsubscription) |  |
| 5 | `adsbIndicatorSubscription` | [AdsbIndicatorSubscription](#adsbindicatorsubscription) |  |
| 7 | `serverProgressSubscription` | [ServerProgressSubscription](#serverprogresssubscription) |  |
| 8 | `vehicleTrackSubscription` | [VehicleTrackSubscription](#vehicletracksubscription) |  |
| 9 | `environmentSubscription` | [EnvironmentSubscription](#environmentsubscription) |  |
| 10 | `commandSubscription` | [CommandSubscription](#commandsubscription) |  |
| 11 | `telemetryBatchSubscription` | [TelemetryBatchSubscription](#telemetrybatchsubscription) |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
