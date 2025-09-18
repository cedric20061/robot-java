# 📘 Simulateur de Robot d’Intervention

## 🎯 Contexte du projet

Ce projet est développé dans le cadre d’un cours d’informatique en **Java** à l’**ESIGELEC**.

La société **Novel** conçoit et développe des robots d’intervention utilisés à l’international. Suite à un appel d’offre remporté auprès de la **CIP (Chemical Industry of Paris)**, l’équipe **R\&D** dirigée par **M. Jamel** doit réaliser un **simulateur de robot mobile** capable de :

* Explorer un environnement (sans obstacle) ;
* Détecter et compter des victimes ;
* Déterminer la position de la victime dans l’état le plus critique ;
* Afficher les caractéristiques de cette victime ;
* Fournir les messages d’information en **4 langues** (français, anglais, + 2 autres proposées : espagnol et allemand).

Le simulateur doit permettre de valider l’**algorithme de déplacement du robot** et préparer une intégration directe sur le futur robot réel.

---

## 🛠️ Fonctionnalités principales

- **Choix de la langue au démarrage** : FR 🇫🇷 | EN 🇬🇧 | ES 🇪🇸 | DE 🇩🇪
- **Exploration du terrain** (ligne par ligne ou jusqu’à détection de la dernière victime)
- **Détection des victimes** :

  - Comptage du nombre de victimes
  - Calcul de la gravité moyenne
  - Identification de la victime la plus critique (gravité maximale)

- **Affichages** :

  - Dans la console ou dans une boîte de dialogue
  - Message d’encouragement à la victime (selon la langue choisie)

- **Statistiques** :

  - Variante 1 : Gravité moyenne des victimes
  - Variante 2 : Pourcentage du terrain non exploré

## 🚀 Variantes de simulation

### 🔹 Variante 1 – Difficulté \*

- Le robot parcourt **tout le terrain**
- Affiche :

  - Nombre de victimes
  - Gravité moyenne
  - Victime la plus critique et ses caractéristiques

### 🔹 Variante 2 – Difficulté \*\*

- Saisie du nombre de victimes (entre **1 et 6**)
- Le robot **interrompt son exploration** dès que la dernière victime est trouvée
- Affiche :

  - Victime la plus critique
  - Message d’encouragement dans la langue choisie (fenêtre dédiée)
  - Pourcentage de terrain **non exploré**

---

---

## 🖥️ Prérequis techniques

- **Langage** : Java 17+
- **IDE recommandé** : Eclipse
- **Librairies utilisées** :

  - Swing (pour les boîtes de dialogue)
  - Collections Java (List, ArrayList, etc.)

---

## ▶️ Exécution

1. Cloner le projet :

   ```bash
   git clone https://github.com/cedric20061/robot-java.git
   cd robot-java
   ```

2. Compiler le projet :

   ```bash
   javac -d bin -encoding ISO-8859-1 src/*.java
   ```

3. Lancer la simulation :

   ```bash
   java -cp bin SimulateurAppX
   ```

---

## 👥 Auteurs

- Projet réalisé par : _Cédric_
- Encadrant : _TEBOUL_
- Dans le cadre du cours d’informatique à **ESIGELEC**

---
