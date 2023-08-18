        Label        -mem-manager-initialize   
        DLabel       $heap-start-ptr           
        DataZ        4                         
        DLabel       $heap-after-ptr           
        DataZ        4                         
        DLabel       $heap-first-free          
        DataZ        4                         
        DLabel       $mmgr-newblock-block      
        DataZ        4                         
        DLabel       $mmgr-newblock-size       
        DataZ        4                         
        PushD        $heap-memory              
        Duplicate                              
        PushD        $heap-start-ptr           
        Exchange                               
        StoreI                                 
        PushD        $heap-after-ptr           
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $heap-first-free          
        Exchange                               
        StoreI                                 
        Memtop                                 
        PushD        $frame-pointer            
        Exchange                               
        StoreI                                 
        Memtop                                 
        PushD        $stack-pointer            
        Exchange                               
        StoreI                                 
        Jump         $$main                    
        Label        $lowest-terms             
        PushD        $stash-return-address     
        Exchange                               
        StoreI                                 
        Duplicate                              
        JumpFalse    $$r-divide-by-zero        
        PushD        $r-denominator-storage    
        Exchange                               
        StoreI                                 
        PushD        $r-numerator-storage      
        Exchange                               
        StoreI                                 
        PushD        $r-numerator-storage      
        LoadI                                  
        JumpFalse    setGCDtoOne               
        PushD        $r-numerator-storage      
        LoadI                                  
        Duplicate                              
        JumpPos      storeNumerator            
        Negate                                 
        Label        storeNumerator            
        PushD        $r-numerator-gcd-storage  
        Exchange                               
        StoreI                                 
        PushD        $r-denominator-storage    
        LoadI                                  
        Duplicate                              
        JumpPos      storeDenominator          
        Negate                                 
        Label        storeDenominator          
        PushD        $r-denominator-gcd-storage 
        Exchange                               
        StoreI                                 
        Label        startGCDLoop              
        PushD        $r-numerator-gcd-storage  
        LoadI                                  
        PushD        $r-denominator-gcd-storage 
        LoadI                                  
        Subtract                               
        JumpFalse    exitGCDLoop               
        PushD        $r-numerator-gcd-storage  
        LoadI                                  
        PushD        $r-denominator-gcd-storage 
        LoadI                                  
        Subtract                               
        JumpNeg      subtractAfromB            
        PushD        $r-numerator-gcd-storage  
        LoadI                                  
        PushD        $r-denominator-gcd-storage 
        LoadI                                  
        Subtract                               
        PushD        $r-numerator-gcd-storage  
        Exchange                               
        StoreI                                 
        Jump         startGCDLoop              
        Label        subtractAfromB            
        PushD        $r-denominator-gcd-storage 
        LoadI                                  
        PushD        $r-numerator-gcd-storage  
        LoadI                                  
        Subtract                               
        PushD        $r-denominator-gcd-storage 
        Exchange                               
        StoreI                                 
        Jump         startGCDLoop              
        Label        setGCDtoOne               
        PushI        1                         
        Jump         exitGCD                   
        Label        exitGCDLoop               
        PushD        $r-numerator-gcd-storage  
        LoadI                                  
        Label        exitGCD                   
        PushD        $gcd-storage              
        Exchange                               
        StoreI                                 
        PushD        $r-numerator-storage      
        LoadI                                  
        PushD        $gcd-storage              
        LoadI                                  
        Divide                                 
        PushD        $r-denominator-storage    
        LoadI                                  
        PushD        $gcd-storage              
        LoadI                                  
        Divide                                 
        PushD        $stash-return-address     
        LoadI                                  
        Return                                 
        Label        $clear-n-bytes            
        PushD        $stash-return-address     
        Exchange                               
        StoreI                                 
        Label        zeroOutElements_loopStart 
        PushI        1                         
        Subtract                               
        Add                                    
        PushI        0                         
        StoreC                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushI        -1                        
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Add                                    
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Duplicate                              
        JumpNeg      zeroOutElements_loopExit  
        Jump         zeroOutElements_loopStart 
        Label        zeroOutElements_loopExit  
        Pop                                    
        Pop                                    
        PushD        $stash-return-address     
        LoadI                                  
        Return                                 
        Label        $copy-n-bytes             
        PushD        $stash-return-address     
        Exchange                               
        StoreI                                 
        PushD        $a-array-destination-temp 
        Exchange                               
        StoreI                                 
        PushD        $a-array-destination-temp 
        LoadI                                  
        PushD        $a-array-source-temp      
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        Label        copyOutElements_loopStart 
        PushI        1                         
        Subtract                               
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Add                                    
        PushD        $a-array-source-temp      
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Add                                    
        LoadI                                  
        StoreI                                 
        PushD        $a-array-destination-temp 
        LoadI                                  
        PushI        -1                        
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Add                                    
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Duplicate                              
        JumpNeg      copyOutElements_loopExit  
        Jump         copyOutElements_loopStart 
        Label        copyOutElements_loopExit  
        Pop                                    
        PushD        $stash-return-address     
        LoadI                                  
        Return                                 
        Label        $copy-string-bytes        
        PushD        $stash-return-address     
        Exchange                               
        StoreI                                 
        PushD        $s-source-ptr-temp        
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        Label        -substring-1-loopStart    
        Duplicate                              
        PushD        $a-indexing-index         
        LoadI                                  
        Add                                    
        LoadC                                  
        PushD        $s-source-ptr-temp        
        LoadI                                  
        PushD        $a-indexing-index         
        LoadI                                  
        Add                                    
        Exchange                               
        StoreC                                 
        PushD        $a-indexing-index         
        LoadI                                  
        PushD        $a-string-length-temp     
        LoadI                                  
        PushI        1                         
        Subtract                               
        Subtract                               
        PushI        1                         
        PushD        $a-indexing-index         
        LoadI                                  
        Add                                    
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        JumpNeg      -substring-1-loopStart    
        Pop                                    
        PushI        0                         
        PushD        $s-source-ptr-temp        
        LoadI                                  
        PushD        $a-indexing-index         
        LoadI                                  
        Add                                    
        Exchange                               
        StoreC                                 
        PushD        $stash-return-address     
        LoadI                                  
        Return                                 
        DLabel       $eat-location-zero        
        DataZ        8                         
        DLabel       $print-format-integer     
        DataC        37                        %% "%d"
        DataC        100                       
        DataC        0                         
        DLabel       $print-format-floating    
        DataC        37                        %% "%g"
        DataC        103                       
        DataC        0                         
        DLabel       $print-frac-format-rational 
        DataC        37                        %% "%d/%d"
        DataC        100                       
        DataC        47                        
        DataC        37                        
        DataC        100                       
        DataC        0                         
        DLabel       $print-whole-format-rational 
        DataC        37                        %% "%d"
        DataC        100                       
        DataC        0                         
        DLabel       $print-whole-frac-format-rational 
        DataC        37                        %% "%d_%d/%d"
        DataC        100                       
        DataC        95                        
        DataC        37                        
        DataC        100                       
        DataC        47                        
        DataC        37                        
        DataC        100                       
        DataC        0                         
        DLabel       $print-format-string      
        DataC        37                        %% "%s"
        DataC        115                       
        DataC        0                         
        DLabel       $print-format-character   
        DataC        37                        %% "%c"
        DataC        99                        
        DataC        0                         
        DLabel       $print-format-boolean     
        DataC        37                        %% "%s"
        DataC        115                       
        DataC        0                         
        DLabel       $print-format-newline     
        DataC        10                        %% "\n"
        DataC        0                         
        DLabel       $print-format-tab         
        DataC        9                         %% "\t"
        DataC        0                         
        DLabel       $print-format-space       
        DataC        32                        %% " "
        DataC        0                         
        DLabel       $boolean-true-string      
        DataC        116                       %% "true"
        DataC        114                       
        DataC        117                       
        DataC        101                       
        DataC        0                         
        DLabel       $boolean-false-string     
        DataC        102                       %% "false"
        DataC        97                        
        DataC        108                       
        DataC        115                       
        DataC        101                       
        DataC        0                         
        DLabel       $array-separator-char     
        DataC        44                        %% ","
        DataC        0                         
        DLabel       $space-char               
        DataC        32                        %% " "
        DataC        0                         
        DLabel       $array-opening-char       
        DataC        91                        %% "["
        DataC        0                         
        DLabel       $array-closing-char       
        DataC        93                        %% "]"
        DataC        0                         
        DLabel       $errors-general-message   
        DataC        82                        %% "Runtime error: %s\n"
        DataC        117                       
        DataC        110                       
        DataC        116                       
        DataC        105                       
        DataC        109                       
        DataC        101                       
        DataC        32                        
        DataC        101                       
        DataC        114                       
        DataC        114                       
        DataC        111                       
        DataC        114                       
        DataC        58                        
        DataC        32                        
        DataC        37                        
        DataC        115                       
        DataC        10                        
        DataC        0                         
        Label        $$general-runtime-error   
        PushD        $errors-general-message   
        Printf                                 
        Halt                                   
        DLabel       $errors-int-divide-by-zero 
        DataC        105                       %% "integer divide by zero"
        DataC        110                       
        DataC        116                       
        DataC        101                       
        DataC        103                       
        DataC        101                       
        DataC        114                       
        DataC        32                        
        DataC        100                       
        DataC        105                       
        DataC        118                       
        DataC        105                       
        DataC        100                       
        DataC        101                       
        DataC        32                        
        DataC        98                        
        DataC        121                       
        DataC        32                        
        DataC        122                       
        DataC        101                       
        DataC        114                       
        DataC        111                       
        DataC        0                         
        Label        $$i-divide-by-zero        
        PushD        $errors-int-divide-by-zero 
        Jump         $$general-runtime-error   
        DLabel       $errors-float-divide-by-zero 
        DataC        102                       %% "float divide by zero"
        DataC        108                       
        DataC        111                       
        DataC        97                        
        DataC        116                       
        DataC        32                        
        DataC        100                       
        DataC        105                       
        DataC        118                       
        DataC        105                       
        DataC        100                       
        DataC        101                       
        DataC        32                        
        DataC        98                        
        DataC        121                       
        DataC        32                        
        DataC        122                       
        DataC        101                       
        DataC        114                       
        DataC        111                       
        DataC        0                         
        Label        $$f-divide-by-zero        
        PushD        $errors-float-divide-by-zero 
        Jump         $$general-runtime-error   
        DLabel       $errors-deleted-record    
        DataC        114                       %% "record is already deallocated"
        DataC        101                       
        DataC        99                        
        DataC        111                       
        DataC        114                       
        DataC        100                       
        DataC        32                        
        DataC        105                       
        DataC        115                       
        DataC        32                        
        DataC        97                        
        DataC        108                       
        DataC        114                       
        DataC        101                       
        DataC        97                        
        DataC        100                       
        DataC        121                       
        DataC        32                        
        DataC        100                       
        DataC        101                       
        DataC        97                        
        DataC        108                       
        DataC        108                       
        DataC        111                       
        DataC        99                        
        DataC        97                        
        DataC        116                       
        DataC        101                       
        DataC        100                       
        DataC        0                         
        Label        $a-double-free            
        PushD        $errors-deleted-record    
        Jump         $$general-runtime-error   
        DLabel       $errors-index-out-of-bounds 
        DataC        105                       %% "index is out of bounds"
        DataC        110                       
        DataC        100                       
        DataC        101                       
        DataC        120                       
        DataC        32                        
        DataC        105                       
        DataC        115                       
        DataC        32                        
        DataC        111                       
        DataC        117                       
        DataC        116                       
        DataC        32                        
        DataC        111                       
        DataC        102                       
        DataC        32                        
        DataC        98                        
        DataC        111                       
        DataC        117                       
        DataC        110                       
        DataC        100                       
        DataC        115                       
        DataC        0                         
        Label        $a-index-out-of-bounds    
        PushD        $errors-index-out-of-bounds 
        Jump         $$general-runtime-error   
        DLabel       $errors-empty-slice       
        DataC        115                       %% "slice range is invalid"
        DataC        108                       
        DataC        105                       
        DataC        99                        
        DataC        101                       
        DataC        32                        
        DataC        114                       
        DataC        97                        
        DataC        110                       
        DataC        103                       
        DataC        101                       
        DataC        32                        
        DataC        105                       
        DataC        115                       
        DataC        32                        
        DataC        105                       
        DataC        110                       
        DataC        118                       
        DataC        97                        
        DataC        108                       
        DataC        105                       
        DataC        100                       
        DataC        0                         
        Label        $a-empty-string-slice     
        PushD        $errors-empty-slice       
        Jump         $$general-runtime-error   
        DLabel       $errors-negative-length-array 
        DataC        97                        %% "array length is negative"
        DataC        114                       
        DataC        114                       
        DataC        97                        
        DataC        121                       
        DataC        32                        
        DataC        108                       
        DataC        101                       
        DataC        110                       
        DataC        103                       
        DataC        116                       
        DataC        104                       
        DataC        32                        
        DataC        105                       
        DataC        115                       
        DataC        32                        
        DataC        110                       
        DataC        101                       
        DataC        103                       
        DataC        97                        
        DataC        116                       
        DataC        105                       
        DataC        118                       
        DataC        101                       
        DataC        0                         
        Label        $a-negative-length-array  
        PushD        $errors-negative-length-array 
        Jump         $$general-runtime-error   
        DLabel       $errors-null-array        
        DataC        97                        %% "array is null"
        DataC        114                       
        DataC        114                       
        DataC        97                        
        DataC        121                       
        DataC        32                        
        DataC        105                       
        DataC        115                       
        DataC        32                        
        DataC        110                       
        DataC        117                       
        DataC        108                       
        DataC        108                       
        DataC        0                         
        Label        $a-null-array             
        PushD        $errors-null-array        
        Jump         $$general-runtime-error   
        DLabel       $errors-null-string       
        DataC        115                       %% "string is null"
        DataC        116                       
        DataC        114                       
        DataC        105                       
        DataC        110                       
        DataC        103                       
        DataC        32                        
        DataC        105                       
        DataC        115                       
        DataC        32                        
        DataC        110                       
        DataC        117                       
        DataC        108                       
        DataC        108                       
        DataC        0                         
        Label        $a-null-string            
        PushD        $errors-null-string       
        Jump         $$general-runtime-error   
        DLabel       $errors-rational-divide-by-zero 
        DataC        114                       %% "rational divide by zero"
        DataC        97                        
        DataC        116                       
        DataC        105                       
        DataC        111                       
        DataC        110                       
        DataC        97                        
        DataC        108                       
        DataC        32                        
        DataC        100                       
        DataC        105                       
        DataC        118                       
        DataC        105                       
        DataC        100                       
        DataC        101                       
        DataC        32                        
        DataC        98                        
        DataC        121                       
        DataC        32                        
        DataC        122                       
        DataC        101                       
        DataC        114                       
        DataC        111                       
        DataC        0                         
        Label        $$r-divide-by-zero        
        PushD        $errors-rational-divide-by-zero 
        Jump         $$general-runtime-error   
        DLabel       $frame-pointer            
        DataZ        4                         
        DLabel       $stack-pointer            
        DataZ        4                         
        DLabel       $a-indexing-array         
        DataZ        4                         
        DLabel       $a-indexing-index         
        DataZ        4                         
        DLabel       $stash-return-address     
        DataZ        4                         
        DLabel       $r-numerator-storage      
        DataZ        4                         
        DLabel       $r-numerator-gcd-storage  
        DataZ        4                         
        DLabel       $r-denominator-storage    
        DataZ        4                         
        DLabel       $r-denominator-gcd-storage 
        DataZ        4                         
        DLabel       $gcd-storage              
        DataZ        4                         
        DLabel       $r-express-over-denominator 
        DataZ        4                         
        DLabel       $r-rational-denominator-temp 
        DataZ        4                         
        DLabel       $r-rational-address-temp  
        DataZ        4                         
        DLabel       $r-rational-denominator-temp2 
        DataZ        4                         
        DLabel       $r-rational-numerator-temp 
        DataZ        4                         
        DLabel       $r-rational-numerator-temp2 
        DataZ        4                         
        DLabel       $record-creation-temp     
        DataZ        4                         
        DLabel       $s-data-ptr-temp          
        DataZ        4                         
        DLabel       $s-source-ptr-temp        
        DataZ        4                         
        DLabel       $a-data-ptr-temp          
        DataZ        4                         
        DLabel       $a-array-length-temp      
        DataZ        4                         
        DLabel       $a-length-temp            
        DataZ        4                         
        DLabel       $a-string-addr-temp-1     
        DataZ        4                         
        DLabel       $a-string-addr-temp-2     
        DataZ        4                         
        DLabel       $a-addr-temp              
        DataZ        4                         
        DLabel       $a-character-temp         
        DataZ        4                         
        DLabel       $a-string-length-temp     
        DataZ        4                         
        DLabel       $a-array-index-temp       
        DataZ        4                         
        DLabel       $a-counter-temp           
        DataZ        4                         
        DLabel       $a-subtype-size-temp      
        DataZ        4                         
        DLabel       $a-array-source-temp      
        DataZ        4                         
        DLabel       $a-array-datasize-temp    
        DataZ        4                         
        DLabel       $a-array-destination-temp 
        DataZ        4                         
        DLabel       $usable-memory-start      
        DLabel       $global-memory-block      
        DataZ        68                        
        Label        $$main                    
        PushD        $global-memory-block      
        PushI        0                         
        Add                                    %% r
        PushI        5                         
        PushI        12                        
        Add                                    
        PushI        1                         
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        6                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        9                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        PushD        $s-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushI        97                        
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        98                        
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        1                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        99                        
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        2                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        100                       
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        3                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        101                       
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        0                         
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        5                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        5                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        12                        
        Subtract                               
        PushD        $a-string-addr-temp-1     
        Exchange                               
        StoreI                                 
        PushD        $a-string-addr-temp-1     
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-string-length-temp     
        Exchange                               
        StoreI                                 
        PushD        $a-string-length-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        PushI        1                         
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        6                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        9                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        PushD        $s-source-ptr-temp        
        Exchange                               
        StoreI                                 
        PushD        $a-string-addr-temp-1     
        LoadI                                  
        PushI        12                        
        Add                                    
        PushI        0                         
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        Label        -reverseString-2-loopStart 
        Duplicate                              
        PushD        $a-indexing-index         
        LoadI                                  
        Add                                    
        LoadC                                  
        PushD        $s-source-ptr-temp        
        LoadI                                  
        PushD        $a-string-length-temp     
        LoadI                                  
        Add                                    
        PushI        1                         
        Subtract                               
        PushD        $a-indexing-index         
        LoadI                                  
        Subtract                               
        Exchange                               
        StoreC                                 
        PushD        $a-indexing-index         
        LoadI                                  
        PushD        $a-string-length-temp     
        LoadI                                  
        PushI        1                         
        Subtract                               
        Subtract                               
        PushI        1                         
        PushD        $a-indexing-index         
        LoadI                                  
        Add                                    
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        JumpNeg      -reverseString-2-loopStart 
        Pop                                    
        PushI        0                         
        PushD        $s-source-ptr-temp        
        LoadI                                  
        PushD        $a-indexing-index         
        LoadI                                  
        Add                                    
        Exchange                               
        StoreC                                 
        PushD        $a-string-length-temp     
        LoadI                                  
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $s-source-ptr-temp        
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        0                         
        Add                                    %% r
        LoadI                                  
        PushD        $print-format-string      
        Printf                                 
        PushD        $print-format-space       
        Printf                                 
        PushD        $print-format-newline     
        Printf                                 
        PushD        $global-memory-block      
        PushI        4                         
        Add                                    %% a
        PushI        5                         
        PushI        12                        
        Add                                    
        PushI        1                         
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        6                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        9                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        PushD        $s-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushI        104                       
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        101                       
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        1                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        108                       
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        2                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        108                       
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        3                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        111                       
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        0                         
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        5                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        5                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $s-data-ptr-temp          
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        8                         
        Add                                    %% b
        PushD        $global-memory-block      
        PushI        4                         
        Add                                    %% a
        LoadI                                  
        PushI        12                        
        Subtract                               
        PushD        $a-string-addr-temp-1     
        Exchange                               
        StoreI                                 
        PushD        $a-string-addr-temp-1     
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-string-length-temp     
        Exchange                               
        StoreI                                 
        PushD        $a-string-length-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        PushI        1                         
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        6                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        9                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        PushD        $s-source-ptr-temp        
        Exchange                               
        StoreI                                 
        PushD        $a-string-addr-temp-1     
        LoadI                                  
        PushI        12                        
        Add                                    
        PushI        0                         
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        Label        -reverseString-3-loopStart 
        Duplicate                              
        PushD        $a-indexing-index         
        LoadI                                  
        Add                                    
        LoadC                                  
        PushD        $s-source-ptr-temp        
        LoadI                                  
        PushD        $a-string-length-temp     
        LoadI                                  
        Add                                    
        PushI        1                         
        Subtract                               
        PushD        $a-indexing-index         
        LoadI                                  
        Subtract                               
        Exchange                               
        StoreC                                 
        PushD        $a-indexing-index         
        LoadI                                  
        PushD        $a-string-length-temp     
        LoadI                                  
        PushI        1                         
        Subtract                               
        Subtract                               
        PushI        1                         
        PushD        $a-indexing-index         
        LoadI                                  
        Add                                    
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        JumpNeg      -reverseString-3-loopStart 
        Pop                                    
        PushI        0                         
        PushD        $s-source-ptr-temp        
        LoadI                                  
        PushD        $a-indexing-index         
        LoadI                                  
        Add                                    
        Exchange                               
        StoreC                                 
        PushD        $a-string-length-temp     
        LoadI                                  
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $s-source-ptr-temp        
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        4                         
        Add                                    %% a
        LoadI                                  
        PushD        $print-format-string      
        Printf                                 
        PushD        $print-format-space       
        Printf                                 
        PushD        $global-memory-block      
        PushI        8                         
        Add                                    %% b
        LoadI                                  
        PushD        $print-format-string      
        Printf                                 
        PushD        $print-format-newline     
        Printf                                 
        PushD        $global-memory-block      
        PushI        12                        
        Add                                    %% c
        PushI        1                         
        PushI        2                         
        PushI        4                         
        PushI        8                         
        PushI        4                         
        Duplicate                              
        JumpNeg      $a-negative-length-array  
        Duplicate                              
        PushI        4                         
        Multiply                               
        Duplicate                              
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushI        16                        
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        7                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Call         $clear-n-bytes            
        PushI        4                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushI        4                         
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        PushI        4                         
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -populate-4-start         
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        PushD        $a-data-ptr-temp          
        LoadI                                  
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -populate-4-exit          
        Jump         -populate-4-start         
        Label        -populate-4-exit          
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        16                        
        Add                                    %% d
        PushD        $global-memory-block      
        PushI        12                        
        Add                                    %% c
        LoadI                                  
        PushD        $a-indexing-array         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        Duplicate                              
        JumpNeg      $a-negative-length-array  
        Duplicate                              
        PushI        4                         
        Multiply                               
        Duplicate                              
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushI        16                        
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        7                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Call         $clear-n-bytes            
        PushI        4                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        16                        
        Add                                    
        PushI        0                         
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        Label        -reverseArray-5-loopStart 
        Duplicate                              
        PushD        $a-indexing-index         
        LoadI                                  
        PushI        4                         
        Multiply                               
        Add                                    
        PushD        $a-array-source-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-source-temp      
        LoadI                                  
        LoadI                                  
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-indexing-index         
        LoadI                                  
        Subtract                               
        PushI        4                         
        Multiply                               
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-index         
        LoadI                                  
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        Subtract                               
        PushI        1                         
        PushD        $a-indexing-index         
        LoadI                                  
        Add                                    
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        JumpNeg      -reverseArray-5-loopStart 
        Pop                                    
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        12                        
        Add                                    %% c
        LoadI                                  
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        Label        -loadArray-6-loop         
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        LoadI                                  
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -loadArray-6-exit         
        Jump         -loadArray-6-loop         
        Label        -loadArray-6-exit         
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -printArray-7-start       
        PushD        $print-format-integer     
        Printf                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpFalse    -printArray-7-exit        
        PushD        $array-separator-char     
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $space-char               
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-7-start       
        Label        -printArray-7-exit        
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $print-format-space       
        Printf                                 
        PushD        $global-memory-block      
        PushI        16                        
        Add                                    %% d
        LoadI                                  
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        Label        -loadArray-8-loop         
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        LoadI                                  
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -loadArray-8-exit         
        Jump         -loadArray-8-loop         
        Label        -loadArray-8-exit         
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -printArray-9-start       
        PushD        $print-format-integer     
        Printf                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpFalse    -printArray-9-exit        
        PushD        $array-separator-char     
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $space-char               
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-9-start       
        Label        -printArray-9-exit        
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $print-format-newline     
        Printf                                 
        PushD        $global-memory-block      
        PushI        20                        
        Add                                    %% e
        PushI        97                        
        PushI        99                        
        PushI        122                       
        PushI        3                         
        Duplicate                              
        JumpNeg      $a-negative-length-array  
        Duplicate                              
        PushI        1                         
        Multiply                               
        Duplicate                              
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushI        16                        
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        7                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Call         $clear-n-bytes            
        PushI        1                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushI        1                         
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        PushI        3                         
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -populate-10-start        
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        PushD        $a-data-ptr-temp          
        LoadI                                  
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -populate-10-exit         
        Jump         -populate-10-start        
        Label        -populate-10-exit         
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        24                        
        Add                                    %% f
        PushD        $global-memory-block      
        PushI        20                        
        Add                                    %% e
        LoadI                                  
        PushD        $a-indexing-array         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        Duplicate                              
        JumpNeg      $a-negative-length-array  
        Duplicate                              
        PushI        1                         
        Multiply                               
        Duplicate                              
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushI        16                        
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        7                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Call         $clear-n-bytes            
        PushI        1                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        16                        
        Add                                    
        PushI        0                         
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        Label        -reverseArray-11-loopStart 
        Duplicate                              
        PushD        $a-indexing-index         
        LoadI                                  
        PushI        1                         
        Multiply                               
        Add                                    
        PushD        $a-array-source-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-source-temp      
        LoadI                                  
        LoadC                                  
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-indexing-index         
        LoadI                                  
        Subtract                               
        PushI        1                         
        Multiply                               
        Add                                    
        Exchange                               
        StoreC                                 
        PushD        $a-indexing-index         
        LoadI                                  
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        Subtract                               
        PushI        1                         
        PushD        $a-indexing-index         
        LoadI                                  
        Add                                    
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        JumpNeg      -reverseArray-11-loopStart 
        Pop                                    
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        20                        
        Add                                    %% e
        LoadI                                  
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        Label        -loadArray-12-loop        
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        LoadC                                  
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -loadArray-12-exit        
        Jump         -loadArray-12-loop        
        Label        -loadArray-12-exit        
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -printArray-13-start      
        PushD        $print-format-character   
        Printf                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpFalse    -printArray-13-exit       
        PushD        $array-separator-char     
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $space-char               
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-13-start      
        Label        -printArray-13-exit       
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $print-format-space       
        Printf                                 
        PushD        $global-memory-block      
        PushI        24                        
        Add                                    %% f
        LoadI                                  
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        Label        -loadArray-14-loop        
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        LoadC                                  
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -loadArray-14-exit        
        Jump         -loadArray-14-loop        
        Label        -loadArray-14-exit        
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -printArray-15-start      
        PushD        $print-format-character   
        Printf                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpFalse    -printArray-15-exit       
        PushD        $array-separator-char     
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $space-char               
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-15-start      
        Label        -printArray-15-exit       
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $print-format-newline     
        Printf                                 
        PushD        $global-memory-block      
        PushI        28                        
        Add                                    %% g
        PushI        1                         
        PushI        0                         
        PushI        2                         
        Duplicate                              
        JumpNeg      $a-negative-length-array  
        Duplicate                              
        PushI        1                         
        Multiply                               
        Duplicate                              
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushI        16                        
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        7                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Call         $clear-n-bytes            
        PushI        1                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushI        1                         
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        PushI        2                         
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -populate-16-start        
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        PushD        $a-data-ptr-temp          
        LoadI                                  
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -populate-16-exit         
        Jump         -populate-16-start        
        Label        -populate-16-exit         
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        32                        
        Add                                    %% h
        PushD        $global-memory-block      
        PushI        28                        
        Add                                    %% g
        LoadI                                  
        PushD        $a-indexing-array         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        Duplicate                              
        JumpNeg      $a-negative-length-array  
        Duplicate                              
        PushI        1                         
        Multiply                               
        Duplicate                              
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushI        16                        
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        7                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Call         $clear-n-bytes            
        PushI        1                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        16                        
        Add                                    
        PushI        0                         
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        Label        -reverseArray-17-loopStart 
        Duplicate                              
        PushD        $a-indexing-index         
        LoadI                                  
        PushI        1                         
        Multiply                               
        Add                                    
        PushD        $a-array-source-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-source-temp      
        LoadI                                  
        LoadC                                  
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-indexing-index         
        LoadI                                  
        Subtract                               
        PushI        1                         
        Multiply                               
        Add                                    
        Exchange                               
        StoreC                                 
        PushD        $a-indexing-index         
        LoadI                                  
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        Subtract                               
        PushI        1                         
        PushD        $a-indexing-index         
        LoadI                                  
        Add                                    
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        JumpNeg      -reverseArray-17-loopStart 
        Pop                                    
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        28                        
        Add                                    %% g
        LoadI                                  
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        Label        -loadArray-18-loop        
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        LoadC                                  
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -loadArray-18-exit        
        Jump         -loadArray-18-loop        
        Label        -loadArray-18-exit        
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -printArray-19-start      
        JumpTrue     -print-boolean-20-true    
        PushD        $boolean-false-string     
        Jump         -print-boolean-20-join    
        Label        -print-boolean-20-true    
        PushD        $boolean-true-string      
        Label        -print-boolean-20-join    
        PushD        $print-format-boolean     
        Printf                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpFalse    -printArray-19-exit       
        PushD        $array-separator-char     
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $space-char               
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-19-start      
        Label        -printArray-19-exit       
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $print-format-space       
        Printf                                 
        PushD        $global-memory-block      
        PushI        32                        
        Add                                    %% h
        LoadI                                  
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        Label        -loadArray-21-loop        
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        LoadC                                  
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -loadArray-21-exit        
        Jump         -loadArray-21-loop        
        Label        -loadArray-21-exit        
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -printArray-22-start      
        JumpTrue     -print-boolean-23-true    
        PushD        $boolean-false-string     
        Jump         -print-boolean-23-join    
        Label        -print-boolean-23-true    
        PushD        $boolean-true-string      
        Label        -print-boolean-23-join    
        PushD        $print-format-boolean     
        Printf                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpFalse    -printArray-22-exit       
        PushD        $array-separator-char     
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $space-char               
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-22-start      
        Label        -printArray-22-exit       
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $print-format-newline     
        Printf                                 
        PushD        $global-memory-block      
        PushI        36                        
        Add                                    %% i
        PushI        1                         
        PushI        2                         
        Call         $lowest-terms             
        PushI        3                         
        PushI        5                         
        Call         $lowest-terms             
        PushI        2                         
        Duplicate                              
        JumpNeg      $a-negative-length-array  
        Duplicate                              
        PushI        8                         
        Multiply                               
        Duplicate                              
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushI        16                        
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        7                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Call         $clear-n-bytes            
        PushI        8                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushI        8                         
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        PushI        2                         
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -populate-24-start        
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        PushD        $a-data-ptr-temp          
        LoadI                                  
        Add                                    
        Exchange                               
        Exchange                               
        Duplicate                              
        PushD        $r-rational-address-temp  
        Exchange                               
        StoreI                                 
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $r-rational-address-temp  
        LoadI                                  
        Exchange                               
        StoreI                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -populate-24-exit         
        Jump         -populate-24-start        
        Label        -populate-24-exit         
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        40                        
        Add                                    %% j
        PushD        $global-memory-block      
        PushI        36                        
        Add                                    %% i
        LoadI                                  
        PushD        $a-indexing-array         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        Duplicate                              
        JumpNeg      $a-negative-length-array  
        Duplicate                              
        PushI        8                         
        Multiply                               
        Duplicate                              
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushI        16                        
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        7                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Call         $clear-n-bytes            
        PushI        8                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        16                        
        Add                                    
        PushI        0                         
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        Label        -reverseArray-25-loopStart 
        Duplicate                              
        PushD        $a-indexing-index         
        LoadI                                  
        PushI        8                         
        Multiply                               
        Add                                    
        PushD        $a-array-source-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-source-temp      
        LoadI                                  
        Duplicate                              
        LoadI                                  
        Exchange                               
        PushI        4                         
        Add                                    
        LoadI                                  
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-indexing-index         
        LoadI                                  
        Subtract                               
        PushI        8                         
        Multiply                               
        Add                                    
        Exchange                               
        Exchange                               
        Duplicate                              
        PushD        $r-rational-address-temp  
        Exchange                               
        StoreI                                 
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $r-rational-address-temp  
        LoadI                                  
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-index         
        LoadI                                  
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        Subtract                               
        PushI        1                         
        PushD        $a-indexing-index         
        LoadI                                  
        Add                                    
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        JumpNeg      -reverseArray-25-loopStart 
        Pop                                    
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        36                        
        Add                                    %% i
        LoadI                                  
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        Label        -loadArray-26-loop        
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        PushI        4                         
        Add                                    
        LoadI                                  
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        LoadI                                  
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -loadArray-26-exit        
        Jump         -loadArray-26-loop        
        Label        -loadArray-26-exit        
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -printArray-27-start      
        PushD        $r-rational-denominator-temp 
        Exchange                               
        StoreI                                 
        PushD        $r-rational-numerator-temp 
        Exchange                               
        StoreI                                 
        PushD        $r-rational-numerator-temp 
        LoadI                                  
        PushD        $r-rational-denominator-temp 
        LoadI                                  
        Divide                                 
        JumpPos      -rational-28-wholePrint   
        PushD        $r-rational-denominator-temp 
        LoadI                                  
        PushD        $r-rational-numerator-temp 
        LoadI                                  
        PushD        $print-frac-format-rational 
        Jump         -rational-28-exitPrint    
        Label        -rational-28-wholePrint   
        PushD        $r-rational-numerator-temp 
        LoadI                                  
        PushD        $r-rational-denominator-temp 
        LoadI                                  
        Remainder                              
        JumpFalse    -rational-28-fracPrint    
        PushD        $r-rational-denominator-temp 
        LoadI                                  
        PushD        $r-rational-numerator-temp 
        LoadI                                  
        PushD        $r-rational-denominator-temp 
        LoadI                                  
        Remainder                              
        PushD        $r-rational-numerator-temp 
        LoadI                                  
        PushD        $r-rational-denominator-temp 
        LoadI                                  
        Divide                                 
        PushD        $print-whole-frac-format-rational 
        Jump         -rational-28-exitPrint    
        Label        -rational-28-fracPrint    
        PushD        $r-rational-numerator-temp 
        LoadI                                  
        PushD        $r-rational-denominator-temp 
        LoadI                                  
        Divide                                 
        PushD        $print-whole-format-rational 
        Label        -rational-28-exitPrint    
        Printf                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpFalse    -printArray-27-exit       
        PushD        $array-separator-char     
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $space-char               
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-27-start      
        Label        -printArray-27-exit       
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $print-format-space       
        Printf                                 
        PushD        $global-memory-block      
        PushI        40                        
        Add                                    %% j
        LoadI                                  
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        Label        -loadArray-29-loop        
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        PushI        4                         
        Add                                    
        LoadI                                  
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        LoadI                                  
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -loadArray-29-exit        
        Jump         -loadArray-29-loop        
        Label        -loadArray-29-exit        
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -printArray-30-start      
        PushD        $r-rational-denominator-temp 
        Exchange                               
        StoreI                                 
        PushD        $r-rational-numerator-temp 
        Exchange                               
        StoreI                                 
        PushD        $r-rational-numerator-temp 
        LoadI                                  
        PushD        $r-rational-denominator-temp 
        LoadI                                  
        Divide                                 
        JumpPos      -rational-31-wholePrint   
        PushD        $r-rational-denominator-temp 
        LoadI                                  
        PushD        $r-rational-numerator-temp 
        LoadI                                  
        PushD        $print-frac-format-rational 
        Jump         -rational-31-exitPrint    
        Label        -rational-31-wholePrint   
        PushD        $r-rational-numerator-temp 
        LoadI                                  
        PushD        $r-rational-denominator-temp 
        LoadI                                  
        Remainder                              
        JumpFalse    -rational-31-fracPrint    
        PushD        $r-rational-denominator-temp 
        LoadI                                  
        PushD        $r-rational-numerator-temp 
        LoadI                                  
        PushD        $r-rational-denominator-temp 
        LoadI                                  
        Remainder                              
        PushD        $r-rational-numerator-temp 
        LoadI                                  
        PushD        $r-rational-denominator-temp 
        LoadI                                  
        Divide                                 
        PushD        $print-whole-frac-format-rational 
        Jump         -rational-31-exitPrint    
        Label        -rational-31-fracPrint    
        PushD        $r-rational-numerator-temp 
        LoadI                                  
        PushD        $r-rational-denominator-temp 
        LoadI                                  
        Divide                                 
        PushD        $print-whole-format-rational 
        Label        -rational-31-exitPrint    
        Printf                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpFalse    -printArray-30-exit       
        PushD        $array-separator-char     
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $space-char               
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-30-start      
        Label        -printArray-30-exit       
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $print-format-newline     
        Printf                                 
        PushD        $global-memory-block      
        PushI        44                        
        Add                                    %% k
        PushI        4                         
        PushI        12                        
        Add                                    
        PushI        1                         
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        6                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        9                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        PushD        $s-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushI        104                       
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        101                       
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        1                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        104                       
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        2                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        101                       
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        3                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        0                         
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        4                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        2                         
        PushI        12                        
        Add                                    
        PushI        1                         
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        6                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        9                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        PushD        $s-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushI        99                        
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        120                       
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        1                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        0                         
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        2                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        2                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        2                         
        Duplicate                              
        JumpNeg      $a-negative-length-array  
        Duplicate                              
        PushI        4                         
        Multiply                               
        Duplicate                              
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushI        16                        
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        7                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        4                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Call         $clear-n-bytes            
        PushI        4                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushI        4                         
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        PushI        2                         
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -populate-32-start        
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        PushD        $a-data-ptr-temp          
        LoadI                                  
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -populate-32-exit         
        Jump         -populate-32-start        
        Label        -populate-32-exit         
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        48                        
        Add                                    %% l
        PushD        $global-memory-block      
        PushI        44                        
        Add                                    %% k
        LoadI                                  
        PushD        $a-indexing-array         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        Duplicate                              
        JumpNeg      $a-negative-length-array  
        Duplicate                              
        PushI        4                         
        Multiply                               
        Duplicate                              
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushI        16                        
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        7                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        2                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Call         $clear-n-bytes            
        PushI        4                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        16                        
        Add                                    
        PushI        0                         
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        Label        -reverseArray-33-loopStart 
        Duplicate                              
        PushD        $a-indexing-index         
        LoadI                                  
        PushI        4                         
        Multiply                               
        Add                                    
        PushD        $a-array-source-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-source-temp      
        LoadI                                  
        LoadI                                  
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-indexing-index         
        LoadI                                  
        Subtract                               
        PushI        4                         
        Multiply                               
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-index         
        LoadI                                  
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        Subtract                               
        PushI        1                         
        PushD        $a-indexing-index         
        LoadI                                  
        Add                                    
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        JumpNeg      -reverseArray-33-loopStart 
        Pop                                    
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        44                        
        Add                                    %% k
        LoadI                                  
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        Label        -loadArray-34-loop        
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        LoadI                                  
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -loadArray-34-exit        
        Jump         -loadArray-34-loop        
        Label        -loadArray-34-exit        
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -printArray-35-start      
        PushD        $print-format-string      
        Printf                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpFalse    -printArray-35-exit       
        PushD        $array-separator-char     
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $space-char               
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-35-start      
        Label        -printArray-35-exit       
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $print-format-space       
        Printf                                 
        PushD        $global-memory-block      
        PushI        48                        
        Add                                    %% l
        LoadI                                  
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        Label        -loadArray-36-loop        
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        LoadI                                  
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -loadArray-36-exit        
        Jump         -loadArray-36-loop        
        Label        -loadArray-36-exit        
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -printArray-37-start      
        PushD        $print-format-string      
        Printf                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpFalse    -printArray-37-exit       
        PushD        $array-separator-char     
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $space-char               
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-37-start      
        Label        -printArray-37-exit       
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $print-format-newline     
        Printf                                 
        PushD        $global-memory-block      
        PushI        52                        
        Add                                    %% m
        PushF        1.100000                  
        PushF        2.700000                  
        PushF        4.200000                  
        PushF        8.920000                  
        PushI        4                         
        Duplicate                              
        JumpNeg      $a-negative-length-array  
        Duplicate                              
        PushI        8                         
        Multiply                               
        Duplicate                              
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushI        16                        
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        7                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Call         $clear-n-bytes            
        PushI        8                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushI        8                         
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        PushI        4                         
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -populate-38-start        
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        PushD        $a-data-ptr-temp          
        LoadI                                  
        Add                                    
        Exchange                               
        StoreF                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -populate-38-exit         
        Jump         -populate-38-start        
        Label        -populate-38-exit         
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        56                        
        Add                                    %% n
        PushD        $global-memory-block      
        PushI        52                        
        Add                                    %% m
        LoadI                                  
        PushD        $a-indexing-array         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        Duplicate                              
        JumpNeg      $a-negative-length-array  
        Duplicate                              
        PushI        8                         
        Multiply                               
        Duplicate                              
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushI        16                        
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        7                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Call         $clear-n-bytes            
        PushI        8                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        16                        
        Add                                    
        PushI        0                         
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        Label        -reverseArray-39-loopStart 
        Duplicate                              
        PushD        $a-indexing-index         
        LoadI                                  
        PushI        8                         
        Multiply                               
        Add                                    
        PushD        $a-array-source-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-source-temp      
        LoadI                                  
        LoadF                                  
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-indexing-index         
        LoadI                                  
        Subtract                               
        PushI        8                         
        Multiply                               
        Add                                    
        Exchange                               
        StoreF                                 
        PushD        $a-indexing-index         
        LoadI                                  
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        Subtract                               
        PushI        1                         
        PushD        $a-indexing-index         
        LoadI                                  
        Add                                    
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        JumpNeg      -reverseArray-39-loopStart 
        Pop                                    
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        52                        
        Add                                    %% m
        LoadI                                  
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        Label        -loadArray-40-loop        
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        LoadF                                  
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -loadArray-40-exit        
        Jump         -loadArray-40-loop        
        Label        -loadArray-40-exit        
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -printArray-41-start      
        PushD        $print-format-floating    
        Printf                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpFalse    -printArray-41-exit       
        PushD        $array-separator-char     
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $space-char               
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-41-start      
        Label        -printArray-41-exit       
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $print-format-space       
        Printf                                 
        PushD        $global-memory-block      
        PushI        56                        
        Add                                    %% n
        LoadI                                  
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        Label        -loadArray-42-loop        
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        LoadF                                  
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -loadArray-42-exit        
        Jump         -loadArray-42-loop        
        Label        -loadArray-42-exit        
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -printArray-43-start      
        PushD        $print-format-floating    
        Printf                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpFalse    -printArray-43-exit       
        PushD        $array-separator-char     
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $space-char               
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-43-start      
        Label        -printArray-43-exit       
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $print-format-newline     
        Printf                                 
        PushD        $global-memory-block      
        PushI        60                        
        Add                                    %% z
        PushI        1                         
        PushI        2                         
        PushI        4                         
        PushI        3                         
        Duplicate                              
        JumpNeg      $a-negative-length-array  
        Duplicate                              
        PushI        4                         
        Multiply                               
        Duplicate                              
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushI        16                        
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        7                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Call         $clear-n-bytes            
        PushI        4                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushI        4                         
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        PushI        3                         
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -populate-44-start        
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        PushD        $a-data-ptr-temp          
        LoadI                                  
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -populate-44-exit         
        Jump         -populate-44-start        
        Label        -populate-44-exit         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        5                         
        PushI        7                         
        PushI        9                         
        PushI        3                         
        Duplicate                              
        JumpNeg      $a-negative-length-array  
        Duplicate                              
        PushI        4                         
        Multiply                               
        Duplicate                              
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushI        16                        
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        7                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Call         $clear-n-bytes            
        PushI        4                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushI        4                         
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        PushI        3                         
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -populate-45-start        
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        PushD        $a-data-ptr-temp          
        LoadI                                  
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -populate-45-exit         
        Jump         -populate-45-start        
        Label        -populate-45-exit         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        2                         
        Duplicate                              
        JumpNeg      $a-negative-length-array  
        Duplicate                              
        PushI        4                         
        Multiply                               
        Duplicate                              
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushI        16                        
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        7                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        4                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Call         $clear-n-bytes            
        PushI        4                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushI        4                         
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        PushI        2                         
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -populate-46-start        
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        PushD        $a-data-ptr-temp          
        LoadI                                  
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -populate-46-exit         
        Jump         -populate-46-start        
        Label        -populate-46-exit         
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        64                        
        Add                                    %% w
        PushD        $global-memory-block      
        PushI        60                        
        Add                                    %% z
        LoadI                                  
        PushD        $a-indexing-array         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        Duplicate                              
        JumpNeg      $a-negative-length-array  
        Duplicate                              
        PushI        4                         
        Multiply                               
        Duplicate                              
        PushD        $a-array-datasize-temp    
        Exchange                               
        StoreI                                 
        PushI        16                        
        Add                                    
        Call         -mem-manager-allocate     
        PushD        $record-creation-temp     
        Exchange                               
        StoreI                                 
        PushI        7                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        2                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-datasize-temp    
        LoadI                                  
        Call         $clear-n-bytes            
        PushI        4                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        16                        
        Add                                    
        PushI        0                         
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        Label        -reverseArray-47-loopStart 
        Duplicate                              
        PushD        $a-indexing-index         
        LoadI                                  
        PushI        4                         
        Multiply                               
        Add                                    
        PushD        $a-array-source-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-source-temp      
        LoadI                                  
        LoadI                                  
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-indexing-index         
        LoadI                                  
        Subtract                               
        PushI        4                         
        Multiply                               
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-index         
        LoadI                                  
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        Subtract                               
        PushI        1                         
        PushD        $a-indexing-index         
        LoadI                                  
        Add                                    
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        JumpNeg      -reverseArray-47-loopStart 
        Pop                                    
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        12                        
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        60                        
        Add                                    %% z
        LoadI                                  
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        Label        -loadArray-48-loop        
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        LoadI                                  
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -loadArray-48-exit        
        Jump         -loadArray-48-loop        
        Label        -loadArray-48-exit        
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -printArray-49-start      
        PushD        $a-array-index-temp       
        LoadI                                  
        Exchange                               
        Duplicate                              
        JumpFalse    -printArray-49-skipElement 
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        Label        -loadArray-50-loop        
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        LoadI                                  
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -loadArray-50-exit        
        Jump         -loadArray-50-loop        
        Label        -loadArray-50-exit        
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -printArray-51-start      
        PushD        $print-format-integer     
        Printf                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpFalse    -printArray-51-exit       
        PushD        $array-separator-char     
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $space-char               
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-51-start      
        Label        -printArray-51-exit       
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-49-continue   
        Label        -printArray-49-skipElement 
        Pop                                    
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Label        -printArray-49-continue   
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpFalse    -printArray-49-exit       
        PushD        $array-separator-char     
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $space-char               
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-49-start      
        Label        -printArray-49-exit       
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $print-format-space       
        Printf                                 
        PushD        $global-memory-block      
        PushI        64                        
        Add                                    %% w
        LoadI                                  
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        Label        -loadArray-52-loop        
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        LoadI                                  
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -loadArray-52-exit        
        Jump         -loadArray-52-loop        
        Label        -loadArray-52-exit        
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -printArray-53-start      
        PushD        $a-array-index-temp       
        LoadI                                  
        Exchange                               
        Duplicate                              
        JumpFalse    -printArray-53-skipElement 
        PushD        $a-data-ptr-temp          
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-array-length-temp      
        Exchange                               
        StoreI                                 
        PushD        $a-array-length-temp      
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-subtype-size-temp      
        Exchange                               
        StoreI                                 
        Label        -loadArray-54-loop        
        PushD        $a-data-ptr-temp          
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-array-index-temp       
        LoadI                                  
        PushD        $a-subtype-size-temp      
        LoadI                                  
        Multiply                               
        Add                                    
        LoadI                                  
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpNeg      -loadArray-54-exit        
        Jump         -loadArray-54-loop        
        Label        -loadArray-54-exit        
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -printArray-55-start      
        PushD        $print-format-integer     
        Printf                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpFalse    -printArray-55-exit       
        PushD        $array-separator-char     
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $space-char               
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-55-start      
        Label        -printArray-55-exit       
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-53-continue   
        Label        -printArray-53-skipElement 
        Pop                                    
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Label        -printArray-53-continue   
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushI        -1                        
        PushD        $a-array-index-temp       
        LoadI                                  
        Add                                    
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        PushD        $a-array-index-temp       
        LoadI                                  
        JumpFalse    -printArray-53-exit       
        PushD        $array-separator-char     
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $space-char               
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-53-start      
        Label        -printArray-53-exit       
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $print-format-newline     
        Printf                                 
        Halt                                   
        Label        -mem-manager-make-tags    
        DLabel       $mmgr-tags-size           
        DataZ        4                         
        DLabel       $mmgr-tags-start          
        DataZ        4                         
        DLabel       $mmgr-tags-available      
        DataZ        4                         
        DLabel       $mmgr-tags-nextptr        
        DataZ        4                         
        DLabel       $mmgr-tags-prevptr        
        DataZ        4                         
        DLabel       $mmgr-tags-return         
        DataZ        4                         
        PushD        $mmgr-tags-return         
        Exchange                               
        StoreI                                 
        PushD        $mmgr-tags-size           
        Exchange                               
        StoreI                                 
        PushD        $mmgr-tags-start          
        Exchange                               
        StoreI                                 
        PushD        $mmgr-tags-available      
        Exchange                               
        StoreI                                 
        PushD        $mmgr-tags-nextptr        
        Exchange                               
        StoreI                                 
        PushD        $mmgr-tags-prevptr        
        Exchange                               
        StoreI                                 
        PushD        $mmgr-tags-prevptr        
        LoadI                                  
        PushD        $mmgr-tags-size           
        LoadI                                  
        PushD        $mmgr-tags-available      
        LoadI                                  
        PushD        $mmgr-tags-start          
        LoadI                                  
        Call         -mem-manager-one-tag      
        PushD        $mmgr-tags-nextptr        
        LoadI                                  
        PushD        $mmgr-tags-size           
        LoadI                                  
        PushD        $mmgr-tags-available      
        LoadI                                  
        PushD        $mmgr-tags-start          
        LoadI                                  
        Duplicate                              
        PushI        4                         
        Add                                    
        LoadI                                  
        Add                                    
        PushI        9                         
        Subtract                               
        Call         -mem-manager-one-tag      
        PushD        $mmgr-tags-return         
        LoadI                                  
        Return                                 
        Label        -mem-manager-one-tag      
        DLabel       $mmgr-onetag-return       
        DataZ        4                         
        DLabel       $mmgr-onetag-location     
        DataZ        4                         
        DLabel       $mmgr-onetag-available    
        DataZ        4                         
        DLabel       $mmgr-onetag-size         
        DataZ        4                         
        DLabel       $mmgr-onetag-pointer      
        DataZ        4                         
        PushD        $mmgr-onetag-return       
        Exchange                               
        StoreI                                 
        PushD        $mmgr-onetag-location     
        Exchange                               
        StoreI                                 
        PushD        $mmgr-onetag-available    
        Exchange                               
        StoreI                                 
        PushD        $mmgr-onetag-size         
        Exchange                               
        StoreI                                 
        PushD        $mmgr-onetag-location     
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $mmgr-onetag-size         
        LoadI                                  
        PushD        $mmgr-onetag-location     
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $mmgr-onetag-available    
        LoadI                                  
        PushD        $mmgr-onetag-location     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushD        $mmgr-onetag-return       
        LoadI                                  
        Return                                 
        Label        -mem-manager-allocate     
        DLabel       $mmgr-alloc-return        
        DataZ        4                         
        DLabel       $mmgr-alloc-size          
        DataZ        4                         
        DLabel       $mmgr-alloc-current-block 
        DataZ        4                         
        DLabel       $mmgr-alloc-remainder-block 
        DataZ        4                         
        DLabel       $mmgr-alloc-remainder-size 
        DataZ        4                         
        PushD        $mmgr-alloc-return        
        Exchange                               
        StoreI                                 
        PushI        18                        
        Add                                    
        PushD        $mmgr-alloc-size          
        Exchange                               
        StoreI                                 
        PushD        $heap-first-free          
        LoadI                                  
        PushD        $mmgr-alloc-current-block 
        Exchange                               
        StoreI                                 
        Label        -mmgr-alloc-process-current 
        PushD        $mmgr-alloc-current-block 
        LoadI                                  
        JumpFalse    -mmgr-alloc-no-block-works 
        Label        -mmgr-alloc-test-block    
        PushD        $mmgr-alloc-current-block 
        LoadI                                  
        PushI        4                         
        Add                                    
        LoadI                                  
        PushD        $mmgr-alloc-size          
        LoadI                                  
        Subtract                               
        PushI        1                         
        Add                                    
        JumpPos      -mmgr-alloc-found-block   
        PushD        $mmgr-alloc-current-block 
        LoadI                                  
        Duplicate                              
        PushI        4                         
        Add                                    
        LoadI                                  
        Add                                    
        PushI        9                         
        Subtract                               
        PushI        0                         
        Add                                    
        LoadI                                  
        PushD        $mmgr-alloc-current-block 
        Exchange                               
        StoreI                                 
        Jump         -mmgr-alloc-process-current 
        Label        -mmgr-alloc-found-block   
        PushD        $mmgr-alloc-current-block 
        LoadI                                  
        Call         -mem-manager-remove-block 
        PushD        $mmgr-alloc-current-block 
        LoadI                                  
        PushI        4                         
        Add                                    
        LoadI                                  
        PushD        $mmgr-alloc-size          
        LoadI                                  
        Subtract                               
        PushI        26                        
        Subtract                               
        JumpNeg      -mmgr-alloc-return-userblock 
        PushD        $mmgr-alloc-current-block 
        LoadI                                  
        PushD        $mmgr-alloc-size          
        LoadI                                  
        Add                                    
        PushD        $mmgr-alloc-remainder-block 
        Exchange                               
        StoreI                                 
        PushD        $mmgr-alloc-size          
        LoadI                                  
        PushD        $mmgr-alloc-current-block 
        LoadI                                  
        PushI        4                         
        Add                                    
        LoadI                                  
        Exchange                               
        Subtract                               
        PushD        $mmgr-alloc-remainder-size 
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushI        0                         
        PushI        0                         
        PushD        $mmgr-alloc-current-block 
        LoadI                                  
        PushD        $mmgr-alloc-size          
        LoadI                                  
        Call         -mem-manager-make-tags    
        PushI        0                         
        PushI        0                         
        PushI        1                         
        PushD        $mmgr-alloc-remainder-block 
        LoadI                                  
        PushD        $mmgr-alloc-remainder-size 
        LoadI                                  
        Call         -mem-manager-make-tags    
        PushD        $mmgr-alloc-remainder-block 
        LoadI                                  
        PushI        9                         
        Add                                    
        Call         -mem-manager-deallocate   
        Jump         -mmgr-alloc-return-userblock 
        Label        -mmgr-alloc-no-block-works 
        PushD        $mmgr-alloc-size          
        LoadI                                  
        PushD        $mmgr-newblock-size       
        Exchange                               
        StoreI                                 
        PushD        $heap-after-ptr           
        LoadI                                  
        PushD        $mmgr-newblock-block      
        Exchange                               
        StoreI                                 
        PushD        $mmgr-newblock-size       
        LoadI                                  
        PushD        $heap-after-ptr           
        LoadI                                  
        Add                                    
        PushD        $heap-after-ptr           
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushI        0                         
        PushI        0                         
        PushD        $mmgr-newblock-block      
        LoadI                                  
        PushD        $mmgr-newblock-size       
        LoadI                                  
        Call         -mem-manager-make-tags    
        PushD        $mmgr-newblock-block      
        LoadI                                  
        PushD        $mmgr-alloc-current-block 
        Exchange                               
        StoreI                                 
        Label        -mmgr-alloc-return-userblock 
        PushD        $mmgr-alloc-current-block 
        LoadI                                  
        PushI        9                         
        Add                                    
        PushD        $mmgr-alloc-return        
        LoadI                                  
        Return                                 
        Label        -mem-manager-deallocate   
        DLabel       $mmgr-dealloc-return      
        DataZ        4                         
        DLabel       $mmgr-dealloc-block       
        DataZ        4                         
        PushD        $mmgr-dealloc-return      
        Exchange                               
        StoreI                                 
        PushI        9                         
        Subtract                               
        PushD        $mmgr-dealloc-block       
        Exchange                               
        StoreI                                 
        PushD        $heap-first-free          
        LoadI                                  
        JumpFalse    -mmgr-bypass-firstFree    
        PushD        $mmgr-dealloc-block       
        LoadI                                  
        PushD        $heap-first-free          
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        Label        -mmgr-bypass-firstFree    
        PushI        0                         
        PushD        $mmgr-dealloc-block       
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $heap-first-free          
        LoadI                                  
        PushD        $mmgr-dealloc-block       
        LoadI                                  
        Duplicate                              
        PushI        4                         
        Add                                    
        LoadI                                  
        Add                                    
        PushI        9                         
        Subtract                               
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushI        1                         
        PushD        $mmgr-dealloc-block       
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        1                         
        PushD        $mmgr-dealloc-block       
        LoadI                                  
        Duplicate                              
        PushI        4                         
        Add                                    
        LoadI                                  
        Add                                    
        PushI        9                         
        Subtract                               
        PushI        8                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushD        $mmgr-dealloc-block       
        LoadI                                  
        PushD        $heap-first-free          
        Exchange                               
        StoreI                                 
        PushD        $mmgr-dealloc-return      
        LoadI                                  
        Return                                 
        Label        -mem-manager-remove-block 
        DLabel       $mmgr-remove-return       
        DataZ        4                         
        DLabel       $mmgr-remove-block        
        DataZ        4                         
        DLabel       $mmgr-remove-prev         
        DataZ        4                         
        DLabel       $mmgr-remove-next         
        DataZ        4                         
        PushD        $mmgr-remove-return       
        Exchange                               
        StoreI                                 
        PushD        $mmgr-remove-block        
        Exchange                               
        StoreI                                 
        PushD        $mmgr-remove-block        
        LoadI                                  
        PushI        0                         
        Add                                    
        LoadI                                  
        PushD        $mmgr-remove-prev         
        Exchange                               
        StoreI                                 
        PushD        $mmgr-remove-block        
        LoadI                                  
        Duplicate                              
        PushI        4                         
        Add                                    
        LoadI                                  
        Add                                    
        PushI        9                         
        Subtract                               
        PushI        0                         
        Add                                    
        LoadI                                  
        PushD        $mmgr-remove-next         
        Exchange                               
        StoreI                                 
        Label        -mmgr-remove-process-prev 
        PushD        $mmgr-remove-prev         
        LoadI                                  
        JumpFalse    -mmgr-remove-no-prev      
        PushD        $mmgr-remove-next         
        LoadI                                  
        PushD        $mmgr-remove-prev         
        LoadI                                  
        Duplicate                              
        PushI        4                         
        Add                                    
        LoadI                                  
        Add                                    
        PushI        9                         
        Subtract                               
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        Jump         -mmgr-remove-process-next 
        Label        -mmgr-remove-no-prev      
        PushD        $mmgr-remove-next         
        LoadI                                  
        PushD        $heap-first-free          
        Exchange                               
        StoreI                                 
        Label        -mmgr-remove-process-next 
        PushD        $mmgr-remove-next         
        LoadI                                  
        JumpFalse    -mmgr-remove-done         
        PushD        $mmgr-remove-prev         
        LoadI                                  
        PushD        $mmgr-remove-next         
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreI                                 
        Label        -mmgr-remove-done         
        PushD        $mmgr-remove-return       
        LoadI                                  
        Return                                 
        DLabel       $heap-memory              
