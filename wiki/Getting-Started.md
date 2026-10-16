## What you need

- **Java 11** or later, and **Maven** to build the SDK.
- **A UgCS server to connect to.** The server runs on the computer where UgCS is installed and
  listens on TCP port **3334**. To connect from another computer, allow that port through the
  firewall of the UgCS computer (see
  [Network Firewall](https://manuals-ugcs.sphengineering.com/docs/network-firewall) in the UgCS
  manual).
- **A UgCS user** to log in with: the same login and password as in UgCS.

## Getting the SDK

```sh
git clone https://github.com/ugcs/ugcs-java-sdk.git
cd ugcs-java-sdk
git tag                 # releases: 4.2, 5.1, 5.1.1, 5.1.2, 5.17.1 ...
git checkout 5.17.1
```

Releases are tagged with the UgCS version number. The SDK has four modules:

| Module | What it is |
|---|---|
| `ucs-client` | `Client` and `ClientSession`, the classes your application uses, and the [[Samples]]. |
| `ucs-api` | The protocol: the messages and objects, generated from `Messages.proto` and `Domain.proto` when the SDK is built. |
| `ucs-messaging` | The network layer, on [Apache MINA](https://mina.apache.org/). |
| `ucs-common` | Utilities the other modules use. |

## Building

```sh
mvn clean package
```

The build generates the Java classes of the protocol from the `.proto` files, then puts every jar
the client needs, with a `client.properties` for the samples, into `target/ugcs-java-sdk`:

```
ucs-client-5.17.1.jar     ucs-api-5.17.1.jar       ucs-messaging-5.17.1.jar
ucs-common-5.17.1.jar     protobuf-java-3.11.4.jar mina-core-2.2.3.jar
slf4j-api-2.0.7.jar       ...                      client.properties
```

**On a Mac with Apple silicon** the build stops with
`Error extracting protoc for version 3.11.4: Unsupported platform: protoc-3.11.4-osx-aarch_64.exe`:
there is no native build of that protocol compiler for it. Build with the Intel one instead, which
macOS runs with Rosetta 2:

```sh
MAVEN_OPTS="-Dos.arch=x86_64" mvn clean package
```

## Using the SDK in your project

Install the SDK into your local Maven repository:

```sh
mvn clean install
```

and add the client to your `pom.xml`; it brings the other modules with it:

```xml
<dependency>
    <groupId>com.ugcs</groupId>
    <artifactId>ucs-client</artifactId>
    <version>5.17.1</version>
</dependency>
```

Without Maven, put every jar of `target/ugcs-java-sdk` on your class path.

The SDK logs through [SLF4J](https://www.slf4j.org/) and does not choose a logging library. Without
one, Java prints `SLF4J: No SLF4J providers were found` at the start and the SDK's log is not shown;
add one (e.g. `logback-classic` or `slf4j-simple`) to see it.

## The first program

```java
import java.net.InetSocketAddress;

import com.ugcs.ucs.client.Client;
import com.ugcs.ucs.client.ClientSession;
import com.ugcs.ucs.proto.DomainProto.DomainObjectWrapper;
import com.ugcs.ucs.proto.DomainProto.Vehicle;

public class ListVehicles {
    public static void main(String[] args) throws Exception {
        try (Client client = new Client(new InetSocketAddress("localhost", 3334))) {
            client.connect();

            ClientSession session = new ClientSession(client);
            session.authorizeHci();
            session.login("admin", "admin");

            for (DomainObjectWrapper wrapper : session.getObjectList(Vehicle.class))
                System.out.println(wrapper.getVehicle().getName());
        }
    }
}
```

It connects, opens a session, logs in and prints the names of the vehicles UgCS knows. See
[[Connecting and Sessions]] for what each step does.

## Running the samples

```sh
cd target/ugcs-java-sdk
```

Set the server and the user in `client.properties`:

```properties
server.host=localhost
server.port=3334
user.login=admin
user.password=admin
```

and run a sample with the directory on the class path. On macOS and Linux the class path
separator is `:`, on Windows `;`:

```sh
java -cp ".:*" com.ugcs.ucs.client.samples.GetRouteList        # macOS, Linux
java -cp ".;*" com.ugcs.ucs.client.samples.GetRouteList        # Windows
```

`GetRouteList` prints the names of the routes on the server. The other samples are on the
[[Samples]] page.
