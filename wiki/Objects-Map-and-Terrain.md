No-fly zones, buildings (prisms), elevation sources and ADS-B aircraft.

## Contents

- [AdsbAircraft](#adsbaircraft)
- [Prism](#prism)
- [PolygonPoint](#polygonpoint)
- [NoFlightZone](#noflightzone)
- [ConeFrustum](#conefrustum)
- [ElevationSource](#elevationsource)
- [FeatureSource](#featuresource)
- [GeoServer](#geoserver)
- [ElevationData](#elevationdata)
- [ElevationSourceMetadata](#elevationsourcemetadata)
- [Tile](#tile)

### AdsbAircraft

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `icaoAddress` | `int32` |  |
| 4 | `icaoAddressType` | [IcaoAddressType](Enums#icaoaddresstype) |  |
| 5 | `updateTime` | `int64` | Milliseconds since 1970-01-01 UTC. |
| 6 | `identification` | `string` |  |
| 7 | `latitude` | `double` |  |
| 8 | `longitude` | `double` |  |
| 9 | `mslAltitude` | `double` |  |
| 10 | `heading` | `double` |  |
| 11 | `groundSpeed` | `double` |  |
| 12 | `verticalVelocity` | `double` |  |
| 13 | `tag` | `string` |  |
| 14 | `registration` | `string` |  |
| 15 | `vehicle` | [Vehicle](Objects-Vehicles-and-Profiles#vehicle) |  |

### Prism

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `tag` | `string` |  |
| 4 | `points` | repeated [PolygonPoint](#polygonpoint) |  |
| 5 | `altitudeType` | [AltitudeType](Enums#altitudetype) |  |
| 6 | `baseAltitude` | `double` |  |
| 7 | `height` | `double` |  |

### PolygonPoint

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `tag` | `string` |  |
| 5 | `prism` | [Prism](#prism) |  |
| 6 | `latitude` | `double` |  |
| 7 | `longitude` | `double` |  |

### NoFlightZone

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `tag` | `string` |  |
| 4 | `name` | `string` | Common characteristics. |
| 5 | `type` | [NfzType](Enums#nfztype) |  |
| 6 | `sealed` | `bool` |  |
| 7 | `prism` | [Prism](#prism) | Definition of the volume. |
| 8 | `coneFrustum` | [ConeFrustum](#conefrustum) |  |

### ConeFrustum

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `tag` | `string` |  |
| 4 | `topRadius` | `double` |  |
| 5 | `baseRadius` | `double` |  |
| 6 | `baseLatitude` | `double` |  |
| 7 | `baseLongitude` | `double` |  |
| 8 | `altitudeType` | [AltitudeType](Enums#altitudetype) |  |
| 9 | `baseAltitude` | `double` |  |
| 10 | `height` | `double` |  |

### ElevationSource

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `order` | `int32` |  |
| 4 | `geoServer` | [GeoServer](#geoserver) |  |
| 5 | `sourceName` | `string` |  |
| 6 | `tag` | `string` |  |
| 7 | `enabled` | `bool` | **Deprecated.** |
| 8 | `category` | [ElevationSourceCategory](Enums#elevationsourcecategory) |  |

### FeatureSource

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 4 | `geoServer` | [GeoServer](#geoserver) |  |
| 5 | `sourceName` | `string` |  |
| 6 | `featureType` | [FeatureType](Enums#featuretype) |  |
| 7 | `enabled` | `bool` |  |
| 8 | `tag` | `string` |  |

### GeoServer

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `id` | `int32` |  |
| 2 | `version` | `int32` |  |
| 3 | `name` | `string` |  |
| 4 | `uri` | `string` |  |
| 6 | `elevationSources` | repeated [ElevationSource](#elevationsource) |  |
| 7 | `featureSources` | repeated [FeatureSource](#featuresource) |  |
| 8 | `tag` | `string` |  |

### ElevationData

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `metadata` | [ElevationSourceMetadata](#elevationsourcemetadata) |  |
| 2 | `geoServerUri` | `string` |  |
| 3 | `tiles` | repeated [Tile](#tile) |  |

### ElevationSourceMetadata

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `access` | `string` |  |
| 2 | `originLatitude` | `double` | **Deprecated.** |
| 3 | `originLongitude` | `double` | **Deprecated.** |
| 4 | `wgsTileMinEasting` | `int32` | **Deprecated.** |
| 5 | `wgsTileMaxEasting` | `int32` | **Deprecated.** |
| 6 | `wgsTileResolution` | `int32` | **Deprecated.** |
| 7 | `version` | `int64` |  |
| 8 | `pyramidBaseLevel` | `int32` |  |

### Tile

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `data` | `bytes` | gzipped geoserver tile data. |
| 2 | `easting` | `int32` |  |
| 3 | `northing` | `int32` |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
