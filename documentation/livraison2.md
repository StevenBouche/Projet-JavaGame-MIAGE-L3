# Livraison 2

Dans cette itération nous avons implémenté de nouvelles notion tel que : 

- L'ajout d'un nouveau type de face (les faces hybrides).
- L'ajout de la forge du jeu (Magasin permettant au joueur d'acheter des faces pour ses dés).
- Mis à jour et ajouts de protocole client serveur pour le choix des ressources sur les faces hybrides et aussi le choix d'effectuer la forge d'un dé.
- Nous avons ajouté aussi l'arrêt de la partie si un des joueurs se déconnecte.

La plus grande difficulté de cette itération est la serialisation des objets, et les envents réseau.

Nous avons ajouté des tests qu'il manquait a l'itération 1, mais nous avons toujours des tests manquant que nous allons écrire pendant l'itération suivante.
Cependant, lors d'une partie tous fonctionne comme il faut, les joueurs font un choix aléatoire pour le choix des ressources d'une face hybride

## Run
Dans le dossier DiceForge:

- mvn clean install
- mvn test
- mvn exec:java
