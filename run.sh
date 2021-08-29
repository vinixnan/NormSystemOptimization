#!/bin/bash
algs="NSGAII MOEADD SPEA2 MOMBI"
numAgentsSet="200"
numEvadersSet="10"
numSegmentsSet="5" 
investRateSet="0.05"
lengthSet="10"
pathSet="5000"
qtdObjSet="2 5"
qtdExp=30
rm -f "runMain.txt"




for m in $qtdObjSet
do
    for numAgents in $numAgentsSet
    do
        for numEvaders in $numEvadersSet
        do
            for numSegments in $numSegmentsSet
            do
                for investRate in $investRateSet
                do
                    for length in $lengthSet
                    do
                        for path in $pathSet
                        do
                            for alg in $algs
                            do
                                run=0
                                outputname="saida_"$alg"_"$numAgents"_"$numEvaders"_"$numSegments"_"$investRate"_"$length"_"$path"_"$m
                                while [ $run -le $qtdExp ]
                                do
                                    echo "java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.main.Main  $alg $numAgents $numEvaders $numSegments $investRate $length $path $m $run > $outputname 2> $outputname" >> "runMain.txt"
                                    let run=$run+1;
                                done
                            done
                        done
                    done
                done
            done
        done
    done
done

cat "runMain.txt" | xargs -I CMD -P 20  bash -c CMD &
wait
