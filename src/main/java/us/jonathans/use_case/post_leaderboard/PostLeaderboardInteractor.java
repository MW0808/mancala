package us.jonathans.use_case.post_leaderboard;

import us.jonathans.data_access.match.InMemoryMatchDataAccess;
import us.jonathans.data_access.user.InMemoryUserDataAccess;
import us.jonathans.entity.match.EngineMatch;
import us.jonathans.observable.subscriber.Subscriber;

import java.util.logging.Logger;

public class PostLeaderboardInteractor implements PostLeaderboardInputBoundary, Subscriber<EngineMatch> {
    PostLeaderboardRepositoryInterface postLeaderboardRepository;
    PostLeaderboardOutputBoundary postLeaderboardPresenter;

    public PostLeaderboardInteractor(PostLeaderboardRepositoryInterface postLeaderboardRepository,
                                     PostLeaderboardOutputBoundary postLeaderboardPresenter) {
        this.postLeaderboardRepository = postLeaderboardRepository;
        this.postLeaderboardPresenter = postLeaderboardPresenter;
    }

    @Override
    public void execute(PostLeaderboardInputData postLeaderboardInputData){
        Logger.getLogger("post_leaderboard").info("Posting the leaderboard");
        postLeaderboardRepository.postLeaderboard(postLeaderboardInputData.getUsername(),
                postLeaderboardInputData.getOpponent(),
                postLeaderboardInputData.getScore(),
                postLeaderboardInputData.getTimestamp());

        PostLeaderboardOutputData postLeaderboardOutputData = new PostLeaderboardOutputData(false);
        postLeaderboardPresenter.prepareSuccessView(postLeaderboardOutputData);
    }

    @Override
    public void onNext(EngineMatch engineMatch) {
        this.execute(
                new PostLeaderboardInputData(
                        InMemoryUserDataAccess.getInstance().getUser(engineMatch.getPlayerId()).getUsername(),
                        engineMatch.getEngineId(),
                        engineMatch.getGame().getBoard().getStones(engineMatch.getPlayerSide().getGoal())
                )
        );
    }
}
