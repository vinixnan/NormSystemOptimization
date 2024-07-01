package ie.ucd.cs.mas3.normsystem.reasoning;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author viniciusdecarvalho
 */
public class AgentKind {

    public static final int NUMBER_OF_DIFFERENT_AGENTS = 3;
    public static final int FAIR = 0;
    public static final int SELFISH = 1;
    public static final int MODERATE_SELFISH = 2;

    public static double[] generateWeights(int nObj, int preferedObj, int kind, Random rdn) {
        double[] weights = new double[nObj];
        switch (kind) {
            case FAIR:
                Arrays.fill(weights, 1.0 / ((double) nObj));
                break;
            case SELFISH: {
                double preferedWeight = rdn.nextDouble(0.8, 1);
                double remainingWight = (1.0 - preferedWeight) / (nObj - 1);
                Arrays.fill(weights, remainingWight);
                weights[preferedObj] = preferedWeight;
                break;
            }
            case MODERATE_SELFISH: {
                double preferedWeight = rdn.nextDouble(0.6, 0.79);
                double remainingWight = (1.0 - preferedWeight) / (nObj - 1);
                Arrays.fill(weights, remainingWight);
                weights[preferedObj] = preferedWeight;
                break;
            }
            default:
                break;
        }
        return weights;
    }
}
