[![Release](https://jitpack.io/v/UgCS/ugcs-java-sdk.svg)](https://jitpack.io/#UgCS/ugcs-java-sdk)

API and client libraries for the [UgCS Ground Control Station](https://www.ugcs.com/).

## Using as a dependency

Available via the [JitPack build](https://jitpack.io/#UgCS/ugcs-java-sdk):

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
```
<dependency>
    <groupId>com.github.UgCS</groupId>
    <artifactId>ugcs-java-sdk</artifactId>
    <version>3.7</version>
</dependency>
```

## Running Samples

Build a package.

```
$ mvn clean package
```

Go to the output directory containing jars and `client.properties` file.

```
$ cd target/ugcs-java-sdk
```

Modify the `client.properties` if necessary. Make sure the target UgCS server instance is running.

## [Telemetry example](https://github.com/ugcs/ugcs-java-sdk/blob/master/ucs-client/src/main/java/com/ugcs/ucs/client/samples/ListenTelemetry.java)

Start a telemetry listener for specified amount of seconds.

```
$ java -cp .;* com.ugcs.ucs.client.samples.ListenTelemetry <args>
```

```
ListenTelemetry -t waitSeconds

        Listen to all telemetry, received by the UgCS server
        for a specified amount of time in seconds.

Example:

        ListenTelemetry -t 10
```

## [Vehicle command example](https://github.com/ugcs/ugcs-java-sdk/blob/master/ucs-client/src/main/java/com/ugcs/ucs/client/samples/SendCommand.java)

Send a specified command to the vehicle.

```
$ java -cp .;* com.ugcs.ucs.client.samples.SendCommand <args>
```

```
SendCommand -c commandCode [-a commandArgument=value]* vehicleName

        List of supported command codes:

          * arm
          * disarm
          * auto
          * manual
          * guided
          * joystick
          * takeoff_command
          * land_command
          * emergency_land
          * return_to_home
          * mission_pause
          * mission_resume
          * waypoint (latitude, longitude, altitude_amsl/altitude_agl, altitude_origin,
                      ground_speed, vertical_speed, acceptance_radius, heading)
          * direct_vehicle_control (pitch, roll, yaw, trottle)

        For more details on the supported commands and its arguments and expected
        vehicle behavior see UgCS User Manual ("Direct Vehicle Control" section).
        Also note that this tool support a limited subset of the vehicles commands:
        camera and ADS-B commands are not supported, but can be easily implemented
        by modifying a sample source.

Examples:

        SendCommand -c arm "EMU-101"
        SendCommand -c guided "EMU-101"
        SendCommand -c waypoint -a latitude=0.99442 -a longitude=0.42015 -a altitude_agl=100.0 -a ground_speed=5.0 -a vertical_speed=1.0 "EMU-101"
```

## [Route upload example](https://github.com/ugcs/ugcs-java-sdk/blob/master/ucs-client/src/main/java/com/ugcs/ucs/client/samples/UploadSingleWaypointRoute.java)

Generate and try to upload a single waypoint mission to the vehicle.

```
$ java -cp .;* com.ugcs.ucs.client.samples.UploadSingleWaypointRoute <args>
```

```
UploadSingleWaypointRoute -w waypoint [-s speed] vehicleName

        Waypoint is specified as "lat,lon,alt" string, with respective values
        in degrees (latitude and longitude) and AGL meters (altitude). Positive
        directions for latitude and longitude are North and East.

Example:

        UploadSingleWaypointRoute -w "56.9761591,24.0730345,100.0" -s 5.0 "EMU-101"
```
