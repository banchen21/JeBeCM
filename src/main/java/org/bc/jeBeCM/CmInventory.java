package org.bc.jeBeCM;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

import static org.bc.jeBeCM.CmSerializerUtil.read_json_to_list;

public class CmInventory implements InventoryHolder, Listener {

    // 原有字段
    private JeBeCM plugin;
    private @NotNull Inventory inventory;
    private LinkedHashMap<Material, CmItem> materialMap;
    // 新增字段
    private int currentPage;
    private int totalPages;
    private List<Material> materials;
    private String path_file;
    private String title;
    private int itemsPerPage; // 动态计算每页容量
    private int middleRows;   // 中间区域行数（1-4）

//    页面数组
    private static int[] PAGE_BUTTON_SLOTS;
    static HashMap<UUID, Map<Material, CmItem>> playerMapMap;

    public CmInventory(JeBeCM plugin) {
        this.plugin = plugin;
        this.playerMapMap = new HashMap<>();
    }

    public CmInventory(JeBeCM plugin, Player player, String path_file, String title) {
        this(plugin, player, path_file, title, 0); // 默认第一页
    }

    public CmInventory(JeBeCM plugin, Player player, String path_file, String title, int currentPage) {
        this.plugin = plugin;
        this.path_file = path_file;
        this.title = title;

        // 读取物品数据
        try {
            materialMap = read_json_to_list(path_file);
            playerMapMap.put(player.getUniqueId(), materialMap);
        } catch (IOException e) {
            player.sendMessage("§c配置文件错误: " + path_file);
            return;
        }

        materials = new ArrayList<>(materialMap.keySet());
        this.totalPages = calculateDynamicPages(materials.size());
        this.currentPage = Math.max(0, Math.min(currentPage, totalPages - 1));

        // 动态计算当前页的行数
        this.middleRows = calculateCurrentPageRows(materials.size(), currentPage);
        this.itemsPerPage = middleRows * 9;

        this.inventory = createDynamicInventory(player);
        // 创建动态大小的库存
    }
    // 根据总物品数量计算需要的中间行数（1-4行）
    private int calculateMiddleRows(int totalItems) {
        if (totalItems <= 9)  return 1;
        if (totalItems <= 18) return 2;
        if (totalItems <= 27) return 3;
        return 4; // 最多4行36格
    }

    // 构建带占位符替换的物品
    private ItemStack buildItem(Player player, Material mat, CmItem cmItem) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(PlaceholderAPI.setPlaceholders(player, cmItem.getItemDisplayName()));

        List<String> lore =new ArrayList<>();
        lore.add(PlaceholderAPI.setPlaceholders(player, cmItem.getItemDescription()));
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    // 动态计算总页数
    private int calculateDynamicPages(int totalItems) {
        int pages = 0;
        int remaining = totalItems;
        while (remaining > 0) {
            int rows = Math.min(4, (int) Math.ceil(remaining / 9.0));
            remaining -= rows * 9;
            pages++;
        }
        return pages;
    }

    // 计算指定页的行数
    private int calculateCurrentPageRows(int totalItems, int page) {
        int remaining = totalItems;
        int currentPage = 0;
        while (remaining > 0 && currentPage <= page) {
            int rows = Math.min(4, (int) Math.ceil(remaining / 9.0));
            if (currentPage == page) return rows;
            remaining -= rows * 9;
            currentPage++;
        }
        return 1; // 默认最少1行
    }

