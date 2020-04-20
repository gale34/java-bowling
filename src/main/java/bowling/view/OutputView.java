package bowling.view;

import bowling.dto.BowlingFrameConsoleResult;
import bowling.dto.BowlingGameBoardResult;
import bowling.dto.BowlingGameResult;
import bowling.dto.FrameState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OutputView {

    private static final String FRAME_DELIMITER = " | ";
    private static final String GAME_ROW_PREFIX = "| ";
    private static final String GAME_ROW_SUFFIX = " |";
    private static final String GAME_ROW_BLANK = " ";
    private static final String GAME_ROW_SCORE_HEAD = "   ";
    private static final String GAME_FIRST_ROW_FRAME_NUMBER_FORMAT = " %02d ";
    private static final String GAME_FIRST_ROW_NAME_FORMAT = "NAME";
    private static final String GAME_FRAME_SCORE_FORMAT = "%-4s";
    private static final String GAME_FRAME_SCORE_EMPTY_FORMAT = "    ";
    private static final String GAME_FIRST_ROW;
    private static final int MAX_BOWLING_FRAME_SIZE = 10;

    static {
        List<String> frames = IntStream.range(0, MAX_BOWLING_FRAME_SIZE)
                .mapToObj(number -> String.format(GAME_FIRST_ROW_FRAME_NUMBER_FORMAT, number + 1))
                .collect(Collectors.toList());

        GAME_FIRST_ROW = makeGameRow(GAME_FIRST_ROW_NAME_FORMAT, frames);
    }

    private static String makeGameRow(final String name, final List<String> frames) {
        List<String> frameWords = new ArrayList<>();
        frameWords.add(name);
        frameWords.addAll(frames);

        return GAME_ROW_PREFIX + String.join(FRAME_DELIMITER, frameWords) + GAME_ROW_SUFFIX;
    }

    public static void printBowlingGame(final BowlingGameResult bowlingGameResult) {
        List<String> scores = getScoreRow(bowlingGameResult, bowlingFrameConsoleResult -> extractFrameScoreResult(bowlingFrameConsoleResult.getFrameState()));
        List<String> totalScores = getScoreRow(bowlingGameResult, bowlingFrameConsoleResult -> String.valueOf(bowlingFrameConsoleResult.getTotalScore()));

        System.out.println(makeGameRow(GAME_ROW_BLANK + bowlingGameResult.getName(), makeFrameWordsWithBlank(scores)));
        System.out.println(makeGameRow(GAME_ROW_BLANK + GAME_ROW_SCORE_HEAD, makeFrameWordsWithBlank(totalScores)));
    }

    public static void printBowlingGameBoard(final BowlingGameBoardResult bowlingGameBoardResult) {
        System.out.println(GAME_FIRST_ROW);

        for(BowlingGameResult bowlingGameResult : bowlingGameBoardResult.getResult()) {
            printBowlingGame(bowlingGameResult);
        }

        System.out.println();
    }

    private static List<String> getScoreRow(final BowlingGameResult bowlingGameResult, Function<BowlingFrameConsoleResult, String> frameScoreFunction) {
        return bowlingGameResult.getBowlingFrameConsoleResults()
                .stream()
                .map(bowlingFrameConsoleResult -> String.format(GAME_FRAME_SCORE_FORMAT, frameScoreFunction.apply(bowlingFrameConsoleResult)))
                .collect(Collectors.toList());
    }

    private static List<String> makeFrameWordsWithBlank(final List<String> frameWords) {
        List<String> scoresWithBlank = new ArrayList<>(frameWords);
        IntStream.range(0, MAX_BOWLING_FRAME_SIZE - frameWords.size())
                .forEach(i -> scoresWithBlank.add(GAME_FRAME_SCORE_EMPTY_FORMAT));

        return scoresWithBlank;
    }


    private static String extractFrameScoreResult(final FrameState frameState) {
        FrameScoreConsoleResult frameScoreConsoleResult = FrameScoreConsoleResult.of(frameState.getState());
        return frameScoreConsoleResult.toString(frameState);
    }
}
