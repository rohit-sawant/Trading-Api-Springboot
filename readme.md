# Trading REST-API

A portfolio tracking API needs to be implemented which allows adding/deleting/updating trades and can do basic return calculations.

 [Click here](https://springboottradingapi.herokuapp.com/trade) to start using the api.

## Running Locally

Make sure you have Java and Maven installed.  Also, install the [Heroku Toolbelt](https://toolbelt.heroku.com/).

```sh
$ git clone https://github.com/rohit-sawant/Trading-Api-Springboot.git 
$ cd Trading-Api-Springboot
$ mvn install
$ heroku local web
```
Your app should now be running on [localhost:5000](http://localhost:5000/).

As a JPA sample, you can get the traded list on [localhost:5000/trade](http://localhost:5000/trade)
And in order to add a trade, send a POST request to [localhost:5000/trade](http://localhost:5000/trade) with a json message.
Below is a curl example.  
````sh
$ curl -X POST -H "Content-Type: application/json" -d '{"symbol":"tcs","price":100,"type":"buy","qty":10}' http://localhost:5000/trade/
````
For reference, my test postgresql is configured in the application.properties. 


## Running from Eclipse
* Install maven eclipse plugin - m2e. (if you are a strong spring user, Spring Tool Suite is recommended which includes the m2e.)
* Import 'Existing Maven Project' and select the directory you clone.
* Run 'SampleApplication' as a Java Application or Spring Boot App if you installed Spring Tool Suite. 
Your app should now be running on [localhost:8080](http://localhost:8080/).

## Deploying to Heroku

```sh
$ heroku create <appname>
$ git push heroku master
$ heroku open
```

## Documentation


## Get list of Things

### Request

`GET /trade/`

    curl -i -H 'Accept: application/json' https://springboottradingapi.herokuapp.com/trade

### Response

    HTTP/1.1 200
    Server: Cowboy
    Connection: keep-alive
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Mon, 04 Oct 2021 16:19:00 GMT
    Via: 1.1 vegur

    [{"id":1,"symbol":"QWE","createdOn":"2021-10-04T14:50:24.188967","upDatedOn":"2021-10-04T14:50:24.188998","type":"buy","price":100.0,"qty":10},{"id":3,"symbol":"TCS","createdOn":"2021-10-04T15:49:21.15401","upDatedOn":"2021-10-04T15:49:21.154038","type":"buy","price":100.0,"qty":10}]

## Get a specific trade with id

### Request

`GET /trade/id`

    curl -i -H 'Accept: application/json' https://springboottradingapi.herokuapp.com/trade/1

### Response

    HTTP/1.1 200
    Server: Cowboy
    Connection: keep-alive
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Mon, 04 Oct 2021 16:32:29 GMT
    Via: 1.1 vegur

    {"status":"success","message":"empty ","trade":{"id":1,"symbol":"QWE","createdOn":"2021-10-04T14:50:24.188967","upDatedOn":"2021-10-04T14:50:24.188998","type":"buy","price":100.0,"qty":10}}

## Get a non-existent trade

### Request

`GET /thing/id`

    curl -i -H 'Accept: application/json' https://springboottradingapi.herokuapp.com/trade/56

### Response

    HTTP/1.1 400
    Server: Cowboy
    Connection: keep-alive
    Content-Type: application/json
    Transfer-Encoding: chunked
    Date: Mon, 04 Oct 2021 17:09:55 GMT
    Via: 1.1 vegur

    {"status":"error","message":"no such id exist! ","trade":null}

## Create another new trade

### Request

`POST /thing/`

    curl -d '{"symbol":"aewf","price":10,"type":"buy","qty":10}' -H "Content-Type: application/json" -X POST https://springboottradingapi.herokuapp.com/trade

### Response

    HTTP/1.1 201 Created
    Date: Thu, 24 Feb 2011 12:36:31 GMT
    Status: 201 Created
    Connection: close
    Content-Type: application/json
    Location: /thing/2
    Content-Length: 35

    {"status":"success","message":"","trade":{"id":7,"symbol":"AEWF","createdOn":"2021-10-04T22:45:13.4479093","upDatedOn":"2021-10-04T22:45:13.4479093","type":"buy","price":10.0,"qty":10}}

## Get holding

### Request

`GET /holdings/`

    curl -i -H 'Accept: application/json' https://springboottradingapi.herokuapp.com/holdings

### Response

    HTTP/1.1 200 OK
    Date: Thu, 24 Feb 2011 12:36:31 GMT
    Status: 200 OK
    Connection: close
    Content-Type: application/json
    Content-Length: 74

    [{"id":2,"symbol":"QWE","qty":10,"avg_price":100.0},{"id":4,"symbol":"TCS","qty":10,"avg_price":100.0},{"id":6,"symbol":"WIPRO","qty":10,"avg_price":100.0},{"id":8,"symbol":"AEWF","qty":10,"avg_price":10.0}]


## Delete a trade using id

### Request

`DELETE /trade/id`

    curl -i -H 'Accept: application/json' -X POST -d'_method=DELETE' http://localhost:7000/thing/2/

### Response

    HTTP/1.1 200 NO CONTENT
    Date: Thu, 24 Feb 2011 12:36:31 GMT
    Status: 204 NO CONTENT 
    Connection: close
    Content-Type: application/json
                                                        
    {"status":"success","message":"deleted","trade":null}





