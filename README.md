# Opening House
by João Carlos (https://www.linkedin.com/in/joaocarlosvale/)

This project consists of a REST API example that takes JSON-formatted opening hours of a restaurant as an input and 
outputs hours in **more human readable format**.

## Technologies used:
* Kotlin
* Spring and Spring Boot
* Maven 

## Logic
1. The Controller receives a JSON in a DTO object representing a week business hours of a restaurant
2. The Service is called to process the DTO into a more human readable format
3. Using a Converter, the DTO is transformed in a Domain Object (BusinessHours)
4. The BusinessHours is validate by a Validator and it's verified:
    - the range of hour (in Unix format values [0-86399])
    - the OPEN/CLOSE type of events sequence, it´s not possible two equal types in a row.
    - the sequence of working days, it´s not possible in a working day OPEN the restaurant and no CLOSE in the same day or
    the next day is NOT a working day. The same in case of the first EVENT in a day is a CLOSE event but the previous day is 
    NOT a working day.
5. It´s sorted the event types (OPEN/CLOSE hours), for example: if the restaurant OPEN Sunday but CLOSE Monday, 
the CLOSE event is inserted in the Sunday.
6. Finally the Business Hour is printed in a **more human readable format**.
7. It´s returned the String and one Accepted Http Code (202)

NOTES: 
1. In case of Parser Error (step 4), it throws an ParserException and returns a **Bad Request** Http code.
2. In case the invalid JSON Format, one **Unsupported Media Type** is returned.
3. The exceptions are controlled by one Main Exception Handler (@ControllerAdvice)
4. If one provided date contains minutes and seconds, only the hour field is used.

## Endpoint:
***POST: /wolt/insertOpeningHours***

### Example:
localhost:8080/wolt/insertOpeningHours

###Input (*json*) example:
```json
{
    "monday" : [],
    "tuesday" : [
    {
        "type" : "open",
        "value" : 36000 },
    {
        "type" : "close",
        "value" : 64800
    } ],
    "wednesday" : [],
    "thursday" : [
    {
        "type" : "open",
        "value" : 36000
    },
    {
    "type" : "close",
    "value" : 64800
    }],
    "friday" : [
    {
        "type" : "open",
        "value" : 36000
    } ],
    "saturday" : [ {
        "type" : "close",
        "value" : 3600 },
    {
        "type" : "open",
        "value" : 36000
    } ],
    "sunday" : [ {
        "type" : "close",
        "value" : 3600
    },
    {
        "type" : "open",
        "value" : 43200 
    },
    {
        "type" : "close", 
        "value" : 75600
    }]
}
```
Output:
```
Monday: Closed
Tuesday: 10 AM - 6 PM
Wednesday: Closed
Thursday: 10 AM - 6 PM
Friday: 10 AM - 1 AM
Saturday: 10 AM - 1 AM
Sunday: 12 PM - 9 PM
```

To run:

    mvn spring-boot:run
    
To stop:
  
    (ctrl or CMD) + C

To run tests:

    mvn test

## PART 2: Some Considerations
The opening / closing time is the UNIX time (1.1.1970 as a date) reference, 
e.g. 32400 = 9 AM, 37800 = 10.30 AM, max value is 86399 = 11.59:59 PM.

The problem of this approach is to establish the Date in 1.1.1970 and to use the 24 hour range of this date.
One alternative is use a standard time like the range [00 - 24], more easy to recognize the hour of OPEN or CLOSE 
the restaurant.

### Possible refactoring
1. Modify the DTO to use a MAP of Days Of Week instead one field for each day (ex Monday, Tuesday,...)
