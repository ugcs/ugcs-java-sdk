Commands sent to a vehicle, the commands a vehicle supports, and the values they carry. See [[Vehicle Commands]].

## Contents

- [CommandDefinition](#commanddefinition)
- [CommandArgumentDefinition](#commandargumentdefinition)
- [Command](#command)
- [CommandArgument](#commandargument)
- [Value](#value)
- [Property](#property)
- [File](#file)

### CommandDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `code` | `string` |  |
| 4 | `name` | `string` |  |
| 5 | `arguments` | repeated [CommandArgumentDefinition](#commandargumentdefinition) |  |
| 6 | `tag` | `string` |  |
| 7 | `subsystem` | [Subsystem](Enums#subsystem) |  |
| 8 | `subsystemId` | `int32` |  |
| 9 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 10 | `enabled` | `bool` |  |
| 11 | `available` | `bool` |  |
| 12 | `hidden` | `bool` |  |

### CommandArgumentDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 11 | `command` | [CommandDefinition](#commanddefinition) |  |
| 13 | `code` | `string` |  |
| 14 | `semantic` | [Semantic](Enums#semantic) |  |
| 15 | `defaultValue` | [Value](#value) |  |
| 16 | `minValue` | [Value](#value) |  |
| 17 | `maxValue` | [Value](#value) |  |
| 12 | `tag` | `string` |  |
| 18 | `mandatory` | `bool` |  |

### Command

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `code` | `string` |  |
| 6 | `subsystem` | [Subsystem](Enums#subsystem) |  |
| 7 | `subsystemId` | `int32` |  |
| 2 | `arguments` | repeated [CommandArgument](#commandargument) |  |
| 3 | `silent` | `bool` |  |
| 4 | `resultIndifferent` | `bool` |  |
| 5 | `forced` | `bool` |  |

### CommandArgument

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `code` | `string` |  |
| 2 | `value` | [Value](#value) |  |

### Value

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `boolValue` | `bool` |  |
| 4 | `intValue` | `int32` |  |
| 5 | `longValue` | `int64` |  |
| 6 | `floatValue` | `float` |  |
| 7 | `doubleValue` | `double` |  |
| 8 | `stringValue` | `string` |  |
| 10 | `listValue` | repeated [Value](#value) |  |
| 11 | `bytesValue` | `bytes` |  |
| 9 | `tag` | `string` |  |

### Property

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `name` | required `string` |  |
| 2 | `value` | required [Value](#value) |  |
| 3 | `semantic` | required [Semantic](Enums#semantic) |  |

### File

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `name` | `string` |  |
| 2 | `data` | `bytes` |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
