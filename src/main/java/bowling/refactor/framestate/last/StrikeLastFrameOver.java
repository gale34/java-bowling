package bowling.refactor.framestate.last;

import bowling.refactor.FrameScore;
import bowling.refactor.LeftScoreCount;
import bowling.refactor.Score;
import bowling.refactor.framestate.State;

public class StrikeLastFrameOver implements State {

    private int secondPin;

    private StrikeLastFrameOver(final int countOfPin) {
        this.secondPin = countOfPin;
    }

    public static State newInstance(final int countOfPin) {
        return new StrikeLastFrameOver(countOfPin);
    }

    @Override
    public State Bowl(int countOfPin) {
        throw new IllegalStateException("No more bowl.");
    }

    @Override
    public FrameScore createFrameScore() {
        return FrameScore.newInstance(calculateScore(), LeftScoreCount.of(0));
    }

    private Score calculateScore() {
        return Score.of(10 + secondPin + secondPin);
    }
}
