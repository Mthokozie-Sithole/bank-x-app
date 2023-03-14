Bank-X-App

This is a springboot application that uses the embedded tomcat and H2 database.

To run the application you can run the main class.

To test the Rest endpoints exposed by the application you can use a client like Postman.

To view the database entries you can access the H2 database via http://localhost:8080/h2/login.jsp

The postman collection is in the project path to test the application

Note: The /onboard endpoint should first be called to create new customers in the H2 in-memory database before all the other endpoints can be tested.