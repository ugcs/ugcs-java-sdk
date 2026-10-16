"""
Tests of `generate.py`: `python3 -m pytest wiki/tools`.

The reference generator against a small SDK laid out like the real one: where requests, objects and
enums go, the type numbers, the descriptions it adds, and the pages it writes and deletes.
"""

import importlib.util
import sys
from pathlib import Path

import pytest

spec = importlib.util.spec_from_file_location("generate", Path(__file__).parent / "generate.py")
generate = importlib.util.module_from_spec(spec)
# Registered first: `dataclasses` looks the module up by name.
sys.modules["generate"] = generate
spec.loader.exec_module(generate)

DOMAIN = """syntax = "proto2";
import "Options.proto";

//*****************************************************************************
// Enums
//*****************************************************************************

enum FailsafeAction {
	FA_GO_HOME = 0;
	FA_LAND = 1; // Lands where it is.
}

//-- Route, the definition of -------------------------------------------------

message Route {
	optional int32 id = 1;
	optional string name = 4;
	optional int64 creationTime = 5 [(ugcs_field_posix_time) = true];
	optional FailsafeAction action = 6;
	optional string oldField = 7 [deprecated=true];
}

//-- Commands -----------------------------------------------------------------

message Command {
	optional string code = 1;
}
"""

MESSAGES = """syntax = "proto2";
import "Domain.proto";

// Types (DTO)

message Wgs84LocationDto {
	required double latitude = 1;
}

// Messages

message Error {
	optional string errorMessage = 1;
	optional int32 errorCode = 2;
}

message ProcessRouteRequest {
	required int32 clientId = 1;
	required Route route = 2;
	optional string token = 3;
}

message ProcessRouteResponse {
	required Route route = 1;
}

// deprecated
message ExportRouteRequest {
	required int32 clientId = 1;
	/* The place to start
	   from. */
	optional Wgs84LocationDto start = 2;
}

message SendCommandRequest {
	required int32 clientId = 1;
	optional Command command = 2;
	optional int64 interDelay = 4 [default = 0];
}
"""

MAPPING = """public class HciMessageMapping extends ProtoMessageMapping {
	public HciMessageMapping() {
		putMapping(0, MessagesProto.Error.class);
		putMapping(32, ProcessRouteRequest.class);
		putMapping(33, ProcessRouteResponse.class);
		putMapping(54, ExportRouteRequest.class);
		putMapping(64, SendCommandRequest.class);
	}
}
"""


@pytest.fixture
def sdk(tmp_path) -> Path:
    root = tmp_path / "ugcs-java-sdk"
    (root / generate.PROTO_DIR).mkdir(parents=True)
    (root / generate.PROTO_DIR / "Domain.proto").write_text(DOMAIN)
    (root / generate.PROTO_DIR / "Messages.proto").write_text(MESSAGES)
    (root / generate.MAPPING).parent.mkdir(parents=True)
    (root / generate.MAPPING).write_text(MAPPING)
    (root / generate.POM).write_text("<project><artifactId>ugcs-java-sdk</artifactId>"
                                     "<version>9.1.0</version></project>")
    return root


def test_parse_proto(sdk):
    defs = {d.name: d for d in generate.ParseProto(sdk / generate.PROTO_DIR / "Domain.proto")}

    assert defs["FailsafeAction"].section == "Enums"
    assert defs["FailsafeAction"].values == [("FA_GO_HOME", 0, ""), ("FA_LAND", 1, "Lands where it is.")]
    route = defs["Route"]
    assert route.section == "Route, the definition of"
    # `optional` is a field, not an `option` line.
    assert [f.name for f in route.fields] == ["id", "name", "creationTime", "action", "oldField"]
    assert route.fields[2].posixTime and route.fields[4].deprecated
    assert defs["Command"].section == "Commands"

    messages = {d.name: d for d in generate.ParseProto(sdk / generate.PROTO_DIR / "Messages.proto")}
    assert messages["ExportRouteRequest"].deprecated and messages["ExportRouteRequest"].comment == ""
    assert messages["ExportRouteRequest"].fields[1].comment == "The place to start from."
    assert messages["SendCommandRequest"].fields[2].default == "0"


def test_render_places_and_links(sdk):
    pages = generate.Render(sdk)

    assert set(pages) == {"API-Routes", "API-Import-and-Export", "API-Commands", "API-Operations",
                          "Objects-Missions-and-Routes", "Objects-Commands", "Enums", "Message-Types"}
    routes = pages["API-Routes"]
    assert "### ProcessRouteRequest\n\nMessage type `32`. Answered with [ProcessRouteResponse](#processrouteresponse)." in routes
    assert "| 1 | `clientId` | required `int32` | The session, from [AuthorizeHciResponse]" in routes
    assert "| 2 | `route` | required [Route](Objects-Missions-and-Routes#route) |  |" in routes
    assert "SDK 9.1.0" in routes

    export = pages["API-Import-and-Export"]
    assert "### ExportRouteRequest\n\n**Deprecated.** Message type `54`." in export
    # A type of Messages.proto used by one page only is shown on it.
    assert "### Wgs84LocationDto" in export

    objects = pages["Objects-Missions-and-Routes"]
    assert "| 5 | `creationTime` | `int64` | Milliseconds since 1970-01-01 UTC. |" in objects
    assert "| 6 | `action` | [FailsafeAction](Enums#failsafeaction) |  |" in objects
    assert "| 7 | `oldField` | `string` | **Deprecated.** |" in objects
    assert "| `FA_LAND` | 1 | Lands where it is. |" in pages["Enums"]
    assert "| 64 | [SendCommandRequest](API-Commands#sendcommandrequest) |" in pages["Message-Types"]
    assert "| 0 | [Error](API-Operations#error) |" in pages["Message-Types"]


def test_an_object_in_no_section_stops_it(sdk):
    domain = sdk / generate.PROTO_DIR / "Domain.proto"
    domain.write_text(domain.read_text() + "\n//-- Brand new things ---\n\nmessage Gadget {\n\toptional int32 id = 1;\n}\n")

    with pytest.raises(SystemExit, match="Gadget"):
        generate.Render(sdk)


def test_main_writes_and_deletes_generated_pages_only(sdk, tmp_path, monkeypatch):
    wiki = tmp_path / "wiki"
    wiki.mkdir()
    (wiki / "Home.md").write_text("Hand-written.")
    (wiki / "API-Gone.md").write_text("No longer generated.")
    monkeypatch.setattr("sys.argv", ["generate.py", str(sdk), str(wiki)])

    generate.main()

    assert (wiki / "Home.md").read_text() == "Hand-written."
    assert not (wiki / "API-Gone.md").exists()
    assert (wiki / "API-Routes.md").is_file() and (wiki / "Enums.md").is_file()
