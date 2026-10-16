A route goes through four steps: you **define** it, the server **saves** it, the server
**processes** it into what a vehicle can fly, and the processed route is **uploaded** to a vehicle.

## How a route is made up

```
Mission                      a project: a name, an owner, its routes
└── Route                    a name, a vehicle profile, speeds and altitudes, failsafes
    └── SegmentDefinition    one part of the route, in the order flown
        ├── algorithmClassName   how the part is flown: a waypoint, an area scan, a circle...
        ├── Figure               where: points, a polygon, a polyline, a circle
        ├── ParameterValue       the algorithm's parameters: speed, turn type...
        └── ActionDefinition     what to do there: camera, wait, heading...
```

| Object | Main fields |
|---|---|
| [Mission](Objects-Missions-and-Routes#mission) | `name`, `owner` (a [User](Objects-Common#user)), `routes`, `vehicles` |
| [Route](Objects-Missions-and-Routes#route) | `name`, `mission`, `vehicleProfile`, `segments`, `failsafes`, `initialSpeed`, `maxSpeed`, `safeAltitude`, `maxAltitude`, `cornerRadius`, `checkAerodromeNfz`, `checkCustomNfz` |
| [SegmentDefinition](Objects-Missions-and-Routes#segmentdefinition) | `algorithmClassName`, `figure`, `parameterValues`, `actionDefinitions` |
| [Figure](Objects-Missions-and-Routes#figure) | `type` ([FigureType](Enums#figuretype)), `points` ([FigurePoint](Objects-Missions-and-Routes#figurepoint)), `parameters` |

## Route algorithms

A segment's `algorithmClassName` is the `implementationClass` of a
[TraverseAlgorithm](Objects-Missions-and-Routes#traversealgorithm). The algorithms each platform
supports, with their parameters, come from [GetMappingRequest](API-Objects#getmappingrequest):

```java
GetMappingResponse response = client.execute(GetMappingRequest.newBuilder()
        .setClientId(session.getClientId())
        .setGetAlgorithms(true)
        .build());
for (AlgorithmsDto platform : response.getAlgorithmsMapping().getAlgorithmsList()) {
    if (!platform.getPlatformAndVehicleType().getPlatform().getCode().equals("ArduPilot"))
        continue;
    for (AlgorithmAndActionCodeDto entry : platform.getAlgorithmAndActionCodesList()) {
        TraverseAlgorithm algorithm = entry.getAlgorithm();
        System.out.println(algorithm.getCode() + ": " + algorithm.getImplementationClass());
        for (ParameterDefinition parameter : algorithm.getParameterDefinitionsList())
            System.out.println("  " + parameter.getName() + " " + parameter.getType()
                    + (parameter.getMandatory() ? " (mandatory)" : ""));
    }
}
```

Each entry also has the algorithm's action codes ([ActionCode](Enums#actioncode)) on that
platform. The response is large - every algorithm for every platform - so ask for the algorithms
only when you need them, and keep the result.

For orientation, these are the algorithms of UgCS 6.0:

| Code | Name in UgCS | Figure | `implementationClass` |
|---|---|---|---|
| `waypoint` | Waypoint | `FT_POINT` | `com.ugcs.ucs.service.routing.impl.WaypointAlgorithm` |
| `areaScan` | Area scan | `FT_POLYGON` | `com.ugcs.ucs.service.routing.impl.AreaScanAlgorithm` |
| `photogrammetry` | Photogrammetry tool | `FT_POLYGON` | `com.ugcs.ucs.service.routing.impl.PhotogrammetryAlgorithm` |
| `circlegrammetry` | Circlegrammetry tool | `FT_POLYGON` | `com.ugcs.ucs.service.routing.impl.CirclegrammetryAlgorithm` |
| `perimeter` | Perimeter | `FT_POLYGON` | `com.ugcs.ucs.service.routing.impl.PerimeterAlgorithm` |
| `creepingLine` | Creeping line | `FT_POLYGON` | `com.ugcs.ucs.service.routing.impl.CreepingLineAlgorithm` |
| `lidar` | Lidar area | `FT_POLYGON` | `com.ugcs.ucs.service.routing.areasurveying.LidarAlgorithm` |
| `corridorMapping` | Corridor mapping | `FT_POLYLINE` | `com.ugcs.ucs.service.routing.impl.CorridorMappingAlgorithm` |
| `corridorLidar` | Lidar corridor | `FT_POLYLINE` | `com.ugcs.ucs.service.routing.impl.CorridorSurveyingLidarAlgorithm` |
| `facadeScan` | Vertical scan | `FT_POLYLINE` | `com.ugcs.ucs.service.routing.impl.FacadeScanAlgorithm` |
| `circle` | Circle | `FT_CIRCLE` | `com.ugcs.ucs.service.routing.impl.CircleAlgorithm` |
| `expandingSquare` | Expanding square | `FT_POINT` | `com.ugcs.ucs.service.routing.impl.ExpandingSquareAlgorithm` |
| `pattern` | Pattern | `FT_POINT` | `com.ugcs.ucs.service.routing.impl.PatternAlgorithm` |
| `patternU` | U pattern | `FT_POINT` | `com.ugcs.ucs.service.routing.impl.PatternUAlgorithm` |
| `subArea` | Area scan | `FT_POINT` | `com.ugcs.ucs.service.routing.areasurveying.SubAreaAlgorithm` |
| `landing` | Landing | `FT_LANDING_POINT` | `com.ugcs.ucs.service.routing.impl.LandingAlgorithm` |

What each one does and how its parameters work is in the UgCS manual's chapters on
[route planning](https://manuals-ugcs.sphengineering.com/).

### The waypoint algorithm

| Parameter | Type | Mandatory | Default | In UgCS |
|---|---|---|---|---|
| `speed` | `PT_DOUBLE` (`PS_SPEED`) | yes | | Flight speed |
| `wpTurnType` | `PT_SELECTION` | yes | | Turn type, e.g. `STOP_AND_TURN` |
| `altitudeType` | `PT_SELECTION` | yes | `AGL` | Altitude mode |
| `cornerRadius` | `PT_DOUBLE` (`PS_DISTANCE`) | no | | Corner radius |
| `avoidObstacles` | `PT_BOOLEAN` | yes | `true` | Avoid obstacles |
| `avoidTerrain` | `PT_BOOLEAN` | yes | `true` | Avoid terrain |

The turn types a platform supports come from `GetMappingRequest` with
`setGetWaypointTurnTypes(true)`, and the values of a selection parameter for a route from
[GetParametersSelectionValuesRequest](API-Routes#getparametersselectionvaluesrequest).

## Defining a route

A waypoint segment, with latitude and longitude in radians (see [[Units and Coordinates]]):

```java
static SegmentDefinition waypoint(double latitudeDeg, double longitudeDeg,
                                  double altitudeAgl, double speed) {
    Figure.Builder figure = Figure.newBuilder()
            .setType(FigureType.FT_POINT)
            .addPoints(FigurePoint.newBuilder()
                    .setLatitude(Math.toRadians(latitudeDeg))
                    .setLongitude(Math.toRadians(longitudeDeg))
                    .setAglAltitude(altitudeAgl)
                    .setAltitudeType(AltitudeType.AT_AGL));
    return SegmentDefinition.newBuilder()
            .setAlgorithmClassName("com.ugcs.ucs.service.routing.impl.WaypointAlgorithm")
            .setFigure(figure)
            .addParameterValues(parameter("speed", Double.toString(speed)))
            .addParameterValues(parameter("wpTurnType", "STOP_AND_TURN"))
            .addParameterValues(parameter("altitudeType", "AGL"))
            .addParameterValues(parameter("avoidObstacles", "true"))
            .addParameterValues(parameter("avoidTerrain", "true"))
            .build();
}

static ParameterValue parameter(String name, String value) {
    return ParameterValue.newBuilder().setName(name).setValue(value).build();
}
```

The route, for the profile of the vehicle which will fly it:

```java
Route route = Route.newBuilder()
        .setMission(mission)
        .setName("Two waypoints")
        .setVehicleProfile(vehicle.getProfile())
        .setCheckAerodromeNfz(true)
        .setCheckCustomNfz(false)
        .setInitialSpeed(5.0)
        .setMaxSpeed(10.0)
        .setMaxAltitude(10000.0)
        .setSafeAltitude(50.0)
        .addFailsafes(Failsafe.newBuilder()
                .setReason(FailsafeReason.FR_GPS_LOST)
                .setAction(FailsafeAction.FA_WAIT))
        .addSegments(waypoint(56.9761591, 24.0730345, 100.0, 5.0))
        .addSegments(waypoint(56.9770000, 24.0750000, 100.0, 5.0))
        .build();
```

A failsafe says what the vehicle does on a failure: the reasons are
[FailsafeReason](Enums#failsafereason) (`FR_RC_LOST`, `FR_GPS_LOST`, `FR_LOW_BATTERY`,
`FR_DATALINK_LOST`), the actions [FailsafeAction](Enums#failsafeaction) (`FA_GO_HOME`, `FA_LAND`,
`FA_WAIT`, `FA_CONTINUE`). The actions a platform supports for each reason come from
`GetMappingRequest` with `setGetFailsafeActions(true)`.

The mission must exist on the server. Find it by name, or create it:

```java
static Mission findOrCreateMission(Session session, String name) throws Exception {
    for (DomainObjectWrapper wrapper : session.getObjectList(Mission.class))
        if (wrapper.getMission().getName().equals(name))
            return wrapper.getMission();
    User owner = session.getObjectList(User.class).get(0).getUser();
    Mission mission = Mission.newBuilder().setName(name).setOwner(owner).build();
    return session.createOrUpdateObject(
            DomainObjectWrapper.newBuilder().setMission(mission).build(), Mission.class).getMission();
}
```

## Saving it

```java
route = session.createOrUpdateObject(
        DomainObjectWrapper.newBuilder().setRoute(route).build(), Route.class).getRoute();
```

## Processing it

A route as defined is a plan: the path between its points may cross terrain, buildings or no-fly
zones. [ProcessRouteRequest](API-Routes#processrouterequest) has the server work out the route the
vehicle flies, with all its waypoints and commands, and returns it as a
[ProcessedRoute](Objects-Processed-Routes#processedroute), one
[ProcessedSegment](Objects-Processed-Routes#processedsegment) per segment:

```java
ProcessRouteResponse response = client.execute(ProcessRouteRequest.newBuilder()
        .setClientId(session.getClientId())
        .setRoute(route)
        .build());
ProcessedRoute processed = response.getProcessedRoute();
for (ProcessedSegment segment : processed.getSegmentsList()) {
    if (segment.getStatus() == RouteProcessingStatus.RPS_PROCESSED)
        continue;
    for (LocalizedMessage message : segment.getMessageSet().getMessagesList())
        System.err.println(message.getSeverity() + ": " + message.getDefaultText());
    throw new IllegalStateException("The route cannot be flown: " + segment.getStatus());
}
```

Each segment's `status` ([RouteProcessingStatus](Enums#routeprocessingstatus)):

| Status | The segment |
|---|---|
| `RPS_PROCESSED` | is fully processed: it has all its commands. |
| `RPS_MARKED` | has its basic points only. |
| `RPS_INVALID` | is incorrect; its `messageSet` says why. |
| `RPS_MODIFIED` | is new or changed. |

A segment's `messageSet` has the server's messages about it: their severity (`MS_ERROR`,
`MS_WARNING`, `MS_INFORMATION`), the text (`defaultText`), and its translations by locale
(`variants`). Each segment also has its
`estimatedDistance` and `estimatedTime`, and the route its `estimatedDistance`.

`ClientSession.processRoute(route)` does the same: it accepts the route only when every segment
is `RPS_PROCESSED`, and otherwise prints the messages to the standard error stream and throws
`IllegalStateException("Route processing error")`. Send the request yourself, as above, to handle
the messages in your application.

## Uploading it

Lock the vehicle, upload, and release the lock:

```java
session.gainVehicleControl(vehicle);
try {
    session.uploadRoute(vehicle, processed);
} finally {
    session.releaseVehicleControl(vehicle);
}
```

To start the uploaded route, switch the vehicle to automatic mode with the `auto` command: see
[[Vehicle Commands]].

### Continuing a route

[UploadRouteRequest2](API-Routes#uploadrouterequest2) uploads a route to be continued from a
waypoint. Without `resumeOptions` the route
starts from its first waypoint. With them:

- `resumePoint.targetWaypointIndex` (more than 0): the waypoint of the route the vehicle flies to
  first;
- `resumePoint.location` (optional): a point on the route's path to start from instead. When it is
  100 metres or more from the route, the upload fails;
- `goThroughFirstWaypoint`: true by default.

```java
client.execute(UploadRouteRequest2.newBuilder()
        .setClientId(session.getClientId())
        .setVehicle(vehicle)
        .setProcessedRoute(processed)
        .setResumeOptions(RouteResumeOptions.newBuilder()
                .setResumePoint(ResumePoint.newBuilder().setTargetWaypointIndex(3))
                .setGoThroughFirstWaypoint(true))
        .build());
```

### Following the flight

When a route is uploaded, the server creates a [RoutePass](Objects-Processed-Routes#routepass) for
it, and removes it once the route is done. Its `waypointIndex` is the waypoint of the route reached
last (counted from 0), `waypointCount` the number of waypoints, and `latitude`, `longitude`,
`altitude` the vehicle's position on the route. A [RouteUpload](Objects-Processed-Routes#routeupload)
has the route, vehicle, user and time of an upload.

## Other route requests

| Request | What it does |
|---|---|
| [ChangeRouteVehicleProfileRequest](API-Routes#changeroutevehicleprofilerequest) | The route for another vehicle profile, with warning messages. |
| [SplitRouteBySegmentsRequest](API-Routes#splitroutebysegmentsrequest), [SplitRouteByDistanceRequest](API-Routes#splitroutebydistancerequest), [SplitRouteByCorridorPointRequest](API-Routes#splitroutebycorridorpointrequest) | Splits a route into parts. |
| [JoinRouteRequest](API-Routes#joinrouterequest) | Joins routes into one. |
| [ReconstructRouteRequest](API-Routes#reconstructrouterequest) | The route definition of a processed route. |
| [CalculateSplineRequest](API-Routes#calculatesplinerequest) | The path of a spline through waypoints. |
| [GetNativeRouteRequest](API-Routes#getnativerouterequest) | The processed route as `nativeRoute` bytes, for a vehicle profile. |
| [ResolveGsdByAltitudeAglRequest](API-Routes#resolvegsdbyaltitudeaglrequest) and the other `Resolve...` requests | The ground sample distance (GSD) of a camera's payload profile at an altitude, and the altitude for a GSD. |
| [GetPolygonAreaRequest](API-Terrain-and-Map#getpolygonarearequest) | The area of a polygon. |

To create a route from a file - KML, CSV, a UgCS route file - see [[Import and Export]].
