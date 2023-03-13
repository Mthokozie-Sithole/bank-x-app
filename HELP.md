Bank-X-App

This is a springboot application that uses the embedded tomcat and H2 database.

To run the application you can run the main class.

To test the Rest endpoints exposed by the application you can use a client like Postman.

To view the database entries you can access the H2 database via http://localhost:8080/h2/login.jsp

The following payload can be used for testing the customer onboarding endpoint

{
"firstName": "Mthoko",
"lastName": "Sithole",
"gender": "Male",
"ethnicity": "African",
"email": "mondise.mtho@gmail.com"
}

To test the retrieving of all customers the following endpoint can be used
http://localhost:8080/customers/retrieveAllCustomers


The following payload can be used for testing the receive payment endpoint

{
"accountNumber": "534121741189133013128",
"paymentAmount": "1000",
"toSavingsAccount": "true"
}

The following payload can be used for testing the transfer amount endpoint
{
"transferAmount": "200",
"customerId": "1",
"fromSavingsAccount": "true"
}

To test the retrieving of all transaction history the following endpoint can be used
http://localhost:8080/transaction/history


The following payload can be used for testing the debiting of the customer accounts
{
"accountNumber": "288938522741478087175663",
"amount": "1000",
"accountType": ""
}

The following payload can be used for testing the crediting of the customer accounts

{
"accountNumber": "912423402418958826256",
"amount": "1000",
"accountType": ""
}

Note: The /onboard endpoint should first be called to create new customers in the H2 in-memory database before all the other endpoints can be tested.