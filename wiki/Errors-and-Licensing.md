## When a request fails

The server answers a request it cannot carry out with an [Error](API-Operations#error) instead of
the response: an `errorMessage`, an `errorCode`, and in some cases an `errorDetail`.
`Client.execute` throws it as an exception:

| Exception | When |
|---|---|
| `com.ugcs.ucs.client.UcsException` | The server answered with an error. `getMessage()` is its `errorMessage`, `getErrorCode()` its `errorCode` (or `null`). |
| `UgcsSealedException`, `LicenseLimitationExceededException` | The error is a license constraint, see below. Both are a `LicenseConstraintException`, which is a `UcsException`. |
| `java.util.concurrent.TimeoutException` | No answer came within the time limit: 60 seconds, or the one given to `execute(message, timeoutMillis)`. |
| `java.io.IOException` | From `connect()`: the server cannot be reached. |

```java
try {
    session.login("operator", "wrong password");
} catch (UgcsSealedException | LicenseLimitationExceededException e) {
    System.err.println("Not allowed by the license: " + e.getMessage());
} catch (UcsException e) {
    System.err.println("The server refused: " + e.getMessage() + " (code " + e.getErrorCode() + ")");
} catch (TimeoutException e) {
    System.err.println("No answer in time");
}
```

A few methods of `ClientSession` check the response themselves and throw an
`IllegalStateException` when it is not what they need:

| Method | Throws when |
|---|---|
| `processRoute` | A segment of the processed route is not `RPS_PROCESSED`; the messages are printed to the standard error stream. See [[Routes and Missions]]. |
| `sendCommand` | The command's status is not `CS_SUCCEEDED`. See [[Vehicle Commands]]. |

The error details of the protocol ([ErrorDetailWrapper](Objects-Common#errordetailwrapper)) are an
[InvalidTakeOffAltitudeDetail](Objects-Common#invalidtakeoffaltitudedetail) with a
`recommendedAltitude`, an
[UnsupportedProtocolVersionDetail](Objects-Common#unsupportedprotocolversiondetail) with the
server's protocol version, and a
[LicenseConstraintErrorDetail](Objects-Common#licenseconstrainterrordetail). The exceptions of
`Client.execute` carry the message and the code, not the details.

## License constraints

What a client may do depends on the UgCS license of the server. An error about it has the
`errorCode` **1001** (`UcsException.ErrorCodes.LICENSE_CONSTRAINT`) and a
[LicenseConstraintErrorDetail](Objects-Common#licenseconstrainterrordetail), whose subtype
([LicenseConstraintErrorSubtype](Enums#licenseconstrainterrorsubtype)) chooses the exception:

| Subtype | Exception |
|---|---|
| `LCS_UGCS_SEALED` | `UgcsSealedException` |
| `LCS_PERMISSION_EXCEEDED` | `LicenseLimitationExceededException` |

The detail also names the `permission`.

## The license and its permissions

[GetLicenseRequest](API-Licensing-and-Updates#getlicenserequest) returns the activated
[License](Objects-Common#license)s and what they permit, a
[LicensePermissionsDto](API-Licensing-and-Updates#licensepermissionsdto), among others:

| Permission | |
|---|---|
| `licenseName` | The name of the license. |
| `maxAuthenticatedHciSessions` | The limit of logged-in sessions. |
| `vehicleConnectionsLimit`, `djiPilotConnectionsLimit` | The limits of vehicle and DJI Pilot connections. |
| `maxAglAltitude`, `maxDistanceToHome` | Limits per platform. |
| `forbiddenAlgorithmCodes`, `forbiddenCommandCodes` | Route algorithms and commands the license does not allow. |
| `kmlImport`, `kmlExport`, `demImport`, `groupOperations`, `smartAgl`, `trajectorySmoothing`, `skyhub` ... | Features the license has. |

The response may also have a `noLicenseMessage`.
[CheckPermissionRequest](API-Sessions#checkpermissionrequest) asks about one action
([ServerAction](Enums#serveraction): route export to KML, WPML or JSON, route upload, mission
creation) for a platform.

Licenses are activated and deactivated with
[ActivateLicenseRequest](API-Licensing-and-Updates#activatelicenserequest) and
[DeactivateLicenseRequest](API-Licensing-and-Updates#deactivatelicenserequest), by activation code.
About UgCS licenses themselves, see the [UgCS manual](https://manuals-ugcs.sphengineering.com/).
