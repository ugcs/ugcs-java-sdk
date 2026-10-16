The generic requests which read, create, update and delete the objects of the UgCS database (vehicles, missions, routes, profiles...), and the mapping of what each platform supports. See [[Working with Objects]].

## Requests

- [GetObjectListRequest](#getobjectlistrequest)
- [GetObjectRequest](#getobjectrequest)
- [CreateOrUpdateObjectRequest](#createorupdateobjectrequest)
- [UpdateObjectFieldsRequest](#updateobjectfieldsrequest)
- [DeleteObjectRequest](#deleteobjectrequest)
- [GetMappingRequest](#getmappingrequest)
- [GetMissionPreferencesRequest](#getmissionpreferencesrequest)
- [UpdateMissionPreferencesRequest](#updatemissionpreferencesrequest)
- [GetRouteListRequest](#getroutelistrequest)

### GetObjectListRequest

Message type `10`. Answered with [GetObjectListResponse](#getobjectlistresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `objectType` | required `string` | The message name of the objects, e.g. `Vehicle`, `Route`. |
| 3 | `refreshDependencies` | `bool` | Fill in the objects this one refers to; otherwise these references are left empty. Default `false`. |
| 4 | `refreshExcludes` | repeated `string` |  |
| 5 | `translate` | `bool` | Default `true`. |

### GetObjectListResponse

Message type `11`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `objectType` | required `string` |  |
| 2 | `objects` | repeated [DomainObjectWrapper](Objects-Common#domainobjectwrapper) |  |

### GetObjectRequest

Message type `12`. Answered with [GetObjectResponse](#getobjectresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `objectType` | required `string` | The message name of the object, e.g. `Vehicle`, `Route`. |
| 3 | `objectId` | required `int32` |  |
| 4 | `refreshDependencies` | `bool` | Fill in the objects this one refers to; otherwise these references are left empty. Default `false`. |
| 5 | `refreshExcludes` | repeated `string` |  |
| 6 | `translate` | `bool` | Default `true`. |

### GetObjectResponse

Message type `13`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `objectType` | required `string` |  |
| 2 | `object` | required [DomainObjectWrapper](Objects-Common#domainobjectwrapper) |  |

### CreateOrUpdateObjectRequest

Message type `14`. Answered with [CreateOrUpdateObjectResponse](#createorupdateobjectresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `objectType` | required `string` | The message name of the object, e.g. `Mission`. |
| 3 | `object` | required [DomainObjectWrapper](Objects-Common#domainobjectwrapper) |  |
| 4 | `withComposites` | `bool` | Apply the operation to the objects referred to as composite parts as well. Default `true`. |
| 5 | `acquireLock` | `bool` | Default `false`. |

### CreateOrUpdateObjectResponse

Message type `15`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `objectType` | required `string` |  |
| 2 | `object` | required [DomainObjectWrapper](Objects-Common#domainobjectwrapper) |  |

### UpdateObjectFieldsRequest

Message type `18`. Answered with [UpdateObjectFieldsResponse](#updateobjectfieldsresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `objectType` | required `string` |  |
| 3 | `object` | required [DomainObjectWrapper](Objects-Common#domainobjectwrapper) |  |
| 4 | `fieldNames` | repeated `string` |  |
| 5 | `acquireLock` | `bool` | Default `false`. |

### UpdateObjectFieldsResponse

Message type `19`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `objectType` | required `string` |  |
| 2 | `object` | required [DomainObjectWrapper](Objects-Common#domainobjectwrapper) |  |

### DeleteObjectRequest

Message type `16`. Answered with [DeleteObjectResponse](#deleteobjectresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `objectType` | required `string` |  |
| 3 | `objectId` | required `int32` |  |

### DeleteObjectResponse

Message type `17`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `objectType` | required `string` |  |
| 2 | `objectId` | required `int32` |  |
| 3 | `objectVersion` | `int32` |  |

### GetMappingRequest

Message type `124`. Answered with [GetMappingResponse](#getmappingresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `getAlgorithms` | `bool` |  |
| 3 | `getFailsafeActions` | `bool` |  |
| 4 | `getWaypointTurnTypes` | `bool` |  |
| 5 | `getActionExecutions` | `bool` |  |
| 6 | `getCameras` | `bool` |  |
| 7 | `getAltitudeTypes` | `bool` |  |
| 9 | `getFacadeScanPatterns` | `bool` |  |
| 10 | `getAltitudeFrames` | `bool` |  |
| 11 | `getPlatformParameters` | `bool` |  |
| 12 | `getPayloadParameters` | `bool` |  |
| 13 | `getCircleVerticalDirections` | `bool` |  |

### GetMappingResponse

Message type `125`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` |  |
| 2 | `algorithmsMapping` | [AlgorithmsMappingDto](#algorithmsmappingdto) |  |
| 3 | `failsafeActionsMapping` | [FailsafeActionsMappingDto](#failsafeactionsmappingdto) |  |
| 4 | `waypointTurnTypesMapping` | [TurnTypesMappingDto](#turntypesmappingdto) |  |
| 5 | `actionExecutionsMapping` | [ActionExecutionsMappingDto](#actionexecutionsmappingdto) |  |
| 6 | `camerasMapping` | [CamerasMappingDto](#camerasmappingdto) |  |
| 7 | `altitudeTypesMapping` | [AltitudeTypesMappingDto](#altitudetypesmappingdto) |  |
| 9 | `facadeScanPaterrnsMapping` | [FacadeScanPatternsMappingDto](#facadescanpatternsmappingdto) |  |
| 10 | `altitudeFramesMapping` | [AltitudeFramesMappingDto](#altitudeframesmappingdto) |  |
| 11 | `platformParameters` | repeated [PlatformParametersDto](#platformparametersdto) |  |
| 12 | `payloadParameters` | repeated [PayloadParametersMappingDto](#payloadparametersmappingdto) |  |
| 13 | `circleVerticalDirectionsMapping` | [CircleVerticalDirectionsMappingDto](#circleverticaldirectionsmappingdto) |  |

### GetMissionPreferencesRequest

Message type `84`. Answered with [GetMissionPreferencesResponse](#getmissionpreferencesresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `user` | required [User](Objects-Common#user) |  |
| 3 | `mission` | [Mission](Objects-Missions-and-Routes#mission) |  |

### GetMissionPreferencesResponse

Message type `85`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `preferences` | repeated [MissionPreference](Objects-Common#missionpreference) |  |

### UpdateMissionPreferencesRequest

Message type `86`. Answered with [UpdateMissionPreferencesResponse](#updatemissionpreferencesresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `preferences` | repeated [MissionPreference](Objects-Common#missionpreference) |  |

### UpdateMissionPreferencesResponse

Message type `87`.

No fields.

### GetRouteListRequest

Message type `202`. Answered with [GetRouteListResponse](#getroutelistresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `filter` | [RouteListFilterDto](#routelistfilterdto) |  |

### GetRouteListResponse

Message type `203`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `routes` | repeated [Route](Objects-Missions-and-Routes#route) |  |

## Other messages and types

### AlgorithmsMappingDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `algorithms` | repeated [AlgorithmsDto](#algorithmsdto) |  |

### FailsafeActionsMappingDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `failsafeActions` | repeated [FailsafeActionsDto](#failsafeactionsdto) |  |

### TurnTypesMappingDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `platformTurnTypes` | repeated [PlatformTurnTypeDto](#platformturntypedto) |  |

### ActionExecutionsMappingDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `algorithmActionExecutions` | repeated [AlgorithmActionExecutionDto](#algorithmactionexecutiondto) |  |

### CamerasMappingDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `profileCameras` | repeated [ProfileCameraDto](#profilecameradto) |  |

### AltitudeTypesMappingDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `altitudeTypes` | repeated [AltitudeTypeDto](#altitudetypedto) |  |

### FacadeScanPatternsMappingDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `facadeScanPatterns` | repeated [FacadeScanPatternDto](#facadescanpatterndto) |  |

### AltitudeFramesMappingDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `platformAltitudeFrames` | repeated [PlatformAltitudeFramesDto](#platformaltitudeframesdto) |  |
| 2 | `algorithmAltitudeFrames` | repeated [AlgorithmAltitudeFramesDto](#algorithmaltitudeframesdto) |  |

### PlatformParametersDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `platformAndVehicleType` | required [PlatformAndVehicleTypePairDto](#platformandvehicletypepairdto) |  |
| 2 | `parameters` | required [PlatformParameters](Objects-Vehicles-and-Profiles#platformparameters) |  |

### PayloadParametersMappingDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `payloadType` | required [PayloadType](Enums#payloadtype) |  |
| 2 | `parameterTypes` | repeated [PayloadParameterType](Enums#payloadparametertype) |  |

### CircleVerticalDirectionsMappingDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `circleVerticalDirections` | repeated [CircleVerticalDirectionDto](#circleverticaldirectiondto) |  |

### PlatformAndVehicleTypePairDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `platform` | required [Platform](Objects-Vehicles-and-Profiles#platform) |  |
| 2 | `vehicleType` | required [VehicleType](Enums#vehicletype) |  |

### FailsafeReasonActionsDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `reason` | required [FailsafeReason](Enums#failsafereason) |  |
| 2 | `actions` | repeated [FailsafeAction](Enums#failsafeaction) |  |

### FailsafeActionsDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `platformAndVehicleType` | required [PlatformAndVehicleTypePairDto](#platformandvehicletypepairdto) |  |
| 2 | `failsafeReasonActions` | repeated [FailsafeReasonActionsDto](#failsafereasonactionsdto) |  |

### AlgorithmAndActionCodeDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `algorithm` | required [TraverseAlgorithm](Objects-Missions-and-Routes#traversealgorithm) |  |
| 2 | `actionCodes` | repeated [ActionCode](Enums#actioncode) |  |

### AlgorithmsDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `platformAndVehicleType` | required [PlatformAndVehicleTypePairDto](#platformandvehicletypepairdto) |  |
| 2 | `algorithmAndActionCodes` | repeated [AlgorithmAndActionCodeDto](#algorithmandactioncodedto) |  |

### TurnTypeDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `turnType` | required `string` |  |
| 2 | `description` | required `string` |  |

### PlatformTurnTypeDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `platformAndVehicleType` | required [PlatformAndVehicleTypePairDto](#platformandvehicletypepairdto) |  |
| 2 | `turnTypes` | repeated [TurnTypeDto](#turntypedto) |  |

### AltitudeTypeDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `altitudeType` | required `string` |  |
| 2 | `description` | required `string` |  |

### AltitudeFrameDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `altitudeFrame` | required `string` |  |
| 2 | `description` | required `string` |  |

### PlatformAltitudeFramesDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `platform` | required [Platform](Objects-Vehicles-and-Profiles#platform) |  |
| 2 | `altitudeFrames` | repeated [AltitudeFrameDto](#altitudeframedto) |  |

### AlgorithmAltitudeFramesDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `algorithm` | required [TraverseAlgorithm](Objects-Missions-and-Routes#traversealgorithm) |  |
| 2 | `altitudeFrames` | repeated [AltitudeFrameDto](#altitudeframedto) |  |

### FacadeScanPatternDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `facadeScanPattern` | required `string` |  |
| 2 | `description` | required `string` |  |

### ActionExecutionDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `actionExecution` | required `string` |  |
| 2 | `description` | required `string` |  |

### AlgorithmActionExecutionDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `algorithm` | required [TraverseAlgorithm](Objects-Missions-and-Routes#traversealgorithm) |  |
| 2 | `actionExecution` | repeated [ActionExecutionDto](#actionexecutiondto) |  |

### CircleVerticalDirectionDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `circleVerticalDirection` | required `string` |  |
| 2 | `description` | required `string` |  |

### CameraDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `cameraId` | required `int32` |  |
| 2 | `cameraName` | required `string` |  |
| 3 | `cameraShortName` | `string` |  |

### ProfileCameraDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `profileId` | required `int32` |  |
| 2 | `cameras` | repeated [CameraDto](#cameradto) |  |

### RouteListFilterDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `missionId` | `int32` |  |
| 2 | `vehiclePlatformCode` | repeated `string` |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
