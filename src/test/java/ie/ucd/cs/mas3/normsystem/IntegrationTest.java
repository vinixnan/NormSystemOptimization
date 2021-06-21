package ie.ucd.cs.mas3.normsystem;

import ie.ucd.cs.mas3.normsystem.problem.Functions;
import ie.ucd.cs.mas3.normsystem.problem.Society;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Vinicius Renan de Carvalho
 */
public class IntegrationTest {

    private Society scBegin;
    private Society scEnd;
    private Functions fn;

    private void prepareData() {
        String jsonBegin = "{'num_segments': 5, 'num_agents': 20, 'num_evaders': 10, 'collecting_rates': [0.5488135039273248, 0.7151893663724195, 0.6027633760716439, 0.5448831829968969, 0.4236547993389047], 'redistribution_rates': [0.19440830746079796, 0.1317098072068841, 0.2684156368884406, 0.29005380671842657, 0.11541244172545075], 'catch': 2, 'fine_rate': 0.5288949197529045, 'invest_rate': 0.05, 'common_fund': 0.0, 'agents': [{'model': '', 'wealth': 96.98090677467488, 'segment': 4, 'position': 2, 'is_evader': False, 'old_wealth': -1}, {'model': '', 'wealth': 65.31400357979376, 'segment': 3, 'position': 6, 'is_evader': False, 'old_wealth': -1}, {'model': '', 'wealth': 17.090958513604516, 'segment': 0, 'position': 19, 'is_evader': False, 'old_wealth': -1}, {'model': '', 'wealth': 35.815216696952504, 'segment': 1, 'position': 15, 'is_evader': False, 'old_wealth': -1}, {'model': '', 'wealth': 75.06861412184563, 'segment': 4, 'position': 4, 'is_evader': False, 'old_wealth': -1}, {'model': '', 'wealth': 60.78306687154678, 'segment': 2, 'position': 11, 'is_evader': False, 'old_wealth': -1}, {'model': '', 'wealth': 32.50472290083525, 'segment': 0, 'position': 17, 'is_evader': True, 'old_wealth': -1}, {'model': '', 'wealth': 3.8425426472734725, 'segment': 0, 'position': 20, 'is_evader': True, 'old_wealth': -1}, {'model': '', 'wealth': 63.427405795733506, 'segment': 2, 'position': 9, 'is_evader': True, 'old_wealth': -1}, {'model': '', 'wealth': 95.89492686245204, 'segment': 4, 'position': 3, 'is_evader': False, 'old_wealth': -1}, {'model': '', 'wealth': 65.27903170054908, 'segment': 3, 'position': 7, 'is_evader': True, 'old_wealth': -1}, {'model': '', 'wealth': 63.50588736035638, 'segment': 3, 'position': 8, 'is_evader': True, 'old_wealth': -1}, {'model': '', 'wealth': 99.52995676778876, 'segment': 4, 'position': 1, 'is_evader': True, 'old_wealth': -1}, {'model': '', 'wealth': 58.18503294385343, 'segment': 2, 'position': 12, 'is_evader': True, 'old_wealth': -1}, {'model': '', 'wealth': 41.43685882263688, 'segment': 1, 'position': 14, 'is_evader': True, 'old_wealth': -1}, {'model': '', 'wealth': 47.46975022884129, 'segment': 1, 'position': 13, 'is_evader': False, 'old_wealth': -1}, {'model': '', 'wealth': 62.351010113186824, 'segment': 2, 'position': 10, 'is_evader': False, 'old_wealth': -1}, {'model': '', 'wealth': 33.800761483889175, 'segment': 1, 'position': 16, 'is_evader': True, 'old_wealth': -1}, {'model': '', 'wealth': 67.47523222590208, 'segment': 3, 'position': 5, 'is_evader': True, 'old_wealth': -1}, {'model': '', 'wealth': 31.720174206929606, 'segment': 0, 'position': 18, 'is_evader': False, 'old_wealth': -1}], 'time_step': 0}";
        String jsonEnd = "{'num_segments': 5, 'num_agents': 20, 'num_evaders': 10, 'collecting_rates': [0.5488135039273248, 0.7151893663724195, 0.6027633760716439, 0.5448831829968969, 0.4236547993389047], 'redistribution_rates': [0.19440830746079796, 0.1317098072068841, 0.2684156368884406, 0.29005380671842657, 0.11541244172545075], 'catch': 2, 'fine_rate': 0.5288949197529045, 'invest_rate': 0.05, 'common_fund': 1018.3875548459123, 'agents': [{'model': '', 'wealth': 119.82184186148653, 'segment': 4, 'position': 1, 'is_evader': False, 'old_wealth': 92.90517004625391, 'payment': 50.62246477167079, 'payback': 77.53913658690341}, {'model': '', 'wealth': 89.21308212625985, 'segment': 3, 'position': 8, 'is_evader': False, 'old_wealth': 101.25919509406623, 'payment': 42.898943978795636, 'payback': 30.852831010989256}, {'model': '', 'wealth': 56.33593516120578, 'segment': 0, 'position': 17, 'is_evader': False, 'old_wealth': 74.17694489871593, 'payment': 53.05056222155452, 'payback': 35.20955248404437}, {'model': '', 'wealth': 76.58322236632027, 'segment': 1, 'position': 13, 'is_evader': False, 'old_wealth': 54.55102236085761, 'payment': 29.938337724680107, 'payback': 51.970537730142766}, {'model': '', 'wealth': 96.05656898880116, 'segment': 3, 'position': 5, 'is_evader': False, 'old_wealth': 113.13313254455856, 'payment': 47.929394566746666, 'payback': 30.852831010989256}, {'model': '', 'wealth': 97.79657001765324, 'segment': 4, 'position': 4, 'is_evader': False, 'old_wealth': 116.15215834169578, 'payment': 49.20841933503181, 'payback': 30.852831010989256}, {'model': '', 'wealth': 35.20955248404437, 'segment': 0, 'position': 20, 'is_evader': True, 'old_wealth': 55.69289821176526, 'payment': 55.69289821176526, 'payback': 35.20955248404437}, {'model': '', 'wealth': 57.46433050990313, 'segment': 1, 'position': 16, 'is_evader': True, 'old_wealth': 34.139513914652646, 'payment': 28.645721134892284, 'payback': 51.970537730142766}, {'model': '', 'wealth': 77.6590232834658, 'segment': 2, 'position': 10, 'is_evader': True, 'old_wealth': 75.27394467956378, 'payment': 69.36959673107015, 'payback': 71.75467533497216}, {'model': '', 'wealth': 116.83993129559431, 'segment': 4, 'position': 2, 'is_evader': False, 'old_wealth': 86.35320260737132, 'payment': 47.052407898680414, 'payback': 77.53913658690341}, {'model': '', 'wealth': 92.50146965679755, 'segment': 3, 'position': 6, 'is_evader': True, 'old_wealth': 89.63191230366971, 'payment': 74.66957923377556, 'payback': 77.53913658690341}, {'model': '', 'wealth': 92.50146962700481, 'segment': 3, 'position': 7, 'is_evader': True, 'old_wealth': 89.6319121251961, 'payment': 74.6695790850947, 'payback': 77.53913658690341}, {'model': '', 'wealth': 77.6590232834372, 'segment': 2, 'position': 11, 'is_evader': True, 'old_wealth': 75.27394467919895, 'payment': 69.36959673073392, 'payback': 71.75467533497216}, {'model': '', 'wealth': 77.65902328341959, 'segment': 2, 'position': 12, 'is_evader': True, 'old_wealth': 75.27394467897456, 'payment': 69.36959673052714, 'payback': 71.75467533497216}, {'model': '', 'wealth': 57.46433050990313, 'segment': 1, 'position': 15, 'is_evader': True, 'old_wealth': 34.139513914652646, 'payment': 28.645721134892284, 'payback': 51.970537730142766}, {'model': '', 'wealth': 56.33542066672219, 'segment': 0, 'position': 18, 'is_evader': False, 'old_wealth': 74.17513845463398, 'payment': 53.04927027195616, 'payback': 35.20955248404437}, {'model': '', 'wealth': 104.60984285603682, 'segment': 4, 'position': 3, 'is_evader': False, 'old_wealth': 82.70931113086459, 'payment': 49.854143609799934, 'payback': 71.75467533497216}, {'model': '', 'wealth': 57.46433050990313, 'segment': 1, 'position': 14, 'is_evader': True, 'old_wealth': 34.139513914652646, 'payment': 28.645721134892284, 'payback': 51.970537730142766}, {'model': '', 'wealth': 35.20955248404437, 'segment': 0, 'position': 19, 'is_evader': True, 'old_wealth': 55.69289821176526, 'payment': 55.69289821176526, 'payback': 35.20955248404437}, {'model': '', 'wealth': 85.28660320642501, 'segment': 2, 'position': 9, 'is_evader': False, 'old_wealth': 94.44647432302312, 'payment': 40.012702127587374, 'payback': 30.852831010989256}], 'time_step': 10}";
        int totalRun = 10;

        scBegin = new Society();
        scBegin.createFromJSON(jsonBegin);

        scEnd = new Society();
        scEnd.createFromJSON(jsonEnd);

        fn = new Functions(totalRun);
    }

