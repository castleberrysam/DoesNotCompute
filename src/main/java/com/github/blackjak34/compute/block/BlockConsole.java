package com.github.blackjak34.compute.block;

import com.github.blackjak34.compute.DoesNotCompute;
import com.github.blackjak34.compute.entity.tile.TileEntityConsole;
import com.github.blackjak34.compute.entity.tile.TileEntityEmulator;
import com.github.blackjak34.compute.gui.GuiConsole;
import com.github.blackjak34.compute.item.ItemFloppy;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockConsole extends Block implements ITileEntityProvider {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool RUNNING = PropertyBool.create("running");
	public static final PropertyBool DISK = PropertyBool.create("disk");

	public BlockConsole(Material material) {
		super(material);
		
		setCreativeTab(CreativeTabs.tabMisc);
		setUnlocalizedName("blockConsole");
		setHarvestLevel("pickaxe", 1);
		setDefaultState(blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH)
				.withProperty(RUNNING, false)
				.withProperty(DISK, false)
		);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, FACING, RUNNING, DISK);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		boolean isRunning = (Boolean) state.getValue(RUNNING);
		boolean hasDisk = (Boolean) state.getValue(DISK);

		return (isRunning ? 1 : 0) + (hasDisk ? 2 : 0);
	}

	@Override
	public IBlockState getStateFromMeta(int metadata) {
		IBlockState state = getDefaultState();

		switch(metadata) {
			case 1:
				return state.withProperty(RUNNING, true);
			case 2:
				return state.withProperty(DISK, true);
			case 3:
				return state.withProperty(RUNNING, true).withProperty(DISK, true);
			default:
				return state;
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos coords, IBlockState state, EntityPlayer player,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack itemInHand = player.getCurrentEquippedItem();
		if(itemInHand != null && itemInHand.getItem() instanceof ItemFloppy) {
			if(!world.isRemote) {
				TileEntityEmulator emulator = (TileEntityEmulator) world.getTileEntity(coords);

				if(emulator.insertFloppy(itemInHand.getTagCompound().getString("filename"))) {
					player.setCurrentItemOrArmor(0, null);
				}
			}
		} else {
			player.openGui(DoesNotCompute.instance, GuiConsole.GUIID, world, coords.getX(), coords.getY(), coords.getZ());
		}

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2) {
		if(world.isRemote) {
			return new TileEntityConsole();
		} else {
			return new TileEntityEmulator(world);
		}
	}

	@Override
	public void breakBlock(World world, BlockPos coords, IBlockState state) {
		if(!world.isRemote) {
			((TileEntityEmulator) world.getTileEntity(coords)).ejectFloppy();
		}
		world.removeTileEntity(coords);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos coords, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(coords, state.withProperty(FACING, EnumFacing.fromAngle(placer.getRotationYawHead())));
	}

}