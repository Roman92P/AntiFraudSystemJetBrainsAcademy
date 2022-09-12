package antifraud.app.model;

public enum FeedbackEnum {
    ALLOWED(200) {
        @Override
        public void increaseLimit(long valueFromTransaction) {
            double newLimit = this.getMaxLimit() * 0.8 + 0.2 * valueFromTransaction;
            int result = (int) Math.ceil(newLimit);
            this.setMaxLimit(result);
        }

        @Override
        public void decreaseLimit(long valueFromTransaction) {
            double newLimit = this.getMaxLimit() * 0.8 - 0.2 * valueFromTransaction;
            int result = (int) Math.ceil(newLimit);
            this.setMaxLimit(result);
        }
    },
    MANUAL_PROCESSING(1500) {
        @Override
        public void increaseLimit(long valueFromTransaction) {
            double newLimit = this.getMaxLimit() * 0.8 + 0.2 * valueFromTransaction;
            int result = (int) Math.ceil(newLimit);
            this.setMaxLimit(result);
        }

        @Override
        public void decreaseLimit(long valueFromTransaction) {
            double newLimit = this.getMaxLimit() * 0.8 - 0.2 * valueFromTransaction;
            int result = (int) Math.ceil(newLimit);
            this.setMaxLimit(result);
        }
    },
    PROHIBITED(1501) {
        @Override
        public void increaseLimit(long valueFromTransaction) {
            double newLimit = this.getMaxLimit() * 0.8 + 0.2 * valueFromTransaction;
            int result = (int) Math.ceil(newLimit);
            this.setMaxLimit(result);
        }

        @Override
        public void decreaseLimit(long valueFromTransaction) {
            double newLimit = this.getMaxLimit() * 0.8 - 0.2 * valueFromTransaction;
            int result = (int) Math.ceil(newLimit);
            this.setMaxLimit(result);
        }
    };

    private int maxLimit;

    FeedbackEnum(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    FeedbackEnum() {
    }


    public int getMaxLimit() {
        return this.maxLimit;
    }

    protected void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    public abstract void increaseLimit(long valueFromTransaction);
    public abstract void decreaseLimit(long valueFromTransaction);


}