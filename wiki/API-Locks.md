Locking an object, e.g. a vehicle before sending it a command or uploading a route. See [[Working with Objects]].

## Requests

- [AcquireLockRequest](#acquirelockrequest)
- [ReleaseLockRequest](#releaselockrequest)
- [CheckLockRequest](#checklockrequest)
- [CheckMultipleLocksRequest](#checkmultiplelocksrequest)

### AcquireLockRequest

Message type `76`. Answered with [AcquireLockResponse](#acquirelockresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `objectType` | required `string` | The message name of the object, e.g. `Vehicle`. |
| 3 | `objectId` | required `int32` |  |

### AcquireLockResponse

Message type `77`.

No fields.

### ReleaseLockRequest

Message type `78`. Answered with [ReleaseLockResponse](#releaselockresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `objectType` | required `string` | The message name of the object, e.g. `Vehicle`. |
| 3 | `objectId` | required `int32` |  |
| 4 | `ifExclusive` | `bool` | Default `false`. |

### ReleaseLockResponse

Message type `79`.

No fields.

### CheckLockRequest

Message type `80`. Answered with [CheckLockResponse](#checklockresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `objectType` | required `string` | The message name of the object, e.g. `Vehicle`. |
| 3 | `objectId` | required `int32` |  |

### CheckLockResponse

Message type `81`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `objectType` | required `string` |  |
| 2 | `objectId` | required `int32` |  |
| 3 | `lock` | [Lock](Objects-Common#lock) | null if object is free. |

### CheckMultipleLocksRequest

Message type `82`. Answered with [CheckMultipleLocksResponse](#checkmultiplelocksresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `objectType` | required `string` |  |
| 3 | `objectIds` | repeated `int32` |  |

### CheckMultipleLocksResponse

Message type `83`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `objectType` | required `string` |  |
| 2 | `objectIds` | repeated `int32` |  |
| 3 | `locks` | repeated [Lock](Objects-Common#lock) |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
