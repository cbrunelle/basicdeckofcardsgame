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
    public void canAddDeckToGame() throws Exception {
        JSONObject expected = new JSONObject("{ remaining : 52 }");

        this.webTestClient.post().uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"testAddDeck\"}"))
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.put().uri("/games/testAddDeck/shoe/add").exchange()
                .expectStatus().isOk();
    }

    @Test
    public void canAddPlayerToGame() throws Exception {
        JSONObject expectedEmpty = new JSONObject("{ players : [] }");
        JSONObject expectedPlayer = new JSONObject("{ players : [ {name : 'joe' }] }");

        this.webTestClient.post().uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"testAddPlayer\"}"))
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.get().uri("/games/testAddPlayer/players").exchange()
                .expectStatus().isOk()
                .expectBody().json(expectedEmpty.toString());

        this.webTestClient.post().uri("/games/testAddPlayer/players")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"joe\"}"))
                .exchange()
                .expectStatus().isOk();

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
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.post().uri("/games/testRemovePlayer/players")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"joe\"}"))
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.post().uri("/games/testRemovePlayer/players")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"lydia\"}"))
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.delete().uri("/games/testRemovePlayer/players/lydia")
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.get().uri("/games/testRemovePlayer/players").exchange()
                .expectStatus().isOk()
                .expectBody().json(expected.toString());
    }

    @Test
    public void canDealCardsToPlayer() throws Exception {
        JSONObject expected = new JSONObject("{ name : 'joe', cards: [ { suit : 'hearts' , value: 'King' } ], total: 13}");
        this.webTestClient.post().uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"testDealCards\"}"))
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.post().uri("/games/testDealCards/players")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"joe\"}"))
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.put().uri("/games/testDealCards/shoe/add").exchange()
                .expectStatus().isOk();

        this.webTestClient.put().uri("/games/testDealCards/shoe/deal?player=joe")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(expected.toString());
    }

    @Test
    public void canListCardsOfPlayer() throws Exception {
        JSONObject expected = new JSONObject("{ name : 'joe', cards: [], total: 0}");
        this.webTestClient.post().uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"testListCards\"}"))
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.post().uri("/games/testListCards/players")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"joe\"}"))
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.get().uri("/games/testListCards/players/joe").exchange()
                .expectStatus().isOk()
                .expectBody().json(expected.toString());
    }

    @Test
    public void canListPlayersSortedWithHandValue() throws Exception {
        JSONObject expected = new JSONObject("{players:[{total:13,name:'peter'},{'total':12,'name':'joe'}]}");
        this.webTestClient.post().uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"testListPlayers\"}"))
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.post().uri("/games/testListPlayers/players")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"joe\"}"))
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.post().uri("/games/testListPlayers/players")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"peter\"}"))
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.put().uri("/games/testListPlayers/shoe/add")
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.put().uri("/games/testListPlayers/shoe/deal?player=peter")
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.put().uri("/games/testListPlayers/shoe/deal?player=joe")
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.get().uri("/games/testListPlayers/players")
                .exchange()
                .expectBody().json(expected.toString());
    }

    @Test
    public void canGetCardCountPerSuit() throws Exception {
        JSONObject expected = new JSONObject("{ game : testCardCountPerSuit , cards :{ hearts :13, spades :13, clubs :13, diamonds :13}}");
        this.webTestClient.post().uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"testCardCountPerSuit\"}"))
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.put().uri("/games/testCardCountPerSuit/shoe/add").exchange()
                .expectStatus().isOk();

        this.webTestClient.get().uri("/games/testCardCountPerSuit/shoe?group-by=SUIT")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(expected.toString());
    }

    @Test
    public void canGetCardCountPerCard() throws Exception {
        JSONObject expected = new JSONObject("{  game : testCardCountPerCard, cards :{ " +
                "hearts :{ King :1, Queen :1, Jack :1, \"10\":1, \"9\":1, \"8\":1, \"7\":1, \"6\":1, \"5\":1, \"4\":1, \"3\":1, \"2\":1, Ace :1  }, " +
                "spades :{ King :1, Queen :1, Jack :1, \"10\":1, \"9\":1, \"8\":1, \"7\":1, \"6\":1, \"5\":1, \"4\":1, \"3\":1, \"2\":1, Ace :1  }, " +
                "clubs :{ King :1, Queen :1, Jack :1, \"10\":1, \"9\":1, \"8\":1, \"7\":1, \"6\":1, \"5\":1, \"4\":1, \"3\":1, \"2\":1, Ace :1  }, " +
                "diamonds :{ King :1, Queen :1, Jack :1, \"10\":1, \"9\":1, \"8\":1, \"7\":1, \"6\":1, \"5\":1, \"4\":1, \"3\":1, \"2\":1, Ace :1  }}}");

        this.webTestClient.post().uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"testCardCountPerCard\"}"))
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.put().uri("/games/testCardCountPerCard/shoe/add").exchange()
                .expectStatus().isOk();

        this.webTestClient.get().uri("/games/testCardCountPerCard/shoe?group-by=CARD")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(expected.toString());
    }

    @Test
    public void canShuffleShoe() throws Exception {
        this.webTestClient.post().uri("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{ \"name\": \"testShuffleShoe\"}"))
                .exchange()
                .expectStatus().isOk();

        this.webTestClient.put().uri("/games/testShuffleShoe/shoe/shuffle").exchange()
                .expectStatus().isOk();
    }
}
