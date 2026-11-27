package net.callbells.util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.sneakyutils.util.FontAnimations;
import com.sneakyutils.util.PlayerUtil;
import com.sneakyutils.util.TextUtility;

import net.callbells.CallBells;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

/**
 * What players see and hear when a registered bell is rung: one entry under {@code feedback} in config.yml per
 * outcome, each with an optional message, sound and font animation. Server thread only.
 */
public final class BellFeedback {

	/** An owner hears their bell rung by someone. */
	public static final String RUNG = "rung";
	/** The ringer: an owner is online and not AFK. */
	public static final String SUCCESS = "success";
	/** The ringer: owners are online, but all of them are AFK. */
	public static final String SUCCESS_AFK = "success-afk";
	/** The ringer: no owner is online. */
	public static final String FAILURE = "failure";

	private BellFeedback() {
	}

	/**
	 * Plays the {@code outcome} feedback to {@code player}. {@code bellName} fills {@code <bell>} in the message and
	 * {@code ringer}'s character name fills {@code <ringer>}; both go in as plain text.
	 */
	public static void play(Player player, String outcome, String bellName, Player ringer) {
		ConfigurationSection section = CallBells.getInstance().getConfig().getConfigurationSection("feedback." + outcome);
		if (section == null)
			return;

		String message = section.getString("message", "");
		if (!message.isEmpty()) {
			String ringerName = ringer == null ? "" : PlayerUtil.getCharacterName(ringer);
			player.sendMessage(MiniMessage.miniMessage()
					.deserialize(TextUtility.INSTANCE.replaceFormatCodes(message),
							Placeholder.unparsed("bell", displayName(bellName)),
							Placeholder.unparsed("ringer", ringerName))
					.decoration(TextDecoration.ITALIC, false));
		}

		ConfigurationSection sound = section.getConfigurationSection("sound");
		if (sound != null)
			playSound(player, sound);

		ConfigurationSection animation = section.getConfigurationSection("animation");
		if (animation != null && !animation.getString("font", "").isEmpty()) {
			FontAnimations.play(player, animation.getString("font"), animation.getInt("duration", 20),
					FontAnimations.Part.SUBTITLE, animation.getInt("start-frame", 0), animation.getInt("interval", 1),
					animation.getLong("delay", 0));
		}
	}

	/** Bell names are stored with underscores for spaces. */
	private static String displayName(String bellName) {
		return bellName == null ? "" : bellName.replace('_', ' ');
	}

	/** A sound only {@code player} hears, at their own position (MagicSpells' soundpersonal). */
	private static void playSound(Player player, ConfigurationSection config) {
		String name = config.getString("key", "");
		if (name.isEmpty())
			return;
		Key key;
		try {
			key = Key.key(name);
		} catch (RuntimeException e) {
			CallBells.getInstance().getLogger().warning("Invalid sound key in config: " + name);
			return;
		}
		Sound sound = Sound.sound(key, Sound.Source.MASTER, (float) config.getDouble("volume", 1.0),
				(float) config.getDouble("pitch", 1.0));
		long delay = config.getLong("delay", 0);
		if (delay <= 0) {
			player.playSound(sound);
			return;
		}
		Bukkit.getScheduler().runTaskLater(CallBells.getInstance(), () -> {
			if (player.isOnline())
				player.playSound(sound);
		}, delay);
	}

}
