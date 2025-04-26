package com.laffeynyaa.bocchichan.state;

import java.util.HashMap;

import com.laffeynyaa.bocchichan.BocchiChan;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

public class StateSaverAndLoader extends PersistentState {
    public HashMap<String, Boolean> hashMap = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        hashMap.forEach((key, value) -> {
            nbt.putBoolean(key, value);
        });

        return nbt;
    }

    public static StateSaverAndLoader createFromNbt(NbtCompound tag) {
        StateSaverAndLoader state = new StateSaverAndLoader();
        tag.getKeys().forEach((key) -> {
            state.hashMap.put(key, tag.getBoolean(key));
        });
        return state;
    }

    public static StateSaverAndLoader getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        StateSaverAndLoader state = persistentStateManager.getOrCreate(StateSaverAndLoader::createFromNbt, StateSaverAndLoader::new, BocchiChan.MOD_ID);

        state.markDirty();
 
        return state;
    }


    
}
