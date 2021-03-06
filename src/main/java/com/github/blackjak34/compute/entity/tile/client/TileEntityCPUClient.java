package com.github.blackjak34.compute.entity.tile.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileEntityCPUClient extends TileEntityRedbus {

    private boolean running;

    public TileEntityCPUClient() {}

    public boolean isRunning() {
        return running;
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet) {
        running = packet.getNbtCompound().getBoolean("running");

        super.onDataPacket(networkManager, packet);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos coords, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

}
