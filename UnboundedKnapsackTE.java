import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

class KnapsackItem {
    int weight;
    int value;

    public KnapsackItem(int weight, int value) {
        this.weight = weight;
        this.value = value;
    }
}

public class UnboundedKnapsackTE {
    private static final long SEED = 42;
    // private static int maxProfit;

    // public static int knapsack(int capacity, int[] weights, int[] values) {
    // KnapsackItem[] items = new KnapsackItem[weights.length];
    // for (int i = 0; i < weights.length; i++) {
    // items[i] = new KnapsackItem(weights[i], values[i]);
    // }

    // Arrays.sort(items, (a, b) -> Double.compare((double) b.value / b.weight,
    // (double) a.value / a.weight));
    // int n = items.length;
    // int[] currentItems = new int[n];
    // int[] currentWeight = new int[n];
    // int[] currentValue = new int[n];
    // maxProfit = 0;

    // branchAndBound(0, 0, 0, capacity, items, currentItems, currentWeight,
    // currentValue);

    // return maxProfit;
    // }

    // private static void branchAndBound(int level, int currentWeight, int
    // currentValue, int capacity,
    // KnapsackItem[] items, int[] currentItems, int[] currentWeightArray,
    // int[] currentValueArray) {
    // if (currentWeight <= capacity && currentValue > maxProfit) {
    // maxProfit = currentValue;
    // }

    // if (level == items.length) {
    // return;
    // }

    // // Include the item at the current level
    // currentItems[level] = 1;
    // currentWeightArray[level] = currentWeight + items[level].weight;
    // currentValueArray[level] = currentValue + items[level].value;

    // branchAndBound(level + 1, currentWeightArray[level],
    // currentValueArray[level], capacity,
    // items, currentItems, currentWeightArray, currentValueArray);

    // // Exclude the item at the current level
    // currentItems[level] = 0;
    // currentWeightArray[level] = currentWeight;
    // currentValueArray[level] = currentValue;

    // branchAndBound(level + 1, currentWeight, currentValue, capacity,
    // items, currentItems, currentWeightArray, currentValueArray);
    // }

    static int max(int a, int b) {
        return (a > b) ? a : b;
    }

    // Returns the maximum value that
    // can be put in a knapsack of
    // capacity W
    private static int unboundedKnapsack(int W, int n,
            int[] val, int[] wt) {

        // dp[i] is going to store maximum value
        // with knapsack capacity i.
        int dp[] = new int[W + 1];

        // Fill dp[] using above recursive formula
        for (int i = 0; i <= W; i++) {
            for (int j = 0; j < n; j++) {
                if (wt[j] <= i) {
                    dp[i] = max(dp[i], dp[i - wt[j]] +
                            val[j]);
                }
            }
        }
        return dp[W];
    }

    public static int[] generateInputWeight(int size) {
        Random random = new Random(SEED); // Use the fixed seed
        int[] wt = new int[size];

        for (int i = 0; i < size; i++) {
            int weight = random.nextInt(50) + 1;
            wt[i] = weight;
        }

        return wt;
    }

    public static int[] generateInputValue(int size) {
        Random random = new Random(SEED); // Use the fixed seed
        int[] val = new int[size];

        for (int i = 0; i < size; i++) {
            int value = random.nextInt(100) + 1; // Random value between 1 and 200
            val[i] = value;
        }

        return val;
    }

    public static void runtimeCalculatorDP(int W, int n,
            int[] val, int[] wt) {

        long startTime = System.nanoTime();
        int resultDP = unboundedKnapsack(W, n, val, wt);
        long endTime = System.nanoTime();

        double elapsedTimeInSeconds = (endTime - startTime) / 1e9;
        double elapsedTimeInMilliseconds = elapsedTimeInSeconds * 1e3;

        System.out.println(resultDP);
        System.out.printf("Waktu eksekusi DP: %.6f milidetik\n",
                elapsedTimeInMilliseconds);
    }

    public static int[] removeDominatedItems(int[] weights, int[] values) {
        int n = weights.length;
        int[] ndIndices = new int[n];
        for (int i = 0; i < n; i++) {
            ndIndices[i] = i;
        }

        for (int j = 0; j < n - 1; j++) {
            if (ndIndices[j] == -1) {
                continue;
            }
            for (int k = j + 1; k < n; k++) {
                if (ndIndices[k] == -1) {
                    continue;
                }

                int wj = weights[j];
                int wk = weights[k];
                int vj = values[j];
                int vk = values[k];

                if (Math.floor(wk / (double) wj) * vj >= vk) {
                    ndIndices[k] = -1;
                } else if (Math.floor(wj / (double) wk) * vk >= vj) {
                    ndIndices[j] = -1;
                    break;
                }
            }
        }

        int count = 0;
        for (int index : ndIndices) {
            if (index != -1) {
                count++;
            }
        }

        int[] result = new int[count];
        count = 0;
        for (int index : ndIndices) {
            if (index != -1) {
                result[count++] = index;
            }
        }

        return result;
    }