    // 创建动态大小的库存
    private Inventory createDynamicInventory(Player player) {
        int totalRows = 2 + middleRows; // 顶1 + 中间动态行 + 底1
        Inventory inv = Bukkit.createInventory(this, totalRows * 9, title);

        // 填充顶部玻璃
        ItemStack glass = createGlassPane();
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, glass);
        }

        // 计算当前页的起始索引
        int startIndex = 0;
        int tempPage = 0;
        int remaining = materials.size();
        while (tempPage < currentPage && remaining > 0) {
            int rows = Math.min(4, (int) Math.ceil(remaining / 9.0));
            startIndex += rows * 9;
            remaining -= rows * 9;
            tempPage++;
        }

        // 填充当前页物品
        int endIndex = Math.min(startIndex + (middleRows * 9), materials.size());
        int slot = 9; // 中间区域起始槽位
        for (int i = startIndex; i < endIndex; i++) {
            Material mat = materials.get(i);
            CmItem cmItem = materialMap.get(mat);
            inv.setItem(slot++, buildItem(player, mat, cmItem));
        }

        // 填充底部栏
        int bottomRowStart = (totalRows - 1) * 9;
        for (int i = 0; i < 9; i++) {
            inv.setItem(bottomRowStart + i, glass);
        }

        // 添加翻页按钮
        if (currentPage > 0) {
            ItemStack prev = buildNavigationButton("上一页", Material.ARROW);
            inv.setItem(bottomRowStart, prev); // 底部栏第一个槽位
        }

        if (currentPage < totalPages - 1) {
            ItemStack next = buildNavigationButton("下一页", Material.ARROW);
            inv.setItem(bottomRowStart + 8, next); // 底部栏最后一个槽位
        }

        // 页码显示在底部中间
        ItemStack pageInfo = buildPageInfo();
        inv.setItem(bottomRowStart + 4, pageInfo);

        return inv;
    }

    // 创建翻页按钮
    private ItemStack buildNavigationButton(String name, Material material) {
        ItemStack button = new ItemStack(material);
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(name);
        button.setItemMeta(meta);
        return button;
    }

    // 创建页码信息物品
    private ItemStack buildPageInfo() {
        ItemStack page = new ItemStack(Material.PAPER);
        ItemMeta meta = page.getItemMeta();
        meta.setDisplayName("§e第 " + (currentPage + 1) + " 页 / 共 " + totalPages + " 页");
        page.setItemMeta(meta);
        return page;
    }

    // 统一创建玻璃板
    private ItemStack createGlassPane() {
        ItemStack glass = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta meta = glass.getItemMeta();
        meta.setDisplayName(" ");
        glass.setItemMeta(meta);
        return glass;
    }
    // 修改点击事件处理
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof CmInventory)) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) return;

        CmInventory holder = (CmInventory) event.getClickedInventory().getHolder();

        // 处理翻页点击
        if (clicked.getType() == Material.ARROW) {
            int newPage = holder.currentPage;
            String displayName = clicked.getItemMeta().getDisplayName();

            if (displayName.equals("上一页") && holder.currentPage > 0) {
                newPage--;
            } else if (displayName.equals("下一页") && holder.currentPage < holder.totalPages - 1) {
                newPage++;
            }

            if (newPage != holder.currentPage) {
                CmInventory newInventory = new CmInventory(holder.plugin, player, holder.path_file, holder.title, newPage);
                player.openInventory(newInventory.getInventory());
            }
        } else {
            // 原有物品点击处理
            holder.click_item(clicked, player);
        }
    }
    void click_item(ItemStack itemStack, Player player) {
        Material material = itemStack.getType();
        CmItem cmItem = playerMapMap.get(player.getUniqueId()).get(material);
        if (cmItem == null) {
            return;
        }
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

    /**
     * 打开子菜单界面：
     * 关闭当前 Inventory，从指定路径读取新的物品数据，创建新的 CmInventory 后打开
     */
    void openinventory(Player player, String path, CmItem cmItem) {
        player.closeInventory();
        try {
            LinkedHashMap<Material, CmItem> subMap = read_json_to_list(path);
            if (subMap != null) {
                CmInventory subMenu = new CmInventory(plugin, player, path, cmItem.getItemDisplayName());
                player.openInventory(subMenu.getInventory());
            } else {
                player.sendMessage(plugin.getLocalizedMessage("error.format") + path);
            }
        } catch (IOException e) {
            player.sendMessage(plugin.getLocalizedMessage("error.path") + path);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
