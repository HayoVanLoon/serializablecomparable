DEST := $(shell pwd)/data.out
SRC := $(DEST)
TYPE := 'nested'

build-shared:
	$(MAKE) -C shared build

build-gson:
	$(MAKE) -C gson build

run-gson:
	@$(MAKE) -C gson run

build-json:
	$(MAKE) -C json build

run-json:
	@$(MAKE) -C json run

build-kryo:
	$(MAKE) -C kryo build

run-kryo:
	@$(MAKE) -C kryo run

build-native:
	$(MAKE) -C native build

run-native:
	@$(MAKE) -C native run

build-protobuf:
	$(MAKE) -C protobuf build

run-protobuf:
	@$(MAKE) -C protobuf run

build-protobuflean:
	$(MAKE) -C protobuflean build

run-protobuflean:
	@$(MAKE) -C protobuflean run

clean:
	$(MAKE) -C shared clean
	$(MAKE) -C json clean
	$(MAKE) -C kryo clean
	$(MAKE) -C native clean
	$(MAKE) -C protobuf clean
	$(MAKE) -C protobuflean clean

build: build-shared build-gson build-json build-kryo build-native build-protobuf build-protobuflean

generate:
	$(MAKE) -C native generate TYPE=nested DEST=$(DEST)

run:
	@$(MAKE) run-gson TYPE=$(TYPE) SRC=$(SRC)
	@$(MAKE) run-json TYPE=$(TYPE) SRC=$(SRC)
	@$(MAKE) run-kryo TYPE=$(TYPE) SRC=$(SRC)
	@$(MAKE) run-native TYPE=$(TYPE) SRC=$(SRC)
	@$(MAKE) run-protobuf TYPE=$(TYPE) SRC=$(SRC)
	@$(MAKE) run-protobuflean TYPE=$(TYPE) SRC=$(SRC)