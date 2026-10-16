The flight tracks of vehicles and their log messages.

## Requests

- [GetVehicleTracksRequest](#getvehicletracksrequest)
- [DeleteVehicleTrackRequest](#deletevehicletrackrequest)
- [MergeVehicleTrackRequest](#mergevehicletrackrequest)
- [SplitVehicleTrackRequest](#splitvehicletrackrequest)
- [GetVehicleLogRequest](#getvehiclelogrequest)
- [GetVehicleLogByTimeRangeRequest](#getvehiclelogbytimerangerequest)
- [GetVehicleVideoLogRequest](#getvehiclevideologrequest)

### GetVehicleTracksRequest

search for a telemetry tracks in a given range: start time in [from, to). Message type `126`. Answered with [GetVehicleTracksResponse](#getvehicletracksresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicles` | repeated [Vehicle](Objects-Vehicles-and-Profiles#vehicle) | target vehicle. |
| 3 | `from` | `int64` | defines a lower bound limit for a track start time start time >= from. |
| 4 | `to` | `int64` | defines an upper bound limit for a track start time start time < from. |
| 6 | `limit` | required `int32` | max number of a resulting tracks. Default `7`. |

### GetVehicleTracksResponse

Message type `127`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicleTracks` | repeated [VehicleTrack](Objects-Telemetry-and-Logs#vehicletrack) | tracks sorted in a descending order (by start time). |

### DeleteVehicleTrackRequest

remove a track. Message type `148`. Answered with [DeleteVehicleTrackResponse](#deletevehicletrackresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicleTrack` | required [VehicleTrack](Objects-Telemetry-and-Logs#vehicletrack) |  |

### DeleteVehicleTrackResponse

Message type `149`.

No fields.

### MergeVehicleTrackRequest

merge the given track with a previous one of the same vehicle. Message type `150`. Answered with [MergeVehicleTrackResponse](#mergevehicletrackresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicleTrack` | required [VehicleTrack](Objects-Telemetry-and-Logs#vehicletrack) |  |

### MergeVehicleTrackResponse

Message type `151`.

| # | Field | Type | Description |
|---|---|---|---|
| 2 | `vehicleTrack` | required [VehicleTrack](Objects-Telemetry-and-Logs#vehicletrack) | merged track. |

### SplitVehicleTrackRequest

split given track to a two by specified date. Message type `154`. Answered with [SplitVehicleTrackResponse](#splitvehicletrackresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicleTrack` | required [VehicleTrack](Objects-Telemetry-and-Logs#vehicletrack) |  |
| 3 | `splitTime` | required `int64` |  |

### SplitVehicleTrackResponse

Message type `155`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `first` | required [VehicleTrack](Objects-Telemetry-and-Logs#vehicletrack) | track before split date. |
| 2 | `second` | required [VehicleTrack](Objects-Telemetry-and-Logs#vehicletrack) | track after split date. |

### GetVehicleLogRequest

Message type `62`. Answered with [GetVehicleLogResponse](#getvehiclelogresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicles` | repeated [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 3 | `limit` | `int32` | Default `15`. |
| 4 | `level` | [SeverityLevel](Enums#severitylevel) | Default `SL_INFORMATIONAL`. |
| 5 | `fromTime` | `int64` |  |
| 6 | `reverseOrder` | `bool` | Default `false`. |

### GetVehicleLogResponse

Message type `63`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicleLogEntries` | repeated [VehicleLogEntry](Objects-Telemetry-and-Logs#vehiclelogentry) |  |

### GetVehicleLogByTimeRangeRequest

Message type `88`. Answered with [GetVehicleLogByTimeRangeResponse](#getvehiclelogbytimerangeresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicles` | repeated [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 3 | `fromTime` | required `int64` |  |
| 4 | `toTime` | required `int64` |  |
| 5 | `level` | [SeverityLevel](Enums#severitylevel) | **Deprecated.** Default `SL_INFORMATIONAL`. |

### GetVehicleLogByTimeRangeResponse

Message type `89`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicleLogEntries` | repeated [VehicleLogEntry](Objects-Telemetry-and-Logs#vehiclelogentry) |  |

### GetVehicleVideoLogRequest

**Deprecated.** Message type `156`. Answered with [GetVehicleVideoLogResponse](#getvehiclevideologresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicles` | repeated [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 3 | `fromTime` | required `int64` |  |
| 4 | `toTime` | required `int64` |  |

### GetVehicleVideoLogResponse

**Deprecated.** Message type `157`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicleLogEntries` | repeated [VehicleLogEntry](Objects-Telemetry-and-Logs#vehiclelogentry) |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
