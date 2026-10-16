Processing a route into the commands a vehicle executes, changing and splitting routes, uploading a route to a vehicle, and camera calculations. See [[Routes and Missions]].

## Requests

- [ProcessRouteRequest](#processrouterequest)
- [ConvertRouteAltitudesRequest](#convertroutealtitudesrequest)
- [ChangeRouteVehicleProfileRequest](#changeroutevehicleprofilerequest)
- [GetParametersSelectionValuesRequest](#getparametersselectionvaluesrequest)
- [CalculateSplineRequest](#calculatesplinerequest)
- [SplitRouteBySegmentsRequest](#splitroutebysegmentsrequest)
- [SplitRouteByDistanceRequest](#splitroutebydistancerequest)
- [SplitRouteByCorridorPointRequest](#splitroutebycorridorpointrequest)
- [JoinRouteRequest](#joinrouterequest)
- [ReconstructRouteRequest](#reconstructrouterequest)
- [GetNativeRouteRequest](#getnativerouterequest)
- [UploadRouteRequest](#uploadrouterequest)
- [UploadRouteRequest2](#uploadrouterequest2)
- [ResolveGsdByAltitudeAglRequest](#resolvegsdbyaltitudeaglrequest)
- [ResolveGsdByAltitudeAmslRequest](#resolvegsdbyaltitudeamslrequest)
- [ResolveAltitudeAglByGsdRequest](#resolvealtitudeaglbygsdrequest)
- [ResolveAltitudeAmslByGsdRequest](#resolvealtitudeamslbygsdrequest)
- [SubmitUtmOperationRequest](#submitutmoperationrequest)

### ProcessRouteRequest

Message type `32`. Answered with [ProcessRouteResponse](#processrouteresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |
| 3 | `token` | `string` |  |

### ProcessRouteResponse

Message type `33`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `processedRoute` | required [ProcessedRoute](Objects-Processed-Routes#processedroute) |  |
| 2 | `hash` | required `string` |  |

### ConvertRouteAltitudesRequest

**Deprecated.** Message type `44`. Answered with [ConvertRouteAltitudesResponse](#convertroutealtitudesresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |
| 4 | `altitudeType` | required [AltitudeType](Enums#altitudetype) |  |

### ConvertRouteAltitudesResponse

Message type `45`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |

### ChangeRouteVehicleProfileRequest

Message type `114`. Answered with [ChangeRouteVehicleProfileResponse](#changeroutevehicleprofileresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |
| 3 | `newProfile` | required [VehicleProfile](Objects-Vehicles-and-Profiles#vehicleprofile) |  |

### ChangeRouteVehicleProfileResponse

Message type `115`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |
| 2 | `warningMessage` | repeated `string` |  |

### GetParametersSelectionValuesRequest

Message type `106`. Answered with [GetParametersSelectionValuesResponse](#getparametersselectionvaluesresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |
| 3 | `algorithm` | required [TraverseAlgorithm](Objects-Missions-and-Routes#traversealgorithm) |  |

### GetParametersSelectionValuesResponse

Message type `107`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `selections` | repeated [Selection](Objects-Missions-and-Routes#selection) |  |

### CalculateSplineRequest

Message type `112`. Answered with [CalculateSplineResponse](#calculatesplineresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `waypounts` | repeated [Waypoint](Objects-Route-Actions#waypoint) |  |
| 3 | `accuracy` | `double` |  |

### CalculateSplineResponse

Message type `113`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `locations` | repeated [Wgs84LocationDto](API-Common-Types#wgs84locationdto) |  |

### SplitRouteBySegmentsRequest

Message type `134`. Answered with [SplitRouteBySegmentsResponse](#splitroutebysegmentsresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |
| 3 | `segmentIndices` | repeated `int32` |  |
| 4 | `overshoot` | required `double` | Default `10.0`. |

### SplitRouteBySegmentsResponse

Message type `135`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `parts` | repeated [Route](Objects-Missions-and-Routes#route) |  |

### SplitRouteByDistanceRequest

Message type `136`. Answered with [SplitRouteByDistanceResponse](#splitroutebydistanceresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |
| 4 | `distance` | required `double` |  |
| 5 | `tolerance` | required `double` | Default `10.0`. |
| 6 | `overshoot` | required `double` | Default `10.0`. |
| 7 | `limit` | required `int32` | Default `2`. |

### SplitRouteByDistanceResponse

Message type `137`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `parts` | repeated [Route](Objects-Missions-and-Routes#route) |  |

### SplitRouteByCorridorPointRequest

Message type `174`. Answered with [SplitRouteByCorridorPointResponse](#splitroutebycorridorpointresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |
| 3 | `segmentIndex` | required `int32` |  |
| 4 | `pointIndex` | required `int32` |  |

### SplitRouteByCorridorPointResponse

Message type `175`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `parts` | repeated [Route](Objects-Missions-and-Routes#route) |  |

### JoinRouteRequest

Message type `138`. Answered with [JoinRouteResponse](#joinrouteresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `parts` | repeated [Route](Objects-Missions-and-Routes#route) |  |
| 3 | `mergeEndpoints` | required `bool` | **Deprecated.** Default `true`. |

### JoinRouteResponse

Message type `139`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |

### ReconstructRouteRequest

Message type `140`. Answered with [ReconstructRouteResponse](#reconstructrouteresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `processedRoute` | required [ProcessedRoute](Objects-Processed-Routes#processedroute) |  |

### ReconstructRouteResponse

Message type `141`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |

### GetNativeRouteRequest

Message type `128`. Answered with [GetNativeRouteResponse](#getnativerouteresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `processedRoute` | required [ProcessedRoute](Objects-Processed-Routes#processedroute) |  |
| 3 | `vehicleProfile` | required [VehicleProfile](Objects-Vehicles-and-Profiles#vehicleprofile) |  |

### GetNativeRouteResponse

Message type `129`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `nativeRoute` | required `bytes` |  |

### UploadRouteRequest

Message type `66`. Answered with [UploadRouteResponse](#uploadrouteresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicle` | required [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 3 | `processedRoute` | required [ProcessedRoute](Objects-Processed-Routes#processedroute) |  |
| 4 | `startIndex` | `int32` |  |
| 5 | `addFirstWaypoint` | `bool` | Default `true`. |
| 6 | `startLocation` | [Wgs84LocationDto](API-Common-Types#wgs84locationdto) | if startLocation != null -> startAction != null startLocation strictly before startAction. |
| 7 | `suppressErrorCodes` | repeated `int32` |  |

### UploadRouteResponse

Message type `67`.

No fields.

### UploadRouteRequest2

Message type `231`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicle` | required [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 3 | `processedRoute` | required [ProcessedRoute](Objects-Processed-Routes#processedroute) |  |
| 4 | `resumeOptions` | [RouteResumeOptions](#routeresumeoptions) | If null, the route will start from the 1st waypoint. Otherwise the route will be continue according to the provided options. |
| 5 | `suppressErrorCodes` | repeated `int32` |  |

### ResolveGsdByAltitudeAglRequest

Message type `212`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `payloadProfile` | required [PayloadProfile](Objects-Vehicles-and-Profiles#payloadprofile) |  |
| 3 | `altitudeAgl` | required `double` |  |
| 4 | `camTopFacingForward` | required `bool` |  |

### ResolveGsdByAltitudeAmslRequest

Message type `213`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `payloadProfile` | required [PayloadProfile](Objects-Vehicles-and-Profiles#payloadprofile) |  |
| 3 | `figurePoints` | repeated [FigurePoint](Objects-Missions-and-Routes#figurepoint) |  |
| 4 | `altitudeAmsl` | required `double` |  |
| 5 | `camTopFacingForward` | required `bool` |  |

### ResolveAltitudeAglByGsdRequest

Message type `214`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `payloadProfile` | required [PayloadProfile](Objects-Vehicles-and-Profiles#payloadprofile) |  |
| 3 | `gsd` | required `double` |  |
| 4 | `camTopFacingForward` | required `bool` |  |

### ResolveAltitudeAmslByGsdRequest

Message type `215`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `payloadProfile` | required [PayloadProfile](Objects-Vehicles-and-Profiles#payloadprofile) |  |
| 3 | `figurePoints` | repeated [FigurePoint](Objects-Missions-and-Routes#figurepoint) |  |
| 4 | `gsd` | required `double` |  |
| 5 | `camTopFacingForward` | required `bool` |  |

### SubmitUtmOperationRequest

Message type `118`. Answered with [SubmitUtmOperationResponse](#submitutmoperationresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `utmOperation` | required [UtmOperation](Objects-Processed-Routes#utmoperation) |  |
| 3 | `processedRoute` | required [ProcessedRoute](Objects-Processed-Routes#processedroute) |  |

### SubmitUtmOperationResponse

Message type `119`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `utmOperation` | required [UtmOperation](Objects-Processed-Routes#utmoperation) |  |

## Other messages and types

### SingleValueResponse

Message type `216`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `doubleValue` | `double` |  |

### ResumePoint

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `targetWaypointIndex` | `int32` | Required, Must be > 0. An index of the route waypoint the drone should start fly to. |
| 2 | `location` | [Wgs84LocationDto](API-Common-Types#wgs84locationdto) | Optional. A location on the route path, the route should start from. If null, the route starts from the waypoint defined by targetWaypointIndex. If passed location is 100 meters (or more) far from the rote, uploading fails. |

### RouteResumeOptions

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `resumePoint` | [ResumePoint](#resumepoint) | Required. |
| 2 | `goThroughFirstWaypoint` | `bool` | Required. Default `true`. |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
