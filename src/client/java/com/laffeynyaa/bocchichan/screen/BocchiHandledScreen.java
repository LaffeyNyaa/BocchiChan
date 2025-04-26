package com.laffeynyaa.bocchichan.screen;

import com.laffeynyaa.bocchichan.BocchiChan;
import com.laffeynyaa.bocchichan.entity.BocchiEntity;
import com.laffeynyaa.bocchichan.handler.BocchiScreenHandler;
import com.laffeynyaa.bocchichan.network.NetworkingConstants;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BocchiHandledScreen extends HandledScreen<BocchiScreenHandler> {
    private ButtonWidget buttonClaim;
    private ButtonWidget buttonSit;
    private ButtonWidget buttonEffect;
    private Text textButtonSit;
    private Text textButtonClaim;
    private Text textButtonEffect;
    private Text textTip;
    private BocchiEntity bocchiEntity;
    private int i;
    private int j;
    String currentLanguage;
    String ownerName;
    private static final Identifier TEXTURE = new Identifier("textures/gui/demo_background.png");

    public BocchiHandledScreen(BocchiScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, Text.literal(""));
    }

    @Override
    public void init() {
        if (bocchiEntity == null) {
            bocchiEntity = (BocchiEntity) client.targetedEntity;
        }

        currentLanguage = client.getLanguageManager().getLanguage();
        ownerName = bocchiEntity.getOwner() == null ? null : bocchiEntity.getOwner().getEntityName();
        i = (width - backgroundWidth) / 2;
        j = (height - backgroundHeight) / 2;

        if (ownerName != null) {
            textTip = Text.literal("This one's owner is " + ownerName);
        } else {
            textTip = Text.literal("This one has no owner.");
        }

        if (bocchiEntity.isSitting()) {
            textButtonSit = Text.literal("Stand");
        } else {
            textButtonSit = Text.literal("Sit");
        }

        if (bocchiEntity.hasEffect) {
            textButtonEffect = Text.literal("Eff. Off");
        } else {
            textButtonEffect = Text.literal("Eff. On");
        }

        textButtonClaim = Text.literal("Claim");

        if (currentLanguage.equals("zh_cn")) {
            if (ownerName != null) {
                textTip = Text.literal("这个波奇酱已与" + ownerName + "绑定。");
            } else {
                textTip = Text.literal("这个波奇酱独来独往。");
            }

            if (bocchiEntity.isSitting()) {
                textButtonSit = Text.literal("站立");
            } else {
                textButtonSit = Text.literal("坐下");
            }

            if (bocchiEntity.hasEffect) {
                textButtonEffect = Text.literal("关闭效果");
            } else {
                textButtonEffect = Text.literal("开启效果");
            }

            textButtonClaim = Text.literal("绑定");
        }

        buttonClaim = ButtonWidget.builder(textButtonClaim, button -> {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(bocchiEntity.getId());
            ClientPlayNetworking.send(NetworkingConstants.BOCCHI_FOLLOW_PACKET_ID, buf);
            button.active = false;
            buttonSit.active = true;
            buttonEffect.active = true;
            textTip = Text.literal("This one's owner is " + client.player.getEntityName() + ".");
            if (currentLanguage.equals("zh_cn")) {
                textTip = Text.literal("这个波奇酱已与" + client.player.getEntityName() + "绑定。");
            }
        })
                .dimensions(i + 25, j + 20, 50, 20)
                .build();

        buttonSit = ButtonWidget.builder(textButtonSit, button -> {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(bocchiEntity.getId());
            ClientPlayNetworking.send(NetworkingConstants.BOCCHI_SIT_PACKET_ID, buf);
            bocchiEntity.setSitting(!bocchiEntity.isSitting());
            if (bocchiEntity.isSitting()) {
                button.setMessage(Text.literal("Stand"));
            } else {
                button.setMessage(Text.literal("Sit"));
            }
            if (currentLanguage.equals("zh_cn")) {
                if (bocchiEntity.isSitting()) {
                    button.setMessage(Text.literal("站立"));
                } else {
                    button.setMessage(Text.literal("坐下"));
                }
            }
        })
                .dimensions(i + backgroundWidth - 75, j + 20, 50, 20)
                .build();

        buttonEffect = ButtonWidget.builder(textButtonEffect, button -> {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(bocchiEntity.getId());
            ClientPlayNetworking.send(NetworkingConstants.BOCCHI_EFFECT_PACKET_ID, buf);
            bocchiEntity.hasEffect = !bocchiEntity.hasEffect;

            if (bocchiEntity.hasEffect) {
                button.setMessage(Text.literal("Eff. Off"));
            } else {
                button.setMessage(Text.literal("Eff. On"));
            }

            if (currentLanguage.equals("zh_cn")) {
                if (bocchiEntity.hasEffect) {
                    button.setMessage(Text.literal("关闭效果"));
                } else {
                    button.setMessage(Text.literal("开启效果"));
                    
                }
            }
        })
                .dimensions(i + 25, j + 50, 50, 20)
                .build();

        if (ownerName != null) {
            buttonClaim.active = false;
        }

        if (ownerName != client.player.getEntityName()) {
            buttonSit.active = false;
            buttonEffect.active = false;
        }

        addDrawableChild(buttonClaim);
        addDrawableChild(buttonSit);
        addDrawableChild(buttonEffect);
    }

    @Override
    public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(TEXTURE, i, j, 0, 0.0F, 0.0F, backgroundWidth, backgroundHeight, 180, 256);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        if (textTip != null) {
            context.drawCenteredTextWithShadow(textRenderer, textTip, width / 2, j - 20, 0xffffff);
        }
    }

    @Override
    public void drawForeground(DrawContext context, int mouseX, int mouseY) {

    }

    public static void register() {
        HandledScreens.register(BocchiChan.BOCCHI_SCREEN_HANDLER, BocchiHandledScreen::new);
    }
}
