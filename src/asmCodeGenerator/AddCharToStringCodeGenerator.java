package asmCodeGenerator;

import static asmCodeGenerator.codeStorage.ASMOpcode.*;
import static asmCodeGenerator.runtime.RunTime.*;

import asmCodeGenerator.codeStorage.ASMCodeFragment;
import asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType;
import parseTree.ParseNode;

public class AddCharToStringCodeGenerator implements SimpleCodeGenerator {

	public ASMCodeFragment generate(ParseNode node) {
		ASMCodeFragment frag = new ASMCodeFragment(CodeType.GENERATES_ADDRESS);
														// [... strPtr char]
		frag.add(Exchange);								// [... char strPtr]
		
		frag.add(PushI, STRING_HEADER_SIZE);			// subtract header size
		frag.add(Subtract);
		frag.add(Exchange);								// [... strPtr char]

		int statusFlags = 0b1001; // isPerm, isDel, isRef, isImmut

		final int typecode = STRING_TYPE_ID;
		Macros.storeITo(frag, CHARACTER_TEMP_STORAGE);	// [... strPtr]

		Macros.storeITo(frag, STRING_TEMP_ADDR_STORAGE1);// [...]
		Macros.loadIFrom(frag, STRING_TEMP_ADDR_STORAGE1);// [... strPtr]
		
		frag.add(PushI, STRING_LENGTH_OFFSET); 	//[... strPtr lengthOffset]
		frag.add(Add);				 			//[... strPtr+lengthOffset]
		frag.add(LoadI);						//[... strLength]
		frag.add(PushI, 1); 					//[... strLength 1]
		frag.add(Add);							//[... strAndCharLength]
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
		Macros.storeITo(frag, STRING_DATA_PTR_TEMPORARY);	// [...]
		
		Macros.loadIFrom(frag, STRING_TEMP_ADDR_STORAGE1);	// [... srcAddr]
		frag.add(PushI, STRING_HEADER_SIZE);				// [... srcAddr SHS]
		frag.add(Add);										// [... srcAddr+SHS]
		Macros.loadIFrom(frag, STRING_DATA_PTR_TEMPORARY);	// [... srcStrAddr dstStrAddr]
		
		frag.add(Call, COPY_STRING_BYTES);					// [...]
		
		Macros.loadIFrom(frag, CHARACTER_TEMP_STORAGE);	// [... char]
		Macros.loadIFrom(frag, STRING_DATA_PTR_TEMPORARY);	// [... char dstStrAddr]
		
		Macros.loadIFrom(frag, STRING_TEMP_ADDR_STORAGE1);	// [... char dstStrAddr srcAddr]
		Macros.readIOffset(frag, STRING_LENGTH_OFFSET);		// [... char dstStrAddr Strlength]
		frag.add(Add);										// [... char dstStrAddr+Strlength]
		
		frag.add(Exchange);									// [... dstStrAddr+Str1length char]
		frag.add(StoreC);									// [...]
		
		Macros.loadIFrom(frag, STRING_LENGTH_TEMPORARY); 	// [... length]
		Macros.writeIPtrOffset(frag, 						// [...]
				RECORD_CREATION_TEMPORARY, STRING_LENGTH_OFFSET);	
		
		frag.add(PushD,  STRING_DATA_PTR_TEMPORARY);
		
		return frag;
	}
}
