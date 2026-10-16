UgCS keeps its data as objects in its database: vehicles, vehicle and payload profiles, missions,
routes, users... A handful of generic requests read and change all of them.

## Objects and their wrapper

Each kind of object is a message of `Domain.proto` (see the Objects pages of the reference, e.g.
[Vehicle](Objects-Vehicles-and-Profiles#vehicle), [Mission](Objects-Missions-and-Routes#mission),
[Route](Objects-Missions-and-Routes#route)). Objects of the database have an `id`, which the
server gives them, and a `version`.

The generic requests carry objects in a [DomainObjectWrapper](Objects-Common#domainobjectwrapper),
which has one field per kind of object; one of them is set:

```java
DomainObjectWrapper wrapper = DomainObjectWrapper.newBuilder().setMission(mission).build();
Mission same = wrapper.getMission();
```

and they name the kind of object by its message name, in `objectType`: `"Vehicle"`, `"Mission"`,
`"Route"`. `ClientSession` takes the class instead and sends its simple name:
`session.getObjectList(Vehicle.class)` sends `"Vehicle"`.

## Reading objects

All objects of a kind, with [GetObjectListRequest](API-Objects#getobjectlistrequest):

```java
List<DomainObjectWrapper> vehicles = session.getObjectList(Vehicle.class, true);
for (DomainObjectWrapper wrapper : vehicles) {
    Vehicle vehicle = wrapper.getVehicle();
    System.out.println(vehicle.getId() + " " + vehicle.getName()
            + " (" + vehicle.getPlatform().getCode() + ")");
}
```

One object by its id, with [GetObjectRequest](API-Objects#getobjectrequest):

```java
GetObjectRequest request = GetObjectRequest.newBuilder()
        .setClientId(session.getClientId())
        .setObjectType("Route")
        .setObjectId(routeId)
        .setRefreshDependencies(true)
        .build();
GetObjectResponse response = client.execute(request);
Route route = response.getObject().getRoute();
```

(`session` is the `Session` of [Connecting and Sessions](Connecting-and-Sessions#requests-clientsession-has-no-method-for).)

For the routes there is also [GetRouteListRequest](API-Objects#getroutelistrequest), with a
filter by mission and by vehicle platform.

### Dependencies

An object refers to other objects: a vehicle to its platform and profile, a route to its mission
and segments. By default (`refreshDependencies` false) these references are **not filled in**,
which keeps the request light: `vehicle.getPlatform().getCode()` is then empty. Set
`refreshDependencies` to true when you need them, as above.

A list of many large objects with their dependencies - all the routes of the server, for
example - takes the server a long time and a lot of memory, and may not come back within the time
limit of `execute`. Get the list without dependencies, then the objects you need one by one with
`GetObjectRequest`.

## Creating and changing objects

[CreateOrUpdateObjectRequest](API-Objects#createorupdateobjectrequest) saves an object: one without
an `id` is created, and the response carries it as saved, with its `id`.

```java
Mission mission = Mission.newBuilder()
        .setName("Survey 2026-10")
        .setOwner(session.getObjectList(User.class).get(0).getUser())
        .build();
DomainObjectWrapper saved = session.createOrUpdateObject(
        DomainObjectWrapper.newBuilder().setMission(mission).build(), Mission.class);
mission = saved.getMission();
```

Its `withComposites` field (true by default) makes the operation apply to the objects which the
saved one refers to as its parts (composite references) as well.

[UpdateObjectFieldsRequest](API-Objects#updateobjectfieldsrequest) changes only the fields listed
in its `fieldNames`, and [DeleteObjectRequest](API-Objects#deleteobjectrequest) deletes an object
by its type and id.

To be told when objects are created, changed or deleted, subscribe to their changes: see
[[Events and Subscriptions]].

## Locks

Objects can be locked by a session. The samples lock a vehicle before sending it a command or
uploading a route, and release it afterwards, also when the operation failed:

```java
session.gainVehicleControl(vehicle);        // AcquireLockRequest for "Vehicle", vehicle.getId()
try {
    session.sendCommand(vehicle, command);
} finally {
    session.releaseVehicleControl(vehicle); // ReleaseLockRequest
}
```

[CheckLockRequest](API-Locks#checklockrequest) tells whether an object is locked: its response has
the [Lock](Objects-Common#lock), with the user who holds it, or none when the object is free.
`CheckMultipleLocksRequest` does the same for several objects. Lock changes can be subscribed to
as well (`LockSubscription`, see [[Events and Subscriptions]]).

## What a platform supports

[GetMappingRequest](API-Objects#getmappingrequest) returns what UgCS supports for each platform
and vehicle type: the route algorithms and the actions they may contain, the failsafe actions,
waypoint turn types, altitude types and frames, cameras, and more. Set the `get...` field of each
part you want, e.g. `setGetAlgorithms(true)`. [[Routes and Missions]] shows how it is
used.
