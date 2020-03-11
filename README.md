# Basic Deck of Cards Game

## Requirements 
The application was developed with the following tools. No tests or explorations were done to validate other software versions.
* JAVA JDK 13.0.2
* Maven 3.6.3
* IntelliJ IDEA 2019.3.3

## Development Overview and Decisions 

My development has been mostly driven by creating a comprehensive testing environment. However, my lack of knowledge of the different tools (Spring, Spring Boot, Maven, Mockito and a few other software/library) took me a considerable
amount of time, and I needed to get up to speed with Java Language. I did a lot of research before choosing any of these
tools, always trying to find the best, most supported and most scaleable one. Nevertheless, I successfully extracted the requirements created an integration test file ( see src/test/java/com/demo/basicdeckofcards/BasicDeckOfCardsIntegrationTests.java ).
The primary purpose of this file is to stay as close as possible to the client's requirements and completely agnostic of the technological choices of the app. Consequently, this file should always remain untouched even if there are significant refactorings
or technology migration. 

On top of that, I used unit test files as a development tool mostly. Also, exception management is not thorough, but it's a good start. I am confident that this software can be a good start and will evolve easily.

Disclaimer: I am a fan of Robert C. Martin and his Clean Code philosophy.

Here are a few points that I find that improvements would be required (having time to do it) :

### Database Structure

I tried to implement a database structure; however, I fall short of succeeding. My limited knowledge of the tools and good practices created a time pit that I put aside. I tried to implement a JpaRepository with all the mocking tools 
(EntityManager and others ) but failed. I also explored the Reactive Programming without implementing it.

However, I did extract the persistency in a separate layer ( see src/main/java/com/demo/basicdeckofcards/GameRepository.java).
Implementing an Interface and replacing a DB managed layer should be relatively easy.

### Deck Creation and Linking

Following on my previous point, not having a comprehensive database structure confused me on how to link Decks to Games properly.
Consequently, I did not implement any Deck management. 

Moreover, two things made me doubt on how and where the requirement of deck creation would go: the requirement explicitly specifies that a deck cannot be removed from a game, and there are no requirements about deleting deck or viewing decks. To me, there is an "implicit" business rule that I am not aware of. It is all very uncleared for me, so I put aside the Deck management implementation.

### Spring Hateoas Integration

I found the Spring Hateoas module late in my development. Otherwise, I would have used it and improved the Hateoas aspect of the REST API. For now, my API is not utterly RESTful because it lacks most the Hateos aspect. I did start to implement some.
