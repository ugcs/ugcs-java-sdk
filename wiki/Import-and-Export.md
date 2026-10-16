Routes, missions and profiles go to and from files through the server: a request sends or
returns the file's content as bytes, and your application reads or writes the file.

## Exporting a route for DJI Pilot 2 (WPML)

[ExportRouteToWpmlRequest](API-Import-and-Export#exportroutetowpmlrequest) turns a **processed**
route into a `.kmz` file in [DJI WPML format](https://developer.dji.com/doc/cloud-api-tutorial/en/api-reference/dji-wpml/overview.html),
which DJI Pilot 2 imports. Process the route first (see [[Routes and Missions]]), then:

```java
session.exportRouteToWpml(
        processedRoute,
        WpmlExportAltitudeMode.W_RELATIVE_TO_FIRST_WP,
        () -> {
            try {
                return new FileOutputStream("route.kmz");
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
```

The last argument is a `LazyResource<OutputStream>`: the SDK opens the stream only when the answer
has come, writes the file into it and closes it.

The altitudes of the file ([WpmlExportAltitudeMode](Enums#wpmlexportaltitudemode)):

| Mode | Altitudes |
|---|---|
| `W_AMSL` | Above mean sea level. |
| `W_RELATIVE_TO_FIRST_WP` | Relative to the first waypoint. |

The `ExportRouteToWpml` sample does this for a DJI Matrice 350 RTK, see [[Samples]].

## Exporting a route to KML

[ExportRouteToKmlRequest](API-Import-and-Export#exportroutetokmlrequest) takes a processed route as
well, with an altitude mode ([KmlExportAltitudeMode](Enums#kmlexportaltitudemode): `E_AMSL` or
`E_RELATIVE_TO_FIRST_WP`) and a [KmlExportSchema](Enums#kmlexportschema):

| Schema | The file has |
|---|---|
| `KES_KML_EXTENDED_BY_UGCS` | KML extended with the UgCS schema: the route's actions, like speed, and more attributes. |
| `KES_KML` | Standard KML: only the path of the processed route. |

```java
ExportRouteToKmlResponse response = client.execute(ExportRouteToKmlRequest.newBuilder()
        .setClientId(session.getClientId())
        .setRoute(processedRoute)
        .setAltitudeMode(KmlExportAltitudeMode.E_AMSL)
        .setSchema(KmlExportSchema.KES_KML)
        .build());
Files.write(Path.of("route.kml"), response.getKmlData().toByteArray());
```

## Importing a route

| Request | From |
|---|---|
| [ImportRouteFromKmlRequest](API-Import-and-Export#importroutefromkmlrequest) | KML. Its `lineAlgorithm` and `polygonAlgorithm` are the [TraverseAlgorithm](Objects-Missions-and-Routes#traversealgorithm)s for the lines and the polygons of the file. |
| [ImportRouteFromCsvRequest](API-Import-and-Export#importroutefromcsvrequest) | CSV. |
| [ImportRouteFromUgcsRequest](API-Import-and-Export#importroutefromugcsrequest) | A route file of UgCS. |
| [ImportMissionOrRouteFromUgcsRequest](API-Import-and-Export#importmissionorroutefromugcsrequest) | A mission or a route file of UgCS: the response has the one the file contained. |

Each takes the file's content in `data` and returns a [Route](Objects-Missions-and-Routes#route):

```java
ImportRouteFromCsvResponse response = client.execute(ImportRouteFromCsvRequest.newBuilder()
        .setClientId(session.getClientId())
        .setData(ByteString.copyFrom(Files.readAllBytes(Path.of("route.csv"))))
        .build());
Route route = response.getRoute();
```

## UgCS files of missions, routes and profiles

| Request | |
|---|---|
| [ExportMissionToUgcsRequest](API-Import-and-Export#exportmissiontougcsrequest), [ImportMissionFromUgcsRequest](API-Import-and-Export#importmissionfromugcsrequest) | A mission. |
| [ExportRouteToUgcsRequest](API-Import-and-Export#exportroutetougcsrequest), [ImportRouteFromUgcsRequest](API-Import-and-Export#importroutefromugcsrequest) | A route. |
| [ExportProfilesToUgcsRequest](API-Import-and-Export#exportprofilestougcsrequest), [ImportProfilesFromUgcsRequest](API-Import-and-Export#importprofilesfromugcsrequest) | Vehicle and payload profiles. The import's response lists the profiles imported and the profiles skipped. |

The older `ExportRouteRequest`, `ImportRouteRequest`, `ExportMissionRequest`,
`ImportMissionRequest` and `GetKmlRouteRepresentationRequest` are deprecated: use the requests of
this page.

Telemetry is exported and imported with its own requests: see [[Telemetry]].
