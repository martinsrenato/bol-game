# bol-game
A simple web-based [Mancala game](https://en.wikipedia.org/wiki/Mancala)

## How to Run
####Execute tests:
 `mvn clean install`

####Starting application:
 `mvn spring-boot:run`

Alternatively, a jar package can be obtained with: `mvn clean package` <br/>
and run with: `java -jar target/com.bol-bol-game.jar`

Either way, afterwards the game can be started at [http://localhost:8080](http://localhost:8080)

##Possible Improvements / New features
* Better Interface
* Database persistence, so games can be stopped and continued endlessly;
* Configurable number of stones and pits per player;
* Player profile and history, with data such as high-scores, number of victories, among others;
* Play versus CPU (Artificial Intelligence);
* Online play

##Stack
* Java
* SpringBoot
* EhCache
* Thymeleaf
* JUnit
* Mockito