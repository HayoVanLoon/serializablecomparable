SAMPLE_SIZE := 10000
CYCLES := 1000
MAX_DURATION := 30

clean:
	mvn clean
	rm -f src/main/java/nl/hayovanloon/serializablecomparable/protobuf/generated/*

build: clean
	protoc --java_out=src/main/java messages.proto
	mvn compile assembly:single

generate:
	java -jar target/*.jar generate $(TYPE) $(DEST) $(SAMPLE_SIZE)

run:
	@java -jar target/*.jar run $(TYPE) $(SRC) $(CYCLES) $(MAX_DURATION)
