# gateway

The purpose of this project is to learn the pros and cons of using a multi-level gradle project with the root module
being the "api gateway" vs. splitting it up into multiple microservices with an api gateway routing to the appropriate
microservice.

### Helpful Project Locations

- [Documentation](documentation)
- [Api Calls](api-calls)
- [Quick Start Local Env Setup](Makefile)
- [Test Postgres Database](docker-compose.yml)
- [Test Suite](integration-test-module/src/test/java/com/kosmin)

### Local Setup

- Ensure docker is installed and the docker daemon is running
- in the [resources](api-gateway/src/main/resources) directory, create a file called `application-local.yml`
- add `spring.auth.jwt.key:` to the file and run the below command to generate a jwt secret

```bash
make update-jwt-key
```

- additionally, add the following remaining configurations

```bash
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/your_database_name
    username: your_username
    password: your_password
```

- this will fill out the env variables in `application.yml` and map to the docker compose credentials. This can also be
  reused to connect to your actual Postgres Database
- run the following command to create the tables and populate the database

```bash
make init
```

- create a file called `http-client.private.env.json` in [api calls](api-calls) directory with the following content

```bash
{
  "local": {
    "GATEWAY_BASE_URL": "localhost:8080",
    "TOKEN_VALUE": "<token value>",
    "API_KEY": "<make update-jwt-key>"
  }
}
```

- to get the token value, run the project locally and call
  the [token generation api](api-calls/auth/generate-token.http) and use this token for the `TOKEN_VALUE` above
- you can then run the project locally and test the [api calls](api-calls)
- to tear down the environment run the following command

```bash
make clean
```

### Learnings

#### Pros of using a multi-level gradle project over splitting into multiple microservices

- since all modules are in one project, debugging is typically simpler. If something like an api gateway is in place,
  two projects would potentially need to be set up locally to determine where the failure point is
- since modules are just function calls between each other as opposed to api calls, random possible network failures are
  reduced since api calls between api gateway and the appropriate service is now just a function call
- if there are a suite of microservices that do different things but share many common components, putting them all in a
  common project allows declaring a shared module that all modules can pull from as opposed to setting up a new
  microservice for each task
- when splitting a service into multiple microservices, in the context of integration testing, the line between what
  end-end test should be completed at each layer gets complicated. ex. should the api gateway test end-to-end all the
  way to the completion of the microservice call? If so, is there any point in testing the microservice end-to-end?
  There can be some redundancy in this approach whereas testing in individual component or just the single entry point
  can assist in avoiding duplication while implementing adequate test cases
- if there is a certain part of a multi-level gradle project that experiences heavier traffic than the rest of the
  service, it easily becomes a candidate to split into a standalone service

#### Cons of using a multi-level gradle project over splitting into multiple microservices

- multi-level gradle projects for a few modules is easy to keep organized and can help assist in avoiding duplication
  over multiple microservices, however if there are too many modules being added to the service it can easily become
  very difficult to maintain
- if there is a specific module being heavily used, since everything is in a single project, deployment resources cannot
  be increased to that single module. Instead, the entire project will need more memory or servers even if a single
  module is causing most of the traffic
- if there is a single contributor to a multi-level gradle project, it is easy to maintain and declare dependencies
  between modules that are common vs. module specific dependencies. If there are multiple contributors, clashing between
  common dependencies between modules could easily become an issue