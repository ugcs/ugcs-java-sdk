This package includes pre-compiled protocol buffer Java sources of the protocol messages. To re-generate sources you will need  to install [protobuf compiler 2.4.1](https://github.com/google/protobuf/releases/tag/v2.4.1).

```
protoc -I=src/main/proto --java_out=src/main/java src/main/proto/*.proto
```
