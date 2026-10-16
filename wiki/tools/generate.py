"""
The reference pages of the UgCS Java SDK wiki, generated from the SDK's protocol definitions.

The guide pages of the wiki are written by hand; these are made from `Messages.proto`,
`Domain.proto` and the message type numbers of `HciMessageMapping.java`, so that they list every
request, object, field and enum exactly as the SDK declares them, and can be made again for a new
SDK version. From the root of the SDK:

    python3 wiki/tools/generate.py . wiki

Only the pages it generates are written (`API-*`, `Objects-*`, `Enums`, `Message-Types`); a page
of this kind which is no longer generated is deleted. Standard library only.
"""

from __future__ import annotations

import argparse
import re
import sys
from collections import defaultdict
from dataclasses import dataclass, field
from pathlib import Path

PROTO_DIR = Path("ucs-api/src/main/proto")
MAPPING = Path("ucs-api/src/main/java/com/ugcs/ucs/proto/mapping/HciMessageMapping.java")
POM = Path("pom.xml")
GENERATED = re.compile(r"^(API-.+|Objects-.+|Enums|Message-Types)\.md$")


@dataclass
class Field:
    label: str
    type: str
    name: str
    number: int
    default: str | None = None
    deprecated: bool = False
    comment: str = ""
    # The UgCS field options of Options.proto which say something to a reader.
    posixTime: bool = False
    uuid: bool = False
    readonly: bool = False


@dataclass
class Definition:
    kind: str  # "message" or "enum"
    name: str
    file: str
    section: str
    comment: str = ""
    deprecated: bool = False
    fields: list[Field] = field(default_factory=list)
    # enum values: (name, number, comment)
    values: list[tuple[str, int, str]] = field(default_factory=list)


# Parsing


def _CommentText(lines: list[str]) -> str:
    text = " ".join(l.strip() for l in lines if l.strip())
    return re.sub(r"\s+", " ", text).strip()


def _Deprecated(comment: str, deprecated: bool) -> tuple[str, bool]:
    """A comment which says the thing is deprecated becomes the flag, and the rest of it stays."""
    if m := re.match(r"^deprecated\b[.:]?\s*(.*)$", comment, flags=re.IGNORECASE):
        return m.group(1), True
    return comment, deprecated


def ParseProto(path: Path) -> list[Definition]:
    """The messages and enums of a proto2 file without nesting, with their comments."""
    text = path.read_text(encoding="utf-8")
    # Block comments become line comments, so that one pass handles both.
    text = re.sub(r"(?<!/)/\*+(.*?)\*/", lambda m: "\n".join("// " + l.strip(" *\t")
                                                       for l in m.group(1).splitlines()), text, flags=re.DOTALL)
    defs: list[Definition] = []
    section = ""
    pending: list[str] = []
    current: Definition | None = None
    for raw in text.splitlines():
        line = raw.strip()
        code, _, trailing = line.partition("//")
        code = code.strip()
        trailing = trailing.strip()
        if not code:
            if line.startswith("//"):
                body = line.lstrip("/").strip()
                # Section headers: `//-- Route, the definition of ----` or `// Locks` on its own.
                if m := re.match(r"^-+\s*(.+?)\s*-+$", body):
                    if current is None:
                        section, pending = m.group(1), []
                    continue
                if re.fullmatch(r"\*+", body):
                    continue
                pending.append(body)
            elif current is None and pending and not line:
                # A comment followed by an empty line outside a definition is a section title when
                # short, otherwise a note which belongs to nothing.
                if len(pending) == 1 and len(pending[0]) < 40:
                    section = pending[0]
                pending = []
            continue
        if current is None:
            if m := re.match(r"^(message|enum)\s+(\w+)\s*\{", code):
                comment, deprecated = _Deprecated(_CommentText(pending), False)
                current = Definition(m.group(1), m.group(2), path.name, section, comment, deprecated)
                pending = []
                if code.endswith("}"):
                    defs.append(current)
                    current = None
            continue
        if code.startswith("}"):
            defs.append(current)
            current = None
            pending = []
            continue
        if re.match(r"option\s", code):
            if re.search(r"\bdeprecated\s*=\s*true", code):
                current.deprecated = True
            continue
        comment = _CommentText(pending + ([trailing] if trailing else []))
        pending = []
        if current.kind == "enum":
            if m := re.match(r"^(\w+)\s*=\s*(-?\d+)", code):
                current.values.append((m.group(1), int(m.group(2)), comment))
            continue
        if m := re.match(r"^(required|optional|repeated)\s+([\w.]+)\s+(\w+)\s*=\s*(\d+)\s*(\[(.*)\])?\s*;", code):
            options = m.group(6) or ""
            default = re.search(r"\bdefault\s*=\s*([^,\]]+)", options)
            deprecated = bool(re.search(r"\bdeprecated\s*=\s*true", options))
            comment, deprecated = _Deprecated(comment, deprecated)
            current.fields.append(Field(
                m.group(1), m.group(2), m.group(3), int(m.group(4)),
                default.group(1).strip() if default else None, deprecated, comment,
                "ugcs_field_posix_time) = true" in options, "ugcs_field_uuid) = true" in options,
                "(readonly) = true" in options))
    return defs


