The Java SDK speaks the UgCS server's protocol for you. To talk to the server from another
language, implement the same protocol: Protocol Buffers messages over a TCP connection, each with a
small header.

## The connection

A TCP connection to the server's port, 3334 by default. Both sides send messages over it at any
time: responses to requests, and the events of subscriptions.

## A message

Each message is a 16-byte header followed by the message itself. The numbers of the header are
big-endian:

| Bytes | Field | Value |
|---|---|---|
| 0-1 | Signature | `0x4850` ("HP") |
| 2-3 | Protocol version | `1` |
| 4-7 | Instance id | A signed 32-bit number, see below. |
| 8-11 | Message type | The type number of the message, from the [[Message Types]] page, e.g. `20` for `AuthorizeHciRequest`. |
| 12-15 | Length | The length of the message which follows, in bytes. |
| 16- | Message | The message, serialized with Protocol Buffers (binary). |

The SDK refuses messages longer than 64 MB.

For example, `AuthorizeHciRequest` with `clientId = -1` and `clientVersion` 2.0, as the first
request (instance id 0):

```
4850 0001 00000000 00000014 00000011  08ffffffffffffffffff011a0408021000
sig  ver  instance type=20  length=17 message
```

## Requests and responses

- The client gives each request an **instance id** - the Java SDK counts from 0 - and the
  server's answer has the same id. That is how a response is matched to its request.
- The answer is the response of the request, or an [Error](API-Operations#error) (type `0`).
- The progress and data messages of long operations can have the instance id of a request too:
  `OperationStatus` (type `2`), `OperationStream` (`3`), `OperationStreamRequest` (`4`),
  `CancelOperationRequest` and `CancelOperationResponse` (`5`, `6`). They are not the answer, and
  the Java SDK skips them while it waits for the response or the error. An `OperationStream`
  carries a chunk of data, and an empty chunk ends its stream.
- Events of subscriptions come as `Notification` messages (type `1`) with the instance id **-1**.

## A session

1. Send [AuthorizeHciRequest](API-Sessions#authorizehcirequest) with `clientId = -1` and your
   protocol version in `clientVersion`: the server creates a session and returns its id in
   `AuthorizeHciResponse.clientId`.
2. Send [LoginRequest](API-Sessions#loginrequest) with that `clientId`, the login and the
   password.
3. Put the `clientId` into every later request.

See [[Connecting and Sessions]].

## Generating the messages

The messages are defined in three files of the SDK, in `ucs-api/src/main/proto`: `Messages.proto`
(requests and responses), `Domain.proto` (objects and events) and `Options.proto` (UgCS options of
the definitions; it imports Google's `descriptor.proto`, which comes with `protoc`). They are
`proto2`. Generate the code for your language with `protoc`, e.g. for Python:

```sh
protoc -I ucs-api/src/main/proto --python_out=. \
    ucs-api/src/main/proto/Options.proto \
    ucs-api/src/main/proto/Domain.proto \
    ucs-api/src/main/proto/Messages.proto
```

Use a `protoc` matching the Protocol Buffers library of your language; for Python, the one of the
`grpcio-tools` package (`python -m grpc_tools.protoc ...`) matches the `protobuf` package it
installs.

## In Python

Writing and reading messages:

```python
import socket
import struct

import Messages_pb2

SIGNATURE, VERSION = 0x4850, 1
AUTHORIZE_HCI_REQUEST = 20  # see the Message Types page


def Frame(message, messageType: int, instanceId: int) -> bytes:
    body = message.SerializeToString()
    return struct.pack(">HHiii", SIGNATURE, VERSION, instanceId, messageType, len(body)) + body


def Receive(sock: socket.socket, n: int) -> bytes:
    data = b""
    while len(data) < n:
        chunk = sock.recv(n - len(data))
        if not chunk:
            raise ConnectionError("The server closed the connection")
        data += chunk
    return data


def ReadMessage(sock: socket.socket) -> tuple[int, int, bytes]:
    """The next message: (instance id, message type, body)."""
    signature, version, instanceId, messageType, length = struct.unpack(">HHiii", Receive(sock, 16))
    if signature != SIGNATURE or version != VERSION:
        raise ValueError("Not a UgCS message")
    return instanceId, messageType, Receive(sock, length)


request = Messages_pb2.AuthorizeHciRequest(clientId=-1)
request.clientVersion.major, request.clientVersion.minor = 2, 0
sock = socket.create_connection(("localhost", 3334))
sock.sendall(Frame(request, AUTHORIZE_HCI_REQUEST, 0))
```

Then read messages until one has the instance id of the request and is neither a progress nor a
data message (types 2 to 6); parse it with the response class of its type, e.g.
`Messages_pb2.AuthorizeHciResponse.FromString(body)` for type `21`, or `Messages_pb2.Error` for
type `0`.
