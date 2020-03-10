package com.demo.basicdeckofcards;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BasicDeckOfCardsIntegrationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void canListGames() throws Exception {
        JSONObject expected = new JSONObject("{ games: [] }");

        this.webTestClient.get().uri("/games").exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.games").isArray();
    }

    @Test
    public void canCreateGame() throws Exception {
        JSONObject expectedPostResponse = new JSONObject("{ name: 'testCreateGame', href: '/games/testCreateGame'}");

        this.webTestClient.post().uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"testCreateGame\"}"))
                .exchange()
                .expectBody().json(expectedPostResponse.toString());

        this.webTestClient.get().uri("/games").exchange()
                .expectStatus().isOk().expectBody()
                .jsonPath("$.games[?(@.name == 'testCreateGame')].name").hasJsonPath()
                .jsonPath("$.games[?(@.href == '/games/testCreateGame')].href").hasJsonPath();
    }


    @Test
    public void canDeleteGame() throws Exception {
        JSONObject expectedPostResponse = new JSONObject("{ name: 'testDeleteGame', href: '/games/testDeleteGame'}");

        this.webTestClient.post().uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"testDeleteGame\"}"))
                .exchange()
                .expectBody().json(expectedPostResponse.toString());

        this.webTestClient.delete().uri("/games/testDeleteGame").exchange()
                .expectStatus().isOk().expectBody()
                .jsonPath("$.games[?(@.name == 'testDeleteGame')].name").doesNotHaveJsonPath();
    }

    @Test
    public void canCreateDeck() throws Exception {
        JSONObject expectedPostResponse = new JSONObject("{ name: 'testCreateDeck', href: '/games/testCreateDeck'}");

        this.webTestClient.post().uri("/decks")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"testCreateDeck\"}"))
                .exchange()
                .expectBody().json(expectedPostResponse.toString());
    }

    @Test
    public void canAddDeckToGame() throws Exception {
        JSONObject expected = new JSONObject("{ remaining : 52 }");

        this.webTestClient.post().uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"testAddDeck\"}"))
                .exchange();

        this.webTestClient.get().uri("/games/testAddDeck/deck/add").exchange()
                .expectStatus().isOk();
    }

    @Test
    public void canAddPlayerToGame() throws Exception {
        JSONObject expectedEmpty = new JSONObject("{ players : [] }");
        JSONObject expectedPlayer = new JSONObject("{ players : [ {name : 'joe' }] }");

        this.webTestClient.post().uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"testAddPlayer\"}"))
                .exchange();

        this.webTestClient.get().uri("/games/testAddPlayer/players").exchange()
                .expectStatus().isOk()
                .expectBody().json(expectedEmpty.toString());

        this.webTestClient.post().uri("/games/testAddPlayer/players")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"joe\"}"))
                .exchange();

        this.webTestClient.get().uri("/games/testAddPlayer/players").exchange()
                .expectStatus().isOk()
                .expectBody().json(expectedPlayer.toString());
    }

    @Test
    public void canRemovePlayerToGame() throws Exception {
        JSONObject expected = new JSONObject("{ players : [{name : 'joe' }] }");

        this.webTestClient.post().uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"testRemovePlayer\"}"))
                .exchange();

        this.webTestClient.post().uri("/games/testRemovePlayer/players")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"joe\"}"))
                .exchange();

        this.webTestClient.post().uri("/games/testRemovePlayer/players")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"lydia\"}"))
                .exchange();

        this.webTestClient.delete().uri("/games/testRemovePlayer/players/lydia")
                .exchange();

        this.webTestClient.get().uri("/games/testRemovePlayer/players").exchange()
                .expectStatus().isOk()
                .expectBody().json(expected.toString());
    }

    @Test
    public void canListCardsOfPlayer() throws Exception {
        JSONObject expected = new JSONObject("{ name : 'joe', cards: [], total: 0}");
        this.webTestClient.post().uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"testListCards\"}"))
                .exchange();

        this.webTestClient.post().uri("/games/testListCards/players")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"joe\"}"))
                .exchange();

        this.webTestClient.get().uri("/games/testListCards/players/joe").exchange()
                .expectStatus().isOk()
                .expectBody().json(expected.toString());
    }

    @Test
    public void canDealCardsToPlayer() throws Exception {
        JSONObject expected = new JSONObject("{ name : 'joe', cards: [ { suit : 'hearts' , value: 'ACE' } ], total: 1}");
        this.webTestClient.post().uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"testDealCards\"}"))
                .exchange();

        this.webTestClient.post().uri("/games/testDealCards/players")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"joe\"}"))
                .exchange();

        this.webTestClient.put().uri("/games/testDealCards/deck/deal?player=joe")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"joe\"}"))
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(expected.toString());
    }
}
