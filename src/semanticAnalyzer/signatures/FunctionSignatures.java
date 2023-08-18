package semanticAnalyzer.signatures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asmCodeGenerator.AddCharToStringCodeGenerator;
import asmCodeGenerator.AddRationalsCodeGenerator;
import asmCodeGenerator.AddStringToCharCodeGenerator;
import asmCodeGenerator.AddStringsCodeGenerator;
import asmCodeGenerator.AllocOperatorCodeGenerator;
import asmCodeGenerator.ArrayIndexingCodeGenerator;
import asmCodeGenerator.CharToBoolCodeGenerator;
import asmCodeGenerator.CharToRatCodeGenerator;
import asmCodeGenerator.DivideRationalsCodeGenerator;
import asmCodeGenerator.FloatToRatCodeGenerator;
import asmCodeGenerator.FloatingDivideCodeGenerator;
import asmCodeGenerator.FloatingExpressOverCodeGenerator;
import asmCodeGenerator.FloatingRationalizeCodeGenerator;
import asmCodeGenerator.IntToBoolCodeGenerator;
import asmCodeGenerator.IntToCharCodeGenerator;
import asmCodeGenerator.IntToRatCodeGenerator;
import asmCodeGenerator.FormRationalCodeGenerator;
import asmCodeGenerator.IntegerDivideCodeGenerator;
import asmCodeGenerator.MultiplyRationalsCodeGenerator;
import asmCodeGenerator.RatToFloatCodeGenerator;
import asmCodeGenerator.RatToIntCodeGenerator;
import asmCodeGenerator.RationalExpressOverCodeGenerator;
import asmCodeGenerator.RationalRationalizeCodeGenerator;
import asmCodeGenerator.ShortCircuitAndCodeGenerator;
import asmCodeGenerator.ShortCircuitOrCodeGenerator;
import asmCodeGenerator.StringIndexingCodeGenerator;
import asmCodeGenerator.SubtractRationalsCodeGenerator;
import asmCodeGenerator.codeStorage.ASMOpcode;
import lexicalAnalyzer.Keyword;
import lexicalAnalyzer.Punctuator;
import semanticAnalyzer.types.Array;
import semanticAnalyzer.types.PrimitiveType;
import semanticAnalyzer.types.Type;
import semanticAnalyzer.types.TypeVariable;


public class FunctionSignatures extends ArrayList<FunctionSignature> {
	private static final long serialVersionUID = -4907792488209670697L;
	private static Map<Object, FunctionSignatures> signaturesForKey = new HashMap<Object, FunctionSignatures>();
	
	Object key;
	
	public FunctionSignatures(Object key, FunctionSignature ...functionSignatures) {
		this.key = key;
		for(FunctionSignature functionSignature: functionSignatures) {
			add(functionSignature);
		}
		signaturesForKey.put(key, this);
	}
	
	public Object getKey() {
		return key;
	}
	public boolean hasKey(Object key) {
		return this.key.equals(key);
	}
	
	public FunctionSignature acceptingSignature(List<Type> types) {
		for(FunctionSignature functionSignature: this) {
			if(functionSignature.accepts(types)) {
				return functionSignature;
			}
		}
		return FunctionSignature.nullInstance();
	}
	public boolean accepts(List<Type> types) {
		return !acceptingSignature(types).isNull();
	}

	
	/////////////////////////////////////////////////////////////////////////////////
	// access to FunctionSignatures by key object.
	
	public static FunctionSignatures nullSignatures = new FunctionSignatures(0, FunctionSignature.nullInstance());

	public static FunctionSignatures signaturesOf(Object key) {
		if(signaturesForKey.containsKey(key)) {
			return signaturesForKey.get(key);
		}
		return nullSignatures;
	}
	public static FunctionSignature signature(Object key, List<Type> types) {
		FunctionSignatures signatures = FunctionSignatures.signaturesOf(key);
		return signatures.acceptingSignature(types);
	}

	
	
	/////////////////////////////////////////////////////////////////////////////////
	// Put the signatures for operators in the following static block.
	
