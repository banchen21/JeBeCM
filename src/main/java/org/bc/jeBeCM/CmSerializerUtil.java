package org.bc.jeBeCM;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CmSerializerUtil {


//     读取json文件获取菜单列表
public static LinkedHashMap<Material, CmItem> read_json_to_list(String pathname) throws IOException {
    File file = new File(pathname);
    if (!file.exists()) return null;

    StringBuilder stringBuilder = new StringBuilder();
    FileReader fileReader = new FileReader(file);
    while (fileReader.ready()) {
        stringBuilder.append((char) fileReader.read());
    }
    fileReader.close();

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<LinkedHashMap<Material, CmItem>>() {}.getType(), new MaterialMapDeserializer())
            .create();
    Map<Material, CmItem> tempMap = gson.fromJson(stringBuilder.toString(), new TypeToken<Map<Material, CmItem>>() {}.getType());

    // **转换为 `LinkedHashMap` 以保持顺序**
    return new LinkedHashMap<>(tempMap);
}

    //    FloodgatePlayer 创建form菜单
    public static void create_form_ui(JeBeCM plugin,Player player, String main_path,String title) {
        LinkedHashMap<Material, CmItem> materialMap = null;
        try {
            materialMap = read_json_to_list(main_path);
        } catch (IOException e) {
            player.sendMessage(plugin.getLocalizedMessage("error.path")+main_path);
        }
        SimpleForm.Builder form = SimpleForm.builder().title(title);
        Map<String, CmItem> cm_itemMap=new HashMap<>();
        if (materialMap != null) {
            materialMap.forEach(
                    (material, cm_item) -> {
                        form.button(cm_item.itemDisplayName);
                        cm_itemMap.put(cm_item.itemDisplayName,cm_item);
                    }
            );
        }
        FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
        form.validResultHandler((player1, response) -> {
            String text=response.clickedButton().text();
            CmItem cmItem=cm_itemMap.get(text);
            String data_file_path = plugin.getDataFolder().getPath() + "/" + cmItem.getItemCommand();
            String commandText = PlaceholderAPI.setPlaceholders(player, cmItem.getItemCommand());
            switch (cmItem.getItemType())
            {
                case TELL:
                    player.sendMessage(commandText);
                    break;
                case COMMAND:
                    player.performCommand(commandText);
                    break;
                case FORM:
                    create_form_ui(plugin,player, data_file_path,cmItem.getItemDisplayName());
                    break;
                case OP_FORM:
                    if (player.isOp())
                    {
                        create_form_ui(plugin,player,data_file_path,cmItem.getItemDisplayName());
                    }else {
                        player.sendMessage(plugin.getLocalizedMessage("permission.no"));
                    }
                    break;
                case OP_COMMAND:
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandText);
                    break;
            }
        });
        floodgatePlayer.sendForm(form);
    }
}
