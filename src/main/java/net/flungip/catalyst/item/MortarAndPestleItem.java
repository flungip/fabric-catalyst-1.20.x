package net.flungip.catalyst.item;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.flungip.catalyst.registry.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity; // <-- add this
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MortarAndPestleItem extends Item {
    public MortarAndPestleItem(Settings settings) { super(settings); }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (!world.isClient) {
            final Hand usedHand = hand;
            player.openHandledScreen(new ExtendedScreenHandlerFactory() {
                @Override
                public void writeScreenOpeningData(ServerPlayerEntity p, PacketByteBuf buf) { // <-- ServerPlayerEntity
                    buf.writeEnumConstant(usedHand);
                }

                @Override
                public Text getDisplayName() {
                    return Text.translatable("item.catalyst.mortar_and_pestle");
                }

                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity p) {
                    return ModScreenHandlers.createMortarItem(syncId, inv, usedHand);
                }
            });
        }

        return TypedActionResult.success(stack, world.isClient);
    }
}
