package com.github.blackjak34.compute;

import com.github.blackjak34.compute.block.*;
import com.github.blackjak34.compute.entity.tile.*;
import com.github.blackjak34.compute.entity.tile.client.*;
import com.github.blackjak34.compute.item.ItemFloppy;
import com.github.blackjak34.compute.item.ItemScrewdriver;
import com.github.blackjak34.compute.item.ItemSystemFloppy;
import com.github.blackjak34.compute.packet.MessageActionPerformed;
import com.github.blackjak34.compute.packet.MessageChangeAddress;
import com.github.blackjak34.compute.packet.MessageKeyTyped;
import com.github.blackjak34.compute.packet.MessageUpdateDisplay;
import com.github.blackjak34.compute.packet.handler.HandlerActionPerformed;
import com.github.blackjak34.compute.packet.handler.HandlerChangeAddress;
import com.github.blackjak34.compute.packet.handler.HandlerKeyTyped;
import com.github.blackjak34.compute.packet.handler.HandlerUpdateDisplay;
import com.github.blackjak34.compute.proxy.CommonProxy;
import com.google.common.io.ByteStreams;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.io.*;

@Mod(modid = DoesNotCompute.MODID, name = DoesNotCompute.NAME, version = DoesNotCompute.VERSION)
public class DoesNotCompute {

    public static final String MODID = "doesnotcompute";

    public static final String NAME = "Does Not Compute";

    public static final String VERSION = "1.1.15";
    
    public static SimpleNetworkWrapper networkWrapper;
    
    public static BlockTerminal terminal;
    public static BlockCPU cpu;
    public static BlockDiskDrive diskDrive;
    public static BlockCableRibbon ribbonCable;
    public static BlockSID sid;
    
    public static ItemFloppy floppy;
    public static ItemSystemFloppy systemFloppy;
    public static ItemScrewdriver screwdriver;

    @Mod.Instance(value = DoesNotCompute.MODID)
    public static DoesNotCompute instance;

