# Implemented REST Api 

|CMD | URI | PARAMS |
|----------|-------------|---------------------|
| GET       | /games 
| POST      | /games
| GET       | /games/{name}
| DELETE    | /games/{name}
| GET       | /games/{name}/players
| POST      | /games/{name}/players
| DELETE    | /games/{name}/players/{name}
| POST      | /games/{name}/shoe
| GET       | /games/{name}/shoe                     | ?group-by=[suit or card]
| PUT       | /games/{name}/shoe/shuffle
| PUT       | /games/{name}/shoe/deal                | ?player={name}
| GET       | /games/{name}/players/{name}/cards