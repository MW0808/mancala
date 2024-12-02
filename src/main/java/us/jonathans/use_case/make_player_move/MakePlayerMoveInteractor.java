package us.jonathans.use_case.make_player_move;

import us.jonathans.data_access.match.MatchDataAccessInterface;
import us.jonathans.entity.match.EngineMatch;
import us.jonathans.entity.rule.Game;
import us.jonathans.entity.rule.MoveResult;
import us.jonathans.interface_adapter.make_player_move.MakePlayerMovePresenter;
import us.jonathans.observable.publisher.MatchEndPublisher;

import java.util.logging.Logger;

public class MakePlayerMoveInteractor implements MakePlayerMoveInputBoundary {

    private final MatchDataAccessInterface matchDataAccessObject;
    private final MakePlayerMoveOutputBoundary makePlayerMovePresenter;

    public MakePlayerMoveInteractor(MatchDataAccessInterface matchDataAccessObject, MakePlayerMoveOutputBoundary makePlayerMovePresenter) {

        this.matchDataAccessObject = matchDataAccessObject;
        this.makePlayerMovePresenter = makePlayerMovePresenter;
    }

    @Override
    public void execute(MakePlayerMoveInputData inputData) {
        Logger.getLogger("player move use case").info("Making a player move");
        EngineMatch engineMatch = matchDataAccessObject.getCurrentMatch();
        Game game = engineMatch.getGame();
        Boolean success = game.makeMove(
                inputData.getMancalaHole()
        );
        makePlayerMovePresenter.presentUpdatedBoard(new MakePlayerMoveOutputData(
                game.getBoard(),
                success
        ));
        if (game.isGameOver()) {
            MatchEndPublisher.getInstance().publish(engineMatch);
        }
    }
}
