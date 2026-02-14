# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Phase 2 Sequence Diagrams
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+uB5bEaR3wUVR9YkahDY6fCvrOl2MAIEJ4oYoJwkEkSYCkm+hi7jS+4MkyU7KVyN4+XeS7CjAYoSm6MpymW7xKpgKrBhqbowGgEDMAAZr4TbDg6SbWZ23a9v2nkgdU-rMtMl7QEgABeKAcFGMZxoUWkFfAyCpjA6YAIwETmqh5vM0FFiW9Q+NVeq1Q1ux0blt75eJVBIhu7pbkl6owE0+h-DsRgQGoaAAOTMMcYAgPE25ectlVTfEM2Nc1KCxgp6HwsmXXYT1Tj9aMYyDcNBZjGN0ATfdj1zY2DFld5vIjvUh5yCgz7xOel7Xnli6VMuxprgGGNbrDH6PHC9TOeKGSqABmCWeVElgYR+nzGZMHkZeJnIazaHtZ9JRgDheEEYFNFkWMxmIaZhnzQxnjeH4-heCg6AxHEiRKyrzm+FgomCqB9QNNIEb8RG7QRt0PRyaoCnDBLSHvdppP+nb6C007VkditLp2UJ2tOb7p6uWo7nXYKWOjjAjJgKj6PwZLaBziF+U4+FkVPoT8iyvKLuFJtGqo5zaUZTA2WTQx4f017tk9n2ocdfrkoQ1A9VPdGL2tQ7HWiWmv0DfyQOjcWYMKk3LdQ-RwXw0thVV-UqOvjZYeLfSRgoNwx6XrHlHx4nU-Y0KyJHBANAEy+ROLx1lnkwHYBUzTdP1xVYGaU-mFfQLMC4fhowy4x8sBCidc-hsDig1PxNEMAADiSoNC61uqWBokDTYW3sEqW2HN46d0qFfY0GD7YPxnqtZAORoE5icmiIOJI65UmXhHKOMcc67wXAKMK9Q06nyvNoLOuC472zzhwwu6Uso5XLrQyuq0a6lQvvA10o9ZrPVevGXmlRu4-T+tmfu+ZB7jRHhRSGDYJ4V0fkVDhC9OxLyTivYhYBSFqAxEw-c95wqQKZJWBAUClQeiMctVaiDPHaE6HXEmsJ-TgJyHfBAgF3aV10mMVBOYUINBGPElAvULhrGSUqcI6SYAuDyZ0F+DM3780Ft-cYKTVCJMyXMNJGSUnZLWHklwBToZ-2Yv4DgAB2NwTgUBOBiBGYIcAuIADZ4ATkMLYmARR356yfgbVoHQUFoPupzLMKSABySpCmJmwe7eoOcMlxKVFsz40s3YhI9p5RGEzbEYjgLcpUlCQ6wxoZYuhTIGF4PQA40KKc2HinTmfTOsUc6JVVPnb5aAi7CLLpPZh4jvaSKCTPO6ejm7yLbootqr8u7vx7uogGmiRqFiHqWSa6Kx4GIWu8xFtl57n3MXDZhNyjwoDuZspUvzk4H3GWyjxcwvFiOMbPSOSoACS0gUUPEufUB5bLbERKiZcmJjMUmSpQgAVhGZ00IOyMJ4pKZ-IW-11XSC1TqvVrS5btMsGveymxVZIASGAO1fYICOoAFIQHFAKww-hkigDVDM-mcyGYLOZDJHoKT0G8PQFmbACBgB2qgHACA9koBrDNfqj2ODDkwHGIm5NlA00ZuBmLM1BZtW6uhNEnx3sABWPq0B3O9eKRVKBCTBw8hfN5e8PnR03ow+FjjWERUBaYrhoKoXguSgI+OMKS4iJHaFet1cSrSoblVSlmKWpvWUZ1I1fU+65i0aSnRFKaoYsatS0RtKRWrQZfIahMhaF+RsUqDEZruX71xuw2xMV5RmtnVtKZQil1wu8YQpFG7iaotLAAIRDM8hRHcD2qPTGUolp6SUgzJfUPQ64URdpyLeld09ParQAxtCF8B03QErGaGs4YcVMpkQGKsoYWOof3bivm3V0yZn+oDM9eGdEmiYzAWs9Zf6vNfe8ia2AtDog5f4+QP6WH-MlEp5GfqzFV3bDgttLann-kiRcr8qqXg5uKd1L+WZf42oVl4ZNTqXUuflIgYMsBgDYETYQPIBRplwPgwbI2JszYW2MFgmVX5agWeAmuxG3A8D2JfeHZL3nAwIDS0YrT0g15MkMCadx0Utx5d5QV9exW+x6fK8K-LhX0RuMnc+qDlGXTSpwV5vASqEsGvYyMGzhq7MmqG60oAA
