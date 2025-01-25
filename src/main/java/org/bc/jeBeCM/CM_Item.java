package org.bc.jeBeCM;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public class CM_Item {
    //    选项名
    String itemDisplayName;
    //    物品描述
    String itemDescription;
    //    类型
    CMType itemType;
    //    命令
    String itemCommand;

    public String getItemDisplayName() {
        return itemDisplayName;
    }

    public void setItemDisplayName(String itemDisplayName) {
        this.itemDisplayName = itemDisplayName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public CMType getItemType() {
        return itemType;
    }

    public void setItemType(CMType itemType) {
        this.itemType = itemType;
    }

    public String getItemCommand() {
        return itemCommand;
    }

    public void setItemCommand(String itemCommand) {
        this.itemCommand = itemCommand;
    }

    public CM_Item(String itemDisplayName, String itemDescription, CMType itemType, String itemCommand) {
        this.itemDisplayName = itemDisplayName;
        this.itemDescription = itemDescription;
        this.itemType = itemType;
        this.itemCommand = itemCommand;
    }
}

