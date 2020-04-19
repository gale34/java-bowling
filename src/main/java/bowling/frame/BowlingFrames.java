package bowling.frame;

import bowling.Score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BowlingFrames {
    public static final int MAX_BOWLING_FRAME_SIZE = 10;

    private final List<BowlingFrame> frames;

    private BowlingFrames(final List<BowlingFrame> frames) {
        this.frames = new ArrayList<>(frames);
    }

    public static BowlingFrames newInstance() {
        BowlingFrame firstFrame = BowlingFrame.createFirstFrame();
        return new BowlingFrames(Collections.singletonList(firstFrame));
    }

    public void bowl(final int countOfPin) {
        BowlingFrame recentFrame = getRecentBowlingFrame();
        recentFrame.bowl(countOfPin);

        if (recentFrame.isOver() && frames.size() < MAX_BOWLING_FRAME_SIZE) {
            frames.add(recentFrame.addNextFrame(frames.size()));
        }
    }

    public boolean isAllFrameOver() {
        if (getRecentBowlingFrame().isOver()) {
            return frames.size() == MAX_BOWLING_FRAME_SIZE;
        }

        return false;
    }

    private BowlingFrame getRecentBowlingFrame() {
        return frames.get(frames.size() - 1);
    }

    public int size() {
        return frames.size();
    }

    public List<Score> getTotalScores() {
        List<Score> totalScores = new ArrayList<>();
        Score totalScore = Score.of(0);

        for (BowlingFrame bowlingFrame : frames) {
            Score subTotalScore = bowlingFrame.getTotalScore(totalScore);
            totalScore = subTotalScore;
            totalScores.add(subTotalScore);
        }

        return totalScores;
    }

    public BowlingFrame getFrame(final int index) {
        return frames.get(index);
    }
}
