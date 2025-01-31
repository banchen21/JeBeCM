package org.bc.jeBeCM;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Map;

import static org.bc.jeBeCM.CmSerializerUtil.read_json_to_list;

public class CommmandListener implements Listener {
    private JeBeCM plugin;

    public CommmandListener(JeBeCM jeBeCM) {
        this.plugin = jeBeCM;
    }

    @EventHandler
    public void onPlayerRightClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (!(inventory.getHolder(false) instanceof CmInventory)) {
//            拦截取出物品
            ItemStack itemStack = event.getCurrentItem();
//            获取玩家
            Player player = (Player) event.getWhoClicked();
            if (itemStack != null) {
                Material material = itemStack.getType();
                CmItem cmItem = plugin.getPlayerMapMap().get(player).get(material);
                String commandText = PlaceholderAPI.setPlaceholders(player, cmItem.getItemCommand());
                String path = plugin.getDataFolder().getPath() + "/" + cmItem.getItemCommand();
                player.closeInventory();
                switch (cmItem.getItemType()) {
                    case COMMAND:
                        player.performCommand(commandText);
                        break;
                    case OP_COMMAND:
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandText);
                        break;
                    case TELL:
                        player.sendMessage(commandText);
                        break;
                    case OP_FORM:
                        if (player.isOp()) {
                            openinventory(player, path, cmItem);
                        } else {
                            player.closeInventory();
                            player.sendMessage(plugin.getLocalizedMessage("permission.no"));
                        }
                        break;
                    case FORM:
                        openinventory(player, path, cmItem);
                        break;
                    default:
                        player.closeInventory();
                        break;
                }
            }
            event.setCancelled(true);
        }
    }

    void openinventory(Player player, String path, CmItem cmItem) {
        player.closeInventory();
        Map<Material, CmItem> cm_itemMap = null;
        try {
            cm_itemMap = read_json_to_list(path);
        } catch (IOException e) {
            player.sendMessage(plugin.getLocalizedMessage("error.path")+path);
        }
        if (cm_itemMap != null) {
            CmInventory cmInventory = new CmInventory(plugin, player, path, cmItem.getItemDisplayName());
            plugin.setPlayerMapMap(player, cm_itemMap);
            player.openInventory(cmInventory.getInventory());
        } else {
            player.sendMessage(plugin.getLocalizedMessage("error.path")+path);
        }

    }
}
