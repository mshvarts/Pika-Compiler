package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.*;
import static asmCodeGenerator.runtime.RunTime.*;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import parseTree.ParseNode;
import semanticAnalyzer.types.Array;
import semanticAnalyzer.types.Type;

public class ReverseArrayCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment frag = new ASMCodeFragment(CodeType.GENERATES_ADDRESS);
														// [... arrayPtr]
		Type subType = ((Array)node.child(0).getType()).getSubType();
		int subtypeSize = subType.getSize();
		
		int statusFlags;
		if(subType.isReferenceType()) {
			statusFlags = 0b0010; // isPerm, isDel, isRef, isImmut
		} else {
			statusFlags = 0b0000; // isPerm, isDel, isRef, isImmut
		}

		Macros.storeITo(frag, ARRAY_INDEXING_ARRAY);// [... ]
		Macros.loadIFrom(frag, ARRAY_INDEXING_ARRAY);// [... arrayPtr]
		
		frag.add(PushI, ARRAY_LENGTH_OFFSET); 	//[... arrayPtr lengthOffset]
		frag.add(Add);				 			//[... arrayPtr+lengthOffset]
		frag.add(LoadI);						//[... arrayLength]
		Macros.storeITo(frag, ARRAY_LENGTH_TEMPORARY);  // [...]
		Macros.loadIFrom(frag, ARRAY_LENGTH_TEMPORARY); // [... length]
		
		createEmptyArrayRecord(frag, statusFlags, subtypeSize); // [...]
		
		Macros.loadIFrom(frag,  RECORD_CREATION_TEMPORARY); // [... arrayPtr]
		frag.add(PushI, ARRAY_HEADER_SIZE);					// [... ptr AHS]
		frag.add(Add);										// [... elemsPtr]
		Macros.storeITo(frag, ARRAY_DATA_PTR_TEMPORARY);	// [...]
		
		Macros.loadIFrom(frag, ARRAY_INDEXING_ARRAY);		// [... srcAddr]
		frag.add(PushI, ARRAY_HEADER_SIZE);					// [... srcAddr SHS]
		frag.add(Add);										// [... srcAddr+SHS]
		
		// Copy array in reverse 
		frag.add(PushI, 0);									// [... srcArrAddr 0]
		Macros.storeITo(frag, ARRAY_INDEXING_INDEX);		// [... srcArrAddr]
		
		Labeller labeller = new Labeller("reverseArray");
		String loopArrCopyStart = labeller.newLabel("loopStart");
		
		frag.add(Label, loopArrCopyStart);					// [... srcArrAddr]
		frag.add(Duplicate);								// [... srcArrAddr srcArrAddr]
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX);		// [... srcArrAddr srcArrAddr index]
		frag.add(PushI, subtypeSize);						// [... srcArrAddr srcArrAddr index typeSize]
		frag.add(Multiply);									// [... srcArrAddr srcArrAddr index*typeSize]
		frag.add(Add);										// [... srcArrAddr srcArrAddr+index*typeSize]
		Macros.storeITo(frag, ARRAY_SOURCE_TEMPORARY);
		Macros.loadIFrom(frag, ARRAY_SOURCE_TEMPORARY);
		frag.append(getLoadOpcode(subType));				// [... srcArrAddr *(srcArrAddr+elemOffSet)]
		//frag.add(PStack);     
		
		Macros.loadIFrom(frag, ARRAY_DATA_PTR_TEMPORARY);	// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr]
		Macros.loadIFrom(frag, ARRAY_LENGTH_TEMPORARY);	// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr arrLength]
		frag.add(PushI, 1);								// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr arrLength 1]
		frag.add(Subtract);								// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr arrLength-1]
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX);// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr arrLength-1 index]
		frag.add(Subtract);							// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr arrLength-1-index]
		frag.add(PushI, subtypeSize); 				// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr arrLength-1-index typeSize]
		frag.add(Multiply); 						// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr (arrLength-1-index)*typeSize]
		frag.add(Add);								// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr+(arrLength-1-index)*typeSize]
		frag.add(Exchange);							// [... srcArrAddr dstArrAddr+(arrLength-1-index)*typeSize *(srcArrAddr+elemOffSet)]
		//frag.add(PStack);
		frag.append(getStoreOpcode(subType));		// [... srcArrAddr ]
		
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX); 	// [... srcArrAddr index ]
		Macros.loadIFrom(frag, ARRAY_LENGTH_TEMPORARY); // [... srcArrAddr index length]
		frag.add(PushI, 1);								// [... srcArrAddr index length 1]
		frag.add(Subtract);								// [... srcArrAddr index length-1]
		frag.add(Subtract);								// [... srcArrAddr index-(length-1)]
		Macros.incrementInteger(frag, ARRAY_INDEXING_INDEX);// [... srcArrAddr index-(length-1)]
		frag.add(JumpNeg, loopArrCopyStart);				// [... srcArrAddr]
		frag.add(Pop);										// [...]
		
		Macros.loadIFrom(frag, ARRAY_LENGTH_TEMPORARY); 	// [... length]
		Macros.writeIPtrOffset(frag, 						// [...]
				RECORD_CREATION_TEMPORARY,ARRAY_LENGTH_OFFSET);	
		
		Macros.loadIFrom(frag, RECORD_CREATION_TEMPORARY);

		return frag;
	}
}
