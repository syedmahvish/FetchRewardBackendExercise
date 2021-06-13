# FetchRewardBackendExercise
## Description:
### A web service that accepts HTTP requests and returns responses based on the conditions for reward system

## How to run using command line
#### Download the provided jar file
#### Go to the same folder in which fetchRewards.jar has been downloaded and run the jar file using the following command
java -jar fetchRewards.jar  --server.port=8090

## Using curl command to make GET and POST request:

### Method type : POST
### Method name : addRewards
curl  -i -X POST -H "Content-Type: application/json"  -d '{ "payer": "DANNON", "points": 1000, "timestamp": "2020-11-02T14:00:00Z" }' http://localhost:8090/addRewards

### Method type : POST
### Method name : spendPoints
curl -i -X POST -H "Content-Type: application/json"  -d '{ "points": 1000 }'  http://localhost:8090/spendPoints

### Method type : GET
### Method name : checkBalance
curl -i http://localhost:8090/checkBalance


# ---------------------- OR ---------------------

## How to run using Intellij:
#### Download complete project from github.
#### Open in IDE(IntellIJ) build and run project.

## Use postman to make GET and POST request.

### Add transaction using postman:
#### Method type : POST
#### http://localhost:8090/addRewards (localHost/addRewards)
#### Parameter : In body : { "payer": "DANNON", "points": 1000, "timestamp": "2020-11-02T14:00:00Z" }
#### Return type : HttpStatus : Return : HttpStatus: Ok : success,  EXPECTATION_FAILED : if datatype/parameter does not matches


###  Spend Points using postman:
#### Method type : POST
#### http://localhost:8090/spendPoints (localHost/spendPoints)
#### Parameter : In body : { "points": 5000 }
#### Return type : HttpStatus : Ok - success, FORBIDDEN - unsuccessful.

### Check balance using Browser
#### Method type : GET
#### http://localhost:8090/checkBalance (localHost/checkBalance)
#### Return type : HttpStatus : Ok - success


