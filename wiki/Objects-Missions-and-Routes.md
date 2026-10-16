Missions and the routes in them as they are planned: segments with their algorithm, figure and parameters, and the algorithms available. See [[Routes and Missions]].

## Contents

- [TraverseAlgorithm](#traversealgorithm)
- [ParameterDefinition](#parameterdefinition)
- [Figure](#figure)
- [FigureParameter](#figureparameter)
- [FigurePoint](#figurepoint)
- [Mission](#mission)
- [MissionVehicle](#missionvehicle)
- [Route](#route)
- [Failsafe](#failsafe)
- [SegmentDefinition](#segmentdefinition)
- [ParameterValue](#parametervalue)
- [Selection](#selection)
- [SelectionItem](#selectionitem)

### TraverseAlgorithm

Definitions of algorithms in the system.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 4 | `name` | `string` |  |
| 5 | `implementationClass` | `string` |  |
| 6 | `selectFigure` | `bool` |  |
| 7 | `figureType` | [FigureType](Enums#figuretype) |  |
| 9 | `parameterDefinitions` | repeated [ParameterDefinition](#parameterdefinition) |  |
| 10 | `tag` | `string` |  |
| 11 | `code` | `string` |  |

### ParameterDefinition

Definitions of algorithms' parameters. The actual values are stored in ParameterValue; referenced to the corresponded definitions by its name.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 4 | `name` | `string` |  |
| 5 | `description` | `string` |  |
| 6 | `type` | [ParameterType](Enums#parametertype) |  |
| 7 | `mandatory` | `bool` |  |
| 8 | `defaultValue` | `string` |  |
| 9 | `validationRegex` | `string` |  |
| 10 | `algorithm` | [TraverseAlgorithm](#traversealgorithm) |  |
| 11 | `physicalSemantic` | [PhysicalSemantic](Enums#physicalsemantic) |  |
| 12 | `tag` | `string` |  |
| 13 | `category` | [ParameterCategory](Enums#parametercategory) |  |
| 14 | `automatic` | `bool` |  |

### Figure

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `type` | [FigureType](Enums#figuretype) |  |
| 4 | `points` | repeated [FigurePoint](#figurepoint) |  |
| 5 | `parameters` | repeated [FigureParameter](#figureparameter) |  |
| 6 | `tag` | `string` |  |

### FigureParameter

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 4 | `value` | `double` |  |
| 5 | `figure` | [Figure](#figure) |  |
| 6 | `tag` | `string` |  |

### FigurePoint

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 4 | `latitude` | `double` | Radians. |
| 5 | `longitude` | `double` | Radians. |
| 6 | `wgs84Altitude` | `double` |  |
| 7 | `aglAltitude` | `double` |  |
| 8 | `altitudeType` | [AltitudeType](Enums#altitudetype) |  |
| 9 | `figure` | [Figure](#figure) |  |
| 10 | `tag` | `string` |  |

### Mission

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `name` | `string` |  |
| 4 | `description` | `string` |  |
| 5 | `creationTime` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 6 | `routes` | repeated [Route](#route) |  |
| 7 | `owner` | [User](Objects-Common#user) |  |
| 8 | `vehicles` | repeated [MissionVehicle](#missionvehicle) |  |
| 9 | `tag` | `string` |  |
| 10 | `uuid` | `string` | A UUID, as a string. |

### MissionVehicle

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `mission` | [Mission](#mission) |  |
| 4 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 5 | `tag` | `string` |  |

### Route

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `uuid` | `string` | A UUID, as a string. |
| 4 | `name` | `string` |  |
| 5 | `creationTime` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 6 | `scheduledTime` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 7 | `mission` | [Mission](#mission) |  |
| 8 | `segments` | repeated [SegmentDefinition](#segmentdefinition) |  |
| 10 | `trajectoryType` | [TrajectoryType](Enums#trajectorytype) | **Deprecated.** |
| 11 | `safeAltitude` | `double` |  |
| 12 | `maxAltitude` | `double` |  |
| 13 | `initialSpeed` | `double` |  |
| 14 | `maxSpeed` | `double` |  |
| 15 | `cornerRadius` | `double` |  |
| 20 | `failsafes` | repeated [Failsafe](#failsafe) |  |
| 21 | `vehicleProfile` | [VehicleProfile](Objects-Vehicles-and-Profiles#vehicleprofile) |  |
| 22 | `tag` | `string` |  |
| 23 | `checkAerodromeNfz` | `bool` |  |
| 24 | `checkCustomNfz` | `bool` |  |
| 25 | `modificationUuid` | `string` | A UUID, as a string. |
| 26 | `startDelay` | `double` |  |
| 27 | `takeoffHeight` | `double` |  |
| 28 | `lastModificationTime` | `int64` | Milliseconds since 1970-01-01 UTC. |

### Failsafe

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `route` | [Route](#route) |  |
| 4 | `reason` | [FailsafeReason](Enums#failsafereason) |  |
| 5 | `action` | [FailsafeAction](Enums#failsafeaction) |  |
| 6 | `tag` | `string` |  |

### SegmentDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `uuid` | `string` | A UUID, as a string. |
| 5 | `route` | [Route](#route) |  |
| 6 | `algorithmClassName` | `string` | The `implementationClass` of a [TraverseAlgorithm](#traversealgorithm), e.g. `com.ugcs.ucs.service.routing.impl.WaypointAlgorithm`. |
| 8 | `figure` | [Figure](#figure) |  |
| 10 | `parameterValues` | repeated [ParameterValue](#parametervalue) |  |
| 11 | `actionDefinitions` | repeated [ActionDefinition](Objects-Route-Actions#actiondefinition) |  |
| 12 | `tag` | `string` |  |
| 13 | `modificationUuid` | `string` | A UUID, as a string. |

### ParameterValue

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `name` | `string` |  |
| 4 | `value` | `string` |  |
| 5 | `segmentDefinition` | [SegmentDefinition](#segmentdefinition) |  |
| 6 | `tag` | `string` |  |

### Selection

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `selectionId` | `string` |  |
| 2 | `items` | repeated [SelectionItem](#selectionitem) |  |

### SelectionItem

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `value` | `string` |  |
| 2 | `description` | `string` |  |
| 3 | `markDefault` | `bool` |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
