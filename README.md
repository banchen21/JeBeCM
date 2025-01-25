

# 介绍
这是一个用于互通端的菜单插件，本插件为保存文件够自动热加载。

# 如何使用？
将插件文件放入plugins文件夹

# 示例参考：
```json
{
  "DIAMOND": {
    "itemDisplayName": "物品名",
    "itemDescription": "点击设置时间为0",
    "itemType": "TELL",
    "itemCommand": "hi"
  },
  "END_STONE": {
    "itemDisplayName": "管理员菜单",
    "itemDescription": "点击设置时间为0",
    "itemType": "OP_FORM",
    "itemCommand": "op_menu.json"
  },
  "STONE": {
    "itemDisplayName": "我的家园",
    "itemDescription": "点击设置时间为0",
    "itemType": "FORM",
    "itemCommand": "main.json"
  },
  "CLOCK": {
    "itemDisplayName": "时间重置",
    "itemDescription": "点击设置时间为0",
    "itemType": "COMMAND",
    "itemCommand": "time set 0"
  },
  "CLOCK": {
    "itemDisplayName": "时间重置",
    "itemDescription": "点击设置时间为0",
    "itemType": "OP_COMMAND",
    "itemCommand": "time set 0"
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