def ParseMapping(path: Path) -> dict[str, int]:
    """Message name to its type number on the wire."""
    # `Error` is written `MessagesProto.Error.class`, against `java.lang.Error`.
    return {m.group(2).split(".")[-1]: int(m.group(1))
            for m in re.finditer(r"putMapping\((\d+),\s*([\w.]+)\.class\)", path.read_text(encoding="utf-8"))}


def SdkVersion(path: Path) -> str:
    m = re.search(r"<artifactId>ugcs-java-sdk</artifactId>.*?<version>([^<]+)</version>",
                  path.read_text(encoding="utf-8"), flags=re.DOTALL)
    return m.group(1).strip() if m else "unknown"


# Where each definition goes


@dataclass
class PageSpec:
    slug: str
    title: str
    intro: str


API_PAGES = [
    PageSpec("API-Sessions", "API: Sessions",
             "Opening a session with the UgCS server, logging in and out, and what the server "
             "supports. See [[Connecting and Sessions]] for how they are used."),
    PageSpec("API-Objects", "API: Objects",
             "The generic requests which read, create, update and delete the objects of the UgCS "
             "database (vehicles, missions, routes, profiles...), and the mapping of what each platform "
             "supports. See [[Working with Objects]]."),
    PageSpec("API-Locks", "API: Locks",
             "Locking an object, e.g. a vehicle before sending it a command or uploading a route. "
             "See [[Working with Objects]]."),
    PageSpec("API-Routes", "API: Routes",
             "Processing a route into the commands a vehicle executes, changing and splitting routes, "
             "uploading a route to a vehicle, and camera calculations. See [[Routes and Missions]]."),
    PageSpec("API-Import-and-Export", "API: Import and Export",
             "Routes, missions and profiles from and to files: UgCS formats, KML, CSV and DJI WPML. "
             "See [[Import and Export]]."),
    PageSpec("API-Commands", "API: Commands",
             "Sending a command to one or more vehicles. See [[Vehicle Commands]]."),
    PageSpec("API-Telemetry", "API: Telemetry",
             "Reading the telemetry the server has recorded, its latest values, and exporting and "
             "importing it. See [[Telemetry]]."),
    PageSpec("API-Tracks-and-Logs", "API: Tracks and Logs",
             "The flight tracks of vehicles and their log messages."),
    PageSpec("API-Terrain-and-Map", "API: Terrain and Map",
             "Elevation, line of sight, no-fly zones, place search and ADS-B traffic."),
    PageSpec("API-Events", "API: Events",
             "Subscribing to server events (telemetry, object changes, locks, commands...) and the "
             "notifications which deliver them. See [[Events and Subscriptions]]."),
    PageSpec("API-Operations", "API: Errors and Operations",
             "The error answer to a failed request, and the progress and data stream messages of "
             "long operations. See [[Errors and Licensing]] and [[Wire Protocol]]."),
    PageSpec("API-Licensing-and-Updates", "API: Licensing and Updates",
             "The UgCS license, its activation and permissions, and checking for new UgCS versions. "
             "See [[Errors and Licensing]]."),
    PageSpec("API-Other", "API: Other Requests",
             "Requests of the UgCS client application: feedback, usage statistics and analytics."),
]

