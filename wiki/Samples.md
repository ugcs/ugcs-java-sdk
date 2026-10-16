The SDK comes with five sample programs, in
[`ucs-client/src/main/java/com/ugcs/ucs/client/samples`](https://github.com/ugcs/ugcs-java-sdk/tree/master/ucs-client/src/main/java/com/ugcs/ucs/client/samples).
Build the SDK and run them from `target/ugcs-java-sdk`, with the server and the user set in its
`client.properties` (see [[Getting Started]]):

```sh
cd target/ugcs-java-sdk
java -cp ".:*" com.ugcs.ucs.client.samples.<Sample> <arguments>     # macOS, Linux
java -cp ".;*" com.ugcs.ucs.client.samples.<Sample> <arguments>     # Windows
```

Each one connects, opens a session and logs in, then:

| Sample | Shows |
|---|---|
| [GetRouteList](#getroutelist) | Reading objects. |
| [ListenTelemetry](#listentelemetry) | Subscribing to telemetry and receiving it. |
| [SendCommand](#sendcommand) | Finding a vehicle, locking it, sending it a command. |
| [UploadSingleWaypointRoute](#uploadsinglewaypointroute) | Creating a mission and a route, processing and uploading it. |
| [ExportRouteToWpml](#exportroutetowpml) | Building a route for a DJI profile and exporting it to WPML. |

**Some samples change the server or move a vehicle.** `SendCommand` commands a real vehicle when
given one, `UploadSingleWaypointRoute` creates a mission and a route and uploads the route. Try them
with an emulated vehicle first, such as the `EMU-101` of the examples.

## GetRouteList

```sh
java -cp ".:*" com.ugcs.ucs.client.samples.GetRouteList
```

Prints the names of all routes: `session.getObjectList(Route.class)`. See
[[Working with Objects]].

## ListenTelemetry

```sh
java -cp ".:*" com.ugcs.ucs.client.samples.ListenTelemetry -t 10
```

Prints all telemetry the server receives, for the given number of seconds: the vehicle, the time,
the field (with its subsystem when it is not the flight controller) and the value. It registers a
`ServerNotificationListener`, subscribes with `subscribeTelemetryEvent()`, and unsubscribes at the
end. See [[Telemetry]].

## SendCommand

```sh
java -cp ".:*" com.ugcs.ucs.client.samples.SendCommand -c commandCode [-a argument=value]* vehicleName
```

```sh
java -cp ".:*" com.ugcs.ucs.client.samples.SendCommand -c arm "EMU-101"
java -cp ".:*" com.ugcs.ucs.client.samples.SendCommand -c guided "EMU-101"
java -cp ".:*" com.ugcs.ucs.client.samples.SendCommand -c waypoint -a latitude=0.99442 -a longitude=0.42015 \
    -a altitude_agl=100.0 -a ground_speed=5.0 -a vertical_speed=1.0 "EMU-101"
```

Sends a command to the flight controller of the vehicle with that name. Every argument is sent as
a `double`; latitude and longitude are in radians. The commands it knows are on the
[[Vehicle Commands]] page; camera and ADS-B commands are not in the sample.

## UploadSingleWaypointRoute

```sh
java -cp ".:*" com.ugcs.ucs.client.samples.UploadSingleWaypointRoute -w "lat,lon,alt" [-s speed] vehicleName
java -cp ".:*" com.ugcs.ucs.client.samples.UploadSingleWaypointRoute -w "56.9761591,24.0730345,100.0" -s 5.0 "EMU-101"
```

The waypoint is given in degrees and metres above the ground; the speed is 5 by default. The
sample:

1. finds the vehicle;
2. finds the mission "Sample mission", or creates it;
3. builds the route "WP-Direct sample route" with one waypoint segment, for the vehicle's profile,
   and saves it;
4. processes it;
5. locks the vehicle, uploads the route, and releases the lock.

See [[Routes and Missions]].

## ExportRouteToWpml

```sh
java -cp ".:*" com.ugcs.ucs.client.samples.ExportRouteToWpml /path/to/exported-route.kmz
```

Builds a route of one waypoint for the vehicle profile "DJI Matrice 350 RTK", processes it, and
writes it to the given file in DJI WPML format, with the altitudes relative to the first waypoint.
The sample does not save the route. See [[Import and Export]].
