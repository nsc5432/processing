package aoms.pm;

public class DistributionUtils {

    public static double round(double value, int decimalPlaces) {
        double scale = Math.pow(10, decimalPlaces);
        return Math.round(value * scale) / scale;
    }

    /**
     * 각 요소를 지정한 소수점 자릿수로 반올림 후 합계가 정확히 1이 되도록 보정.
     * 가장 큰 값을 가진 요소에 반올림 오차를 더해 합계를 맞춤.
     */
    public static double[] roundAndNormalize(double[] dist, int decimalPlaces) {
        int length = dist.length;
        double[] rounded = new double[length];
        double sum = 0.0;

        for (int i = 0; i < length; i++) {
            rounded[i] = round(dist[i], decimalPlaces);
            sum += rounded[i];
        }

        double error = 1.0 - round(sum, decimalPlaces);
        if (error != 0.0) {
            int maxIdx = 0;
            for (int i = 1; i < length; i++) {
                if (rounded[i] > rounded[maxIdx]) maxIdx = i;
            }
            rounded[maxIdx] = round(rounded[maxIdx] + error, decimalPlaces);
        }

        return rounded;
    }

    public static double[] roundAndNormalize(double[] dist) {
        return roundAndNormalize(dist, 4);
    }
}
