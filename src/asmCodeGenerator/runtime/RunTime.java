package asmCodeGenerator.runtime;
import static asmCodeGenerator.codeStorage.ASMCodeFragment.CodeType.*;
import static asmCodeGenerator.codeStorage.ASMOpcode.*;

import asmCodeGenerator.Labeller;
import asmCodeGenerator.Macros;
import asmCodeGenerator.codeStorage.ASMCodeFragment;
import semanticAnalyzer.types.Array;
import semanticAnalyzer.types.Lambda;
import semanticAnalyzer.types.PrimitiveType;
import semanticAnalyzer.types.Type;
public class RunTime {
	public static final String EAT_LOCATION_ZERO      = "$eat-location-zero";		// helps us distinguish null pointers from real ones.
	public static final String STACK_POINTER   		  = "$stack-pointer";
	public static final String FRAME_POINTER   		  = "$frame-pointer";
	public static final String INTEGER_PRINT_FORMAT   = "$print-format-integer";
	public static final String FLOATING_PRINT_FORMAT  = "$print-format-floating";
	public static final String RATIONAL_FRAC_PRINT_FORMAT = "$print-frac-format-rational";
	public static final String RATIONAL_WHOLE_PRINT_FORMAT = "$print-whole-format-rational";
	public static final String RATIONAL_WHOLE_FRAC_PRINT_FORMAT = "$print-whole-frac-format-rational";
	public static final String RATIONAL_NEG_FRAC_PRINT_FORMAT = "$print-frac-format-rational-neg";
	public static final String RATIONAL_NEG_WHOLE_PRINT_FORMAT = "$print-whole-format-rational-neg";
	public static final String RATIONAL_NEG_WHOLE_FRAC_PRINT_FORMAT = "$print-whole-frac-format-rational-neg";
	public static final String CHARACTER_PRINT_FORMAT = "$print-format-character";
	public static final String STRING_PRINT_FORMAT 	  = "$print-format-string";
	public static final String BOOLEAN_PRINT_FORMAT   = "$print-format-boolean";
	public static final String NEWLINE_PRINT_FORMAT   = "$print-format-newline";
	public static final String TAB_PRINT_FORMAT   	  = "$print-format-tab";
	public static final String SPACE_PRINT_FORMAT     = "$print-format-space";
	//public static final String LAMBDA_PRINT_STRING    = "$lambda-print-string";
	public static final String LAMBDA_REF_TEMP    	  = "$lambda-ref-temp";
	public static final String BOOLEAN_TRUE_STRING    = "$boolean-true-string";
	public static final String BOOLEAN_FALSE_STRING   = "$boolean-false-string";
	public static final String ARRAY_SEPARATOR_CHARACTER = "$array-separator-char";
	public static final String SPACE_CHARACTER 		  = "$space-char";
	public static final String ARRAY_OPENING_CHARACTER= "$array-opening-char";
	public static final String ARRAY_CLOSING_CHARACTER= "$array-closing-char";
	public static final String GLOBAL_MEMORY_BLOCK    = "$global-memory-block";
	public static final String USABLE_MEMORY_START    = "$usable-memory-start";
	public static final String MAIN_PROGRAM_LABEL     = "$$main";
	public static final String ARRAY_INDEXING_ARRAY   = "$a-indexing-array";
	public static final String ARRAY_INDEXING_ARRAY2  = "$a-indexing-array-2";
	public static final String ARRAY_INDEXING_INDEX   = "$a-indexing-index";
	public static final String STRING_INDEXING_PTR    = "$s-indexing-array";
	public static final String STRING_INDEXING_INDEX  = "$s-indexing-index";
	public static final String STRING_INDEXING_INDEX2 = "$s-indexing-index-2";
	public static final String LOWEST_TERMS   		  = "$lowest-terms";
	public static final String CLEAR_N_BYTES   		  = "$clear-n-bytes";
	public static final String POPULATE_N_BYTES   	  = "$populate-n-bytes";
	public static final String COPY_N_BYTES   		  = "$copy-n-bytes";
	public static final String COPY_STRING_BYTES   	  = "$copy-string-bytes";
	public static final String STASHED_RETURN_ADDRESS = "$stash-return-address";
	public static final String NUMERATOR_STORAGE   	  = "$r-numerator-storage";
	public static final String NUMERATOR_GCD_STORAGE  = "$r-numerator-gcd-storage";
	public static final String DENOMINATOR_STORAGE    = "$r-denominator-storage";
	public static final String DENOMINATOR_GCD_STORAGE= "$r-denominator-gcd-storage";
	public static final String GCD_STORAGE   		  = "$gcd-storage";
	public static final String EXPRESS_OVER_DENOMINATOR= "$r-express-over-denominator";
	public static final String RATIONAL_NUMERATOR_TEMP = "$r-rational-numerator-temp";
	public static final String RATIONAL_DENOMINATOR_TEMP = "$r-rational-denominator-temp";
	public static final String RATIONAL_ADDRESS_TEMP  	= "$r-rational-address-temp";
	public static final String RATIONAL_NUMERATOR_TEMP2 = "$r-rational-numerator-temp2";
	public static final String RATIONAL_DENOMINATOR_TEMP2 = "$r-rational-denominator-temp2";
	public static final String RECORD_CREATION_TEMPORARY = "$record-creation-temp";
	public static final String STRING_DATA_PTR_TEMPORARY = "$s-data-ptr-temp";
	public static final String STRING_DEST_PTR_TEMPORARY = "$s-source-ptr-temp";
	public static final String ARRAY_DATA_PTR_TEMPORARY = "$a-data-ptr-temp";
	public static final String ARRAY_DATA_PTR_TEMPORARY2 = "$a-data-ptr-temp2";
	public static final String ARRAY_LENGTH_TEMPORARY 	= "$a-array-length-temp";
	public static final String LENGTH_TEMPORARY 	   	= "$a-length-temp";
	public static final String STRING_LENGTH_TEMPORARY 	 = "$a-string-length-temp";
	public static final String STRING_TEMP_ADDR_STORAGE1 = "$a-string-addr-temp-1";
	public static final String STRING_TEMP_ADDR_STORAGE2 = "$a-string-addr-temp-2";
	public static final String TEMP_ADDR_STORAGE 		= "$a-addr-temp";
	public static final String TEMP_DATA_STORAGE 		= "$a-data-temp";
	public static final String CHARACTER_TEMP_STORAGE 	= "$a-character-temp";
	public static final String ARRAY_INDEX_TEMPORARY 	= "$a-array-index-temp";
	public static final String COUNTER_TEMPORARY  		= "$a-counter-temp";
	public static final String ARRAY_SUBTYPE_SIZE_TEMPORARY = "$a-subtype-size-temp";
	public static final String ARRAY_DATASIZE_TEMPORARY = "$a-array-datasize-temp";
	public static final String ARRAY_SOURCE_TEMPORARY 	= "$a-array-source-temp";
	public static final String ARRAY_DESTINATION_TEMPORARY = "$a-array-destination-temp";
	// Record constants 
	public static final int RECORD_TYPEID_OFFSET 	  = 0;
	public static final int RECORD_STATUS_OFFSET 	  = 4;
	public static final int ARRAY_TYPE_ID 	  		  = 7;
	public static final int STRING_TYPE_ID 	  		  = 6;
	// Array constants
	public static final int ARRAY_HEADER_SIZE 	      = 16;
	public static final int ARRAY_LENGTH_OFFSET 	  = 12;
	public static final int ARRAY_SUBTYPE_SIZE_OFFSET = 8;
	// String constants
	public static final int STRING_HEADER_SIZE 	      = 12;
	public static final int STRING_LENGTH_OFFSET 	  = 8;
	
