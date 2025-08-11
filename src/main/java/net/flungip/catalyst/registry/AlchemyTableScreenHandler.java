package net.flungip.catalyst.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.flungip.catalyst.block.AlchemyTableBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

@Environment(EnvType.CLIENT)
public final class AlchemyTableScreenHandler extends ScreenHandler {
    private static final int INPUT_SLOT1    = 0;
    private static final int INPUT_SLOT2    = 1;
    private static final int BLAZE_POWDER   = 2;
    private static final int BREW_OUTPUT    = 3;
    private static final int GRIND_INPUT    = 4;
    private static final int GRIND_OUTPUT   = 5;
    private static final int BLOCK_INV_SIZE = 6;

    private static final int HOTBAR_SIZE      = 9;
    private static final int PLAYER_INV_START = BLOCK_INV_SIZE;
    private static final int PLAYER_INV_ROWS  = 3;
    private static final int SLOT_SPACING     = 18;

    private final Inventory      inventory;
    private final PropertyDelegate delegate;

    public AlchemyTableScreenHandler(int syncId,
                                     PlayerInventory playerInv,
                                     AlchemyTableBlockEntity blockEntity) {
        super(ModScreenHandlers.ALCHEMY_TABLE, syncId);
        this.inventory = blockEntity;
        this.delegate  = blockEntity.getDelegate();
        addProperties(delegate);

        // ── Block entity slots ─────────────────────────────────────────────
        // brew‐input 1: measured at (10,27)
        addSlot(new Slot(inventory, INPUT_SLOT1,  10, 27));
        // brew‐input 2: measured at (52,24)
        addSlot(new Slot(inventory, INPUT_SLOT2,  52, 24));
        // blaze powder: measured at (75,17)
        addSlot(new Slot(inventory, BLAZE_POWDER, 75, 17));
        // brew output: measured at (75,58)
        addSlot(new Slot(inventory, BREW_OUTPUT,  75, 58));

        // grind input: measured at (116,17)
        addSlot(new Slot(inventory, GRIND_INPUT,  116, 17));
        // grind output: measured at (116,58)
        addSlot(new Slot(inventory, GRIND_OUTPUT, 116, 58));

        // ── Player inventory (3×9) ────────────────────────────────────────
        // top‐left of grid at (8,152), slots spaced by 18px
        for (int row = 0; row < PLAYER_INV_ROWS; row++) {
            for (int col = 0; col < HOTBAR_SIZE; col++) {
                addSlot(new Slot(
                        playerInv,
                        col + row * HOTBAR_SIZE + HOTBAR_SIZE,
                        8  + col * SLOT_SPACING,
                        152 + row * SLOT_SPACING
                ));
            }
        }

        // ── Hotbar ─────────────────────────────────────────────────────────
        // one row below the 3×9 (152 + 3*18 = 206)
        for (int i = 0; i < HOTBAR_SIZE; i++) {
            addSlot(new Slot(
                    playerInv,
                    i,
                    8  + i * SLOT_SPACING,
                    152 + PLAYER_INV_ROWS * SLOT_SPACING
            ));
        }
    }

    public AlchemyTableScreenHandler(int syncId,
                                     PlayerInventory playerInv,
                                     PacketByteBuf buf) {
        this(syncId,
                playerInv,
                (AlchemyTableBlockEntity) playerInv.player
                        .getWorld()
                        .getBlockEntity(buf.readBlockPos()));
    }

    public float getBrewProgress() {
        int time = delegate.get(0), max = delegate.get(1);
        return max > 0 ? (float) time / max : 0f;
    }

    public float getGrindProgress() {
        int time = delegate.get(2), max = delegate.get(3);
        return max > 0 ? (float) time / max : 0f;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack copied = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasStack()) {
            ItemStack stack = slot.getStack();
            copied = stack.copy();
            if (index < BLOCK_INV_SIZE) {
                if (!insertItem(stack, PLAYER_INV_START, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!insertItem(stack, 0, BLOCK_INV_SIZE, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (stack.isEmpty()) slot.setStack(ItemStack.EMPTY);
            else slot.markDirty();
        }
        return copied;
    }
}
