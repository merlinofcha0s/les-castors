## Synopsis

Project that helps people to find good craftsmen. Basically, you sign in, post the work you want to be done in your appartment / house, then craftsmen contact you. You can choose among them referering to their ranks (5 stars) and comments from other clients who does their work with them.

## Motivation

I've worked alone to produce the code of this project and my plan was to build a startup based on this website with other non-tech colleagues.

## Installation

### Prerequisite

- Maven 3
- JDK 8
- Install Postgres 9.x
- Account in MandrillApp (https://mandrillapp.com)
- Account in Cloudinary (http://cloudinary.com/)
- Key for Recaptcha 2 (https://www.google.com/recaptcha/admin)

You'll find the "standalone.xml" for the Wildfly configuration in the runtime folder.

## Build the application
`mvn clean install`

### Create DB

Create a database named batimen on your postgres

`mvn process-test-resources -Ddatabase.name=batimen -Ddatabase.login=toto Ddatabase.password=tata`

### Build without test

`mvn clean install -Dmaven.test.skip=true`

## Tests

Basically there are two kinds of tests, integration test for webservice with arquillian and integration test with selenium

### Webservice test (Arquillian)

In order to run the webservice test you have to get wildfly and fill the jboss.home property in the castor-ws pom.

### Selenium test

 In order to run the selenium test you have to deploy the application (webservice + webapp) on Wildlfy. For that you can deploy by means of an IDE (Eclipse or IntelliJ) or manually. I haven't set up the automatic deployment through maven on dev env.

### Launch test

`mvn clean install`

## License

GNU General Public License v3.0