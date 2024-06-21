# Répertoire de débogage pour la soutenance

Ces fichiers ont été établis avec le commit marqué par le tag rendu-FINAL.
Le but est de corriger le maximum de problèmes rapportés ici (il peut y avoir plusieurs problèmes par fichier). Si le problème est déjà corrigé dans votre dernière version, il compte comme corrigé ! A vous d'en être sûr cependant.

## Liens symboliques

Ce sont (une partie de) vos fichiers de tests mal classés. Ils correspondent à une mauvaise interprétation de la spécification. Par exemple: l'absence de return sur un chemin d'exécution d'une méthode n'est pas une erreur contextuelle, mais une erreur à l'exécution (relisez bien [SyntaxeContextuelle] et [Semantique]).

Faut éventuellement fixer le bug correspondant dans votre compilo puis déplacer les fichiers de tests originaux au bon endroit (et effacer le lien symbolique mort).

## Fichier `test*.deca`

Des fichiers qui ne se comportent pas comme attendu avec **votre** compilateur.
A vous de trouver le comportement attendu (parfois des commentaires dans le fichier peuvent aider), 
puis de trouver et corriger le(s) problème(s) dans **votre** compilateur.

