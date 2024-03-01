package derp.nbp.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.client.gui.screens.reporting.ReportPlayerScreen;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin{
	@Mutable
	@Final
	@Shadow
	public static final ResourceLocation MENU_BACKGROUND;
	static {
		MENU_BACKGROUND = new ResourceLocation("textures/gui/menu_background.png");
	}
	@Nullable
	@Shadow
	protected Minecraft minecraft;
	@Shadow public int width;
	@Shadow public int height;

	@Shadow
	protected void renderBlurredBackground(float f) {
		this.minecraft.gameRenderer.processBlurEffect(f);
		this.minecraft.getMainRenderTarget().bindWrite(false);
	}
	@Unique
	public void renderDirtBackground(GuiGraphics guiGraphics) {
		guiGraphics.setColor(0.25F, 0.25F, 0.25F, 1.0F);
		guiGraphics.blit(MENU_BACKGROUND, 0, 0, 0, 0.0F, 0.0F, this.width, this.height, 32, 32);
		guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
	}
	@Inject(method = "renderBackground(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At("HEAD"), cancellable = true)
	private void makeRenderBackgroundDirt(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo info) {
		Screen currentScreen = this.minecraft.screen;
		if (!(currentScreen instanceof OptionsScreen) &&
				!(currentScreen instanceof ShareToLanScreen) &&
				!(currentScreen instanceof SkinCustomizationScreen) &&
				!(currentScreen instanceof CreditsAndAttributionScreen) &&
				!(currentScreen instanceof AdvancementsScreen) &&
				!(currentScreen instanceof ReportPlayerScreen) &&
				!(currentScreen instanceof ProgressScreen) &&
				!(currentScreen instanceof PauseScreen) &&
				!(currentScreen instanceof ControlsScreen)) {
			this.renderDirtBackground(guiGraphics);
		} else if (this.minecraft.level != null) {
			this.renderBlurredBackground(f);
			info.cancel();
		}
	}
	@Inject(method = "renderPanorama", at = @At("HEAD"), cancellable = true)
	protected void renderPanorama(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
		ci.cancel();
	}
}
