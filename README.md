# CampusSignal

Application web Jakarta EE 10 pour le signalement et le suivi des problèmes du campus de l'Université de Fianarantsoa.

Le périmètre initial est volontairement limité à l'Université de Fianarantsoa. Le champ étudiant correspond à son école ou sa faculté, par exemple `Faculté des Sciences` ou `Faculté de Droit et de Sciences Politiques`.

## Parcours

- Étudiant : création et suivi de ses signalements.
- Responsable : tableau de bord, validation des comptes étudiants et gestion des signalements.
- Inscription étudiant : demande en attente jusqu'à validation par un responsable.
- Profil étudiant : nom, email, école/faculté et matricule.
- Statuts d'un signalement : `Signalé`, `Reçu`, `Résolu`.

## Lancer le projet

Le projet utilise Java 17, Maven et GlassFish 7.

```powershell
mvn clean package
C:\glassfish7\bin\asadmin.bat deploy --force=true --contextroot CampusSignal target\HelloJSF-1.0-SNAPSHOT.war
```

Application : `http://localhost:8080/CampusSignal/`

Comptes de démonstration :

- Étudiant : `student@campus.local` / `student123`
- Responsable : `admin@campus.local` / `admin123`

## Vérification

```powershell
mvn test
```

Les tests couvrent l'inscription en attente, la validation d'un compte, les doublons, la validation des signalements et leur changement de statut.

## Évolution prévue

Les données sont actuellement conservées en mémoire pour le prototype. La prochaine évolution de production est la migration des utilisateurs et signalements vers JPA avec une base de données, ainsi que le hachage des mots de passe.