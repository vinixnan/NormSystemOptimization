qtdExp=30
problemNameBase="NormSystem"
MaxIteractions=200
MaxIteractions=500

ms="2 5"



for m in $ms
do
        pf="PF"$m
        alldata="nalldata_"$m"obj.csv"
        rm -f $pf
        rm -f $alldata
        echo "problem;type;runupdate;mombi2;moead;nsgaii;spea2" > $alldata
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
		pf="PF"$M"_"$m
		pfmombi2="result/Mombi2/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN_ALL"
		pfmoead="result/MoeaDD/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN_ALL"
		pfnsgaii="result/Nsgaii/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN_ALL"
		pfspea2="result/Spea2/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN_ALL"
		rm -f $pfmombi2 2> /dev/null
		rm -f $pfmoead 2> /dev/null
		rm -f $pfnsgaii 2> /dev/null
		rm -f $pfspea2 2> /dev/null
                touch $pfmombi2
                touch $pfmoead
                touch $pfnsgaii
                touch $pfspea2
		run=1
		while [ $run -le $qtdExp ]
		do
			mombi2="result/Mombi2/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN"$run
                        moead="result/MoeaDD/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN"$run
                        nsgaii="result/Nsgaii/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN"$run
                        spea2="result/Spea2/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN"$run

			cat $mombi2 >> $pfmombi2
			cat $moead >> $pfmoead
			cat $nsgaii >> $pfnsgaii
			cat $spea2 >> $pfspea2
			let run=$run+1;
		done
		
		#clean files
                java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.main.cleanPf $pfmombi2
                java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.main.cleanPf $pfmoead
                java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.main.cleanPf $pfnsgaii
                java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.main.cleanPf $pfspea2

                cat $pfmombi2".Clean" > $pf
                cat $pfmoead".Clean" >> $pf
                cat $pfnsgaii".Clean" >> $pf
                cat $pfspea2".Clean" >> $pf


		run=1
		while [ $run -le $qtdExp ]
		do
			mombi2="result/Mombi2/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN"$run
                        moead="result/MoeaDD/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN"$run
                        nsgaii="result/Nsgaii/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN"$run
                        spea2="result/Spea2/"$problemName"/"$M"/"$MaxIteractions"/FUN_ALLMIN"$run

                        java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.main.calculateHypervolumeForENGNottsAlgs $problemName"_"$M $pf $m 0 1 $mombi2 $moead $nsgaii $spea2 >> $alldata
                        #java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.main.calculateHypervolumeForENGNottsAlgs $problemName"_"$M $pf $m 0 0 $mombi2 $moead $nsgaii $spea2 >> $alldata
                        #java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.main.calculateHypervolumeForENGNottsAlgs $problemName"_"$M $pf $m 4 1 $mombi2 $moead $nsgaii $spea2 >> $alldata
                        #java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.main.calculateHypervolumeForENGNottsAlgs $problemName"_"$M $pf $m 4 0 $mombi2 $moead $nsgaii $spea2 >> $alldata
                        #java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.main.calculateHypervolumeForENGNottsAlgs $problemName"_"$M $pf $m 1 0 $mombi2 $moead $nsgaii $spea2 >> $alldata
                        #java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.main.calculateHypervolumeForENGNottsAlgs $problemName"_"$M $pf $m 3 0 $mombi2 $moead $nsgaii $spea2 >> $alldata
                        java -Xms1024m -Xmx1024m -cp './target/NormSystemOptimization-1.0-SNAPSHOT.jar:./target/lib/*' ie.ucd.cs.mas3.normsystem.main.calculateHypervolumeForENGNottsAlgs $problemName"_"$M $pf $m 3 1 $mombi2 $moead $nsgaii $spea2 >> $alldata
			let run=$run+1;
		done
		
	done
done
