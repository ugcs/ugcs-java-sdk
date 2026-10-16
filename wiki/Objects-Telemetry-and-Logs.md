Telemetry values, flight tracks and vehicle log messages. See [[Telemetry]].

## Contents

- [VehicleLogEntry](#vehiclelogentry)
- [Notice](#notice)
- [TelemetryInterval](#telemetryinterval)
- [Telemetry](#telemetry)
- [TelemetryField](#telemetryfield)
- [TrackPoint](#trackpoint)
- [Track](#track)
- [VehicleTrack](#vehicletrack)
- [TelemetrySnapshot](#telemetrysnapshot)

### VehicleLogEntry

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `time` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 4 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 5 | `message` | `string` |  |
| 6 | `level` | [SeverityLevel](Enums#severitylevel) |  |
| 12 | `tag` | `string` |  |

### Notice

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `tag` | `string` |  |
| 4 | `time` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 5 | `message` | `string` |  |
| 6 | `kind` | [NoticeKind](Enums#noticekind) |  |

### TelemetryInterval

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `name` | `string` |  |
| 4 | `beginningTime` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 5 | `endTime` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 7 | `tag` | `string` |  |

### Telemetry

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `value` | [Value](Objects-Commands#value) |  |
| 2 | `time` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 3 | `telemetryField` | [TelemetryField](#telemetryfield) |  |

### TelemetryField

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `code` | `string` |  |
| 3 | `semantic` | [Semantic](Enums#semantic) |  |
| 4 | `subsystem` | [Subsystem](Enums#subsystem) |  |
| 5 | `subsystemId` | `int32` |  |

### TrackPoint

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `time` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 2 | `latitude` | `double` |  |
| 3 | `longitude` | `double` |  |
| 4 | `altitude` | `double` |  |
| 5 | `elevation` | `double` |  |

### Track

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `points` | repeated [TrackPoint](#trackpoint) |  |
| 2 | `approximationThreshold` | `double` |  |

### VehicleTrack

| # | Field | Type | Description |
|---|---|---|---|
| 3 | `id` | `int32` |  |
| 1 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 2 | `track` | [Track](#track) |  |

### TelemetrySnapshot

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 2 | `snapshotTime` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 3 | `telemetry` | repeated [Telemetry](#telemetry) |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
