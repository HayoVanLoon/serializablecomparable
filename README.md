# Making Serialization Comparable

Let's you generate some numbers on various Java serialization techniques.

Currently contains:
* Native Java
* Jackson (JSON) [https://github.com/FasterXML/jackson](https://github.com/FasterXML/jackson)
* Kryo [https://github.com/EsotericSoftware/kryo](https://github.com/EsotericSoftware/kryo)
* Protobuf [https://developers.google.com/protocol-buffers/](https://developers.google.com/protocol-buffers/)

## What it does
By default 10k objects are generated and written to a file using native Java 
serialization.

This file is then read by each of the simulators. The objects are deserialized
through standard Java serialization and then serialized by each simulators own
serialization method.



## Prerequisites

* GNU Make
* JDK 8 or higher
* Maven3
* Protobuf Compiler (protoc) (available through [https://developers.google.com/protocol-buffers/](https://developers.google.com/protocol-buffers/))

## Getting the numbers

Build the all projects.
```bash
make build
```

Generate base data shared by all simulators.
```bash
make generate
```

Run all simulators.
```bash
make run
```