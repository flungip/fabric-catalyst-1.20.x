package net.flungip.catalyst.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MortarItemScreen extends HandledScreen<MortarItemScreenHandler> {
    private static final Identifier CHEST_TEX = new Identifier("minecraft", "textures/gui/container/generic_54.png");

    private static final int SLOT_X = 80;
    private static final int SLOT_Y = 35;

    public MortarItemScreen(MortarItemScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
        this.titleX = 8;
        this.titleY = 7;
    }

    @Override
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
        final int x = (width - backgroundWidth) / 2;
        final int y = (height - backgroundHeight) / 2;

        int padX = 6;
        int padY = 4;
        int plateW = textRenderer.getWidth(this.title) + padX * 2;
        int plateH = 14;
        int plateX = x + this.titleX - padX;
        int plateY = y + this.titleY - padY;
        ctx.fill(plateX, plateY, plateX + plateW, plateY + plateH, 0x99000000);
        ctx.fill(plateX, plateY + plateH - 1, plateX + plateW, plateY + plateH, 0x33FFFFFF);

        final int u = 7, v = 17, w = 18, h = 18;

        int invLeft = x + 8;
        int invTop  = y + 84;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int sx = invLeft + col * 18 - 1;
                int sy = invTop  + row * 18 - 1;
                ctx.drawTexture(CHEST_TEX, sx, sy, u, v, w, h);
            }
        }

        int hotbarTop = y + 142;
        for (int col = 0; col < 9; col++) {
            int sx = invLeft + col * 18 - 1;
            int sy = hotbarTop - 1;
            ctx.drawTexture(CHEST_TEX, sx, sy, u, v, w, h);
        }

        ctx.drawTexture(CHEST_TEX, x + SLOT_X - 1, y + SLOT_Y - 1, u, v, w, h);
    }

    @Override
    protected void drawForeground(DrawContext ctx, int mouseX, int mouseY) {
        ctx.drawText(textRenderer, this.title, this.titleX, this.titleY, 0xE0E0E0, false);
        ctx.drawText(textRenderer, this.playerInventoryTitle, 8, this.backgroundHeight - 96 + 2, 0x404040, false);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx);
        super.render(ctx, mouseX, mouseY, delta);
        drawMouseoverTooltip(ctx, mouseX, mouseY);
    }
}
