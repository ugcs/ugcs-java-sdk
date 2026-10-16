The UgCS license, its activation and permissions, and checking for new UgCS versions. See [[Errors and Licensing]].

## Requests

- [GetLicenseRequest](#getlicenserequest)
- [ActivateLicenseRequest](#activatelicenserequest)
- [DeactivateLicenseRequest](#deactivatelicenserequest)
- [GetLicenseImprovementWindowContentRequest](#getlicenseimprovementwindowcontentrequest)
- [CheckForUpdatesRequest](#checkforupdatesrequest)
- [GetChangelogRequest](#getchangelogrequest)
- [GetFreeUsageCodeRequest](#getfreeusagecoderequest)
- [StartFreeUsageRequest](#startfreeusagerequest)

### GetLicenseRequest

Message type `68`. Answered with [GetLicenseResponse](#getlicenseresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |

### GetLicenseResponse

Message type `69`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `licenses` | repeated [License](Objects-Common#license) |  |
| 2 | `licensePermissions` | required [LicensePermissionsDto](#licensepermissionsdto) |  |
| 3 | `noLicenseMessage` | `string` |  |

### ActivateLicenseRequest

Message type `70`. Answered with [ActivateLicenseResponse](#activatelicenseresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `activationCode` | required `string` |  |
| 3 | `locale` | `string` | Overrides the locale defined in HciSession (if any). |

### ActivateLicenseResponse

Message type `71`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `license` | [License](Objects-Common#license) |  |

### DeactivateLicenseRequest

Message type `98`. Answered with [DeactivateLicenseResponse](#deactivatelicenseresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `activationCode` | required `string` |  |

### DeactivateLicenseResponse

Message type `99`.

No fields.

### GetLicenseImprovementWindowContentRequest

Message type `227`. Answered with [GetLicenseImprovementWindowContentResponse](#getlicenseimprovementwindowcontentresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |

### GetLicenseImprovementWindowContentResponse

Message type `228`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `content` | `string` |  |

### CheckForUpdatesRequest

Message type `100`. Answered with [CheckForUpdatesResponse](#checkforupdatesresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `since` | [Version](Objects-Common#version) | The result will contain only versions newer than this one. |

### CheckForUpdatesResponse

Message type `101`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `newVersions` | [VersionsInfoDto](#versionsinfodto) |  |

### GetChangelogRequest

Message type `178`. Answered with [GetChangelogResponse](#getchangelogresponse).

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). |
| 2 | `version` | required [Version](Objects-Common#version) |  |

### GetChangelogResponse

Message type `179`.

| # | Field | Type | Description |
|---|---|---|---|
| 3 | `changelog` | `string` |  |

### GetFreeUsageCodeRequest

Message type `209`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). Default `-1`. |
| 2 | `email` | required `string` |  |
| 3 | `firstName` | required `string` |  |
| 4 | `lastName` | required `string` |  |
| 5 | `country` | required `string` |  |
| 6 | `role` | required `string` |  |
| 7 | `company` | `string` |  |
| 8 | `drones` | repeated `string` |  |
| 9 | `useCase` | `string` |  |
| 10 | `bvlos` | `bool` |  |
| 11 | `teamSize` | `string` |  |
| 12 | `payload` | `string` |  |

### StartFreeUsageRequest

Message type `210`.

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse). Default `-1`. |
| 2 | `email` | required `string` |  |
| 3 | `code` | required `string` |  |

## Other messages and types

### VersionInfoDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `version` | required [Version](Objects-Common#version) |  |
| 2 | `releaseDate` | required `int64` | Milliseconds since 1970-01-01 UTC. |
| 3 | `releaseInfo` | `string` |  |

### VersionsInfoDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `versions` | repeated [VersionInfoDto](#versioninfodto) |  |
| 2 | `releaseInfo` | `string` |  |

### LicensePermissionsDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `licenseName` | required `string` |  |
| 2 | `maxAuthenticatedHciSessions` | required `int32` |  |
| 3 | `multinode` | required `bool` |  |
| 4 | `kmlImport` | required `bool` |  |
| 5 | `demImport` | required `bool` |  |
| 6 | `groupOperations` | required `bool` |  |
| 7 | `maxAglAltitude` | repeated [PlatformAndDoublePairDto](#platformanddoublepairdto) |  |
| 8 | `maxDistanceToHome` | repeated [PlatformAndDoublePairDto](#platformanddoublepairdto) |  |
| 9 | `maxBuildings` | repeated [PlatformAndIntegerPairDto](#platformandintegerpairdto) |  |
| 12 | `adsbReceiver` | repeated [PlatformAndBooleanPairDto](#platformandbooleanpairdto) |  |
| 13 | `videoRecording` | repeated [PlatformAndBooleanPairDto](#platformandbooleanpairdto) |  |
| 14 | `cameraFootprint` | repeated [PlatformAndBooleanPairDto](#platformandbooleanpairdto) |  |
| 15 | `adsbTransponder` | repeated [PlatformAndBooleanPairDto](#platformandbooleanpairdto) |  |
| 16 | `forbiddenAlgorithmCodes` | repeated `string` |  |
| 17 | `maxImagesToProcess` | required `int32` |  |
| 18 | `remoteVsmConnection` | required `bool` |  |
| 19 | `ugcsVideoPlayer` | required `bool` |  |
| 20 | `kmlExport` | required `bool` |  |
| 21 | `vehicleConnectionsLimit` | `int32` |  |
| 22 | `dssClient` | `bool` | Default `false`. |
| 23 | `forbiddenCommandCodes` | repeated `string` |  |
| 24 | `smartAgl` | required `bool` |  |
| 25 | `djiPilotConnectionsLimit` | required `int32` |  |
| 26 | `trajectorySmoothing` | `bool` |  |
| 27 | `maxMissions` | `int32` |  |
| 28 | `skyhub` | required `bool` |  |

### PlatformAndBooleanPairDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `platform` | required [Platform](Objects-Vehicles-and-Profiles#platform) |  |
| 2 | `value` | required `bool` |  |

### PlatformAndIntegerPairDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `platform` | required [Platform](Objects-Vehicles-and-Profiles#platform) |  |
| 2 | `value` | required `int32` |  |

### PlatformAndDoublePairDto

| # | Field | Type | Description |
|---|---|---|---|
| 1 | `platform` | required [Platform](Objects-Vehicles-and-Profiles#platform) |  |
| 2 | `value` | required `double` |  |

---
_Generated from the protocol definitions of the UgCS Java SDK 5.17.1 (`Messages.proto`, `Domain.proto`)._
