package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.*;
import static asmCodeGenerator.runtime.RunTime.*;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import asmCodeGenerator.runtime.RunTime;
import parseTree.ParseNode;
import semanticAnalyzer.types.PrimitiveType;

public class StringSliceCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment frag = new ASMCodeFragment(CodeType.GENERATES_ADDRESS);
									 					// [... str idx1 idx2]
		Macros.storeITo(frag, STRING_INDEXING_INDEX2); 	// [... str idx1]
		Macros.storeITo(frag, STRING_INDEXING_INDEX); 	// [... str]
		frag.add(PushI, STRING_HEADER_SIZE);			// subtract header size
		frag.add(Subtract);
		
		Macros.storeITo(frag, STRING_INDEXING_PTR); 	// [...]
		Macros.loadIFrom(frag, STRING_INDEXING_PTR); 	// [... str]
		frag.add(JumpFalse, NULL_STRING_RUNTIME_ERROR); // [...]
		
		Macros.loadIFrom(frag, STRING_INDEXING_INDEX); 	// [... index]
		frag.add(JumpNeg, INDEX_OUT_OF_BOUNDS_RUNTIME_ERROR); // [...]
		
		Labeller labeller = new Labeller("string-indexing");
		String labelValidSlice = labeller.newLabel("valid-slice");
		
		Macros.loadIFrom(frag, STRING_INDEXING_PTR); 	// [... str]
		Macros.readIOffset(frag, STRING_LENGTH_OFFSET); // [... length]
		Macros.loadIFrom(frag, STRING_INDEXING_INDEX2); // [... length index2]
		frag.add(Subtract); 							// [... length-index2]
		frag.add(JumpNeg, INDEX_OUT_OF_BOUNDS_RUNTIME_ERROR); // [...]
		
		Macros.loadIFrom(frag, STRING_INDEXING_INDEX2);	// [... idx2]
		Macros.loadIFrom(frag, STRING_INDEXING_INDEX); 	// [... idx2 idx]
		frag.add(Subtract);								// [... idx2-idx]
		Macros.storeITo(frag, STRING_LENGTH_TEMPORARY);
		Macros.loadIFrom(frag, STRING_LENGTH_TEMPORARY);
		frag.add(JumpPos, labelValidSlice);				// [...]
		frag.add(Jump, EMPTY_SLICE_RUNTIME_ERROR);		// [...]
		frag.add(Label, labelValidSlice);				// [...]
		
		Macros.loadIFrom(frag, STRING_INDEXING_PTR);  	// [... str]
		frag.add(PushI, STRING_HEADER_SIZE);  			// [... str size]
		frag.add(Add);  								// [... str+size]
		Macros.loadIFrom(frag, STRING_INDEXING_INDEX); 	// [... str+size index]
		
		frag.add(PushI, PrimitiveType.CHARACTER.getSize());// [... str+size index charSize]
		frag.add(Multiply); 							// [... str+size index*charSize]
		frag.add(Add); 									// [... str+size+(index*charSize)]
		
		Macros.loadIFrom(frag, STRING_LENGTH_TEMPORARY);// [... startAddrOfSubString substringLength]
		
		int statusFlags = 0b1001; // isPerm, isDel, isRef, isImmut
		RunTime.createSubstringRecord(frag, statusFlags);// [... ptrToArray]
		return frag;
	}

}
