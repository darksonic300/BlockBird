package com.github.darksonic300.blockbird.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.LevelSummary;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class MainMailScreen extends Screen {
    protected final Screen lastScreen;
    private Button deleteButton;
    private Button selectButton;
    private Button renameButton;
    private Button copyButton;
    protected EditBox searchBox;
    private List list;

    public MainMailScreen(Screen lastScreen) {
        super(Component.translatable("mainMail.title"));
        this.lastScreen = lastScreen;
    }

    protected void init() {
        this.searchBox = new EditBox(this.font, this.width / 2 - 100, 22, 200, 20, this.searchBox, Component.translatable("selectWorld.search"));
        //this.searchBox.setResponder((p_232980_) -> this.list.updateFilter(p_232980_));
        this.addWidget(this.searchBox);
        //this.list = (WorldSelectionList)this.addRenderableWidget(new WorldSelectionList(this, this.minecraft, this.width, this.height - 112, 48, 36, this.searchBox.getValue(), this.list));
        //this.selectButton = (Button)this.addRenderableWidget(Button.builder(LevelSummary.PLAY_WORLD, (p_232984_) -> this.list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::joinWorld)).bounds(this.width / 2 - 154, this.height - 52, 150, 20).build());
        this.addRenderableWidget(Button.builder(Component.translatable("selectWorld.create"), (p_280918_) -> CreateWorldScreen.openFresh(this.minecraft, this)).bounds(this.width / 2 + 4, this.height - 52, 150, 20).build());
        //this.renameButton = (Button)this.addRenderableWidget(Button.builder(Component.translatable("selectWorld.edit"), (p_101378_) -> this.list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::editWorld)).bounds(this.width / 2 - 154, this.height - 28, 72, 20).build());
        //this.deleteButton = (Button)this.addRenderableWidget(Button.builder(Component.translatable("selectWorld.delete"), (p_101376_) -> this.list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::deleteWorld)).bounds(this.width / 2 - 76, this.height - 28, 72, 20).build());
        //this.copyButton = (Button)this.addRenderableWidget(Button.builder(Component.translatable("selectWorld.recreate"), (p_101373_) -> this.list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::recreateWorld)).bounds(this.width / 2 + 4, this.height - 28, 72, 20).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, (p_280917_) -> this.minecraft.setScreen(this.lastScreen)).bounds(this.width / 2 + 82, this.height - 28, 72, 20).build());
        this.updateButtonStatus((LevelSummary)null);
    }

    protected void setInitialFocus() {
        this.setInitialFocus(this.searchBox);
    }

    public void onClose() {
        this.minecraft.setScreen(this.lastScreen);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.searchBox.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 8, 16777215);
    }

    public void updateButtonStatus(@Nullable LevelSummary levelSummary) {
        if (levelSummary == null) {
            this.selectButton.setMessage(LevelSummary.PLAY_WORLD);
            this.selectButton.active = false;
            this.renameButton.active = false;
            this.copyButton.active = false;
            this.deleteButton.active = false;
        } else {
            this.selectButton.setMessage(levelSummary.primaryActionMessage());
            this.selectButton.active = levelSummary.primaryActionActive();
            this.renameButton.active = levelSummary.canEdit();
            this.copyButton.active = levelSummary.canRecreate();
            this.deleteButton.active = levelSummary.canDelete();
        }

    }

    public void removed() {
        if (this.list != null) {
            //this.list.children().forEach(WorldSelectionList.Entry::close);
        }
    }
}
