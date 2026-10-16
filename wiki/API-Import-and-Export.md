Routes, missions and profiles from and to files: UgCS formats, KML, CSV and DJI WPML. See [[Import and Export]].

## Requests

- [ImportMissionOrRouteFromUgcsRequest](#importmissionorroutefromugcsrequest)
- [ImportMissionFromUgcsRequest](#importmissionfromugcsrequest)
- [ImportRouteFromUgcsRequest](#importroutefromugcsrequest)
- [ImportProfilesFromUgcsRequest](#importprofilesfromugcsrequest)
- [ImportRouteFromCsvRequest](#importroutefromcsvrequest)
- [ImportRouteFromKmlRequest](#importroutefromkmlrequest)
- [ExportMissionToUgcsRequest](#exportmissiontougcsrequest)
- [ExportRouteToUgcsRequest](#exportroutetougcsrequest)
- [ExportProfilesToUgcsRequest](#exportprofilestougcsrequest)
- [ExportRouteToKmlRequest](#exportroutetokmlrequest)
- [ExportRouteToWpmlRequest](#exportroutetowpmlrequest)
- [ExportRouteRequest](#exportrouterequest)
- [ImportRouteRequest](#importrouterequest)
- [ExportMissionRequest](#exportmissionrequest)
- [ImportMissionRequest](#importmissionrequest)
- [GetKmlRouteRepresentationRequest](#getkmlrouterepresentationrequest)

### ImportMissionOrRouteFromUgcsRequest

Message type `182`. Answered with [ImportMissionOrRouteFromUgcsResponse](#importmissionorroutefromugcsresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `data` | required `bytes` |  |

### ImportMissionOrRouteFromUgcsResponse

Message type `183`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `mission` | [Mission](Objects-Missions-and-Routes#mission) |  |
| 2 | `route` | [Route](Objects-Missions-and-Routes#route) |  |

### ImportMissionFromUgcsRequest

Message type `184`. Answered with [ImportMissionFromUgcsResponse](#importmissionfromugcsresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `data` | required `bytes` |  |

### ImportMissionFromUgcsResponse

Message type `185`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `mission` | required [Mission](Objects-Missions-and-Routes#mission) |  |

### ImportRouteFromUgcsRequest

Message type `186`. Answered with [ImportRouteFromUgcsResponse](#importroutefromugcsresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `data` | required `bytes` |  |

### ImportRouteFromUgcsResponse

Message type `187`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |

### ImportProfilesFromUgcsRequest

Message type `188`. Answered with [ImportProfilesFromUgcsResponse](#importprofilesfromugcsresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `data` | required `bytes` |  |

### ImportProfilesFromUgcsResponse

Message type `189`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `vehicleProfiles` | repeated [ImportedVehicleProfile](Objects-Vehicles-and-Profiles#importedvehicleprofile) |  |
| 2 | `payloadProfiles` | repeated [ImportedPayloadProfile](Objects-Vehicles-and-Profiles#importedpayloadprofile) |  |
| 3 | `skippedVehicleProfiles` | repeated [ImportedVehicleProfile](Objects-Vehicles-and-Profiles#importedvehicleprofile) |  |
| 4 | `skippedPayloadProfiles` | repeated [ImportedPayloadProfile](Objects-Vehicles-and-Profiles#importedpayloadprofile) |  |

### ImportRouteFromCsvRequest

Message type `190`. Answered with [ImportRouteFromCsvResponse](#importroutefromcsvresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `data` | required `bytes` |  |

### ImportRouteFromCsvResponse

Message type `191`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |

### ImportRouteFromKmlRequest

Message type `192`. Answered with [ImportRouteFromKmlResponse](#importroutefromkmlresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `data` | required `bytes` |  |
| 3 | `lineAlgorithm` | required [TraverseAlgorithm](Objects-Missions-and-Routes#traversealgorithm) |  |
| 4 | `polygonAlgorithm` | required [TraverseAlgorithm](Objects-Missions-and-Routes#traversealgorithm) |  |

### ImportRouteFromKmlResponse

Message type `193`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |

### ExportMissionToUgcsRequest

Message type `194`. Answered with [ExportMissionToUgcsResponse](#exportmissiontougcsresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `mission` | required [Mission](Objects-Missions-and-Routes#mission) |  |

### ExportMissionToUgcsResponse

Message type `195`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `data` | required `bytes` |  |

### ExportRouteToUgcsRequest

Message type `196`. Answered with [ExportRouteToUgcsResponse](#exportroutetougcsresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |

### ExportRouteToUgcsResponse

Message type `197`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `data` | required `bytes` |  |

### ExportProfilesToUgcsRequest

Message type `198`. Answered with [ExportProfilesToUgcsResponse](#exportprofilestougcsresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `vehicleProfiles` | repeated [VehicleProfile](Objects-Vehicles-and-Profiles#vehicleprofile) |  |
| 3 | `payloadProfiles` | repeated [PayloadProfile](Objects-Vehicles-and-Profiles#payloadprofile) |  |

### ExportProfilesToUgcsResponse

Message type `199`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `data` | required `bytes` |  |

### ExportRouteToKmlRequest

Message type `164`. Answered with [ExportRouteToKmlResponse](#exportroutetokmlresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `route` | [ProcessedRoute](Objects-Processed-Routes#processedroute) |  |
| 3 | `altitudeMode` | required [KmlExportAltitudeMode](Enums#kmlexportaltitudemode) |  |
| 4 | `schema` | [KmlExportSchema](Enums#kmlexportschema) |  |

### ExportRouteToKmlResponse

Message type `165`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `kmlData` | required `bytes` |  |

### ExportRouteToWpmlRequest

Message type `172`. Answered with [ExportRouteToWpmlResponse](#exportroutetowpmlresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `route` | required [ProcessedRoute](Objects-Processed-Routes#processedroute) |  |
| 3 | `altitudeMode` | required [WpmlExportAltitudeMode](Enums#wpmlexportaltitudemode) |  |

### ExportRouteToWpmlResponse

Message type `173`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `routeData` | required `bytes` |  |

### ExportRouteRequest

**Deprecated.** Use specific ExportRouteTo[FORMAT] message. Message type `54`. Answered with [ExportRouteResponse](#exportrouteresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |

### ExportRouteResponse

**Deprecated.** Message type `55`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `routeData` | required `bytes` |  |

### ImportRouteRequest

**Deprecated.** Use specific ImportRouteFrom[FORMAT] message. Message type `56`. Answered with [ImportRouteResponse](#importrouteresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `routeData` | required `bytes` |  |
| 3 | `filename` | required `string` |  |
| 4 | `polygonAlgorithm` | [TraverseAlgorithm](Objects-Missions-and-Routes#traversealgorithm) |  |
| 5 | `parameters` | repeated [ImportRouteParameterDto](#importrouteparameterdto) |  |
| 6 | `lineAlgorithm` | [TraverseAlgorithm](Objects-Missions-and-Routes#traversealgorithm) |  |

### ImportRouteResponse

**Deprecated.** Message type `57`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |

### ExportMissionRequest

**Deprecated.** Use specific ExportMissionTo[FORMAT] message. Message type `72`. Answered with [ExportMissionResponse](#exportmissionresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `mission` | required [Mission](Objects-Missions-and-Routes#mission) |  |

### ExportMissionResponse

**Deprecated.** Message type `73`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `missionData` | required `bytes` |  |

### ImportMissionRequest

**Deprecated.** Use specific ImportMissionFrom[FORMAT] message. Message type `74`. Answered with [ImportMissionResponse](#importmissionresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `missionData` | required `bytes` |  |
| 3 | `filename` | `string` |  |

### ImportMissionResponse

**Deprecated.** Message type `75`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `mission` | required [Mission](Objects-Missions-and-Routes#mission) |  |

### GetKmlRouteRepresentationRequest

**Deprecated.** Use ExportRouteToKmlRequest. Message type `38`. Answered with [GetKmlRouteRepresentationResponse](#getkmlrouterepresentationresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `routeName` | required `string` |  |
| 3 | `routeVehicleProfile` | required [VehicleProfile](Objects-Vehicles-and-Profiles#vehicleprofile) |  |
| 4 | `waypoints` | repeated [Wgs84LocationDto](API-Common-Types#wgs84locationdto) |  |
| 5 | `altitudeMode` | required [KmlExportAltitudeMode](Enums#kmlexportaltitudemode) |  |

### GetKmlRouteRepresentationResponse

**Deprecated.** Message type `39`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `kmlData` | required `bytes` |  |

## Other messages and types

### ImportRouteParameterDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `algorithmClass` | required `string` |  |
| 2 | `parameterName` | required `string` |  |
| 3 | `parameterValue` | required `string` |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
