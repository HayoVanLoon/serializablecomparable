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

This file then serves as input for the simulators. These will deserialize those 
objects again through standard Java serialization and store the entire data set 
in memory.

They will then serialize the objects sequentially through their own 
serialization method. Upon serializtion, the creation time and size of the 
serialised object are recorded.

Each simulator will printi a small report to the standard output upon 
completion. 


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

You can also run a specific simulator by adding its short id. You will have to 
provide TYPE (simple/nested) and SRC (data file) parameters though
```bash
make run-protobuf TYPE=nested SRC=`pwd`/data.out
```