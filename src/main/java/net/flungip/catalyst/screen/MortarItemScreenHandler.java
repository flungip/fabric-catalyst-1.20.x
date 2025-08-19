package net.flungip.catalyst.screen;

import net.flungip.catalyst.item.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;

public class MortarItemScreenHandler extends ScreenHandler {
    private final Inventory inv;
    public final Hand hand;

    public MortarItemScreenHandler(int syncId, PlayerInventory playerInv, Hand hand) {
        super(net.flungip.catalyst.registry.ModScreenHandlers.MORTAR_ITEM, syncId);
        this.hand = hand;
        this.inv = new ItemStackBackedInventory(playerInv.player, hand, 1);

        this.addSlot(new Slot(inv, 0, 80, 35) {
            @Override public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.BONE)
                        || stack.isOf(Items.CHORUS_FRUIT)
                        || stack.isOf(Items.NETHER_BRICK);
            }
        });

        for (int m = 0; m < 3; ++m)
            for (int l = 0; l < 9; ++l)
                this.addSlot(new Slot(playerInv, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
        for (int l = 0; l < 9; ++l)
            this.addSlot(new Slot(playerInv, l, 8 + l * 18, 142));
    }

    public MortarItemScreenHandler(int syncId, PlayerInventory playerInv, Hand hand, boolean clientDummy) {
        this(syncId, playerInv, hand);
    }

    @Override public boolean canUse(PlayerEntity player) { return true; }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasStack()) {
            ItemStack stack = slot.getStack();
            newStack = stack.copy();

            if (index == 0) {
                if (!this.insertItem(stack, 1, this.slots.size(), true)) return ItemStack.EMPTY;
            } else {
                if (!this.insertItem(stack, 0, 1, false)) return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) slot.setStack(ItemStack.EMPTY);
            else slot.markDirty();
        }
        return newStack;
    }

    static class ItemStackBackedInventory implements Inventory {
        private final PlayerEntity player;
        private final Hand hand;
        private final DefaultedList<ItemStack> items;
        private boolean converting = false;

        ItemStackBackedInventory(PlayerEntity player, Hand hand, int size) {
            this.player = player;
            this.hand = hand;
            this.items = DefaultedList.ofSize(size, ItemStack.EMPTY);
            load();
        }

        private ItemStack carrier() { return player.getStackInHand(hand); }

        private void load() {
            ItemStack c = carrier();
            NbtCompound nbt = c.getOrCreateNbt();
            if (nbt.contains("MortarInv")) {
                Inventories.readNbt(nbt.getCompound("MortarInv"), items);
            }
        }

        private void save() {
            ItemStack c = carrier();
            NbtCompound nbt = c.getOrCreateNbt();
            NbtCompound invTag = new NbtCompound();
            Inventories.writeNbt(invTag, items);
            nbt.put("MortarInv", invTag);
            c.setNbt(nbt);
        }

        private ItemStack convert(ItemStack in) {
            if (in.isEmpty()) return ItemStack.EMPTY;

            if (in.isOf(Items.BONE))          return new ItemStack(ModItems.BONE_DUST,   in.getCount());
            if (in.isOf(Items.CHORUS_FRUIT))  return new ItemStack(ModItems.CHORUS_POWDER, in.getCount());
            if (in.isOf(Items.NETHER_BRICK))  return new ItemStack(ModItems.NETHER_POWDER, in.getCount());
            return ItemStack.EMPTY;
        }

        @Override public void markDirty() {
            if (!player.getWorld().isClient && !converting) {
                ItemStack in = items.get(0);
                ItemStack out = convert(in);
                if (!out.isEmpty()) {
                    converting = true;
                    items.set(0, out);
                    // little grind sound
                    player.getWorld().playSound(
                            null, player.getBlockPos(),
                            SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.PLAYERS,
                            0.7f, 1.1f
                    );
                    converting = false;
                }
            }
            save();
        }

        @Override public int size() { return items.size(); }
        @Override public boolean isEmpty() { return items.get(0).isEmpty(); }
        @Override public ItemStack getStack(int slot) { return items.get(slot); }
        @Override public ItemStack removeStack(int slot, int amount) {
            ItemStack taken = items.get(slot).split(amount);
            if (!taken.isEmpty()) markDirty();
            return taken;
        }
        @Override public ItemStack removeStack(int slot) {
            ItemStack s = items.get(slot);
            items.set(slot, ItemStack.EMPTY);
            markDirty();
            return s;
        }
        @Override public void setStack(int slot, ItemStack stack) {
            items.set(slot, stack);
            if (!stack.isEmpty() && stack.getCount() > stack.getMaxCount()) stack.setCount(stack.getMaxCount());
            markDirty();
        }
        @Override public boolean canPlayerUse(PlayerEntity player) { return true; }
        @Override public void clear() { items.replaceAll(__ -> ItemStack.EMPTY); markDirty(); }
    }
}
