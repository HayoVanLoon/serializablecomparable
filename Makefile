DEST := $(shell pwd)/data.out
SRC := $(DEST)
TYPE := 'nested'

build-shared:
	$(MAKE) -C shared build

build-json:
	$(MAKE) -C json build

generate-json:
	$(MAKE) -C json generate

run-json:
	@$(MAKE) -C json run

build-kryo:
	$(MAKE) -C kryo build

generate-kryo:
	$(MAKE) -C kryo generate

run-kryo:
	@$(MAKE) -C kryo run

build-native:
	$(MAKE) -C native build

generate-native:
	$(MAKE) -C native generate

run-native:
	@$(MAKE) -C native run

build-protobuf:
	$(MAKE) -C protobuf build

generate-protobuf:
	$(MAKE) -C protobuf generate

run-protobuf:
	@$(MAKE) -C protobuf run

clean:
	$(MAKE) -C shared clean
	$(MAKE) -C json clean
	$(MAKE) -C kryo clean
	$(MAKE) -C native clean
	$(MAKE) -C protobuf clean

build: build-shared build-json build-kryo build-native build-protobuf

generate:
	$(MAKE) -C native generate TYPE=nested DEST=$(DEST)

run:
	@$(MAKE) run-json TYPE=$(TYPE) SRC=$(SRC)
	@$(MAKE) run-kryo TYPE=$(TYPE) SRC=$(SRC)
	@$(MAKE) run-native TYPE=$(TYPE) SRC=$(SRC)
	@$(MAKE) run-protobuf TYPE=$(TYPE) SRC=$(SRC)