	static {
		// here's one example to get you started with FunctionSignatures: the signatures for addition.		
		// for this to work, you should statically import PrimitiveType.*

		new FunctionSignatures(Punctuator.ADD,
		    new FunctionSignature(ASMOpcode.Add, PrimitiveType.INTEGER, PrimitiveType.INTEGER, PrimitiveType.INTEGER),
		    new FunctionSignature(ASMOpcode.FAdd, PrimitiveType.FLOATING, PrimitiveType.FLOATING, PrimitiveType.FLOATING),
		    new FunctionSignature(new AddStringsCodeGenerator(), PrimitiveType.STRING, PrimitiveType.STRING, PrimitiveType.STRING),
		    new FunctionSignature(new AddCharToStringCodeGenerator(), PrimitiveType.STRING, PrimitiveType.CHARACTER, PrimitiveType.STRING),
		    new FunctionSignature(new AddStringToCharCodeGenerator(), PrimitiveType.CHARACTER, PrimitiveType.STRING, PrimitiveType.STRING),
		    new FunctionSignature(new AddRationalsCodeGenerator(), PrimitiveType.RATIONAL, PrimitiveType.RATIONAL, PrimitiveType.RATIONAL)
		);
		
		new FunctionSignatures(Punctuator.SUBTRACT,
		    new FunctionSignature(ASMOpcode.Subtract, PrimitiveType.INTEGER, PrimitiveType.INTEGER, PrimitiveType.INTEGER),
		    new FunctionSignature(ASMOpcode.FSubtract, PrimitiveType.FLOATING, PrimitiveType.FLOATING, PrimitiveType.FLOATING),
		    new FunctionSignature(new SubtractRationalsCodeGenerator(), PrimitiveType.RATIONAL, PrimitiveType.RATIONAL, PrimitiveType.RATIONAL)
		);
		
		new FunctionSignatures(Punctuator.MULTIPLY,
		    new FunctionSignature(ASMOpcode.Multiply, PrimitiveType.INTEGER, PrimitiveType.INTEGER, PrimitiveType.INTEGER),
		    new FunctionSignature(ASMOpcode.FMultiply, PrimitiveType.FLOATING, PrimitiveType.FLOATING, PrimitiveType.FLOATING),
		    new FunctionSignature(new MultiplyRationalsCodeGenerator(), PrimitiveType.RATIONAL, PrimitiveType.RATIONAL, PrimitiveType.RATIONAL)
		);
		
		new FunctionSignatures(Punctuator.DIVIDE,
		    new FunctionSignature(new IntegerDivideCodeGenerator(), PrimitiveType.INTEGER, PrimitiveType.INTEGER, PrimitiveType.INTEGER),
		    new FunctionSignature(new FloatingDivideCodeGenerator(), PrimitiveType.FLOATING, PrimitiveType.FLOATING, PrimitiveType.FLOATING),
		    new FunctionSignature(new DivideRationalsCodeGenerator(), PrimitiveType.RATIONAL, PrimitiveType.RATIONAL, PrimitiveType.RATIONAL)
		);
		
		TypeVariable S = new TypeVariable("S");
		List<TypeVariable> setS = Arrays.asList(S);
		
		// arrayVar[index]
		new FunctionSignatures(Punctuator.ARRAY_INDEXING, 
			new FunctionSignature(new ArrayIndexingCodeGenerator(), setS, new Array(S), PrimitiveType.INTEGER, S),
			new FunctionSignature(new StringIndexingCodeGenerator(), PrimitiveType.STRING, PrimitiveType.INTEGER, PrimitiveType.CHARACTER)
		);
		
		/*TypeVariable T = new TypeVariable("T");
		TypeVariable U = new TypeVariable("U");
		List<Type> params = Arrays.asList(T);
		Lambda lambda = new Lambda(params, U);
		
		new FunctionSignatures(Keyword.MAP, 
			new FunctionSignature(new MapOperatorCodeGenerator(), setS, new Array(S), lambda, U)
		);*/
		
		// alloc arrayType(#num of elements) = ex. alloc [char](14)
		new FunctionSignatures(Keyword.ALLOC,
			new FunctionSignature(new AllocOperatorCodeGenerator(), setS, new Array(S), PrimitiveType.INTEGER, new Array(S))
		);
		
		// Rationals/fractions (f // d)
		new FunctionSignatures(Punctuator.OVER,
			new FunctionSignature(new FormRationalCodeGenerator(), PrimitiveType.INTEGER, PrimitiveType.INTEGER, PrimitiveType.RATIONAL)
		);
		
		//  The result of f///d is the integer that would “best” express the number f when it is used as a numerator over the denominator d. 
		new FunctionSignatures(Punctuator.EXPRESS_OVER,
			new FunctionSignature(new RationalExpressOverCodeGenerator(), PrimitiveType.RATIONAL, PrimitiveType.INTEGER, PrimitiveType.INTEGER),
			new FunctionSignature(new FloatingExpressOverCodeGenerator(), PrimitiveType.FLOATING, PrimitiveType.INTEGER, PrimitiveType.INTEGER)
		);
		
		// f //// d = (f /// d) // d
		new FunctionSignatures(Punctuator.RATIONALIZE, 
    		new FunctionSignature(new RationalRationalizeCodeGenerator(), PrimitiveType.RATIONAL, PrimitiveType.INTEGER, PrimitiveType.RATIONAL),
			new FunctionSignature(new FloatingRationalizeCodeGenerator(), PrimitiveType.FLOATING, PrimitiveType.INTEGER, PrimitiveType.RATIONAL)
		);
	
		new FunctionSignatures(Punctuator.BOOLEAN_OR,
			new FunctionSignature(new ShortCircuitOrCodeGenerator(), PrimitiveType.BOOLEAN, PrimitiveType.BOOLEAN, PrimitiveType.BOOLEAN)
		);
		
		new FunctionSignatures(Punctuator.BOOLEAN_AND,
			new FunctionSignature(new ShortCircuitAndCodeGenerator(), PrimitiveType.BOOLEAN, PrimitiveType.BOOLEAN, PrimitiveType.BOOLEAN)
		);
		
		new FunctionSignatures(Punctuator.CAST,
		    new FunctionSignature(ASMOpcode.Nop, PrimitiveType.BOOLEAN, PrimitiveType.BOOLEAN, PrimitiveType.BOOLEAN),
		    new FunctionSignature(ASMOpcode.Nop, PrimitiveType.CHARACTER, PrimitiveType.CHARACTER, PrimitiveType.CHARACTER),
		    new FunctionSignature(ASMOpcode.Nop, PrimitiveType.CHARACTER, PrimitiveType.INTEGER, PrimitiveType.INTEGER),
		    new FunctionSignature(new CharToBoolCodeGenerator(), PrimitiveType.CHARACTER, PrimitiveType.BOOLEAN, PrimitiveType.BOOLEAN),
		    new FunctionSignature(ASMOpcode.Nop, PrimitiveType.INTEGER, PrimitiveType.INTEGER, PrimitiveType.INTEGER),
		    new FunctionSignature(ASMOpcode.Nop, PrimitiveType.FLOATING, PrimitiveType.FLOATING, PrimitiveType.FLOATING),
		    //new FunctionSignature(new IntToStringCodeGenerator(), PrimitiveType.INTEGER, PrimitiveType.STRING, PrimitiveType.STRING),
		    //new FunctionSignature(new FloatToStringCodeGenerator(), PrimitiveType.FLOATING, PrimitiveType.STRING, PrimitiveType.STRING),
		    new FunctionSignature(ASMOpcode.Nop, PrimitiveType.STRING, PrimitiveType.STRING, PrimitiveType.STRING),
		    
		    new FunctionSignature(new IntToBoolCodeGenerator(), PrimitiveType.INTEGER, PrimitiveType.BOOLEAN, PrimitiveType.BOOLEAN),
		    new FunctionSignature(new IntToCharCodeGenerator(), PrimitiveType.INTEGER, PrimitiveType.CHARACTER, PrimitiveType.CHARACTER),
		    new FunctionSignature(ASMOpcode.ConvertF, PrimitiveType.INTEGER, PrimitiveType.FLOATING, PrimitiveType.FLOATING),
		    new FunctionSignature(ASMOpcode.ConvertI, PrimitiveType.FLOATING, PrimitiveType.INTEGER, PrimitiveType.INTEGER),
		    
		    new FunctionSignature(ASMOpcode.Nop, PrimitiveType.RATIONAL, PrimitiveType.RATIONAL, PrimitiveType.RATIONAL),
		    new FunctionSignature(new RatToIntCodeGenerator(), PrimitiveType.RATIONAL, PrimitiveType.INTEGER, PrimitiveType.INTEGER),
		    new FunctionSignature(new RatToFloatCodeGenerator(), PrimitiveType.RATIONAL, PrimitiveType.FLOATING, PrimitiveType.FLOATING),
		    new FunctionSignature(new IntToRatCodeGenerator(), PrimitiveType.INTEGER, PrimitiveType.RATIONAL, PrimitiveType.RATIONAL),
		    new FunctionSignature(new CharToRatCodeGenerator(), PrimitiveType.CHARACTER, PrimitiveType.RATIONAL, PrimitiveType.RATIONAL),
		    new FunctionSignature(new FloatToRatCodeGenerator(), PrimitiveType.FLOATING, PrimitiveType.RATIONAL, PrimitiveType.RATIONAL)
		);
		
		Punctuator[] comparisons = { Punctuator.GREATER, Punctuator.GREATER_EQ, Punctuator.LESS, 
									Punctuator.LESS_EQ, Punctuator.EQUAL, Punctuator.NOT_EQUAL };
		
		// TODO: implement type promotion (somewhere probably not here)
		
		for(Punctuator comparison: comparisons) {
			FunctionSignature iSignature = new FunctionSignature(1, 
					PrimitiveType.INTEGER, PrimitiveType.INTEGER, PrimitiveType.BOOLEAN);
			FunctionSignature cSignature = new FunctionSignature(1, 
					PrimitiveType.CHARACTER, PrimitiveType.CHARACTER, PrimitiveType.BOOLEAN);
			FunctionSignature fSignature = new FunctionSignature(1, 
					PrimitiveType.FLOATING, PrimitiveType.FLOATING, PrimitiveType.BOOLEAN);
			FunctionSignature bSignature = new FunctionSignature(1, 
					PrimitiveType.BOOLEAN, PrimitiveType.BOOLEAN, PrimitiveType.BOOLEAN);
			FunctionSignature sSignature = new FunctionSignature(1, 
					PrimitiveType.STRING, PrimitiveType.STRING, PrimitiveType.BOOLEAN);
			FunctionSignature rSignature = new FunctionSignature(1, 
					PrimitiveType.RATIONAL, PrimitiveType.RATIONAL, PrimitiveType.BOOLEAN);
			FunctionSignature aSignature = new FunctionSignature(1, 
					new Array(S), new Array(S), PrimitiveType.BOOLEAN);
		
			if(comparison == Punctuator.EQUAL || comparison == Punctuator.NOT_EQUAL) {
				new FunctionSignatures(comparison, iSignature, cSignature, fSignature, bSignature, sSignature, rSignature, aSignature);
			}
			else {
				new FunctionSignatures(comparison, iSignature, cSignature, fSignature, rSignature);
			}
		
		}
		
		// First, we use the operator itself (in this case the Punctuator ADD) as the key.
		// Then, we give that key two signatures: one an (INT x INT -> INT) and the other
		// a (FLOAT x FLOAT -> FLOAT).  Each signature has a "whichVariant" parameter where
		// I'm placing the instruction (ASMOpcode) that needs to be executed.
		//
		// I'll follow the convention that if a signature has an ASMOpcode for its whichVariant,
		// then to generate code for the operation, one only needs to generate the code for
		// the operands (in order) and then add to that the Opcode.  For instance, the code for
		// floating addition should look like:
		//
		//		(generate argument 1)	: may be many instructions
		//		(generate argument 2)   : ditto
		//		FAdd					: just one instruction
		//
		// If the code that an operator should generate is more complicated than this, then
		// I will not use an ASMOpcode for the whichVariant.  In these cases I typically use
		// a small object with one method (the "Command" design pattern) that generates the
		// required code.

	}

}
