Sending a command to one or more vehicles. See [[Vehicle Commands]].

## Requests

- [SendCommandRequest](#sendcommandrequest)

### SendCommandRequest

Message type `64`. Answered with [SendCommandResponse](#sendcommandresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `command` | [Command](Objects-Commands#command) |  |
| 3 | `vehicles` | repeated [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 4 | `interDelay` | `int64` | Default `0`. |
| 5 | `commands` | repeated [VehicleCommandsDto](#vehiclecommandsdto) |  |

### SendCommandResponse

Message type `65`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `commandResults` | repeated [VehicleCommandResultDto](#vehiclecommandresultdto) |  |

## Other messages and types

### VehicleCommandResultDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicle` | required [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 2 | `commandStatus` | required [CommandStatus](Enums#commandstatus) |  |
| 3 | `errorMessage` | `string` |  |
| 4 | `files` | repeated [File](Objects-Commands#file) |  |
| 5 | `properties` | repeated [Property](Objects-Commands#property) |  |

### VehicleCommandsDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 2 | `commands` | repeated [Command](Objects-Commands#command) |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
