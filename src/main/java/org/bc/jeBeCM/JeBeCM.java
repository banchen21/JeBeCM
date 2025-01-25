package org.bc.jeBeCM;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JeBeCM extends JavaPlugin implements Listener {

    public HashMap<Player, Map<Material, CM_Item>> playerMapMap;

    public Map<Player, Map<Material, CM_Item>> getPlayerMapMap() {
        return this.playerMapMap;
    }

    public void setPlayerMapMap(Player player, Map<Material, CM_Item> materialMap) {
        this.playerMapMap.put(player, materialMap);
    }

    @Override
    public void onEnable() {
        getLogger().info("JeBeCM is enabled!");
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getDataFolder().mkdirs();
            getServer().getPluginManager().registerEvents(new CommmandCM(getDataFolder().getPath()), this);
            init_data();
            this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
                commands.registrar().register(CommmandCM.createCommand("cm", this, this.getDataFolder().getPath()), "钟表菜单");
            });
            getServer().getPluginManager().registerEvents(new CommmandListener(this), this);
        } else {
            this.getLogger().warning("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void init_data() {
        if (!new File(getDataFolder().getPath() + "/main.json").exists()) {
            Map<Material, CM_Item> cmItemHashMap = new HashMap<>();
            cmItemHashMap.put(Material.DIAMOND, new CM_Item("物品名", "点击设置时间为0", CMType.TELL, "hi"));
            cmItemHashMap.put(Material.CLOCK, new CM_Item("时间重置", "点击设置时间为0", CMType.COMMAND, "time set 0"));
            cmItemHashMap.put(Material.STONE, new CM_Item("我的家园", "点击设置时间为0", CMType.FORM, "home.json"));
            cmItemHashMap.put(Material.END_STONE, new CM_Item("管理员菜单", "点击设置时间为0", CMType.OP_FORM, "op_menu.json"));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(cmItemHashMap);
            try {
                FileWriter fileWriter = new FileWriter(getDataFolder().getPath() + "/main.json");
                fileWriter.append(json);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                this.getLogger().info("写入失败");
            }
        }
        playerMapMap = new HashMap<>();
    }

    @Override
    public void onDisable() {
    }
}
