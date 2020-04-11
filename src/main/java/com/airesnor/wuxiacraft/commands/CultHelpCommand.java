package com.airesnor.wuxiacraft.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public class CultHelpCommand extends CommandBase {

    public CultHelpCommand() { super(); }

    @Override
    @Nonnull
    public String getName() { return "culthelp"; }

    @Override
    @ParametersAreNonnullByDefault
    @Nonnull
    public String getUsage(ICommandSender sender) { return "/cultinfo"; }

    @Override
    @Nonnull
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("chelp");
        return aliases;
    }

    @Override
    public int getRequiredPermissionLevel() { return 0; }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) { return true; }

    @Override
    @ParametersAreNonnullByDefault
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            if (sender instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) sender;
                //Creating book
                ItemStack tutorialBook = new ItemStack(Items.WRITTEN_BOOK);
                NBTTagCompound tagCompound = new NBTTagCompound();
                NBTTagList tagList = new NBTTagList();

                //Pages
                String page1 = "\n\n\n\n\n         Welcome" + "\n     WuxiaCraft mod";
                String page2 = "To begin your journey of cultivation you must slay wandering cultivators for an element scroll to gain the cultivate skill. " +
                        "\nYou can also make do with not gaining an element scroll and use only resources to cultivate. ";
                String page3 = "Beware: Do not aim too high and slay cultivators much stronger than you. " +
                        "\n\nBeware: The mobs at night are lurking, please stay safe. ";
                String page4 = "How to Alchemy:" +
                        "\nStep 1: \nRight-click with coal in your hand to use as fuel." +
                        "\nStep 2: \nUse featherfan to control the temperature." +
                        "\nStep 3: \nPlace items in at a specific temperature and order.";
                String page5 = "Step 4: \nOnce all the items are in let the cauldron cook at a specific temperature." +
                        "\n\nColors: ";

                //NBT data editing
                addBookPages("Anophobia & Aires", "WuxiaCraft Tutorial Book", tutorialBook, tagCompound, tagList,
                        page1, page2, page3, page4, page5);

                //Giving book to player
                player.inventory.addItemStackToInventory(tutorialBook);
            }
        } else throw new CommandException("Not used correctly!");
    }

    //Method to reduce repetition
    public void addBookPages(String author, String title, ItemStack item, NBTTagCompound nbtTagCompound, NBTTagList tagList, Object... args) {
        for (int i = 0; i < (args.length); i++) {
            String page = args[i].toString();
            tagList.appendTag(new NBTTagString(String.format("{\"text\":\"%s\"}", page)));
        }
        item.setTagInfo("pages", tagList);
        item.setTagInfo("author", new NBTTagString(author));
        item.setTagInfo("title", new NBTTagString(title));
    }
}
