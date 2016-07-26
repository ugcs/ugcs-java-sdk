[![Release](https://jitpack.io/v/UgCS/ugcs-java-sdk.svg)](https://jitpack.io/#UgCS/ugcs-java-sdk)

API and client libraries for the [UgCS Ground Control Station](https://www.ugcs.com/). Available via the [JitPack build](https://jitpack.io/#UgCS/ugcs-java-sdk):

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
    <version>2.8.1</version>
</dependency>
```

## Running Samples

Build a package.

```
mvn clean package
```

Go to the output directory containing jars and `client.properties` file.

```
target/ugcs-java-sdk-{release-version}/ucs-client
```

Modify the `client.properties` if necessary. Make sure the target UgCS server instance is running.

The example below starts a telemetry listener for 10 seconds.

```
java -cp .;* com.ugcs.ucs.client.samples.ListenTelemetry -t 10
```

This one generates and tries to upload a single waypoint mission to the vehicle.

```
java -cp .;* com.ugcs.ucs.client.samples.UploadSingleWaypointRoute -w "56.9761591,24.0730345,100.0" -s 5.0 "EmuCopter-101"
```

And this one sends a specified command to the vehicle.

```
java -cp .;* com.ugcs.ucs.client.samples.SendCommand -c Takeoff "EmuCopter-101"
java -cp .;* com.ugcs.ucs.client.samples.SendCommand -c Waypoint -a latitude=0.99442 -a longitude=0.42015 -a altitude=100.0 -a speed=5.0 "EmuCopter-101"
```

