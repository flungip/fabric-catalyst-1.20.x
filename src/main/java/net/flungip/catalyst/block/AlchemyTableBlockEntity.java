package net.flungip.catalyst.block;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.flungip.catalyst.registry.ModBlockEntities;
import net.flungip.catalyst.registry.AlchemyTableScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/**
 * BlockEntity for the Alchemy Table: manages inventory, tick behavior, and GUI synchronization.
 */
public class AlchemyTableBlockEntity extends BlockEntity
        implements ExtendedScreenHandlerFactory, SidedInventory {

    private static final int SLOT_BREW_IN1   = 0;
    private static final int SLOT_BREW_IN2   = 1;
    private static final int SLOT_FUEL       = 2;
    private static final int SLOT_BREW_OUT   = 3;
    private static final int SLOT_GRIND_IN   = 4;
    private static final int SLOT_GRIND_OUT  = 5;
    private static final int TOTAL_SLOTS     = 6;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(TOTAL_SLOTS, ItemStack.EMPTY);

    private int brewTime;
    private static final int BREW_TIME_TOTAL = 200;

    private int grindTime;
    private static final int GRIND_TIME_TOTAL = 100;

    private final PropertyDelegate delegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> brewTime;
                case 1 -> BREW_TIME_TOTAL;
                case 2 -> grindTime;
                case 3 -> GRIND_TIME_TOTAL;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) brewTime = value;
            if (index == 2) grindTime = value;
        }

        @Override
        public int size() {
            return 4;
        }
    };

    public AlchemyTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALCHEMY_TABLE, pos, state);
    }

    public PropertyDelegate getDelegate() {
        return delegate;
    }

    /**
     * Server-side tick: handles brewing and grinding progression.
     */
    public static void tick(World world, BlockPos pos, BlockState state, AlchemyTableBlockEntity be) {
        if (world.isClient) return;
        boolean dirty = false;

        // Brewing
        if (canBrew(be)) {
            if (++be.brewTime >= BREW_TIME_TOTAL) {
                doBrew(be);
                be.brewTime = 0;
                dirty = true;
            }
        } else if (be.brewTime > 0) {
            be.brewTime = 0;
            dirty = true;
        }

        // Grinding
        if (canGrind(be)) {
            if (++be.grindTime >= GRIND_TIME_TOTAL) {
                doGrind(be);
                be.grindTime = 0;
                dirty = true;
            }
        } else if (be.grindTime > 0) {
            be.grindTime = 0;
            dirty = true;
        }

        if (dirty) {
            be.markDirty();
            world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
        }
    }

    private static boolean canBrew(AlchemyTableBlockEntity be) {
        ItemStack in1  = be.inventory.get(SLOT_BREW_IN1);
        ItemStack in2  = be.inventory.get(SLOT_BREW_IN2);
        ItemStack fuel = be.inventory.get(SLOT_FUEL);
        if (in1.isEmpty() || in2.isEmpty() || fuel.getItem() != Items.BLAZE_POWDER) return false;
        ItemStack result = getBrewResult(in1, in2);
        if (result.isEmpty()) return false;
        ItemStack out = be.inventory.get(SLOT_BREW_OUT);
        return out.isEmpty()
                || (out.isOf(result.getItem()) && out.getCount() + result.getCount() <= out.getMaxCount());
    }

    private static void doBrew(AlchemyTableBlockEntity be) {
        ItemStack result = getBrewResult(be.inventory.get(SLOT_BREW_IN1), be.inventory.get(SLOT_BREW_IN2));
        be.inventory.get(SLOT_FUEL).decrement(1);
        be.inventory.get(SLOT_BREW_IN1).decrement(1);
        be.inventory.get(SLOT_BREW_IN2).decrement(1);
        ItemStack out = be.inventory.get(SLOT_BREW_OUT);
        if (out.isEmpty()) out = result.copy();
        else out.increment(result.getCount());
        be.inventory.set(SLOT_BREW_OUT, out);
    }

    private static boolean canGrind(AlchemyTableBlockEntity be) {
        ItemStack in = be.inventory.get(SLOT_GRIND_IN);
        if (in.isEmpty()) return false;
        ItemStack result = getGrindResult(in);
        if (result.isEmpty()) return false;
        ItemStack out = be.inventory.get(SLOT_GRIND_OUT);
        return out.isEmpty()
                || (out.isOf(result.getItem()) && out.getCount() + result.getCount() <= out.getMaxCount());
    }

    private static void doGrind(AlchemyTableBlockEntity be) {
        ItemStack result = getGrindResult(be.inventory.get(SLOT_GRIND_IN));
        be.inventory.get(SLOT_GRIND_IN).decrement(1);
        ItemStack out = be.inventory.get(SLOT_GRIND_OUT);
        if (out.isEmpty()) out = result.copy();
        else out.increment(result.getCount());
        be.inventory.set(SLOT_GRIND_OUT, out);
    }

    /**
     * Placeholder brewing recipes.
     */
    private static ItemStack getBrewResult(ItemStack a, ItemStack b) {
        // TODO: define custom recipes
        return ItemStack.EMPTY;
    }

    /**
     * Placeholder grinding recipes.
     */
    private static ItemStack getGrindResult(ItemStack in) {
        // TODO: define custom recipes
        return ItemStack.EMPTY;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        Inventories.readNbt(tag, inventory);
        brewTime  = tag.getInt("BrewTime");
        grindTime = tag.getInt("GrindTime");
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        Inventories.writeNbt(tag, inventory);
        tag.putInt("BrewTime", brewTime);
        tag.putInt("GrindTime", grindTime);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.catalyst.alchemy_table");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new AlchemyTableScreenHandler(syncId, inv, this);
    }

    /** ← Fabric hook: writes the block-pos so the client opens the right BE instance **/
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return side == Direction.DOWN
                ? new int[]{SLOT_BREW_OUT, SLOT_GRIND_OUT}
                : new int[]{SLOT_BREW_IN1, SLOT_BREW_IN2, SLOT_FUEL, SLOT_GRIND_IN};
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.UP && slot == SLOT_FUEL && stack.getItem() == Items.BLAZE_POWDER;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN && (slot == SLOT_BREW_OUT || slot == SLOT_GRIND_OUT);
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int count) {
        return Inventories.splitStack(inventory, slot, count);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return world != null
                && player.squaredDistanceTo(
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5
        ) <= 64;
    }
}