# Requests by their name without `Request`, and the pages they go to.
API_GROUPS = {
    "API-Sessions": "AuthorizeHci Login Logout SetHciLocale Ping GetDeviceId GetCapabilities CheckPermission",
    "API-Objects": "GetObjectList GetObject CreateOrUpdateObject UpdateObjectFields DeleteObject GetMapping "
                   "GetMissionPreferences UpdateMissionPreferences GetRouteList",
    "API-Locks": "AcquireLock ReleaseLock CheckLock CheckMultipleLocks",
    "API-Routes": "ProcessRoute ConvertRouteAltitudes ChangeRouteVehicleProfile GetParametersSelectionValues "
                  "CalculateSpline SplitRouteBySegments SplitRouteByDistance SplitRouteByCorridorPoint JoinRoute "
                  "ReconstructRoute GetNativeRoute UploadRoute UploadRoute2 ResolveGsdByAltitudeAgl "
                  "ResolveGsdByAltitudeAmsl ResolveAltitudeAglByGsd ResolveAltitudeAmslByGsd SubmitUtmOperation",
    "API-Import-and-Export": "ImportMissionOrRouteFromUgcs ImportMissionFromUgcs ImportRouteFromUgcs "
                             "ImportProfilesFromUgcs ImportRouteFromCsv ImportRouteFromKml ExportMissionToUgcs "
                             "ExportRouteToUgcs ExportProfilesToUgcs ExportRouteToKml ExportRouteToWpml ExportRoute "
                             "ImportRoute ExportMission ImportMission GetKmlRouteRepresentation",
    "API-Commands": "SendCommand",
    "API-Telemetry": "GetTelemetry DeleteTelemetry TraceTelemetryFrames CountTelemetry GetLocationPassingTime "
                     "ExportTelemetry ImportTelemetry ExportTelemetryStream ImportTelemetryStream "
                     "GetTelemetrySnapshot IsTelemetryFieldRegistered",
    "API-Tracks-and-Logs": "GetVehicleTracks DeleteVehicleTrack MergeVehicleTrack SplitVehicleTrack GetVehicleLog "
                           "GetVehicleLogByTimeRange GetVehicleVideoLog",
    "API-Terrain-and-Map": "GetElevationProfile GetElevationProfileV2 GetRasterGrid GetRasterValues GetElevationTiles "
                           "GetElevationTilesV2 SwapElevationOrder GetLineOfSight GetNfzList SearchPlaces "
                           "CheckLocation GetPolygonArea GetAdsbAircraftList GetAdsbBulbStatus",
    "API-Events": "SubscribeEvent UnsubscribeEvent",
    "API-Operations": "CancelOperation OperationStream",
    "API-Licensing-and-Updates": "GetLicense ActivateLicense DeactivateLicense GetLicenseImprovementWindowContent "
                                 "CheckForUpdates GetChangelog GetFreeUsageCode StartFreeUsage",
    "API-Other": "SendFeedback ConfigureAnalytics ReadAnalyticsConfiguration SendAnalyticsEvent SendStatistics",
}
# Messages of Messages.proto which are neither a request nor its response, placed by hand: the
# rest go to the page of the requests which use them.
API_EXTRA = {
    "Error": "API-Operations", "OperationStatus": "API-Operations", "OperationStream": "API-Operations",
    "Notification": "API-Events", "EmptyResponse": "API-Other", "SingleValueResponse": "API-Routes",
}
# Descriptions of fields the protos do not comment, each settled by the SDK's code, its samples or
# a comment elsewhere in the protos: (message, field) to the text.
NOTES = {
    ("AuthorizeHciRequest", "clientId"): "`-1`: the server creates a new session.",
    ("AuthorizeHciRequest", "locale"): "The session's locale, as a language tag (e.g. `de-DE`).",
    ("AuthorizeHciRequest", "clientVersion"): "The client's protocol version.",
    ("AuthorizeHciResponse", "clientId"): "The id of the new session: the `clientId` of every later request.",
    ("AuthorizeHciResponse", "serverVersion"): "The server's protocol version.",
    ("LoginRequest", "userLogin"): "A UgCS user's login.",
    ("GetObjectListRequest", "objectType"): "The message name of the objects, e.g. `Vehicle`, `Route`.",
    ("GetObjectRequest", "objectType"): "The message name of the object, e.g. `Vehicle`, `Route`.",
    ("CreateOrUpdateObjectRequest", "objectType"): "The message name of the object, e.g. `Mission`.",
    ("GetObjectListRequest", "refreshDependencies"):
        "Fill in the objects this one refers to; otherwise these references are left empty.",
    ("GetObjectRequest", "refreshDependencies"):
        "Fill in the objects this one refers to; otherwise these references are left empty.",
    ("CreateOrUpdateObjectRequest", "withComposites"):
        "Apply the operation to the objects referred to as composite parts as well.",
    ("AcquireLockRequest", "objectType"): "The message name of the object, e.g. `Vehicle`.",
    ("ReleaseLockRequest", "objectType"): "The message name of the object, e.g. `Vehicle`.",
    ("CheckLockRequest", "objectType"): "The message name of the object, e.g. `Vehicle`.",
    ("SegmentDefinition", "algorithmClassName"):
        "The `implementationClass` of a [TraverseAlgorithm](#traversealgorithm), e.g. "
        "`com.ugcs.ucs.service.routing.impl.WaypointAlgorithm`.",
    ("FigurePoint", "latitude"): "Radians.",
    ("FigurePoint", "longitude"): "Radians.",
    ("Notification", "subscriptionId"): "From [SubscribeEventResponse](API-Events#subscribeeventresponse).",
    ("SubscribeEventResponse", "subscriptionId"): "The id the notifications of this subscription carry.",
    ("UnsubscribeEventRequest", "subscriptionId"): "From [SubscribeEventResponse](API-Events#subscribeeventresponse).",
    ("ProcessedSegment", "status"): "`RPS_PROCESSED` when the segment is fully processed.",
    ("Error", "errorCode"): "`1001`: a license constraint, see `errorDetail`.",
}
COMMON_TYPES = PageSpec("API-Common-Types", "API: Common Types",
                        "Types of the requests which more than one area of the API uses.")

