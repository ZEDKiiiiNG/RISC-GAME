# ece651-risc-game

![pipeline](https://gitlab.oit.duke.edu/yh254/ece651-risc-game/badges/master/pipeline.svg)
![coverage](https://gitlab.oit.duke.edu/yh254/ece651-risc-game/badges/master/coverage.svg?job=test)
This is the group project for Duke ECE 651.

## Coverage 

[Detailed coverage](https://yh254.pages.oit.duke.edu/ece651-risc-game/dashboard.html)

Game flow:
In this project, version1, we make a board game "RISK". This game can be divided into two parts: a server and a text-based client. 

At first, the server will ask in terminal how many players are in this game. Number of players can be set to 2-5. After committed how many players exactly, server will wait the connection of clients. After all players connect the sever, the game begin.

In client part, each player will be assigned several territories and several units. Each player can assign their units to any territory that belongs to them. Client will keep asking players where they want to put their units in, until all units have been assigned a territory. If one player finish the placement earlier, he should wait other players finish the placement. After the placement, the game can be divided into several turns. A turn is composed of issue orders, execute orders and receieve new units.

After all players finish the placement,  one turn begin. Players will be asked what actions they want to commit. which are move, attack and done. Move means the player can move the units from one territory to another one, as long as both territories are belong to him and the two territories have path to each other. After player type M, player will be asked to specify instruction in terminal in the following format: <sourceTerritoryId>,<destinationId>,<UnitType>,<amount>. Attack means one player can arrange several units in one territory to attack another territory that not belong to him. And the two territories must be adjacent to each other. Player need to enter instruction in the following format: <sourceTerritoryId>,<destinationId>,<UnitType>,<amount>. Done means the player has finished all his move and attack, and do not want to do any actions in this round. After all players have typed D(Done), issue orders part od a turn is finished. 

Execute orders will happen in server part. And the server will do all the move first, and then do all the attack parts. Then comes to receieve new units part, one new unit will appear in each territory. 

After one turn finished, each player will new pattern of their territories, and the actions all players did in this turn. And then the new turn begin. If one player lose all his  territories, the player is lost, he can choose to watch the game or exit. Once one player owns all territories, the player wins the game and server and client exit.
