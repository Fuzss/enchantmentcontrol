package fuzs.enchantmentcontrol.impl.client.gui.screens;

import fuzs.enchantmentcontrol.impl.EnchantmentControl;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.FocusableTextWidget;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class InvalidCacheOnboardingScreen extends Screen {
    public static final Component TEXT_COMPONENT = Component.literal(
            "Found invalid cache for " + EnchantmentControl.MOD_NAME +
                    " mod!\n\nCustom enchantment settings will only apply after a game restart.");
    private static boolean hasSeenOnboardingScreen;

    private final PanoramaRenderer panorama = new PanoramaRenderer(TitleScreen.CUBE_MAP);
    private final LogoRenderer logoRenderer = new LogoRenderer(true);
    private final Screen lastScreen;
    @Nullable
    private FocusableTextWidget textWidget;

    public InvalidCacheOnboardingScreen(Screen lastScreen) {
        super(TEXT_COMPONENT);
        this.lastScreen = lastScreen;
        hasSeenOnboardingScreen = true;
    }

    public static boolean hasSeenOnboardingScreen() {
        return hasSeenOnboardingScreen;
    }

    @Override
    public void init() {
        FrameLayout frameLayout = new FrameLayout(this.width, this.height - 90);
        frameLayout.defaultChildLayoutSetting().alignVerticallyTop().padding(4);
        LinearLayout linearLayout = frameLayout.addChild(LinearLayout.vertical());
        linearLayout.defaultCellSetting().alignHorizontallyCenter().padding(2);
        this.textWidget = new FocusableTextWidget(this.width - 16, this.title, this.font);
        linearLayout.addChild(this.textWidget, (layoutSettings) -> {
            layoutSettings.paddingBottom(16);
        });
        frameLayout.addChild(Button.builder(CommonComponents.GUI_CONTINUE, (button) -> {
            this.onClose();
        }).build(), frameLayout.newChildLayoutSettings().alignVerticallyBottom().padding(8));
        frameLayout.arrangeElements();
        FrameLayout.alignInRectangle(frameLayout, 0, 90, this.width, this.height, 0.5F, 0.0F);
        frameLayout.visitWidgets(this::addRenderableWidget);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.lastScreen);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.logoRenderer.renderLogo(guiGraphics, this.width, 1.0F);
        if (this.textWidget != null) {
            this.textWidget.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.panorama.render(0.0F, 1.0F);
        guiGraphics.fill(0, 0, this.width, this.height, -1877995504);
    }
}
