package us.jonathans;

import us.jonathans.app.App;
import us.jonathans.app.AppBuilder;
import us.jonathans.data_access.user.InMemoryUserDataAccess;
import us.jonathans.entity.match.EngineMatch;
import us.jonathans.entity.user.User;
import us.jonathans.observable.publisher.MatchEndPublisher;

public class Main {
    public static void main(String[] args) {
        App app = new AppBuilder()
                .addStartGameUseCase()
                .addLeaderboardUseCase()
                .addPostLeaderboardUseCase()
                .addMakePlayerMoveUseCase()
                .addMakeComputerMoveUseCase()
                .addCancelMatchUseCase()
                .addNotifyUserUseCase()
                .build();
        app.run();
    }
}