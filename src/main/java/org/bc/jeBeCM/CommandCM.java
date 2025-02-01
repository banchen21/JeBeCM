package org.bc.jeBeCM;

//import com.mojang.brigadier.Command;
//import com.mojang.brigadier.tree.LiteralCommandNode;
//import io.papermc.paper.command.brigadier.CommandSourceStack;
//import io.papermc.paper.command.brigadier.Commands;
//import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CommandCM implements Listener, CommandExecutor {

    String main_path;
    JeBeCM plugin;

    public CommandCM(JeBeCM jeBeCM, String path) {
        this.main_path = path;
        this.plugin = jeBeCM;
        jeBeCM.getCommand("cm").setExecutor(this);
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
//            检车玩家背包是否足有空间
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage("你的背包已满，无法添加钟表");
            }else {
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.CLOCK));
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        // 命令执行逻辑
        if (command.getName().equalsIgnoreCase("cm")&&strings[0].equals("help")) {
            // 你的代码逻辑
            Player player = sender.getServer().getPlayer(sender.getName());
            if (player == null) {
                sender.sendMessage("请在游戏内以玩家的身份进行操作");
                return false;
            }
            FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
            if (floodgatePlayer == null) {
//                        JE
                CmInventory cmInventory= new CmInventory(this.plugin,player,main_path+"/main.json", "主菜单");
                player.openInventory(cmInventory.getInventory());
            } else {
//                        BE
                CmSerializerUtil.create_form_ui(this.plugin,player,main_path+"/main.json","主菜单");
            }
            return true;
        }
        return false;
    }
}
