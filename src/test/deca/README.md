# Tests du compilateur Decac

Les tests sont organisés en 3 répertoires "syntax", "context" et "codegen", correspondant aux différentes passes du compilateur.

Chaque répertoire contient un répertoire "valid", contenant les tests devant passer sans lever d'erreur.

Les répertoires "syntax" et "context" possèdent un répertoire "invalid", dont les tests contiennent des programmes qui doivent être réfusés par le compilateur.

## Exécution des tests

Pour chaque incrément, on prévoit un script de test :
- Incrément Initial: `renduInitial.sh`
- Incrément 1: `renduInterm1.sh`

Qui utilisent des scripts de test plus généraux :
- `test_syntax_repo.sh [rep] [-r]`: exécute test_syntax sur chacun des tests présent récursivement dans `syntax/(valid|invalid)/[rep]/`.
- `test_context_repo.sh [rep] [-r]`: exécute test_context sur chacun des tests présent récursivement dans `context/(valid|invalid)/[rep]/`.
- `test_codegen_repo.sh [rep] [-r]`: exécute test_syntax sur chacun des tests présent récursivement dans `codegen/valid/[rep]/`.

L'option -r permet de choisir s'il faut recompiler le projet à chaque fois.

Le script `clean.sh` permet de supprimer tous les fichiers générés par les tests, d'extension `res`, `ass` et `debug`.

## Convention de nommage des tests

Pour un incrément donné `monIncrement`, chaque répertoire "valid" ou "invalid" peut contenir un répertoire de la forme `<id>.monIncrement`, où l'identifiant est unique et propre à l'incrément.

Pour chaque incrément on trouve des répertoires et des fichiers de formes:
- `<i>.<feature>`
- `<i>.<feature>.deca`

Si plusieurs fichiers concernent une même fonctionnalité, on les regroupe sous un répertoire.

Chaque nom de fichier ou fonctionnalité est de préférence en camelCase.
Chaque identifiant est unique pour chaque répertoire, et permet d'organiser les tests.

### Fichiers requis pour un test `monTest` dans "syntax" et "context"

- `valid`:
  - uniquement un fichier `<id>.monTest.deca`
- `invalid`:
  - un fichier `<id>.monTest.deca`
  - un fichier `<id>.monTest.expected`

Les fichiers "expected" doivent contenir l'endroit où l'erreur sera détectée au format `<nom_fichier>:<ligne>`.

### Fichiers requis pour un test `monTest` dans "codegen"
- `valid`:
  - un fichier `<id>.monTest.deca`
  - un fichier `<id>.monTest.expected` par `<id>.monTest.deca`
  - un fichier `<id>.monTest.input` contenant les entrées à transmettre aux fonctions d'entrée dans le programme. Une donnée par ligne, dans l'ordre chronologique des appels.

### Exemples
---
#### Fichiers et dossiers de tests de "context": "invalid" et "valid".
```
src/test/deca/context/invalid/01.renduInterm1/00.expr
├── 00.equals
│   ├── 00.equalsBoolInt.deca
│   ├── 00.equalsBoolInt.expected
│   ├── 01.equalsBoolFloat.deca
│   [...]
├── 01.leq
│   ├── 00.leqBool.deca
│   ├── 00.leqBool.expected
│   ├── 01.leqBoolInt.deca
│   [...]
├── 02.less
[...]

src/test/deca/context/valid/01.renduInterm1/00.expr
├── 00.equals.deca
├── 01.leq.deca
├── 02.less.deca
├── 03.or.deca
├── 04.and.deca
└── 05.not.deca
```
---
#### Fichiers et dossiers de tests de "codegen".
```
src/test/deca/codegen/valid/01.renduInterm1/
├── 00.devinetteDicho.deca
├── 00.devinetteDicho.expected
├── 00.devinetteDicho.input
├── 00.expr
│   ├── 00.equals.deca
│   ├── 00.equals.expected
│   ├── 01.leq.deca
│   [...]
├── 01.inst
│   ├── 00.while
│   │   ├── 00.while.deca
│   │   └── 00.while.expected
│   └── 01.ifthenelse
│       ├── 00.if.deca
│       [...]
├── 02.primaryExpr
│   ├── 00.readInt.deca
│   ├── 00.readInt.expected
│   └── 00.readInt.input
[...]
```
---