    @Test
    public void testEqualityComplete() {
        this.prepareData();
        double returned = fn.equality_single_path(scBegin);
        System.out.println(returned);
        assertTrue(scBegin.equals(scEnd));
        assertEquals(0.6582872293204067, returned, 0.0001);
    }

    @Test
    public void testFairnessComplete() {
        this.prepareData();
        double returned = fn.fairness_single_path(scBegin);
        System.out.println(returned);
        assertTrue(scBegin.equals(scEnd));
        assertEquals(-0.6, returned, 0.0001);
    }

    @Test
    public void testnewWealth_single_pathComplete() {
        this.prepareData();
        double returned = fn.newWealth_single_path(scBegin);
        System.out.println(returned);
        assertTrue(scBegin.equals(scEnd));
        assertEquals(0.2814636109355926, returned, 0.0001);
    }

    @Test
    public void testgainedAmount_single_pathComplete() {
        this.prepareData();
        double returned = fn.gainedAmount_single_path(scBegin);
        System.out.println(returned);
        assertTrue(scBegin.equals(scEnd));
        assertEquals(-0.02299774502636816, returned, 0.0001);
    }

    @Test
    public void testredistributeAmount_single_pathComplete() {
        this.prepareData();
        double returned = fn.redistributeAmount_single_path(scBegin);
        System.out.println(returned);
        assertTrue(scBegin.equals(scEnd));
        assertEquals(0.1317098072068841, returned, 0.0001);
    }

    @Test
    public void testcollectAmount_single_pathComplete() {
        this.prepareData();
        double returned = fn.collectAmount_single_path(scBegin);
        System.out.println(returned);
        assertTrue(scBegin.equals(scEnd));
        assertEquals(0.45118649607267525, returned, 0.0001);
    }

    @Test
    public void testaggregation_single_pathComplete() {
        this.prepareData();
        double returned = fn.aggregation_single_path(scBegin);
        System.out.println(returned);
        assertTrue(scBegin.equals(scEnd));
        assertEquals(-0.3949916909786685, returned, 0.0001);
    }
}
