package aoms.pm;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ShowUpRate {
	/**
     * 1. 당일 관측 분포 (Likelihood) 산출 함수
     * * @param stdTime 운항편 출발 예정 시간 (STD)
     * @param passengerTimes 승객별 태깅(또는 도착) 시간 리스트
     * @param binEdges 시간 구간 경계값 배열
     * @return 구간별 출현 비율 배열 (Likelihood)
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
            // 출발시간 기준 몇 분 전에 도착했는지 계산
            long diffMinutes = Duration.between(stdTime, pTime).toMinutes() * -1;

            // 어느 구간(Bin)에 속하는지 확인 (lowerBound <= diff < upperBound)
            for (int i = 0; i < numBins; i++) {
                if (binEdges[i] <= diffMinutes && diffMinutes < binEdges[i + 1]) {
                    counts[i]++;
                    validPassengers++;
                    break;
                }
            }
        }

        double[] likelihoodDist = new double[numBins];
        
        // 관측된 승객이 없는 경우 0.0 배열 반환 (ZeroDivision 방지)
        if (validPassengers == 0) {
            return likelihoodDist;
        }

        // 전체 관측 승객 대비 각 구간의 비율 계산
        for (int i = 0; i < numBins; i++) {
            likelihoodDist[i] = (double) counts[i] / validPassengers;
        }

        return likelihoodDist;
    }

    /**
     * 2. 사후 확률 (Posterior) 보정 함수
     * * @param priorDist 과거 평균 분포 배열 (사전 확률)
     * @param likelihoodDist 당일 관측 분포 배열 (우도)
     * @param observationWeight 당일 관측치 반영 강도 (0.0 ~ 1.0)
     * @return 융합된 최종 사후 확률 분포 배열
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

        // 디리클레 가중 평균 방식을 이용한 확률 업데이트
        for (int i = 0; i < length; i++) {
            posteriorDist[i] = (priorDist[i] * priorWeight) + (likelihoodDist[i] * observationWeight);
        }

        return posteriorDist;
    }
    
	public static void main(String[] args) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // 1. 초기 데이터 셋업
        LocalDateTime stdTime = LocalDateTime.parse("2026-02-12 08:10", formatter);
        
        // 태깅 시간 자체를 기준 시간으로 간주하여 테스트
        List<LocalDateTime> passengerTimes = Arrays.asList(
        		LocalDateTime.parse("2026-02-12 04:35", formatter),
        		LocalDateTime.parse("2026-02-12 04:36", formatter),
        		LocalDateTime.parse("2026-02-12 04:46", formatter),
        		LocalDateTime.parse("2026-02-12 04:53", formatter),
        		LocalDateTime.parse("2026-02-12 04:53", formatter),
        		LocalDateTime.parse("2026-02-12 04:53", formatter),
        		LocalDateTime.parse("2026-02-12 05:01", formatter),
        		LocalDateTime.parse("2026-02-12 05:18", formatter),
        		LocalDateTime.parse("2026-02-12 05:20", formatter),
        		LocalDateTime.parse("2026-02-12 05:25", formatter),
        		LocalDateTime.parse("2026-02-12 05:25", formatter),
        		LocalDateTime.parse("2026-02-12 05:25", formatter),
        		LocalDateTime.parse("2026-02-12 05:29", formatter),
        		LocalDateTime.parse("2026-02-12 05:30", formatter),
        		LocalDateTime.parse("2026-02-12 05:36", formatter),
        		LocalDateTime.parse("2026-02-12 05:37", formatter),
        		LocalDateTime.parse("2026-02-12 05:37", formatter),
        		LocalDateTime.parse("2026-02-12 05:41", formatter),
        		LocalDateTime.parse("2026-02-12 05:41", formatter),
        		LocalDateTime.parse("2026-02-12 05:41", formatter),
        		LocalDateTime.parse("2026-02-12 05:43", formatter),
        		LocalDateTime.parse("2026-02-12 05:46", formatter),
        		LocalDateTime.parse("2026-02-12 05:46", formatter),
        		LocalDateTime.parse("2026-02-12 05:54", formatter),
        		LocalDateTime.parse("2026-02-12 05:54", formatter),
        		LocalDateTime.parse("2026-02-12 05:54", formatter),
        		LocalDateTime.parse("2026-02-12 05:55", formatter),
        		LocalDateTime.parse("2026-02-12 05:55", formatter),
        		LocalDateTime.parse("2026-02-12 05:56", formatter),
        		LocalDateTime.parse("2026-02-12 05:56", formatter),
        		LocalDateTime.parse("2026-02-12 05:56", formatter),
        		LocalDateTime.parse("2026-02-12 05:56", formatter),
        		LocalDateTime.parse("2026-02-12 05:57", formatter),
        		LocalDateTime.parse("2026-02-12 05:57", formatter),
        		LocalDateTime.parse("2026-02-12 05:57", formatter),
        		LocalDateTime.parse("2026-02-12 05:59", formatter),
        		LocalDateTime.parse("2026-02-12 05:59", formatter),
        		LocalDateTime.parse("2026-02-12 06:02", formatter),
        		LocalDateTime.parse("2026-02-12 06:02", formatter),
        		LocalDateTime.parse("2026-02-12 06:03", formatter),
        		LocalDateTime.parse("2026-02-12 06:03", formatter),
        		LocalDateTime.parse("2026-02-12 06:03", formatter),
        		LocalDateTime.parse("2026-02-12 06:03", formatter),
        		LocalDateTime.parse("2026-02-12 06:03", formatter),
        		LocalDateTime.parse("2026-02-12 06:05", formatter),
        		LocalDateTime.parse("2026-02-12 06:11", formatter),
        		LocalDateTime.parse("2026-02-12 06:12", formatter),
        		LocalDateTime.parse("2026-02-12 06:12", formatter),
        		LocalDateTime.parse("2026-02-12 06:12", formatter),
        		LocalDateTime.parse("2026-02-12 06:13", formatter),
        		LocalDateTime.parse("2026-02-12 06:15", formatter),
        		LocalDateTime.parse("2026-02-12 06:15", formatter),
        		LocalDateTime.parse("2026-02-12 06:15", formatter),
        		LocalDateTime.parse("2026-02-12 06:15", formatter),
        		LocalDateTime.parse("2026-02-12 06:16", formatter),
        		LocalDateTime.parse("2026-02-12 06:16", formatter),
        		LocalDateTime.parse("2026-02-12 06:16", formatter),
        		LocalDateTime.parse("2026-02-12 06:16", formatter),
        		LocalDateTime.parse("2026-02-12 06:16", formatter),
        		LocalDateTime.parse("2026-02-12 06:16", formatter),
        		LocalDateTime.parse("2026-02-12 06:16", formatter),
        		LocalDateTime.parse("2026-02-12 06:17", formatter),
        		LocalDateTime.parse("2026-02-12 06:17", formatter),
        		LocalDateTime.parse("2026-02-12 06:18", formatter),
        		LocalDateTime.parse("2026-02-12 06:20", formatter),
        		LocalDateTime.parse("2026-02-12 06:20", formatter),
        		LocalDateTime.parse("2026-02-12 06:22", formatter),
        		LocalDateTime.parse("2026-02-12 06:22", formatter),
        		LocalDateTime.parse("2026-02-12 06:25", formatter),
        		LocalDateTime.parse("2026-02-12 06:26", formatter),
        		LocalDateTime.parse("2026-02-12 06:26", formatter),
        		LocalDateTime.parse("2026-02-12 06:26", formatter),
        		LocalDateTime.parse("2026-02-12 06:26", formatter),
        		LocalDateTime.parse("2026-02-12 06:27", formatter),
        		LocalDateTime.parse("2026-02-12 06:28", formatter),
        		LocalDateTime.parse("2026-02-12 06:28", formatter),
        		LocalDateTime.parse("2026-02-12 06:28", formatter),
        		LocalDateTime.parse("2026-02-12 06:29", formatter),
        		LocalDateTime.parse("2026-02-12 06:29", formatter),
        		LocalDateTime.parse("2026-02-12 06:29", formatter),
        		LocalDateTime.parse("2026-02-12 06:29", formatter),
        		LocalDateTime.parse("2026-02-12 06:30", formatter),
        		LocalDateTime.parse("2026-02-12 06:30", formatter),
        		LocalDateTime.parse("2026-02-12 06:31", formatter),
        		LocalDateTime.parse("2026-02-12 06:31", formatter),
        		LocalDateTime.parse("2026-02-12 06:31", formatter),
        		LocalDateTime.parse("2026-02-12 06:31", formatter),
        		LocalDateTime.parse("2026-02-12 06:31", formatter),
        		LocalDateTime.parse("2026-02-12 06:32", formatter),
        		LocalDateTime.parse("2026-02-12 06:32", formatter),
        		LocalDateTime.parse("2026-02-12 06:33", formatter),
        		LocalDateTime.parse("2026-02-12 06:33", formatter),
        		LocalDateTime.parse("2026-02-12 06:33", formatter),
        		LocalDateTime.parse("2026-02-12 06:33", formatter),
        		LocalDateTime.parse("2026-02-12 06:33", formatter),
        		LocalDateTime.parse("2026-02-12 06:33", formatter),
        		LocalDateTime.parse("2026-02-12 06:33", formatter),
        		LocalDateTime.parse("2026-02-12 06:33", formatter),
        		LocalDateTime.parse("2026-02-12 06:33", formatter),
        		LocalDateTime.parse("2026-02-12 06:34", formatter),
        		LocalDateTime.parse("2026-02-12 06:34", formatter),
        		LocalDateTime.parse("2026-02-12 06:34", formatter),
        		LocalDateTime.parse("2026-02-12 06:34", formatter),
        		LocalDateTime.parse("2026-02-12 06:34", formatter),
        		LocalDateTime.parse("2026-02-12 06:34", formatter),
        		LocalDateTime.parse("2026-02-12 06:34", formatter),
        		LocalDateTime.parse("2026-02-12 06:35", formatter),
        		LocalDateTime.parse("2026-02-12 06:35", formatter),
        		LocalDateTime.parse("2026-02-12 06:35", formatter),
        		LocalDateTime.parse("2026-02-12 06:35", formatter),
        		LocalDateTime.parse("2026-02-12 06:35", formatter),
        		LocalDateTime.parse("2026-02-12 06:36", formatter),
        		LocalDateTime.parse("2026-02-12 06:37", formatter),
        		LocalDateTime.parse("2026-02-12 06:37", formatter),
        		LocalDateTime.parse("2026-02-12 06:37", formatter),
        		LocalDateTime.parse("2026-02-12 06:37", formatter),
        		LocalDateTime.parse("2026-02-12 06:38", formatter),
        		LocalDateTime.parse("2026-02-12 06:38", formatter),
        		LocalDateTime.parse("2026-02-12 06:38", formatter),
        		LocalDateTime.parse("2026-02-12 06:38", formatter),
        		LocalDateTime.parse("2026-02-12 06:38", formatter),
        		LocalDateTime.parse("2026-02-12 06:39", formatter),
        		LocalDateTime.parse("2026-02-12 06:40", formatter),
        		LocalDateTime.parse("2026-02-12 06:40", formatter),
        		LocalDateTime.parse("2026-02-12 06:42", formatter),
        		LocalDateTime.parse("2026-02-12 06:42", formatter),
        		LocalDateTime.parse("2026-02-12 06:43", formatter),
        		LocalDateTime.parse("2026-02-12 06:47", formatter),
        		LocalDateTime.parse("2026-02-12 06:48", formatter),
        		LocalDateTime.parse("2026-02-12 06:48", formatter),
        		LocalDateTime.parse("2026-02-12 06:48", formatter),
        		LocalDateTime.parse("2026-02-12 06:48", formatter),
        		LocalDateTime.parse("2026-02-12 06:49", formatter),
        		LocalDateTime.parse("2026-02-12 06:49", formatter),
        		LocalDateTime.parse("2026-02-12 06:49", formatter),
        		LocalDateTime.parse("2026-02-12 06:50", formatter),
        		LocalDateTime.parse("2026-02-12 06:50", formatter),
        		LocalDateTime.parse("2026-02-12 06:50", formatter),
        		LocalDateTime.parse("2026-02-12 06:52", formatter),
        		LocalDateTime.parse("2026-02-12 06:52", formatter),
        		LocalDateTime.parse("2026-02-12 06:55", formatter),
        		LocalDateTime.parse("2026-02-12 06:55", formatter),
        		LocalDateTime.parse("2026-02-12 06:55", formatter),
        		LocalDateTime.parse("2026-02-12 06:56", formatter),
        		LocalDateTime.parse("2026-02-12 06:56", formatter),
        		LocalDateTime.parse("2026-02-12 06:56", formatter),
        		LocalDateTime.parse("2026-02-12 06:56", formatter),
        		LocalDateTime.parse("2026-02-12 06:56", formatter),
        		LocalDateTime.parse("2026-02-12 06:57", formatter),
        		LocalDateTime.parse("2026-02-12 07:01", formatter),
        		LocalDateTime.parse("2026-02-12 07:01", formatter),
        		LocalDateTime.parse("2026-02-12 07:01", formatter),
        		LocalDateTime.parse("2026-02-12 07:02", formatter),
        		LocalDateTime.parse("2026-02-12 07:05", formatter),
        		LocalDateTime.parse("2026-02-12 07:05", formatter),
        		LocalDateTime.parse("2026-02-12 07:05", formatter),
        		LocalDateTime.parse("2026-02-12 07:06", formatter),
        		LocalDateTime.parse("2026-02-12 07:06", formatter),
        		LocalDateTime.parse("2026-02-12 07:06", formatter),
        		LocalDateTime.parse("2026-02-12 07:08", formatter),
        		LocalDateTime.parse("2026-02-12 07:08", formatter),
        		LocalDateTime.parse("2026-02-12 07:08", formatter),
        		LocalDateTime.parse("2026-02-12 07:08", formatter),
        		LocalDateTime.parse("2026-02-12 07:08", formatter),
        		LocalDateTime.parse("2026-02-12 07:08", formatter),
        		LocalDateTime.parse("2026-02-12 07:09", formatter),
        		LocalDateTime.parse("2026-02-12 07:09", formatter),
        		LocalDateTime.parse("2026-02-12 07:09", formatter),
        		LocalDateTime.parse("2026-02-12 07:13", formatter),
        		LocalDateTime.parse("2026-02-12 07:13", formatter),
        		LocalDateTime.parse("2026-02-12 07:16", formatter),
        		LocalDateTime.parse("2026-02-12 07:16", formatter),
        		LocalDateTime.parse("2026-02-12 07:16", formatter),
        		LocalDateTime.parse("2026-02-12 07:16", formatter),
        		LocalDateTime.parse("2026-02-12 07:16", formatter),
        		LocalDateTime.parse("2026-02-12 07:16", formatter),
        		LocalDateTime.parse("2026-02-12 07:16", formatter),
        		LocalDateTime.parse("2026-02-12 07:18", formatter),
        		LocalDateTime.parse("2026-02-12 07:18", formatter),
        		LocalDateTime.parse("2026-02-12 07:19", formatter),
        		LocalDateTime.parse("2026-02-12 07:19", formatter),
        		LocalDateTime.parse("2026-02-12 07:30", formatter),
        		LocalDateTime.parse("2026-02-12 07:30", formatter),
        		LocalDateTime.parse("2026-02-12 07:30", formatter)
        );

        // 시간 구간(Bin) 설정 (10분 간격)
        int[] binEdges = {
    		0, 10, 20, 30, 40, 50, 60, 70, 80, 90,
    		100, 110, 120, 130, 140, 150, 160, 170, 180, 190,
    		200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 300
		};
        
        String[] binLabels = {
        		"-10분", "-20분", "-30분", "-40분", "-50분", "-60분", "-70분", "-80분", "-90분", "-100분",
        		"-110분", "-120분", "-130분", "-140분", "-150분", "-160분", "-170분", "-180분", "-190분", "-200분",
        		"-210분", "-220분", "-230분", "-240분", "-250분", "-260분", "-270분", "-280분", "-290분", "-300분"
		};

        // 과거 평균 분포 (Prior) 설정
        double[] priorDist = {0, 0, 0.0001, 0.0002, 0.0004, 0.0006, 0.0012, 0.0018, 0.0027, 0.0031, 0.0046, 0.0074, 0.0127, 0.025, 0.04, 0.062, 0.0848, 0.11, 0.1222, 0.1286, 0.1249, 0.1053, 0.076, 0.049, 0.0247, 0.0071, 0.0023, 0.0014, 0.0019, 0};
        
        // 반영 강도 설정
        double observationWeight = 0.8;

        // 2. 당일 관측 분포(Likelihood) 산출
        double[] likelihoodDist = calculateLikelihoodDistribution(stdTime, passengerTimes, binEdges);

        // 3. 사후 확률(Posterior) 보정
        double[] posteriorDist = calculatePosteriorDistribution(priorDist, likelihoodDist, observationWeight);

        // 4. 결과 출력
        System.out.printf("%-12s | %-10s | %-10s | %-10s\n", "시간 구간", "과거(Prior)", "당일(Likelihood)", "최종(Posterior)");
        System.out.println("=========================================================");
        for (int i = 0; i < binLabels.length; i++) {
            System.out.printf("%-12s | %9.4f | %12.4f | %11.4f\n", 
                binLabels[i], 
                priorDist[i], 
                likelihoodDist[i], 
                posteriorDist[i]);
        }
	}
}
