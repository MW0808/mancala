package us.jonathans.entity.rule;

public enum MancalaSide {
    PLAYER1{
        @Override
        public MancalaSide opposite() {
            return PlAYER2;
        }

        @Override
        public MancalaHole getGoal() {
            return MancalaHole.G;
        }
    },

    PlAYER2{
        @Override
        public MancalaSide opposite() {
            return PLAYER1;
        }

        @Override
        public MancalaHole getGoal() {
            return MancalaHole.g;
        }
    };

    public abstract MancalaSide opposite();

    public abstract  MancalaHole getGoal();
}
