The wrapper of every object in the generic object requests, and the types shared by all areas. See [[Working with Objects]].

## Contents

- [DomainObjectWrapper](#domainobjectwrapper)
- [Version](#version)
- [License](#license)
- [User](#user)
- [MissionPreference](#missionpreference)
- [Setting](#setting)
- [Lock](#lock)
- [InvalidTakeOffAltitudeDetail](#invalidtakeoffaltitudedetail)
- [UnsupportedProtocolVersionDetail](#unsupportedprotocolversiondetail)
- [LicenseConstraintErrorDetail](#licenseconstrainterrordetail)
- [ErrorDetailWrapper](#errordetailwrapper)
- [ProtocolVersion](#protocolversion)
- [ExperimentalFeature](#experimentalfeature)

### DomainObjectWrapper

This object is a container for other domain objects that are handled by the generic CRUD service. The DOM type must be included here if it is assumed that objects of this type will be created / written / read / deleted using the generic CRUD-service (see CreateOrUpdateObject..., GetObject..., etc.).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `action` | [Action](Objects-Route-Actions#action) |  |
| 2 | `actionDefinition` | [ActionDefinition](Objects-Route-Actions#actiondefinition) |  |
| 3 | `adsbAircraft` | [AdsbAircraft](Objects-Map-and-Terrain#adsbaircraft) |  |
| 5 | `cameraControl` | [CameraControl](Objects-Route-Actions#cameracontrol) |  |
| 6 | `cameraControlDefinition` | [CameraControlDefinition](Objects-Route-Actions#cameracontroldefinition) |  |
| 7 | `cameraTrigger` | [CameraTrigger](Objects-Route-Actions#cameratrigger) |  |
| 8 | `cameraTriggerDefinition` | [CameraTriggerDefinition](Objects-Route-Actions#cameratriggerdefinition) |  |
| 9 | `commandDefinition` | [CommandDefinition](Objects-Commands#commanddefinition) |  |
| 10 | `figure` | [Figure](Objects-Missions-and-Routes#figure) |  |
| 11 | `figureParameter` | [FigureParameter](Objects-Missions-and-Routes#figureparameter) |  |
| 12 | `figurePoint` | [FigurePoint](Objects-Missions-and-Routes#figurepoint) |  |
| 13 | `headingChange` | [HeadingChange](Objects-Route-Actions#headingchange) |  |
| 14 | `headingDefinition` | [HeadingDefinition](Objects-Route-Actions#headingdefinition) |  |
| 16 | `landing` | [Landing](Objects-Route-Actions#landing) |  |
| 18 | `license` | [License](#license) |  |
| 19 | `mission` | [Mission](Objects-Missions-and-Routes#mission) |  |
| 21 | `missionPreference` | [MissionPreference](#missionpreference) |  |
| 22 | `missionVehicle` | [MissionVehicle](Objects-Missions-and-Routes#missionvehicle) |  |
| 25 | `noFlightZone` | [NoFlightZone](Objects-Map-and-Terrain#noflightzone) |  |
| 26 | `panorama` | [Panorama](Objects-Route-Actions#panorama) |  |
| 27 | `panoramaDefinition` | [PanoramaDefinition](Objects-Route-Actions#panoramadefinition) |  |
| 28 | `parameterDefinition` | [ParameterDefinition](Objects-Missions-and-Routes#parameterdefinition) |  |
| 29 | `parameterValue` | [ParameterValue](Objects-Missions-and-Routes#parametervalue) |  |
| 30 | `payloadParameter` | [PayloadParameter](Objects-Vehicles-and-Profiles#payloadparameter) |  |
| 31 | `payloadProfile` | [PayloadProfile](Objects-Vehicles-and-Profiles#payloadprofile) |  |
| 32 | `platform` | [Platform](Objects-Vehicles-and-Profiles#platform) |  |
| 33 | `platformPayloadProfile` | [PlatformPayloadProfile](Objects-Vehicles-and-Profiles#platformpayloadprofile) |  |
| 36 | `poiChange` | [PoiChange](Objects-Route-Actions#poichange) |  |
| 37 | `poiDefinition` | [PoiDefinition](Objects-Route-Actions#poidefinition) |  |
| 38 | `prism` | [Prism](Objects-Map-and-Terrain#prism) |  |
| 39 | `polygonPoint` | [PolygonPoint](Objects-Map-and-Terrain#polygonpoint) |  |
| 40 | `processedRoute` | [ProcessedRoute](Objects-Processed-Routes#processedroute) |  |
| 41 | `route` | [Route](Objects-Missions-and-Routes#route) |  |
| 42 | `segmentDefinition` | [SegmentDefinition](Objects-Missions-and-Routes#segmentdefinition) |  |
| 44 | `speedChange` | [SpeedChange](Objects-Route-Actions#speedchange) |  |
| 45 | `takeoff` | [Takeoff](Objects-Route-Actions#takeoff) |  |
| 47 | `traverseAlgorithm` | [TraverseAlgorithm](Objects-Missions-and-Routes#traversealgorithm) |  |
| 48 | `user` | [User](#user) |  |
| 49 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |
| 51 | `vehicleLogEntry` | [VehicleLogEntry](Objects-Telemetry-and-Logs#vehiclelogentry) |  |
| 52 | `vehicleParameter` | [VehicleParameter](Objects-Vehicles-and-Profiles#vehicleparameter) |  |
| 53 | `vehicleProfile` | [VehicleProfile](Objects-Vehicles-and-Profiles#vehicleprofile) |  |
| 54 | `vehicleProfilePayloadProfile` | [VehicleProfilePayloadProfile](Objects-Vehicles-and-Profiles#vehicleprofilepayloadprofile) |  |
| 55 | `vsm` | [Vsm](Objects-Vehicles-and-Profiles#vsm) |  |
| 57 | `wait` | [Wait](Objects-Route-Actions#wait) |  |
| 58 | `waitDefinition` | [WaitDefinition](Objects-Route-Actions#waitdefinition) |  |
| 59 | `waypoint` | [Waypoint](Objects-Route-Actions#waypoint) |  |
| 60 | `messageSet` | [MessageSet](Objects-Processed-Routes#messageset) |  |
| 61 | `localizedMessage` | [LocalizedMessage](Objects-Processed-Routes#localizedmessage) |  |
| 62 | `localizedMessageVariant` | [LocalizedMessageVariant](Objects-Processed-Routes#localizedmessagevariant) |  |
| 63 | `telemetryInterval` | [TelemetryInterval](Objects-Telemetry-and-Logs#telemetryinterval) |  |
| 67 | `elevationSource` | [ElevationSource](Objects-Map-and-Terrain#elevationsource) |  |
| 68 | `geoServer` | [GeoServer](Objects-Map-and-Terrain#geoserver) |  |
| 69 | `cameraSeriesByDistance` | [CameraSeriesByDistance](Objects-Route-Actions#cameraseriesbydistance) |  |
| 70 | `cameraSeriesByDistanceDefinition` | [CameraSeriesByDistanceDefinition](Objects-Route-Actions#cameraseriesbydistancedefinition) |  |
| 71 | `cameraSeriesByTime` | [CameraSeriesByTime](Objects-Route-Actions#cameraseriesbytime) |  |
| 72 | `cameraSeriesByTimeDefinition` | [CameraSeriesByTimeDefinition](Objects-Route-Actions#cameraseriesbytimedefinition) |  |
| 73 | `platformAction` | [PlatformAction](Objects-Vehicles-and-Profiles#platformaction) |  |
| 74 | `featureSource` | [FeatureSource](Objects-Map-and-Terrain#featuresource) |  |
| 76 | `utmOperation` | [UtmOperation](Objects-Processed-Routes#utmoperation) |  |
| 77 | `commandArgumentDefinition` | [CommandArgumentDefinition](Objects-Commands#commandargumentdefinition) |  |
| 78 | `failsafe` | [Failsafe](Objects-Missions-and-Routes#failsafe) |  |
| 79 | `processedSegment` | [ProcessedSegment](Objects-Processed-Routes#processedsegment) |  |
| 80 | `routeAttributes` | [RouteAttributes](Objects-Route-Actions#routeattributes) |  |
| 81 | `value` | [Value](Objects-Commands#value) |  |
| 82 | `setServo` | [SetServo](Objects-Route-Actions#setservo) |  |
| 83 | `repeatServo` | [RepeatServo](Objects-Route-Actions#repeatservo) |  |
| 84 | `transitionFixed` | [TransitionFixed](Objects-Route-Actions#transitionfixed) |  |
| 85 | `transitionVtol` | [TransitionVtol](Objects-Route-Actions#transitionvtol) |  |
| 86 | `routeUpload` | [RouteUpload](Objects-Processed-Routes#routeupload) |  |
| 87 | `routePass` | [RoutePass](Objects-Processed-Routes#routepass) |  |
| 88 | `setting` | [Setting](#setting) |  |
| 89 | `lidarRecordingControlDefinition` | [LidarRecordingControlDefinition](Objects-Route-Actions#lidarrecordingcontroldefinition) |  |
| 90 | `lidarRecordingControl` | [LidarRecordingControl](Objects-Route-Actions#lidarrecordingcontrol) |  |
| 91 | `experimentalFeature` | [ExperimentalFeature](#experimentalfeature) |  |
| 92 | `lidarCalibrationDefinition` | [LidarCalibrationDefinition](Objects-Route-Actions#lidarcalibrationdefinition) |  |
| 93 | `lidarCalibration` | [LidarCalibration](Objects-Route-Actions#lidarcalibration) |  |
| 94 | `notice` | [Notice](Objects-Telemetry-and-Logs#notice) |  |

### Version

Doesn't contain version field as not a part of domain.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `major` | `int32` |  |
| 3 | `minor` | `int32` |  |
| 5 | `patch` | `int32` |  |
| 6 | `build` | `string` |  |
| 4 | `component` | [Component](Enums#component) |  |

### License

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `type` | `string` |  |
| 4 | `activationCode` | `string` |  |
| 5 | `activationTime` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 6 | `tag` | `string` |  |
| 7 | `subscriptionExpirationTime` | `int64` | Milliseconds since 1970-01-01 UTC. Set by the server. |
| 8 | `supportExpirationTime` | `int64` | Milliseconds since 1970-01-01 UTC. Set by the server. |

### User

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `login` | `string` |  |
| 4 | `password` | `string` |  |
| 5 | `role` | [UserRole](Enums#userrole) |  |
| 6 | `tag` | `string` |  |
| 7 | `missionPreferences` | repeated [MissionPreference](#missionpreference) |  |

### MissionPreference

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `user` | [User](#user) |  |
| 4 | `mission` | [Mission](Objects-Missions-and-Routes#mission) |  |
| 5 | `name` | `string` |  |
| 6 | `value` | `string` |  |
| 7 | `tag` | `string` |  |

### Setting

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `name` | `string` |  |
| 3 | `value` | `string` |  |

### Lock

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `objectType` | `string` |  |
| 2 | `objectId` | `int32` |  |
| 3 | `owner` | [User](#user) |  |

### InvalidTakeOffAltitudeDetail

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `recommendedAltitude` | `double` |  |

### UnsupportedProtocolVersionDetail

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `serverVersion` | [ProtocolVersion](#protocolversion) |  |

### LicenseConstraintErrorDetail

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `licenseConstraintErrorSubtype` | [LicenseConstraintErrorSubtype](Enums#licenseconstrainterrorsubtype) |  |
| 2 | `permission` | `string` |  |

### ErrorDetailWrapper

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `invalidTakeOffAltitudeDetail` | [InvalidTakeOffAltitudeDetail](#invalidtakeoffaltitudedetail) |  |
| 2 | `unsupportedProtocolVersionDetail` | [UnsupportedProtocolVersionDetail](#unsupportedprotocolversiondetail) |  |
| 3 | `licenseConstraintErrorDetail` | [LicenseConstraintErrorDetail](#licenseconstrainterrordetail) |  |

### ProtocolVersion

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `major` | `int32` |  |
| 2 | `minor` | `int32` |  |

### ExperimentalFeature

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `tag` | `string` |  |
| 4 | `code` | `string` |  |
| 5 | `description` | `string` |  |
| 6 | `enabled` | `bool` |  |
| 7 | `isAppRestartRequired` | `bool` |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
