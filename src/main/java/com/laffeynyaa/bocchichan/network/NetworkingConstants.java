package com.laffeynyaa.bocchichan.network;

import net.minecraft.util.Identifier;

public class NetworkingConstants {
    public static final Identifier BOCCHI_FOLLOW_PACKET_ID = Identifier.of("bocchichan", "bocchi_follow");
    public static final Identifier BOCCHI_SIT_PACKET_ID = Identifier.of("bocchichan", "bocchi_sit");
    public static final Identifier BOCCHI_IS_SITTING_PACKET_ID = Identifier.of("bocchichan", "bocchi_is_sitting");
    public static final Identifier BOCCHI_IS_NOT_SITTING_PACKET_ID = Identifier.of("bocchichan", "bocchi_is_not_sitting");
    public static final Identifier BOCCHI_EFFECT_PACKET_ID = Identifier.of("bocchichan", "bocchi_effect");
    public static final Identifier BOCCHI_HAS_EFFECT_PACKET_ID = Identifier.of("bocchichan", "bocchi_has_effect_sitting");
    public static final Identifier BOCCHI_HAS_NO_EFFECT_PACKET_ID = Identifier.of("bocchichan", "bocchi_has_no_effect_sitting");
}
