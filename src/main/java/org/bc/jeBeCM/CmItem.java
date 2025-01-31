package org.bc.jeBeCM;

public class CmItem {
    //    选项名
    String itemDisplayName;
    //    物品描述
    String itemDescription;
    //    类型
    CmType itemType;
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

    public CmType getItemType() {
        return itemType;
    }

    public void setItemType(CmType itemType) {
        this.itemType = itemType;
    }

    public String getItemCommand() {
        return itemCommand;
    }

    public void setItemCommand(String itemCommand) {
        this.itemCommand = itemCommand;
    }

    public CmItem(String itemDisplayName, String itemDescription, CmType itemType, String itemCommand) {
        this.itemDisplayName = itemDisplayName;
        this.itemDescription = itemDescription;
        this.itemType = itemType;
        this.itemCommand = itemCommand;
    }
}

