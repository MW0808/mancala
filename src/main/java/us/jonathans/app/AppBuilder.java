package us.jonathans.app;

import us.jonathans.data_access.leaderboard.LeaderboardRepository;
import us.jonathans.data_access.match.InMemoryMatchDataAccess;
import us.jonathans.data_access.user.InMemoryUserDataAccess;
import us.jonathans.interface_adapter.cancel_match.CancelMatchController;
import us.jonathans.interface_adapter.cancel_match.CancelMatchPresenter;
import us.jonathans.interface_adapter.cancel_match.CancelMatchViewModel;
import us.jonathans.interface_adapter.get_leaderboard.GetLeaderboardController;
import us.jonathans.interface_adapter.get_leaderboard.GetLeaderboardPresenter;
import us.jonathans.interface_adapter.get_leaderboard.GetLeaderboardViewModel;
import us.jonathans.interface_adapter.make_player_move.MakePlayerMoveController;
import us.jonathans.interface_adapter.make_player_move.MakePlayerMovePresenter;
import us.jonathans.interface_adapter.make_player_move.MakePlayerMoveViewModel;
import us.jonathans.interface_adapter.notifyuser.NotifyUserController;
import us.jonathans.interface_adapter.notifyuser.NotifyUserPresenter;
import us.jonathans.interface_adapter.post_leaderboard.PostLeaderboardController;
import us.jonathans.interface_adapter.post_leaderboard.PostLeaderboardPresenter;
import us.jonathans.interface_adapter.post_leaderboard.PostLeaderboardViewModel;
import us.jonathans.interface_adapter.start_game.StartGameController;
import us.jonathans.interface_adapter.start_game.StartGamePresenter;
import us.jonathans.interface_adapter.start_game.StartGameViewModel;
import us.jonathans.interface_adapter.make_computer_move.MakeComputerMoveController;
import us.jonathans.interface_adapter.make_computer_move.MakeComputerMovePresenter;
import us.jonathans.interface_adapter.make_computer_move.MakeComputerMoveViewModel;
import us.jonathans.observable.publisher.MatchEndPublisher;
import us.jonathans.use_case.cancel_match.CancelMatchInteractor;
import us.jonathans.use_case.get_leaderboard.GetLeaderboardInteractor;
import us.jonathans.use_case.get_leaderboard.GetLeaderboardOutputBoundary;
import us.jonathans.use_case.make_player_move.MakePlayerMoveInteractor;
import us.jonathans.use_case.make_computer_move.MakeComputerMoveInteractor;
import us.jonathans.use_case.notify_user.NotifyUserInteractor;
import us.jonathans.use_case.start_game.StartGameInteractor;
import us.jonathans.view.TwilioNotificationService;
import java.util.UUID;
import us.jonathans.use_case.post_leaderboard.PostLeaderboardInteractor;
import us.jonathans.use_case.start_game.StartGameInteractor;
import us.jonathans.view.PostLeaderboardView;

public class AppBuilder {
    private StartGameViewModel startGameViewModel;
    private StartGameController startGameController;
    private GetLeaderboardViewModel getLeaderboardViewModel;
    private GetLeaderboardController getLeaderboardController;
    private MakePlayerMoveViewModel makePlayerMoveViewModel;
    private MakePlayerMoveController makePlayerMoveController;
    private MakeComputerMoveViewModel makeComputerMoveViewModel;
    private MakeComputerMoveController makeComputerMoveController;
    private CancelMatchViewModel cancelMatchViewModel;
    private CancelMatchController cancelMatchController;
    private NotifyUserController notifyUserController;
    private PostLeaderboardViewModel postLeaderboardViewModel;
    private PostLeaderboardController postLeaderboardController;

    public AppBuilder addStartGameUseCase() {
        startGameViewModel = new StartGameViewModel();
        startGameController = new StartGameController(
                    new StartGameInteractor(
                            InMemoryMatchDataAccess.getInstance(),
                            InMemoryUserDataAccess.getInstance(),
                            new StartGamePresenter(startGameViewModel)
                    )
            );
            return this;
    }

    public AppBuilder addCancelMatchUseCase() {
        cancelMatchViewModel = new CancelMatchViewModel();
        cancelMatchController = new CancelMatchController(
                new CancelMatchInteractor(
                        InMemoryMatchDataAccess.getInstance(),
                        new CancelMatchPresenter(cancelMatchViewModel)
                )
        );
        return this;
    }

    public AppBuilder addLeaderboardUseCase() {
        getLeaderboardViewModel = new GetLeaderboardViewModel();
        getLeaderboardController = new GetLeaderboardController(
                new GetLeaderboardInteractor(
                        new LeaderboardRepository(),
                        new GetLeaderboardPresenter(getLeaderboardViewModel)
                )
        );
        return this;
    }

    public AppBuilder addPostLeaderboardUseCase(){
        postLeaderboardViewModel = new PostLeaderboardViewModel("postLeaderboard");
        PostLeaderboardInteractor postLeaderboardInteractor = new PostLeaderboardInteractor(
                new LeaderboardRepository(),
                new PostLeaderboardPresenter(postLeaderboardViewModel)
        );
        postLeaderboardController = new PostLeaderboardController(postLeaderboardInteractor);
        MatchEndPublisher.getInstance().subscribe(postLeaderboardInteractor);
        return this;
    }

    public AppBuilder addMakePlayerMoveUseCase() {
        makePlayerMoveViewModel = new MakePlayerMoveViewModel();
        makePlayerMoveController = new MakePlayerMoveController(
                new MakePlayerMoveInteractor(
                        InMemoryMatchDataAccess.getInstance(),
                        new MakePlayerMovePresenter(makePlayerMoveViewModel)
                )
        );
        return this;
    }
  
    public AppBuilder addMakeComputerMoveUseCase() {
        makeComputerMoveViewModel = new MakeComputerMoveViewModel();
        makeComputerMoveController = new MakeComputerMoveController(
                new MakeComputerMoveInteractor(
                        InMemoryMatchDataAccess.getInstance(),
                        new MakeComputerMovePresenter(makeComputerMoveViewModel)
                )
        );
        return this;
    }

    public AppBuilder addNotifyUserUseCase() {
        NotifyUserInteractor notifyUserInteractor = new NotifyUserInteractor(
                new LeaderboardRepository(),
                new NotifyUserPresenter(
                        new TwilioNotificationService(
                                System.getenv("ACCOUNT_SID"),
                                System.getenv("AUTH_TOKEN"),
                                System.getenv("TWILIO_NUMBER")
                        )
                )
        );
        notifyUserController = new NotifyUserController(notifyUserInteractor);
        MatchEndPublisher.getInstance().subscribe(notifyUserInteractor);
        return this;
    }

    public App build() {
        return new App(
                startGameController,
                startGameViewModel,
                getLeaderboardController,
                getLeaderboardViewModel,
                postLeaderboardController,
                postLeaderboardViewModel,
                makePlayerMoveViewModel,
                makePlayerMoveController,
                makeComputerMoveController,
                makeComputerMoveViewModel,
                cancelMatchController,
                cancelMatchViewModel
        );
    }
}
