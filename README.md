# ucs-java-client

To build the project you will need to install [protobuf compiler 2.4.1](https://github.com/google/protobuf/releases/tag/v2.4.1) to your system. Add `protoc` executable to the project root directory or place it somewhere listed in the path system variable.

# Building and Running Samples

Build a client package.

```
mvn clean package
```

Go to the output directory containing jars and `client.properties` file.

```
target/ucs-java-client-1.0-bin/ucs-client
```

The example below starts a telemetry listener for 10 seconds.

```
java -cp * com.ugcs.ucs.client.samples.ListenTelemetry -t 10
```

And this one generates and tries to upload a single waypoint mission to the vehicle.

```
java -cp * com.ugcs.ucs.client.samples.UploadSingleWaypointRoute -w "0.0,0.0,0.0" -s 5.0 "EmuCopter-101"
```

