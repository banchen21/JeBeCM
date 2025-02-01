package org.bc.jeBeCM;

import me.clip.placeholderapi.PlaceholderAPI;
import org.checkerframework.checker.nullness.qual.NonNull;

public class CmItem {
    //    选项名
    String itemDisplayName;
    //    物品描述
    String itemDescription;
    //    类型
    CmType itemType;
    //    命令
    String itemCommand;
//    url path
    String FormImage_type;

    String FormImage_data;

    public String getFormImage_data() {
        return FormImage_data;
    }

    public void setFormImage_data(String formImage_data) {
        FormImage_data = formImage_data;
    }

    public String getFormImage_type() {
        return FormImage_type;
    }

    public void setFormImage_type(String formImage_type) {
        FormImage_type = formImage_type;
    }

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

    public CmItem(String itemDisplayName, String itemDescription, CmType itemType, String itemCommand, String FormImage_type, String form_data) {
        this.itemDisplayName = itemDisplayName;
        this.itemDescription = itemDescription;
        this.itemType = itemType;
        this.itemCommand = itemCommand;
        this.FormImage_type = FormImage_type;
        this.FormImage_data = form_data;
    }
}