OBJECT_PAGES = [
    PageSpec("Objects-Common", "Objects: Common",
             "The wrapper of every object in the generic object requests, and the types shared by all "
             "areas. See [[Working with Objects]]."),
    PageSpec("Objects-Vehicles-and-Profiles", "Objects: Vehicles and Profiles",
             "Vehicles, their platforms and vehicle profiles, payload profiles, and the vehicle "
             "specific modules (VSM) which connect them."),
    PageSpec("Objects-Missions-and-Routes", "Objects: Missions and Routes",
             "Missions and the routes in them as they are planned: segments with their algorithm, "
             "figure and parameters, and the algorithms available. See [[Routes and Missions]]."),
    PageSpec("Objects-Route-Actions", "Objects: Route Actions",
             "Actions in a route: the definitions planned on a segment, and the actions of a "
             "processed route which the vehicle executes."),
    PageSpec("Objects-Processed-Routes", "Objects: Processed Routes",
             "A route as the server has processed it, with the result and the messages of each "
             "segment, and its upload to a vehicle."),
    PageSpec("Objects-Commands", "Objects: Commands",
             "Commands sent to a vehicle, the commands a vehicle supports, and the values they "
             "carry. See [[Vehicle Commands]]."),
    PageSpec("Objects-Telemetry-and-Logs", "Objects: Telemetry and Logs",
             "Telemetry values, flight tracks and vehicle log messages. See [[Telemetry]]."),
    PageSpec("Objects-Map-and-Terrain", "Objects: Map and Terrain",
             "No-fly zones, buildings (prisms), elevation sources and ADS-B aircraft."),
    PageSpec("Objects-Events", "Objects: Events",
             "What can be subscribed to, and the events which come back. See "
             "[[Events and Subscriptions]]."),
]

