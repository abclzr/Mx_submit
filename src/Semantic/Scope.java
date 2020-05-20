package Semantic;

import AST.FuncDeclNode;
import Utils.Position;
import Utils.SemanticError;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Scope {
    public static Scope globalScope;
    private Scope fatherScope;
    private Type returnType;
    private Type classType;
    private FunctionSymbol functionSymbol;
    private boolean inLoop;
    private Map<String, VariableSymbol> varMap = new LinkedHashMap<>();
    private Map<String, FunctionSymbol> funcMap = new LinkedHashMap<>();
    private Map<String, Type> typeMap = new LinkedHashMap<>();

    public static PrimitiveType stringType;
    public static PrimitiveType intType;
    public static PrimitiveType boolType;
    public static PrimitiveType voidType;
    public static PrimitiveType nullType;

    public static FunctionSymbol lengthSymbol;
    public static FunctionSymbol substringSymbol;
    public static FunctionSymbol parseIntSymbol;
    public static FunctionSymbol ordSymbol;

    public static FunctionSymbol printSymbol;
    public static FunctionSymbol printlnSymbol;
    public static FunctionSymbol printIntSymbol;
    public static FunctionSymbol printlnIntSymbol;
    public static FunctionSymbol getStringSymbol;
    public static FunctionSymbol getIntSymbol;
    public static FunctionSymbol toStringSymbol;

    public static FunctionSymbol sizeSymbol;

    public Scope(Scope fa) {
        this.fatherScope = fa;
        if (fa != null) this.returnType = fa.returnType;
        if (fa != null) this.inLoop = fa.inLoop;
        if (fa != null) this.classType = fa.classType;
        if (fa != null) this.functionSymbol = fa.functionSymbol;
    }

    //for FunctionSymbol
    public Scope(Scope fa, Type rt, FunctionSymbol func) {
        this.fatherScope = fa;
        this.returnType = rt;
        this.inLoop = false;
        this.classType = fa.classType;
        this.functionSymbol = func;
    }

    //for ClassType
    public Scope(Scope fa, ClassType ct) {
        this.fatherScope = fa;
        this.returnType = null;
        this.inLoop = false;
        this.classType = ct;
        this.functionSymbol = null;
    }

    //for StringType
    public Scope(Scope fa, PrimitiveType st) {
        this.fatherScope = fa;
        this.returnType = null;
        this.inLoop = false;
        this.classType = st;
        this.functionSymbol = null;
    }

    //for Loop
    public Scope(Scope fa, boolean in) {
        this.fatherScope = fa;
        this.returnType = fa.returnType;
        this.inLoop = in;
        this.classType = fa.classType;
        this.functionSymbol = fa.functionSymbol;
    }

    public FunctionSymbol getFunctionSymbol() {
        return functionSymbol;
    }

    public void setFunctionSymbol(FunctionSymbol functionSymbol) {
        this.functionSymbol = functionSymbol;
    }

    public void setClassType(Type classType) {
        this.classType = classType;
    }

    public Type getClassType() {
        return classType;
    }

    public boolean isInLoop() {
        return inLoop;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void initGlobalScope() {
        stringType = new PrimitiveType("string");
        intType = new PrimitiveType("int");
        boolType = new PrimitiveType("bool");
        voidType = new PrimitiveType("void");
        nullType = new PrimitiveType("null");

        Scope stringScope = new Scope(globalScope, stringType);
        List<Type> strParameter = new ArrayList<>();
        strParameter.add(stringType);
        List<Type> intParameter = new ArrayList<>();
        intParameter.add(intType);
        List<Type> doubleIntParameter = new ArrayList<>();
        doubleIntParameter.add(intType); doubleIntParameter.add(intType);

        lengthSymbol = new FunctionSymbol(intType, "length", null, new Position(null), stringScope, null);
        substringSymbol = new FunctionSymbol(stringType, "substring", null, new Position(null), stringScope, doubleIntParameter);
        parseIntSymbol = new FunctionSymbol(intType, "parseInt", null, new Position(null), stringScope, null);
        ordSymbol = new FunctionSymbol(intType, "ord", null, new Position(null), stringScope, intParameter);

        stringScope.addFunction(lengthSymbol);
        stringScope.addFunction(substringSymbol);
        stringScope.addFunction(parseIntSymbol);
        stringScope.addFunction(ordSymbol);

        stringType.setScope(stringScope);

        typeMap.put("string", stringType);
        typeMap.put("int", intType);
        typeMap.put("bool", boolType);
        typeMap.put("void", voidType);
        typeMap.put("null", nullType);

        printSymbol = new FunctionSymbol(voidType, "print", null, new Position(null), this, strParameter);
        printlnSymbol = new FunctionSymbol(voidType, "println", null, new Position(null), this, strParameter);
        printIntSymbol = new FunctionSymbol(voidType, "printInt", null, new Position(null), this, intParameter);
        printlnIntSymbol = new FunctionSymbol(voidType, "printlnInt", null, new Position(null), this, intParameter);
        getStringSymbol = new FunctionSymbol(stringType, "getString", null, new Position(null), this, null);
        getIntSymbol = new FunctionSymbol(intType, "getInt", null, new Position(null), this, null);
        toStringSymbol = new FunctionSymbol(stringType, "toString", null, new Position(null), this, intParameter);

        funcMap.put("print", printSymbol);
        funcMap.put("println", printlnSymbol);
        funcMap.put("printInt", printIntSymbol);
        funcMap.put("printlnInt", printlnIntSymbol);
        funcMap.put("getString", getStringSymbol);
        funcMap.put("getInt", getIntSymbol);
        funcMap.put("toString", toStringSymbol);

        ArrayType.setScope(new Scope(globalScope));
        sizeSymbol = new FunctionSymbol(intType, "size", null, new Position(null), ArrayType.getScope(), null);
        ArrayType.getScope().addFunction(sizeSymbol);
    }

    public boolean alreadyContains(String str) {
        return varMap.containsKey(str) || funcMap.containsKey(str) || typeMap.containsKey(str);
    }

    public boolean alreadyContainsVar(String str) {
        if (varMap.containsKey(str) || funcMap.containsKey(str) || typeMap.containsKey(str))
            return true;
        else
            return globalScope.typeMap.containsKey(str);
    }

    public void addVariable(VariableSymbol var) {
        if (alreadyContainsVar(var.getName()))
            throw new SemanticError("already contains " + var.getName(), var.getPos());
        varMap.put(var.getName(), var);
    }

    public void addFunction(FunctionSymbol func) {
        if (alreadyContains(func.getName()))
            throw new SemanticError("already contains " + func.getName(), func.getPos());
        funcMap.put(func.getName(), func);
    }

    public void addClass(ClassType ct) {
        if (alreadyContains(ct.getTypeName()))
            throw new SemanticError("already contains " + ct.getTypeName(), ct.getDefine().getPosition());
        typeMap.put(ct.getTypeName(), ct);
    }

    public VariableSymbol findVar(String var, Position pos) {
        if (varMap.containsKey(var)) {
            VariableSymbol v = varMap.get(var);
            if(v.getDefine().getPosition().getRow() < pos.getRow() ||
                    (v.getDefine().getPosition().getRow() == pos.getRow()
                    && v.getDefine().getPosition().getCol() < pos.getCol()))
                return v;
        }
        if (fatherScope != null) return fatherScope.findVar(var, pos);
        else throw new SemanticError(var + " not defined!", pos);
    }

    public VariableSymbol findVarInScope(String var, Position pos) {
        if (varMap.containsKey(var)) return varMap.get(var);
        else throw new SemanticError(var + " not defined!", pos);
    }

    public FunctionSymbol findFunc(String func, Position pos) {
        if (this == globalScope) {
            return globalScope.findFuncInScope(func, pos);
        } else {
            if (funcMap.containsKey(func)) return funcMap.get(func);
            else return fatherScope.findFunc(func, pos);
        }
    }

    public FunctionSymbol findFuncInScope(String func, Position pos) {
        if (funcMap.containsKey(func)) return funcMap.get(func);
        else throw new SemanticError(func + " not found!", pos);
    }

    public Type findType(String type, Position pos) {
        if (typeMap.containsKey(type)) return typeMap.get(type);
        else throw new SemanticError(type + " not defined!", pos);
    }

    public Scope getFatherScope() {
        return fatherScope;
    }

    public Map<String, Type> getTypeMap() {
        return typeMap;
    }
}
