package ie.ucd.cs.mas3.normsystem;

import ie.ucd.cs.mas3.normsystem.problem.Individual;
import ie.ucd.cs.mas3.normsystem.problem.Functions;
import ie.ucd.cs.mas3.normsystem.problem.Society;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author Vinicius Renan de Carvalho
 */
public class BaseProblemTest extends TestCase {

    public Society generateBase() {
        int num_agents = 20;
        int num_evaders = 10;
        double invest_rate = 0.05;
        double[] collecting_rate = {0.5488135039273248, 0.7151893663724195, 0.6027633760716439, 0.5448831829968969, 0.4236547993389047};
        double[] redistribution_rates = {0.19440830746079796, 0.1317098072068841, 0.2684156368884406, 0.29005380671842657, 0.11541244172545075};
        double catche = 0.3958625190413323;
        double fine_rate = 0.5288949197529045;
        /*{'num_agents': 200, 'num_evaders': 10, 'invest_rate': 0.05, 
        'collecting_rates': [0.6478898663769349, 0.7134226815101801, 0.08904775878693694, 0.04600230062452726, 0.6034071555626516], 
        'redistribution_rates': [0.015188582298102793, 0.2716671931032027, 0.03478122550201137, 0.3916746189756817, 0.2866883801210014], 'catch': 0.4058077132698487, 'fine_rate': 0.36355946153811125}gens 0.24268958843571323*/
        Society sc = new Society(collecting_rate, redistribution_rates, num_agents, num_evaders, fine_rate, invest_rate);
        return sc;
    }

    @Test
    public void testEqualityNoWalk() {
        Society sc = this.generateBase();
        double[] weaths = {
            0.23587385934796812, 15.2951836929746, 21.420288087864925, 60.07979084694459, 0.5051886281540852, 55.40775414175477, 73.54301346986657, 62.32684485583187, 86.9597079209005, 6.463258289875762, 85.57718153995056, 63.86670596189635, 91.42683787732592, 61.154678314608745, 41.967632440338576, 99.30554030603918, 28.936482530375507, 66.20574992988337, 7.943875954400359, 99.45824098516304
        };
        sc.setWeath(weaths);
        Functions.length = 0;
        double returned = Functions.equality_single_path(sc);
        assertEquals(0.2703962133238431, returned, 0.0001);
        System.out.println(returned);
    }

    @Test
    public void testFairNessNoWalk() {
        Society sc = this.generateBase();
        boolean[] evaders = {
            false, true, false, true, true, false, true, true, false, true, true, false, true, false, false, true, false, false, true, false
        };
        int[] segments = {0, 4, 1, 2, 2, 1, 4, 0, 1, 0, 4, 3, 3, 0, 3, 4, 3, 2, 2, 1};
        sc.setEvaders(evaders);
        sc.setSegments(segments);
        Functions.length = 0;
        double returned = Functions.fairness_single_path(sc);
        assertEquals(-0.6, returned, 0.0001);
        System.out.println(returned);
    }

    @Test
    public void testNewWeathNessNoWalk() {
        Society sc = this.generateBase();
        double[] weaths = {0.18651123159715288, 97.21198050042787, 25.19444936172067, 71.01132100813365, 21.45533302241166, 17.73795874956988, 65.36553460570921, 8.762885757889471, 88.32334146734337, 68.16382655544786, 29.52650327341648, 56.33162743233267, 69.87462044885105, 48.99631288244225, 46.05472991310957, 16.397870991701268, 55.852501509252185, 77.87766428490374, 99.2790733873843, 64.04467925600036};
        int[] segments = {0, 4, 1, 3, 1, 0, 3, 0, 4, 3, 1, 2, 3, 2, 1, 0, 2, 4, 4, 2};
        sc.setSegments(segments);
        sc.setWeath(weaths);
        Functions.length = 0;
        double returned = Functions.newWealth_single_path(sc);
        assertEquals(0.35293388741790827, returned, 0.0001);
        System.out.println(returned);
    }

