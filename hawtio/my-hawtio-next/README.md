curl -k -d '{"type":"version"}' -H "Content-Type: application/json"  -X POST https://s2i-odr-integrator-s2i-sandbox.apps.dev-ocp4.ist.sus.ebc.gov.on.ca/actuator/hawtio/jolokia/

curl -k -X GET https://s2i-odr-integrator-s2i-sandbox.apps.dev-ocp4.ist.sus.ebc.gov.on.ca/actuator/hawtio/jolokia/
# Docker 
   docker build  . -t s2i-hawtio-next

  docker run -it --rm -p 3000:3000 s2i-hawtio-next
docker tag s2i-hawtio-next gsicregistry.azurecr.io/s2i-hawtio-next:v1-07-28-23-17-01
docker push gsicregistry.azurecr.io/s2i-hawtio-next:v1-07-28-23-17-01
    docker run \
    -it \
    --rm \
    -v ${PWD}:/app \
    -v /app/node_modules \
    -p 3001:3000 \
    s2i-hawtio-next

   docker build  -f ./Dockerfile.prod . -t s2i-hawtio-next
   docker run -it --rm -p 1337:80 s2i-hawtio-next
# Hawtio.next

[![Test](https://github.com/hawtio/hawtio-next/actions/workflows/test.yml/badge.svg)](https://github.com/hawtio/hawtio-next/actions/workflows/test.yml)
[![Lint](https://github.com/hawtio/hawtio-next/actions/workflows/lint.yml/badge.svg)](https://github.com/hawtio/hawtio-next/actions/workflows/lint.yml)

A Hawtio reimplementation based on TypeScript + React.
This project reimplements the following Hawtio JS components in one project:

- [hawtio-core](https://github.com/hawtio/hawtio-core)
- [hawtio-integration](https://github.com/hawtio/hawtio-integration)
- [hawtio-oauth](https://github.com/hawtio/hawtio-oauth)

## Development

This project was generated with [Create React App](https://create-react-app.dev/) and is managed through [CRACO](https://craco.js.org/) for customised Webpack configurations.

See also [Developing Hawtio.next](./docs/developing.md) for the project styling, guidelines, and more details on development.

### Prerequisites

You need to have installed the following tools before developing the project.

- [Node.js >= 16](https://nodejs.org/en/)
- [Yarn v3](https://yarnpkg.com/getting-started/install)

#### Minimum Version of Yarn is 3.4.1

The default installation version of yarn on many operating systems is _1.22-19_ (the classic version). This causes a problem as the development app downloads the `@hawtio/react` package rather than using
the project directory. As a result, the mandated minimum version has been set to _3.4.1_.

If `yarn install` is attempted with a version lower than _3.4.1_ then an error message is displayed, eg.

> $ /usr/bin/yarn install
> yarn install v1.22.19
> [1/5] Validating package.json...
> error @hawtio/next-root@0.0.0: The engine "yarn" is incompatible with this module. Expected version ">=3.4.1". Got "1.22.19"
> error Found incompatible module.

To upgrade such a version to 3.4.1, use yarn's own `set-version` command:

> yarn set version 3.4.1

This will download the 3.4.1 internals to `hawtio-next/.yarn` which are then deferred to by the installed yarn binary.

### Developing

After checking out the project, run the following command to set up the project dependencies.

```console
yarn install
```

To develop the project, run the following command and then open <http://localhost:3000/> in the browser.

```console
yarn start
```

Then run another Java application which has a Jolokia endpoint from a different terminal, and connect to it from the Connect tab in the Hawtio console. For example, you can run [this Spring Boot example](https://github.com/hawtio/hawtio/tree/main/examples/springboot) in background.

```console
mvn spring-boot:run
```

You can connect to this example application at the Jolokia URL: <http://localhost:10001/actuator/hawtio/jolokia>. Open the Connect tab and create a connection with the following parameters:

- Name: \<any name\>
- Scheme: HTTP
- Host: `localhost`
- Port: `10001`
- Path: `/actuator/hawtio/jolokia`

### Building

To build the project for production, run the following command. It's built into the `build/` directory.

```console
yarn build
```

### Testing

To execute the unit tests, run the following command.

```console
yarn test
```

### Linting

It is recommended to run linting against the code periodically with the following command.

```console
yarn lint
```

### Contributing

When making a PR E2E tests from hawtio/hawtio will be run against your frontend.
If you want to specify a different branch to run the tests from you can add
`` `branch: [remote:]branch` `` to the PR description, ie. `branch: e2e-test-fix` or in your own fork `branch: jdoe:my-branch`.