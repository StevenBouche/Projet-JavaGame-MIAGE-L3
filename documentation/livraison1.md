# Livraison 1

Dans cette itération, notre livraison permet au client de:

- Lancer une partie
- Durant la partie, 4 joueurs se connecte
- Le serveur lance leurs dés
- Leurs inventaires sont mit à jours
- A la fin de la partie, le serveur se coupe, et les joueurs se déconnectent

Nous avons eu certaines difficultés, plus particulièrement au niveau des evénements, mais aussi au niveau du réseau mais aussi avec maven.
En lisant la documentation de SocketIO, de maven et en regardant des exemple de disign pattern Observer, nous avons réussi à fournir une livraison nous permettant de jouer une partie.

De plus nous n'avons pas eu le temps d'implémenter tous les Tests lié au code. Les tests non écrits sera un objectif de l'itération 2 en plus d'implémenter de nouvelles fonctionnalités décrit dans le fichier itération.

## Run
Dans le dossier DiceForge:

- mvn clean install
- mvn exec:java

