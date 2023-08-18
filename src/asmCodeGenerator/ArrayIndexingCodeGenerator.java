package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.*;
import static asmCodeGenerator.runtime.RunTime.ARRAY_INDEXING_INDEX;
import static asmCodeGenerator.runtime.RunTime.ARRAY_INDEXING_ARRAY;
import static asmCodeGenerator.runtime.RunTime.NULL_ARRAY_RUNTIME_ERROR;
import static asmCodeGenerator.runtime.RunTime.INDEX_OUT_OF_BOUNDS_RUNTIME_ERROR;
import static asmCodeGenerator.runtime.RunTime.ARRAY_LENGTH_OFFSET;
import static asmCodeGenerator.runtime.RunTime.ARRAY_HEADER_SIZE;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import parseTree.ParseNode;
import semanticAnalyzer.types.Array;
import semanticAnalyzer.types.Type;

public class ArrayIndexingCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment frag = new ASMCodeFragment(CodeType.GENERATES_ADDRESS);
									 					// [... array index]
		Macros.storeITo(frag, ARRAY_INDEXING_INDEX); 	// [... arr]
		Macros.storeITo(frag, ARRAY_INDEXING_ARRAY); 	// [...]
		
		Macros.loadIFrom(frag, ARRAY_INDEXING_ARRAY); 	// [... arr]
		frag.add(JumpFalse, NULL_ARRAY_RUNTIME_ERROR); 	// [...]
		
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX); 	// [... index]
		frag.add(JumpNeg, INDEX_OUT_OF_BOUNDS_RUNTIME_ERROR); // [...]
		
		Labeller labeller = new Labeller("array-indexing");
		String labelNotOOB = labeller.newLabel("in-bounds");
		
		Macros.loadIFrom(frag, ARRAY_INDEXING_ARRAY); 	// [... arr]
		Macros.readIOffset(frag, ARRAY_LENGTH_OFFSET); 	// [... length]
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX); 	// [... length index]
		frag.add(Subtract); 							// [... length-index]
		frag.add(JumpPos, labelNotOOB); 					// [...]
		frag.add(Jump, INDEX_OUT_OF_BOUNDS_RUNTIME_ERROR); 
		frag.add(Label, labelNotOOB);  
		
		Macros.loadIFrom(frag, ARRAY_INDEXING_ARRAY);  	// [... arr]
		frag.add(PushI, ARRAY_HEADER_SIZE);  			// [... arr size]
		frag.add(Add);  								// [... arr+size]
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX); 	// [... arr+size index]
		
		Array arrayType = (Array)(node.child(0).getType());
		Type subtype = arrayType.getSubType();
		
		frag.add(PushI, subtype.getSize()); 			// [... arr+size index sizeSubtype]
		
		frag.add(Multiply); 							// [... arr+size index*sizeSubtype]
		frag.add(Add); 									// [... arr+size+(index*sizeSubtype)]
		
		return frag;
	}

}
