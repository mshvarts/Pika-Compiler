package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.*;
import static asmCodeGenerator.runtime.RunTime.*;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import parseTree.ParseNode;
import semanticAnalyzer.types.PrimitiveType;

public class StringIndexingCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment frag = new ASMCodeFragment(CodeType.GENERATES_ADDRESS);
									 					// [... str index]
		Macros.storeITo(frag, STRING_INDEXING_INDEX); 	// [... str]
		frag.add(PushI, STRING_HEADER_SIZE);			// subtract header size
		frag.add(Subtract);
		
		Macros.storeITo(frag, STRING_INDEXING_PTR); 	// [...]
		
		Macros.loadIFrom(frag, STRING_INDEXING_PTR); 	// [... str]
		frag.add(JumpFalse, NULL_STRING_RUNTIME_ERROR); 	// [...]
		
		Macros.loadIFrom(frag, STRING_INDEXING_INDEX); 	// [... index]
		frag.add(JumpNeg, INDEX_OUT_OF_BOUNDS_RUNTIME_ERROR); // [...]
		
		Labeller labeller = new Labeller("string-indexing");
		String labelNotOOB = labeller.newLabel("in-bounds");
		
		Macros.loadIFrom(frag, STRING_INDEXING_PTR); 	// [... str]
		Macros.readIOffset(frag, STRING_LENGTH_OFFSET); // [... length]
		Macros.loadIFrom(frag, STRING_INDEXING_INDEX); 	// [... length index]
		frag.add(Subtract); 							// [... length-index]
		frag.add(JumpPos, labelNotOOB); 				// [...]
		frag.add(Jump, INDEX_OUT_OF_BOUNDS_RUNTIME_ERROR); 
		frag.add(Label, labelNotOOB);  
		
		Macros.loadIFrom(frag, STRING_INDEXING_PTR);  	// [... str]
		frag.add(PushI, STRING_HEADER_SIZE);  			// [... str size]
		frag.add(Add);  								// [... str+size]
		Macros.loadIFrom(frag, STRING_INDEXING_INDEX); 	// [... str+size index]
		
		frag.add(PushI, PrimitiveType.CHARACTER.getSize());// [... str+size index charSize]
		frag.add(Multiply); 							// [... str+size index*charSize]
		frag.add(Add); 									// [... str+size+(index*charSize)]
		return frag;
	}

}
