package aoms.pm;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShowUpRate {

    private static final int[] BIN_EDGES = {
        0, 10, 20, 30, 40, 50, 60, 70, 80, 90,
        100, 110, 120, 130, 140, 150, 160, 170, 180, 190,
        200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 300
    };

    private static final String[] BIN_LABELS = {
        "-10분", "-20분", "-30분", "-40분", "-50분", "-60분", "-70분", "-80분", "-90분", "-100분",
        "-110분", "-120분", "-130분", "-140분", "-150분", "-160분", "-170분", "-180분", "-190분", "-200분",
        "-210분", "-220분", "-230분", "-240분", "-250분", "-260분", "-270분", "-280분", "-290분", "-300분"
    };

    private static final String[] PERIODS = {"P1", "P2", "P3", "P4"};

    /**
     * 1. 당일 관측 분포 (Likelihood) 산출
     */
    public static double[] calculateLikelihoodDistribution(
        LocalDateTime stdTime,
        List<LocalDateTime> passengerTimes,
        int[] binEdges
    ) {
        int numBins = binEdges.length - 1;
        int[] counts = new int[numBins];
        int validPassengers = 0;

        for (LocalDateTime pTime : passengerTimes) {
            long diffMinutes = Duration.between(stdTime, pTime).toMinutes() * -1;

            for (int i = 0; i < numBins; i++) {
                if (binEdges[i] <= diffMinutes && diffMinutes < binEdges[i + 1]) {
                    counts[i]++;
                    validPassengers++;
                    break;
                }
            }
        }

        double[] likelihoodDist = new double[numBins];
        if (validPassengers == 0) return likelihoodDist;

        for (int i = 0; i < numBins; i++) {
            likelihoodDist[i] = (double) counts[i] / validPassengers;
        }
        return likelihoodDist;
    }

    /**
     * 2. 사후 확률 (Posterior) 보정
     */
    public static double[] calculatePosteriorDistribution(
        double[] priorDist,
        double[] likelihoodDist,
        double observationWeight
    ) {
        if (priorDist.length != likelihoodDist.length) {
            throw new IllegalArgumentException("사전 확률 배열과 우도 배열의 길이가 같아야 합니다.");
        }

        int length = priorDist.length;
        double[] posteriorDist = new double[length];
        double priorWeight = 1.0 - observationWeight;

        for (int i = 0; i < length; i++) {
            posteriorDist[i] = (priorDist[i] * priorWeight) + (likelihoodDist[i] * observationWeight);
        }
        return posteriorDist;
    }

    /**
     * 3. STD 시간대 구분
     * P1: 00~05시 59분 / P2: 06~06시 59분 / P3: 07~22시 59분 / P4: 23~23시 59분
     */
    public static String classifyTimePeriod(LocalDateTime stdTime) {
        int hour = stdTime.getHour();
        if (hour < 6)  return "P1";
        if (hour < 7)  return "P2";
        if (hour < 23) return "P3";
        return "P4";
    }

    /**
     * 4. 복수 사후확률 배열의 평균 산출
     * 반환값이 다음 실행의 사전확률(Prior)로 사용됨
     */
    public static double[] averagePosteriors(List<double[]> posteriors) {
        int numBins = BIN_EDGES.length - 1;
        double[] avg = new double[numBins];
        if (posteriors.isEmpty()) return avg;

        for (double[] posterior : posteriors) {
            for (int i = 0; i < numBins; i++) {
                avg[i] += posterior[i];
            }
        }
        for (int i = 0; i < numBins; i++) {
            avg[i] /= posteriors.size();
        }
        return avg;
    }

    public static void main(String[] args) {
        double observationWeight = 0.8;

        // 시간대별 posterior 수집 버킷 초기화
        Map<String, List<double[]>> posteriorsByPeriod = new LinkedHashMap<>();
        for (String period : PERIODS) {
            posteriorsByPeriod.put(period, new ArrayList<>());
        }

        // 각 운항편 처리: prior는 실행 내 고정
        for (LocalDateTime stdTime : MockData.STD_TIMES) {
            String period = classifyTimePeriod(stdTime);
            List<LocalDateTime> passengerTimes = MockData.PASSENGER_TIMES_BY_FLIGHT.get(stdTime);
            double[] prior = MockData.PRIOR_BY_PERIOD.get(period);

            double[] likelihood = calculateLikelihoodDistribution(stdTime, passengerTimes, BIN_EDGES);
            double[] posterior  = calculatePosteriorDistribution(prior, likelihood, observationWeight);

            posteriorsByPeriod.get(period).add(posterior);
        }

        // 시간대별 평균 posterior 출력
        System.out.println("=== ShowUp Rate 사후확률 분석 결과 ===");
        System.out.printf("반영 강도(observationWeight): %.1f%n%n", observationWeight);

        for (String period : PERIODS) {
            List<double[]> posteriors = posteriorsByPeriod.get(period);
            double[] prior = MockData.PRIOR_BY_PERIOD.get(period);
            int count = posteriors.size();

            // 기여 운항편 0개 → prior를 최종값으로 그대로 사용
            double[] finalPosterior = DistributionUtils.roundAndNormalize(count > 0 ? averagePosteriors(posteriors) : prior);
            String note = count == 0 ? " (관측 없음 — Prior 유지)" : "";

            System.out.printf("[%s] 기여 운항편: %d개%s%n", period, count, note);
            System.out.printf("%-12s | %-10s | %-14s%n", "시간 구간", "사전확률", "평균 사후확률");
            System.out.println("=".repeat(44));
            for (int i = 0; i < BIN_LABELS.length; i++) {
                System.out.printf("%-12s | %9.4f | %14.4f%n",
                    BIN_LABELS[i], prior[i], finalPosterior[i]);
            }
            System.out.println();
        }
    }
}
