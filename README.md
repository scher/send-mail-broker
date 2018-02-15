# Send mail broker 
Send mail broker that accepts the necessary information and sends emails 
via Mailgun and Sengrid services.

Application uses AWS SQS for messages queueing.

* [Mailgun](https://www.mailgun.com)
* [SendGrid](https://sendgrid.com)  

Solution handles multiple recipients and plain text message body.

## Installation and Start

1. build project:
    ```bash
    mvn clean package
    ```
2. You have to add application.properties file to config dir, with
mailgun and sendgrid credentials. See application.properties.example

3. Setup AWS SQS Credentials
    1. Create a credentials file in the location ~/.aws with name "credentials".
    2. Under the `default` profile fill in your Access Key ID and Secret Access Key:
        ```
          [default]
          aws_access_key_id =
          aws_secret_access_key =
          ```
4. Run application 
    ```
    java -jar target/siteminder-0.1.0.jar
    ```
## Play with it

There is handy script `cmd/siteminder`

1. Configure email recipients and email body in this script and execute it.
2. Check your mailbox!

## Technical solution and motivation

Application consists of three main components
* Spring Boot REST Service.
* AWS SQS
* SQS Poller Service

In current configuration SQS Poller and REST Service are deployed as one application,
but they could be easily decoupled and run as independent application. 

### Data flow

REST service accepts user input and implements basic input validation.
Then message is being pushed to AWS SQS.
SQS Poller periodically fetches messages from SQS service and sends emails.  

## Todo

* Leverage AWS Lambda for SQS Polling and Send mail workers 
* Configure SQS Dead-Letter queue, to get rid of invalid messages. 
* Limit size of input message.
* Provide tests.

## Testing

The following considerations should be taken into account during test.

1. Mock testing of MVC Endpoint
2. Separate testing of SQS Poller
3. Mock testing of *Request classes
4. E2E/Integration test of the whole application. Mock/Local implementation of SQS is required.
