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


OBRACE:'{';
CBRACE:'}';
OPARENT:'(';
CPARENT:')';
EOL: '\n' {skip();} ; 
SPACE: ' ' {skip();}; 
SEMI:';';
PRINTLN:'println';
PRINT:'print';
COMMA:',';
EQUALS:'=';



IDENT: (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*;
LETTER: [a-zA-Z];


/INT: '0' | POSITIVE_DIGIT DIGIT* ;
DIGIT:[0-9]; 
POSITIVE_DIGIT: [1-9] ;
NUM: DIGIT+;
SIGN : '+' | '-' | ;
EXP: ('E' | 'e') SIGN NUM;
DEC: NUM '.' NUM;
FLOATDEC:(DEC | DEC EXP) ('F' | 'f' | );
DIGITHEX: [a-fA-F0-9]+;
NUMHEX:DIGITHEX+;
FLOATHEX:('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' | );
FLOAT: FLOATDEC | FLOATHEX ;

//STRING_CAR: ~('\n' | '"' | '\\')+ ;
STRING: '"' ( ~('\n' | '"' | '\\')+ | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING: '"' ( ~('\n' | '"' | '\\')+ | EOL | '\\"' + '\\\\')* '"';


//implemntation of the grammar


//  





  
