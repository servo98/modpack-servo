package com.servo.delivery.block;

import com.servo.delivery.DeliveryRegistry;
import com.servo.delivery.data.ChapterDelivery;
import com.servo.delivery.data.DeliveryDataLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Menu for the Delivery Terminal GUI.
 * Syncs chapter progress data from server to client.
 * No item slots — items are delivered by right-clicking the block.
 */
public class DeliveryTerminalMenu extends AbstractContainerMenu {

    private final ContainerData data;
    private final DeliveryTerminalBlockEntity terminal;
    private final List<RequirementInfo> requirements;
    private final int chapter;
    private final String chapterName;

    // Data slot indices
    private static final int DATA_PROGRESS = 0;
    private static final int DATA_READY = 1;
    private static final int DATA_SLOT_COUNT = 2; // + 1 per requirement for delivered count

    /** Server constructor */
    public DeliveryTerminalMenu(int containerId, Inventory playerInv, DeliveryTerminalBlockEntity terminal) {
        super(DeliveryRegistry.TERMINAL_MENU.get(), containerId);
        this.terminal = terminal;
        this.chapter = terminal.getCurrentChapter();

        ChapterDelivery chapterData = DeliveryDataLoader.getChapter(chapter);
        this.chapterName = chapterData != null ? chapterData.getName() : "Unknown";
        this.requirements = new ArrayList<>();
        if (chapterData != null) {
            for (ChapterDelivery.Requirement req : chapterData.getRequirements()) {
                requirements.add(new RequirementInfo(req.id(), req.item().toString(), req.count(), req.description()));
            }
        }

        int totalSlots = DATA_SLOT_COUNT + requirements.size();
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                if (index == DATA_PROGRESS) return terminal.getProgressPercent();
                if (index == DATA_READY) {
                    ChapterDelivery ch = DeliveryDataLoader.getChapter(terminal.getCurrentChapter());
                    return (ch != null && ch.isComplete(terminal.getDeliveredItems())) ? 1 : 0;
                }
                int reqIndex = index - DATA_SLOT_COUNT;
                if (reqIndex >= 0 && reqIndex < requirements.size()) {
                    return terminal.getDeliveredItems().getOrDefault(requirements.get(reqIndex).id(), 0);
                }
                return 0;
            }

            @Override
            public void set(int index, int value) { }

            @Override
            public int getCount() { return totalSlots; }
        };
        addDataSlots(data);
    }

    /** Client constructor — reads from network buffer */
    public DeliveryTerminalMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
        super(DeliveryRegistry.TERMINAL_MENU.get(), containerId);
        this.terminal = null;
        this.chapter = buf.readVarInt();
        this.chapterName = buf.readUtf();

        int reqCount = buf.readVarInt();
        this.requirements = new ArrayList<>();
        for (int i = 0; i < reqCount; i++) {
            requirements.add(new RequirementInfo(
                    buf.readUtf(), buf.readUtf(), buf.readVarInt(), buf.readUtf()
            ));
        }

        int totalSlots = DATA_SLOT_COUNT + reqCount;
        this.data = new SimpleContainerData(totalSlots);
        addDataSlots(data);
    }

    /** Write initial data to network buffer (called on server when opening) */
    public void writeOpenData(FriendlyByteBuf buf) {
        buf.writeVarInt(chapter);
        buf.writeUtf(chapterName);
        buf.writeVarInt(requirements.size());
        for (RequirementInfo req : requirements) {
            buf.writeUtf(req.id());
            buf.writeUtf(req.itemId());
            buf.writeVarInt(req.count());
            buf.writeUtf(req.description());
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        // Button 0 = Launch
        if (id == 0 && terminal != null) {
            ChapterDelivery ch = DeliveryDataLoader.getChapter(terminal.getCurrentChapter());
            if (ch != null && ch.isComplete(terminal.getDeliveredItems())) {
                terminal.launchDelivery();
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        if (terminal == null) return true;
        return player.distanceToSqr(terminal.getBlockPos().getX() + 0.5,
                terminal.getBlockPos().getY() + 0.5,
                terminal.getBlockPos().getZ() + 0.5) <= 64.0;
    }

    // === Getters for Screen ===
    public int getChapter() { return chapter; }
    public String getChapterName() { return chapterName; }
    public List<RequirementInfo> getRequirements() { return requirements; }
    public int getProgress() { return data.get(DATA_PROGRESS); }
    public boolean isReady() { return data.get(DATA_READY) == 1; }
    public int getDelivered(int reqIndex) { return data.get(DATA_SLOT_COUNT + reqIndex); }

    public record RequirementInfo(String id, String itemId, int count, String description) {}
}
