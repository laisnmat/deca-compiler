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

// Deca lexer rules.


//not in the grammar but seems to be needed in the parser
OBRACE:'{';
CBRACE:'}';
EOL: '\n' {skip();} ; 
SPACE: ' '{skip();} ; 
PRINTLN:'println';
SEMI:';';




//implemntation of the grammar

// LETTER: [a-zA-Z];
// DIGIT:[0-9];  
// IDENT: (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*;

// INT: '0' | POSITIVE_DIGIT DIGIT* ;
// POSITIVE_DIGIT: [1-9] ;

// FLOAT: FLOATDEC | FLOATHEX ;
// FLOATDEC:(DEC | DEC EXP) ('F' | 'f' | );
// EXP: ('E' | 'e') SIGN NUM;
// SIGN : '+' | '-' | ;
// DEC: NUM '.' NUM;
// NUM: DIGIT+;

// FLOATHEX:('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' | );
// NUMHEX:DIGITHEX+;
// DIGITHEX: [a-fA-F0-9]+;

// STRING: '"' (STRING_CAR | '\\"' | '\\\\')* '"';
// MULTI_LINE_STRING: '"' (STRING_CAR | EOL | '\\"' + '\\\\')* '"';
// STRING_CAR: ~('\n' | '"' | '\\')+ ;
  