    @SidedProxy(clientSide="com.github.blackjak34.compute.proxy.client.ClientProxy",
    		serverSide="com.github.blackjak34.compute.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static final CreativeTabs tabDoesNotCompute = new CreativeTabs("tabDoesNotCompute") {

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return Item.getItemFromBlock(terminal);
        }

    };

    @SuppressWarnings("unused")
	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	terminal = new BlockTerminal();
        cpu = new BlockCPU();
        diskDrive = new BlockDiskDrive();
        ribbonCable = new BlockCableRibbon();
        sid = new BlockSID();
    	
    	GameRegistry.registerBlock(terminal, "blockTerminal");
        GameRegistry.registerBlock(cpu, "blockCPU");
        GameRegistry.registerBlock(diskDrive, "blockDiskDrive");
        GameRegistry.registerBlock(ribbonCable, "blockCableRibbon");
        GameRegistry.registerBlock(sid, "blockSID");
        GameRegistry.registerTileEntity(TileEntityTerminal.class, "tileEntityTerminal");
    	GameRegistry.registerTileEntity(TileEntityTerminalClient.class, "tileEntityTerminalClient");
        GameRegistry.registerTileEntity(TileEntityCPU.class, "tileEntityEmulator");
        GameRegistry.registerTileEntity(TileEntityCPUClient.class, "tileEntityCPUClient");
        GameRegistry.registerTileEntity(TileEntityDiskDrive.class, "tileEntityDiskDrive");
        GameRegistry.registerTileEntity(TileEntityDiskDriveClient.class, "tileEntityDiskDriveClient");
        GameRegistry.registerTileEntity(TileEntityCableRibbon.class, "tileEntityCableRibbon");
        GameRegistry.registerTileEntity(TileEntityCableRibbonClient.class, "tileEntityCableRibbonClient");
        GameRegistry.registerTileEntity(TileEntitySID.class, "tileEntitySID");
        GameRegistry.registerTileEntity(TileEntitySIDClient.class, "tileEntitySIDClient");

        ItemStack ribbonCableStack = new ItemStack(ribbonCable);

        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(terminal),
                "III",
                "GCI",
                "IRI",
                'I', "ingotIron",
                'G', new ItemStack(Blocks.glass),
                'C', "dyeGreen",
                'R', ribbonCableStack
            )
        );

        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(cpu),
                "III",
                "ICI",
                "IRI",
                'I', "ingotIron",
                'C', new ItemStack(Items.comparator),
                'R', ribbonCableStack
            )
        );

        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(diskDrive),
                "III",
                " JI",
                "IRI",
                'I', "ingotIron",
                'J', new ItemStack(Blocks.jukebox),
                'R', ribbonCableStack
            )
        );

        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ribbonCable, 8),
                " D ",
                "DSD",
                " D ",
                'D', "dustRedstone",
                'S', new ItemStack(Blocks.stone_slab)
            )
        );

        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(sid),
                "III",
                "NNN",
                "IRI",
                'I', "ingotIron",
                'N', new ItemStack(Blocks.noteblock),
                'R', ribbonCableStack
            )
        );
    	
    	floppy = new ItemFloppy();
        systemFloppy = new ItemSystemFloppy();
        screwdriver = new ItemScrewdriver();
    	
    	GameRegistry.registerItem(floppy, "itemFloppy");
        GameRegistry.registerItem(systemFloppy, "itemSystemFloppy");
        GameRegistry.registerItem(screwdriver, "itemScrewdriver");

        ItemStack floppyStack = new ItemStack(floppy);

        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                floppyStack,
                "BIB",
                "BMB",
                "BPB",
                'B', "dyeBlue",
                'I', "ingotIron",
                'M', "record",
                'P', new ItemStack(Items.paper)
            )
        );

        GameRegistry.addRecipe(
            new ShapelessOreRecipe(
                new ItemStack(systemFloppy),
                "dustRedstone",
                floppyStack
            )
        );

        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(screwdriver),
                "I  ",
                " S ",
                "  B",
                'I', "ingotIron",
                'S', "stickWood",
                'B', "dyeBlue"
            )
        );
    	
    	networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(DoesNotCompute.MODID);
    	networkWrapper.registerMessage(HandlerActionPerformed.class, MessageActionPerformed.class, 0, Side.SERVER);
        networkWrapper.registerMessage(HandlerKeyTyped.class, MessageKeyTyped.class, 1, Side.SERVER);
        networkWrapper.registerMessage(HandlerChangeAddress.class, MessageChangeAddress.class, 2, Side.SERVER);

        networkWrapper.registerMessage(HandlerUpdateDisplay.class, MessageUpdateDisplay.class, 3, Side.CLIENT);
    }

    @SuppressWarnings("unused")
	@Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerRenderers();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
    }

    @SuppressWarnings("unused")
	@Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {}

    public InputStream getResourceFromAssetsDirectory(String filePath) {
        return getClass().getResourceAsStream("/assets/doesnotcompute/" + filePath);
    }

    public static RandomAccessFile getFileFromWorldDirectory(World world, String filename, String mode) {
        File modDirectory = new File(world.getSaveHandler().getWorldDirectory(), "/doesnotcompute/");
        if(!modDirectory.exists() && !modDirectory.mkdir()) {
            System.err.println("Failed to generate a new folder at " + modDirectory.getAbsolutePath());
            return null;
        }

        return getRandomAccessFile(new File(modDirectory, filename), mode);
    }

    public static RandomAccessFile getRandomAccessFile(File file, String mode) {
        if(file == null) {
            return null;
        }
        try {
            if(!mode.equals("r") && file.createNewFile()) {
                System.out.println("Generated a new file at " + file.getAbsolutePath());
            }

            return new RandomAccessFile(file, mode);
        } catch(FileNotFoundException e) {
            System.err.println("A file could not be found at " + file.getAbsolutePath());
            return null;
        } catch(IOException e) {
            System.err.println("Failed to generate a new file at " + file.getAbsolutePath());
            return null;
        }
    }

    public static byte[] getStreamAsByteArray(InputStream stream, int maxLength) {
        if(stream == null) {
            return new byte[0];
        }

        try {
            byte[] entireFile = ByteStreams.toByteArray(stream);
            stream.close();
            if(entireFile.length > maxLength) {
                byte[] truncatedFile = new byte[maxLength];
                System.arraycopy(entireFile, 0, truncatedFile, 0, maxLength);
                return truncatedFile;
            }
            return entireFile;
        } catch(IOException e) {
            return new byte[0];
        }
    }
    
}
