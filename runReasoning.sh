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
                rm -f $problemName"_"$M"_"variable.csv
                rm -f $problemName"_"$M"_"objective.csv
                echo "Run;bestKey;bestValue;objectives;algName;problemName" > $problemName"_"$M"_"objective.csv
                echo "Run;bestKey;bestValue;objectives;algName;problemName" > $problemName"_"$M"_"variable.csv
		while [ $run -le $qtdExp ]
		do
			mombi2="result/Mombi2/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN"$run
                        mombi2_var="result/Mombi2/"$problemName"/"$M"/"$MaxIteractions"/VAR_ALLMIN"$run
                        moead="result/MoeaDD/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN"$run
                        moead_var="result/MoeaDD/"$problemName"/"$M"/"$MaxIteractions"/VAR_ALLMIN"$run
                        nsgaii="result/Nsgaii/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN"$run
                        nsgaii_var="result/Nsgaii/"$problemName"/"$M"/"$MaxIteractions"/VAR_ALLMIN"$run
                        spea2="result/Spea2/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN"$run
                        spea2_var="result/Spea2/"$problemName"/"$M"/"$MaxIteractions"/VAR_ALLMIN"$run    

                        java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.reasoning.runReasoningEngineOverResults $nsgaii_var $nsgaii 200 0 $m $run NSGAII $problemName"_"$M >> $problemName"_"$M"_"variable.csv
                        java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.reasoning.runReasoningEngineOverResults $nsgaii_var $nsgaii 0 200 $m $run NSGAII $problemName"_"$M >> $problemName"_"$M"_"objective.csv
                        
                        java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.reasoning.runReasoningEngineOverResults $mombi2_var $mombi2 200 0 $m $run MOMBI2 $problemName"_"$M >> $problemName"_"$M"_"variable.csv
                        java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.reasoning.runReasoningEngineOverResults $mombi2_var $mombi2 0 200 $m $run MOMBI2 $problemName"_"$M >> $problemName"_"$M"_"objective.csv
                        
                        java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.reasoning.runReasoningEngineOverResults $moead_var $moead 200 0 $m $run MOEAD $problemName"_"$M >> $problemName"_"$M"_"variable.csv
                        java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.reasoning.runReasoningEngineOverResults $moead_var $moead 0 200 $m $run MOEAD $problemName"_"$M >> $problemName"_"$M"_"objective.csv
                        
                        java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.reasoning.runReasoningEngineOverResults $spea2_var $spea2 200 0 $m $run SPEA2 $problemName"_"$M >> $problemName"_"$M"_"variable.csv
                        java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.reasoning.runReasoningEngineOverResults $spea2_var $spea2 0 200 $m $run SPEA2 $problemName"_"$M >> $problemName"_"$M"_"objective.csv
                        

                        let run=$run+1;
		done
		
	done
done
