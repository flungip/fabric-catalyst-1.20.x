package net.flungip.catalyst.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.flungip.catalyst.Catalyst;
import net.flungip.catalyst.registry.AlchemyTableScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AlchemyTableScreen extends HandledScreen<AlchemyTableScreenHandler> {
    private static final Identifier TEXTURE =
            new Identifier(Catalyst.MOD_ID, "textures/gui/alchemy_table_ui.png");


    private static final int UI_WIDTH  = 176;
    private static final int UI_HEIGHT = 166;


    private static final int PROG_WIDTH  = 24;
    private static final int PROG_HEIGHT = 17;
    private static final int BREW_U = 176, BREW_V = 0;
    private static final int GRIND_U = 176, GRIND_V = PROG_HEIGHT;

    public AlchemyTableScreen(AlchemyTableScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title);


        this.backgroundWidth  = UI_WIDTH;
        this.backgroundHeight = UI_HEIGHT;


        this.playerInventoryTitleX = 8;                    // align with vanilla left margin
        this.playerInventoryTitleY = UI_HEIGHT - 94;       // 166 - 94 = 72
    }

    @Override
    protected void init() {
        super.init();

        this.x = (this.width  - this.backgroundWidth ) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;
    }

    @Override
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        ctx.drawTexture(
                TEXTURE,
                this.x, this.y,
                0, 0,
                UI_WIDTH, UI_HEIGHT
        );


        int brewPx = (int)(handler.getBrewProgress() * PROG_WIDTH);
        ctx.drawTexture(
                TEXTURE,
                this.x + 76, this.y + 34,
                BREW_U, BREW_V,
                brewPx, PROG_HEIGHT
        );


        int grindPx = (int)(handler.getGrindProgress() * PROG_WIDTH);
        ctx.drawTexture(
                TEXTURE,
                this.x + 116, this.y + 34,
                GRIND_U, GRIND_V,
                grindPx, PROG_HEIGHT
        );
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx);
        super.render(ctx, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(ctx, mouseX, mouseY);
    }
}
