## Latitude and longitude: radians

Route points and command arguments take latitude and longitude in **radians**, positive to the
north and to the east. Convert from degrees with `Math.toRadians`:

```java
FigurePoint point = FigurePoint.newBuilder()
        .setLatitude(Math.toRadians(56.9761591))
        .setLongitude(Math.toRadians(24.0730345))
        .setAglAltitude(100.0)
        .setAltitudeType(AltitudeType.AT_AGL)
        .build();
```

The `waypoint` command of the `SendCommand` sample, which flies a vehicle to a point, takes
`latitude=0.99442 longitude=0.42015`: 56.976° N, 24.073° E.

## Altitude

Route altitudes are in metres. [AltitudeType](Enums#altitudetype) says which kind of altitude a
value is: `AT_AGL`, above ground level, or `AT_WGS84`.

A [FigurePoint](Objects-Missions-and-Routes#figurepoint) has both fields, `aglAltitude` and
`wgs84Altitude`, and its `altitudeType` says which one is set. A route segment says it once more in
its `altitudeType` parameter (`AGL`), see [[Routes and Missions]].

The waypoints of a processed route have an [AltitudeFrame](Enums#altitudeframe) instead, which
also has `AF_RANGEFINDER` and `AF_SMART_AGL`.

## Time

Times are **milliseconds since 1970-01-01 UTC**, as Java's `System.currentTimeMillis()` and
`new java.util.Date(millis)` use them. The reference marks these fields with "Milliseconds since
1970-01-01 UTC", e.g. [Telemetry](Objects-Telemetry-and-Logs#telemetry)`.time` and
[Route](Objects-Missions-and-Routes#route)`.creationTime`.

## Parameter values are strings

The parameters of a route segment ([ParameterValue](Objects-Missions-and-Routes#parametervalue))
are name and value pairs of strings, whatever their type:

```java
ParameterValue.newBuilder().setName("speed").setValue(Double.toString(5.0))
ParameterValue.newBuilder().setName("avoidTerrain").setValue("true")
ParameterValue.newBuilder().setName("wpTurnType").setValue("STOP_AND_TURN")
```

The type of each parameter is in its [ParameterDefinition](Objects-Missions-and-Routes#parameterdefinition):
`PT_STRING`, `PT_INTEGER`, `PT_DOUBLE`, `PT_BOOLEAN` or `PT_SELECTION` (one of a list of values),
and what a number means in its `physicalSemantic` (`PS_SPEED`, `PS_DISTANCE`, `PS_ANGLE`...).

## Values of telemetry and commands

Telemetry values and command arguments are a [Value](Objects-Commands#value): one of its typed
fields is set, `boolValue`, `intValue`, `longValue`, `floatValue`, `doubleValue`, `stringValue`,
`listValue` or `bytesValue`. Check which with `hasDoubleValue()` and the like before reading it.
