# CallBells
 A Minecraft Paper plugin that allows players to opt in for chat warnings when a specific bell gets rung


## Feedback

What players see and hear when a registered bell is rung lives in `config.yml` under `feedback`, one entry per outcome:

- `rung` — each online owner, when someone rings their bell
- `success` — the ringer, when an owner who isn't AFK heard it
- `success-afk` — the ringer, when owners are online but all of them are AFK (CMI `%cmi_user_afk%`)
- `failure` — the ringer, when no owner is online

Each entry can have a `message` (`<bell>` and `<ringer>` placeholders, the ringer's character name), a `sound` heard only by that player, and an `animation` (a font flipbook on the subtitle, played with SneakyUtils `FontAnimations`). This replaces the MagicSpells spells `bell-rung`, `bell-ring-success`, `bell-ring-successAFK` and `bell-ring-failure`.
