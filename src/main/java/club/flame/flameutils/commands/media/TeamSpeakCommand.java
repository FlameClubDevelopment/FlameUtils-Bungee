package club.flame.flameutils.commands.media;

import club.flame.flameutils.FlameUtils;
import club.flame.flameutils.utils.CC;
import club.flame.flameutils.utils.CreatorYML;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TeamSpeakCommand extends Command {

    private final CreatorYML config = FlameUtils.getInstance().getConfigYML();

    public TeamSpeakCommand() {
        super("teamspeak", "", "ts3", "ts");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }
        config.getConfiguration().getStringList("TEAMSPEAK.TEXT").stream().map(CC::translate).forEach(sender::sendMessage);
    }
}