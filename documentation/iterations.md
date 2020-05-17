# Détails des itérations du projet

# Iteration 1

- Joueurs peuvent lancer les 2 dès (faveur majeur)
- Des composés de 6 faces (faces simples)
- Les joueurs ont un inventaire
- Inventaire composé de ressources (gold , lunaire , solaire, gloire)
- La partie est composée de 9 manches
- 4 joueurs uniquement 
- Durant la manche le serveur lance les des 
- Les joueurs peuvent se connecter au serveur

La partie démarre quand 4 joueurs sont connectés au serveur et pas avant, chaque joueur peut recevoir une faveur majeur par manche, la partie est composée de 9 manches, les joueurs ont tous un inventaire propre avec des golds, fragments lunaire et fragments solaire. Tous les dés sont composés uniquement de 6 faces simples (uniquement un gain de ressources). Chaque manche, les joueurs lancent leurs dès(faveur majeur) et leur inventaire est mis à jour.Quand la partie sera terminée, les joueurs vont se deconnecter.

# Iteration 2

Lors de l'itération 2 nous allons implémenter de nouvelles notions. De plus finir les tests de l'itération 1 que nous avons pas terminer (dette technique). Le serveur pourra maintenant proposer au joueur de forger des faces y compris des faces hybrides. Pour cela nous devons implémenter un nouveau type de face ainsi que la forge. Ecrire le protocole réseau de discussion entre le serveur et le client pour l'interaction choisir ressource des faces ainsi que l'action de forger sur le serveur et le client. Nous ferons en plus a la fin les tests de l'itération 2. 

Le joueur au final recevera donc a chaque tour la faveur majeur puis choisira si il veut forger une face de son dé ou pas. Dans le nouveau cas où il tombe sur une face de dé hybrid il pourra choisir la ressource de son choix si il est nécessaire. 

De plus si un joueur se deconnecte en milieu de partie, la partie est arretée. 

# Iteration 3

Dans cette itération, nous allons maintenant implémenter une premiere version du temple qui sera au début constitué uniquement de cartes simples. Nous devrons alors implémenter les premieres cartes.Le serveur pourra proposer au joueur de choisir entre la forge et le temple.Cela impliquera d'écrire des nouveaux protocoles réseau de discussion entre le serveur et le client pour choisir l'intéraction choisir la forge ou le temple et choisir les cartes dans le temple.Enfin,nous allons continuer à ajouter de la documentation et nous devrons finir quelques tests qui n'ont pas pu etre fait dans les itérations précédentes ainsi que les tests de cette nouvelle itération. 

# Iteration 4

Nous avons maintenant notre temple constitué d'ilots qui sont constitués de cartes simples, c'est à dire qu'elles n'ont pas encore d'effet. Dans cette itération,nous devrons alors implémenter les différents effets liés aux cartes et les mettre en place dans le jeu.
Sachant qu'il y a beaucoup de cartes et plusieurs effets à prendre en compte, nous ne garantirons pas de tous les implémenter.

# Iteration 5

Si nous n'avons pas pu implémenter tous les effets des cartes dans la précédente itération, nous tâcherons de les finir dans celle-ci.
Nous allons également implémenter les faces spéciales pour les dès. Nous devrons écrire de nouveaux protocoles réseau de discussion entre le serveur et le client pour choisir différentes intéractions avec les différents effets des cartes et faces spéciales.


# Iteration 6

Dans cette itération, nous espérerons avoir finit d'implémenter tous les effets des cartes et faces spéciales et de les avoir bien pris en compte. Nous devrons alors commencer d'implémenter une stratégie chez les joueurs afin d'obtenir une certaine logique dans leur jeu et donc d'éviter certains choix aléatoires.


# Iteration 7

- test finaux 
- integrations

