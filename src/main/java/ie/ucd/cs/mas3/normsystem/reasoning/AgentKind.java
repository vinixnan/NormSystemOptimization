package ie.ucd.cs.mas3.normsystem.reasoning;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author viniciusdecarvalho
 */
public class AgentKind {

    public static byte[] seed = SecureRandom.getSeed((int) System.currentTimeMillis());
    public static final int NUMBER_OF_DIFFERENT_AGENTS = 3; //update to 6 after implementing
    public static final int FAIR = 0;
    public static final int SELFISH = 1;
    public static final int MODERATE_SELFISH = 2;
    public static final int SUBJECTIVE = 3;
    public static final int MODERATE_SELFISH_SUBJECTIVE = 4;
    public static final int MODERATE_SELFISH_FAIR = 5;

    public static double[] generateWeights(int nObj, int preferedObj, int kind) {
        double[] weights = new double[nObj];
        Random rdn = new SecureRandom(seed);
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
