Vehicles, their platforms and vehicle profiles, payload profiles, and the vehicle specific modules (VSM) which connect them.

## Contents

- [Vsm](#vsm)
- [Platform](#platform)
- [PlatformAction](#platformaction)
- [PlatformPayloadProfile](#platformpayloadprofile)
- [Vehicle](#vehicle)
- [VehicleProfilePayloadProfile](#vehicleprofilepayloadprofile)
- [VehicleProfile](#vehicleprofile)
- [VehicleParameter](#vehicleparameter)
- [PayloadProfile](#payloadprofile)
- [PayloadParameter](#payloadparameter)
- [PlatformParameters](#platformparameters)
- [ImportedVehicleProfile](#importedvehicleprofile)
- [ImportedPayloadProfile](#importedpayloadprofile)

### Vsm

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `name` | `string` |  |
| 4 | `host` | `string` |  |
| 5 | `port` | `int32` |  |
| 6 | `protocol` | `string` |  |
| 8 | `tag` | `string` |  |

### Platform

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `code` | `string` |  |
| 4 | `name` | `string` |  |
| 5 | `alias` | `string` |  |
| 6 | `actions` | repeated [PlatformAction](#platformaction) |  |
| 9 | `payloadProfiles` | repeated [PlatformPayloadProfile](#platformpayloadprofile) |  |
| 10 | `tag` | `string` |  |
| 11 | `autopilotType` | `string` | computed property, any change has no effect. |
| 12 | `aliases` | repeated `string` |  |

### PlatformAction

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `platform` | [Platform](#platform) |  |
| 4 | `actionCode` | [ActionCode](Enums#actioncode) |  |
| 5 | `tag` | `string` |  |

### PlatformPayloadProfile

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `platform` | [Platform](#platform) |  |
| 4 | `payloadProfile` | [PayloadProfile](#payloadprofile) |  |
| 5 | `tag` | `string` |  |

### Vehicle

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `vehicleId` | `int64` |  |
| 5 | `name` | `string` |  |
| 6 | `tailNumber` | `string` |  |
| 7 | `type` | [VehicleType](Enums#vehicletype) |  |
| 13 | `altitudeOrigin` | `double` |  |
| 15 | `platform` | [Platform](#platform) |  |
| 16 | `profile` | [VehicleProfile](#vehicleprofile) |  |
| 17 | `tag` | `string` |  |
| 18 | `emulator` | `bool` |  |
| 23 | `icaoAddress` | `int32` |  |
| 24 | `adsbAircrafts` | repeated [AdsbAircraft](Objects-Map-and-Terrain#adsbaircraft) |  |
| 25 | `serialNumber` | `string` |  |
| 26 | `port` | `string` |  |
| 27 | `source` | [VehicleSource](Enums#vehiclesource) |  |
| 28 | `remoteIdSerialNumber` | `string` |  |
| 29 | `remoteIdIdentifierType` | [RemoteIdIdentifierType](Enums#remoteididentifiertype) |  |
| 30 | `remoteIdEnabled` | `bool` |  |

### VehicleProfilePayloadProfile

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `vehicleProfile` | [VehicleProfile](#vehicleprofile) |  |
| 4 | `payloadProfile` | [PayloadProfile](#payloadprofile) |  |
| 5 | `tag` | `string` |  |

### VehicleProfile

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `name` | `string` |  |
| 4 | `vehicleType` | [VehicleType](Enums#vehicletype) |  |
| 6 | `parameters` | repeated [VehicleParameter](#vehicleparameter) |  |
| 7 | `payloadProfiles` | repeated [VehicleProfilePayloadProfile](#vehicleprofilepayloadprofile) |  |
| 8 | `platform` | [Platform](#platform) |  |
| 9 | `sealed` | `bool` |  |
| 10 | `unremovable` | `bool` |  |
| 11 | `tag` | `string` |  |
| 12 | `primary` | `bool` |  |
| 13 | `modelKey` | `string` |  |
| 14 | `imageKey` | `string` |  |
| 15 | `code` | `string` |  |

### VehicleParameter

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `value` | `double` |  |
| 4 | `vehicleProfile` | [VehicleProfile](#vehicleprofile) |  |
| 5 | `type` | [VehicleParameterType](Enums#vehicleparametertype) |  |
| 6 | `tag` | `string` |  |

### PayloadProfile

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `name` | `string` |  |
| 4 | `payloadType` | [PayloadType](Enums#payloadtype) |  |
| 5 | `imageKey` | `string` |  |
| 7 | `parameters` | repeated [PayloadParameter](#payloadparameter) |  |
| 8 | `platforms` | repeated [PlatformPayloadProfile](#platformpayloadprofile) |  |
| 9 | `sealed` | `bool` |  |
| 10 | `unremovable` | `bool` |  |
| 11 | `tag` | `string` |  |
| 12 | `shortName` | `string` |  |
| 13 | `code` | `string` |  |

### PayloadParameter

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `value` | `double` |  |
| 4 | `payloadProfile` | [PayloadProfile](#payloadprofile) |  |
| 5 | `type` | [PayloadParameterType](Enums#payloadparametertype) |  |
| 6 | `tag` | `string` |  |

### PlatformParameters

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `tag` | `string` |  |
| 4 | `minReturnToHomeAltitude` | `double` |  |
| 5 | `maxReturnToHomeAltitude` | `double` |  |
| 6 | `supportedParameters` | repeated [VehicleParameterType](Enums#vehicleparametertype) |  |

### ImportedVehicleProfile

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `source` | [VehicleProfile](#vehicleprofile) |  |
| 2 | `destination` | [VehicleProfile](#vehicleprofile) |  |

### ImportedPayloadProfile

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `source` | [PayloadProfile](#payloadprofile) |  |
| 2 | `destination` | [PayloadProfile](#payloadprofile) |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