    public static int upperBound(int[] weights, int[] values, int capacity, int nodeIndex) {
        if (weights.length == 1) {
            return (int) Math.floor(capacity / weights[0] * values[0]);
        } else if (weights.length == 2) {
            return (int) (Math.floor(capacity / weights[0] * values[0])
                    + Math.floor((capacity - Math.floor(capacity / weights[0]) * weights[0]) / (double) weights[1]
                            * values[1]));
        }

        int W = capacity;
        int w1 = weights[nodeIndex];
        int w2 = weights[nodeIndex + 1];
        int w3 = weights[nodeIndex + 2];
        int v1 = values[nodeIndex];
        int v2 = values[nodeIndex + 1];
        int v3 = values[nodeIndex + 2];

        int WPrime = W - (int) Math.floor(W / w1) * w1;
        int zPrime = (int) (Math.floor(W / w1) * v1 + Math.floor(WPrime / w2) * v2);
        int WDoublePrime = WPrime - (int) Math.floor(WPrime / w2) * v2;

        int UPrime = zPrime + (int) Math.floor(WDoublePrime * v3 / (double) w3);
        int UDoublePrime = zPrime + (int) Math.floor((WDoublePrime + Math.ceil(1 / w1) * (w2 - WDoublePrime) * w1)
                * (v2 / (double) w2) - Math.ceil(1 / w1) * (w2 - WDoublePrime) * v1);

        int U = Math.max(UPrime, UDoublePrime);

        return U;
    }

