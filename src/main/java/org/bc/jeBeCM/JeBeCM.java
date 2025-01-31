package org.bc.jeBeCM;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class JeBeCM extends JavaPlugin implements Listener {
    public static final String LANG_MESSAGES = "lang.messages";
    public HashMap<Player, Map<Material, CmItem>> playerMapMap;
    private static Locale locale = Locale.CHINA;
    public Map<Player, Map<Material, CmItem>> getPlayerMapMap() {
        return this.playerMapMap;
    }

    public void setPlayerMapMap(Player player, Map<Material, CmItem> materialMap) {
        this.playerMapMap.put(player, materialMap);
    }

    @Override
    public void onEnable() {
        getDataFolder().mkdirs();
        saveResource("config.yml", false);
        saveResource("main.json", false);
        saveResource("lang/messages_zh_CN.properties", false);

        saveDefaultConfig();

        //        初始化翻译系统
        setupTranslations(this.getConfig().getString("lang"));

//        检查前置插件
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Bukkit.getPluginManager().registerEvents(this, this); //
        } else {
            getLogger().warning("PlaceholderAPI 没有被加载，插件将不会工作！");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        getServer().getPluginManager().registerEvents(new CommandCM(getDataFolder().getPath()), this);
        playerMapMap = new HashMap<>();
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(CommandCM.createCommand("cm", this, this.getDataFolder().getPath()), "钟表菜单");
        });
        getServer().getPluginManager().registerEvents(new CommmandListener(this), this);
    }

    private void setupTranslations(@Nullable String language) {
        TranslationRegistry registry = TranslationRegistry.create(Key.key("labor_value", "translations"));
        if (language != null) {
            switch (language) {
                case "en_US":
                    locale = Locale.US;
                    break;
                case "zh_CN":
                default:
                    locale = Locale.CHINA;
                    break;
            }
            ResourceBundle default_zhBundle = ResourceBundle.getBundle(LANG_MESSAGES, locale, UTF8ResourceBundleControl.get());
            registry.registerAll(locale, default_zhBundle, true);
        } else {
            locale = Locale.CHINA;
            ResourceBundle default_zhBundle = ResourceBundle.getBundle(LANG_MESSAGES, locale, UTF8ResourceBundleControl.get());
            registry.registerAll(locale, default_zhBundle, true);
        }
        // 将翻译注册到全局翻译器
        GlobalTranslator.translator().addSource(registry);
    }

    static String getLocalizedMessage(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle(LANG_MESSAGES, locale, UTF8ResourceBundleControl.get());
        return bundle.getString(key);
    }


    @Override
    public void onDisable() {
    }
}
