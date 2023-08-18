package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.*;
import static asmCodeGenerator.runtime.RunTime.*;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import parseTree.ParseNode;

public class ReverseStringCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment frag = new ASMCodeFragment(CodeType.GENERATES_ADDRESS);
														// [... strPtr]
		frag.add(PushI, STRING_HEADER_SIZE);			// subtract header size
		frag.add(Subtract);

		int statusFlags = 0b1001; // isPerm, isDel, isRef, isImmut

		final int typecode = STRING_TYPE_ID;

		Macros.storeITo(frag, STRING_TEMP_ADDR_STORAGE1);// [... ]
		Macros.loadIFrom(frag, STRING_TEMP_ADDR_STORAGE1);// [... strPtr]
		
		frag.add(PushI, STRING_LENGTH_OFFSET); 	//[... strPtr lengthOffset]
		frag.add(Add);				 			//[... strPtr+lengthOffset]
		frag.add(LoadI);						//[... strLength]
		Macros.storeITo(frag, STRING_LENGTH_TEMPORARY);
		Macros.loadIFrom(frag, STRING_LENGTH_TEMPORARY);
		
		frag.add(PushI, STRING_HEADER_SIZE); 	// [... length SHS]
		frag.add(Add); 							// [... totalRecordSize]
		frag.add(PushI, 1);						// [... totalRecordSize 1] (add space for null byte)
		frag.add(Add);							// [... totalRecordSize+1]
		
		createRecord(frag, typecode, statusFlags); // [...]
		
		Macros.loadIFrom(frag,  RECORD_CREATION_TEMPORARY);	// [... ptr]
		frag.add(PushI, STRING_HEADER_SIZE);				// [... ptr SHS]
		frag.add(Add);										// [... elemsPtr]
		Macros.storeITo(frag, STRING_DEST_PTR_TEMPORARY);	// [...]
		
		Macros.loadIFrom(frag, STRING_TEMP_ADDR_STORAGE1);	// [... srcAddr]
		frag.add(PushI, STRING_HEADER_SIZE);				// [... srcAddr SHS]
		frag.add(Add);										// [... srcAddr+SHS]
		
		// Copy string in reverse 
		frag.add(PushI, 0);									// [... srcStrAddr 0]
		Macros.storeITo(frag, STRING_INDEXING_INDEX);		// [... srcStrAddr]
		
		Labeller labeller = new Labeller("reverseString");
		String loopStrCopyStart = labeller.newLabel("loopStart");
		
		frag.add(Label, loopStrCopyStart);					// [... srcStrAddr]
		frag.add(Duplicate);								// [... srcStrAddr srcStrAddr]
		Macros.loadIFrom(frag, STRING_INDEXING_INDEX);		// [... srcStrAddr srcStrAddr index]
		frag.add(Add);										// [... srcStrAddr srcStrAddr+index ]
		frag.add(LoadC);									// [... srcStrAddr *(srcStrAddr+index)]
		
		Macros.loadIFrom(frag, STRING_DEST_PTR_TEMPORARY);	// [... srcStrAddr *(srcStrAddr+index) dstStrAddr]
		Macros.loadIFrom(frag, STRING_LENGTH_TEMPORARY);	// [... srcStrAddr *(srcStrAddr+index) dstStrAddr strLength]
		frag.add(Add);										// [... srcStrAddr *(srcStrAddr+index) dstStrAddr+strLength]
		frag.add(PushI, 1);									// [... srcStrAddr *(srcStrAddr+index) dstStrAddr+strLength 1]
		frag.add(Subtract);									// [... srcStrAddr *(srcStrAddr+index) dstStrAddr+strLength-1]
		Macros.loadIFrom(frag, STRING_INDEXING_INDEX);// [... srcStrAddr *(srcStrAddr+index) dstStrAddr+strLength-1 index]
		frag.add(Subtract);							  // [... srcStrAddr *(srcStrAddr+index) dstStrAddr+strLength-1-index]
		frag.add(Exchange);							  // [... srcStrAddr dstStrAddr+strLength-1-index *(srcStrAddr+index) ]
		frag.add(StoreC);							  // [... srcStrAddr ]
		
		Macros.loadIFrom(frag, STRING_INDEXING_INDEX); // [... srcStrAddr index ]
		Macros.loadIFrom(frag, STRING_LENGTH_TEMPORARY); // [... srcStrAddr index length]
		frag.add(PushI, 1);								 // [... srcStrAddr index length 1]
		frag.add(Subtract);								 // [... srcStrAddr index length-1]
		frag.add(Subtract);								 // [... srcStrAddr index-(length-1)]
		Macros.incrementInteger(frag, STRING_INDEXING_INDEX);// [... srcStrAddr index-(length-1)]
		frag.add(JumpNeg, loopStrCopyStart);				// [... srcStrAddr]
		frag.add(Pop);										// [...]
		
		frag.add(PushI, '\0');								// [... \0]
		Macros.loadIFrom(frag, STRING_DEST_PTR_TEMPORARY);	// [... \0 dstStrAddr]
		Macros.loadIFrom(frag, STRING_INDEXING_INDEX);		// [... \0 dstStrAddr index]
		frag.add(Add);										// [... \0 dstStrAddr+index]
		frag.add(Exchange);									// [... dstStrAddr+index \0 ]
		frag.add(StoreC);									// [... ]
		
		Macros.loadIFrom(frag, STRING_LENGTH_TEMPORARY); 	// [... length]
		Macros.writeIPtrOffset(frag, 						// [...]
				RECORD_CREATION_TEMPORARY, STRING_LENGTH_OFFSET);	
		
		Macros.loadIFrom(frag, STRING_DEST_PTR_TEMPORARY);
		
		return frag;
	}
}
