package ie.ucd.cs.mas3.normsystem.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
 * @author vinicius
 */
public class cleanPfX {

    public static void main(String[] args) throws FileNotFoundException {

        String pfKnown = args[0];
        String pfKnownalg = args[1];

        Front pfFrontalg = new ArrayFront(pfKnownalg);
        Front pfFront = new ArrayFront(pfKnown);;

        String dirAlg = pfKnown + ".Clean";
        String dir = pfKnown + ".removedDominated.file";

        int m = Integer.parseInt(args[2]);

        
        File f = new File(dir);
        if (f.exists()) {
            pfFront = new ArrayFront(dir);
        } else {
            //generate ndominated front
            List<PointSolution> pflist = FrontUtils.convertFrontToSolutionList(pfFront);
            NonDominatedSolutionListArchive nd = new NonDominatedSolutionListArchive();
            for (PointSolution sol : pflist) {
                calculateHypervolumeForENGNottsAlgs.revertToMaximization(sol, sol.getNumberOfObjectives());
                nd.add(sol);
            }
            pflist = nd.getSolutionList();
            for (PointSolution sol : pflist) {
                calculateHypervolumeForENGNottsAlgs.revertToMaximization(sol, sol.getNumberOfObjectives());
            }
            pfFront = new ArrayFront(pflist);
            new SolutionListOutput(pflist)
                    .setSeparator("\t")
                    .setFunFileOutputContext(new DefaultFileOutputContext(dir))
                    .print();
        }

        List<PointSolution> pflist = FrontUtils.convertFrontToSolutionList(pfFrontalg);
        NonDominatedSolutionListArchive nd = new NonDominatedSolutionListArchive();
        for (PointSolution sol : pflist) {
            calculateHypervolumeForENGNottsAlgs.revertToMaximization(sol, sol.getNumberOfObjectives());
            nd.add(sol);
        }
        pflist = nd.getSolutionList();
        for (PointSolution sol : pflist) {
            calculateHypervolumeForENGNottsAlgs.revertToMaximization(sol, sol.getNumberOfObjectives());
        }

        double[] nadir = FrontUtils.getMinimumValues(pfFront);
        pflist = removeWorseThanNadir(pflist, nadir, m);

        new SolutionListOutput(pflist)
                .setSeparator("\t")
                .setFunFileOutputContext(new DefaultFileOutputContext(dirAlg))
                .print();

    }

    public static List<PointSolution> removeWorseThanNadir(List<PointSolution> population, double[] nadir, int m) {
        List<PointSolution> newpopulation = new ArrayList();
        for (PointSolution s : population) {
            boolean stillOk = true;
            for (int i = 0; i < m && stillOk; i++) {
                if (nadir[i] > s.getObjective(i)) {
                    //System.err.println(nadir[i]+" menor "+s.getObjective(i));
                    stillOk = false;
                }

            }
            if (stillOk) {

                newpopulation.add(s);
            }
        }
        return newpopulation;
    }

}
