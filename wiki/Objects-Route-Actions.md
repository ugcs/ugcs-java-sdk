Actions in a route: the definitions planned on a segment, and the actions of a processed route which the vehicle executes.

## Contents

- [Action](#action)
- [Waypoint](#waypoint)
- [CameraControl](#cameracontrol)
- [CameraTrigger](#cameratrigger)
- [CameraSeriesByTime](#cameraseriesbytime)
- [CameraSeriesByDistance](#cameraseriesbydistance)
- [Wait](#wait)
- [HeadingChange](#headingchange)
- [LidarRecordingControl](#lidarrecordingcontrol)
- [LidarCalibration](#lidarcalibration)
- [PoiChange](#poichange)
- [SpeedChange](#speedchange)
- [Takeoff](#takeoff)
- [Landing](#landing)
- [Panorama](#panorama)
- [RouteAttributes](#routeattributes)
- [SetServo](#setservo)
- [RepeatServo](#repeatservo)
- [TransitionFixed](#transitionfixed)
- [TransitionVtol](#transitionvtol)
- [HopAction](#hopaction)
- [Drop](#drop)
- [ActionDefinition](#actiondefinition)
- [CameraControlDefinition](#cameracontroldefinition)
- [CameraTriggerDefinition](#cameratriggerdefinition)
- [CameraSeriesByTimeDefinition](#cameraseriesbytimedefinition)
- [CameraSeriesByDistanceDefinition](#cameraseriesbydistancedefinition)
- [LidarRecordingControlDefinition](#lidarrecordingcontroldefinition)
- [LidarCalibrationDefinition](#lidarcalibrationdefinition)
- [HeadingDefinition](#headingdefinition)
- [PoiDefinition](#poidefinition)
- [WaitDefinition](#waitdefinition)
- [PanoramaDefinition](#panoramadefinition)
- [SetServoDefinition](#setservodefinition)
- [RepeatServoDefinition](#repeatservodefinition)
- [TransitionFixedDefinition](#transitionfixeddefinition)
- [TransitionVtolDefinition](#transitionvtoldefinition)
- [HopActionDefinition](#hopactiondefinition)
- [DropDefinition](#dropdefinition)

### Action

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 4 | `origin` | [ObjectOrigin](Enums#objectorigin) |  |
| 5 | `processedSegment` | [ProcessedRoute](Objects-Processed-Routes#processedroute) |  |
| 6 | `processedConnection` | [ProcessedRoute](Objects-Processed-Routes#processedroute) |  |
| 7 | `optimizedRoute` | [ProcessedRoute](Objects-Processed-Routes#processedroute) |  |
| 8 | `waypoint` | [Waypoint](#waypoint) |  |
| 9 | `speedChange` | [SpeedChange](#speedchange) |  |
| 11 | `wait` | [Wait](#wait) |  |
| 12 | `poiChange` | [PoiChange](#poichange) |  |
| 13 | `headingChange` | [HeadingChange](#headingchange) |  |
| 14 | `cameraControl` | [CameraControl](#cameracontrol) |  |
| 15 | `cameraTrigger` | [CameraTrigger](#cameratrigger) |  |
| 16 | `cameraSeriesByTime` | [CameraSeriesByTime](#cameraseriesbytime) |  |
| 17 | `cameraSeriesByDistance` | [CameraSeriesByDistance](#cameraseriesbydistance) |  |
| 18 | `takeoff` | [Takeoff](#takeoff) |  |
| 19 | `landing` | [Landing](#landing) |  |
| 20 | `panorama` | [Panorama](#panorama) |  |
| 22 | `routeAttributes` | [RouteAttributes](#routeattributes) |  |
| 23 | `setServo` | [SetServo](#setservo) |  |
| 24 | `repeatServo` | [RepeatServo](#repeatservo) |  |
| 26 | `transitionVtol` | [TransitionVtol](#transitionvtol) |  |
| 27 | `transitionFixed` | [TransitionFixed](#transitionfixed) |  |
| 28 | `lidarRecordingControl` | [LidarRecordingControl](#lidarrecordingcontrol) |  |
| 29 | `lidarCalibration` | [LidarCalibration](#lidarcalibration) |  |
| 30 | `hopAction` | [HopAction](#hopaction) |  |
| 31 | `drop` | [Drop](#drop) |  |
| 21 | `tag` | `string` |  |
| 25 | `estimatedTime` | `double` |  |

### Waypoint

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `latitude` | `double` |  |
| 4 | `longitude` | `double` |  |
| 5 | `wgs84Altitude` | `double` |  |
| 6 | `elevation` | `double` |  |
| 7 | `heading` | `double` |  |
| 8 | `acceptanceRadius` | `double` |  |
| 9 | `turnType` | [WaypointTurnType](Enums#waypointturntype) |  |
| 11 | `altitudeFrame` | [AltitudeFrame](Enums#altitudeframe) |  |
| 10 | `tag` | `string` |  |
| 12 | `cornerRadius` | `double` |  |
| 13 | `tags` | repeated [WaypointTag](Enums#waypointtag) |  |

### CameraControl

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `tilt` | `double` |  |
| 4 | `roll` | `double` |  |
| 5 | `yaw` | `double` |  |
| 6 | `zoomLevel` | `int32` |  |
| 7 | `tag` | `string` |  |

### CameraTrigger

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `state` | [TriggerState](Enums#triggerstate) |  |
| 5 | `tag` | `string` |  |

### CameraSeriesByTime

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `interval` | `double` |  |
| 4 | `shotsNumber` | `int32` |  |
| 5 | `startDelay` | `double` |  |
| 7 | `autoCalc` | `bool` |  |
| 6 | `tag` | `string` |  |

### CameraSeriesByDistance

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `interval` | `double` |  |
| 4 | `shotsNumber` | `int32` |  |
| 5 | `startDelay` | `double` |  |
| 7 | `autoCalc` | `bool` |  |
| 6 | `tag` | `string` |  |

### Wait

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `interval` | `double` |  |
| 7 | `scheduledTime` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 4 | `waitForOperator` | `bool` |  |
| 6 | `waitForInstant` | `bool` |  |
| 5 | `tag` | `string` |  |

### HeadingChange

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `heading` | `double` |  |
| 4 | `relativeToNextWaypoint` | `bool` |  |
| 5 | `relativeToNorth` | `bool` |  |
| 6 | `tag` | `string` |  |

### LidarRecordingControl

Start/Stop lidar data recording.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `targetState` | [RecordingState](Enums#recordingstate) |  |
| 4 | `tag` | `string` |  |

### LidarCalibration

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `direction` | [LidarCalibrationDirection](Enums#lidarcalibrationdirection) |  |
| 4 | `distance` | `double` |  |
| 5 | `times` | `int32` |  |
| 6 | `tag` | `string` |  |

### PoiChange

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `mode` | [PoiMode](Enums#poimode) |  |
| 4 | `latitude` | `double` |  |
| 5 | `longitude` | `double` |  |
| 6 | `wgs84Altitude` | `double` |  |
| 7 | `tag` | `string` |  |

### SpeedChange

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `groundSpeed` | `double` |  |
| 4 | `rateOfClimb` | `double` |  |
| 5 | `tag` | `string` |  |
| 6 | `speed` | `double` |  |

### Takeoff

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `acceptanceRadius` | `double` |  |
| 4 | `rateOfClimb` | `double` |  |
| 5 | `latitude` | `double` |  |
| 6 | `longitude` | `double` |  |
| 7 | `wgs84Altitude` | `double` |  |
| 8 | `elevation` | `double` |  |
| 9 | `tag` | `string` |  |

### Landing

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `acceptanceRadius` | `double` |  |
| 4 | `rateOfDescent` | `double` |  |
| 5 | `latitude` | `double` |  |
| 6 | `longitude` | `double` |  |
| 7 | `wgs84Altitude` | `double` |  |
| 8 | `elevation` | `double` |  |
| 9 | `tag` | `string` |  |

### Panorama

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `angle` | `double` |  |
| 4 | `stepAngle` | `double` |  |
| 5 | `stepDelay` | `double` |  |
| 6 | `rotationDirection` | [RotationDirection](Enums#rotationdirection) |  |
| 7 | `rotationSpeed` | `double` |  |
| 8 | `panoramaMode` | [PanoramaMode](Enums#panoramamode) |  |
| 9 | `tag` | `string` |  |

### RouteAttributes

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `safeAltitude` | `double` |  |
| 9 | `takeoffAltitude` | `double` |  |
| 4 | `rcLostAction` | [FailsafeAction](Enums#failsafeaction) |  |
| 5 | `gpsLostAction` | [FailsafeAction](Enums#failsafeaction) |  |
| 6 | `lowBatteryAction` | [FailsafeAction](Enums#failsafeaction) |  |
| 7 | `datalinkLostAction` | [FailsafeAction](Enums#failsafeaction) |  |
| 8 | `tag` | `string` |  |

### SetServo

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `servoId` | `int32` |  |
| 4 | `pwm` | `int32` |  |
| 5 | `tag` | `string` |  |

### RepeatServo

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `servoId` | `int32` |  |
| 4 | `pwm` | `int32` |  |
| 5 | `delay` | `double` |  |
| 6 | `count` | `int32` |  |
| 7 | `tag` | `string` |  |

### TransitionFixed

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `tag` | `string` |  |

### TransitionVtol

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `tag` | `string` |  |

### HopAction

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `tag` | `string` |  |
| 5 | `interval` | `double` |  |
| 6 | `descentAltitude` | `double` |  |
| 7 | `waitTime` | `double` |  |
| 8 | `dropAction` | `bool` |  |
| 9 | `descentSpeed` | `double` |  |

### Drop

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `tag` | `string` |  |

### ActionDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `uuid` | `string` | A UUID, as a string. |
| 5 | `segmentDefinition` | [SegmentDefinition](Objects-Missions-and-Routes#segmentdefinition) |  |
| 6 | `waitDefinition` | [WaitDefinition](#waitdefinition) |  |
| 7 | `poiDefinition` | [PoiDefinition](#poidefinition) |  |
| 8 | `headingDefinition` | [HeadingDefinition](#headingdefinition) |  |
| 9 | `cameraControlDefinition` | [CameraControlDefinition](#cameracontroldefinition) |  |
| 10 | `cameraTriggerDefinition` | [CameraTriggerDefinition](#cameratriggerdefinition) |  |
| 11 | `cameraSeriesByTimeDefinition` | [CameraSeriesByTimeDefinition](#cameraseriesbytimedefinition) |  |
| 12 | `cameraSeriesByDistanceDefinition` | [CameraSeriesByDistanceDefinition](#cameraseriesbydistancedefinition) |  |
| 13 | `panoramaDefinition` | [PanoramaDefinition](#panoramadefinition) |  |
| 14 | `setServoDefinition` | [SetServoDefinition](#setservodefinition) |  |
| 15 | `repeatServoDefinition` | [RepeatServoDefinition](#repeatservodefinition) |  |
| 17 | `transitionVtolDefinition` | [TransitionVtolDefinition](#transitionvtoldefinition) |  |
| 18 | `transitionFixedDefinition` | [TransitionFixedDefinition](#transitionfixeddefinition) |  |
| 19 | `lidarRecordingControlDefinition` | [LidarRecordingControlDefinition](#lidarrecordingcontroldefinition) |  |
| 20 | `lidarCalibrationDefinition` | [LidarCalibrationDefinition](#lidarcalibrationdefinition) |  |
| 21 | `hopActionDefinition` | [HopActionDefinition](#hopactiondefinition) |  |
| 22 | `DropDefinition` | [DropDefinition](#dropdefinition) |  |
| 16 | `tag` | `string` |  |

### CameraControlDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `tilt` | `double` |  |
| 4 | `roll` | `double` |  |
| 5 | `yaw` | `double` |  |
| 6 | `zoomLevel` | `int32` |  |
| 7 | `tag` | `string` |  |

### CameraTriggerDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `state` | [TriggerState](Enums#triggerstate) |  |
| 5 | `tag` | `string` |  |

### CameraSeriesByTimeDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 4 | `interval` | `double` |  |
| 5 | `shotsNumber` | `int32` |  |
| 6 | `startDelay` | `double` |  |
| 7 | `autoCalc` | `bool` |  |
| 8 | `tag` | `string` |  |

### CameraSeriesByDistanceDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 4 | `interval` | `double` |  |
| 5 | `shotsNumber` | `int32` |  |
| 6 | `startDelay` | `double` |  |
| 7 | `autoCalc` | `bool` |  |
| 8 | `tag` | `string` |  |

### LidarRecordingControlDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `targetState` | [RecordingState](Enums#recordingstate) |  |
| 4 | `tag` | `string` |  |

### LidarCalibrationDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `direction` | [LidarCalibrationDirection](Enums#lidarcalibrationdirection) |  |
| 4 | `distance` | `double` |  |
| 5 | `times` | `int32` |  |
| 6 | `tag` | `string` |  |

### HeadingDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `heading` | `double` |  |
| 4 | `relativeToNextWaypoint` | `bool` |  |
| 5 | `relativeToNorth` | `bool` |  |
| 6 | `tag` | `string` |  |

### PoiDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `mode` | [PoiMode](Enums#poimode) |  |
| 4 | `latitude` | `double` |  |
| 5 | `longitude` | `double` |  |
| 6 | `wgs84Altitude` | `double` |  |
| 7 | `aglAltitude` | `double` |  |
| 8 | `altitudeType` | [AltitudeType](Enums#altitudetype) |  |
| 9 | `tag` | `string` |  |

### WaitDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `interval` | `double` |  |
| 4 | `waitForOperator` | `bool` |  |
| 6 | `waitForInstant` | `bool` |  |
| 5 | `tag` | `string` |  |

### PanoramaDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `angle` | `double` |  |
| 4 | `stepAngle` | `double` |  |
| 5 | `stepDelay` | `double` |  |
| 6 | `rotationDirection` | [RotationDirection](Enums#rotationdirection) |  |
| 7 | `rotationSpeed` | `double` |  |
| 8 | `panoramaMode` | [PanoramaMode](Enums#panoramamode) |  |
| 9 | `tag` | `string` |  |

### SetServoDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `servoId` | `int32` |  |
| 4 | `pwm` | `int32` |  |
| 5 | `tag` | `string` |  |

### RepeatServoDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `servoId` | `int32` |  |
| 4 | `pwm` | `int32` |  |
| 5 | `delay` | `double` |  |
| 6 | `count` | `int32` |  |
| 7 | `tag` | `string` |  |

### TransitionFixedDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `tag` | `string` |  |

### TransitionVtolDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `tag` | `string` |  |

### HopActionDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `tag` | `string` |  |
| 5 | `interval` | `double` |  |
| 6 | `descentAltitude` | `double` |  |
| 7 | `waitTime` | `double` |  |
| 8 | `dropAction` | `bool` |  |
| 9 | `descentSpeed` | `double` |  |

### DropDefinition

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `tag` | `string` |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
