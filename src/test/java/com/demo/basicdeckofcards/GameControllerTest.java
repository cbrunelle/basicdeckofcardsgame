package com.demo.basicdeckofcards;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GameService gameService;

    @Test
    public void getGames_shouldReturnGames() throws Exception {
        given(gameService.getList()).willReturn(List.of(new Game("test1"), new Game("test2")));

        mockMvc.perform(MockMvcRequestBuilders.get("/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.games").isArray())
                .andExpect(jsonPath("$.games", hasSize(2)))
                .andExpect(jsonPath("$.games[0].name").value("test1"))
                .andExpect(jsonPath("$.games[0].href").value("/games/test1"))
                .andExpect(jsonPath("$.games[1].name").value("test2"))
                .andExpect(jsonPath("$.games[1].href").value("/games/test2"));
    }

    @Test
    public void getGameID_shouldReturnCorrespondingGame() throws Exception {
        given(gameService.getGameDetails("test1")).willReturn(new Game("test1"));

        mockMvc.perform(MockMvcRequestBuilders.get("/games/test1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("test1"))
                .andExpect(jsonPath("href").value("/games/test1"));
    }

    @Test
    public void postGames_shouldCreateCorrespondingGame() throws Exception {
        doAnswer(returnsFirstArg()).when(gameService).create(ArgumentMatchers.any(Game.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/games").contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\" : \"test1\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("test1"))
                .andExpect(jsonPath("href").value("/games/test1"));

        Mockito.verify(gameService).create(ArgumentMatchers.any(Game.class));
    }

    @Test
    public void deleteGames_shouldDeleteCorrespondingGame() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/games/testDeleteGame"))
                .andExpect(status().isOk());

        Mockito.verify(gameService).deleteByName("testDeleteGame");
    }

    @Test
    public void getPlayers_shouldReturnPlayers() throws Exception {
        Game game = new Game("testPlayers");
        game.addPlayer(new Player("Joe"));
        game.addPlayer(new Player("Amanda"));
        given(gameService.getGameDetails("testPlayers")).willReturn(game);

        mockMvc.perform(MockMvcRequestBuilders.get("/games/testPlayers/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.players").isArray())
                .andExpect(jsonPath("$.players", hasSize(2)))
                .andExpect(jsonPath("$.players[0].name").value("Joe"))
                .andExpect(jsonPath("$.players[1].name").value("Amanda"));
    }

    @Test
    public void getPlayers_shouldReturnPlayersSorted() throws Exception {
        Game game = new Game("testPlayersSorted");
        game.addPlayer(new Player("Joe"));
        game.addPlayer(new Player("Amanda"));
        game.getShoe().addDeck(new Deck());
        game.getShoe().deal(game.getPlayer("Amanda"),1);
        game.getShoe().deal(game.getPlayer("Joe"),1);
        given(gameService.getGameDetails("testPlayersSorted")).willReturn(game);

        mockMvc.perform(MockMvcRequestBuilders.get("/games/testPlayersSorted/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.players").isArray())
                .andExpect(jsonPath("$.players", hasSize(2)))
                .andExpect(jsonPath("$.players[0].name").value("Amanda"))
                .andExpect(jsonPath("$.players[0].total").value("13"))
                .andExpect(jsonPath("$.players[1].name").value("Joe"))
                .andExpect(jsonPath("$.players[1].total").value("12"));
    }

    @Test
    public void postPlayers_shouldAddAndReturnPlayers() throws Exception {
        Game game = new Game("testPlayers");
        given(gameService.getGameDetails("testPlayers")).willReturn(game);

        mockMvc.perform(MockMvcRequestBuilders.post("/games/testPlayers/players").contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\" : \"Joe\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.players").isArray())
                .andExpect(jsonPath("$.players", hasSize(1)))
                .andExpect(jsonPath("$.players[0].name").value("Joe"));
    }

    @Test
    public void deletePlayers_shouldDeleteReturnPlayers() throws Exception {
        Game game = new Game("testPlayers");
        game.addPlayer(new Player("Joe"));
        given(gameService.getGameDetails("testPlayers")).willReturn(game);

        mockMvc.perform(MockMvcRequestBuilders.delete("/games/testPlayers/players/Joe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.players").isArray())
                .andExpect(jsonPath("$.players", hasSize(0)));
    }

    @Test
    public void getPlayer_shouldReturnPlayer() throws Exception {
        Game game = new Game("testPlayers");
        game.addPlayer(new Player("Joe"));
        given(gameService.getGameDetails("testPlayers")).willReturn(game);

        mockMvc.perform(MockMvcRequestBuilders.get("/games/testPlayers/players/Joe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Joe"))
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.cards").isArray())
                .andExpect(jsonPath("$.cards", hasSize(0)));
    }

    @Test
    public void getDeck_shouldReturnDeck() throws Exception {
        Game game = new Game("testDeck");
        game.getShoe().addDeck(new Deck());
        given(gameService.getGameDetails("testDeck")).willReturn(game);

        mockMvc.perform(MockMvcRequestBuilders.get("/games/testDeck/shoe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remaining").value(52))
                .andExpect(jsonPath("$.game").value("testDeck"));
    }

    @Test
    public void deckDeal_shouldReturnPlayer() throws Exception {
        Game game = new Game("testDeal");
        game.addPlayer(new Player("Joe"));
        game.getShoe().addDeck(new Deck());
        given(gameService.getGameDetails("testDeal")).willReturn(game);

        mockMvc.perform(MockMvcRequestBuilders.put("/games/testDeal/shoe/deal").param("player", "Joe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Joe"))
                .andExpect(jsonPath("$.total").value(13))
                .andExpect(jsonPath("$.cards").isArray())
                .andExpect(jsonPath("$.cards", hasSize(1)))
                .andExpect(jsonPath("$.cards[0].suit").value("hearts"))
                .andExpect(jsonPath("$.cards[0].value").value("King"));

        mockMvc.perform(MockMvcRequestBuilders.get("/games/testDeal/shoe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remaining").value(51))
                .andExpect(jsonPath("$.game").value("testDeal"));
    }

    @Test
    public void getDeck_shouldReturnDeckGroupedBySuit() throws Exception {
        Game game = new Game("testDeck");
        game.getShoe().addDeck(new Deck());
        given(gameService.getGameDetails("testDeck")).willReturn(game);

        mockMvc.perform(MockMvcRequestBuilders.get("/games/testDeck/shoe").param("group-by", "SUIT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remaining").value(52))
                .andExpect(jsonPath("$.game").value("testDeck"))
                .andExpect(jsonPath("$.cards.hearts").exists())
                .andExpect(jsonPath("$.cards.hearts").value(13))
                .andExpect(jsonPath("$.cards.spades").exists())
                .andExpect(jsonPath("$.cards.spades").value(13))
                .andExpect(jsonPath("$.cards.clubs").exists())
                .andExpect(jsonPath("$.cards.clubs").value(13))
                .andExpect(jsonPath("$.cards.diamonds").exists())
                .andExpect(jsonPath("$.cards.diamonds").value(13));
    }

    @Test
    public void getDeck_shouldReturnDeckGroupedByCard() throws Exception {
        Game game = new Game("testDeck");
        game.getShoe().addDeck(new Deck());
        given(gameService.getGameDetails("testDeck")).willReturn(game);

        mockMvc.perform(MockMvcRequestBuilders.get("/games/testDeck/shoe").param("group-by", "CARD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remaining").value(52))
                .andExpect(jsonPath("$.game").value("testDeck"))
                .andExpect(jsonPath("$.cards.hearts").exists())
                .andExpect(jsonPath("$.cards.hearts.King").value(1))
                .andExpect(jsonPath("$.cards.hearts.*", hasSize(13)))
                .andExpect(jsonPath("$.cards.spades").exists())
                .andExpect(jsonPath("$.cards.spades.*", hasSize(13)))
                .andExpect(jsonPath("$.cards.clubs").exists())
                .andExpect(jsonPath("$.cards.clubs.*", hasSize(13)))
                .andExpect(jsonPath("$.cards.diamonds").exists())
                .andExpect(jsonPath("$.cards.diamonds.*", hasSize(13)));
    }
}
