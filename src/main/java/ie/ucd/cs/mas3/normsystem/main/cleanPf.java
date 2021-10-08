package ie.ucd.cs.mas3.normsystem.main;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchive;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

/**
 *
 * 
 */
public class cleanPf {

    public static void main(String[] args) throws FileNotFoundException {

        String pfKnown = args[0];

        Front pfFront = new ArrayFront(pfKnown);

        String dir = pfKnown + ".Clean";
        int m=0;
        
        List<PointSolution> pflist = FrontUtils.convertFrontToSolutionList(pfFront);
        NonDominatedSolutionListArchive nd = new NonDominatedSolutionListArchive();
        for (PointSolution sol : pflist) {
            calculateHypervolumeForENGNottsAlgs.revertToMaximization(sol, sol.getNumberOfObjectives());
            nd.add(sol);
            m=sol.getNumberOfObjectives();
        }
        pflist = nd.getSolutionList();
        for (PointSolution sol : pflist) {
            calculateHypervolumeForENGNottsAlgs.revertToMaximization(sol, sol.getNumberOfObjectives());
        }
        PointSolution[] bests=new PointSolution[m];
        double[] bestValues=new double[m];
        for (PointSolution sol : pflist) {
            for (int i = 0; i < m; i++) {
                if(sol.getObjective(i) > bestValues[i]){
                    bestValues[i]=sol.getObjective(i);
                    bests[i]=sol;
                }
            }
        }
        DecimalFormat nf = new DecimalFormat("###.########");
        System.out.println(Arrays.toString(bestValues));
        for (int i = 0; i < m; i++) {
            String sep="[";
            for (int k = 0; k < m; k++) {
                System.out.print(sep+nf.format(bests[i].getObjective(k)));
                sep=", "; 
            }
            System.out.print("]");
        }
        
        new SolutionListOutput(pflist)
                .setSeparator("\t")
                .setFunFileOutputContext(new DefaultFileOutputContext(dir))
                .print();

    }
    
                }
