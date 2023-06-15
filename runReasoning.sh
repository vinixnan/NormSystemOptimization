qtdExp=30
problemNameBase="NormSystem"
MaxIteractions=200
MaxIteractions=500

ms="2 5"



for m in $ms
do
        problemName=$problemNameBase"_obj_"$m
	labels=()
        #for 2 obj
        #labels+=("conf_200_10_5_0.05_10_5000")
        #labels+=("conf_200_30_5_0.05_10_5000")
        #for 5 obj
	#labels+=("conf_400_100_5_0.05_10_5000")

        #both
        labels+=("conf_200_10_5_0.05_10_5000")
        for M in ${labels[@]}
	do
		run=1
		while [ $run -le $qtdExp ]
		do
			mombi2="result/Mombi2/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN"$run
                        moead="result/MoeaDD/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN"$run
                        nsgaii="result/Nsgaii/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN"$run
                        spea2="result/Spea2/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN"$run
                        java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.reasoning.runReasoningEngineOverResults $nsgaii 100 $m $run
                        let run=$run+1;
		done
		
	done
done
