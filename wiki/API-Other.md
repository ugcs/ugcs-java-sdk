Requests of the UgCS client application: feedback, usage statistics and analytics.

## Requests

- [SendFeedbackRequest](#sendfeedbackrequest)
- [ConfigureAnalyticsRequest](#configureanalyticsrequest)
- [ReadAnalyticsConfigurationRequest](#readanalyticsconfigurationrequest)
- [SendAnalyticsEventRequest](#sendanalyticseventrequest)
- [SendStatisticsRequest](#sendstatisticsrequest)

### SendFeedbackRequest

Message type `200`. Answered with [SendFeedbackResponse](#sendfeedbackresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `ReporterFullName` | `string` | Optional. |
| 3 | `ReporterEmail` | `string` | Optional. |
| 4 | `Summary` | `string` | Required not empty string, 255 symbols maximum. |
| 5 | `Details` | `string` | Optional. |

### SendFeedbackResponse

Message type `201`.

No fields.

### ConfigureAnalyticsRequest

Message type `204`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `configuration` | [AnalyticsConfigurationDto](#analyticsconfigurationdto) |  |

### ReadAnalyticsConfigurationRequest

Message type `206`. Answered with [ReadAnalyticsConfigurationResponse](#readanalyticsconfigurationresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |

### ReadAnalyticsConfigurationResponse

Message type `207`.

| # | Field | Type | Description |
|---|---|---|---|
| 2 | `configuration` | [AnalyticsConfigurationDto](#analyticsconfigurationdto) |  |

### SendAnalyticsEventRequest

Message type `208`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | `int32` | Is not used. Kept for backward compatibility. |
| 2 | `event` | required [AnalyticsEvent](#analyticsevent) |  |

### SendStatisticsRequest

**Deprecated.** Statistics. Message type `102`. Answered with [SendStatisticsResponse](#sendstatisticsresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `statistics` | required [StatisticsDto](#statisticsdto) |  |

### SendStatisticsResponse

**Deprecated.** Message type `103`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `accepted` | required `bool` |  |

## Other messages and types

### StatisticsRecordDto

**Deprecated.**

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `parameter` | required `string` |  |
| 2 | `value` | required `string` |  |

### StatisticsDto

**Deprecated.**

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `time` | required `int64` |  |
| 2 | `clientUID` | required `string` |  |
| 3 | `clientMAC` | required `string` |  |
| 4 | `clientOsType` | required `string` |  |
| 5 | `clientOsVersion` | required `string` |  |
| 6 | `unityVersion` | required `string` |  |
| 7 | `details` | repeated [StatisticsRecordDto](#statisticsrecorddto) |  |

### AnalyticsEventAttributeValue

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `string` | `string` |  |
| 2 | `number` | `int64` |  |
| 3 | `boolean` | `bool` |  |

### AnalyticsEventAttribute

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `code` | required `string` |  |
| 2 | `value` | required [AnalyticsEventAttributeValue](#analyticseventattributevalue) |  |

### AnalyticsEvent

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `eventCode` | required `string` |  |
| 2 | `attributes` | repeated [AnalyticsEventAttribute](#analyticseventattribute) |  |

### AnalyticsConfigurationDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `allowToCollectAndSendData` | `bool` |  |

### EmptyResponse

Message type `205`.

No fields.

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