# Domain.proto sections to pages; definitions in no section listed here go by the overrides.
DOMAIN_SECTIONS = {
    "Generic CRUD services": "Objects-Common",
    "Types": "Objects-Common",
    "Authorization / Authentication / Preferences": "Objects-Common",
    "Definitions of Platform, Vehicle, and other sys. references": "Objects-Vehicles-and-Profiles",
    "The data of objects on the map": "Objects-Map-and-Terrain",
    "Route, the definition of": "Objects-Missions-and-Routes",
    "Route, the processed data": "Objects-Processed-Routes",
    "Actions: the container and specific actions data": "Objects-Route-Actions",
    "Definitions of Actions - Container and specific definitions": "Objects-Route-Actions",
    "No-Flight Zones": "Objects-Map-and-Terrain",
    "Commands": "Objects-Commands",
    "Logs": "Objects-Telemetry-and-Logs",
    "Telemetry": "Objects-Telemetry-and-Logs",
    "GeoServer": "Objects-Map-and-Terrain",
    "Non-persistent types": "Objects-Common",
    "Events": "Objects-Events",
}
DOMAIN_OVERRIDES = {
    "TraverseAlgorithm": "Objects-Missions-and-Routes", "ParameterDefinition": "Objects-Missions-and-Routes",
    "Figure": "Objects-Missions-and-Routes", "FigureParameter": "Objects-Missions-and-Routes",
    "FigurePoint": "Objects-Missions-and-Routes", "RouteUpload": "Objects-Processed-Routes",
    "RoutePass": "Objects-Processed-Routes", "Selection": "Objects-Missions-and-Routes",
    "SelectionItem": "Objects-Missions-and-Routes", "TrackPoint": "Objects-Telemetry-and-Logs",
    "Track": "Objects-Telemetry-and-Logs", "VehicleTrack": "Objects-Telemetry-and-Logs",
    "TelemetrySnapshot": "Objects-Telemetry-and-Logs", "UtmOperation": "Objects-Processed-Routes",
    "AdsbAircraft": "Objects-Map-and-Terrain", "VehicleTelemetry": "Objects-Events",
    "InvalidTakeOffAltitudeDetail": "Objects-Common", "UnsupportedProtocolVersionDetail": "Objects-Common",
    "LicenseConstraintErrorDetail": "Objects-Common", "ErrorDetailWrapper": "Objects-Common",
    "ProtocolVersion": "Objects-Common", "ImportedVehicleProfile": "Objects-Vehicles-and-Profiles",
    "ImportedPayloadProfile": "Objects-Vehicles-and-Profiles", "ExperimentalFeature": "Objects-Common",
    "ProgressDto": "Objects-Events", "ProgressStackDto": "Objects-Events",
}


def BaseName(name: str) -> str | None:
    """`ProcessRouteRequest` → `ProcessRoute`, `UploadRouteRequest2` → `UploadRoute2`."""
    if m := re.fullmatch(r"(\w+?)(Request|Response)(\d*)", name):
        return m.group(1) + m.group(3)
    return None


def Place(defs: list[Definition]) -> dict[str, str]:
    """Each message and enum to the page it is described on."""
    byName = {d.name: d for d in defs}
    groupOf = {base: page for page, bases in API_GROUPS.items() for base in bases.split()}
    page: dict[str, str] = {}
    for d in defs:
        if d.kind == "enum":
            page[d.name] = "Enums"
        elif d.file == "Domain.proto":
            target = DOMAIN_OVERRIDES.get(d.name) or DOMAIN_SECTIONS.get(d.section)
            if target is None:
                raise SystemExit(f"Domain.proto `{d.name}` (section `{d.section}`) has no page: add it to "
                                 "DOMAIN_SECTIONS or DOMAIN_OVERRIDES")
            page[d.name] = target
        elif d.name in API_EXTRA:
            page[d.name] = API_EXTRA[d.name]
        elif (base := BaseName(d.name)) is not None and base in groupOf:
            page[d.name] = groupOf[base]
    # The types of Messages.proto used by one page only go there; the others to the common page.
    for d in defs:
        if d.kind == "message" and d.name not in page:
            users = {page[u.name] for u in defs if u.kind == "message" and u.name in page
                     and u.file == "Messages.proto" and any(f.type == d.name for f in u.fields)}
            # A type used by another type of the same kind: placed in a second pass below.
            page[d.name] = users.pop() if len(users) == 1 else ""
    for _ in range(5):
        for d in defs:
            if page.get(d.name) == "":
                users = {page[u.name] for u in defs if u.kind == "message" and page.get(u.name)
                         and any(f.type == d.name for f in u.fields)}
                if len(users) == 1 and next(iter(users)).startswith("API-"):
                    page[d.name] = users.pop()
    for name, p in page.items():
        if p == "":
            page[name] = COMMON_TYPES.slug
    missing = [base for bases in API_GROUPS.values() for base in bases.split()
               if not any(BaseName(n) == base for n in byName)]
    if missing:
        # A request dropped from the SDK: only its line in API_GROUPS is left to delete.
        print(f"Requests listed in API_GROUPS but not in the protos: {', '.join(missing)}", file=sys.stderr)
    return page


