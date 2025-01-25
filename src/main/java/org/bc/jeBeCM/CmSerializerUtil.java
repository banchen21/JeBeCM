package org.bc.jeBeCM;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmSerializerUtil {


    public static Map<Material, CM_Item> read_json_to_list(String pathname) {
        try {
            File file = new File(pathname);
            if (file.exists()) {
//                    读取文件内容
                StringBuilder stringBuilder = new StringBuilder();

                FileReader fileReader = new FileReader(file);
                while (fileReader.ready()) {
                    stringBuilder.append((char) fileReader.read());
                }
                fileReader.close();

                Gson gson1 = new GsonBuilder()
                        .registerTypeAdapter(new TypeToken<Map<Material, CM_Item>>() {
                        }.getType(), new MaterialMapDeserializer())
                        .create();
                Map<Material, CM_Item> materialMap = gson1.fromJson(stringBuilder.toString(), new TypeToken<Map<Material, CM_Item>>() {
                }.getType());
                return materialMap;
            } else {
                return null;
            }
        } catch (Exception e) {

        }
        return null;
    }

    //    创建formui
    public static void create_form_ui(JeBeCM jeBeCM,Player player, String main_path,String title) {
        Map<Material, CM_Item> materialMap = read_json_to_list(main_path);
        SimpleForm.Builder form = SimpleForm.builder()
                .title(title);
        Map<String,CM_Item> cm_itemMap=new HashMap<>();
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
            CM_Item cmItem=cm_itemMap.get(text);
            switch (cmItem.getItemType())
            {
                case TELL:
                    player.sendMessage(cmItem.getItemCommand());
                    break;
                case COMMAND:
                    player.performCommand(cmItem.getItemCommand());
                    break;
                case FORM:
                    create_form_ui(jeBeCM,player,jeBeCM.getDataFolder().getPath()+"/"+cmItem.getItemCommand(),cmItem.getItemDisplayName());
                    break;
                case OP_FORM:
                    if (player.isOp())
                    {
                        create_form_ui(jeBeCM,player,jeBeCM.getDataFolder().getPath()+"/"+cmItem.getItemCommand(),cmItem.getItemDisplayName());
                    }else {
                        player.sendMessage("您没有权限");
                    }
                    break;
                case OP_COMMAND:
                    String joinText = cmItem.itemCommand;
//                    joinText = PlaceholderAPI.setPlaceholders(player, joinText);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), joinText);
                    break;
            }
        });
        floodgatePlayer.sendForm(form); // or #sendForm(formBuilder)
    }
}
