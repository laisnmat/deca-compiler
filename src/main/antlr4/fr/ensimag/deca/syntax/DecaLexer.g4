lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// reserved words
ASM: 'asm';
CLASS: 'class';
EXTENDS: 'extends';
ELSE: 'else';
FALSE: 'false';
IF: 'if';
INSTANCEOF: 'instanceof';
NEW: 'new';
NULL: 'null';
READINT: 'readInt';
READFLOAT: 'readFloat';
PRINT: 'print';
PRINTLN: 'println';
PRINTX: 'printx';
PRINTLNX: 'printlnx';
PROTECTED: 'protected';
RETURN: 'return';
THIS: 'this';
TRUE: 'true';
WHILE: 'while';

// special characters
LT: '<';
GT: '>';
EQUALS: '=';
PLUS: '+';
MINUS: '-';
TIMES: '*';
SLASH: '/';
PERCENT: '%';
DOT: '.';
COMMA: ',';
OPARENT: '(';
CPARENT: ')';
OBRACE: '{';
CBRACE: '}';
EXCLAM: '!';
SEMI: ';';
EQEQ: '==';
NEQ: '!=';
GEQ: '>=';
LEQ: '<=';
AND: '&&';
OR: '||';

// spaces
SPACES: (' ' | '\t' | '\r' | '\n') { skip(); };

// Integer literals
INT: '0' | POSITIVE_DIGIT DIGIT*;
POSITIVE_DIGIT: [1-9];

// Identifiers
IDENT: (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*;
LETTER: [a-zA-Z];
DIGIT: [0-9];

// Floating point literals
FLOAT: (FLOATDEC | FLOATHEX);
FLOATHEX: ('0x' | '0X') NUMHEX '.' NUMHEX ('p' | 'P') SIGN? NUM ('F' | 'f')?;
NUMHEX: DIGITHEX+;
DIGITHEX: [0-9A-Fa-f];
FLOATDEC: (DEC | DEC EXP)('F' | 'f')?;
DEC: NUM '.' NUM;
EXP: ('e' | 'E') SIGN? NUM;
SIGN: ('+' | '-');
NUM: DIGIT+;

// Strings
EOL: '\n' | '\r' | '\r\n';
STRING_CAR: ~["\n\r];
STRING: '"' (STRING_CAR | '\\' | '\\\\')* '"';
MULTI_LINE_STRING: '"' (STRING_CAR | EOL | '\\' | '\\\\')* '"';

// Comments
COMMENT: '//' ~[\r\n]* { skip(); };
MULTI_LINE_COMMENT: '/*' .*? '*/' { skip(); };

// File inclusion
INCLUDE: '#include' (' ')* '"' (LETTER | DIGIT | DOT | MINUS | '_')+ '"';