	public static final String GENERAL_RUNTIME_ERROR = "$$general-runtime-error";
	public static final String INTEGER_DIVIDE_BY_ZERO_RUNTIME_ERROR = "$$i-divide-by-zero";
	public static final String FLOAT_DIVIDE_BY_ZERO_RUNTIME_ERROR = "$$f-divide-by-zero";
	public static final String RATIONAL_DIVIDE_BY_ZERO_RUNTIME_ERROR = "$$r-divide-by-zero";
	public static final String NULL_ARRAY_RUNTIME_ERROR = "$a-null-array";
	public static final String DIFFERENT_ARRAY_LENGTHS_RUNTIME_ERROR = "$a-different-lengths";
	public static final String ZERO_LENGTH_RUNTIME_ERROR = "$a-zero-length";
	public static final String NULL_STRING_RUNTIME_ERROR = "$a-null-string";
	public static final String DOUBLE_FREE_RUNTIME_ERROR = "$a-double-free";
	public static final String INDEX_OUT_OF_BOUNDS_RUNTIME_ERROR = "$a-index-out-of-bounds";
	public static final String EMPTY_SLICE_RUNTIME_ERROR = "$a-empty-string-slice";
	public static final String NEGATIVE_LENGTH_ARRAY_RUNTIME_ERROR = "$a-negative-length-array";
	
	private ASMCodeFragment environmentASM() {
		ASMCodeFragment result = new ASMCodeFragment(GENERATES_VOID);
		result.append(jumpToMain());
		result.append(lowestTermsSubroutine());
		result.append(ZeroOutElements());
		//result.append(CopyElements());
		result.append(CopyString());
		result.append(stringsForPrintf());
		result.append(runtimeErrors());
		result.append(temporaryVariables());	
		result.add(DLabel, USABLE_MEMORY_START);
		return result;
	}
	
	private ASMCodeFragment jumpToMain() {
		ASMCodeFragment frag = new ASMCodeFragment(GENERATES_VOID);
		frag.append(intializeGlobalVariables());
		frag.add(Jump, MAIN_PROGRAM_LABEL);
		return frag;
	}
	
	public ASMCodeFragment lowestTermsSubroutine() {
		ASMCodeFragment frag = new ASMCodeFragment(GENERATES_VALUE);
		
		frag.add(Label, LOWEST_TERMS); 				// [... num den ret_addr]
		Macros.storeITo(frag, STASHED_RETURN_ADDRESS);// [... num den]
		
		frag.add(Duplicate); 						// [... num den den]

		frag.add(JumpFalse, 						// [... num den]
				RATIONAL_DIVIDE_BY_ZERO_RUNTIME_ERROR); 
					
		Macros.storeITo(frag, DENOMINATOR_STORAGE); // [... num]
		Macros.storeITo(frag, NUMERATOR_STORAGE); 	// [...]
		
		frag.append(getGcd()); 					// [... gcd]
		Macros.storeITo(frag, GCD_STORAGE);			// [...]
		
		Macros.loadIFrom(frag, NUMERATOR_STORAGE); 	// [... num]
		Macros.loadIFrom(frag, GCD_STORAGE);		// [... num gcd]
		frag.add(Divide);							// [... num/gcd]
		
		Macros.loadIFrom(frag, DENOMINATOR_STORAGE);// [... num/gcd den]
		Macros.loadIFrom(frag, GCD_STORAGE);		// [... num/gcd den gcd]
		frag.add(Divide);							// [... num/gcd den/gcd]
		
		Macros.loadIFrom(frag, STASHED_RETURN_ADDRESS);// [... num/gcd den/gcd retaddr]
		frag.add(Return); 							// [... num/gcd den/gcd]
		return frag;
	}
	
	// [...] -> [... gcd]
	private ASMCodeFragment getGcd() {
		ASMCodeFragment frag = new ASMCodeFragment(GENERATES_VALUE);

		Macros.loadIFrom(frag, NUMERATOR_STORAGE);
		frag.add(JumpFalse, "setGCDtoOne"); // check if numerator is zero
		
		Macros.loadIFrom(frag, NUMERATOR_STORAGE);  // [... a]
		frag.add(Duplicate);// [... a a]
		frag.add(JumpPos, "storeNumerator");// [... a]
		frag.add(Negate); // turn negative into positive for gcd to work
		frag.add(Label, "storeNumerator");
		Macros.storeITo(frag, NUMERATOR_GCD_STORAGE); // [...]
		Macros.loadIFrom(frag, DENOMINATOR_STORAGE);// [... b]
		frag.add(Duplicate);// [... b b]
		frag.add(JumpPos, "storeDenominator");// [... b]
		frag.add(Negate); // turn negative into positive for gcd to work
		frag.add(Label, "storeDenominator"); 
		Macros.storeITo(frag, DENOMINATOR_GCD_STORAGE);// [...]
			
		frag.add(Label, "startGCDLoop");
		Macros.loadIFrom(frag, NUMERATOR_GCD_STORAGE);  // [... a]
		Macros.loadIFrom(frag, DENOMINATOR_GCD_STORAGE);// [... a b]
		
		frag.add(Subtract); 						// [... a-b]
		frag.add(JumpFalse, "exitGCDLoop"); 		// [...]
		Macros.loadIFrom(frag, NUMERATOR_GCD_STORAGE);  // [... a]
		Macros.loadIFrom(frag, DENOMINATOR_GCD_STORAGE);// [... a b]
		frag.add(Subtract); 						// [... a-b]
		frag.add(JumpNeg, "subtractAfromB"); 		// [...]
		
		// a = a - b  (a>b)
		Macros.loadIFrom(frag, NUMERATOR_GCD_STORAGE);  // [... a]
		Macros.loadIFrom(frag, DENOMINATOR_GCD_STORAGE);// [... b]
		frag.add(Subtract); 						// [... a-b]
		Macros.storeITo(frag, NUMERATOR_GCD_STORAGE);	// [...]
		frag.add(Jump, "startGCDLoop");
		
		// b = b - a  (b>a)
		frag.add(Label, "subtractAfromB"); 
		Macros.loadIFrom(frag, DENOMINATOR_GCD_STORAGE);// [... b]
		Macros.loadIFrom(frag, NUMERATOR_GCD_STORAGE);  // [... a]
		frag.add(Subtract);							// [... b-a]
		Macros.storeITo(frag, DENOMINATOR_GCD_STORAGE); // [...]
		frag.add(Jump, "startGCDLoop");
		
		frag.add(Label, "setGCDtoOne");
		frag.add(PushI, 1);
		frag.add(Jump, "exitGCD");
		
		frag.add(Label, "exitGCDLoop");
		Macros.loadIFrom(frag, NUMERATOR_GCD_STORAGE);
		
		frag.add(Label, "exitGCD");
		
		return frag;
	}

