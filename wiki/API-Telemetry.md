Reading the telemetry the server has recorded, its latest values, and exporting and importing it. See [[Telemetry]].

## Requests

- [GetTelemetryRequest](#gettelemetryrequest)
- [DeleteTelemetryRequest](#deletetelemetryrequest)
- [TraceTelemetryFramesRequest](#tracetelemetryframesrequest)
- [CountTelemetryRequest](#counttelemetryrequest)
- [GetLocationPassingTimeRequest](#getlocationpassingtimerequest)
- [ExportTelemetryRequest](#exporttelemetryrequest)
- [ImportTelemetryRequest](#importtelemetryrequest)
- [ExportTelemetryStreamRequest](#exporttelemetrystreamrequest)
- [ImportTelemetryStreamRequest](#importtelemetrystreamrequest)
- [GetTelemetrySnapshotRequest](#gettelemetrysnapshotrequest)
- [IsTelemetryFieldRegisteredRequest](#istelemetryfieldregisteredrequest)

### GetTelemetryRequest

Message type `36`. Answered with [GetTelemetryResponse](#gettelemetryresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicle` | required [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 3 | `fromTime` | required `int64` |  |
| 4 | `toTime` | `int64` |  |
| 5 | `limit` | `int32` | Default `7200`. |

### GetTelemetryResponse

Message type `37`.

| # | Field | Type | Description |
|---|---|---|---|
| 2 | `nextTelemetryTime` | `int64` |  |
| 3 | `telemetry` | repeated [Telemetry](Objects-Telemetry-and-Logs#telemetry) |  |

### DeleteTelemetryRequest

Message type `96`. Answered with [DeleteTelemetryResponse](#deletetelemetryresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicle` | required [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 3 | `fromTime` | `int64` |  |
| 4 | `toTime` | `int64` |  |

### DeleteTelemetryResponse

Message type `97`.

No fields.

### TraceTelemetryFramesRequest

Message type `94`. Answered with [TraceTelemetryFramesResponse](#tracetelemetryframesresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicle` | required [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 3 | `originTime` | required `int64` |  |
| 4 | `interval` | required `double` |  |
| 5 | `number` | required `int32` |  |

### TraceTelemetryFramesResponse

Message type `95`.

| # | Field | Type | Description |
|---|---|---|---|
| 2 | `commandFrames` | repeated [CommandSetDto](#commandsetdto) |  |

### CountTelemetryRequest

Message type `110`. Answered with [CountTelemetryResponse](#counttelemetryresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicle` | required [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 3 | `fromTime` | `int64` |  |
| 4 | `toTime` | `int64` |  |
| 5 | `estimateTLogSize` | `bool` | Default `false`. |

### CountTelemetryResponse

Message type `111`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `count` | required `int64` |  |
| 2 | `tLogSize` | `int64` |  |

### GetLocationPassingTimeRequest

Message type `130`. Answered with [GetLocationPassingTimeResponse](#getlocationpassingtimeresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicle` | required [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 3 | `fromTime` | required `int64` |  |
| 4 | `toTime` | required `int64` |  |
| 5 | `location` | required [Wgs84LocationDto](API-Common-Types#wgs84locationdto) |  |

### GetLocationPassingTimeResponse

Message type `131`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `time` | required `int64` |  |

### ExportTelemetryRequest

Message type `90`. Answered with [ExportTelemetryResponse](#exporttelemetryresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicle` | required [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 3 | `from` | `int64` | defines a lower bound limit for a telemetry time time >= from. |
| 4 | `to` | `int64` | defines an upper bound limit for a telemetry time time < from. |
| 5 | `format` | required [TelemetryFormat](Enums#telemetryformat) | format of the serialized telemetry data. Default `TF_TLM`. |

### ExportTelemetryResponse

Message type `91`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `data` | required `bytes` | telemetry data in a requested format. |

### ImportTelemetryRequest

Message type `92`. Answered with [ImportTelemetryResponse](#importtelemetryresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `data` | required `bytes` | telemetry data in a tlm binary format. |

### ImportTelemetryResponse

Message type `93`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicles` | repeated [Vehicle](Objects-Vehicles-and-Profiles#vehicle) | list of vehicles affected by the telemetry import. |

### ExportTelemetryStreamRequest

Message type `158`. Answered with [ExportTelemetryStreamResponse](#exporttelemetrystreamresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicle` | required [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 3 | `from` | `int64` | defines a lower bound limit for a telemetry time time >= from. |
| 4 | `to` | `int64` | defines an upper bound limit for a telemetry time time < from. |
| 5 | `format` | required [TelemetryFormat](Enums#telemetryformat) | format of the serialized telemetry data. Default `TF_TLM`. |

### ExportTelemetryStreamResponse

Message type `159`.

No fields.

### ImportTelemetryStreamRequest

Message type `160`. Answered with [ImportTelemetryStreamResponse](#importtelemetrystreamresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `streamId` | required `string` | id of the telemetry data stream in a tlm binary format. |

### ImportTelemetryStreamResponse

Message type `161`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicles` | repeated [Vehicle](Objects-Vehicles-and-Profiles#vehicle) | list of vehicles affected by the telemetry import. |

### GetTelemetrySnapshotRequest

Message type `168`. Answered with [GetTelemetrySnapshotResponse](#gettelemetrysnapshotresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicles` | repeated [Vehicle](Objects-Vehicles-and-Profiles#vehicle) | list of vehicles to get snapshots for. |

### GetTelemetrySnapshotResponse

Message type `169`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `snapshots` | repeated [TelemetrySnapshot](Objects-Telemetry-and-Logs#telemetrysnapshot) | list of vehicle telemetry snapshots. |

### IsTelemetryFieldRegisteredRequest

Returns true if the provided telemetry field is registered for the vehicle. Message type `180`. Answered with [IsTelemetryFieldRegisteredResponse](#istelemetryfieldregisteredresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicle` | required [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 3 | `fieldCode` | required `string` |  |
| 4 | `subsystem` | required [Subsystem](Enums#subsystem) | The type of the subsystem that owns the field. |
| 5 | `subsystemId` | `int32` | The instance id of the subsystem that owns the field. If not specified, the response returns true if at least one of the vehicle subsystem with the provided type has the field. |

### IsTelemetryFieldRegisteredResponse

Message type `181`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `registered` | required `bool` |  |

## Other messages and types

### CommandSetDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `commandCode` | repeated `string` |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
