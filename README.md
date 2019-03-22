# money-transfer
Test project
## Running
Download [executable jar](/executable/money-transfer-1.0.jar).

Run
```
java -jar money-transfer-1.0.jar
```
or 
```
java -jar money-transfer-1.0.jar loadDummyData
```
to upload dummy data.

To run integration tests:
```
mvn failsafe:integration-test
```

## API
After running project the API description available by the next address:
[http://localhost:9090/application.wadl](http://localhost:9090/application.wadl)