	private ASMCodeFragment stringsForPrintf() {
		ASMCodeFragment frag = new ASMCodeFragment(GENERATES_VOID);
		frag.add(DLabel, EAT_LOCATION_ZERO);
		frag.add(DataZ, 8);
		frag.add(DLabel, INTEGER_PRINT_FORMAT);
		frag.add(DataS, "%d");
		frag.add(DLabel, FLOATING_PRINT_FORMAT);
		frag.add(DataS, "%g");
		frag.add(DLabel, RATIONAL_FRAC_PRINT_FORMAT);
		frag.add(DataS, "_%d/%d");
		frag.add(DLabel, RATIONAL_WHOLE_PRINT_FORMAT);
		frag.add(DataS, "%d");
		frag.add(DLabel, RATIONAL_WHOLE_FRAC_PRINT_FORMAT);
		frag.add(DataS, "%d_%d/%d");
		frag.add(DLabel, RATIONAL_NEG_FRAC_PRINT_FORMAT);
		frag.add(DataS, "-_%d/%d");
		frag.add(DLabel, RATIONAL_NEG_WHOLE_PRINT_FORMAT);
		frag.add(DataS, "-%d");
		frag.add(DLabel, RATIONAL_NEG_WHOLE_FRAC_PRINT_FORMAT);
		frag.add(DataS, "-%d_%d/%d");
		frag.add(DLabel, STRING_PRINT_FORMAT);
		frag.add(DataS, "%s");
		frag.add(DLabel, CHARACTER_PRINT_FORMAT);
		frag.add(DataS, "%c");
		frag.add(DLabel, BOOLEAN_PRINT_FORMAT);
		frag.add(DataS, "%s");
		frag.add(DLabel, NEWLINE_PRINT_FORMAT);
		frag.add(DataS, "\n");
		frag.add(DLabel, TAB_PRINT_FORMAT);
		frag.add(DataS, "\t");
		frag.add(DLabel, SPACE_PRINT_FORMAT);
		frag.add(DataS, " ");
		/*frag.add(DLabel, LAMBDA_PRINT_STRING);
		frag.add(DataS, "<lambda>");*/
		frag.add(DLabel, BOOLEAN_TRUE_STRING);
		frag.add(DataS, "true");
		frag.add(DLabel, BOOLEAN_FALSE_STRING);
		frag.add(DataS, "false");
		frag.add(DLabel, ARRAY_SEPARATOR_CHARACTER);
		frag.add(DataS, ",");
		frag.add(DLabel, SPACE_CHARACTER);
		frag.add(DataS, " ");
		frag.add(DLabel, ARRAY_OPENING_CHARACTER);
		frag.add(DataS, "[");
		frag.add(DLabel, ARRAY_CLOSING_CHARACTER);
		frag.add(DataS, "]");
		return frag;
	}
	
	// leaves a new record in RECORD_CREATION_TEMPORARY
	// [... size] -> [...]
	public static void createRecord(ASMCodeFragment code, int typecode, int statusFlags) {
		code.add(Call, MemoryManager.MEM_MANAGER_ALLOCATE); // [... ptr]
		Macros.storeITo(code, RECORD_CREATION_TEMPORARY); // [...]
		Macros.writeIPBaseOffset(code, RECORD_CREATION_TEMPORARY,
					RECORD_TYPEID_OFFSET, typecode);
		Macros.writeIPBaseOffset(code, RECORD_CREATION_TEMPORARY,
					RECORD_STATUS_OFFSET, statusFlags);
	}
	
	// leaves new record in RECORD_CREATION_TEMPORARY
	// [... array] -> [...]
	public static void cloneArrayRecord(ASMCodeFragment frag, 
			int statusFlags, int subtypeSize, Type subType) {
													// [... arrayPtr]
		Macros.storeITo(frag, ARRAY_INDEXING_ARRAY);// [...]
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
		
		// Copy array 
		frag.add(PushI, 0);									// [... srcArrAddr 0]
		Macros.storeITo(frag, ARRAY_INDEXING_INDEX);		// [... srcArrAddr]
		
		Labeller labeller = new Labeller("cloneArray");
		String loopArrCopyStart = labeller.newLabel("loopStart");
		
		frag.add(Label, loopArrCopyStart);					// [... srcArrAddr]
		frag.add(Duplicate);								// [... srcArrAddr srcArrAddr]
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX);		// [... srcArrAddr srcArrAddr index]
		frag.add(PushI, subtypeSize);						// [... srcArrAddr srcArrAddr index typeSize]
		frag.add(Multiply);									// [... srcArrAddr srcArrAddr index*typeSize]
		frag.add(Add);										// [... srcArrAddr srcArrAddr+index*typeSize]
		frag.append(getLoadOpcode(subType));				// [... srcArrAddr *(srcArrAddr+elemOffSet)]
		//frag.add(PStack);     
		
