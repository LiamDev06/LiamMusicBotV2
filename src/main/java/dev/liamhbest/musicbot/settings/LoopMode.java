package dev.liamhbest.musicbot.settings;

public enum LoopMode {

    DISABLED(null, "Disabled", "disabled"),
    ENABLED("\uD83D\uDD02", "Enabled", "enabled"); // 🔂

    private final String emoji;
    private final String userFriendlyName;
    private final String userFriendlyNameSentence;

    LoopMode(String emoji, String userFriendlyName, String userFriendlyNameSentence) {
        this.emoji = emoji;
        this.userFriendlyName = userFriendlyName;
        this.userFriendlyNameSentence = userFriendlyNameSentence;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getUserFriendlyName() {
        return userFriendlyName;
    }

    public String getUserFriendlyNameSentence() {
        return userFriendlyNameSentence;
    }
}
