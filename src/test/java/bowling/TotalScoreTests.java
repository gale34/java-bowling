package bowling;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Total score 테스트")
public class TotalScoreTests {

    @DisplayName("생성 테스트")
    @Test
    public void generateTest() {
        assertThatCode(() -> TotalScore.of(6));
    }

    @DisplayName("생성 테스트 - 오류")
    @Test
    public void generateAbnormalTest() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> TotalScore.of(-5))
                .withMessageContaining("Total Score must be greater than zero.");
    }

    @DisplayName("스트라이크 합산 테스트")
    @ParameterizedTest
    @MethodSource("sumStrikeTestCases")
    public void sumStrikeTest(int[] scores, int expectedResult) {
        TotalScore totalScore = TotalScore.of(10);
        NextAddingUpScores nextAddingUpScores = NextAddingUpScores.newInstance(scores);

        assertThat(totalScore.sumStrike(nextAddingUpScores)).isEqualTo(TotalScore.of(expectedResult));
    }

    @DisplayName("스페어 합산 테스트")
    @ParameterizedTest
    @MethodSource("sumSpareTestCases")
    public void sumSpareTest(int[] scores, int expectedResult) {
        TotalScore totalScore = TotalScore.of(10);
        NextAddingUpScores nextAddingUpScores = NextAddingUpScores.newInstance(scores);

        assertThat(totalScore.sumSpare(nextAddingUpScores)).isEqualTo(TotalScore.of(expectedResult));
    }

    @DisplayName("Total Score 계산 테스트")
    @Test
    public void calculateTotalScoreTest() {
        List<Score> scores = Stream.of(10, 4)
                .map(Score::of)
                .collect(Collectors.toList());

        assertThat(TotalScore.calculateTotalScore(scores)).isEqualTo(TotalScore.of(18));

        List<Score> scores2 = Stream.of(8, 2)
                .map(Score::of)
                .collect(Collectors.toList());

        assertThat(TotalScore.calculateTotalScore(scores2)).isEqualTo(TotalScore.of(10));
    }

    @DisplayName("Total Score 계산 테스트2")
    @Test
    public void calculateTotalScoreTest2() {
        FrameScore frameScore = FrameScore.newInstance(Arrays.asList(6, 4));
        NextAddingUpScores nextAddingUpScores = NextAddingUpScores.newInstance(9, 1);

        assertThat(TotalScore.calculateTotalScore(frameScore, nextAddingUpScores)).isEqualTo(TotalScore.of(19));

        FrameScore frameScore2 = FrameScore.newInstance(Collections.singletonList(10));
        NextAddingUpScores nextAddingUpScores2 = NextAddingUpScores.newInstance(9, 1);

        assertThat(TotalScore.calculateTotalScore(frameScore2, nextAddingUpScores2)).isEqualTo(TotalScore.of(20));
    }


    private static Stream<Arguments> sumStrikeTestCases() {
        return Stream.of(
                Arguments.of(new int[]{9, 1}, 20),
                Arguments.of(new int[]{10, 10}, 30),
                Arguments.of(new int[]{5, 4}, 19)
        );
    }

    private static Stream<Arguments> sumSpareTestCases() {
        return Stream.of(
                Arguments.of(new int[]{9}, 19),
                Arguments.of(new int[]{10}, 20),
                Arguments.of(new int[]{5}, 15)
        );
    }

}
