# Projet GL — Deca Compiler

> A full compiler for the **Deca** language, built in Java as part of the Software Engineering project at Ensimag (Group 03).

Deca is an object-oriented language inspired by Java, compiling down to **IMA assembly** (a custom educational instruction set). The compiler covers the full pipeline: lexing, parsing, contextual verification, and code generation.

---

## Requirements

- **Java 11+**
- **Maven 3.6+**
- Docker (optional, see `docker/`)

After cloning, initialize the submodules:

```sh
git submodule update --init --recursive
```

---

## Build

```sh
mvn package -DskipTests
```

This produces the `decac` binary in `src/main/bin/`.

---

## Usage

```sh
decac [options] <file.deca> ...
```

| Option | Description |
|---|---|
| `-b` | Print the team banner |
| `-p` | Parse only — outputs the decompiled syntax tree |
| `-v` | Verify only — stops after contextual checks |
| `-n` | No runtime checks (overflow, null dereference, etc.) |
| `-r X` | Limit registers to R0…R{X-1}, with 4 ≤ X ≤ 16 |
| `-d` | Enable debug output (repeat for more verbosity) |
| `-P` | Compile multiple files in parallel |

Compiled `.deca` files produce `.ass` assembly files, which can then be run with the `ima` interpreter:

```sh
decac myprogram.deca
ima myprogram.ass
```

---

## Deca language overview

Deca supports classes, inheritance, fields, methods, `instanceof`, `new`, `this`, `null`, and basic types (`int`, `float`, `boolean`). Example:

```deca
class Animal {
    int age;
    void speak() { println("..."); }
}

class Dog extends Animal {
    void speak() { println("Woof!"); }
}

{
    Dog d = new Dog();
    d.age = 3;
    d.speak();
}
```

---

## Project structure

```
src/main/java/fr/ensimag/
├── deca/
│   ├── DecacMain.java          # Entry point (CLI)
│   ├── DecacCompiler.java      # Compiler orchestration
│   ├── CompilerOptions.java    # CLI option parsing
│   ├── syntax/                 # ANTLR4 lexer & parser
│   ├── tree/                   # AST node classes
│   ├── context/                # Contextual verification (type checking)
│   └── codegen/                # IMA code generation
└── ima/pseudocode/             # IMA instruction set representation
```

The compiler follows a classic 3-phase pipeline: **parse → verify → codegen**, each implemented as a pass over the AST.

---

## Running tests

```sh
mvn test
```

Test sources are in `src/test/`. The `DEFENSE_TESTS/` folder contains additional regression tests used during the project defense.

---

## Docker

A Docker environment is provided for running the compiler in an isolated setup matching the school machines:

```sh
cd docker
./new_container
```

See `docker/README.md` for details.

---

## Documentation

The `docs/` folder contains:
- `Conception.pdf` — design document
- `Validation.pdf` — test & validation report
- `Manuel-Utilisateur.pdf` — user manual
- `Documentation_GIT.pdf` — Git workflow documentation

---

## Team — Group 03

Jérémy, Vincent, Aster, Martha, Valentin, Alexander, Matthew, Hugo.