    @Test
    public void testJSON() {
        String json = "{'num_segments': 5, 'num_agents': 20, 'num_evaders': 10, 'collecting_rates': [0.5488135039273248, 0.7151893663724195, 0.6027633760716439, 0.5448831829968969, 0.4236547993389047], 'redistribution_rates': [0.19440830746079796, 0.1317098072068841, 0.2684156368884406, 0.29005380671842657, 0.11541244172545075], 'catch': 0.3958625190413323, 'fine_rate': 0.5288949197529045, 'invest_rate': 0.05, 'common_fund': 656.1646011506458, 'agents': [{'model': '', 'wealth': 25.92444305263625, 'segment': 2, 'position': 9, 'is_evader': False, 'old_wealth': 65.26196601980956, 'payment': 39.337522967173314, 'payback': 43.461083673171345}, {'model': '', 'wealth': 79.51414435274486, 'segment': 3, 'position': 5, 'is_evader': False, 'old_wealth': 71.51893663724195, 'payment': 38.969465839453775, 'payback': 46.9646735549567}, {'model': '', 'wealth': 48.189047270782474, 'segment': 1, 'position': 14, 'is_evader': True, 'old_wealth': 60.276337607164386, 'payment': 55.54837400955326, 'payback': 43.461083673171345}, {'model': '', 'wealth': 36.84492354146275, 'segment': 1, 'position': 16, 'is_evader': False, 'old_wealth': 54.48831829968969, 'payment': 38.969465839453775, 'payback': 21.326071081226836}, {'model': '', 'wealth': 21.326071081226836, 'segment': 0, 'position': 20, 'is_evader': True, 'old_wealth': 42.36547993389047, 'payment': 42.36547993389047, 'payback': 21.326071081226836}, {'model': '', 'wealth': 48.52735673440576, 'segment': 1, 'position': 13, 'is_evader': True, 'old_wealth': 64.58941130666561, 'payment': 59.5231382454312, 'payback': 43.461083673171345}, {'model': '', 'wealth': 21.326071081226836, 'segment': 0, 'position': 19, 'is_evader': True, 'old_wealth': 43.75872112626925, 'payment': 43.75872112626925, 'payback': 21.326071081226836}, {'model': '', 'wealth': 70.08415770206614, 'segment': 3, 'position': 7, 'is_evader': False, 'old_wealth': 89.17730007820798, 'payment': 37.78039117021849, 'payback': 18.687248794076645}, {'model': '', 'wealth': 52.63480681454729, 'segment': 2, 'position': 10, 'is_evader': True, 'old_wealth': 96.36627605010293, 'payment': 62.41871802963228, 'payback': 18.687248794076645}, {'model': '', 'wealth': 48.77839445250876, 'segment': 2, 'position': 12, 'is_evader': False, 'old_wealth': 38.34415188257777, 'payment': 21.04378834979903, 'payback': 31.478030919730024}, {'model': '', 'wealth': 126.13717736322315, 'segment': 4, 'position': 1, 'is_evader': True, 'old_wealth': 79.17250380826646, 'payment': 0.0, 'payback': 46.9646735549567}, {'model': '', 'wealth': 74.21556305651728, 'segment': 3, 'position': 6, 'is_evader': True, 'old_wealth': 52.88949197529045, 'payment': 0.0, 'payback': 21.326071081226836}, {'model': '', 'wealth': 66.0258940421532, 'segment': 3, 'position': 8, 'is_evader': False, 'old_wealth': 56.80445610939323, 'payment': 34.239645740411376, 'payback': 43.461083673171345}, {'model': '', 'wealth': 111.24691262334275, 'segment': 4, 'position': 2, 'is_evader': True, 'old_wealth': 92.5596638292661, 'payment': 0.0, 'payback': 18.687248794076645}, {'model': '', 'wealth': 34.68308193904195, 'segment': 0, 'position': 17, 'is_evader': False, 'old_wealth': 7.103605819788694, 'payment': 3.898554800476769, 'payback': 31.478030919730024}, {'model': '', 'wealth': 40.190960889884096, 'segment': 1, 'position': 15, 'is_evader': True, 'old_wealth': 8.712929970154072, 'payment': 0.0, 'payback': 31.478030919730024}, {'model': '', 'wealth': 32.390257709460556, 'segment': 0, 'position': 18, 'is_evader': False, 'old_wealth': 2.021839744032572, 'payment': 1.1096129543020412, 'payback': 31.478030919730024}, {'model': '', 'wealth': 84.85860294289598, 'segment': 4, 'position': 3, 'is_evader': False, 'old_wealth': 83.2619845547938, 'payment': 45.36805516685451, 'payback': 46.9646735549567}, {'model': '', 'wealth': 82.37989591713394, 'segment': 4, 'position': 4, 'is_evader': False, 'old_wealth': 77.81567509498505, 'payment': 42.400452732807814, 'payback': 46.9646735549567}, {'model': '', 'wealth': 49.335717419934326, 'segment': 2, 'position': 11, 'is_evader': True, 'old_wealth': 87.00121482468191, 'payment': 56.35274619882423, 'payback': 18.687248794076645}], 'time_step': 1}";
        Society sc = new Society();
        sc.createFromJSON(json);
        String jsonAgent1After = "{'model': '', 'wealth': 10.29813823545215, 'segment': 2, 'position': 9, 'is_evader': False, 'old_wealth': 25.92444305263625, 'payment': 15.6263048171841, 'payback': 43.461083673171345}";
        Individual firstAgent = sc.getAgents().get(0);
        Individual toCompare = new Individual();
        toCompare.createFromJSON(jsonAgent1After);
        firstAgent.step(sc.getCommon_fund(), sc.getCollecting_rates(), sc.getFine_rate());
        assertTrue(firstAgent.equals(toCompare));
    }

}
