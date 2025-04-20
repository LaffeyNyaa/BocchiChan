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

public class BocchiHandledScreen extends HandledScreen<BocchiScreenHandler> {
    public ButtonWidget buttonClaim;
    public ButtonWidget buttonSit;
    public Text textButtonSit;
    public Text textButtonClaim;
    public Text textTip;

    public BocchiHandledScreen(BocchiScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, Text.literal(""));
    }

    @Override
    public void init() {
        BocchiEntity bocchiEntity = (BocchiEntity) this.client.targetedEntity;
        String currentLanguage = this.client.getLanguageManager().getLanguage();
        String ownerName = bocchiEntity.getOwner() == null ? null : bocchiEntity.getOwner().getEntityName();

        if (currentLanguage.equals("zh_cn")) {
            if (ownerName != null) {
                textTip = Text.literal("这个波奇酱已与" + ownerName + "绑定。");
            } else {
                textTip = Text.literal("这个波奇酱独来独往。");
                
            }
        } else {
            if (ownerName != null) {
                textTip = Text.literal("This one's owner is " + ownerName);
            } else {
                textTip = Text.literal("This one has no owner.");
            }
        }

        if (currentLanguage.equals("zh_cn")) {
            textButtonSit = Text.literal("切换移动");
            textButtonClaim = Text.literal("绑定");
        } else {
            textButtonSit = Text.literal("Move / Stop");
            textButtonClaim = Text.literal("Claim");
        }

        buttonClaim = ButtonWidget.builder(textButtonClaim, button -> {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeUuid(bocchiEntity.getUuid());
            ClientPlayNetworking.send(NetworkingConstants.BOCCHI_FOLLOW_PACKET_ID, buf);
            buttonClaim.active = false;
            buttonSit.active = true;
            if (currentLanguage.equals("zh_cn")) {
                textTip = Text.literal("这个波奇酱已与" + this.client.player.getEntityName() + "绑定。");
            } else {
                textTip = Text.literal("This one's owner is " + this.client.player.getEntityName() + ".");
            }
        })
                .dimensions(this.width / 2 - 205, 20, 200, 20)
                .build();
        buttonSit = ButtonWidget.builder(textButtonSit, button -> {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeUuid(bocchiEntity.getUuid());
            ClientPlayNetworking.send(NetworkingConstants.BOCCHI_SIT_PACKET_ID, buf);
        })
                .dimensions(this.width / 2 + 5, 20, 200, 20)
                .build();
        if (ownerName != null) {
            buttonClaim.active = false;
        }

        if (ownerName != this.client.player.getEntityName()) {
            buttonSit.active = false;
        }

        addDrawableChild(buttonClaim);
        addDrawableChild(buttonSit);
    }

    @Override
    public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if (textTip != null) {
            context.drawCenteredTextWithShadow(textRenderer, textTip, width / 2, height / 2, 0xffffff);
        }
    }

    @Override
    public void drawForeground(DrawContext context, int mouseX, int mouseY) {

    }

    public static void register() {
        HandledScreens.register(BocchiChan.BOCCHI_SCREEN_HANDLER, BocchiHandledScreen::new);
    }
}
