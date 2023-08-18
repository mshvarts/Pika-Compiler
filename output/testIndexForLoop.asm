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
        DataZ        28                        
        Label        $$main                    
        PushI        6                         
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
        PushI        114                       
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        0                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        97                        
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        1                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        98                        
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        2                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        98                        
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        3                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        105                       
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        4                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        116                       
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        5                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        0                         
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushI        6                         
        Add                                    
        Exchange                               
        StoreC                                 
        PushI        6                         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        8                         
        Add                                    
        Exchange                               
        StoreI                                 
        PushD        $s-data-ptr-temp          
        LoadI                                  
        PushD        $a-addr-temp              
        Exchange                               
        StoreI                                 
        PushD        $a-addr-temp              
        LoadI                                  
        PushI        12                        
        Subtract                               
        PushI        8                         
        Add                                    
        LoadI                                  
        PushD        $a-length-temp            
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $a-counter-temp           
        Exchange                               
        StoreI                                 
        Label        -for-2-start              
        PushD        $global-memory-block      
        PushI        0                         
        Add                                    %% i
        PushD        $a-counter-temp           
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        0                         
        Add                                    %% i
        LoadI                                  
        PushD        $print-format-integer     
        Printf                                 
        PushD        $print-format-space       
        Printf                                 
        PushD        $a-length-temp            
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-counter-temp           
        LoadI                                  
        Subtract                               
        JumpFalse    -for-2-end                
        PushI        1                         
        PushD        $a-counter-temp           
        LoadI                                  
        Add                                    
        PushD        $a-counter-temp           
        Exchange                               
        StoreI                                 
        Jump         -for-2-start              
        Label        -for-2-end                
        PushD        $print-format-newline     
        Printf                                 
        PushD        $global-memory-block      
        PushI        0                         
        Add                                    %% a
        PushI        1                         
        PushI        2                         
        PushI        3                         
        PushI        4                         
        PushI        5                         
        PushI        5                         
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
        PushI        5                         
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
        Label        -populate-3-start         
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
        JumpNeg      -populate-3-exit          
        Jump         -populate-3-start         
        Label        -populate-3-exit          
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        0                         
        Add                                    %% a
        LoadI                                  
        PushD        $a-addr-temp              
        Exchange                               
        StoreI                                 
        PushD        $a-addr-temp              
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-length-temp            
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $a-counter-temp           
        Exchange                               
        StoreI                                 
        Label        -for-4-start              
        PushD        $global-memory-block      
        PushI        4                         
        Add                                    %% i
        PushD        $a-counter-temp           
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        0                         
        Add                                    %% a
        LoadI                                  
        PushD        $global-memory-block      
        PushI        4                         
        Add                                    %% i
        LoadI                                  
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        JumpFalse    $a-null-array             
        PushD        $a-indexing-index         
        LoadI                                  
        JumpNeg      $a-index-out-of-bounds    
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-indexing-index         
        LoadI                                  
        Subtract                               
        JumpPos      -array-indexing-5-in-bounds 
        Jump         $a-index-out-of-bounds    
        Label        -array-indexing-5-in-bounds 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-indexing-index         
        LoadI                                  
        PushI        4                         
        Multiply                               
        Add                                    
        LoadI                                  
        PushD        $print-format-integer     
        Printf                                 
        PushD        $print-format-space       
        Printf                                 
        PushD        $a-length-temp            
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-counter-temp           
        LoadI                                  
        Subtract                               
        JumpFalse    -for-4-end                
        PushI        1                         
        PushD        $a-counter-temp           
        LoadI                                  
        Add                                    
        PushD        $a-counter-temp           
        Exchange                               
        StoreI                                 
        Jump         -for-4-start              
        Label        -for-4-end                
        PushD        $print-format-newline     
        Printf                                 
        PushD        $global-memory-block      
        PushI        4                         
        Add                                    %% b
        PushI        99                        
        PushI        98                        
        PushI        101                       
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
        Label        -populate-6-start         
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
        JumpNeg      -populate-6-exit          
        Jump         -populate-6-start         
        Label        -populate-6-exit          
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        4                         
        Add                                    %% b
        LoadI                                  
        PushD        $a-addr-temp              
        Exchange                               
        StoreI                                 
        PushD        $a-addr-temp              
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-length-temp            
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $a-counter-temp           
        Exchange                               
        StoreI                                 
        Label        -for-7-start              
        PushD        $global-memory-block      
        PushI        8                         
        Add                                    %% i
        PushD        $a-counter-temp           
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        4                         
        Add                                    %% b
        LoadI                                  
        PushD        $global-memory-block      
        PushI        8                         
        Add                                    %% i
        LoadI                                  
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        JumpFalse    $a-null-array             
        PushD        $a-indexing-index         
        LoadI                                  
        JumpNeg      $a-index-out-of-bounds    
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-indexing-index         
        LoadI                                  
        Subtract                               
        JumpPos      -array-indexing-8-in-bounds 
        Jump         $a-index-out-of-bounds    
        Label        -array-indexing-8-in-bounds 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-indexing-index         
        LoadI                                  
        PushI        1                         
        Multiply                               
        Add                                    
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $print-format-space       
        Printf                                 
        PushD        $a-length-temp            
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-counter-temp           
        LoadI                                  
        Subtract                               
        JumpFalse    -for-7-end                
        PushI        1                         
        PushD        $a-counter-temp           
        LoadI                                  
        Add                                    
        PushD        $a-counter-temp           
        Exchange                               
        StoreI                                 
        Jump         -for-7-start              
        Label        -for-7-end                
        PushD        $print-format-newline     
        Printf                                 
        PushD        $global-memory-block      
        PushI        8                         
        Add                                    %% c
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
        Label        -populate-9-start         
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
        JumpNeg      -populate-9-exit          
        Jump         -populate-9-start         
        Label        -populate-9-exit          
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        8                         
        Add                                    %% c
        LoadI                                  
        PushD        $a-addr-temp              
        Exchange                               
        StoreI                                 
        PushD        $a-addr-temp              
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-length-temp            
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $a-counter-temp           
        Exchange                               
        StoreI                                 
        Label        -for-10-start             
        PushD        $global-memory-block      
        PushI        12                        
        Add                                    %% i
        PushD        $a-counter-temp           
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        8                         
        Add                                    %% c
        LoadI                                  
        PushD        $global-memory-block      
        PushI        12                        
        Add                                    %% i
        LoadI                                  
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        JumpFalse    $a-null-array             
        PushD        $a-indexing-index         
        LoadI                                  
        JumpNeg      $a-index-out-of-bounds    
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-indexing-index         
        LoadI                                  
        Subtract                               
        JumpPos      -array-indexing-11-in-bounds 
        Jump         $a-index-out-of-bounds    
        Label        -array-indexing-11-in-bounds 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-indexing-index         
        LoadI                                  
        PushI        1                         
        Multiply                               
        Add                                    
        LoadC                                  
        JumpTrue     -print-boolean-12-true    
        PushD        $boolean-false-string     
        Jump         -print-boolean-12-join    
        Label        -print-boolean-12-true    
        PushD        $boolean-true-string      
        Label        -print-boolean-12-join    
        PushD        $print-format-boolean     
        Printf                                 
        PushD        $print-format-space       
        Printf                                 
        PushD        $a-length-temp            
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-counter-temp           
        LoadI                                  
        Subtract                               
        JumpFalse    -for-10-end               
        PushI        1                         
        PushD        $a-counter-temp           
        LoadI                                  
        Add                                    
        PushD        $a-counter-temp           
        Exchange                               
        StoreI                                 
        Jump         -for-10-start             
        Label        -for-10-end               
        PushD        $print-format-newline     
        Printf                                 
        PushD        $global-memory-block      
        PushI        12                        
        Add                                    %% d
        PushI        4                         
        PushI        6                         
        Call         $lowest-terms             
        PushI        1                         
        PushI        2                         
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
        Label        -populate-13-start        
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
        JumpNeg      -populate-13-exit         
        Jump         -populate-13-start        
        Label        -populate-13-exit         
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        12                        
        Add                                    %% d
        LoadI                                  
        PushD        $a-addr-temp              
        Exchange                               
        StoreI                                 
        PushD        $a-addr-temp              
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-length-temp            
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $a-counter-temp           
        Exchange                               
        StoreI                                 
        Label        -for-14-start             
        PushD        $global-memory-block      
        PushI        16                        
        Add                                    %% i
        PushD        $a-counter-temp           
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        12                        
        Add                                    %% d
        LoadI                                  
        PushD        $global-memory-block      
        PushI        16                        
        Add                                    %% i
        LoadI                                  
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        JumpFalse    $a-null-array             
        PushD        $a-indexing-index         
        LoadI                                  
        JumpNeg      $a-index-out-of-bounds    
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-indexing-index         
        LoadI                                  
        Subtract                               
        JumpPos      -array-indexing-15-in-bounds 
        Jump         $a-index-out-of-bounds    
        Label        -array-indexing-15-in-bounds 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-indexing-index         
        LoadI                                  
        PushI        8                         
        Multiply                               
        Add                                    
        PushD        $r-rational-address-temp  
        Exchange                               
        StoreI                                 
        PushD        $r-rational-address-temp  
        LoadI                                  
        LoadI                                  
        PushD        $r-rational-address-temp  
        LoadI                                  
        PushI        4                         
        Add                                    
        LoadI                                  
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
        JumpPos      -rational-16-wholePrint   
        PushD        $r-rational-denominator-temp 
        LoadI                                  
        PushD        $r-rational-numerator-temp 
        LoadI                                  
        PushD        $print-frac-format-rational 
        Jump         -rational-16-exitPrint    
        Label        -rational-16-wholePrint   
        PushD        $r-rational-numerator-temp 
        LoadI                                  
        PushD        $r-rational-denominator-temp 
        LoadI                                  
        Remainder                              
        JumpFalse    -rational-16-fracPrint    
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
        Jump         -rational-16-exitPrint    
        Label        -rational-16-fracPrint    
        PushD        $r-rational-numerator-temp 
        LoadI                                  
        PushD        $r-rational-denominator-temp 
        LoadI                                  
        Divide                                 
        PushD        $print-whole-format-rational 
        Label        -rational-16-exitPrint    
        Printf                                 
        PushD        $print-format-space       
        Printf                                 
        PushD        $a-length-temp            
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-counter-temp           
        LoadI                                  
        Subtract                               
        JumpFalse    -for-14-end               
        PushI        1                         
        PushD        $a-counter-temp           
        LoadI                                  
        Add                                    
        PushD        $a-counter-temp           
        Exchange                               
        StoreI                                 
        Jump         -for-14-start             
        Label        -for-14-end               
        PushD        $print-format-newline     
        Printf                                 
        PushD        $global-memory-block      
        PushI        16                        
        Add                                    %% e
        PushF        1.520000                  
        PushF        6.120000                  
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
        Label        -populate-17-start        
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
        JumpNeg      -populate-17-exit         
        Jump         -populate-17-start        
        Label        -populate-17-exit         
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        16                        
        Add                                    %% e
        LoadI                                  
        PushD        $a-addr-temp              
        Exchange                               
        StoreI                                 
        PushD        $a-addr-temp              
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-length-temp            
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $a-counter-temp           
        Exchange                               
        StoreI                                 
        Label        -for-18-start             
        PushD        $global-memory-block      
        PushI        20                        
        Add                                    %% i
        PushD        $a-counter-temp           
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        16                        
        Add                                    %% e
        LoadI                                  
        PushD        $global-memory-block      
        PushI        20                        
        Add                                    %% i
        LoadI                                  
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        JumpFalse    $a-null-array             
        PushD        $a-indexing-index         
        LoadI                                  
        JumpNeg      $a-index-out-of-bounds    
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-indexing-index         
        LoadI                                  
        Subtract                               
        JumpPos      -array-indexing-19-in-bounds 
        Jump         $a-index-out-of-bounds    
        Label        -array-indexing-19-in-bounds 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-indexing-index         
        LoadI                                  
        PushI        8                         
        Multiply                               
        Add                                    
        LoadF                                  
        PushD        $print-format-floating    
        Printf                                 
        PushD        $print-format-space       
        Printf                                 
        PushD        $a-length-temp            
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-counter-temp           
        LoadI                                  
        Subtract                               
        JumpFalse    -for-18-end               
        PushI        1                         
        PushD        $a-counter-temp           
        LoadI                                  
        Add                                    
        PushD        $a-counter-temp           
        Exchange                               
        StoreI                                 
        Jump         -for-18-start             
        Label        -for-18-end               
        PushD        $print-format-newline     
        Printf                                 
        PushD        $global-memory-block      
        PushI        20                        
        Add                                    %% f
        PushI        1                         
        PushI        2                         
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
        Label        -populate-20-start        
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
        JumpNeg      -populate-20-exit         
        Jump         -populate-20-start        
        Label        -populate-20-exit         
        PushD        $record-creation-temp     
        LoadI                                  
        PushI        3                         
        PushI        4                         
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
        Label        -populate-21-start        
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
        JumpNeg      -populate-21-exit         
        Jump         -populate-21-start        
        Label        -populate-21-exit         
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
        Label        -populate-22-start        
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
        JumpNeg      -populate-22-exit         
        Jump         -populate-22-start        
        Label        -populate-22-exit         
        PushD        $record-creation-temp     
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        20                        
        Add                                    %% f
        LoadI                                  
        PushD        $a-addr-temp              
        Exchange                               
        StoreI                                 
        PushD        $a-addr-temp              
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-length-temp            
        Exchange                               
        StoreI                                 
        PushI        0                         
        PushD        $a-counter-temp           
        Exchange                               
        StoreI                                 
        Label        -for-23-start             
        PushD        $global-memory-block      
        PushI        24                        
        Add                                    %% i
        PushD        $a-counter-temp           
        LoadI                                  
        StoreI                                 
        PushD        $global-memory-block      
        PushI        20                        
        Add                                    %% f
        LoadI                                  
        PushD        $global-memory-block      
        PushI        24                        
        Add                                    %% i
        LoadI                                  
        PushD        $a-indexing-index         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        Exchange                               
        StoreI                                 
        PushD        $a-indexing-array         
        LoadI                                  
        JumpFalse    $a-null-array             
        PushD        $a-indexing-index         
        LoadI                                  
        JumpNeg      $a-index-out-of-bounds    
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        12                        
        Add                                    
        LoadI                                  
        PushD        $a-indexing-index         
        LoadI                                  
        Subtract                               
        JumpPos      -array-indexing-24-in-bounds 
        Jump         $a-index-out-of-bounds    
        Label        -array-indexing-24-in-bounds 
        PushD        $a-indexing-array         
        LoadI                                  
        PushI        16                        
        Add                                    
        PushD        $a-indexing-index         
        LoadI                                  
        PushI        4                         
        Multiply                               
        Add                                    
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
        Label        -loadArray-25-loop        
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
        JumpNeg      -loadArray-25-exit        
        Jump         -loadArray-25-loop        
        Label        -loadArray-25-exit        
        PushD        $a-array-length-temp      
        LoadI                                  
        PushD        $array-opening-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $a-array-index-temp       
        Exchange                               
        StoreI                                 
        Label        -printArray-26-start      
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
        JumpFalse    -printArray-26-exit       
        PushD        $array-separator-char     
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $space-char               
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        Jump         -printArray-26-start      
        Label        -printArray-26-exit       
        PushD        $array-closing-char       
        LoadC                                  
        PushD        $print-format-character   
        Printf                                 
        PushD        $print-format-space       
        Printf                                 
        PushD        $a-length-temp            
        LoadI                                  
        PushI        1                         
        Subtract                               
        PushD        $a-counter-temp           
        LoadI                                  
        Subtract                               
        JumpFalse    -for-23-end               
        PushI        1                         
        PushD        $a-counter-temp           
        LoadI                                  
        Add                                    
        PushD        $a-counter-temp           
        Exchange                               
        StoreI                                 
        Jump         -for-23-start             
        Label        -for-23-end               
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