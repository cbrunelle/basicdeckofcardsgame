# Implemented REST Api 

|CMD | URI | PARAMS |
|----------|-------------|---------------------|
| GET       | /games 
| POST      | /games
| GET       | /games/{id}
| DELETE    | /games/{id}
| GET       | /games/{id}/players
| POST      | /games/{id}/players
| DELETE    | /games/{id}/players/{id}
| GET       | /games/{id}/deck
| POST      | /games/{id}/deck
| GET       | /games/{id}/deck | ?group-by=[suit or card]
| PUT       | /games/{id}/deck/shuffle
| PUT       | /games/{id}/deck/deal | ?player={id}
| GET       | /games/{id}/players/{id}/cards