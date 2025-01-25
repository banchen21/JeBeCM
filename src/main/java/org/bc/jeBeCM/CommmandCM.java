package org.bc.jeBeCM;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import java.util.Objects;

public class CommmandCM implements Listener {

    String main_path;

    public CommmandCM(String path) {
        this.main_path = path;
    }

    public static LiteralCommandNode<CommandSourceStack> createCommand(final String commandName,JeBeCM jeBeCM,final String main_path) {
        return Commands.literal(commandName)
                .then(Commands.literal("help").executes(ctx -> {
                    String name = ctx.getSource().getSender().getName();
                    final CommandSender sender = ctx.getSource().getSender();
                    Player player = sender.getServer().getPlayer(name);
                    if (player == null) {
                        sender.sendMessage("请在游戏内以玩家的身份进行操作");
                        return Command.SINGLE_SUCCESS;
                    }
                    FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
                    if (floodgatePlayer == null) {
//                        JE
                        CmInventory cmInventory= new CmInventory(jeBeCM,player,main_path+"/main.json", "主菜单");
                        player.openInventory(cmInventory.getInventory());
                    } else {
//                        BE
                        CmSerializerUtil.create_form_ui(jeBeCM,player,main_path+"/main.json","主菜单");
                    }
                    return Command.SINGLE_SUCCESS;
                })).build();
    }

    //    玩家右键钟表监听事件
    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        try {
            if (Objects.requireNonNull(event.getItem()).getType().equals(Material.CLOCK)&&event.getAction().isRightClick()) {
//            以玩家的名义执行命令: /cm help
                Bukkit.dispatchCommand(event.getPlayer(), "cm help");
            }
        } catch (NullPointerException ignored) {
        }
    }
}
