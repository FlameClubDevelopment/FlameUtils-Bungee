package club.flame.flameutils.listeners;

import club.flame.flameutils.FlameUtils;
import club.flame.flameutils.utils.CC;
import club.flame.flameutils.utils.CreatorYML;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BungeeListener implements Listener {

    private final CreatorYML config = FlameUtils.getInstance().getConfigYML();

    @EventHandler(priority = 64)
    public void onPing(ProxyPingEvent event) {
        if (config.getConfiguration().getBoolean("WHITELIST.ENABLED")) {
            event.getResponse().setVersion(new ServerPing.Protocol("§4Whitelisted", 9999));
            event.setResponse(event.getResponse());
        }
    }

    @EventHandler
    public void onKick(ServerKickEvent event) {
        ProxiedPlayer p = event.getPlayer();
        Random random = new Random();
        int rand = random.nextInt(1) + 1;
        ServerInfo connect = BungeeCord.getInstance().getServerInfo("Hub-01" + rand);
        if (event.getKickedFrom() == connect) {
            p.disconnect(CC.translate("&cKicked from &f" + event.getKickedFrom().getName() + "&c: &f" + event.getKickReason()));
        }
        else {
            event.setCancelServer(connect);
            event.setCancelled(true);
            BungeeCord.getInstance().getScheduler().schedule(FlameUtils.getInstance(), () -> p.sendMessage(CC.translate("&cKicked from &f" + event.getKickedFrom().getName() + "&c: &f" + event.getKickReason())), 2L, TimeUnit.SECONDS);
        }
    }

    @EventHandler
    public void onJoin(LoginEvent event) {
        CreatorYML config = FlameUtils.getInstance().getConfigYML();
        if (config.getConfiguration().getBoolean("WHITELIST.ENABLED") && !FlameUtils.getInstance().getBungeeHandler().isWhitelisted(event.getConnection().getUniqueId().toString())) {
            event.setCancelled(true);
            event.setCancelReason(config.getConfiguration().getStringList("WHITELIST.KICK-MESSAGE")
                    .stream()
                    .map(CC::translate)
                    .collect(Collectors.joining("\n")));
        }
    }
}