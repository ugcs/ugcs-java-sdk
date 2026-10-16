Opening a session with the UgCS server, logging in and out, and what the server supports. See [[Connecting and Sessions]] for how they are used.

## Requests

- [AuthorizeHciRequest](#authorizehcirequest)
- [LoginRequest](#loginrequest)
- [LogoutRequest](#logoutrequest)
- [SetHciLocaleRequest](#sethcilocalerequest)
- [PingRequest](#pingrequest)
- [GetDeviceIdRequest](#getdeviceidrequest)
- [GetCapabilitiesRequest](#getcapabilitiesrequest)
- [CheckPermissionRequest](#checkpermissionrequest)

### AuthorizeHciRequest

Message type `20`. Answered with [AuthorizeHciResponse](#authorizehciresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | `-1`: the server creates a new session. Default `-1`. |
| 2 | `locale` | `string` | The session's locale, as a language tag (e.g. `de-DE`). |
| 3 | `clientVersion` | [ProtocolVersion](Objects-Common#protocolversion) | The client's protocol version. |
| 4 | `keepAlive` | `bool` |  |

### AuthorizeHciResponse

Message type `21`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The id of the new session: the `clientId` of every later request. |
| 2 | `serverVersion` | [ProtocolVersion](Objects-Common#protocolversion) | The server's protocol version. |
| 3 | `keepAlive` | `bool` |  |

### LoginRequest

Message type `22`. Answered with [LoginResponse](#loginresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `userLogin` | required `string` | A UgCS user's login. |
| 3 | `userPassword` | required `string` |  |

### LoginResponse

Message type `23`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `user` | required [User](Objects-Common#user) |  |

### LogoutRequest

Message type `24`. Answered with [LogoutResponse](#logoutresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |

### LogoutResponse

Message type `25`.

No fields.

### SetHciLocaleRequest

Message type `42`. Answered with [SetHciLocaleResponse](#sethcilocaleresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `locale` | required `string` |  |

### SetHciLocaleResponse

Message type `43`.

No fields.

### PingRequest

Message type `500`.

No fields.

### GetDeviceIdRequest

Message type `217`. Answered with [GetDeviceIdResponse](#getdeviceidresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |

### GetDeviceIdResponse

Message type `218`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `deviceId` | required `string` |  |

### GetCapabilitiesRequest

Message type `120`. Answered with [GetCapabilitiesResponse](#getcapabilitiesresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |

### GetCapabilitiesResponse

Message type `121`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `utm` | required `bool` | Default `false`. |

### CheckPermissionRequest

Message type `211`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `action` | [ServerAction](Enums#serveraction) | Required. |
| 3 | `targetPlatformCode` | `string` |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
