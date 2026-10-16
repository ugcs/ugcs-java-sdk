The error answer to a failed request, and the progress and data stream messages of long operations. See [[Errors and Licensing]] and [[Wire Protocol]].

## Requests

- [CancelOperationRequest](#canceloperationrequest)
- [OperationStreamRequest](#operationstreamrequest)

### CancelOperationRequest

Message type `5`. Answered with [CancelOperationResponse](#canceloperationresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `requestId` | required `int32` |  |

### CancelOperationResponse

Message type `6`.

No fields.

### OperationStreamRequest

Message type `4`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `streamId` | required `string` |  |
| 2 | `chunkLength` | required `int32` |  |

## Other messages and types

### Error

Message type `0`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `errorMessage` | `string` |  |
| 2 | `errorCode` | `int32` | `1001`: a license constraint, see `errorDetail`. |
| 3 | `isSuppressible` | `bool` |  |
| 4 | `errorDetail` | [ErrorDetailWrapper](Objects-Common#errordetailwrapper) |  |

### OperationStatus

Message type `2`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `progressStack` | repeated [ProgressDto](Objects-Events#progressdto) |  |

### OperationStream

Message type `3`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `streamId` | required `string` |  |
| 2 | `chunk` | required `bytes` | empty chunk denotes end of stream. |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
