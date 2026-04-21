package aoms.pm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MockData {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // ── 운항편 STD 목록 (시간 오름차순) ──────────────────────────────────────
    public static final List<LocalDateTime> STD_TIMES = Arrays.asList(
        LocalDateTime.parse("2026-02-12 03:30", FMT),  // P1
        LocalDateTime.parse("2026-02-12 05:00", FMT),  // P1
        LocalDateTime.parse("2026-02-12 06:30", FMT),  // P2
        LocalDateTime.parse("2026-02-12 08:10", FMT),  // P3
        LocalDateTime.parse("2026-02-12 14:00", FMT),  // P3
        LocalDateTime.parse("2026-02-12 23:15", FMT)   // P4
    );

    // ── 시간대별 초기 사전확률 ───────────────────────────────────────────────
    public static final Map<String, double[]> PRIOR_BY_PERIOD = new LinkedHashMap<>();

    // ── 운항편별 승객 태깅 시간 ──────────────────────────────────────────────
    public static final Map<LocalDateTime, List<LocalDateTime>> PASSENGER_TIMES_BY_FLIGHT = new LinkedHashMap<>();

    static {
        // P1: 00~05:59 (새벽, 탑승객 적음 / 60~160분 전 집중)
        PRIOR_BY_PERIOD.put("P1", new double[]{
            0,      0,      0,      0,      0,      0,
            0.02,   0.05,   0.10,   0.14,   0.16,   0.17,
            0.14,   0.10,   0.07,   0.04,   0.01,   0,
            0,      0,      0,      0,      0,      0,
            0,      0,      0,      0,      0,      0
        });

        // P2: 06~06:59 (증가 구간 / 70~170분 전 분포)
        PRIOR_BY_PERIOD.put("P2", new double[]{
            0,      0,      0.002,  0.002,  0.005,  0.010,
            0.020,  0.040,  0.070,  0.100,  0.130,  0.150,
            0.140,  0.120,  0.090,  0.055,  0.035,  0.020,
            0.008,  0.003,  0,      0,      0,      0,
            0,      0,      0,      0,      0,      0
        });

        // P3: 07~22:59 (혼잡 / 기존 PRIOR_DIST)
        PRIOR_BY_PERIOD.put("P3", new double[]{
            0,      0,      0.0001, 0.0002, 0.0004, 0.0006,
            0.0012, 0.0018, 0.0027, 0.0031, 0.0046, 0.0074,
            0.0127, 0.025,  0.04,   0.062,  0.0848, 0.11,
            0.1222, 0.1286, 0.1249, 0.1053, 0.076,  0.049,
            0.0247, 0.0071, 0.0023, 0.0014, 0.0019, 0
        });

        // P4: 23~23:59 (심야 / 80~170분 전 집중)
        PRIOR_BY_PERIOD.put("P4", new double[]{
            0,      0,      0,      0,      0,      0,
            0,      0.02,   0.06,   0.13,   0.17,   0.18,
            0.15,   0.11,   0.08,   0.05,   0.03,   0.02,
            0,      0,      0,      0,      0,      0,
            0,      0,      0,      0,      0,      0
        });

        // ── P1 운항편: STD 03:30 ──────────────────────────────────────────
        PASSENGER_TIMES_BY_FLIGHT.put(
            LocalDateTime.parse("2026-02-12 03:30", FMT),
            Arrays.asList(
                LocalDateTime.parse("2026-02-12 01:10", FMT),  // 140분 전
                LocalDateTime.parse("2026-02-12 01:20", FMT),  // 130분 전
                LocalDateTime.parse("2026-02-12 01:35", FMT),  // 115분 전
                LocalDateTime.parse("2026-02-12 01:45", FMT),  // 105분 전
                LocalDateTime.parse("2026-02-12 01:55", FMT),  //  95분 전
                LocalDateTime.parse("2026-02-12 02:00", FMT),  //  90분 전
                LocalDateTime.parse("2026-02-12 02:10", FMT),  //  80분 전
                LocalDateTime.parse("2026-02-12 02:20", FMT),  //  70분 전
                LocalDateTime.parse("2026-02-12 02:40", FMT),  //  50분 전
                LocalDateTime.parse("2026-02-12 03:00", FMT)   //  30분 전
            )
        );

        // ── P1 운항편: STD 05:00 ──────────────────────────────────────────
        PASSENGER_TIMES_BY_FLIGHT.put(
            LocalDateTime.parse("2026-02-12 05:00", FMT),
            Arrays.asList(
                LocalDateTime.parse("2026-02-12 02:25", FMT),  // 155분 전
                LocalDateTime.parse("2026-02-12 02:40", FMT),  // 140분 전
                LocalDateTime.parse("2026-02-12 02:55", FMT),  // 125분 전
                LocalDateTime.parse("2026-02-12 03:05", FMT),  // 115분 전
                LocalDateTime.parse("2026-02-12 03:15", FMT),  // 105분 전
                LocalDateTime.parse("2026-02-12 03:20", FMT),  // 100분 전
                LocalDateTime.parse("2026-02-12 03:30", FMT),  //  90분 전
                LocalDateTime.parse("2026-02-12 03:35", FMT),  //  85분 전
                LocalDateTime.parse("2026-02-12 03:45", FMT),  //  75분 전
                LocalDateTime.parse("2026-02-12 03:50", FMT),  //  70분 전
                LocalDateTime.parse("2026-02-12 04:00", FMT),  //  60분 전
                LocalDateTime.parse("2026-02-12 04:10", FMT),  //  50분 전
                LocalDateTime.parse("2026-02-12 04:20", FMT),  //  40분 전
                LocalDateTime.parse("2026-02-12 04:35", FMT),  //  25분 전
                LocalDateTime.parse("2026-02-12 04:50", FMT)   //  10분 전
            )
        );

        // ── P2 운항편: STD 06:30 ──────────────────────────────────────────
        PASSENGER_TIMES_BY_FLIGHT.put(
            LocalDateTime.parse("2026-02-12 06:30", FMT),
            Arrays.asList(
                LocalDateTime.parse("2026-02-12 04:05", FMT),  // 145분 전
                LocalDateTime.parse("2026-02-12 04:15", FMT),  // 135분 전
                LocalDateTime.parse("2026-02-12 04:30", FMT),  // 120분 전
                LocalDateTime.parse("2026-02-12 04:40", FMT),  // 110분 전
                LocalDateTime.parse("2026-02-12 04:50", FMT),  // 100분 전
                LocalDateTime.parse("2026-02-12 05:00", FMT),  //  90분 전
                LocalDateTime.parse("2026-02-12 05:05", FMT),  //  85분 전
                LocalDateTime.parse("2026-02-12 05:10", FMT),  //  80분 전
                LocalDateTime.parse("2026-02-12 05:15", FMT),  //  75분 전
                LocalDateTime.parse("2026-02-12 05:20", FMT),  //  70분 전
                LocalDateTime.parse("2026-02-12 05:30", FMT),  //  60분 전
                LocalDateTime.parse("2026-02-12 05:40", FMT),  //  50분 전
                LocalDateTime.parse("2026-02-12 05:45", FMT),  //  45분 전
                LocalDateTime.parse("2026-02-12 05:50", FMT),  //  40분 전
                LocalDateTime.parse("2026-02-12 05:55", FMT),  //  35분 전
                LocalDateTime.parse("2026-02-12 06:00", FMT),  //  30분 전
                LocalDateTime.parse("2026-02-12 06:05", FMT),  //  25분 전
                LocalDateTime.parse("2026-02-12 06:10", FMT),  //  20분 전
                LocalDateTime.parse("2026-02-12 06:15", FMT),  //  15분 전
                LocalDateTime.parse("2026-02-12 06:20", FMT)   //  10분 전
            )
        );

        // ── P3 운항편: STD 08:10 ──────────────────────────────────────────
        PASSENGER_TIMES_BY_FLIGHT.put(
            LocalDateTime.parse("2026-02-12 08:10", FMT),
            Arrays.asList(
                LocalDateTime.parse("2026-02-12 04:35", FMT),
                LocalDateTime.parse("2026-02-12 04:36", FMT),
                LocalDateTime.parse("2026-02-12 04:46", FMT),
                LocalDateTime.parse("2026-02-12 04:53", FMT),
                LocalDateTime.parse("2026-02-12 04:53", FMT),
                LocalDateTime.parse("2026-02-12 04:53", FMT),
                LocalDateTime.parse("2026-02-12 05:01", FMT),
                LocalDateTime.parse("2026-02-12 05:18", FMT),
                LocalDateTime.parse("2026-02-12 05:20", FMT),
                LocalDateTime.parse("2026-02-12 05:25", FMT),
                LocalDateTime.parse("2026-02-12 05:25", FMT),
                LocalDateTime.parse("2026-02-12 05:25", FMT),
                LocalDateTime.parse("2026-02-12 05:29", FMT),
                LocalDateTime.parse("2026-02-12 05:30", FMT),
                LocalDateTime.parse("2026-02-12 05:36", FMT),
                LocalDateTime.parse("2026-02-12 05:37", FMT),
                LocalDateTime.parse("2026-02-12 05:37", FMT),
                LocalDateTime.parse("2026-02-12 05:41", FMT),
                LocalDateTime.parse("2026-02-12 05:41", FMT),
                LocalDateTime.parse("2026-02-12 05:41", FMT),
                LocalDateTime.parse("2026-02-12 05:43", FMT),
                LocalDateTime.parse("2026-02-12 05:46", FMT),
                LocalDateTime.parse("2026-02-12 05:46", FMT),
                LocalDateTime.parse("2026-02-12 05:54", FMT),
                LocalDateTime.parse("2026-02-12 05:54", FMT),
                LocalDateTime.parse("2026-02-12 05:54", FMT),
                LocalDateTime.parse("2026-02-12 05:55", FMT),
                LocalDateTime.parse("2026-02-12 05:55", FMT),
                LocalDateTime.parse("2026-02-12 05:56", FMT),
                LocalDateTime.parse("2026-02-12 05:56", FMT),
                LocalDateTime.parse("2026-02-12 05:56", FMT),
                LocalDateTime.parse("2026-02-12 05:56", FMT),
                LocalDateTime.parse("2026-02-12 05:57", FMT),
                LocalDateTime.parse("2026-02-12 05:57", FMT),
                LocalDateTime.parse("2026-02-12 05:57", FMT),
                LocalDateTime.parse("2026-02-12 05:59", FMT),
                LocalDateTime.parse("2026-02-12 05:59", FMT),
                LocalDateTime.parse("2026-02-12 06:02", FMT),
                LocalDateTime.parse("2026-02-12 06:02", FMT),
                LocalDateTime.parse("2026-02-12 06:03", FMT),
                LocalDateTime.parse("2026-02-12 06:03", FMT),
                LocalDateTime.parse("2026-02-12 06:03", FMT),
                LocalDateTime.parse("2026-02-12 06:03", FMT),
                LocalDateTime.parse("2026-02-12 06:03", FMT),
                LocalDateTime.parse("2026-02-12 06:05", FMT),
                LocalDateTime.parse("2026-02-12 06:11", FMT),
                LocalDateTime.parse("2026-02-12 06:12", FMT),
                LocalDateTime.parse("2026-02-12 06:12", FMT),
                LocalDateTime.parse("2026-02-12 06:12", FMT),
                LocalDateTime.parse("2026-02-12 06:13", FMT),
                LocalDateTime.parse("2026-02-12 06:15", FMT),
                LocalDateTime.parse("2026-02-12 06:15", FMT),
                LocalDateTime.parse("2026-02-12 06:15", FMT),
                LocalDateTime.parse("2026-02-12 06:15", FMT),
                LocalDateTime.parse("2026-02-12 06:16", FMT),
                LocalDateTime.parse("2026-02-12 06:16", FMT),
                LocalDateTime.parse("2026-02-12 06:16", FMT),
                LocalDateTime.parse("2026-02-12 06:16", FMT),
                LocalDateTime.parse("2026-02-12 06:16", FMT),
                LocalDateTime.parse("2026-02-12 06:16", FMT),
                LocalDateTime.parse("2026-02-12 06:16", FMT),
                LocalDateTime.parse("2026-02-12 06:17", FMT),
                LocalDateTime.parse("2026-02-12 06:17", FMT),
                LocalDateTime.parse("2026-02-12 06:18", FMT),
                LocalDateTime.parse("2026-02-12 06:20", FMT),
                LocalDateTime.parse("2026-02-12 06:20", FMT),
                LocalDateTime.parse("2026-02-12 06:22", FMT),
                LocalDateTime.parse("2026-02-12 06:22", FMT),
                LocalDateTime.parse("2026-02-12 06:25", FMT),
                LocalDateTime.parse("2026-02-12 06:26", FMT),
                LocalDateTime.parse("2026-02-12 06:26", FMT),
                LocalDateTime.parse("2026-02-12 06:26", FMT),
                LocalDateTime.parse("2026-02-12 06:26", FMT),
                LocalDateTime.parse("2026-02-12 06:27", FMT),
                LocalDateTime.parse("2026-02-12 06:28", FMT),
                LocalDateTime.parse("2026-02-12 06:28", FMT),
                LocalDateTime.parse("2026-02-12 06:28", FMT),
                LocalDateTime.parse("2026-02-12 06:29", FMT),
                LocalDateTime.parse("2026-02-12 06:29", FMT),
                LocalDateTime.parse("2026-02-12 06:29", FMT),
                LocalDateTime.parse("2026-02-12 06:29", FMT),
                LocalDateTime.parse("2026-02-12 06:30", FMT),
                LocalDateTime.parse("2026-02-12 06:30", FMT),
                LocalDateTime.parse("2026-02-12 06:31", FMT),
                LocalDateTime.parse("2026-02-12 06:31", FMT),
                LocalDateTime.parse("2026-02-12 06:31", FMT),
                LocalDateTime.parse("2026-02-12 06:31", FMT),
                LocalDateTime.parse("2026-02-12 06:31", FMT),
                LocalDateTime.parse("2026-02-12 06:32", FMT),
                LocalDateTime.parse("2026-02-12 06:32", FMT),
                LocalDateTime.parse("2026-02-12 06:33", FMT),
                LocalDateTime.parse("2026-02-12 06:33", FMT),
                LocalDateTime.parse("2026-02-12 06:33", FMT),
                LocalDateTime.parse("2026-02-12 06:33", FMT),
                LocalDateTime.parse("2026-02-12 06:33", FMT),
                LocalDateTime.parse("2026-02-12 06:33", FMT),
                LocalDateTime.parse("2026-02-12 06:33", FMT),
                LocalDateTime.parse("2026-02-12 06:33", FMT),
                LocalDateTime.parse("2026-02-12 06:33", FMT),
                LocalDateTime.parse("2026-02-12 06:34", FMT),
                LocalDateTime.parse("2026-02-12 06:34", FMT),
                LocalDateTime.parse("2026-02-12 06:34", FMT),
                LocalDateTime.parse("2026-02-12 06:34", FMT),
                LocalDateTime.parse("2026-02-12 06:34", FMT),
                LocalDateTime.parse("2026-02-12 06:34", FMT),
                LocalDateTime.parse("2026-02-12 06:34", FMT),
                LocalDateTime.parse("2026-02-12 06:35", FMT),
                LocalDateTime.parse("2026-02-12 06:35", FMT),
                LocalDateTime.parse("2026-02-12 06:35", FMT),
                LocalDateTime.parse("2026-02-12 06:35", FMT),
                LocalDateTime.parse("2026-02-12 06:35", FMT),
                LocalDateTime.parse("2026-02-12 06:36", FMT),
                LocalDateTime.parse("2026-02-12 06:37", FMT),
                LocalDateTime.parse("2026-02-12 06:37", FMT),
                LocalDateTime.parse("2026-02-12 06:37", FMT),
                LocalDateTime.parse("2026-02-12 06:37", FMT),
                LocalDateTime.parse("2026-02-12 06:38", FMT),
                LocalDateTime.parse("2026-02-12 06:38", FMT),
                LocalDateTime.parse("2026-02-12 06:38", FMT),
                LocalDateTime.parse("2026-02-12 06:38", FMT),
                LocalDateTime.parse("2026-02-12 06:38", FMT),
                LocalDateTime.parse("2026-02-12 06:39", FMT),
                LocalDateTime.parse("2026-02-12 06:40", FMT),
                LocalDateTime.parse("2026-02-12 06:40", FMT),
                LocalDateTime.parse("2026-02-12 06:42", FMT),
                LocalDateTime.parse("2026-02-12 06:42", FMT),
                LocalDateTime.parse("2026-02-12 06:43", FMT),
                LocalDateTime.parse("2026-02-12 06:47", FMT),
                LocalDateTime.parse("2026-02-12 06:48", FMT),
                LocalDateTime.parse("2026-02-12 06:48", FMT),
                LocalDateTime.parse("2026-02-12 06:48", FMT),
                LocalDateTime.parse("2026-02-12 06:48", FMT),
                LocalDateTime.parse("2026-02-12 06:49", FMT),
                LocalDateTime.parse("2026-02-12 06:49", FMT),
                LocalDateTime.parse("2026-02-12 06:49", FMT),
                LocalDateTime.parse("2026-02-12 06:50", FMT),
                LocalDateTime.parse("2026-02-12 06:50", FMT),
                LocalDateTime.parse("2026-02-12 06:50", FMT),
                LocalDateTime.parse("2026-02-12 06:52", FMT),
                LocalDateTime.parse("2026-02-12 06:52", FMT),
                LocalDateTime.parse("2026-02-12 06:55", FMT),
                LocalDateTime.parse("2026-02-12 06:55", FMT),
                LocalDateTime.parse("2026-02-12 06:55", FMT),
                LocalDateTime.parse("2026-02-12 06:56", FMT),
                LocalDateTime.parse("2026-02-12 06:56", FMT),
                LocalDateTime.parse("2026-02-12 06:56", FMT),
                LocalDateTime.parse("2026-02-12 06:56", FMT),
                LocalDateTime.parse("2026-02-12 06:56", FMT),
                LocalDateTime.parse("2026-02-12 06:57", FMT),
                LocalDateTime.parse("2026-02-12 07:01", FMT),
                LocalDateTime.parse("2026-02-12 07:01", FMT),
                LocalDateTime.parse("2026-02-12 07:01", FMT),
                LocalDateTime.parse("2026-02-12 07:02", FMT),
                LocalDateTime.parse("2026-02-12 07:05", FMT),
                LocalDateTime.parse("2026-02-12 07:05", FMT),
                LocalDateTime.parse("2026-02-12 07:05", FMT),
                LocalDateTime.parse("2026-02-12 07:06", FMT),
                LocalDateTime.parse("2026-02-12 07:06", FMT),
                LocalDateTime.parse("2026-02-12 07:06", FMT),
                LocalDateTime.parse("2026-02-12 07:08", FMT),
                LocalDateTime.parse("2026-02-12 07:08", FMT),
                LocalDateTime.parse("2026-02-12 07:08", FMT),
                LocalDateTime.parse("2026-02-12 07:08", FMT),
                LocalDateTime.parse("2026-02-12 07:08", FMT),
                LocalDateTime.parse("2026-02-12 07:08", FMT),
                LocalDateTime.parse("2026-02-12 07:09", FMT),
                LocalDateTime.parse("2026-02-12 07:09", FMT),
                LocalDateTime.parse("2026-02-12 07:09", FMT),
                LocalDateTime.parse("2026-02-12 07:13", FMT),
                LocalDateTime.parse("2026-02-12 07:13", FMT),
                LocalDateTime.parse("2026-02-12 07:16", FMT),
                LocalDateTime.parse("2026-02-12 07:16", FMT),
                LocalDateTime.parse("2026-02-12 07:16", FMT),
                LocalDateTime.parse("2026-02-12 07:16", FMT),
                LocalDateTime.parse("2026-02-12 07:16", FMT),
                LocalDateTime.parse("2026-02-12 07:16", FMT),
                LocalDateTime.parse("2026-02-12 07:16", FMT),
                LocalDateTime.parse("2026-02-12 07:18", FMT),
                LocalDateTime.parse("2026-02-12 07:18", FMT),
                LocalDateTime.parse("2026-02-12 07:19", FMT),
                LocalDateTime.parse("2026-02-12 07:19", FMT),
                LocalDateTime.parse("2026-02-12 07:30", FMT),
                LocalDateTime.parse("2026-02-12 07:30", FMT),
                LocalDateTime.parse("2026-02-12 07:30", FMT)
            )
        );

        // ── P3 운항편: STD 14:00 ──────────────────────────────────────────
        PASSENGER_TIMES_BY_FLIGHT.put(
            LocalDateTime.parse("2026-02-12 14:00", FMT),
            Arrays.asList(
                LocalDateTime.parse("2026-02-12 10:45", FMT),  // 195분 전
                LocalDateTime.parse("2026-02-12 11:00", FMT),  // 180분 전
                LocalDateTime.parse("2026-02-12 11:10", FMT),  // 170분 전
                LocalDateTime.parse("2026-02-12 11:20", FMT),  // 160분 전
                LocalDateTime.parse("2026-02-12 11:30", FMT),  // 150분 전
                LocalDateTime.parse("2026-02-12 11:35", FMT),  // 145분 전
                LocalDateTime.parse("2026-02-12 11:40", FMT),  // 140분 전
                LocalDateTime.parse("2026-02-12 11:45", FMT),  // 135분 전
                LocalDateTime.parse("2026-02-12 11:50", FMT),  // 130분 전
                LocalDateTime.parse("2026-02-12 11:55", FMT),  // 125분 전
                LocalDateTime.parse("2026-02-12 12:00", FMT),  // 120분 전
                LocalDateTime.parse("2026-02-12 12:05", FMT),  // 115분 전
                LocalDateTime.parse("2026-02-12 12:10", FMT),  // 110분 전
                LocalDateTime.parse("2026-02-12 12:15", FMT),  // 105분 전
                LocalDateTime.parse("2026-02-12 12:20", FMT),  // 100분 전
                LocalDateTime.parse("2026-02-12 12:30", FMT),  //  90분 전
                LocalDateTime.parse("2026-02-12 12:40", FMT),  //  80분 전
                LocalDateTime.parse("2026-02-12 12:50", FMT),  //  70분 전
                LocalDateTime.parse("2026-02-12 13:00", FMT),  //  60분 전
                LocalDateTime.parse("2026-02-12 13:10", FMT),  //  50분 전
                LocalDateTime.parse("2026-02-12 13:20", FMT),  //  40분 전
                LocalDateTime.parse("2026-02-12 13:30", FMT),  //  30분 전
                LocalDateTime.parse("2026-02-12 13:40", FMT),  //  20분 전
                LocalDateTime.parse("2026-02-12 13:50", FMT),  //  10분 전
                LocalDateTime.parse("2026-02-12 13:55", FMT)   //   5분 전
            )
        );

        // ── P4 운항편: STD 23:15 ──────────────────────────────────────────
        PASSENGER_TIMES_BY_FLIGHT.put(
            LocalDateTime.parse("2026-02-12 23:15", FMT),
            Arrays.asList(
                LocalDateTime.parse("2026-02-12 21:10", FMT),  // 125분 전
                LocalDateTime.parse("2026-02-12 21:20", FMT),  // 115분 전
                LocalDateTime.parse("2026-02-12 21:30", FMT),  // 105분 전
                LocalDateTime.parse("2026-02-12 21:40", FMT),  //  95분 전
                LocalDateTime.parse("2026-02-12 21:45", FMT),  //  90분 전
                LocalDateTime.parse("2026-02-12 21:50", FMT),  //  85분 전
                LocalDateTime.parse("2026-02-12 22:00", FMT),  //  75분 전
                LocalDateTime.parse("2026-02-12 22:05", FMT),  //  70분 전
                LocalDateTime.parse("2026-02-12 22:15", FMT),  //  60분 전
                LocalDateTime.parse("2026-02-12 22:30", FMT),  //  45분 전
                LocalDateTime.parse("2026-02-12 22:40", FMT),  //  35분 전
                LocalDateTime.parse("2026-02-12 22:50", FMT),  //  25분 전
                LocalDateTime.parse("2026-02-12 23:00", FMT),  //  15분 전
                LocalDateTime.parse("2026-02-12 23:05", FMT),  //  10분 전
                LocalDateTime.parse("2026-02-12 23:10", FMT)   //   5분 전
            )
        );
    }
}