    public static int knapsackBnB(int[] values, int[] weights, int capacity) {
        int[] ndIndices = removeDominatedItems(weights, values);
        int[] reducedWeights = new int[ndIndices.length];
        int[] reducedValues = new int[ndIndices.length];

        for (int i = 0; i < ndIndices.length; i++) {
            reducedWeights[i] = weights[ndIndices[i]];
            reducedValues[i] = values[ndIndices[i]];
        }

        int n = reducedWeights.length;
        double[] ratios = new double[n];
        for (int i = 0; i < n; i++) {
            ratios[i] = reducedValues[i] / (double) reducedWeights[i];
        }

        int[] sortedIndices = new int[n];
        for (int i = 0; i < n; i++) {
            sortedIndices[i] = i;
        }

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (ratios[j] > ratios[i]) {
                    int temp = sortedIndices[i];
                    sortedIndices[i] = sortedIndices[j];
                    sortedIndices[j] = temp;
                }
            }
        }

        int[] x = new int[n];
        int[] xHat = new int[n];
        int i = 0;
        int zHat = 0;
        Map<String, Integer> M = new HashMap<>();
        x[0] = capacity / reducedWeights[sortedIndices[0]];
        int VN = reducedValues[sortedIndices[0]] * x[0];
        int WPrime = capacity - reducedWeights[sortedIndices[0]] * x[0];
        int U = upperBound(reducedWeights, reducedValues, capacity, 0);

        int[] mi = new int[reducedWeights.length];
        for (int j = 0; j < mi.length; j++) {
            int[] tempArray = Arrays.copyOfRange(reducedWeights, j + 1, reducedWeights.length);
            mi[j] = tempArray.length > 0 ? Arrays.stream(tempArray).min().orElse(0) : 0;
        }

        if (n == 1) {
            return VN;
        }

        while (true) {
            if (WPrime < mi[i]) {
                if (zHat < VN) {
                    zHat = VN;
                    xHat = Arrays.copyOf(x, x.length);
                    if (zHat == U) {
                        break;
                    }
                }
                i++;
            } else {
                int j = i + 1;
                while (j < n && reducedWeights[sortedIndices[j]] <= WPrime) {
                    j++;
                }

                if (j == n || VN == U || M.get(j + "," + WPrime) != null && M.get(j + "," + WPrime) >= VN) {
                    i++;
                } else {
                    x[sortedIndices[j]] = WPrime / reducedWeights[sortedIndices[j]];
                    VN += reducedValues[sortedIndices[j]] * x[sortedIndices[j]];
                    WPrime -= reducedWeights[sortedIndices[j]] * x[sortedIndices[j]];
                    M.put(i + "," + WPrime, VN);
                    i = j;
                }
            }

            while (true) {
                int j = i;
                while (j >= 0 && x[sortedIndices[j]] == 0) {
                    j--;
                }

                if (j < 0) {
                    return zHat;
                }

                i = j;
                x[i]--;
                VN -= reducedValues[sortedIndices[i]];
                WPrime += reducedWeights[sortedIndices[i]];

                if (i + 1 < reducedValues.length && i + 1 < reducedWeights.length) {
                    if (WPrime >= mi[i - 1] && VN + (int) Math.floor(WPrime * ((reducedValues[sortedIndices[i + 1]])
                            / (double) reducedWeights[sortedIndices[i + 1]])) > zHat) {
                        break;
                    }
                }

                VN -= reducedValues[sortedIndices[i]] * x[sortedIndices[i]];
                WPrime += reducedWeights[sortedIndices[i]] * x[sortedIndices[i]];
                x[sortedIndices[i]] = 0;
            }

            int j = i;
            int h = j + 1;
            while (h < n) {
                if (zHat >= VN + (int) Math.floor(
                        WPrime * ((reducedValues[sortedIndices[h]]) / (double) reducedWeights[sortedIndices[h]]))) {
                    h++;
                } else if (reducedWeights[sortedIndices[h]] >= reducedWeights[sortedIndices[j]]) {
                    if (reducedWeights[sortedIndices[h]] == reducedWeights[sortedIndices[j]]
                            || reducedWeights[sortedIndices[h]] > WPrime
                            || zHat >= VN + reducedValues[sortedIndices[h]]) {
                        h++;
                    } else {
                        zHat = VN + reducedValues[sortedIndices[h]];
                        xHat = Arrays.copyOf(x, x.length);
                        xHat[sortedIndices[h]] = 1;
                        if (zHat == U) {
                            return zHat;
                        }
                        j = h;
                        h++;
                    }
                } else {
                    if (WPrime - reducedWeights[sortedIndices[h]] < mi[h - 1]) {
                        h++;
                    } else {
                        i = h;
                        x[i] = WPrime / reducedWeights[sortedIndices[i]];
                        VN += reducedValues[sortedIndices[i]] * x[i];
                        WPrime -= reducedWeights[sortedIndices[i]] * x[i];
                        break;
                    }
                }
            }
        }
        return U;
    }

    public static void runtimeCalculatorBnB(int[] weights, int[] values, int capacity) {

        long startTime = System.nanoTime();
        int resultBnB = knapsackBnB(values, weights, capacity);
        long endTime = System.nanoTime();

        double elapsedTimeInSeconds = (endTime - startTime) / 1e9;
        double elapsedTimeInMilliseconds = elapsedTimeInSeconds * 1e3;

        System.out.println(resultBnB);
        System.out.printf("Waktu eksekusi BnB: %.6f milidetik\n",
                elapsedTimeInMilliseconds);
    }

    // Driver program
    public static void main(String[] args) {

        int[] weightsSmall = generateInputWeight(100);
        int[] valuesSmall = generateInputValue(100);

        int[] weightsMedium = generateInputWeight(1000);
        int[] valuesMedium = generateInputValue(1000);

        int[] weightsBig = generateInputWeight(10000);
        int[] valuesBig = generateInputValue(10000);

        int[] arr1 = { 1, 30, 50 };
        int[] arr2 = { 1, 50, 100 };
        // runtimeCalculatorDP(100, 3, arr1, arr2);
        // System.out.println(unboundedKnapsack(100, 3, arr1, arr2));
        // int W = 100;
        // int val[] = { 10, 30, 20 };
        // int wt[] = { 5, 10, 15 };
        // int n = val.length;
        // System.out.println(
        // unboundedKnapsack(W, wt, val, n - 1));

        // runtimeCalculatorBnB(weightsSmall, valuesSmall, W);
        // runtimeCalculatorDP(W, n, valuesBig, weightsBig);

        int[] weights = { 2, 5, 10, 5 };
        int[] values = { 40, 30, 50, 10 };
        int capacity = 16;
        int W = 1000;
        // int n = weightsMedium.length;
        runtimeCalculatorBnB(weights, values, capacity);
        // runtimeCalculatorDP(W, n, valuesBig, weightsBig);
    }
}