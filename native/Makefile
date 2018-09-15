SAMPLE_SIZE := 10000
CYCLES := 1000
MAX_DURATION := 60

clean:
	mvn clean

build: clean
	mvn compile assembly:single

generate:
	java -jar target/*.jar generate $(TYPE) $(DEST) $(SAMPLE_SIZE)

run:
	@java -jar target/*.jar run $(TYPE) $(SRC) $(CYCLES) $(MAX_DURATION)
