

# 介绍
这是一个用于互通端的菜单插件，本插件为保存文件够自动热加载。
支持PAPI变量
# 如何使用？
将插件文件放入plugins文件夹

# 示例参考：
```json
{
  "REDSTONE_BLOCK": {
    "itemDisplayName": "§6§l⚙ 管理员菜单",
    "itemDescription": "§7▸状态：§7未开放",
    "itemType": "OP_FORM",
    "itemCommand": "op_menu.json",
    "FormImage_type": "path",
    "FormImage_data": "textures/blocks/redstone_block.png"
  },
  "DIAMOND": {
    "itemDisplayName": "§6§l💱 兑换花币",
    "itemDescription": "§d1 钻石 ≈ 200 花币 §8| §b单向兑换",
    "itemType": "COMMAND",
    "itemCommand": "exchange 1",
    "FormImage_type": "path",
    "FormImage_data": "textures/items/diamond.png"
  },
  "GRASS_BLOCK": {
    "itemDisplayName": "§e§l✨ 星界地块",
    "itemDescription": "§7▸状态：§a正常运营",
    "itemType": "FORM",
    "itemCommand": "plot.json",
    "FormImage_type": "path",
    "FormImage_data": "textures/blocks/grass_block_snow.png"
  },
  "BOOK":{
    "itemDisplayName": "§b✦ §e指南手册 §b✦",
    "itemDescription": "§7▶ §f点击打开手册！§7❖",
    "itemType": "FORM",
    "itemCommand": "reeworld.json",
    "FormImage_type": "path",
    "FormImage_data": "textures/items/book_normal.png"
  },
  "BEACON": {
    "itemDisplayName": "§b前往星际大厅",
    "itemDescription": "点击回传到星际大厅",
    "itemType": "COMMAND",
    "itemCommand": "spawn",
    "FormImage_type": "path",
    "FormImage_data": "textures/blocks/beacon.png"
  }
}
```

# 使用说明

## DIAMOND、END_STONE、STONE、CLOCK...
可参考：https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html
   
为java端用户菜单子项物品显示做参考
## itemDisplayName
选项名

## itemDescription
说明文，je端表现为lore，在be端不显示
## itemType
```java
//    向当前使用菜单的玩家发送消息
    TELL,
//    玩家可打开菜单项
    FORM,
//    以玩家身份执行命令
    COMMAND,
//    仅管理员可打开菜单项
    OP_FORM,
//    以控制台权限执行命令
    OP_COMMAND,
```

## itemCommand
菜单子项文件、需要执行的命令、需要向玩家发送的消息内容



# 注意
由于papi版本不足，故暂不支持