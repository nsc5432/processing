package aoms.pm;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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
        // 1. 초기 데이터 셋업 (MockData에서 import)
        LocalDateTime stdTime = MockData.STD_TIME;
        List<LocalDateTime> passengerTimes = MockData.PASSENGER_TIMES;
        double[] priorDist = MockData.PRIOR_DIST;

        // 반영 강도 설정
        double observationWeight = 0.8;

        // 2. 당일 관측 분포(Likelihood) 산출
        double[] likelihoodDist = calculateLikelihoodDistribution(stdTime, passengerTimes, BIN_EDGES);

        // 3. 사후 확률(Posterior) 보정
        double[] posteriorDist = calculatePosteriorDistribution(priorDist, likelihoodDist, observationWeight);

        // 4. 결과 출력
        System.out.printf("%-12s | %-10s | %-10s | %-10s\n", "시간 구간", "과거(Prior)", "당일(Likelihood)", "최종(Posterior)");
        System.out.println("=========================================================");
        for (int i = 0; i < BIN_LABELS.length; i++) {
            System.out.printf("%-12s | %9.4f | %12.4f | %11.4f\n",
                BIN_LABELS[i],
                priorDist[i],
                likelihoodDist[i],
                posteriorDist[i]);
        }
	}
}