# Rendering


def Anchor(name: str) -> str:
    return name.lower()


def TypeLink(type_: str, page: dict[str, str], here: str) -> str:
    name = type_.split(".")[-1]
    if name not in page:
        return f"`{type_}`"
    target = "" if page[name] == here else page[name]
    return f"[{name}]({target}#{Anchor(name)})"


def Cell(text: str) -> str:
    return text.replace("|", "\\|").replace("\n", " ")


def RenderMessage(d: Definition, page: dict[str, str], here: str, types: dict[str, int],
                  responseOf: dict[str, str]) -> list[str]:
    out = [f"### {d.name}"]
    notes = []
    if d.deprecated:
        notes.append("**Deprecated.**")
    if d.comment:
        notes.append(d.comment if d.comment.endswith(".") else d.comment + ".")
    if d.name in types:
        notes.append(f"Message type `{types[d.name]}`.")
    if d.name in responseOf:
        notes.append(f"Answered with {TypeLink(responseOf[d.name], page, here)}.")
    if notes:
        out += ["", " ".join(notes)]
    if d.fields:
        out += ["", "| # | Field | Type | Description |", "|---|---|---|---|"]
        for f in d.fields:
            desc = []
            if f.deprecated:
                desc.append("**Deprecated.**")
            if (d.name, f.name) in NOTES:
                desc.append(NOTES[(d.name, f.name)])
            elif f.comment:
                desc.append(f.comment if f.comment.endswith(".") else f.comment + ".")
            elif f.name == "clientId" and f.type == "int32" and re.search(r"Request\d*$", d.name):
                desc.append("The session, from [AuthorizeHciResponse](API-Sessions#authorizehciresponse).")
            if f.posixTime:
                desc.append("Milliseconds since 1970-01-01 UTC.")
            if f.uuid:
                desc.append("A UUID, as a string.")
            if f.readonly:
                desc.append("Set by the server.")
            if f.default is not None:
                desc.append(f"Default `{f.default}`.")
            type_ = TypeLink(f.type, page, here)
            label = {"required": "required ", "optional": "", "repeated": "repeated "}[f.label]
            out.append(f"| {f.number} | `{f.name}` | {label}{type_} | {Cell(' '.join(desc))} |")
    else:
        out += ["", "No fields."]
    out.append("")
    return out


def RenderEnum(d: Definition) -> list[str]:
    out = [f"### {d.name}"]
    if d.comment:
        out += ["", d.comment]
    out += ["", "| Value | Number | Description |", "|---|---|---|"]
    for name, number, comment in d.values:
        out.append(f"| `{name}` | {number} | {Cell(comment)} |")
    out.append("")
    return out


def Footer(version: str) -> list[str]:
    return ["---", f"_Generated from the protocol definitions of the UgCS Java SDK {version} "
                   "(`Messages.proto`, `Domain.proto`)._", ""]


