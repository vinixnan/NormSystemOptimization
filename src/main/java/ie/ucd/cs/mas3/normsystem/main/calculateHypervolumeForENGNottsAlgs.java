package ie.ucd.cs.mas3.normsystem.main;

import br.usp.poli.pcs.lti.jmetalhhhelper.util.metrics.Calculator;
import br.usp.poli.pcs.lti.jmetalhhhelper.util.metrics.EpsilonCalculator;
import br.usp.poli.pcs.lti.jmetalhhhelper.util.metrics.HypervolumeCalculator;
import br.usp.poli.pcs.lti.jmetalhhhelper.util.metrics.IgdCalculator;
import br.usp.poli.pcs.lti.jmetalhhhelper.util.metrics.IgdPlusCalculator;
import br.usp.poli.pcs.lti.jmetalhhhelper.util.metrics.WFGHypervolumeCalculator;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
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
public class calculateHypervolumeForENGNottsAlgs {

    public static void main(String[] args) throws FileNotFoundException {
        //System.out.println(Arrays.toString(args));
        if(args.length==0){
            String[] aux={"NormSystem_obj_2", 
                "PFconf_200_10_5_0.05_10_5000_2", "2", "0", "1", 
                "result/Mombi2/NormSystem_obj_2/conf_200_10_5_0.05_10_5000/200/FUN_ALLMIN1", 
                "result/MoeaDD/NormSystem_obj_2/conf_200_10_5_0.05_10_5000/200/FUN_ALLMIN1", 
                "result/Nsgaii/NormSystem_obj_2/conf_200_10_5_0.05_10_5000/200/FUN_ALLMIN1", 
                "result/Spea2/NormSystem_obj_2/conf_200_10_5_0.05_10_5000/200/FUN_ALLMIN1"};
            args=aux;
        }
        String problemName = args[0];
        String pfKnown = args[1];
        int m = Integer.parseInt(args[2]);
        int type = Integer.parseInt(args[3]);
        int runUpdate = Integer.parseInt(args[4]);
        String[] paths = new String[args.length - 5];
        for (int i = 5; i < args.length; i++) {
            paths[i - 5] = args[i];
        }
        Front[] fronts = new Front[paths.length];
        for (int i = 0; i < fronts.length; i++) {
            fronts[i] = new ArrayFront(paths[i]);
        }
        Front pfFront = new ArrayFront(pfKnown);

        String dir = pfKnown + ".removedDominated.file";

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
        pfFront = new ArrayFront(pfKnown);
        double[] nadir = FrontUtils.getMaximumValues(pfFront);
        double[] min = FrontUtils.getMinimumValues(pfFront);
        System.err.println(Arrays.toString(nadir) + "  " + Arrays.toString(min));
        if (runUpdate == 1) {
            //convert into minimization
            pfFront = calculateHypervolumeForENGNottsAlgs.revertToMaximization(pfFront, m);
            for (int j = 0; j < fronts.length; j++) {
                fronts[j] = calculateHypervolumeForENGNottsAlgs.revertToMaximization(fronts[j], m);
            }
            double constant = 100;
            //Find nadir
            nadir = FrontUtils.getMaximumValues(pfFront);
            min = FrontUtils.getMinimumValues(pfFront);
            for (int i = 0; i < m; i++) {
                nadir[i] = nadir[i] + constant;
                min[i] = min[i] + constant;
            }

            //update points
            calculateHypervolumeForENGNottsAlgs.updatePoint(pfFront, nadir, constant);
            for (int j = 0; j < fronts.length; j++) {
                calculateHypervolumeForENGNottsAlgs.updatePoint(fronts[j], nadir, constant);
            }

            //for (int i = 0; i < fronts.length; i++) {
              //  fronts[i] = calculateHypervolumeForENGNottsAlgs.removeWorseThanNadir(fronts[i], nadir, m);
            //}
        }
        System.err.println(Arrays.toString(nadir) + "  " + Arrays.toString(min));

        Calculator fhc;
        switch (type) {
            case 0:
                fhc = new HypervolumeCalculator(m, pfFront);
                break;
            case 1:
                fhc = new IgdCalculator(m, pfFront);
                break;
            case 2:
                fhc = new EpsilonCalculator(m, pfFront);
                break;
            case 3:
                fhc = new IgdPlusCalculator(m, pfFront);
                fhc.setNormalize(runUpdate == 1);
                break;
            case 4:
                fhc = new WFGHypervolumeCalculator(m, pfFront);
                break;
            default:
                fhc = new HypervolumeCalculator(m, pfFront);
                break;
        }

        double[] values = new double[fronts.length];

        for (int i = 0; i < fronts.length; i++) {
            values[i] = calculateHypervolumeForENGNottsAlgs.calc(fhc, fronts[i]);
        }

        DecimalFormat nf = new DecimalFormat("###.####################");
        String valueHH = "";
        for (int i = 0; i < fronts.length; i++) {
            valueHH += nf.format(values[i]) + ";";
        }
        System.out.println(problemName + ";" + type + ";" + runUpdate + ";" + valueHH);
    }

    public static void updatePoint(Front front, double[] nadir, double constant) {
        for (int i = 0; i < front.getNumberOfPoints(); i++) {
            for (int j = 0; j < nadir.length; j++) {
                front.getPoint(i).setValue(j, front.getPoint(i).getValue(j) + constant);
            }
        }
    }

    public static void revertToMaximization(PointSolution s, int m) {
        for (int i = 0; i < m; i++) {
            s.setObjective(i, -1 * s.getObjective(i));
        }
    }
    
    public static void revertToMaximization(DoubleSolution s, int m) {
        for (int i = 0; i < m; i++) {
            s.setObjective(i, -1 * s.getObjective(i));
        }
    }

    public static Front revertToMaximization(Front front, int m) {
        List<PointSolution> population = FrontUtils.convertFrontToSolutionList(front);
        for (PointSolution s : population) {
            calculateHypervolumeForENGNottsAlgs.revertToMaximization(s, m);
        }
        return new ArrayFront(population);
    }

    public static double calc(Calculator fhc, Front front) {
        if (front == null) {
            return 0D;
        }
        return fhc.execute(front);
    }

    public static ArrayFront removeWorseThanNadir(Front front, double[] nadir, int m) {

        List population = FrontUtils.convertFrontToSolutionList(front);
        List newpopulation = new ArrayList();
        for (Object o : population) {
            Solution s = (Solution) o;
            boolean stillOk = true;
            for (int i = 0; i < m && stillOk; i++) {
                if (i != 0 && s.getObjective(i) < 0) {
                    //wont accept negatives except for obj 0
                    stillOk = false;
                }
                if (nadir[i] < s.getObjective(i) || s.getObjective(i) < 0) {
                    //System.err.println(nadir[i]+" menor "+s.getObjective(i));
                    stillOk = false;
                }

            }
            if (stillOk) {

                newpopulation.add(s);
            }
        }
        if (!newpopulation.isEmpty()) {
            return new ArrayFront(newpopulation);
        }
        return null;
    }

}
