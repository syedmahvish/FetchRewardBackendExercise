# FetchRewardBackendExercise
## Description:
### A web service that accepts HTTP requests and returns responses based on the conditions for reward system

## How to run:
######## Download complete project from github
######## Need tomcat to setup localhost
######## Open in IDE(IntellIJ) build and run project
######## Use postman to make POST request.
######## Use webBrowser to make GET request.


## Add transaction using postman:
######## Method type : POST
######## http://localhost:8090/addRewards (localHost/addRewards)
######## Parameter : In body : { "payer": "DANNON", "points": 1000, "timestamp": "2020-11-02T14:00:00Z" }
######## Return type : HttpStatus : Return : HttpStatus: Ok : success,  EXPECTATION_FAILED : if datatype/parameter does not matches


##  Spend Points using postman:
######## Method type : POST
######## http://localhost:8090/spendPoints (localHost/spendPoints)
######## Parameter : In body : { "points": 5000 }
######## Return type : HttpStatus : Ok - success, FORBIDDEN - unsuccessful.

## Check balance using Browser
######## Method type : GET
######## http://localhost:8090/checkBalance (localHost/checkBalance)
######## Return type : HttpStatus : Ok - success


