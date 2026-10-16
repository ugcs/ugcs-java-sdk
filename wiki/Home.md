The UgCS Java SDK lets your own application work with a UgCS server (UCS), the component of
[UgCS](https://www.ugcs.com/) which keeps missions, routes and vehicles and talks to the drones. With
it you can:

- read and change the objects of the UgCS database: vehicles, vehicle and payload profiles,
  missions, routes;
- build a route, have the server process it, and upload it to a vehicle;
- send commands to vehicles: arm, take off, fly to a point, return home...;
- receive live telemetry and other server events, and read the recorded telemetry;
- import and export routes and missions, including DJI WPML (`.kmz`) for DJI Pilot 2.

The SDK is open source, under the BSD 3-clause license:
[github.com/ugcs/ugcs-java-sdk](https://github.com/ugcs/ugcs-java-sdk).

## Where to start

1. [[Getting Started]] - build the SDK, add it to your project and run a sample.
2. [[Connecting and Sessions]] - connect to the server, log in, send requests.
3. [[Working with Objects]] - read, create and change objects.

Then the topic you need:

| To... | See |
|---|---|
| Plan, process and upload a route | [[Routes and Missions]] |
| Send a command to a vehicle | [[Vehicle Commands]] |
| Receive or read telemetry | [[Telemetry]] |
| Read or write a `.tlm` telemetry file | [.tlm file format](.tlm-file-format) |
| Be told when something changes on the server | [[Events and Subscriptions]] |
| Import or export routes and missions | [[Import and Export]] |
| Handle errors and license limits | [[Errors and Licensing]] |
| Talk to the server from another language than Java | [[Wire Protocol]] |
| See the units of coordinates, altitudes and times | [[Units and Coordinates]] |

The [[Samples]] page explains the sample programs which come with the SDK.

## Reference

Every request, object and enum of the protocol, generated from the SDK's protocol definitions:

- **Requests:** [[API: Sessions|API-Sessions]], [[API: Objects|API-Objects]],
  [[API: Locks|API-Locks]], [[API: Routes|API-Routes]],
  [[API: Import and Export|API-Import-and-Export]], [[API: Commands|API-Commands]],
  [[API: Telemetry|API-Telemetry]], [[API: Tracks and Logs|API-Tracks-and-Logs]],
  [[API: Terrain and Map|API-Terrain-and-Map]], [[API: Events|API-Events]],
  [[API: Errors and Operations|API-Operations]],
  [[API: Licensing and Updates|API-Licensing-and-Updates]], [[API: Other Requests|API-Other]],
  [[API: Common Types|API-Common-Types]].
- **Objects:** [[Objects: Common|Objects-Common]],
  [[Objects: Vehicles and Profiles|Objects-Vehicles-and-Profiles]],
  [[Objects: Missions and Routes|Objects-Missions-and-Routes]],
  [[Objects: Route Actions|Objects-Route-Actions]],
  [[Objects: Processed Routes|Objects-Processed-Routes]], [[Objects: Commands|Objects-Commands]],
  [[Objects: Telemetry and Logs|Objects-Telemetry-and-Logs]],
  [[Objects: Map and Terrain|Objects-Map-and-Terrain]], [[Objects: Events|Objects-Events]].
- [[Enums]] and the [[Message Types]].

This wiki describes SDK version 5.17.1. How to use UgCS itself is in the
[UgCS user manual](https://manuals-ugcs.sphengineering.com/).
