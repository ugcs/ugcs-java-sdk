Subscribing to server events (telemetry, object changes, locks, commands...) and the notifications which deliver them. See [[Events and Subscriptions]].

## Requests

- [SubscribeEventRequest](#subscribeeventrequest)
- [UnsubscribeEventRequest](#unsubscribeeventrequest)

### SubscribeEventRequest

Message type `58`. Answered with [SubscribeEventResponse](#subscribeeventresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `subscription` | required [EventSubscriptionWrapper](Objects-Events#eventsubscriptionwrapper) |  |

### SubscribeEventResponse

Message type `59`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `subscriptionId` | required `int32` | The id the notifications of this subscription carry. |

### UnsubscribeEventRequest

Message type `60`. Answered with [UnsubscribeEventResponse](#unsubscribeeventresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `subscriptionId` | required `int32` | From [SubscribeEventResponse](API-Events#subscribeeventresponse). |

### UnsubscribeEventResponse

Message type `61`.

No fields.

## Other messages and types

### Notification

Message type `1`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `subscriptionId` | required `int32` | From [SubscribeEventResponse](API-Events#subscribeeventresponse). |
| 2 | `event` | required [EventWrapper](Objects-Events#eventwrapper) |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
