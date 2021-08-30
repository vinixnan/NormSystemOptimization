package ie.ucd.cs.mas3.normsystem.main;

import java.io.FileNotFoundException;
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
public class cleanPf {

    public static void main(String[] args) throws FileNotFoundException {

        String pfKnown = args[0];

        Front pfFront = new ArrayFront(pfKnown);

        String dir = pfKnown + ".Clean";
        
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
        new SolutionListOutput(pflist)
                .setSeparator("\t")
                .setFunFileOutputContext(new DefaultFileOutputContext(dir))
                .print();

    }
    
                }
