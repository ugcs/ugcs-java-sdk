Every enum of the protocol. Each value has its number on the wire; send the value, not its position in a list.

## Contents

- [NoticeKind](#noticekind)
- [RouteProcessingStatus](#routeprocessingstatus)
- [UserRole](#userrole)
- [ObjectOrigin](#objectorigin)
- [AltitudeType](#altitudetype)
- [AltitudeFrame](#altitudeframe)
- [ActionExecution](#actionexecution)
- [FacadeScanPattern](#facadescanpattern)
- [FigureType](#figuretype)
- [ParameterType](#parametertype)
- [VehicleType](#vehicletype)
- [VehicleSource](#vehiclesource)
- [VehicleParameterType](#vehicleparametertype)
- [PayloadType](#payloadtype)
- [PayloadParameterType](#payloadparametertype)
- [SeverityLevel](#severitylevel)
- [TrajectoryType](#trajectorytype)
- [FailsafeReason](#failsafereason)
- [FailsafeAction](#failsafeaction)
- [RecordingState](#recordingstate)
- [LidarCalibrationDirection](#lidarcalibrationdirection)
- [TriggerState](#triggerstate)
- [PowerState](#powerstate)
- [PoiMode](#poimode)
- [PanoramaMode](#panoramamode)
- [RotationDirection](#rotationdirection)
- [IcaoAddressType](#icaoaddresstype)
- [Component](#component)
- [PhysicalSemantic](#physicalsemantic)
- [MessageSeverity](#messageseverity)
- [ConflictResolutionMode](#conflictresolutionmode)
- [WaypointTurnType](#waypointturntype)
- [ActionCode](#actioncode)
- [NfzType](#nfztype)
- [EnvironmentEventType](#environmenteventtype)
- [FeatureType](#featuretype)
- [UtmOperationStatus](#utmoperationstatus)
- [CommandStatus](#commandstatus)
- [Subsystem](#subsystem)
- [Semantic](#semantic)
- [KmlExportAltitudeMode](#kmlexportaltitudemode)
- [WpmlExportAltitudeMode](#wpmlexportaltitudemode)
- [WaypointTag](#waypointtag)
- [RemoteIdIdentifierType](#remoteididentifiertype)
- [RemoteIdOperatorType](#remoteidoperatortype)
- [RemoteIdPrearmStatus](#remoteidprearmstatus)
- [ParameterCategory](#parametercategory)
- [ElevationSourceCategory](#elevationsourcecategory)
- [LicenseConstraintErrorSubtype](#licenseconstrainterrorsubtype)
- [ModificationType](#modificationtype)
- [KmlExportSchema](#kmlexportschema)
- [TelemetryFormat](#telemetryformat)
- [ServerAction](#serveraction)

### NoticeKind

| Value | Number | Description |
|---|---|---|
| `NK_WARNING` | 0 |  |

### RouteProcessingStatus

| Value | Number | Description |
|---|---|---|
| `RPS_MODIFIED` | 0 | The new / modified route segment. |
| `RPS_INVALID` | 1 | The route segment is incorrect. |
| `RPS_MARKED` | 2 | The route segment is marked (defines all its basic points). |
| `RPS_PROCESSED` | 3 | The route segment is fully processed (contains all commands). |

### UserRole

| Value | Number | Description |
|---|---|---|
| `UR_ADMINISTRATOR` | 0 |  |
| `UR_OPERATOR` | 1 |  |

### ObjectOrigin

| Value | Number | Description |
|---|---|---|
| `OO_USER` | 0 |  |
| `OO_SYSTEM` | 1 |  |

### AltitudeType

| Value | Number | Description |
|---|---|---|
| `AT_WGS84` | 0 |  |
| `AT_AGL` | 1 |  |

### AltitudeFrame

| Value | Number | Description |
|---|---|---|
| `AF_WGS84` | 0 |  |
| `AF_AGL` | 1 |  |
| `AF_RANGEFINDER` | 2 |  |
| `AF_SMART_AGL` | 3 |  |

### ActionExecution

| Value | Number | Description |
|---|---|---|
| `AE_ONLY_AT_START` | 0 |  |
| `AE_ACTIONS_EVERY_POINT` | 1 |  |
| `AE_ACTIONS_ON_FORWARD_PASSES` | 2 |  |

### FacadeScanPattern

| Value | Number | Description |
|---|---|---|
| `FSP_VERTICAL` | 0 |  |
| `FSP_HORIZONTAL` | 1 |  |

### FigureType

| Value | Number | Description |
|---|---|---|
| `FT_POINT` | 0 |  |
| `FT_POLYGON` | 1 |  |
| `FT_CIRCLE` | 2 |  |
| `FT_EIGHT_CURVE` | 3 |  |
| `FT_TAKEOFF_POINT` | 4 |  |
| `FT_LANDING_POINT` | 5 |  |
| `FT_POLYLINE` | 6 |  |

### ParameterType

| Value | Number | Description |
|---|---|---|
| `PT_STRING` | 0 |  |
| `PT_INTEGER` | 1 |  |
| `PT_DOUBLE` | 2 |  |
| `PT_BOOLEAN` | 3 |  |
| `PT_SELECTION` | 4 |  |

### VehicleType

| Value | Number | Description |
|---|---|---|
| `VT_FIXED_WING` | 0 |  |
| `VT_HELICOPTER` | 1 |  |
| `VT_MULTICOPTER` | 2 |  |
| `VT_VTOL` | 3 |  |

### VehicleSource

| Value | Number | Description |
|---|---|---|
| `VS_VSM` | 0 |  |
| `VS_IMPORT` | 1 |  |

### VehicleParameterType

| Value | Number | Description |
|---|---|---|
| `VPT_HEIGHT` | 0 |  |
| `VPT_WIDTH` | 1 |  |
| `VPT_LENGTH` | 2 |  |
| `VPT_MAX_CLIMB_RATE` | 3 |  |
| `VPT_MAX_HORIZONTAL_SPEED` | 4 |  |
| `VPT_MAX_ALTITUDE` | 5 |  |
| `VPT_MAX_WAYPOINTS` | 6 |  |
| `VPT_MAX_FLIGHT_TIME` | 7 |  |
| `VPT_WIND_RESISTANCE` | 8 |  |
| `VPT_DRY_TAKEOFF_WEIGHT` | 9 |  |
| `VPT_MAX_TAKEOFF_WEIGHT` | 10 |  |
| `VPT_BATTERY_WEIGHT` | 11 |  |
| `VPT_CHARGED_BATTERY_VOLTAGE` | 12 |  |
| `VPT_DISCHARGED_BATTERY_VOLTAGE` | 13 |  |
| `VPT_NORMAL_BATTERY_VOLTAGE` | 14 |  |
| `VPT_LOW_BATTERY_VOLTAGE` | 15 |  |
| `VPT_LOW_GPS_SATELLITES` | 16 |  |
| `VPT_NORMAL_GPS_SATELLITES` | 17 |  |
| `VPT_SAFE_DISTANCE_TO_TERRAIN` | 18 |  |
| `VPT_SAFE_DISTANCE_TO_OBSTACLE` | 19 |  |
| `VPT_WAYPOINT_ACCEPTANCE_RADIUS` | 20 |  |
| `VPT_FENCE_RADIUS` | 21 |  |
| `VPT_LOW_TELEMETRY_LEVEL` | 22 |  |
| `VPT_NORMAL_TELEMETRY_LEVEL` | 23 |  |
| `VPT_DEFAULT_CLIMB_RATE` | 24 |  |
| `VPT_DEFAULT_DESCENT_RATE` | 25 |  |
| `VPT_GLIDE_SLOPE` | 26 |  |
| `VPT_DEFAULT_HORIZONTAL_SPEED` | 27 |  |
| `VPT_LANDING_GROUND_SPEED` | 28 |  |
| `VPT_MAX_ALTITUDE_AGL` | 29 |  |
| `VPT_LANDING_FLARE_ALTITUDE` | 30 |  |
| `VPT_LANDING_FLARE_TIME` | 31 |  |
| `VPT_MIN_LANDING_PITCH` | 32 |  |
| `VPT_LANDING_FLARE_DAMP` | 33 |  |
| `VPT_LANDING_APPROACH_AIRSPEED` | 34 |  |
| `VPT_LANDING_SPEED_WEIGHTING` | 35 |  |
| `VPT_MAX_AUTO_FLIGHT_PITCH` | 36 |  |
| `VPT_MAX_PITCH` | 37 |  |
| `VPT_MIN_THROTTLE` | 38 |  |
| `VPT_LANDING_SINK_RATE` | 39 |  |
| `VPT_LANDING_RANGEFINDER_ENABLED` | 40 |  |
| `VPT_MIN_RANGEFINDER_DISTANCE` | 41 |  |
| `VPT_MAX_DESCENT_RATE` | 42 |  |
| `VPT_LOW_BATTERY_POWER_LEVEL` | 43 |  |
| `VPT_NORMAL_BATTERY_POWER_LEVEL` | 44 |  |
| `VPT_MAX_RANGEFINDER_DISTANCE` | 45 |  |
| `VPT_MIN_DISTANCE_BETWEEN_WAYPOINT` | 46 |  |
| `VPT_FENCE_ALTITUDE` | 47 |  |
| `VPT_INITIAL_SPEED` | 48 |  |
| `VPT_WAYPOINT_CORNER_RADIUS` | 49 |  |
| `VPT_WAYPOINT_MAX_WAIT_TIME` | 50 |  |

### PayloadType

| Value | Number | Description |
|---|---|---|
| `PT_CAMERA` | 0 |  |
| `PT_DATA_LINK` | 1 |  |
| `PT_LIDAR` | 2 |  |

### PayloadParameterType

| Value | Number | Description |
|---|---|---|
| `PPT_WEIGHT` | 0 |  |
| `PPT_FREQUENCY` | 1 |  |
| `PPT_POWER` | 2 |  |
| `PPT_TRUE_FOCAL_LENGTH` | 3 |  |
| `PPT_SENSOR_WIDTH` | 4 |  |
| `PPT_SENSOR_HEIGHT` | 5 |  |
| `PPT_SENSOR_HORIZONTAL_PIXELS` | 6 |  |
| `PPT_SENSOR_VERTICAL_PIXELS` | 7 |  |
| `PPT_MIN_TRIGGERING_INTERVAL` | 8 |  |
| `PPT_SCANNING_FOV` | 9 |  |
| `PPT_SCANNING_RANGE` | 10 |  |
| `PPT_SCANNING_SPEED` | 11 |  |

### SeverityLevel

| Value | Number | Description |
|---|---|---|
| `SL_EMERGENCY` | 0 |  |
| `SL_ALERT` | 1 |  |
| `SL_CRITICAL` | 2 |  |
| `SL_ERROR` | 3 |  |
| `SL_WARNING` | 4 |  |
| `SL_NOTICE` | 5 |  |
| `SL_INFORMATIONAL` | 6 |  |
| `SL_DEBUG` | 7 |  |

### TrajectoryType

| Value | Number | Description |
|---|---|---|
| `TT_STRAIGHT` | 0 |  |
| `TT_STAIR` | 1 |  |

### FailsafeReason

| Value | Number | Description |
|---|---|---|
| `FR_RC_LOST` | 0 |  |
| `FR_GPS_LOST` | 1 |  |
| `FR_LOW_BATTERY` | 2 |  |
| `FR_DATALINK_LOST` | 3 |  |

### FailsafeAction

| Value | Number | Description |
|---|---|---|
| `FA_GO_HOME` | 0 |  |
| `FA_LAND` | 1 |  |
| `FA_WAIT` | 2 |  |
| `FA_CONTINUE` | 3 |  |
| `FA_NA` | 4 |  |

### RecordingState

| Value | Number | Description |
|---|---|---|
| `RS_OFF` | 0 |  |
| `RS_ON` | 1 |  |

### LidarCalibrationDirection

| Value | Number | Description |
|---|---|---|
| `LCD_FORWARD` | 0 |  |
| `LCD_BACKWARD` | 1 |  |

### TriggerState

| Value | Number | Description |
|---|---|---|
| `TS_SINGLE_SHOT` | 0 |  |
| `TS_START_RECORDING` | 1 |  |
| `TS_STOP_RECORDING` | 2 |  |
| `TS_TOGGLE_RECORDING` | 3 |  |

### PowerState

| Value | Number | Description |
|---|---|---|
| `PS_OFF` | 0 |  |
| `PS_ON` | 1 |  |
| `PS_TOGGLE` | 2 |  |

### PoiMode

| Value | Number | Description |
|---|---|---|
| `PM_NONE` | 0 |  |
| `PM_LOCATION` | 1 |  |
| `PM_AUTO` | 2 |  |

### PanoramaMode

| Value | Number | Description |
|---|---|---|
| `PM_PHOTO` | 0 |  |
| `PM_VIDEO` | 1 |  |

### RotationDirection

| Value | Number | Description |
|---|---|---|
| `RD_CLOCKWISE` | 0 |  |
| `RD_COUNTERCLOCKWISE` | 1 |  |

### IcaoAddressType

| Value | Number | Description |
|---|---|---|
| `IAT_REAL` | 0 |  |
| `IAT_ANONYMOUS` | 1 |  |

### Component

| Value | Number | Description |
|---|---|---|
| `C_DATABASE` | 0 |  |
| `C_LICENSE` | 1 |  |

### PhysicalSemantic

| Value | Number | Description |
|---|---|---|
| `PS_NONE` | 0 |  |
| `PS_DISTANCE` | 1 |  |
| `PS_ALTITUDE` | 2 |  |
| `PS_SPEED` | 3 |  |
| `PS_VERTICAL_SPEED` | 4 |  |
| `PS_AREA` | 5 |  |
| `PS_GSD` | 6 |  |
| `PS_ANGLE` | 7 |  |
| `PS_LATITUDE` | 8 |  |
| `PS_LONGITUDE` | 9 |  |
| `PS_DURATION` | 10 |  |
| `PS_PWM` | 11 |  |
| `PS_VOLTAGE` | 12 |  |
| `PS_CURRENT` | 13 |  |
| `PS_SLOPE` | 14 |  |
| `PS_WEIGHT` | 15 |  |
| `PS_LENGTH` | 16 |  |
| `PS_GAIN` | 17 |  |

### MessageSeverity

| Value | Number | Description |
|---|---|---|
| `MS_ERROR` | 0 |  |
| `MS_WARNING` | 1 |  |
| `MS_INFORMATION` | 2 |  |

### ConflictResolutionMode

| Value | Number | Description |
|---|---|---|
| `CRM_KEEP` | 0 |  |
| `CRM_REPLACE` | 1 |  |
| `CRM_MERGE` | 2 |  |

### WaypointTurnType

| Value | Number | Description |
|---|---|---|
| `WTT_STOP_AND_TURN` | 0 |  |
| `WTT_STRAIGHT` | 1 |  |
| `WTT_SPLINE` | 2 |  |
| `WTT_BANK_TURN` | 3 |  |

### ActionCode

| Value | Number | Description |
|---|---|---|
| `AC_WAYPOINT` | 0 |  |
| `AC_SPEED_CHANGE` | 1 |  |
| `AC_WAIT` | 3 |  |
| `AC_POI_CHANGE` | 4 |  |
| `AC_HEADING_CHANGE` | 5 |  |
| `AC_CAMERA_CONTROL` | 6 |  |
| `AC_CAMERA_TRIGGER` | 7 |  |
| `AC_CAMERA_SERIES_BY_TIME` | 8 |  |
| `AC_CAMERA_SERIES_BY_DISTANCE` | 9 |  |
| `AC_TAKEOFF` | 10 |  |
| `AC_LANDING` | 11 |  |
| `AC_PANORAMA` | 12 |  |
| `AC_ROUTE_ATTRIBUTES` | 13 |  |
| `AC_SET_SERVO` | 14 |  |
| `AC_REPEAT_SERVO` | 15 |  |
| `AC_TRANSITION_FIXED` | 16 |  |
| `AC_TRANSITION_VTOL` | 17 |  |
| `AC_LIDAR_RECORDING_CONTROL` | 18 |  |
| `AC_LIDAR_CALIBRATION` | 19 |  |
| `AC_HOP_ACTION` | 20 |  |
| `AC_DROP` | 21 |  |

### NfzType

| Value | Number | Description |
|---|---|---|
| `NT_CUSTOM` | 0 |  |
| `NT_AERODROME` | 1 |  |

### EnvironmentEventType

| Value | Number | Description |
|---|---|---|
| `EET_NFZ_UPDATED` | 0 |  |
| `EET_ELEVATION_UPDATED` | 1 |  |
| `EET_FEATURE_UPDATED` | 2 |  |

### FeatureType

| Value | Number | Description |
|---|---|---|
| `FT_STATIC_OBJECT` | 0 |  |
| `FT_PLACEMARK` | 1 |  |
| `FT_POLYLINE_PLACEMARK` | 2 |  |

### UtmOperationStatus

| Value | Number | Description |
|---|---|---|
| `UOS_PROPOSING` | 0 |  |
| `UOS_ACCEPTED` | 1 |  |
| `UOS_REJECTED` | 2 |  |
| `UOS_READIED` | 3 |  |
| `UOS_ACTIVATED` | 4 |  |
| `UOS_CLOSED` | 5 |  |
| `UOS_EXPIRED` | 6 |  |
| `UOS_CANCELLED` | 7 |  |
| `UOS_NON_CONFORMING` | 8 |  |
| `UOS_ROGUE` | 9 |  |
| `UOS_MODIFYING` | 10 |  |
| `UOS_REQUESTING` | 11 |  |
| `UOS_ABORTING` | 12 |  |
| `UOS_ABORTED` | 13 |  |
| `UOS_INTRUDER` | 14 |  |

### CommandStatus

| Value | Number | Description |
|---|---|---|
| `CS_REJECTED` | 0 |  |
| `CS_SENT` | 1 |  |
| `CS_SUCCEEDED` | 2 |  |
| `CS_FAILED` | 3 |  |

### Subsystem

| Value | Number | Description |
|---|---|---|
| `S_CONTROL_SERVER` | 0 |  |
| `S_FLIGHT_CONTROLLER` | 1 |  |
| `S_GIMBAL` | 2 |  |
| `S_CAMERA` | 3 |  |
| `S_ADSB_TRANSPONDER` | 4 |  |
| `S_WINCH` | 5 |  |
| `S_HANGAR` | 6 |  |
| `S_USER` | 7 |  |
| `S_GPR` | 8 |  |
| `S_ADSB_RECEIVER` | 9 |  |
| `S_ADSB_VEHICLE` | 10 |  |
| `S_WEATHER_STATION` | 11 |  |
| `S_VSM` | 12 |  |
| `S_LIDAR` | 13 |  |
| `S_RANGEFINDER` | 14 |  |
| `S_REMOTE_ID` | 15 |  |
| `S_HORIZONTAL_OBSTACLE_DETECTOR` | 16 |  |
| `S_VERTICAL_OBSTACLE_DETECTOR` | 17 |  |
| `S_OBSTACLE_AVOIDANCE` | 18 |  |
| `S_ALTIMETER` | 19 |  |

### Semantic

| Value | Number | Description |
|---|---|---|
| `S_DEFAULT` | 1 |  |
| `S_LATITUDE` | 2 |  |
| `S_LONGITUDE` | 3 |  |
| `S_ALTITUDE_AMSL` | 4 |  |
| `S_ALTITUDE_AGL` | 5 |  |
| `S_ALTITUDE_RAW` | 6 |  |
| `S_HEADING` | 7 |  |
| `S_VOLTAGE` | 8 |  |
| `S_AIR_SPEED` | 9 |  |
| `S_GROUND_SPEED` | 10 |  |
| `S_VERTICAL_SPEED` | 11 |  |
| `S_SATELLITE_COUNT` | 12 |  |
| `S_GPS_FIX_TYPE` | 13 |  |
| `S_ROLL` | 14 |  |
| `S_PITCH` | 15 |  |
| `S_YAW` | 16 |  |
| `S_RC_LINK_QUALITY` | 17 |  |
| `S_CURRENT` | 18 |  |
| `S_GCS_LINK_QUALITY` | 19 |  |
| `S_FOV_H` | 20 |  |
| `S_FOV_V` | 21 |  |
| `S_CONTROL_MODE` | 22 |  |
| `S_GROUND_ELEVATION` | 23 |  |
| `S_ACCEPTANCE_RADIUS` | 24 |  |
| `S_LOITER_RADIUS` | 25 |  |
| `S_ICAO` | 26 |  |
| `S_ADSB_MODE` | 27 |  |
| `S_SQUAWK` | 28 |  |
| `S_MILLISECONDS` | 29 |  |
| `S_NUMERIC` | 30 |  |
| `S_BOOL` | 31 |  |
| `S_STRING` | 32 |  |
| `S_ENUM` | 33 |  |
| `S_CAPACITY_LEVEL` | 34 |  |
| `S_FLIGHT_MODE` | 35 |  |
| `S_LIST` | 36 |  |
| `S_AUTOPILOT_STATUS` | 37 |  |
| `S_TIMESTAMP` | 38 | Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Timestamp object. |
| `S_ANY` | 39 |  |
| `S_TEMPERATURE` | 40 |  |
| `S_HUMIDITY` | 41 |  |
| `S_PRECIPITATION` | 42 |  |
| `S_DISTANCE` | 43 |  |
| `S_REMOTE_ID_PREARM_STATUS` | 44 |  |
| `S_REMOTE_ID_OPERATOR_ID_TYPE` | 45 |  |
| `S_REMOTE_ID_IDENTIFIER_TYPE` | 46 |  |
| `S_BINARY` | 47 |  |
| `S_HORIZONTAL_OBSTACLE_DETECTOR_DISTANCES` | 48 |  |

### KmlExportAltitudeMode

| Value | Number | Description |
|---|---|---|
| `E_AMSL` | 1 |  |
| `E_RELATIVE_TO_FIRST_WP` | 2 |  |

### WpmlExportAltitudeMode

| Value | Number | Description |
|---|---|---|
| `W_AMSL` | 1 |  |
| `W_RELATIVE_TO_FIRST_WP` | 2 |  |

### WaypointTag

| Value | Number | Description |
|---|---|---|
| `W_UNKNOWN` | 0 |  |
| `W_CALIBRATION_START` | 1 |  |
| `W_CALIBRATION_END` | 2 |  |
| `W_HOP` | 3 |  |
| `W_HOP_START` | 4 |  |
| `W_KNOT` | 5 |  |

### RemoteIdIdentifierType

| Value | Number | Description |
|---|---|---|
| `RIIT_NONE` | 0 |  |
| `RIIT_SERIAL_NUMBER` | 1 |  |
| `RIIT_CAA_REGISTRATION_ID` | 2 |  |
| `RIIT_UTM_ASSIGNED_UUID` | 3 |  |
| `RIIT_SPECIFIC_SESSION_ID` | 4 |  |

### RemoteIdOperatorType

| Value | Number | Description |
|---|---|---|
| `RIOT_CAA` | 0 |  |

### RemoteIdPrearmStatus

| Value | Number | Description |
|---|---|---|
| `RIPS_GOOD_TO_ARM` | 0 |  |
| `RIPS_PRE_ARM_FAIL` | 1 |  |

### ParameterCategory

| Value | Number | Description |
|---|---|---|
| `PG_ADVANCED` | 0 |  |
| `PG_BASIC` | 1 |  |

### ElevationSourceCategory

| Value | Number | Description |
|---|---|---|
| `ESS_DISABLED` | 0 |  |
| `ESS_GROUND` | 1 |  |
| `ESS_SURFACE` | 2 |  |

### LicenseConstraintErrorSubtype

| Value | Number | Description |
|---|---|---|
| `LCS_UGCS_SEALED` | 0 |  |
| `LCS_PERMISSION_EXCEEDED` | 1 |  |

### ModificationType

| Value | Number | Description |
|---|---|---|
| `MT_CREATE` | 0 |  |
| `MT_UPDATE` | 1 |  |
| `MT_DELETE` | 2 |  |

### KmlExportSchema

| Value | Number | Description |
|---|---|---|
| `KES_KML_EXTENDED_BY_UGCS` | 0 | Kml extended by UgCS schema. Contains route actions (like speed), and additional attributes. |
| `KES_KML` | 1 | Just standard kml, only contains the path of the processed route. |

### TelemetryFormat

| Value | Number | Description |
|---|---|---|
| `TF_TLM` | 0 |  |
| `TF_CSV` | 1 |  |
| `TF_KML` | 2 |  |

### ServerAction

| Value | Number | Description |
|---|---|---|
| `P_ROUTE_EXPORT_TO_KML` | 0 |  |
| `P_ROUTE_EXPORT_TO_WPML` | 1 |  |
| `P_ROUTE_EXPORT_TO_JSON` | 2 |  |
| `P_ROUTE_UPLOAD` | 3 |  |
| `P_MISSION_CREATE` | 4 |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
