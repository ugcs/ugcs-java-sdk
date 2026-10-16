A route as the server has processed it, with the result and the messages of each segment, and its upload to a vehicle.

## Contents

- [RouteUpload](#routeupload)
- [RoutePass](#routepass)
- [ProcessedRoute](#processedroute)
- [ProcessedSegment](#processedsegment)
- [MessageSet](#messageset)
- [LocalizedMessage](#localizedmessage)
- [LocalizedMessageVariant](#localizedmessagevariant)
- [UtmOperation](#utmoperation)

### RouteUpload

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `route` | [Route](Objects-Missions-and-Routes#route) |  |
| 4 | `routeModificationUuid` | `string` | A UUID, as a string. |
| 5 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 6 | `user` | [User](Objects-Common#user) |  |
| 7 | `uploadTime` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 8 | `tag` | `string` |  |

### RoutePass

tracks a route execution by the vehicle RoutePass is created on a route upload and removed after it is fully processed.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `route` | [Route](Objects-Missions-and-Routes#route) |  |
| 4 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 5 | `uploadTime` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 6 | `waypointIndex` | `int32` | waypoint index in the original route that was reached last during the current pass (zero-based). |
| 7 | `latitude` | `double` | WGS84 coordinates of the vehicle within the route trajectory. |
| 8 | `longitude` | `double` |  |
| 9 | `altitude` | `double` |  |
| 10 | `routeModificationUuid` | `string` | A UUID, as a string. |
| 11 | `waypointCount` | `int32` | total number of waypoints in the original route. |
| 12 | `tag` | `string` |  |

### ProcessedRoute

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `route` | [Route](Objects-Missions-and-Routes#route) |  |
| 4 | `segments` | repeated [ProcessedSegment](#processedsegment) |  |
| 6 | `tag` | `string` |  |
| 7 | `uuid` | `string` | Route.uuid. A UUID, as a string. |
| 8 | `modificationUuid` | `string` | Route.modificationUuid. A UUID, as a string. |
| 9 | `estimatedDistance` | `double` | Cannot be empty for fully processed routes. |
| 10 | `diameter` | `double` | Cannot be empty for fully processed routes. |

### ProcessedSegment

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `uuid` | `string` | SegmentDefinition.uuid. A UUID, as a string. |
| 4 | `status` | [RouteProcessingStatus](Enums#routeprocessingstatus) | `RPS_PROCESSED` when the segment is fully processed. |
| 5 | `estimatedDistance` | `double` |  |
| 6 | `estimatedTime` | `double` |  |
| 7 | `wrongPointNumber` | `int32` |  |
| 8 | `messageSet` | [MessageSet](#messageset) |  |
| 9 | `segmentActions` | repeated [Action](Objects-Route-Actions#action) | Sets of actions (commands) for vehicle: for segment itself;. |
| 10 | `connectionActions` | repeated [Action](Objects-Route-Actions#action) | for nextcoming connection to the next segment;. |
| 11 | `optimizedActions` | repeated [Action](Objects-Route-Actions#action) |  |
| 12 | `tag` | `string` |  |
| 13 | `modificationUuid` | `string` | SegmentDefinition.modificationUuid. A UUID, as a string. |
| 14 | `algorithmCode` | `string` |  |
| 15 | `autoDirectionAngle` | `double` |  |

### MessageSet

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `messages` | repeated [LocalizedMessage](#localizedmessage) |  |
| 4 | `tag` | `string` |  |

### LocalizedMessage

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 4 | `messageSet` | [MessageSet](#messageset) |  |
| 5 | `severity` | [MessageSeverity](Enums#messageseverity) |  |
| 6 | `defaultText` | `string` |  |
| 7 | `variants` | repeated [LocalizedMessageVariant](#localizedmessagevariant) |  |
| 8 | `tag` | `string` |  |

### LocalizedMessageVariant

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 4 | `message` | [LocalizedMessage](#localizedmessage) |  |
| 5 | `localeId` | `string` |  |
| 6 | `messageText` | `string` |  |
| 7 | `tag` | `string` |  |

### UtmOperation

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 4 | `status` | [UtmOperationStatus](Enums#utmoperationstatus) |  |
| 5 | `gufi` | `string` |  |
| 6 | `flightNumber` | `string` |  |
| 7 | `effectiveBeginningTime` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 8 | `effectiveEndTime` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 9 | `tag` | `string` |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
