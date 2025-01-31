package org.bc.jeBeCM;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.bc.jeBeCM.CmSerializerUtil.read_json_to_list;

public class CmInventory implements InventoryHolder {
    private final Inventory inventory;

    public CmInventory(JeBeCM plugin, Player player, String path_file, String title) {
        Map<Material, CmItem> materialMap = null;
        try {
            materialMap = read_json_to_list(path_file);
        } catch (IOException e) {
            player.sendMessage(plugin.getLocalizedMessage("error.path")+path_file);
        }
        plugin.setPlayerMapMap(player,materialMap );
        Inventory inventory;
        if (materialMap.size() % 9 == 0) {
            inventory = plugin.getServer().createInventory(player, materialMap.size() / 9 * 9, title);
        } else {
            inventory = plugin.getServer().createInventory(player, materialMap.size() / 9 * 9 + 9, title);
        }
        for (Map.Entry<Material, CmItem> material : materialMap.entrySet()) {
            ItemStack itemStack = new ItemStack(material.getKey(), 1);
            ItemMeta meta = itemStack.getItemMeta();

            meta.setDisplayName(PlaceholderAPI.setPlaceholders(player, material.getValue().getItemDisplayName()));
            itemStack.setItemMeta(meta);

            List<String> lore = new ArrayList<>();

            String lore_text =PlaceholderAPI.setPlaceholders(player, material.getValue().getItemCommand()) ;
            lore.add(lore_text);
            itemStack.setLore(lore);

            inventory.addItem(itemStack);
        }

        this.inventory = inventory;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