def Render(sdk: Path) -> dict[str, str]:
    """Page slug to its Markdown."""
    defs = ParseProto(sdk / PROTO_DIR / "Domain.proto") + ParseProto(sdk / PROTO_DIR / "Messages.proto")
    names = [d.name for d in defs]
    if len(names) != len(set(names)):
        raise SystemExit("A name is defined twice in the protos")
    types = ParseMapping(sdk / MAPPING)
    version = SdkVersion(sdk / POM)
    page = Place(defs)
    byName = {d.name: d for d in defs}
    # A request and the response of the same name.
    responseOf = {}
    for d in defs:
        if d.name.endswith("Request") or re.fullmatch(r"\w+Request\d+", d.name):
            resp = re.sub(r"Request(\d*)$", r"Response\1", d.name)
            if resp in byName:
                responseOf[d.name] = resp

    pages: dict[str, str] = {}
    specs = {s.slug: s for s in API_PAGES + [COMMON_TYPES] + OBJECT_PAGES}
    members: dict[str, list[Definition]] = defaultdict(list)
    for d in defs:
        members[page[d.name]].append(d)

    for slug, spec in specs.items():
        items = members.get(slug, [])
        if not items:
            continue
        lines = [spec.intro, ""]
        if slug.startswith("API-"):
            # Requests with their responses first, in the order of the groups, then the other types.
            order = API_GROUPS.get(slug, "").split()
            requests = sorted((d for d in items if BaseName(d.name) in order and d.name.endswith(
                ("Request", "Request2"))), key=lambda d: order.index(BaseName(d.name)))
            shown = set()
            if requests:
                lines += ["## Requests", ""]
                lines += [f"- [{d.name}](#{Anchor(d.name)})" for d in requests]
                lines.append("")
                for d in requests:
                    lines += RenderMessage(d, page, slug, types, responseOf)
                    shown.add(d.name)
                    if d.name in responseOf and page[responseOf[d.name]] == slug:
                        lines += RenderMessage(byName[responseOf[d.name]], page, slug, types, responseOf)
                        shown.add(responseOf[d.name])
            rest = [d for d in items if d.name not in shown]
            if rest:
                lines += ["## Types" if not requests else "## Other messages and types", ""]
                for d in rest:
                    lines += RenderMessage(d, page, slug, types, responseOf)
        else:
            lines += ["## Contents", ""]
            lines += [f"- [{d.name}](#{Anchor(d.name)})" for d in items]
            lines.append("")
            for d in items:
                lines += RenderMessage(d, page, slug, types, responseOf)
        pages[slug] = "\n".join(lines + Footer(version))

    enums = members["Enums"]
    lines = ["Every enum of the protocol. Each value has its number on the wire; send the value, not "
             "its position in a list.", "", "## Contents", ""]
    lines += [f"- [{d.name}](#{Anchor(d.name)})" for d in enums] + [""]
    for d in enums:
        lines += RenderEnum(d)
    pages["Enums"] = "\n".join(lines + Footer(version))

    lines = ["Every message the UgCS server and a client exchange has a type number, which is "
             "written in the header of the message on the wire (see [[Wire Protocol]]). The Java "
             "SDK sets it itself.", "", "| Type | Message |", "|---|---|"]
    for name, number in sorted(types.items(), key=lambda kv: kv[1]):
        lines.append(f"| {number} | {TypeLink(name, page, 'Message-Types')} |")
    lines.append("")
    pages["Message-Types"] = "\n".join(lines + Footer(version))
    unmapped = [n for n in types if n not in byName]
    if unmapped:
        raise SystemExit(f"Message types of HciMessageMapping not in the protos: {', '.join(unmapped)}")
    return pages


def main():
    parser = argparse.ArgumentParser(description=__doc__.split("\n\n")[0])
    parser.add_argument("sdk", type=Path, help="the ugcs-java-sdk checkout")
    parser.add_argument("wiki", type=Path, help="the wiki checkout to write the pages into")
    args = parser.parse_args()
    pages = Render(args.sdk)
    args.wiki.mkdir(parents=True, exist_ok=True)
    for old in args.wiki.glob("*.md"):
        if GENERATED.match(old.name) and old.stem not in pages:
            old.unlink()
            print(f"deleted {old.name}")
    for slug, text in pages.items():
        (args.wiki / f"{slug}.md").write_text(text, encoding="utf-8")
    print(f"{len(pages)} pages written into {args.wiki}", file=sys.stderr)


if __name__ == "__main__":
    main()
