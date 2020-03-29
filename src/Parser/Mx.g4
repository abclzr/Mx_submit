grammar Mx;

INT:'int';
BOOL: 'bool';
STRING:'string';
VOID:'void';
IF:'if';
ELSE:'else';
FOR:'for';
WHILE:'while';
BREAK:'break';
CONTINUE:'continue';
RETURN:'return';
NEW:'new';
CLASS:'class';
THIS:'this';


StringLiteral: '"' (~["\\\r\n]| '\\' ["n\\])* '"';
BooleanLiteral: 'true' | 'false';
NullLiteral: 'null';
IntegerLiteral
    : [1-9] [0-9]*
    | '0'
    ;
Identifier
    : [a-zA-Z] [a-zA-Z_0-9]*
    ;

WS:                 [ \t\r\n\u000C]+ -> channel(HIDDEN);
COMMENT:            '/*' .*? '*/'    -> channel(HIDDEN);
LINE_COMMENT:       '//' ~[\r\n]*    -> channel(HIDDEN);


//parser
program: (declaration)* EOF;

declaration: (classDeclaration | functionDeclaration | variableDeclaration);

classDeclaration: CLASS Identifier classBody;
classBody: '{' classBodyDeclaration* '}' ';'?;
classBodyDeclaration: variableDeclaration
                    | methodDeclaration;
methodDeclaration: (typeOrVoid)? Identifier parameters block;
variableDeclaration: type variableDecorator(',' variableDecorator)* ';';
variableDecorator: Identifier ( '=' expression)?;
type: (classType | primitiveType) ('[' ']')*;
typeOrVoid: type | VOID;
classType: Identifier;
primitiveType: BOOL | INT | STRING;
functionDeclaration: typeOrVoid Identifier parameters block;
parameters: '(' parameterList? ')';
parameterList: parameter (',' parameter)*;
parameter: type Identifier;
block: '{' blockStatement '}' ;
blockStatement : statement*;

statement
    : block                                             #blockStmt
    | IF '('expression')' statement (ELSE statement)?   #ifStmt
    | FOR '(' forControl ')' statement                  #forStmt
    | WHILE '('expression')' statement                  #whileStmt
    | RETURN expression? ';'                            #returnStmt
    | BREAK ';'                                         #breakStmt
    | CONTINUE ';'                                      #continueStmt
    | ';'                                               #semiStmt
    | expression ';'                                    #exprStmt
    | variableDeclaration                               #variableDeclStmt
    ;

expression
    : primary                                    #primaryExpr
    | expression op='.' Identifier               #memberExpr
    | expression '[' expression ']'              #arrayExpr
    | expression '(' expressionList? ')'         #methodCallExpr
    | NEW creator                                #newExpr
    | expression postfix=('++' | '--')           #postfixExpr
    | prefix=('+'|'-'|'++'|'--') expression      #prefixExpr
    | prefix=('~'|'!') expression                #prefixExpr
    | expression op=('*'|'/'|'%') expression     #binaryOpExpr
    | expression op=('+'|'-') expression         #binaryOpExpr
    | expression op=('<<' | '>>') expression     #binaryOpExpr
    | expression op=('<=' | '>=' | '>' | '<') expression #binaryOpExpr
    | expression op=('==' | '!=') expression     #binaryOpExpr
    | expression op='&' expression               #binaryOpExpr
    | expression op='^' expression               #binaryOpExpr
    | expression op='|' expression               #binaryOpExpr
    | expression op='&&' expression              #binaryOpExpr
    | expression op='||' expression              #binaryOpExpr
    | <assoc=right> expression op='=' expression #binaryOpExpr
    ;

expressionList
    : expression (',' expression)*
    ;


creator
    : (classType | primitiveType) ('[' expression ']')+ ('['']')+ ('[' expression ']')+ #errorCreator
    | (classType | primitiveType) ('[' expression ']')+ ('['']')* #arrayCreator
    | (classType | primitiveType) ('(' ')')?  #constructorCreator
    ;

primary
    : '(' expression ')' #parenthesizedExpr
    | THIS               #thisExpr
    | literal            #literalExpr
    | Identifier         #nameExpr
    ;

literal
    : IntegerLiteral
    | StringLiteral
    | BooleanLiteral
    | NullLiteral
    ;

forControl
    : initial=expression? ';' condition=expression? ';' update=expression?
    ;