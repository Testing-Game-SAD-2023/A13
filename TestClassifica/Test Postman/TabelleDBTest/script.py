print("Genera T23 \n")
for i in range(2, 250):
    print("INSERT INTO students VALUES({}, 'giocatore{}@email.com', 0, 0, NULL, NULL, NULL, NULL, NULL);".format(i, i), end='\n')

import random

print("Genera T4 \n")
for i in range(1, 100):
    playedGames = random.randint(1, 500)
    wonGames = random.randint(1, playedGames)
    print("INSERT INTO player_stats VALUES({}, {}, {}, {});".format(i, i, wonGames, playedGames), end='\n')
