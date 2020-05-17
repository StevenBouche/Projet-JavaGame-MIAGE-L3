![](https://i.ibb.co/Vp6LF3V/dice-forge-jeu-de-societe.jpg)

# diceforge 2019-2020 TP1-2 DFA

- BOUCHE Steven
- CHAPOULIE Dorian
- KAROUIA Alaedine
- GARNIER Corentin
- LONGIN Remi

## Run
Dans le dossier DiceForge:

- mvn clean install
- mvn exec:java
- mvn test

Avec argument pour mvn exec:java -Dexec.args="arg1 arg2 etc"

- sans args : lance une partie de DiceForge en mode verbose par default et bot par defaut
- -p NBGAME : pour lancer un nombre de partie (exemple -p 3 lance 3 parties) mode verbose multi-partie par default
- -bot NBRANDOM NBSIMPLE NBSTAT : il faut que la somme fasse 4 joueurs sinon le programme ne s'éxecute pas 
- on peut combiner les commandes au dessus

## Conception

- /Conception

## Couverture des tests

- Accessible dans /coverage

Test serveur plus orienter fonctionnel, test share orienter test unitaire. 

- 94% Package Server
- 73% Package Share sans l'aide du serveur
- 84% Package Share avec l'aide du serveur


## Itération 7 

Dans cette dernière itération, nous avons à présent un jeu totalement fonctionnel avec 4 joueurs, toutes les cartes basique du jeu et leurs effets...
Nous avons également ajouté plusieurs bots/IA dont un simple et un plus avancé grâce à une analyse poussée.
En effet, nous avons analysé sur plusieurs parties (centaines voir milliers) les différents choix permettant d'augmenter les chances de victoire, qui nous ont alors aidés à réaliser des statistiques. Ensuite nous avons simplifié et amélioré le code dans certaines parties. Nous avons également ajouté de la documentation afin de faciliter la compréhension du code et enfin nous avons fini une très grande partie des tests unitaires puis nous avons ajouté et amélioré plusieures parties dans notre conception pour le rendu de COO. Les statistiques ont était aussi implémenter et afficher sous forme de JSON car un gros volume de données. 

## Itération 6

Cette itération est principalement centrée sur l'implémentation des tests du package "share" et "server", ainsi qu'une amélioration de l'architecture principale pour facilité les actions des cartes. Nous avons également amélioré des logs de sortie, puis ajouté des nouvelles fonctionnalités pour les cartes. Toutes les issues de l'itération 6 n'ont pas pu etre réalisées, nous les terminerons alors lors de l"itération 7. 

## Itération 5

Toutes les fonctionnalités précedentes sont fonctionnelles. Une système de commande général dans le programme à était mis en place pour pouvoir enchainer des actions en fonction d'autres particulièrement pour les cartes. Certaines actions des cartes ont été implementées comme la carte Hammer ainsi que la carte permettant d'obtenir une faveur mineur. Chaque joueur peut, si il le souhaite a chaque gain d'argent, en déposer une partie dans le hammer. Les tests seront repoussés pour les prochaines itérations.  

## Itération 4

Toutes les fonctionnalités précedentes sont dorénavant fonctionnelles, de plus maintenant un joueur peut choisir si il souhaite effectuer une action de plus dans le tour, c'est a dire forger ou acheter une carte (basique ou d'extention en fonction). Enfin, le programme est maintenant doté du multipartie via les commandes ci-dessus. 

## Itération 3

Au cours d'une manche, les joueurs peuvent maintenant choisir entre la forge et le temple.Si ils choisissent la forge, ils peuvent acheter des faces si ils ont suffisament de ressources parmis les différents "bassins" et forger ces faces sur leurs dès. Si ils choisissent le temple, ils peuvent acheter des cartes sans oublier de vérifier si ils ont suffisament de ressources. De plus, si un joueur se déconnecte en milieu de partie, celle-ci est arrêtée. Enfin, à la fin de la partie, un classement des joueurs est établi afin de connaitre le vainqueur.

## Itération 2

Les joueurs ont maintenant accès à la forge et choisissent (aléatoirement) une face dans les différents bassins sans oublier de prendre en compte si ils ont suffisament de ressources dans leur inventaire. A chaque tour, ils peuvent choisir si ils souhaitent forger une face de leurs dés ou non.De plus, les nouvelles faces de type hybride sont maintenant prises en compte et notamment celles où les joueurs peuvent faire un choix entre différentes ressources.

## Itération 1 

La partie démarre quand 4 joueurs sont connectés au serveur. La partie est composée de 9 manches. Chaque joueur possède un inventaire qui leur est propre et qui est composé de golds, points de gloire, fragments lunaire et fragments solaire. Chaque manche, les joueurs lancent leurs deux dès(faveur majeur)qui sont composés uniquement de faces simples (gain de ressources) et leur inventaire est mis à jour.
De plus, chaque joueur peut recevoir une faveur majeur par manche. Enfin, quand la partie est terminée(à la fin de la 9 ème manche), les joueurs se deconnectent du serveur.
