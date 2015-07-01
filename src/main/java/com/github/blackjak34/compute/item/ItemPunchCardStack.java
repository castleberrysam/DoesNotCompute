package com.github.blackjak34.compute.item;

import com.github.blackjak34.compute.DoesNotCompute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemPunchCardStack extends Item {

    public ItemPunchCardStack() {
        setCreativeTab(DoesNotCompute.tabDoesNotCompute);
        setUnlocalizedName("itemPunchCardStack");
        setMaxDamage(0);
        setNoRepair();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        return itemStackIn;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
        tooltip.add("Contains " + getNumCardsInStack(stack) + " punch cards");
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getNumCardsInStack(stack) > 0;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return getNumCardsInStack(stack) / 64.0;
    }

    private int getNumCardsInStack(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if(tagCompound == null) {return 0;}

        int numCards = 0;
        for(int i=0;i<64;++i) {
            if(tagCompound.hasKey("card_" + 0)) {++numCards;}
        }

        return numCards;
    }

}
