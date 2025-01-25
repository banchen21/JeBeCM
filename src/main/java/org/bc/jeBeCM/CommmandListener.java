package org.bc.jeBeCM;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

import static org.bc.jeBeCM.CmSerializerUtil.read_json_to_list;

public class CommmandListener implements Listener {
    private JeBeCM jeBeCM;

    public CommmandListener(JeBeCM jeBeCM) {
        this.jeBeCM = jeBeCM;
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
                CM_Item cmItem = jeBeCM.getPlayerMapMap().get(player).get(material);
                switch (cmItem.getItemType()) {
                    case COMMAND:
                        player.performCommand(cmItem.getItemCommand());

                        player.closeInventory();
                        break;
                    case OP_COMMAND:
                        String joinText = cmItem.itemCommand;
//                        joinText = PlaceholderAPI.setPlaceholders(player, joinText);
//                        以控制台的权限执行命令：
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), joinText);
                        break;
                    case TELL:
                        player.sendMessage(cmItem.getItemCommand());

                        player.closeInventory();
                        break;
                    case OP_FORM:
                        if (player.isOp()) {
                            String path = jeBeCM.getDataFolder().getPath() + "/" + cmItem.getItemCommand();
                            openinventory(player, path, cmItem);
                        } else {
                            player.closeInventory();
                            player.sendMessage("权限不足");
                        }
                        break;
                    case FORM:
                        player.closeInventory();
                        String path = jeBeCM.getDataFolder().getPath() + "/" + cmItem.getItemCommand();
                        openinventory(player, path, cmItem);
                        break;
                    default:
                        // 处理未知类型
                        player.closeInventory();
                        break;
                }
            }
            event.setCancelled(true);
        }
    }

    void openinventory(Player player, String path, CM_Item cmItem) {
        player.closeInventory();
        jeBeCM.getLogger().info(path);
        Map<Material, CM_Item> cm_itemMap = read_json_to_list(path);
        if (cm_itemMap != null){
            CmInventory cmInventory = new CmInventory(jeBeCM, player, path, cmItem.getItemDisplayName());
            jeBeCM.setPlayerMapMap(player, cm_itemMap);
            player.openInventory(cmInventory.getInventory());
        }else {
            player.sendMessage("错误文件路径："+path);
        }

    }
}
