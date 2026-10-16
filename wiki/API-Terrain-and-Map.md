Elevation, line of sight, no-fly zones, place search and ADS-B traffic.

## Requests

- [GetElevationProfileRequest](#getelevationprofilerequest)
- [GetElevationProfileV2Request](#getelevationprofilev2request)
- [GetRasterGridRequest](#getrastergridrequest)
- [GetRasterValuesRequest](#getrastervaluesrequest)
- [GetElevationTilesRequest](#getelevationtilesrequest)
- [GetElevationTilesV2Request](#getelevationtilesv2request)
- [SwapElevationOrderRequest](#swapelevationorderrequest)
- [GetLineOfSightRequest](#getlineofsightrequest)
- [GetNfzListRequest](#getnfzlistrequest)
- [SearchPlacesRequest](#searchplacesrequest)
- [CheckLocationRequest](#checklocationrequest)
- [GetPolygonAreaRequest](#getpolygonarearequest)
- [GetAdsbAircraftListRequest](#getadsbaircraftlistrequest)
- [GetAdsbBulbStatusRequest](#getadsbbulbstatusrequest)

### GetElevationProfileRequest

Message type `48`. Answered with [GetElevationProfileResponse](#getelevationprofileresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 3 | `samplingStep` | required `double` |  |
| 4 | `locations` | repeated [Wgs84LocationDto](API-Common-Types#wgs84locationdto) |  |
| 5 | `number` | `int32` |  |

### GetElevationProfileResponse

Message type `49`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `locations` | repeated [Wgs84LocationDto](API-Common-Types#wgs84locationdto) |  |
| 2 | `elevations` | repeated `double` |  |
| 3 | `obstacleHeight` | repeated `double` |  |

### GetElevationProfileV2Request

Message type `176`. Answered with [GetElevationProfileV2Response](#getelevationprofilev2response).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 3 | `samplingStep` | required `double` |  |
| 4 | `locations` | repeated [Wgs84LocationDto](API-Common-Types#wgs84locationdto) |  |
| 5 | `number` | `int32` |  |

### GetElevationProfileV2Response

Message type `177`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `locations` | repeated [Wgs84LocationDto](API-Common-Types#wgs84locationdto) |  |
| 2 | `elevations` | repeated `double` |  |
| 5 | `surfaceHeight` | repeated `double` |  |
| 3 | `obstacleHeight` | repeated `double` |  |
| 4 | `accumulatedDistances` | repeated `double` |  |

### GetRasterGridRequest

Message type `30`. Answered with [GetRasterGridResponse](#getrastergridresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 3 | `minLatitude` | required `double` |  |
| 4 | `minLongitude` | required `double` |  |
| 5 | `maxLatitude` | required `double` |  |
| 6 | `maxLongitude` | required `double` |  |
| 7 | `width` | required `int32` |  |
| 8 | `height` | required `int32` |  |

### GetRasterGridResponse

Message type `31`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `values` | repeated `float` |  |
| 2 | `width` | required `int32` |  |
| 3 | `height` | required `int32` |  |
| 4 | `noData` | required `bool` |  |

### GetRasterValuesRequest

Message type `34`. Answered with [GetRasterValuesResponse](#getrastervaluesresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 3 | `latitudes` | repeated `double` |  |
| 4 | `longitudes` | repeated `double` |  |

### GetRasterValuesResponse

Message type `35`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `values` | repeated `float` |  |
| 2 | `noData` | required `bool` |  |

### GetElevationTilesRequest

**Deprecated.** Request for elevation tiles in specified rectangle. Message type `162`. Answered with [GetElevationTilesResponse](#getelevationtilesresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 3 | `minLatitude` | required `double` |  |
| 4 | `minLongitude` | required `double` |  |
| 5 | `maxLatitude` | required `double` |  |
| 6 | `maxLongitude` | required `double` |  |

### GetElevationTilesResponse

**Deprecated.** Message type `163`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `data` | repeated [ElevationData](Objects-Map-and-Terrain#elevationdata) |  |

### GetElevationTilesV2Request

Request for elevation tiles in specified rectangle. Message type `170`. Answered with [GetElevationTilesV2Response](#getelevationtilesv2response).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 3 | `minLatitude` | required `double` |  |
| 4 | `minLongitude` | required `double` |  |
| 5 | `maxLatitude` | required `double` |  |
| 6 | `maxLongitude` | required `double` |  |

### GetElevationTilesV2Response

Message type `171`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `data` | repeated [ElevationData](Objects-Map-and-Terrain#elevationdata) |  |

### SwapElevationOrderRequest

Message type `146`. Answered with [SwapElevationOrderResponse](#swapelevationorderresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `source1` | required [ElevationSource](Objects-Map-and-Terrain#elevationsource) |  |
| 3 | `source2` | required [ElevationSource](Objects-Map-and-Terrain#elevationsource) |  |

### SwapElevationOrderResponse

Message type `147`.

No fields.

### GetLineOfSightRequest

Message type `46`. Answered with [GetLineOfSightResponse](#getlineofsightresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `observerPosition` | required [MeasurePointDto](#measurepointdto) |  |
| 3 | `maxDistance` | required `double` |  |
| 4 | `approxPointsNumber` | required `int32` |  |

### GetLineOfSightResponse

Message type `47`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `points` | repeated [MeasurePointDto](#measurepointdto) |  |

### GetNfzListRequest

Message type `116`. Answered with [GetNfzListResponse](#getnfzlistresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `minLatitude` | required `double` |  |
| 3 | `minLongitude` | required `double` |  |
| 4 | `maxLatitude` | required `double` |  |
| 5 | `maxLongitude` | required `double` |  |
| 6 | `refreshDependencies` | `bool` | Default `false`. |
| 7 | `refreshExcludes` | repeated `string` |  |
| 8 | `types` | repeated [NfzType](Enums#nfztype) |  |

### GetNfzListResponse

Message type `117`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `zones` | repeated [NoFlightZone](Objects-Map-and-Terrain#noflightzone) |  |

### SearchPlacesRequest

Message type `52`. Answered with [SearchPlacesResponse](#searchplacesresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `query` | required `string` |  |
| 3 | `limit` | required `int32` | Default `7`. |

### SearchPlacesResponse

Message type `53`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `places` | repeated [PlaceDto](#placedto) |  |

### CheckLocationRequest

Message type `152`. Answered with [CheckLocationResponse](#checklocationresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicle` | required [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 3 | `location` | required [Wgs84LocationDto](API-Common-Types#wgs84locationdto) |  |

### CheckLocationResponse

Message type `153`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `passable` | required `bool` |  |
| 2 | `message` | `string` |  |

### GetPolygonAreaRequest

Message type `132`. Answered with [GetPolygonAreaResponse](#getpolygonarearesponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `points` | repeated [Wgs84LocationDto](API-Common-Types#wgs84locationdto) |  |

### GetPolygonAreaResponse

Message type `133`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `area` | required `double` |  |

### GetAdsbAircraftListRequest

Message type `50`. Answered with [GetAdsbAircraftListResponse](#getadsbaircraftlistresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `location` | required [Wgs84LocationDto](API-Common-Types#wgs84locationdto) |  |
| 3 | `radius` | required `double` |  |
| 4 | `height` | required `double` |  |

### GetAdsbAircraftListResponse

Message type `51`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `aircrafts` | repeated [AdsbAircraft](Objects-Map-and-Terrain#adsbaircraft) |  |

### GetAdsbBulbStatusRequest

Message type `108`. Answered with [GetAdsbBulbStatusResponse](#getadsbbulbstatusresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |

### GetAdsbBulbStatusResponse

Message type `109`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `on` | required `bool` |  |

## Other messages and types

### MeasurePointDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `latitude` | required `double` |  |
| 2 | `longitude` | required `double` |  |
| 3 | `altitude` | required `double` |  |
| 4 | `altitudeType` | required [AltitudeType](Enums#altitudetype) |  |
| 5 | `angle` | `double` |  |
| 6 | `distance` | `double` |  |

### PlaceDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `location` | required [Wgs84LocationDto](API-Common-Types#wgs84locationdto) |  |
| 2 | `displayName` | `string` |  |
| 3 | `rank` | `double` |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
