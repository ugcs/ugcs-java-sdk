Talking to the UgCS server takes three steps: open a connection, open a session on it, and log
in. Every request after that is sent with the session.

```java
try (Client client = new Client(new InetSocketAddress("localhost", 3334))) {
    client.connect();

    ClientSession session = new ClientSession(client);
    session.authorizeHci();
    session.login("admin", "admin");

    // ... requests ...

    session.logout();
}
```

## The connection: `Client`

`Client` is the connection to one server, a TCP connection to its port (3334 by default).

| Method | What it does |
|---|---|
| `Client(SocketAddress)` | Prepares a connection; nothing is sent yet. |
| `connect()` | Connects. Throws an `IOException` ("Connection error") when the server cannot be reached. Called again, it closes the old connection and opens a new one. |
| `isConnected()` | Whether the connection is open. |
| `execute(message)` | Sends a request and waits up to 60 seconds for its answer, which it returns. |
| `execute(message, timeoutMillis)` | The same with your own time limit. |
| `addNotificationListener(listener)` | Receives the events of your subscriptions; see [[Events and Subscriptions]]. |
| `close()` | Closes the connection. `Client` is `Closeable`, so `try`-with-resources closes it. |

`execute` takes any request of the protocol (see the [[API: Sessions|API-Sessions]] and the other
API pages) and returns its response, typed by the variable it is assigned to:

```java
GetTelemetrySnapshotResponse response = client.execute(request);
```

When the server answers with an error, `execute` throws it as a `UcsException`; when no answer
comes in time, a `java.util.concurrent.TimeoutException`. See [[Errors and Licensing]].

**One request at a time.** `Client` numbers its requests with a counter which is not synchronized.
Send requests from one thread, or make the threads which share a `Client` take turns.

## The session: `ClientSession`

`ClientSession` opens the session on the connection and wraps the requests used most often.

| Method | Request it sends |
|---|---|
| `authorizeHci()`, `authorizeHci(Locale)` | [AuthorizeHciRequest](API-Sessions#authorizehcirequest): opens a session. With a `Locale`, its language tag is sent as the session's locale. |
| `login(login, password)` | [LoginRequest](API-Sessions#loginrequest): logs the session in as a UgCS user. |
| `logout()` | [LogoutRequest](API-Sessions#logoutrequest). |
| `getObjectList(type)`, `getObjectList(type, refreshDependencies)` | [GetObjectListRequest](API-Objects#getobjectlistrequest); see [[Working with Objects]]. |
| `createOrUpdateObject(object, type)` | [CreateOrUpdateObjectRequest](API-Objects#createorupdateobjectrequest). |
| `acquireLock(type, id)`, `releaseLock(type, id)` | [AcquireLockRequest](API-Locks#acquirelockrequest), [ReleaseLockRequest](API-Locks#releaselockrequest). |
| `lookupVehicle(name)` | Finds a vehicle by its name, ignoring case; `null` when there is none. |
| `gainVehicleControl(vehicle)`, `releaseVehicleControl(vehicle)` | Locks and unlocks a vehicle. |
| `processRoute(route)` | [ProcessRouteRequest](API-Routes#processrouterequest); see [[Routes and Missions]]. |
| `uploadRoute(vehicle, processedRoute)` | [UploadRouteRequest](API-Routes#uploadrouterequest). |
| `sendCommand(vehicle, command)` | [SendCommandRequest](API-Commands#sendcommandrequest); see [[Vehicle Commands]]. |
| `subscribeTelemetryEvent()`, `unsubscribe(id)` | [SubscribeEventRequest](API-Events#subscribeeventrequest) for the telemetry of all vehicles, and [UnsubscribeEventRequest](API-Events#unsubscribeeventrequest). |
| `exportRouteToWpml(route, altitudeMode, output)` | [ExportRouteToWpmlRequest](API-Import-and-Export#exportroutetowpmlrequest); see [[Import and Export]]. |

### Opening a session

The server keeps a session for each client, with an id. To open one, a client sends
`AuthorizeHciRequest` with `clientId` set to `-1`; the server creates the session and returns its
id in `AuthorizeHciResponse.clientId`. Every later request carries this id in its `clientId` field.
`ClientSession.authorizeHci()` does this and keeps the id.

A session must then be logged in as a UgCS user, with `LoginRequest`. Its response,
`LoginResponse`, carries the [User](Objects-Common#user) with the user's role
(`UR_ADMINISTRATOR` or `UR_OPERATOR`).

After a new `connect()`, open a new session and log in again.

### Requests `ClientSession` has no method for

Build the request, set its `clientId` to the session id, and send it with `Client.execute`.
`ClientSession` keeps the id in a protected field, `clientId`, so a small subclass makes it
available:

```java
public class Session extends ClientSession {
    public Session(Client client) {
        super(client);
    }

    public int getClientId() {
        return clientId;
    }
}
```

```java
Session session = new Session(client);
session.authorizeHci();
session.login("admin", "admin");

GetTelemetrySnapshotRequest request = GetTelemetrySnapshotRequest.newBuilder()
        .setClientId(session.getClientId())
        .addVehicles(vehicle)
        .build();
GetTelemetrySnapshotResponse response = client.execute(request);
```

The examples of this wiki use this `Session` class.

## Protocol version

`authorizeHci()` sends the protocol version of the SDK in `AuthorizeHciRequest.clientVersion`:
for SDK 5.17.1 this is 2.0, from the `ugcs_protocol_major_version` and
`ugcs_protocol_minor_version` options of `Messages.proto`. The server answers with its own version
in `AuthorizeHciResponse.serverVersion`. For an error about the version, the protocol has an error
detail, [UnsupportedProtocolVersionDetail](Objects-Common#unsupportedprotocolversiondetail), with the
server's version.

## Connection settings of the samples

The [[Samples]] read the server address and the user from `client.properties`:

```properties
server.host=localhost
server.port=3334
user.login=admin
user.password=admin
```

The file belongs to the samples: `Client` and `ClientSession` take the address and the user as
parameters, so your application can keep them where it likes.