		Macros.loadIFrom(frag, ARRAY_DATA_PTR_TEMPORARY);	// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr]
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX);		// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr index]
		frag.add(PushI, subtypeSize);						// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr index typeSize]
		frag.add(Multiply);									// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr index*typeSize]
		frag.add(Add);										// [... srcArrAddr *(srcArrAddr+elemOffSet) dstArrAddr+index*typeSize]
		frag.add(Exchange);									// [... srcArrAddr dstArrAddr+index*typeSize *(srcArrAddr+elemOffSet)]
		//frag.add(PStack);
		frag.append(getStoreOpcode(subType));				// [... srcArrAddr ]
		
		Macros.loadIFrom(frag, ARRAY_LENGTH_TEMPORARY); // [... srcArrAddr length]
		frag.add(PushI, 1);								// [... srcArrAddr length 1]
		frag.add(Subtract);								// [... srcArrAddr length-1]
		Macros.loadIFrom(frag, ARRAY_INDEXING_INDEX); 	// [... srcArrAddr length-1 index]
		frag.add(Subtract);								// [... srcArrAddr length-1-index]
		Macros.incrementInteger(frag, ARRAY_INDEXING_INDEX);// [... srcArrAddr length-1-index]
		frag.add(JumpPos, loopArrCopyStart);				// [... srcArrAddr]
		frag.add(Pop);										// [...]
		
		Macros.loadIFrom(frag, ARRAY_LENGTH_TEMPORARY); 	// [... length]
		Macros.writeIPtrOffset(frag, 						// [...]
				RECORD_CREATION_TEMPORARY,ARRAY_LENGTH_OFFSET);	
		
		Macros.loadIFrom(frag, RECORD_CREATION_TEMPORARY);
		//frag.add(PStack);
	}
	
	/*public static void cloneArrayRecord(ASMCodeFragment code, 
			int statusFlags, int subtypeSize) {
		
		final int typecode = ARRAY_TYPE_ID;					// [... arrayPtr]
		Macros.storeITo(code, ARRAY_SOURCE_TEMPORARY); 		// [...]
		
		Macros.loadIFrom(code,  ARRAY_SOURCE_TEMPORARY);	// [... arrayPtr]
		Macros.readIOffset(code, ARRAY_LENGTH_OFFSET);		// [... nElems]
		
		code.add(PushI, subtypeSize); // [... nElems subtypeSize]
		code.add(Multiply); // [... elemsSize]
		code.add(PushI, ARRAY_HEADER_SIZE);  // [elemsSize AHS]
		code.add(Add); // [... totalRecordSize]
		
		createRecord(code, typecode, statusFlags); // [...]
		
		Macros.loadIFrom(code,  RECORD_CREATION_TEMPORARY);	// [... dstPtr]
		code.add(PushI, ARRAY_HEADER_SIZE);					// [... dstPtr AHS]
		code.add(Add);										// [... dstElemsPtr]
		code.add(Call, COPY_N_BYTES);						// [...]
		Macros.loadIFrom(code,  ARRAY_SOURCE_TEMPORARY);
		Macros.readIOffset(code, ARRAY_LENGTH_OFFSET);		// [... length]
		Macros.writeIPtrOffset(code, 						// [...]
				RECORD_CREATION_TEMPORARY, ARRAY_LENGTH_OFFSET);	
		Macros.writeIPBaseOffset(code, 					 	// [...]
				RECORD_CREATION_TEMPORARY, ARRAY_SUBTYPE_SIZE_OFFSET, subtypeSize); 
		
		Macros.loadIFrom(code, RunTime.RECORD_CREATION_TEMPORARY); // [... ptrToArray]
	}*/
	
	/*// [ ... dstAddr] -> [...]
	public static ASMCodeFragment CopyElements() {
		ASMCodeFragment frag = new ASMCodeFragment(GENERATES_VOID);
		frag.add(Label, COPY_N_BYTES);	// [... dstAddr retaddr]
		Macros.storeITo(frag, STASHED_RETURN_ADDRESS);	// [... dstAddr]
		
		Macros.storeITo(frag, ARRAY_DESTINATION_TEMPORARY);	// [... dstAddr]
		Macros.loadIFrom(frag, ARRAY_DESTINATION_TEMPORARY);	// [... dstAddr]
		
		Macros.loadIFrom(frag,  ARRAY_SOURCE_TEMPORARY);	// [... dstAddr arrayPtr]
		Macros.readIOffset(frag, ARRAY_LENGTH_OFFSET);		// [... dstAddr nElems]
		
		// TODO: To make clone work, multiply index by type size,
		// then use getLoadOpcode() to get the correct loading instruction for the element type
		frag.add(Label, "copyOutElements_loopStart"); // [... dstAddr numBytes]
		frag.add(PushI, 1);							// [... dstAddr numBytes -1]
		frag.add(Subtract);								// [... dstAddr numBytes-1]
		
		Macros.storeITo(frag, ARRAY_DATASIZE_TEMPORARY);	// [... dstAddr]
		Macros.loadIFrom(frag, ARRAY_DATASIZE_TEMPORARY);	// [... dstAddr byteIndex]
		
		frag.add(Add);									// [... dstAddr+byteIndex]
		Macros.loadIFrom(frag,  ARRAY_SOURCE_TEMPORARY);	// [... dstAddr+offset srcPtr]
		frag.add(PushI, ARRAY_HEADER_SIZE);						// [... dstAddr+byteIndex srcPtr AHS]
		frag.add(Add);											// [... dstAddr+byteIndex srcElemsPtr]
		Macros.loadIFrom(frag, ARRAY_DATASIZE_TEMPORARY);		// [... dstAddr+byteIndex srcElemsPtr byteIndex]
		frag.add(Add);										// [... dstAddr+byteIndex srcElemsPtr+byteIndex]
		frag.add(LoadI);									// [... dstAddr+byteIndex *(srcElemsPtr+byteIndex)]
		frag.add(StoreI);									// [...]
		
		Macros.loadIFrom(frag, ARRAY_DESTINATION_TEMPORARY);	// [... dstAddr]
		Macros.decrementInteger(frag, ARRAY_DATASIZE_TEMPORARY); // [... dstAddr byteIndex-1]
		Macros.loadIFrom(frag, ARRAY_DATASIZE_TEMPORARY);		// [... dstAddr byteIndex-1]
		
		frag.add(Duplicate);									// [... dstAddr byteIndex-1 byteIndex-1]
		frag.add(JumpNeg, "copyOutElements_loopExit"); 			// [... dstAddr byteIndex-1]
		frag.add(Jump, "copyOutElements_loopStart");			// [... dstAddr byteIndex-1]
		
		frag.add(Label, "copyOutElements_loopExit"); 
		frag.add(Pop); // [...]
		Macros.loadIFrom(frag, STASHED_RETURN_ADDRESS); // [... retaddr]
		frag.add(Return); // [...]
		return frag;
	}*/
	
	// [... values...] -> [... arrayPtr]
	public static void createPopulatedArrayRecord(ASMCodeFragment code, 
			int statusFlags, int subtypeSize, int numElements, Type subType) {
		
		code.add(PushI, numElements); // [... values numElements]
		createEmptyArrayRecord(code, statusFlags, subtypeSize); // [... values]
		
		Macros.loadIFrom(code,  RECORD_CREATION_TEMPORARY); // [... values arrayPtr]
		code.add(PushI, ARRAY_HEADER_SIZE);					// [... values ptr AHS]
		code.add(Add);										// [... values elemsPtr]
		
		Macros.storeITo(code, ARRAY_DATA_PTR_TEMPORARY);	// [... values]
		
		code.add(PushI, subtypeSize);						// [... values subTypeSize]
		Macros.storeITo(code, ARRAY_SUBTYPE_SIZE_TEMPORARY);// [... values]
		// start index from Last element
		code.add(PushI, numElements); 						// [... values numElems]
		Macros.storeITo(code, ARRAY_INDEX_TEMPORARY);		// [... values]
		Macros.decrementInteger(code, ARRAY_INDEX_TEMPORARY);// [... values]
		
		Labeller labeller = new Labeller("populate");
		String exitLabel = labeller.newLabel("exit");
		String startLabel = labeller.newLabel("start");
		
		// Populate Array
		code.add(Label, startLabel); 						// [... values]
		
		Macros.loadIFrom(code, ARRAY_INDEX_TEMPORARY); 		// [... values index]
		Macros.loadIFrom(code, ARRAY_SUBTYPE_SIZE_TEMPORARY);// [... values index subtypeSize]
		code.add(Multiply);									// [... values index*subtypeSize]
		Macros.loadIFrom(code, ARRAY_DATA_PTR_TEMPORARY);	// [... values index*subtypeSize elemsPtr]
		code.add(Add);										// [... values index*subtypeSize+elemsPtr]
		code.add(Exchange);									// [... values Index*subtypeSize+elemsPtr value]
		
		code.append(getStoreOpcode(subType));				// [... values]
		
		Macros.decrementInteger(code, ARRAY_INDEX_TEMPORARY); // [... values]
		Macros.loadIFrom(code, ARRAY_INDEX_TEMPORARY); 		  // [... values index]
		 	
		code.add(JumpNeg, exitLabel); 			// [... values]
		code.add(Jump, startLabel);				// [... values]
		
		code.add(Label, exitLabel); 			// [...]
		
		Macros.loadIFrom(code,  RECORD_CREATION_TEMPORARY);	// [... ptrToArray]
	}

	// [... elemAddr value] -> [...]
	public static ASMCodeFragment getStoreOpcode(Type subType) {
		ASMCodeFragment code = new ASMCodeFragment(GENERATES_VOID);

		if(subType == PrimitiveType.BOOLEAN)
			code.add(StoreC);		// [...]
		else if(subType == PrimitiveType.CHARACTER)
			code.add(StoreC);		// [...]
		else if(subType == PrimitiveType.INTEGER)
			code.add(StoreI);		// [...]
		else if(subType == PrimitiveType.FLOATING)
			code.add(StoreF);		// [...]
		else if(subType == PrimitiveType.STRING)
			code.add(StoreI);		// [...]
		else if(subType == PrimitiveType.RATIONAL) { 
							 								// [... numerator elemAddr denominator]
			code.add(Exchange);								// [... numerator denominator elemAddr]
			code.add(Duplicate);							// [... numerator denominator elemAddr elemAddr]
			Macros.storeITo(code, RATIONAL_ADDRESS_TEMP); 	// [...  numerator denominator elemAddr]
			code.add(PushI, 4);								// [... numerator denominator elemAddr 4]
			code.add(Add);									// [... numerator denominator elemAddr+4]
			code.add(Exchange);								// [... numerator elemAddr+4 denominator ]
			code.add(StoreI);								// [... numerator]
			Macros.loadIFrom(code, RATIONAL_ADDRESS_TEMP); 	// [... numerator elemAddr]
			code.add(Exchange);								// [... elemAddr numerator]
			code.add(StoreI);								// [...]
		}
		else if(subType instanceof Array)
			code.add(StoreI);		// [... values]
		else {
			Macros.printString(code, "Error: Can not store unimplemented subtype in array");
			code.add(Halt);
		}
		return code;
	}
	
	// [... elemAddr] -> [... elemVal]
	public static ASMCodeFragment getLoadOpcode(Type subType) {
		ASMCodeFragment code = new ASMCodeFragment(GENERATES_VOID);
		
		if(subType == PrimitiveType.BOOLEAN)
			code.add(LoadC);		// [... elemVal]
		else if(subType == PrimitiveType.CHARACTER)
			code.add(LoadC);		// [... elemVal]
		else if(subType == PrimitiveType.INTEGER)
			code.add(LoadI);		// [... elemVal]
		else if(subType == PrimitiveType.FLOATING)
			code.add(LoadF);		// [... elemVal]
		else if(subType == PrimitiveType.STRING)
			code.add(LoadI);		// [... elemVal]
		else if(subType == PrimitiveType.RATIONAL) { 
									// [... elemAddr]
			code.add(Duplicate);	// [... elemAddr elemAddr]
			code.add(LoadI);		// [... elemAddr numerator]
			code.add(Exchange);		// [... numerator elemAddr]
			code.add(PushI, 4);		// [... numerator elemAddr 4]
			code.add(Add);			// [... numerator elemAddr+4]
			code.add(LoadI);		// [... numerator denominator]
		}
		else if(subType instanceof Array)
			code.add(LoadI);		// [... arrAddr]
		else if(subType instanceof Lambda)
			code.add(LoadI);		// [... lambdaRef]
		else {
			Macros.printString(code, "Error: Can not load unimplemented sub type");
			code.add(Halt);
		}
		return code;
	}
	
	// [ ... srcStrAddr dstStrAddr] -> [...]
	public static ASMCodeFragment CopyString() {
		ASMCodeFragment code = new ASMCodeFragment(GENERATES_VOID);
		code.add(Label, COPY_STRING_BYTES);					// [... srcStrAddr dstStrAddr retaddr]
		Macros.storeITo(code, STASHED_RETURN_ADDRESS);		// [... srcStrAddr dstStrAddr]
		
		Macros.storeITo(code, STRING_DEST_PTR_TEMPORARY); 	// [... srcStrAddr]
		code.add(PushI, 0);									// [... srcStrAddr 0]
		Macros.storeITo(code, STRING_INDEXING_INDEX);		// [... srcStrAddr]
		
		Labeller labeller = new Labeller("substring");
		String loopStrCopyStart = labeller.newLabel("loopStart");
		
		code.add(Label, loopStrCopyStart);					// [... srcStrAddr]
		code.add(Duplicate);								// [... srcStrAddr srcStrAddr]
		Macros.loadIFrom(code, STRING_INDEXING_INDEX);		// [... srcStrAddr srcStrAddr index]
		code.add(Add);										// [... srcStrAddr srcStrAddr+index ]
		code.add(LoadC);									// [... srcStrAddr *(srcStrAddr+index)]
		
		Macros.loadIFrom(code, STRING_DEST_PTR_TEMPORARY);	// [... srcStrAddr *(srcStrAddr+index) dstStrAddr]
		Macros.loadIFrom(code, STRING_INDEXING_INDEX);	// [... srcStrAddr *(srcStrAddr+index) dstStrAddr index]
		code.add(Add);									// [... srcStrAddr *(srcStrAddr+index) dstStrAddr+index]
		code.add(Exchange);								// [... srcStrAddr dstStrAddr+index *(srcStrAddr+index) ]
		code.add(StoreC);								// [... srcStrAddr ]
		
		Macros.loadIFrom(code, STRING_INDEXING_INDEX); // [... srcStrAddr index ]
		Macros.loadIFrom(code, STRING_LENGTH_TEMPORARY); // [... srcStrAddr index length]
		code.add(PushI, 1);								 // [... srcStrAddr index length 1]
		code.add(Subtract);								 // [... srcStrAddr index length-1]
		code.add(Subtract);								 // [... srcStrAddr index-length-1]
		Macros.incrementInteger(code, STRING_INDEXING_INDEX);// [... srcStrAddr index-length-1]
		code.add(JumpNeg, loopStrCopyStart);				// [... srcStrAddr]
		code.add(Pop);										// [...]
		
		code.add(PushI, '\0');								// [... \0]
		Macros.loadIFrom(code, STRING_DEST_PTR_TEMPORARY);	// [... \0 dstStrAddr]
		Macros.loadIFrom(code, STRING_INDEXING_INDEX);		// [... \0 dstStrAddr index]
		code.add(Add);										// [... \0 dstStrAddr+index]
		code.add(Exchange);									// [... dstStrAddr+index \0 ]
		code.add(StoreC);									// [... ]
		
		Macros.loadIFrom(code, STASHED_RETURN_ADDRESS); // [... retaddr]
		code.add(Return); // [...]
		return code;
	}
		
	
	// [... strPtr1 strPtr2] -> [... strPtrResult]
	public static void concatenateTwoStrings(ASMCodeFragment code, int statusFlags) {
		final int typecode = STRING_TYPE_ID;
		Macros.storeITo(code, STRING_TEMP_ADDR_STORAGE2);
		Macros.loadIFrom(code, STRING_TEMP_ADDR_STORAGE2);
		
		code.add(PushI, STRING_LENGTH_OFFSET); 	//[... strPtr1 strPtr2 lengthOffset]
		code.add(Add);							//[... strPtr1 strPtr2+lengthOffset]
		code.add(LoadI);						//[... strPtr1 str2Length]
		
		code.add(Exchange);						//[... str2Length strPtr1]
		Macros.storeITo(code, STRING_TEMP_ADDR_STORAGE1);
		Macros.loadIFrom(code, STRING_TEMP_ADDR_STORAGE1);
		
		code.add(PushI, STRING_LENGTH_OFFSET); 	//[... str2Length strPtr1 lengthOffset]
		code.add(Add);				 			//[... str2Length strPtr1+lengthOffset]
		code.add(LoadI);						//[... str2Length str1Length]
		code.add(Add);							//[... twoStrsLength]
		Macros.storeITo(code, STRING_LENGTH_TEMPORARY);
		Macros.loadIFrom(code, STRING_LENGTH_TEMPORARY);
		
		code.add(PushI, STRING_HEADER_SIZE); 	// [... length SHS]
		code.add(Add); 							// [... totalRecordSize]
		code.add(PushI, 1);						// [... totalRecordSize 1] (add space for null byte)
		code.add(Add);							// [... totalRecordSize+1]
		
		createRecord(code, typecode, statusFlags); // [...]
		
		Macros.loadIFrom(code,  RECORD_CREATION_TEMPORARY);	// [... ptr]
		code.add(PushI, STRING_HEADER_SIZE);				// [... ptr SHS]
		code.add(Add);										// [... elemsPtr]
		Macros.storeITo(code, STRING_DATA_PTR_TEMPORARY);	// [...]
		
		Macros.loadIFrom(code, STRING_TEMP_ADDR_STORAGE1);	// [... srcAddr]
		code.add(PushI, STRING_HEADER_SIZE);				// [... srcAddr SHS]
		code.add(Add);										// [... srcAddr+SHS]
		Macros.loadIFrom(code, STRING_DATA_PTR_TEMPORARY);	// [... srcStrAddr dstStrAddr]
		
		code.add(Call, COPY_STRING_BYTES);					// [...]
		
		Macros.loadIFrom(code, STRING_TEMP_ADDR_STORAGE2);	// [... srcAddr]
		code.add(PushI, STRING_HEADER_SIZE);				// [... srcAddr SHS]
		code.add(Add);										// [... srcAddr+SHS]
		Macros.loadIFrom(code, STRING_DATA_PTR_TEMPORARY);	// [... srcStrAddr dstStrAddr]
		
		Macros.loadIFrom(code, STRING_TEMP_ADDR_STORAGE1);	// [... srcStrAddr dstStrAddr srcAddr]
		Macros.readIOffset(code, STRING_LENGTH_OFFSET);		// [... srcStrAddr dstStrAddr Str1length]
		code.add(Add);										// [... srcStrAddr dstStrAddr+Str1length]
		code.add(Call, COPY_STRING_BYTES);					// [...]
		
		Macros.loadIFrom(code, STRING_LENGTH_TEMPORARY); 	// [... length]
		Macros.writeIPtrOffset(code, 						// [...]
				RECORD_CREATION_TEMPORARY, STRING_LENGTH_OFFSET);	
		
		code.add(PushD,  STRING_DATA_PTR_TEMPORARY);
	}
	
	// [... ptrToSubstringStart length] -> [... addressToNewStringRecord]
	public static void createSubstringRecord(ASMCodeFragment code, int statusFlags) {
		final int typecode = STRING_TYPE_ID;
											// [... ptrToSubstr length]
		Macros.storeITo(code, STRING_LENGTH_TEMPORARY);
		Macros.loadIFrom(code, STRING_LENGTH_TEMPORARY);
		code.add(PushI, STRING_HEADER_SIZE); // [... ptrToSubstr length SHS]
		code.add(Add); // [... ptrToSubstr totalRecordSize]
		code.add(PushI, 1);	// [... ptrToSubstr totalRecordSize 1] (add space for null byte)
		code.add(Add);	// [... ptrToSubstr totalRecordSize+1]
		
		createRecord(code, typecode, statusFlags); // [... ptrToSubstr]
		
		Macros.loadIFrom(code,  RECORD_CREATION_TEMPORARY);	// [... ptrToSubstr ptr]
		code.add(PushI, STRING_HEADER_SIZE);				// [... ptrToSubstr ptr SHS]
		code.add(Add);										// [... ptrToSubstr elemsPtr]
		Macros.storeITo(code, STRING_DATA_PTR_TEMPORARY);	// [... ptrToSubstr]
		
		code.add(PushI, 0);									// [... ptrToSubstr 0]
		Macros.storeITo(code, STRING_INDEXING_INDEX);		// [... ptrToSubstr]
		
		Labeller labeller = new Labeller("substring");
		String loopStrCopyStart = labeller.newLabel("loopStart");
		
		code.add(Label, loopStrCopyStart);					// [... ptrToSubstr]
		code.add(Duplicate);								// [... ptrToSubstr ptrToSubstr]
		Macros.loadIFrom(code, STRING_INDEXING_INDEX);		// [... ptrToSubstr ptrToSubstr index]
		code.add(Add);										// [... ptrToSubstr ptrToSubstr+index ]
		code.add(LoadC);									// [... ptrToSubstr *(ptrToSubstr+index)]
		
		Macros.loadIFrom(code, STRING_DATA_PTR_TEMPORARY);	// [... ptrToSubstr *(ptrToSubstr+index) elemsPtr]
		Macros.loadIFrom(code, STRING_INDEXING_INDEX);	// [... ptrToSubstr *(ptrToSubstr+index) elemsPtr index]
		code.add(Add);									// [... ptrToSubstr *(ptrToSubstr+index) elemsPtr+index]
		code.add(Exchange);								// [... ptrToSubstr elemsPtr+index *(ptrToSubstr+index) ]
		code.add(StoreC);								// [... ptrToSubstr ]
		
		Macros.loadIFrom(code, STRING_INDEXING_INDEX); // [... ptrToSubstr index ]
		Macros.loadIFrom(code, STRING_LENGTH_TEMPORARY); // [... ptrToSubstr index length]
		code.add(PushI, 1);								 // [... ptrToSubstr index length 1]
		code.add(Subtract);								 // [... ptrToSubstr index length-1]
		code.add(Subtract);								 // [... ptrToSubstr index-length-1]
		Macros.incrementInteger(code, STRING_INDEXING_INDEX);// [... ptrToSubstr index-length-1]
		code.add(JumpNeg, loopStrCopyStart);				// [... ptrToSubstr]
		code.add(Pop);										// [...]
		
		code.add(PushI, '\0');								// [... \0]
		Macros.loadIFrom(code, STRING_DATA_PTR_TEMPORARY);	// [... \0 elemsPtr]
		Macros.loadIFrom(code, STRING_INDEXING_INDEX);		// [... \0 elemsPtr index]
		code.add(Add);										// [... \0 elemsPtr+index]
		code.add(Exchange);									// [... elemsPtr+index \0 ]
		code.add(StoreC);									// [... ]
		
		Macros.loadIFrom(code, STRING_LENGTH_TEMPORARY); 	// [... length]
		Macros.writeIPtrOffset(code, 						// [...]
				RECORD_CREATION_TEMPORARY, STRING_LENGTH_OFFSET);	
		
		Macros.loadIFrom(code, STRING_DATA_PTR_TEMPORARY);
		//code.add(PushD, STRING_DATA_PTR_TEMPORARY);	// [... ptrToCharacters]
	}

	// leaves new record in RECORD_CREATION_TEMPORARY
	// [...] -> [... address]
	public static void createStringRecord(ASMCodeFragment code, 
			int statusFlags, String stringValue) {
		final int typecode = STRING_TYPE_ID;
		
		code.add(PushI, stringValue.length());
		code.add(PushI, STRING_HEADER_SIZE); // [... SHS]
		code.add(Add); // [... totalRecordSize]
		code.add(PushI, 1);	// [... totalRecordSize 1] (add space for null byte)
		code.add(Add);	// [... totalRecordSize+1]
		
		createRecord(code, typecode, statusFlags); // [...]
		
		Macros.loadIFrom(code,  RECORD_CREATION_TEMPORARY);	// [... ptr]
		code.add(PushI, STRING_HEADER_SIZE);				// [... ptr SHS]
		code.add(Add);										// [... elemsPtr]
		Macros.storeITo(code, STRING_DATA_PTR_TEMPORARY);	// [...]
		
		int offset = 0;
		for(char ch: stringValue.toCharArray()) {
			code.add(PushI, ch);							// [... charToWrite]
			Macros.loadIFrom(code, STRING_DATA_PTR_TEMPORARY);// [... charToWrite elemsPtr]
			Macros.writeCOffset(code, offset);				// [...]
			offset++;
		}	
		code.add(PushI, '\0');								// [... \0]
		Macros.loadIFrom(code, STRING_DATA_PTR_TEMPORARY);	// [... charToWrite elemsPtr]
		Macros.writeCOffset(code, offset);					// [...]
		
		code.add(PushI, stringValue.length()); 				// [... length]
		Macros.writeIPtrOffset(code, 						// [...]
				RECORD_CREATION_TEMPORARY, STRING_LENGTH_OFFSET);	
		
		code.add(PushD, STRING_DATA_PTR_TEMPORARY);	// [... ptrToCharacters]
		//code.add(PStack);
	}
		
	// leaves new record in RECORD_CREATION_TEMPORARY
	// [... nElems] -> [...]
	public static void createEmptyArrayRecord(ASMCodeFragment code, 
			int statusFlags, int subtypeSize) {
		final int typecode = ARRAY_TYPE_ID;
		
		code.add(Duplicate); // [... nElems nElems]
		code.add(JumpNeg, RunTime.NEGATIVE_LENGTH_ARRAY_RUNTIME_ERROR); // [... nElems]
		
		code.add(Duplicate); // [... nElems nElems]
		code.add(PushI, subtypeSize); // [... nElems nElems subSize]
		code.add(Multiply); // [... nElems elemsSize]
		code.add(Duplicate); // [... nElems elemsSize elemsSize]
		Macros.storeITo(code, ARRAY_DATASIZE_TEMPORARY); // [... nElems elemsSize]
		code.add(PushI, ARRAY_HEADER_SIZE); // [... nElems elemsSize AHS]
		code.add(Add); // [... nElems totalRecordSize]
		
		createRecord(code, typecode, statusFlags); // [... nElems]
		
		Macros.loadIFrom(code,  RECORD_CREATION_TEMPORARY);	// [... nElems ptr]
		code.add(PushI, ARRAY_HEADER_SIZE);					// [... nElems ptr AHS]
		code.add(Add);										// [... nElems elemsPtr]
		Macros.loadIFrom(code, ARRAY_DATASIZE_TEMPORARY); 	// [... nElems elemsPtr elemsSize]
		code.add(Call, CLEAR_N_BYTES);						// [... nElems]
		Macros.writeIPBaseOffset(code, 					 	// [... nElems]
				RECORD_CREATION_TEMPORARY, ARRAY_SUBTYPE_SIZE_OFFSET, subtypeSize); 
		Macros.writeIPtrOffset(code, 						// [...]
				RECORD_CREATION_TEMPORARY, ARRAY_LENGTH_OFFSET);			
	}
	
	// [ ... elementsBaseAddr numBytes] -> [...]
	public static ASMCodeFragment ZeroOutElements() {
		ASMCodeFragment frag = new ASMCodeFragment(GENERATES_VOID);
		frag.add(Label, CLEAR_N_BYTES);				// [... baseAddr numBytes retaddr]
		Macros.storeITo(frag, STASHED_RETURN_ADDRESS);	// [... baseAddr numBytes]
		
		frag.add(Label, "zeroOutElements_loopStart");	// [... baseAddr numBytes]
		frag.add(PushI, 1);								// [... baseAddr byteIndex -1]
		frag.add(Subtract);								// [... baseAddr byteIndex-1]
		
		frag.add(Add);									// [... baseAddr+offset]
		frag.add(PushI, 0);								// [... baseAddr+offset 0]
		frag.add(StoreC);								// [...]
		
		Macros.loadIFrom(frag,  RECORD_CREATION_TEMPORARY);	// [... ptr]
		frag.add(PushI, ARRAY_HEADER_SIZE);					// [... ptr AHS]
		frag.add(Add);										// [... baseAddr]
		Macros.decrementInteger(frag, ARRAY_DATASIZE_TEMPORARY); // [... baseAddr]
		Macros.loadIFrom(frag, ARRAY_DATASIZE_TEMPORARY);		// [... baseAddr byteIndex-1]
		 	
		frag.add(Duplicate);									// [... baseAddr byteIndex-1 byteIndex-1]
		frag.add(JumpNeg, "zeroOutElements_loopExit"); 			// [... baseAddr byteIndex-1]
		frag.add(Jump, "zeroOutElements_loopStart");
		
		frag.add(Label, "zeroOutElements_loopExit"); // [... baseAddr -1]
		frag.add(Pop); // [... baseAddr]
		frag.add(Pop); // [...]
		Macros.loadIFrom(frag, STASHED_RETURN_ADDRESS); // [... retaddr]
		frag.add(Return); // [...]
		return frag;
	}
	
	private ASMCodeFragment runtimeErrors() {
		ASMCodeFragment frag = new ASMCodeFragment(GENERATES_VOID);
		
		generalRuntimeError(frag);
		integerDivideByZeroError(frag);
		floatDivideByZeroError(frag);
		deletedRecordError(frag);
		indexOutOfBoundsError(frag);
		emptySliceError(frag);
		negativeLengthArrayError(frag);
		nullArrayError(frag);
		zeroLengthError(frag);
		differentArrayLengthsError(frag);
		nullStringError(frag);
		rationalDivideByZeroError(frag);
		
		return frag;
	}
	private ASMCodeFragment generalRuntimeError(ASMCodeFragment frag) {
		String generalErrorMessage = "$errors-general-message";

		frag.add(DLabel, generalErrorMessage);
		frag.add(DataS, "Runtime error: %s\n");
		
		frag.add(Label, GENERAL_RUNTIME_ERROR);
		frag.add(PushD, generalErrorMessage);
		frag.add(Printf);
		frag.add(Halt);
		return frag;
	}
	private void integerDivideByZeroError(ASMCodeFragment frag) {
		String intDivideByZeroMessage = "$errors-int-divide-by-zero";
		
		frag.add(DLabel, intDivideByZeroMessage);
		frag.add(DataS, "integer divide by zero");
		
		frag.add(Label, INTEGER_DIVIDE_BY_ZERO_RUNTIME_ERROR);
		frag.add(PushD, intDivideByZeroMessage);
		frag.add(Jump, GENERAL_RUNTIME_ERROR);
	}
	
	private void rationalDivideByZeroError(ASMCodeFragment frag) {
		String rationalDivideByZeroMessage = "$errors-rational-divide-by-zero";
		
		frag.add(DLabel, rationalDivideByZeroMessage);
		frag.add(DataS, "rational divide by zero");
		
		frag.add(Label, RATIONAL_DIVIDE_BY_ZERO_RUNTIME_ERROR);
		frag.add(PushD, rationalDivideByZeroMessage);
		frag.add(Jump, GENERAL_RUNTIME_ERROR);
	}
	
	private void floatDivideByZeroError(ASMCodeFragment frag) {
		String floatDivideByZeroMessage = "$errors-float-divide-by-zero";
		
		frag.add(DLabel, floatDivideByZeroMessage);
		frag.add(DataS, "float divide by zero");
		
		frag.add(Label, FLOAT_DIVIDE_BY_ZERO_RUNTIME_ERROR);
		frag.add(PushD, floatDivideByZeroMessage);
		frag.add(Jump, GENERAL_RUNTIME_ERROR);
	}
	
	private void deletedRecordError(ASMCodeFragment frag) {
		String deletedRecordMessage = "$errors-deleted-record";
		
		frag.add(DLabel, deletedRecordMessage);
		frag.add(DataS, "record is already deallocated");
		
		frag.add(Label, DOUBLE_FREE_RUNTIME_ERROR);
		frag.add(PushD, deletedRecordMessage);
		frag.add(Jump, GENERAL_RUNTIME_ERROR);
	}
	
	private void nullArrayError(ASMCodeFragment frag) {
		String nullArrayMessage = "$errors-null-array";
		
		frag.add(DLabel, nullArrayMessage);
		frag.add(DataS, "array is null");
		
		frag.add(Label, NULL_ARRAY_RUNTIME_ERROR);
		frag.add(PushD, nullArrayMessage);
		frag.add(Jump, GENERAL_RUNTIME_ERROR);
	}
	
	private void differentArrayLengthsError(ASMCodeFragment frag) {
		String differentArrayLengthsMessage = "$errors-different-array-lengths";
		
		frag.add(DLabel, differentArrayLengthsMessage);
		frag.add(DataS, "array lengths must be the same");
		
		frag.add(Label, DIFFERENT_ARRAY_LENGTHS_RUNTIME_ERROR);
		frag.add(PushD, differentArrayLengthsMessage);
		frag.add(Jump, GENERAL_RUNTIME_ERROR);
	}
	
	private void zeroLengthError(ASMCodeFragment frag) {
		String zeroLengthMessage = "$errors-zero-length";
		
		frag.add(DLabel, zeroLengthMessage);
		frag.add(DataS, "Array length is zero");
		
		frag.add(Label, ZERO_LENGTH_RUNTIME_ERROR);
		frag.add(PushD, zeroLengthMessage);
		frag.add(Jump, GENERAL_RUNTIME_ERROR);
	}
	
	private void nullStringError(ASMCodeFragment frag) {
		String nullStringMessage = "$errors-null-string";
		
		frag.add(DLabel, nullStringMessage);
		frag.add(DataS, "string is null");
		
		frag.add(Label, NULL_STRING_RUNTIME_ERROR);
		frag.add(PushD, nullStringMessage);
		frag.add(Jump, GENERAL_RUNTIME_ERROR);
	}
	
	private void indexOutOfBoundsError(ASMCodeFragment frag) {
		String indexOutOfBoundsMessage = "$errors-index-out-of-bounds";
		
		frag.add(DLabel, indexOutOfBoundsMessage);
		frag.add(DataS, "index is out of bounds");
		
		frag.add(Label, INDEX_OUT_OF_BOUNDS_RUNTIME_ERROR);
		frag.add(PushD, indexOutOfBoundsMessage);
		frag.add(Jump, GENERAL_RUNTIME_ERROR);
	}
	
	private void emptySliceError(ASMCodeFragment frag) {
		String emptySliceMessage = "$errors-empty-slice";
		
		frag.add(DLabel, emptySliceMessage);
		frag.add(DataS, "slice range is invalid");
		
		frag.add(Label, EMPTY_SLICE_RUNTIME_ERROR);
		frag.add(PushD, emptySliceMessage);
		frag.add(Jump, GENERAL_RUNTIME_ERROR);
	}
	
	private void negativeLengthArrayError(ASMCodeFragment frag) {
		String negativeLengthArray = "$errors-negative-length-array";
		
		frag.add(DLabel, negativeLengthArray);
		frag.add(DataS, "array length is negative");
		
		frag.add(Label, NEGATIVE_LENGTH_ARRAY_RUNTIME_ERROR);
		frag.add(PushD, negativeLengthArray);
		frag.add(Jump, GENERAL_RUNTIME_ERROR);
	}
	
	private ASMCodeFragment intializeGlobalVariables() {
		ASMCodeFragment frag = new ASMCodeFragment(GENERATES_VOID);
		frag.add(Memtop);
		Macros.storeITo(frag, FRAME_POINTER);
		frag.add(Memtop);
		Macros.storeITo(frag, STACK_POINTER);
		return frag;
	}
	
	private ASMCodeFragment temporaryVariables() {
		ASMCodeFragment frag = new ASMCodeFragment(GENERATES_VOID);
		Macros.declareI(frag, FRAME_POINTER);
		Macros.declareI(frag, STACK_POINTER);
		Macros.declareI(frag, ARRAY_INDEXING_ARRAY);
		Macros.declareI(frag, ARRAY_INDEXING_ARRAY2);
		Macros.declareI(frag, ARRAY_INDEXING_INDEX);
		Macros.declareI(frag, STRING_INDEXING_PTR);
		Macros.declareI(frag, STRING_INDEXING_INDEX);
		Macros.declareI(frag, STRING_INDEXING_INDEX2);
		Macros.declareI(frag, STASHED_RETURN_ADDRESS);
		Macros.declareI(frag, NUMERATOR_STORAGE);
		Macros.declareI(frag, NUMERATOR_GCD_STORAGE);
		Macros.declareI(frag, DENOMINATOR_STORAGE);
		Macros.declareI(frag, DENOMINATOR_GCD_STORAGE);
		Macros.declareI(frag, GCD_STORAGE);
		Macros.declareI(frag, EXPRESS_OVER_DENOMINATOR);
		Macros.declareI(frag, RATIONAL_DENOMINATOR_TEMP);
		Macros.declareI(frag, RATIONAL_ADDRESS_TEMP);
		Macros.declareI(frag, RATIONAL_DENOMINATOR_TEMP2);
		Macros.declareI(frag, LAMBDA_REF_TEMP);
		Macros.declareI(frag, RATIONAL_NUMERATOR_TEMP);
		Macros.declareI(frag, RATIONAL_NUMERATOR_TEMP2);
		Macros.declareI(frag, RECORD_CREATION_TEMPORARY);
		Macros.declareI(frag, STRING_DATA_PTR_TEMPORARY);
		Macros.declareI(frag, STRING_DEST_PTR_TEMPORARY);
		Macros.declareI(frag, ARRAY_DATA_PTR_TEMPORARY);
		Macros.declareI(frag, ARRAY_DATA_PTR_TEMPORARY2);
		Macros.declareI(frag, ARRAY_LENGTH_TEMPORARY);
		Macros.declareI(frag, LENGTH_TEMPORARY);
		Macros.declareI(frag, STRING_TEMP_ADDR_STORAGE1);
		Macros.declareI(frag, STRING_TEMP_ADDR_STORAGE2);
		Macros.declareI(frag, TEMP_ADDR_STORAGE);
		Macros.declareI(frag, TEMP_DATA_STORAGE);
		Macros.declareI(frag, CHARACTER_TEMP_STORAGE);
		Macros.declareI(frag, STRING_LENGTH_TEMPORARY);
		Macros.declareI(frag, ARRAY_INDEX_TEMPORARY);
		Macros.declareI(frag, COUNTER_TEMPORARY);
		Macros.declareI(frag, ARRAY_SUBTYPE_SIZE_TEMPORARY);
		Macros.declareI(frag, ARRAY_SOURCE_TEMPORARY);
		Macros.declareI(frag, ARRAY_DATASIZE_TEMPORARY);
		Macros.declareI(frag, ARRAY_DESTINATION_TEMPORARY);
		return frag;
	}
	
	public static ASMCodeFragment getEnvironment() {
		RunTime rt = new RunTime();
		return rt.environmentASM();
	}
}
