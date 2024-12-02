package us.jonathans.use_case.notify_user;
import us.jonathans.data_access.leaderboard.LeaderboardRepository;
import us.jonathans.data_access.user.InMemoryUserDataAccess;
import us.jonathans.entity.match.EngineMatch;
import us.jonathans.entity.user.User;
import us.jonathans.observable.subscriber.Subscriber;

import java.util.logging.Logger;

public class NotifyUserInteractor implements NotifyUserInputBoundary, Subscriber<EngineMatch> {
    private final LeaderboardRepository leaderboardRepository;
    private final NotifyUserOutputBoundary notifyUserOutputBoundary;


    public NotifyUserInteractor(LeaderboardRepository leaderboardRepository, NotifyUserOutputBoundary notifyUserOutputBoundary) {
        this.leaderboardRepository = leaderboardRepository;
        this.notifyUserOutputBoundary = notifyUserOutputBoundary;
    }


    //Sends an SMS message to the user informing them of their position on the leaderboard
    @Override
    public void execute(NotifyUserInputData notifyUserInputData) {
        Logger.getLogger("notify_user").info("Sending SMS to the user");

        int rank = leaderboardRepository.getLeaderboard().getRank(notifyUserInputData.getUsername());

        String message;

        if (rank != -1) {
            if (rank == 3) {
                message = String.format("Congratulations on finishing your Jacala game %s! \n" +
                        "\nYou are currently ranked %drd on the leaderboard!", notifyUserInputData.getUsername(), rank);
            }
            else if (rank == 2) {
                message = String.format("Congratulations on finishing your Jacala game %s! \n" +
                        "\nYou are currently ranked %dnd on the leaderboard!", notifyUserInputData.getUsername(), rank);
            }
            else if (rank == 1) {
                message = String.format("Congratulations on finishing your Jacala game %s or shall I say Jacala Grandmaster! \n" +
                        "\nYou are currently ranked %dst on the leaderboard!", notifyUserInputData.getUsername(), rank);
            }
            else {
                message = String.format("Congratulations on finishing your Jacala game %s!\n" +
                        "\nYou are currently ranked %dth on the leaderboard!", notifyUserInputData.getUsername(), rank);
            }
        }
        else {
            message = String.format("Congratulations on finishing your Jacala game %s!\n" +
                    "\nConsider posting your results to the leaderboard to know your ranking next time!",
                    notifyUserInputData.getUsername());
        }

        notifyUserOutputBoundary.notifyUser(notifyUserInputData.getPhoneNumber(), message);
    }

    @Override
    public void onNext(EngineMatch engineMatch) {
        User user = InMemoryUserDataAccess.getInstance().getUser(engineMatch.getPlayerId());
        this.execute(
                new NotifyUserInputData(
                    user.getPhoneNumber(),
                    user.getUsername()
                )
        );
    }
}
