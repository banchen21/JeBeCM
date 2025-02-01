package org.bc.jeBeCM;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
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

public class CommandCM implements Listener {

    String main_path;

    public CommandCM(JeBeCM jeBeCM, String path) {
        this.main_path = path;
        jeBeCM.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(CommandCM.createCommand("cm", jeBeCM, jeBeCM.getDataFolder().getPath()), "钟表菜单");
        });
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
                Bukkit.dispatchCommand(event.getPlayer(), "cm help");
            }
            } catch (NullPointerException ignored) {
        }
    }

//    监听玩家进入服务器
    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.getInventory().contains(Material.CLOCK)) {
            player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.CLOCK));
        }
    }
}
