package dev.liamhbest.musicbot.settings;

import com.jagrosh.jdautilities.command.GuildSettingsManager;
import dev.liamhbest.musicbot.utils.OtherUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import net.dv8tion.jda.api.entities.Guild;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

public class SettingsManager implements GuildSettingsManager {

    private final static double SKIP_RATIO = .55;
    private final HashMap<Long, Settings> settings;

    public SettingsManager() {
        this.settings = new HashMap<>();

        try {
            JSONObject loadedSettings = new JSONObject(new String(Files.readAllBytes(OtherUtil.getPath("serversettings.json"))));
            loadedSettings.keySet().forEach((id) -> {
                JSONObject o = loadedSettings.getJSONObject(id);

                // Legacy version support: On versions 0.3.3 and older, the repeat mode was represented as a boolean.
                if (!o.has("loop_mode") && o.has("loop") && o.getBoolean("loop")) {
                    o.put("loop_mode", LoopMode.ENABLED);
                }

                settings.put(Long.parseLong(id), new Settings(this,
                        o.has("text_channel_id") ? o.getString("text_channel_id")            : null,
                        o.has("voice_channel_id")? o.getString("voice_channel_id")           : null,
                        o.has("dj_role_id")      ? o.getString("dj_role_id")                 : null,
                        o.has("volume")          ? o.getInt("volume")                        : 100,
                        o.has("default_playlist")? o.getString("default_playlist")           : null,
                        o.has("loop_mode")     ? o.getEnum(LoopMode.class, "loop_mode"): LoopMode.DISABLED,
                        o.has("prefix")          ? o.getString("prefix")                     : null,
                        o.has("skip_ratio")      ? o.getDouble("skip_ratio")                 : SKIP_RATIO,
                        o.has("now_playing_channel_id") ? o.getString("now_playing_channel_id") : null,
                        o.has("live_mode") && o.getBoolean("live_mode")));
            });

        } catch(IOException | JSONException e) {
            LoggerFactory.getLogger("Settings").warn("Failed to load server settings (this is normal if no settings have been set yet): "+e);
        }
    }

    /**
     * Gets non-null settings for a Guild
     *
     * @param guild the guild to get settings for
     * @return the existing settings, or new settings for that guild
     */
    @Override
    public Settings getSettings(Guild guild) {
        return getSettings(guild.getIdLong());
    }

    public Settings getSettings(long guildId) {
        return settings.computeIfAbsent(guildId, id -> createDefaultSettings());
    }

    private Settings createDefaultSettings() {
        return new Settings(this, 0, 0, 0, 100, null, LoopMode.DISABLED, null, SKIP_RATIO, "0", false);
    }

    protected void writeSettings() {
        JSONObject obj = new JSONObject();

        settings.keySet().stream().forEach(key -> {
            JSONObject o = new JSONObject();
            Settings s = settings.get(key);

            if (s.textId != 0) o.put("text_channel_id", Long.toString(s.textId));

            if (s.voiceId != 0) o.put("voice_channel_id", Long.toString(s.voiceId));

            if (s.roleId != 0) o.put("dj_role_id", Long.toString(s.roleId));

            if (s.getVolume() != 100) o.put("volume",s.getVolume());

            if (s.getDefaultPlaylist() != null) o.put("default_playlist", s.getDefaultPlaylist());

            if (s.getRepeatMode() != LoopMode.DISABLED) o.put("loop_mode", s.getRepeatMode());

            if (s.getPrefix() != null) o.put("prefix", s.getPrefix());

            if (s.getSkipRatio() != SKIP_RATIO) o.put("skip_ratio", s.getSkipRatio());

            if (s.nowPlayingChannelId != 0) o.put("now_playing_channel_id", Long.toString(s.nowPlayingChannelId));

            if (s.liveMode) o.put("live_mode", s.inLiveMode());
            obj.put(Long.toString(key), o);
        });

        try {
            Files.write(OtherUtil.getPath("serversettings.json"), obj.toString(4).getBytes());
        } catch (IOException ex){
            LoggerFactory.getLogger("Settings").warn("Failed to write to file: "+ex);
        }
    }
}
