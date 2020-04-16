package bowling;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TotalScore {

    private final int score;

    private TotalScore(final int score) {
        validateRange(score);
        this.score = score;
    }

    private void validateRange(final int score) {
        if (score < 0) {
            throw new IllegalArgumentException("Total Score must be greater than zero.");
        }
    }

    public static TotalScore of(final int score) {
        return new TotalScore(score);
    }

    public TotalScore sumStrike(final NextAddingUpScores nextAddingUpScores) {
        return new TotalScore(score + nextAddingUpScores.sumAddingUpStrikeCase());
    }

    public TotalScore sumSpare(final NextAddingUpScores nextAddingUpScores) {
        return new TotalScore(score + nextAddingUpScores.sumAddingUpSpareCase());
    }

    public static TotalScore calculateTotalScore(final List<Score> scores) {
        Score preScore = null;
        int result = 0;

        for (int i = 0; i < scores.size(); i++) {
            Score score = scores.get(i);

            FrameScoreResult frameScoreResult = FrameScoreResult.of(preScore, score);
            TotalScore totalScore = frameScoreResult.calculateTotalScore(score.toTotalScore(), NextAddingUpScores.newInstance(sliceScores(scores, i)));

            preScore = score;
            result += totalScore.score;
        }

        return TotalScore.of(result);
    }

    private static List<Score> sliceScores(final List<Score> scores, final int startIndex) {
        return scores.stream()
                .skip(startIndex + 1)
                .limit(scores.size() - startIndex + 1)
                .collect(Collectors.toList());
    }

    public static TotalScore calculateTotalScore(final FrameScore frameScore, final NextAddingUpScores nextAddingUpScores) {
        FrameScoreResult frameScoreResult = frameScore.getResult();
        return frameScoreResult.calculateTotalScore(TotalScore.of(frameScore.sum()), nextAddingUpScores);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TotalScore)) return false;
        TotalScore that = (TotalScore) o;
        return score == that.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(score);
    }

    public int getScore() {
        return score;
    }
}
